import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.activation.UnsupportedDataTypeException;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaBigNumber;
import org.pentaho.di.core.row.value.ValueMetaBinary;
import org.pentaho.di.core.row.value.ValueMetaBoolean;
import org.pentaho.di.core.row.value.ValueMetaDate;
import org.pentaho.di.core.row.value.ValueMetaInteger;
import org.pentaho.di.core.row.value.ValueMetaNumber;
import org.pentaho.di.core.row.value.ValueMetaString;
import org.pentaho.di.core.row.value.ValueMetaTimestamp;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.di.trans.steps.userdefinedjavaclass.TransformClassBase;
import org.pentaho.di.trans.steps.userdefinedjavaclass.UserDefinedJavaClass;
import org.pentaho.di.trans.steps.userdefinedjavaclass.UserDefinedJavaClassData;
import org.pentaho.di.trans.steps.userdefinedjavaclass.UserDefinedJavaClassMeta;

/**
 * Pentaho DIにて「ユーザ定義Java」ステップを試すサンプル
 *
 * [利用方法]
 * ・Pentaho DIのlibクラスから以下をクラスパスに追加
 *   kettele-core-xxxx.jar
 *   kettel-engine-xxxx.jar
 *
 * [参考]
 * http://type-exit.org/adventures-with-open-source-bi/2010/10/the-user-defined-java-class-step
 *
 * [処理内容]
 * ・ktrと同じディレクトリにある"data_text.csv"を読み込んで"data_text_2.csv"として出力
 *
 * [トライアルポイント]
 * ・processRow()メソッド内にクラスExtractorを追加して実行
 *    ⇒後で外部Jarにすれば良いがトライアルの間は暫定利用
 * ・Convertクラスでファイルの読み込みを行い、Kettle側のファイル入力は使用しない
 * ・Table outputで出力するフィールドを指定しないでも実行できるようにする
 *    ⇒data.outputRowMeta.addRowMeta()によってフィールドを動的に設定
 * ・JDBCの接続をパラメータで行う。
 *    ⇒ktrファイルを分割することで解決
 *
 * @author Eiichiro Umino
 *
 */
public class ConvertCSVLoop08_2_ExtractCSV extends TransformClassBase {

	public ConvertCSVLoop08_2_ExtractCSV(UserDefinedJavaClass parent, UserDefinedJavaClassMeta meta,
			UserDefinedJavaClassData data)
			throws KettleStepException {
		super(parent, meta, data);
	}

	///////////////////////////////////////////////////
	// ここからユーザ定義Javaステップへコピー
	// import文もpentaho以外のものはコピーする。
	///////////////////////////////////////////////////

	/* ktrのパラメータとして受け取る */
	static private String PARAM_IN_FILE = "IN_FILE";
	static private String PARAM_START_ROW = "START_ROW";
	static private String PARAM_DB_URL = "DB_URL";
	static private String PARAM_DB_SCHEMA = "DB_SCHEMA";
	static private String PARAM_DB_TABLE = "DB_TABLE";
	static private String PARAM_DB_USER = "DB_USER";
	static private String PARAM_DB_PASSWORD = "DB_PASSWORD";

	/**
	 * 変換を実行する内部クラス
	 */
	private class ExtractorCSV {
		/* ktr側で設定されているパラメータ */

		private BufferedReader _reader = null;

		/**
		 * ファイルを読み込み初期化
		 * @throws KettleException
		 */
		public void init() throws KettleException {
			try {
				/* パラメータで指定されたファイルを読み込む */
				String filePathStr = getVariable(PARAM_IN_FILE);
				logBasic("In FilePath=" + filePathStr);
				Path filePath = Paths.get(filePathStr, new String[0]);
				_reader = Files.newBufferedReader(filePath);

				/* パラメータで指定された最初の行まで読み込む */
				int startRow = Integer.parseInt(getVariable(PARAM_START_ROW));
				for (int i = 0; i < startRow; i++) {
					_reader.readLine();
				}

			} catch (IOException e) {
				throwAsKettleException(e);
			}
		}

		/**
		 * ファイルを1行読み込んで配列を返す.
		 *
		 * @return 読み込んだデータ。終端の場合はnul値とする。
		 */
		public String[] getRowData() throws KettleException {
			if (_reader == null) {
				return null;
			}
			String line = null;
			try {
				line = _reader.readLine();
			} catch (IOException e) {
				throwAsKettleException(e);
			}
			if (line == null) {
				return null;
			}

			String[] columnDatas = line.split(",");


			return columnDatas;
		}

		public String toUpper(String indata) {
			return indata.toUpperCase();
		}

		public void close() throws KettleException {
			if (_reader != null) {
				try {
					_reader.close();
				} catch (IOException e) {
					throwAsKettleException(e);
				}
			}
		}

		private void throwAsKettleException(Throwable e) throws KettleException {
			logError("エラーが発生", e);
			throw new KettleException(e);
		}
	}

	private ExtractorCSV _extractor = new ExtractorCSV();
	private TableColumn[] _tableColumns = null;
	private ValueMetaInterface[] _valueMetaInterfaces;

	// Kettleでエラーとなる
	// @Override
	/**
	 * 一時停止の際に呼ばれる。
	 */
	public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
		super.dispose(smi, sdi);
		logBasic("Extract: dispose()");
	}
	/**
	 * 停止させた際に呼ばれる
	 */
	public void stopRunning(StepMetaInterface stepMetaInterface, StepDataInterface stepDataInterface)
			throws KettleException {
		super.stopRunning(stepMetaInterface, stepDataInterface);
		logBasic("Extract: stopRunning()!");
		if (_extractor != null) {
			try {
				_extractor.close();
			} catch (KettleException e) {
			}
		}
	}



	/**
	 * @return 次の行へ継続する場合はtrue, 終了する場合はfalse
	 */
	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
		try {
			/* 最初の1回目(1行目)はfirst == trueとなる */
			if (first) {
				//first = false;
				logBasic("first");

				/* ファイルの読み込みと初期化 */
				_extractor.init();
			}

			/* 行データを取得。最後の行はnullとなったら終了処理 */
			String[] rowData = _extractor.getRowData();
			if (rowData == null) {
				logBasic("inputRow==null");
				setOutputDone();
				_extractor.close();
				return false;
			}

			if (first) {
				first = false;
				/*
				 * 出力する列データを設定
				 *
				 * ここで出力する列データ数はinsertする列数と同じでないとエラーとなる。
				 * そのため、1行目のデータを取得時に処理を行う。
				 *
				 */
				String url = getVariable(PARAM_DB_URL);
				String user = getVariable(PARAM_DB_USER);
				String pass = getVariable(PARAM_DB_PASSWORD);
				String schema = getVariable(PARAM_DB_SCHEMA);
				String table = getVariable(PARAM_DB_TABLE);
				String schemaTable = schema + "." + table;
				_tableColumns = getTableColumn(url, user, pass, schemaTable);
				_valueMetaInterfaces = getTableValueMetaInterfaces(_tableColumns);

//				/* rowDataの数だけtableColumnsを追加する */
//				RowMetaInterface newFields = new RowMeta();
//				for (int i = 0; i < rowData.length; i++) {
//					logBasic("tableColunns[" + i + "]=" + valueMetaInterfaces);
//					newFields.addValueMeta(valueMetaInterfaces[i]);
//				}
//				data.outputRowMeta.addRowMeta(newFields);
			}

			/* rowDataの数だけtableColumnsを追加する */
			RowMetaInterface newFields = new RowMeta();
			for (int i = 0; i < rowData.length; i++) {
				logBasic("tableColunns[" + i + "]=" + _valueMetaInterfaces[i]);
				newFields.addValueMeta(_valueMetaInterfaces[i]);
			}
			data.outputRowMeta.clear();
			data.outputRowMeta.addRowMeta(newFields);

			/* rowDataをテーブルの型に合わせたオブジェクトに変換する */
			Object[] typedRowData = new Object[rowData.length];
			for (int i = 0; i < rowData.length; i++) {
				logBasic("rowData[" + i + "]=" + rowData[i]);
				typedRowData[i] = strintToObject(rowData[i], _tableColumns[i].type);
			}

			/*
			 * 1行のデータを出力
			 */
			putRow(data.outputRowMeta, typedRowData);
			logBasic("putRow");
		} catch (Exception e) {
			logError("getTableColumns()にてエラー", e);
			throw new KettleException(e);
		}

		return true;
	}

	/**
	 * テーブルのカラムクラス
	 */
	private class TableColumn {
		public String name;
		public String type;

		public TableColumn(String name, String type) {
			this.name = name;
			this.type = type;
		}
	}

	/**
	 * 指定されたテーブルの属性項目を返します。
	 *
	 * JDBCでDBMSへ接続して型情報から生成しますので、多用しないようにする必要あり。
	 *
	 * @param url
	 * @param user
	 * @param pass
	 * @param table
	 * @return
	 */
	private TableColumn[] getTableColumn(String url, String user, String pass, String table)
			throws SQLException, IOException {
		TableColumn[] columns = null;
		// try()はKettleでエラーとなるためNG
		//		try (Connection connection = DriverManager.getConnection(url, user, pass);
		//				Statement stmt = connection.createStatement()) {

		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, user, pass);
			Statement stmt = connection.createStatement();
			ResultSet resultSet = stmt.executeQuery("select * from " + table);
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();
			columns = new TableColumn[columnCount];
			for (int i = 0; i < columnCount; i++) {
				String colName = metaData.getColumnName(i + 1);
				String typeName = metaData.getColumnClassName(i + 1);
				logBasic("TableColumn[" + i + "]=" + colName + ", type=" + typeName);
				columns[i] = new TableColumn(colName, typeName);
			}
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

		return columns;
	}

	private ValueMetaInterface[] getTableValueMetaInterfaces(TableColumn[] columns)
			throws SQLException, IOException {
		ValueMetaInterface[] vmis = new ValueMetaInterface[columns.length];
		for (int i = 0; i < vmis.length; i++) {
			vmis[i] = getValueMetaInterface(columns[i].name, columns[i].type);
		}

		return vmis;
	}

	/**
	 * 指定されたカラム名とJava型情報から{@link ValueMetaInterface}を返します。
	 *
	 * Java型との対応は{@link ValueMetaInterface}のJavaDocを参照。
	 *
	 * @param colName
	 * @param typeName
	 * @return
	 * @throws UnsupportedDataTypeException
	 */
	private ValueMetaInterface getValueMetaInterface(String colName, String typeName)
			throws UnsupportedDataTypeException {
		if (Double.class.getName().equals(typeName) ||
				Float.class.getName().equals(typeName)) {
			return new ValueMetaNumber(colName);
		} else if (String.class.getName().equals(typeName)) {
			return new ValueMetaString(colName);
		} else if (Date.class.getName().equals(typeName)) {
			return new ValueMetaDate(colName);
		} else if (Boolean.class.getName().equals(typeName)) {
			return new ValueMetaBoolean(colName);
		} else if (Long.class.getName().equals(typeName) || Integer.class.getName().equals(typeName)) {
			/* Long型もValueMetaIntegerとなる */
			return new ValueMetaInteger(colName);
		} else if (BigDecimal.class.getName().equals(typeName)) {
			return new ValueMetaBigNumber(colName);
		} else if (Blob.class.getName().equals(typeName) ||
				Clob.class.getName().equals(typeName)) {
			return new ValueMetaBinary(colName);
		} else if (java.sql.Timestamp.class.getName().equals(typeName)) {
			return new ValueMetaTimestamp(colName);
		}

		throw new UnsupportedDataTypeException("Column=" + colName + ", Type=" + typeName);
	}

	private Object strintToObject(String value, String typeName) throws UnsupportedDataTypeException {
		if (String.class.getName().equals(typeName)) {
			return value;
		} else if (Integer.class.getName().equals(typeName) || Long.class.getName().equals(typeName)) {
			/* PDIではIntegerもLong型となる */
			//return Integer.valueOf(value);
			return Long.valueOf(value);
		}
		//TODO: 他の型も処理する

		throw new UnsupportedDataTypeException("Value=" + value + ", Type=" + typeName);

	}

	//// ここまで
}

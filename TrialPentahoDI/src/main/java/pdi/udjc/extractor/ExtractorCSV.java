package pdi.udjc.extractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaString;
import org.pentaho.di.trans.steps.userdefinedjavaclass.TransformClassBase;

public class ExtractorCSV extends AbstractExtractor {

	private BufferedReader _reader = null;
	private TransformClassBase _parent;

	public ExtractorCSV(TransformClassBase parent) {
		super(parent);
	}

	/**
	 * ファイルを読み込み初期化
	 * @throws KettleException
	 */
	public void open() throws KettleException {
		try {
			/* パラメータで指定されたファイルを読み込む */
			String filePathStr = _parent.getVariable(PARAM_IN_FILE);
			_parent.logBasic("In FilePath=" + filePathStr);
			Path filePath = Paths.get(filePathStr, new String[0]);
			_reader = Files.newBufferedReader(filePath);

			/* パラメータで指定された最初の行まで読み込む */
			int startRow = Integer.parseInt(_parent.getVariable(PARAM_START_ROW));
			for (int i = 0; i < startRow; i++) {
				_reader.readLine();
			}

			/* テーブル名を取得 */
			_outTable = _parent.getVariable(PARAM_DB_TABLE);

		} catch (IOException e) {
			throwAsKettleException(e);
		}
	}


	/**
	 * インポート対象のカラム名情報を返却する。
	 *
	 * @return
	 */
	public ValueMetaInterface[] getRowColumns() {
		return new ValueMetaInterface[] {
				new ValueMetaString("col1"),
				new ValueMetaString("col2")
		};
	}

	/**
	 * ファイルを1行読み込んで配列を返す.
	 *
	 * @return 読み込んだデータ。終端の場合はnul値とする。
	 */
	public Object[] readRowData() throws KettleException {
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

		/*
		 * 必要に応じて変換処理
		 */
		// 大文字にする
		columnDatas[1] = toUpper(columnDatas[1]);

		/* IDが5以上の場合はテーブルをTB2とする */
		int id = Integer.parseInt(columnDatas[0]);
		if (id >= 5) {
			_outTable = "TB2";
		}

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


}

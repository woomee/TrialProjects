package pdi.udjc.extractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.trans.steps.userdefinedjavaclass.TransformClassBase;

import pdi.udjc.ExtractUDJC;

public class ExtractorCSV extends AbstractExtractor {

	private BufferedReader _reader = null;

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
			String filePathStr = _parent.getVariable(ExtractUDJC.PARAM_IN_FILE);
			_parent.logBasic("In FilePath=" + filePathStr);
			Path filePath = Paths.get(filePathStr, new String[0]);
			_reader = Files.newBufferedReader(filePath);

			/* パラメータで指定された最初の行まで読み込む */
			int startRow = Integer.parseInt(_parent.getVariable(ExtractUDJC.PARAM_START_ROW));
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
	public String[] readRowData() throws KettleException {
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

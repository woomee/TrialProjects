package trial.poi;

import java.io.InputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


/**
 * 先頭のセル(A0)を読み込むサンプル
 *
 * @author Eiichiro Umino
 *
 */
public class TrialPOI_01 {

	public static void main(String[] args) {

		InputStream in = null;
		Workbook wb = null;

		try {
			in = TrialPOI_01.class.getResourceAsStream("sample.xlsx");
			wb = WorkbookFactory.create(in);

			Sheet sheet0 = wb.getSheetAt(0);
			Row row0 = sheet0.getRow(0);
			Cell cell0 = row0.getCell(0);

			// Cellのタイプが数値だとエラーになる
			//String value00 =  cell0.getStringCellValue();
			//String value00 = cell0.toString();
			String value00 = getCellValue(cell0).toString();

			System.out.println("value00=" + value00);

		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

	static private Object getCellValue(Cell cell) {
		CellType type = cell.getCellType();

		Object value = null;
		if (CellType.NUMERIC.equals(type)) {
			value = cell.getNumericCellValue();
			return returnValue(value);
		}
		else if (CellType.STRING.equals(type)) {
			value = cell.getStringCellValue();
			return returnValue(value);
		}
		else if (CellType.FORMULA.equals(type)) {
			value = cell.getCellFormula();
			return returnValue(value);
		}

		// その他は空値とする
		return returnValue(null);
	}

	static private Object returnValue(Object value) {
		return value == null ? "" : value;
	}

}

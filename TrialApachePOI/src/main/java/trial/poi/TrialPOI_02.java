package trial.poi;

import java.io.InputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


/**
 * 全てのセルを読み込むサンプル
 *
 * @author Eiichiro Umino
 *
 */
public class TrialPOI_02 {

	public static void main(String[] args) {

		InputStream in = null;
		Workbook wb = null;

		try {
			in = TrialPOI_02.class.getResourceAsStream("sample.xlsx");
			wb = WorkbookFactory.create(in);

			int numSheets = wb.getNumberOfSheets();
			for (int sheetNum = 0; sheetNum < numSheets; sheetNum++) {
				Sheet sheet = wb.getSheetAt(sheetNum);
				String sheetName = sheet.getSheetName();
				System.out.println("SheetName=" + sheetName);

				int numRows = sheet.getLastRowNum();
				for (int rowNum = 0; rowNum <= numRows; rowNum++) {		// 行番号は"<="にする必要あり
					Row row = sheet.getRow(rowNum);
					int numCells = row.getLastCellNum();

					for (int cellNum = 0; cellNum < numCells; cellNum++) {
						Cell cell = row.getCell(cellNum);
						String value = getCellValue(cell).toString();
						if (cellNum > 0) {
							System.out.print(", ");
						}
						System.out.print(value);
					}
					System.out.println("");
				}
			}
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
			value = cell.getNumericCellValue();
			value += "[" + cell.getCellFormula() + "]";
			return returnValue(value);
		}

		// その他は空値とする
		return returnValue(null);
	}

	static private Object returnValue(Object value) {
		return value == null ? "" : value;
	}

}

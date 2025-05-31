package base;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataGenerators {

    /**
     * Reads test data from an Excel sheet and returns it as a 2D Object array.
     *
     * @param filePath   Absolute or relative path to the Excel file.
     * @param sheetName  Name of the sheet from which data should be read.
     * @return Object[][] containing the data (excluding header).
     * @throws IOException If file reading fails.
     */
    public static Object[][] getTestData(String filePath, String sheetName) throws IOException {
        try (FileInputStream file = new FileInputStream(filePath);
             XSSFWorkbook workbook = new XSSFWorkbook(file)) {

            XSSFSheet sheet = workbook.getSheet(sheetName);
            int totalRows = sheet.getPhysicalNumberOfRows();
            int totalCols = getMaxColumnCount(sheet);

            Object[][] data = new Object[totalRows - 1][totalCols];

            for (int i = 1; i < totalRows; i++) {
                XSSFRow row = sheet.getRow(i);
                if (row == null) continue;

                for (int j = 0; j < totalCols; j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    data[i - 1][j] = getCellValue(cell);
                }
            }

            return data;
        }
    }

    /**
     * Retrieves the maximum number of columns used across all rows.
     */
    private static int getMaxColumnCount(Sheet sheet) {
        int maxCols = 0;
        for (Row row : sheet) {
            if (row.getLastCellNum() > maxCols) {
                maxCols = row.getLastCellNum();
            }
        }
        return maxCols;
    }

    /**
     * Converts a cell value to its proper Java object.
     */
    private static Object getCellValue(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case BLANK, ERROR -> null;
            case BOOLEAN -> cell.getBooleanCellValue();
            case NUMERIC -> DateUtil.isCellDateFormatted(cell) ? cell.getDateCellValue() : cell.getNumericCellValue();
            case FORMULA -> cell.getCellFormula();
            case _NONE-> "";
        };
    }
}

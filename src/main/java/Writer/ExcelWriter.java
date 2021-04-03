package Writer;

import DataModel.People;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExcelWriter {
    private final String[] columns;
    private final List<People> peopleList;
    private final String sheetName;

    public ExcelWriter(String[] columns, List<People> peopleList, String sheetName) {
        this.columns = columns;
        this.peopleList = peopleList;
        this.sheetName = sheetName;
    }

    public void generateExcelSheet() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        CreationHelper helper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet(sheetName);

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.GREEN.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        int rowNum = 1;
        for (People p : peopleList) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(p.getId());
            row.createCell(1).setCellValue(p.getName());
            row.createCell(2).setCellValue(p.getBio());
            row.createCell(3).setCellValue(p.getLocation());
        }

        for (int i = 0; i < columns.length; i++)
            sheet.autoSizeColumn(i);

        LocalDateTime localDate = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
        String formattedDate = localDate.format(dateTimeFormatter);

        FileOutputStream fileOut = new FileOutputStream("src/Saved_Results/" + sheetName + formattedDate + ".xlsx");
        workbook.write(fileOut);
        fileOut.close();

        workbook.close();
    }
}
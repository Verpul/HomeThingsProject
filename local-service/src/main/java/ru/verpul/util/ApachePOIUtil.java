package ru.verpul.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public class ApachePOIUtil {

    public static XSSFSheet getNewXLSSheet(XSSFWorkbook workbook, String sheetName, List<String> headers, int columnWidth) {
        XSSFSheet sheet = workbook.createSheet(sheetName);

        for (int i = 0; i < headers.size(); i++) {
            sheet.setColumnWidth(i, columnWidth * 256);
        }

        return sheet;
    }

    public static CellStyle getHeaderStyle(XSSFWorkbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        setCellBorderStyle(headerStyle, BorderStyle.MEDIUM);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        return headerStyle;
    }

    public static CellStyle getDateCellStyle(XSSFWorkbook workbook) {
        CreationHelper creationHelper = workbook.getCreationHelper();
        short dateFormat = creationHelper.createDataFormat().getFormat("dd.mm.yyyy");
        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(dateFormat);
        setCellBorderStyle(dateStyle, BorderStyle.THIN);

        return dateStyle;
    }

    public static CellStyle getBorderedCellStyle(XSSFWorkbook workbook) {
        CellStyle borderStyle = workbook.createCellStyle();
        setCellBorderStyle(borderStyle, BorderStyle.THIN);

        return borderStyle;
    }

    public static Cell createCellAndApplyStyle(Row row, int cellNum, CellStyle cellStyle) {
        Cell cell = row.createCell(cellNum);
        cell.setCellStyle(cellStyle);
        return cell;
    }

    private static void setCellBorderStyle(CellStyle cellStyle, BorderStyle borderStyle) {
        cellStyle.setBorderBottom(borderStyle);
        cellStyle.setBorderTop(borderStyle);
        cellStyle.setBorderLeft(borderStyle);
        cellStyle.setBorderRight(borderStyle);
    }
}

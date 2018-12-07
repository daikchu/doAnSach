/*
package com.daicq.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.List;

public abstract class ExcelExportUtility<E extends Object> {
	protected SXSSFWorkbook wb;
	protected Sheet sh;
	protected static final String EMPTY_VALUE = " ";

	private void autoResizeColumns(int listSize) {
		for (int colIndex = 0; colIndex < listSize; colIndex++) {
			sh.autoSizeColumn(colIndex);
		}
	}

	protected CellStyle getHeaderStyle() {
		CellStyle style = wb.createCellStyle();
		style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		style.setAlignment(CellStyle.ALIGN_CENTER);
		return style;
	}

	protected CellStyle getNormalStyle() {
		CellStyle style = wb.createCellStyle();
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		style.setAlignment(CellStyle.ALIGN_CENTER);
		return style;
	}
	private void fillHeader(String[] columns) {
		wb = new SXSSFWorkbook(100); // keep 100 rows in memory, exceeding rows will be flushed to disk
		sh = wb.createSheet("Supermapper");
		CellStyle headerStle = getHeaderStyle();
		for (int rownum = 0; rownum < 1; rownum++) {
			Row row = sh.createRow(rownum);
			for (int cellnum = 0; cellnum < columns.length; cellnum++) {
				Cell cell = row.createCell(cellnum);
				cell.setCellValue(columns[cellnum]);
				cell.setCellStyle(headerStle);
			}
		}
	}

	public final SXSSFWorkbook exportExcel(String[] columns, List<E> dataList) {
		fillHeader(columns);
		fillData(dataList);
		autoResizeColumns(columns.length);
		return wb;
	}
	protected abstract void fillData(List<E> dataList);
}*/

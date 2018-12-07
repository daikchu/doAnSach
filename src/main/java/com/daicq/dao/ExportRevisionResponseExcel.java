/*
package com.daicq.dao;

import com.daicq.util.ExcelExportUtility;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.List;

public class ExportRevisionResponseExcel extends ExcelExportUtility<JSONArray> {
    
    protected void fillData(List<JSONArray> dataList) {
        CellStyle normalStyle = getNormalStyle();
        int rownum = 1;
        SimpleDateFormat dtFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
        for (JSONArray rev : dataList) {
            Row row = sh.createRow(rownum);
            Cell cell_0 = row.createCell(0, Cell.CELL_TYPE_STRING);
            cell_0.setCellStyle(normalStyle);
            cell_0.setCellValue(rev.getString(0));
            Cell cell_1 = row.createCell(1, Cell.CELL_TYPE_STRING);
            cell_1.setCellStyle(normalStyle);
            cell_1.setCellValue(rev != null ? dtFormat.format(rev) : " ");
            rownum++;
        }
    }
}*/

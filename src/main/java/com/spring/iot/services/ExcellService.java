package com.spring.iot.services;

import com.spring.iot.entities.SensorValue;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.time.temporal.ValueRange;
import java.util.List;

public class ExcellService {
    public static void employeeDetailReport(HttpServletResponse response, List<SensorValue> values) {
        try(Workbook workbook = new XSSFWorkbook()){
            Sheet sheet = workbook.createSheet("Employee TechGeekNext Example");

            CellStyle cellStyle = workbook.createCellStyle();


            //set border to table
            cellStyle.setBorderTop(BorderStyle.MEDIUM);
            cellStyle.setBorderRight(BorderStyle.MEDIUM);
            cellStyle.setBorderBottom(BorderStyle.MEDIUM);
            cellStyle.setBorderLeft(BorderStyle.MEDIUM);
            cellStyle.setAlignment(HorizontalAlignment.LEFT);


            // Header
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue("Id");
            cell.setCellStyle(cellStyle);


            Cell cell1 = row.createCell(1);
            cell1.setCellValue("Name");
            cell1.setCellStyle(cellStyle);


            Cell cell2 = row.createCell(2);
            cell2.setCellValue("Role");
            cell2.setCellStyle(cellStyle);



            //Set data
//            int rowNum = 1;
//            for (SensorValue emp : employees) {
//                Row empDataRow = sheet.createRow(rowNum++);
//                Cell empIdCell = empDataRow.createCell(0);
//                empIdCell.setCellStyle(cellStyle);
//                empIdCell.setCellValue(emp.getId());
//
//                Cell empNameCell = empDataRow.createCell(1);
//                empNameCell.setCellStyle(cellStyle);
//                empNameCell.setCellValue(emp.getName());
//
//                Cell empRoleCell = empDataRow.createCell(2);
//                empRoleCell.setCellStyle(cellStyle);
//                empRoleCell.setCellValue(emp.getRole());
//            }

            //write output to response
            workbook.write(response.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

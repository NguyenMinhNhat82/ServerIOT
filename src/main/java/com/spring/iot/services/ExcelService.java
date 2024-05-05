package com.spring.iot.services;

import com.spring.iot.entities.Sensor;
import com.spring.iot.entities.SensorValue;
import com.spring.iot.entities.Station;
import com.spring.iot.util.ExcelUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

public class ExcelService {
    private SXSSFWorkbook workbook;
    private SXSSFSheet sheet;
    private SXSSFSheet sheetStation;
    private SXSSFSheet sheetSensor;
    private List<SensorValue> listData;
    private List<Station> stations;
    private List<Sensor> sensors;

    public ExcelService(List<SensorValue> listData, List<Station> stations, List<Sensor> sensors) {
        this.listData = listData;
        this.stations = stations;
        this.sensors = sensors;
        workbook = new SXSSFWorkbook(-1);
    }


    public void writeToExcelManualFlush() {

        try {
            // turn off auto-flushing and accumulate all rows in memory
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setFontHeight((short) 250);
            style.setFont(font);
            int rownum = 1;

            for (SensorValue valueSensor : listData) {
                Row row = sheet.createRow(rownum++);
                int columnCount = 0;
                createCell(row, columnCount++, valueSensor.getId(), style);
                createCell(row, columnCount++, valueSensor.getValue(), style);
                createCell(row, columnCount++, valueSensor.getTimeUpdate().toString(), style);
                createCell(row, columnCount++, valueSensor.getSensor().getStation().getName().toString(), style);
                createCell(row, columnCount++, valueSensor.getSensor().getId(), style);
                // manually control how rows are flushed to disk
                if (rownum % 100 == 0) {
                    // retain 100 last rows and flush all others
                    sheet.flushRows(100);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void writeToExcelManualFlushStation() {

        try {
            // turn off auto-flushing and accumulate all rows in memory
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setFontHeight((short) 250);
            style.setFont(font);
            int rownum = 1;

            for (Station station : stations) {
                Row row = sheetStation.createRow(rownum++);
                int columnCount = 0;
                createCell(row, columnCount++, station.getId(), style);
                createCell(row, columnCount++, station.getName(), style);
                createCell(row, columnCount++, station.getActive()?"Active":"Inactive", style);
                // manually control how rows are flushed to disk
                if (rownum % 100 == 0) {
                    // retain 100 last rows and flush all others
                    sheetStation.flushRows(100);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void writeToExcelManualFlushSensor() {

        try {
            // turn off auto-flushing and accumulate all rows in memory
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setFontHeight((short) 250);
            style.setFont(font);
            int rownum = 1;

            for (Sensor s : sensors) {
                Row row = sheetSensor.createRow(rownum++);
                int columnCount = 0;
                createCell(row, columnCount++, s.getId(), style);
                createCell(row, columnCount++, s.getStation().getName(), style);
                // manually control how rows are flushed to disk
                if (rownum % 100 == 0) {
                    // retain 100 last rows and flush all others
                    sheetSensor.flushRows(100);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void writeHeaderLineSensor() {
        sheetSensor = workbook.createSheet("Sensors");

        sheetSensor.setColumnWidth(0, 20 * 256);
        sheetSensor.setColumnWidth(1, 25 * 256);


        Row row = sheetSensor.createRow(0);

        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight((short) 320);

        style.setFont(font);

        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillBackgroundColor(IndexedColors.BLUE_GREY.index);
        createCell(row, 0, "Name", style);
        createCell(row, 1, "Belong to station", style);



    }

    private void writeHeaderLineStation() {
        sheetStation = workbook.createSheet("Stations");

        sheetStation.setColumnWidth(0, 10 * 256);
        sheetStation.setColumnWidth(1, 14 * 256);
        sheetStation.setColumnWidth(2, 35 * 256);
        sheetStation.setColumnWidth(3, 20 * 256);
        sheetStation.setColumnWidth(4, 20 * 256);


        Row row = sheetStation.createRow(0);

        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight((short) 320);

        style.setFont(font);

        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillBackgroundColor(IndexedColors.BLUE_GREY.index);
        createCell(row, 0, "ID", style);
        createCell(row, 1, "Name", style);
        createCell(row, 2, "Status", style);



    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Value");

        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 14 * 256);
        sheet.setColumnWidth(2, 35 * 256);
        sheet.setColumnWidth(3, 20 * 256);
        sheet.setColumnWidth(4, 20 * 256);


        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight((short) 320);

        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        createCell(row, 0, "ID", style);
        createCell(row, 1, "Value", style);
        createCell(row, 2, "Time", style);
        createCell(row, 3, "Station", style);
        createCell(row, 4, "Sensor", style);


    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
//        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }


    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLineStation();
        writeToExcelManualFlushStation();
        writeHeaderLineSensor();
        writeToExcelManualFlushSensor();
        writeHeaderLine();
        writeToExcelManualFlush();


//        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();

        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }
}

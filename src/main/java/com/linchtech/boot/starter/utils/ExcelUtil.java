package com.linchtech.boot.starter.utils;

import io.swagger.annotations.ApiModelProperty;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author 107
 * @date 2020-05-10 21:35
 **/
public class ExcelUtil {

    /**
     * 填充报表值
     * @param result 结果集
     * @param wb 工作簿对象
     * @param sheetAt 需要填充的sheet
     * @param rowIndex 从多少行开始插入
     * @param columnIndex 从多少列插入
     * @param <T>
     * @throws IllegalAccessException
     */
    public <T> void insertExcelValue(List<T> result,
                                     XSSFWorkbook wb,
                                     int sheetAt,
                                     int rowIndex,
                                     int columnIndex) throws IllegalAccessException {
        XSSFSheet sheet = wb.getSheetAt(sheetAt);
        for (T t : result) {
            XSSFRow row = sheet.createRow(rowIndex++);
            Class<?> aClass = t.getClass();
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                ApiModelProperty property = declaredField.getAnnotation(ApiModelProperty.class);
                if (property != null && !property.hidden()) {
                    XSSFCell cell = row.createCell(columnIndex);
                    // 数字类型的用数字类型的单元格
                    if (Double.class.isAssignableFrom(declaredField.getType())) {
                        cell.setCellType(CellType.NUMERIC);
                        Double value = (Double) declaredField.get(t);
                        cell.setCellValue(value == null ? 0 : value);
                    } else if (Integer.class.isAssignableFrom(declaredField.getType())) {
                        cell.setCellType(CellType.NUMERIC);
                        Integer value = (Integer) declaredField.get(t);
                        cell.setCellValue(value == null ? 0 : value);
                    } else if (Long.class.isAssignableFrom(declaredField.getType())) {
                        cell.setCellType(CellType.NUMERIC);
                        Long value = (Long) declaredField.get(t);
                        cell.setCellValue(value == null ? 0 : value);
                    } else {
                        Object value = declaredField.get(t);
                        cell.setCellValue(value == null ? "" : value.toString());
                    }
                    columnIndex++;
                }
            }
        }
    }
}

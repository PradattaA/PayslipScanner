package com.neelhridoy.pdf.scanner;

import com.neelhridoy.pdf.scanner.payslip.PayslipModel;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Palash
 * Date: 16/5/16
 * Time: 10:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class PayslipExcelWriter {
    public static final String TEMPLATE_XLSX = "resources/Salary.xlsx";

    void write(List<PayslipModel> payslipModelList, String path) throws IOException, ParseException {
        if (payslipModelList.isEmpty()) {
            System.out.println("Nothing to write!");
            return;
        }

        //sort according to salary month-year
        Collections.sort(payslipModelList, new Comparator<PayslipModel>() {
            @Override
            public int compare(PayslipModel o1, PayslipModel o2) {
                try {
                    return o1.getDate().compareTo(o2.getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 1;
            }
        });

        FileInputStream fis = new FileInputStream(new File(TEMPLATE_XLSX));
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFSheet annualSheet = workbook.getSheetAt(1);

        XSSFDataFormat df = workbook.createDataFormat();
        XSSFCellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(df.getFormat("mmm yyyy"));

        XSSFCellStyle numStyle = workbook.createCellStyle();
        numStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));

        int rowNum = 2;
        int annualSheetRowNum = 1;
        int year, prevYear = -1, totalEarning = 0, netPayable = 0;
        for (PayslipModel payslipModel : payslipModelList) {
            // first sheet
            int columnCount = 0;
            XSSFRow row = getOrCreateRow(sheet, rowNum);
            getOrCreateCell(row, columnCount++).setCellValue(rowNum - 1); // sr no
            columnCount++;
            getOrCreateCell(row, columnCount++, dateStyle).setCellValue(payslipModel.getDate());
            getOrCreateCell(row, columnCount++, numStyle).setCellValue(payslipModel.getBasicEarning());
            getOrCreateCell(row, columnCount++, numStyle).setCellValue(payslipModel.getHra());
            getOrCreateCell(row, columnCount++, numStyle).setCellValue(payslipModel.getConveyanceAllowance());
            getOrCreateCell(row, columnCount++, numStyle).setCellValue(payslipModel.getMedicalAllowance());
            getOrCreateCell(row, columnCount++, numStyle).setCellValue(payslipModel.getLta());
            getOrCreateCell(row, columnCount++, numStyle).setCellValue(payslipModel.getCca());

            XSSFCell cell = getOrCreateCell(row, columnCount++); // total earning
            cell.setCellType(Cell.CELL_TYPE_FORMULA);
            cell.setCellFormula("SUM(D" + (rowNum + 1) + ":I" + (rowNum + 1) + ")");

            getOrCreateCell(row, columnCount++, numStyle).setCellValue(payslipModel.getpFund());
            getOrCreateCell(row, columnCount++, numStyle).setCellValue(payslipModel.getpTax());
            getOrCreateCell(row, columnCount++, numStyle).setCellValue(payslipModel.getiTax());
            getOrCreateCell(row, columnCount++, numStyle).setCellValue(payslipModel.getOther());

            cell = getOrCreateCell(row, columnCount++);
            cell.setCellType(Cell.CELL_TYPE_FORMULA);
            cell.setCellFormula("SUM(K" + (rowNum + 1) + ":N" + (rowNum + 1) + ")");

            cell = getOrCreateCell(row, columnCount);
            cell.setCellType(Cell.CELL_TYPE_FORMULA);
            cell.setCellFormula("J" + (rowNum + 1) + "-O" + (rowNum + 1) + "");

            rowNum++;

            // 2nd sheet
            year = Integer.parseInt(payslipModel.getYear());
            columnCount = 1;
            if (prevYear != -1 && year > prevYear) {
                // new year encountered, create one row for previous year
                row = getOrCreateRow(annualSheet, annualSheetRowNum++);
                getOrCreateCell(row, columnCount++).setCellValue(prevYear);
                getOrCreateCell(row, columnCount++).setCellValue(totalEarning);
                getOrCreateCell(row, columnCount).setCellValue(netPayable);

                //reset annual total
                netPayable = 0;
                totalEarning = 0;
            }
            totalEarning += payslipModel.getTotalEarnings();
            netPayable += payslipModel.getNetPayable();

            prevYear = year;
        }
        workbook.getCreationHelper()
                .createFormulaEvaluator()
                .evaluateAll();

        fis.close();
        try (FileOutputStream fos = new FileOutputStream(path)) {
            workbook.write(fos);
        }
        System.out.println("Excel file saved to " + path);
    }

    private XSSFRow getOrCreateRow(XSSFSheet sheet, int rowNum) {
        XSSFRow row = sheet.getRow(rowNum);
        if (row == null) {
            row = sheet.createRow(rowNum);
        }
        return row;
    }

    private XSSFCell getOrCreateCell(XSSFRow row, int colNum, XSSFCellStyle style) {
        XSSFCell cell = getOrCreateCell(row, colNum);
        cell.setCellStyle(style);
        return cell;
    }

    private XSSFCell getOrCreateCell(XSSFRow row, int colNum) {
        XSSFCell cell = row.getCell(colNum);
        if (cell == null) {
            cell = row.createCell(colNum);
        }
        return cell;
    }

    public static void main(String[] args) throws IOException, ParseException {
        PaySlipScanner paySlipScanner = new PaySlipScanner();
        List<PayslipModel> payslips = paySlipScanner.scan("C:\\path\\to\\Payslip", "password");

        new PayslipExcelWriter().write(payslips, "Salary.xlsx");
    }
}

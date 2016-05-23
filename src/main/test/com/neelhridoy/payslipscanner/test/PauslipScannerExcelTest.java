package com.neelhridoy.payslipscanner.test;

import com.neelhridoy.excel.writer.PayslipExcelWriter;
import com.neelhridoy.pdf.scanner.PaySlipScanner;
import com.neelhridoy.pdf.scanner.payslip.PayslipModel;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Author: Pradatta Adhikary - GIDS/2090131/IK
 * Date: 5/23/2016
 * Time: 5:06 PM
 */

public class PauslipScannerExcelTest {

    public static void main(String[] args) throws IOException, ParseException {
        List<PayslipModel> payslips = PaySlipScanner.scan("C:\\path\\to\\Payslip", "password");

        new PayslipExcelWriter().write(payslips, "Salary.xlsx");
    }
}

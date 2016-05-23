package com.neelhridoy.payslipscanner.test;

import com.neelhridoy.db.sqlite.DbUtil;
import com.neelhridoy.pdf.scanner.PaySlipScanner;
import com.neelhridoy.pdf.scanner.payslip.PayslipModel;

import java.sql.SQLException;
import java.util.List;

/**
 * Author: Pradatta Adhikary - GIDS/2090131/IK
 * Date: 5/23/2016
 * Time: 5:04 PM
 */

public class PayslipScannerSqLiteTest {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        //create table first if not exists
        DbUtil.createTable();

        //fetch existing payslips (if any)
        List<PayslipModel> existingPayslips = DbUtil.fetchPaySlips();

        //scan all payslips from pdf
        List<PayslipModel> payslips = PaySlipScanner.scan("C:\\path\\to\\PaySlips", "password");

        //remove existing payslips (if any)
        payslips.removeAll(existingPayslips);

        //save new payslips
        DbUtil.store(payslips);
    }
}

package com.neelhridoy.pdf.scanner;

import com.neelhridoy.db.sqlite.DbUtil;
import com.neelhridoy.pdf.scanner.payslip.PaySlip;
import com.neelhridoy.pdf.scanner.payslip.PaySlipVersion;
import com.neelhridoy.pdf.scanner.payslip.PayslipModel;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Author: Pradatta Adhikary
 * Date: 9/24/2015
 * Time: 5:02 PM
 */

public class PaySlipScanner {
    public static SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy");

    public List<PayslipModel> scan(String folderPath, String password) {
        List<PayslipModel> payslipModels = new ArrayList<>();
        File file = new File(folderPath);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] listOfFiles = file.listFiles();
                for (File singleFile : listOfFiles != null ? listOfFiles : new File[0]) {
                    if (isPDF(singleFile)) {
                        PaySlipVersion paySlipVersion = checkForPaySlipVersion(singleFile.getName());
                        PaySlip paySlip = analysePayslip(paySlipVersion, singleFile, password);
                        if (paySlip != null) payslipModels.add(paySlip);
                    }
                }
            } else {
                if (isPDF(file)) {
                    PaySlipVersion paySlipVersion = checkForPaySlipVersion(file.getName());
                    PaySlip paySlip = analysePayslip(paySlipVersion, file, password);
                    if (paySlip != null) payslipModels.add(paySlip);
                }
            }
        }
        return payslipModels;
    }

    private PaySlip analysePayslip(PaySlipVersion paySlipVersion, File file, String password) {
        try (PaySlip paySlip = PaySlip.createPaySlip(paySlipVersion, file, password)) {
            System.out.println("paySlip = " + paySlip);
            if (paySlip != null && paySlip.getDesignation() != null) return paySlip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isPDF(File file) {
        String extension = FilenameUtils.getExtension(file.getName());
        return "pdf".equals(extension) || "PDF".equals(extension);
    }

    private PaySlipVersion checkForPaySlipVersion(String name) {
        String[] stringArr = name.split("_");
        String dateString = stringArr[0] + " " + stringArr[1];

        try {
            Date date = formatter.parse(dateString);
            if (date.before(formatter.parse(("APR 2015")))) {
                System.out.println("PaySlip Version  : Old Payslip");
                return PaySlipVersion.V1;
            } else {
                System.out.println("PaySlip Version  : New Payslip");
                return PaySlipVersion.V2;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return PaySlipVersion.V1;
    }

    public void store(List<PayslipModel> payslipList) throws SQLException, ClassNotFoundException {
        try {
            for (PayslipModel payslipModel : payslipList) {
                String insertSql = getInsertSql(payslipModel);
                DbUtil.executeUpdate(insertSql);
            }
        } finally {
            DbUtil.close();
        }
    }


    private int createTable() throws SQLException, ClassNotFoundException {
        String sql = "create table if not exists payslip(" +
                "designation string, " +
                "month string, " +
                "year integer, " +
                "basic integer, " +
                "hra integer, " +
                "ca integer, " +
                "cca integer, " +
                "totalEarning integer, " +
                "pf integer, " +
                "pTax integer, " +
                "iTax integer, " +
                "otherDeduction integer, " +
                "totalDeduction integer, " +
                "net integer " +
                ")";
        return DbUtil.executeUpdate(sql);
    }

    private String getInsertSql(PayslipModel paySlip) {
        String sql = "INSERT INTO PAYSLIP (" +
                "designation, " +
                "month, " +
                "year, " +
                "basic, " +
                "hra, " +
                "ca, " +
                "cca, " +
                "totalEarning, " +
                "pf, " +
                "pTax, " +
                "iTax, " +
                "otherDeduction, " +
                "totalDeduction, " +
                "net \n" +
                ") VALUES (" +
                "'" + paySlip.getDesignation() + "'," +
                "'" + paySlip.getMonth() + "'," +
                "" + paySlip.getYear() + "," +
                "" + paySlip.getBasicEarning() + "," +
                "" + paySlip.getHra() + "," +
                "" + paySlip.getConveyanceAllowance() + "," +
                "" + paySlip.getCca() + "," +
                "" + paySlip.getTotalEarnings() + "," +
                "" + paySlip.getpFund() + "," +
                "" + paySlip.getpTax() + "," +
                "" + paySlip.getiTax() + "," +
                "" + paySlip.getOther() + "," +
                "" + paySlip.getTotalDeduction() + "," +
                "" + paySlip.getNetPayable() + "" +
                ")";
        return sql;
    }

    public List<PayslipModel> fetchPaySlips() throws SQLException, ClassNotFoundException {
        List<PayslipModel> payslipModelList = new ArrayList<>();
        ResultSet resultSet = DbUtil.getResultSet("SELECT * FROM PAYSLIP");
        while (resultSet.next()) {
            PayslipModel payslipModel = new PayslipModel();
            payslipModel.setDesignation(resultSet.getString("designation"));
            payslipModel.setMonth(resultSet.getString("month"));
            payslipModel.setYear(resultSet.getString("year"));
            payslipModel.setBasicEarning(resultSet.getInt("basic"));
            payslipModel.setHra(resultSet.getInt("hra"));
            payslipModel.setConveyanceAllowance(resultSet.getInt("ca"));
            payslipModel.setCca(resultSet.getInt("cca"));
            payslipModel.setTotalEarnings(resultSet.getInt("totalEarning"));
            payslipModel.setpFund(resultSet.getInt("pf"));
            payslipModel.setpTax(resultSet.getInt("pTax"));
            payslipModel.setiTax(resultSet.getInt("iTax"));
            payslipModel.setOther(resultSet.getInt("otherDeduction"));
            payslipModel.setTotalDeduction(resultSet.getInt("totalDeduction"));
            payslipModel.setNetPayable(resultSet.getInt("net"));

            payslipModelList.add(payslipModel);
        }
        return payslipModelList;
    }


    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        PaySlipScanner scanner = new PaySlipScanner();

        //create table first if not exists
        scanner.createTable();

        //fetch existing payslips (if any)
        List<PayslipModel> existingPayslips = scanner.fetchPaySlips();

        //scan all payslips from pdf
        List<PayslipModel> payslips = scanner.scan("C:\\path\\to\\PaySlips", "password");

        //remove existing payslips (if any)
        payslips.removeAll(existingPayslips);

        //save new payslips
        scanner.store(payslips);
    }
}

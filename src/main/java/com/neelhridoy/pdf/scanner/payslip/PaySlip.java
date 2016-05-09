package com.neelhridoy.pdf.scanner.payslip;

import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.ObjectExtractor;
import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;
import technology.tabula.extractors.ExtractionAlgorithm;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Author: Pradatta Adhikary
 * Date: 9/24/2015
 * Time: 5:02 PM
 */

public abstract class PaySlip implements AutoCloseable {
    ObjectExtractor objectExtractor;
    ExtractionAlgorithm extractionAlgorithm;
    String designation;

    private String month;
    private String year;
    private File file;
    private String password;

    int basicEarning;
    int hra;
    int conveyanceAllowance;
    int medicalAllowance;
    int lta;
    int cca;
    int totalEarnings;
    int pFund;
    int pTax;
    int iTax;
    int esi;
    int other;
    int totalDeduction;
    int netPayable;


    static final String DATE_START_LINE = "MONTHLY PAYSLIP";
    static final String PROVIDENT_FUND = "Provident Fund(INR)";
    private static SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy");


    PaySlip(File file, String password) throws IOException {
        this.file = file;
        this.password = password;
        loadPaySlip();
        analyse();
    }

    public static PaySlip createPaySlip(PaySlipVersion paySlipVersion, File file, String password) throws IOException {
        PaySlip paySlip = null;
        switch (paySlipVersion) {
            case V1:
                paySlip = new PaySlipV1(file, password);
                break;
            case V2:
                paySlip = new PaySlipV2(file, password);
                break;
            default:
                paySlip = new PaySlipV1(file, password);
                break;
        }
        return paySlip;
    }

    private void loadPaySlip() throws IOException {
        System.out.println("Reading File : " + file.getName());
        PDDocument document = PDDocument.load(file);
        objectExtractor = new ObjectExtractor(document, password);
        extractionAlgorithm = new SpreadsheetExtractionAlgorithm();
    }

    abstract void analyse();


    void processDate(Table table) {
        String dateString = table.getCell(1, 0).getText(false);
        dateString = dateString.trim();
        dateString = dateString.substring(DATE_START_LINE.length() + 4, dateString.length());
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(formatter.parse(dateString));
            month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
            year = String.valueOf(cal.get(Calendar.YEAR));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    void processDesignation(Table table) {
        List<RectangularTextContainer> row = table.getRows().get(4);
        designation = row.size() == 10 ? row.get(8).getText(false) : row.get(7).getText(false);
    }

    int parseInt(String text) {
        return text != null && !text.equalsIgnoreCase("NULL") && text.trim().length() > 0 ? Integer.parseInt(text) : 0;
    }

    @Override
    public void close() throws Exception {
        if (objectExtractor != null) {
            objectExtractor.close();
        }
    }

    @Override
    public String toString() {
        return "PaySlip{" +
                "designation='" + designation + '\'' +
                ", month='" + month + '\'' +
                ", year='" + year + '\'' +
                ", basicEarning=" + basicEarning +
                ", hra=" + hra +
                ", conveyanceAllowance=" + conveyanceAllowance +
                ", cca=" + cca +
                ", totalEarnings=" + totalEarnings +
                ", pFund=" + pFund +
                ", pTax=" + pTax +
                ", iTax=" + iTax +
                ", other=" + other +
                ", totalDeduction=" + totalDeduction +
                ", netPayable=" + netPayable +
                '}';
    }
}

package com.neelhridoy.pdf.scanner;

import com.neelhridoy.pdf.scanner.payslip.PaySlip;
import com.neelhridoy.pdf.scanner.payslip.PaySlipVersion;
import com.neelhridoy.pdf.scanner.payslip.PayslipModel;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
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

    public static List<PayslipModel> scan(String folderPath, String password) {
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

    private static PaySlip analysePayslip(PaySlipVersion paySlipVersion, File file, String password) {
        try (PaySlip paySlip = PaySlip.createPaySlip(paySlipVersion, file, password)) {
            System.out.println("paySlip = " + paySlip);
            if (paySlip != null && paySlip.getDesignation() != null) return paySlip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean isPDF(File file) {
        String extension = FilenameUtils.getExtension(file.getName());
        return "pdf".equals(extension) || "PDF".equals(extension);
    }

    private static PaySlipVersion checkForPaySlipVersion(String name) {
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
}

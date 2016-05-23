package com.neelhridoy.pdf.scanner.payslip;

import com.neelhridoy.pdf.scanner.PaySlipScanner;

import java.text.ParseException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Palash
 * Date: 12/5/16
 * Time: 11:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class PayslipModel {
    protected String designation;
    protected String month;
    protected String year;
    protected int basicEarning;
    protected int hra;
    protected int conveyanceAllowance;
    protected int medicalAllowance;
    protected int lta;
    protected int cca;
    protected int totalEarnings;
    protected int pFund;
    protected int pTax;
    protected int iTax;
    protected int esi;
    protected int other;
    protected int totalDeduction;
    protected int netPayable;

    public String getDesignation() {
        return designation;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public Date getDate() throws ParseException {
        return PaySlipScanner.formatter.parse(month + " " + year);
    }

    public int getBasicEarning() {
        return basicEarning;
    }

    public int getHra() {
        return hra;
    }

    public int getConveyanceAllowance() {
        return conveyanceAllowance;
    }

    public int getMedicalAllowance() {
        return medicalAllowance;
    }

    public int getLta() {
        return lta;
    }

    public int getCca() {
        return cca;
    }

    public int getTotalEarnings() {
        return totalEarnings;
    }

    public int getpFund() {
        return pFund;
    }

    public int getpTax() {
        return pTax;
    }

    public int getiTax() {
        return iTax;
    }

    public int getEsi() {
        return esi;
    }

    public int getOther() {
        return other;
    }

    public int getTotalDeduction() {
        return totalDeduction;
    }

    public int getNetPayable() {
        return netPayable;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setBasicEarning(int basicEarning) {
        this.basicEarning = basicEarning;
    }

    public void setHra(int hra) {
        this.hra = hra;
    }

    public void setConveyanceAllowance(int conveyanceAllowance) {
        this.conveyanceAllowance = conveyanceAllowance;
    }

    public void setMedicalAllowance(int medicalAllowance) {
        this.medicalAllowance = medicalAllowance;
    }

    public void setLta(int lta) {
        this.lta = lta;
    }

    public void setCca(int cca) {
        this.cca = cca;
    }

    public void setTotalEarnings(int totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public void setpFund(int pFund) {
        this.pFund = pFund;
    }

    public void setpTax(int pTax) {
        this.pTax = pTax;
    }

    public void setiTax(int iTax) {
        this.iTax = iTax;
    }

    public void setEsi(int esi) {
        this.esi = esi;
    }

    public void setOther(int other) {
        this.other = other;
    }

    public void setTotalDeduction(int totalDeduction) {
        this.totalDeduction = totalDeduction;
    }

    public void setNetPayable(int netPayable) {
        this.netPayable = netPayable;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PayslipModel that = (PayslipModel) o;

        if (basicEarning != that.basicEarning) return false;
        if (!month.equals(that.month)) return false;
        if (!year.equals(that.year)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = month.hashCode();
        result = 31 * result + year.hashCode();
        result = 31 * result + basicEarning;
        return result;
    }
}

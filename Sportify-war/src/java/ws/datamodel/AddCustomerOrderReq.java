/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.ProductPurchase;
import java.util.Date;
import java.util.List;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author shanw
 */
public class AddCustomerOrderReq {
    private long customerId;
    
    private double totalAmount;
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePaid;
    
    private String voucherCode;
    private double discountFromVoucher;
    
    List<ProductPurchase> productPurchases;

    public AddCustomerOrderReq() {
    }

    public AddCustomerOrderReq(long customerId,double totalAmount, Date datePaid, String voucherCode, double discountFromVoucher, List<ProductPurchase> productPurchases) {
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.datePaid = datePaid;
        this.voucherCode = voucherCode;
        this.discountFromVoucher = discountFromVoucher;
        this.productPurchases = productPurchases;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(Date datePaid) {
        this.datePaid = datePaid;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public double getDiscountFromVoucher() {
        return discountFromVoucher;
    }

    public void setDiscountFromVoucher(double discountFromVoucher) {
        this.discountFromVoucher = discountFromVoucher;
    }

    public List<ProductPurchase> getProductPurchases() {
        return productPurchases;
    }

    public void setProductPurchases(List<ProductPurchase> productPurchases) {
        this.productPurchases = productPurchases;
    }
    
    
}

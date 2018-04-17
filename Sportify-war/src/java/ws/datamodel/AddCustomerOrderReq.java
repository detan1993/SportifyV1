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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author shanw
 */
@XmlRootElement
@XmlType(name = "addCustomerOrderReq", propOrder = {
   "customerId", "totalAmount" , "datePaid" , "voucherId" , "discountFromVoucher", "productPurchases", "sizePurchases"
})

public class AddCustomerOrderReq {
    private long customerId;
    
    private double totalAmount;
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePaid;
    
    private long voucherId;
    private double discountFromVoucher;
    
    List<ProductPurchase> productPurchases;
    List<String> sizePurchases;

    public AddCustomerOrderReq() {
    }

    public AddCustomerOrderReq(long customerId,double totalAmount, Date datePaid, long voucherId, double discountFromVoucher, List<ProductPurchase> productPurchases, List<String> sizePurchases) {
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.datePaid = datePaid;
        this.voucherId = voucherId;
        this.discountFromVoucher = discountFromVoucher;
        this.productPurchases = productPurchases;
        this.sizePurchases = sizePurchases;
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

    public long getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(long voucherId) {
        this.voucherId = voucherId;
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

    public List<String> getSizePurchases() {
        return sizePurchases;
    }

    public void setSizePurchases(List<String> sizePurchases) {
        this.sizePurchases = sizePurchases;
    }
    
    
}

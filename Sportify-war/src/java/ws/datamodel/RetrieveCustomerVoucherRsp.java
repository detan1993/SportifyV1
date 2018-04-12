/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.Voucher;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author shanw
 */

@XmlRootElement
@XmlType(name = "retrieveCustomerAccountRsp", propOrder = {
    "voucherList"
})

public class RetrieveCustomerVoucherRsp {
    List<Voucher> voucherList;

    public RetrieveCustomerVoucherRsp() {
    }

    public RetrieveCustomerVoucherRsp(List<Voucher> voucherList) {
        this.voucherList = voucherList;
    }

    public List<Voucher> getVoucherList() {
        return voucherList;
    }

    public void setVoucherList(List<Voucher> voucherList) {
        this.voucherList = voucherList;
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerVoucher;
import java.util.List;

public interface CustomerVoucherControllerRemote {
    public CustomerVoucher createNewCustomerVoucher(CustomerVoucher customerVoucher);
    public List<CustomerVoucher> retrieveAllCustomerVouchers();
    public List<CustomerVoucher> retrieveCustomerVouchersByVoucherId(long voucherid);
    public List<CustomerVoucher> retrieveUnusedCustomerVouchersByVoucherId(long voucherid);
}

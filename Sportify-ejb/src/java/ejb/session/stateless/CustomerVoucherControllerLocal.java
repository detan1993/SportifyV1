/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.CustomerOrder;
import entity.CustomerVoucher;
import entity.Voucher;
import java.util.List;

public interface CustomerVoucherControllerLocal {
    public CustomerVoucher createNewCustomerVoucher(CustomerVoucher customerVoucher);
    public List<CustomerVoucher> retrieveAllCustomerVouchers();
    public CustomerVoucher retrieveCustomerVoucher(Customer c, Voucher v);
    public void useCustomerVoucher(CustomerOrder co, Voucher v, CustomerVoucher cv);
}

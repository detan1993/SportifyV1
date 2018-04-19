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

public interface CustomerVoucherControllerRemote {
    public CustomerVoucher createNewCustomerVoucher(CustomerVoucher customerVoucher);
    public List<CustomerVoucher> retrieveAllCustomerVouchers();
    public CustomerVoucher retrieveCustomerVoucher(Customer c, Voucher v);
    public void useCustomerVoucher(CustomerOrder co, Voucher v, CustomerVoucher cv);
    public List<CustomerVoucher> retrieveCustomerVouchersByVoucherId(long voucherid);
    public List<CustomerVoucher> retrieveUnusedCustomerVouchersByVoucherId(long voucherid);
    public List<Voucher> Ws_retrieveUnusedCustomerVouchersByCustomerId(long customerId);
    public List<CustomerVoucher> retrieveCustomerListOfVouchers(Customer c);
    public int countNumOfVoucher(Customer c, Voucher v);
//    public CustomerVoucher retrieveVoucherByCustomerOrder(CustomerOrder cv );
}

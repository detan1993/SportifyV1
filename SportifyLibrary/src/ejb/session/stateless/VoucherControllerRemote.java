/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Voucher;
import java.util.List;

public interface VoucherControllerRemote {
    public Voucher createNewVoucher(Voucher newVoucher);
    public List<Voucher> retrieveVouchers();
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Voucher;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author shanw
 */
@Stateless
@Local(VoucherControllerLocal.class)
@Remote(VoucherControllerRemote.class)
public class VoucherController implements VoucherControllerRemote, VoucherControllerLocal {
    @PersistenceContext(unitName = "Sportify-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }
    
    @Override
    public Voucher createNewVoucher(Voucher newVoucher){
        em.persist(newVoucher);
        em.flush();
        em.refresh(newVoucher);
        return newVoucher;
    }
    
    @Override
    public List<Voucher> retrieveVouchers(){
      Query query = em.createQuery("SELECT v FROM Voucher v");
      return query.getResultList();
    }
}

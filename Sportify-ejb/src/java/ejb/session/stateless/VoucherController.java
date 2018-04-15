/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Voucher;
import java.util.Date;
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
    
    @Override
    public Voucher retrieveVoucher(String voucherCode){
        try{
        Query q = em.createQuery("SELECT v FROM Voucher v WHERE v.voucherCode=:code");
        q.setParameter("code", voucherCode);
        return (Voucher)q.getSingleResult();
        }catch(Exception ex){
            return null;
        }
    }
    
    @Override
     public Voucher retrieveCustomerVoucher(String promocode, String customeremail){
         Query q = em.createQuery("SELECT v FROM Voucher v JOIN v.customerVouchers cv JOIN cv.customer c WHERE c.email = :email AND v.voucherCode = :promocode AND v.dateExpired > :todaydate AND cv.customerOrder IS NULL");
         q.setParameter("promocode", promocode);
         q.setParameter("email", customeremail);
         q.setParameter("todaydate", new Date());
         Voucher v = new Voucher();
         if (!q.getResultList().isEmpty()){
             v = (Voucher)q.getResultList().get(0);
             return v;
         }
         return v;
     }
}

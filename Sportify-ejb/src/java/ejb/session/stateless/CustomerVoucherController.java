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
@Local(CustomerVoucherControllerLocal.class)
@Remote(CustomerVoucherControllerRemote.class)
public class CustomerVoucherController implements CustomerVoucherControllerRemote, CustomerVoucherControllerLocal {
    @PersistenceContext(unitName = "Sportify-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }
    
    @Override 
    public CustomerVoucher createNewCustomerVoucher(CustomerVoucher customerVoucher){
        em.persist(customerVoucher);
        em.flush();
        em.refresh(customerVoucher);
        return customerVoucher;
    }
    
    @Override
    public List<CustomerVoucher> retrieveAllCustomerVouchers(){
        Query query = em.createQuery("SELECT v FROM CustomerVoucher v");
        return query.getResultList();
    }
    
    @Override
     public CustomerVoucher retrieveCustomerVoucher(Customer c, Voucher v){
         Query q = em.createQuery("SELECT cv FROM CustomerVoucher cv WHERE cv.customer.id =:cid AND cv.voucher.id =:vid AND cv.customerOrder IS NULL");
         q.setParameter("cid", c.getId());
         q.setParameter("vid", v.getId());
         return (CustomerVoucher)q.getResultList().get(0);
     }
     
     @Override 
     public void useCustomerVoucher(CustomerOrder co, Voucher v, CustomerVoucher cv){
         co.setCustomerVoucher(cv);
         cv.setCustomerOrder(co);
         em.merge(co);
         em.merge(cv);
         em.merge(v);
     }
     
    @Override
    public List<CustomerVoucher> retrieveCustomerVouchersByVoucherId(long voucherid){
        Query query = em.createQuery("SELECT v FROM CustomerVoucher v WHERE v.voucher.id=:voucherid");
        query.setParameter("voucherid", voucherid);
        return  query.getResultList();
    }
    
    @Override
    public List<CustomerVoucher> retrieveUnusedCustomerVouchersByVoucherId(long voucherid){
        Query query = em.createQuery("SELECT v FROM CustomerVoucher v WHERE v.voucher.id=:voucherid AND v.customerOrder IS NULL");
        query.setParameter("voucherid", voucherid);
        return  query.getResultList();
    }
    
    @Override
    public List<Voucher> Ws_retrieveUnusedCustomerVouchersByCustomerId(long customerId){
        Query query = em.createQuery("SELECT v FROM CustomerVoucher cv JOIN Voucher v WHERE cv.customer.id=:customerId AND cv.customerOrder IS NULL");
        query.setParameter("customerId", customerId);
        return  query.getResultList();
    }
    
    @Override
     public List<CustomerVoucher> retrieveCustomerListOfVouchers(Customer c){
         Query q = em.createQuery("SELECT cv FROM CustomerVoucher cv WHERE cv.customer.id =:cid AND cv.customerOrder IS NULL");
         q.setParameter("cid", c.getId());
         return  q.getResultList();
     }
     
     @Override
     public int countNumOfVoucher(Customer c, Voucher v){
         Query q = em.createQuery("SELECT cv FROM CustomerVoucher cv WHERE cv.customer.id =:cid AND cv.voucher.id=:vid AND cv.customerOrder IS NULL ");
         q.setParameter("cid", c.getId());
         q.setParameter("vid", v.getId());
         return  q.getResultList().size();
     }
}

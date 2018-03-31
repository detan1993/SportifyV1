/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerVoucher;
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
}

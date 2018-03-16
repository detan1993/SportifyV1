/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerOrder;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author shanw
 */
@Stateless
public class CustomerOrderController implements CustomerOrderControllerRemote, CustomerOrderControllerLocal {

    @PersistenceContext(unitName = "Sportify-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }
    
    @Override
    public CustomerOrder CreateNewCustomerOrder(CustomerOrder newCustomerOrder)
    {
        em.persist(newCustomerOrder);
        em.flush();
        em.refresh(newCustomerOrder);
        return newCustomerOrder;
    }
    
    @Override
    public List<CustomerOrder> RetrieveAllCustomerOrder(){
        Query query = em.createQuery("SELECT p FROM CustomerOrder p");
        return query.getResultList();
    }
}

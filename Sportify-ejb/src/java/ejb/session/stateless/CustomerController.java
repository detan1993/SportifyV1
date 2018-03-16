/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
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
@Local(CustomerControllerLocal.class)
@Remote(CustomerControllerRemote.class)
@Stateless
public class CustomerController implements CustomerControllerRemote, CustomerControllerLocal {
    
    @PersistenceContext(unitName = "Sportify-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }
    
    @Override
    public Customer createNewCustomer(Customer newCustomer){
        
        em.persist(newCustomer);
        em.flush();
        em.refresh(newCustomer);
        
        return newCustomer;
    }
    
    @Override
    public List<Customer> retrieveCustomer(){
      Query query = em.createQuery("SELECT e FROM Customer e");
      return query.getResultList();
    }
    
    
    @Override
    public Customer retrieveCustomer(String email){
        Query query = em.createQuery("Select e FROM Customer e WHERE e.email =:email");
        query.setParameter("email", email);
        Customer c = new Customer();
        if(query.getResultList().size() >0){
            return (Customer)query.getSingleResult();
        }
        
        return null;
    }
    
    @Override
    public Customer login(String email, String password)
    {
        Customer customer = em.find(Customer.class, email);
        if(customer != null)
        {
            if(customer.getPassword().equals(password))
                return customer;
        }
        
        return null;
    }
}

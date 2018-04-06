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
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;

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
    public Customer createNewCustomer(Customer newCustomer) {

        em.persist(newCustomer);
        em.flush();
        em.refresh(newCustomer);

        return newCustomer;
    }

    @Override
    public List<Customer> retrieveCustomer() {
        Query query = em.createQuery("SELECT e FROM Customer e");
        return query.getResultList();
    }

    @Override
    public Customer retrieveCustomer(String email) throws CustomerNotFoundException {
        Query query = em.createQuery("Select e FROM Customer e WHERE e.email =:email");
        query.setParameter("email", email);
        Customer c = new Customer();
        if (query.getResultList().size() > 0) {
            return (Customer) query.getSingleResult();
        } else {
            throw new CustomerNotFoundException("Wrong customer credential");
        }

    }

    @Override
    public List<Customer> retrieveCustomerByMonth(int month) {
        Query query = em.createQuery("Select e FROM Customer e WHERE SUBSTRING(e.dateOfBirth,4,2) =:month");
        query.setParameter("month", month);
        return query.getResultList();
    }

    @Override
    public Customer login(String email, String password) throws InvalidLoginCredentialException {

        System.out.println("Custoemr login email: " + email + " password: " + password);

        try {
            Customer customer = retrieveCustomer(email);
            if (customer != null) {
                if (customer.getPassword().equals(password)) {
                    return customer;
                } else {
                    throw new InvalidLoginCredentialException("Wrong user password");
                }
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }

        } catch (CustomerNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }

    @Override
    public boolean checkEmailAlreadyExist(String email){
        Query query = em.createQuery("Select e FROM Customer e WHERE e.email =:email");
        query.setParameter("email", email);
        Customer c = new Customer();
        if (query.getResultList().size() > 0) {
            return true;
        }
        return false;
    }
}

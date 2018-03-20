/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Staff;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;

/**
 *
 * @author shanw
 */
@Local(StaffControllerLocal.class)
@Remote(StaffControllerRemote.class)
@Stateless
public class StaffController implements StaffControllerRemote, StaffControllerLocal {
    @PersistenceContext(unitName = "Sportify-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }
    

    @Override
    public Staff login(String email, String password) throws InvalidLoginCredentialException
    {
        
        System.out.println("Staff login email: " + email + " password: " + password);
        
        try{
              Staff staff = retrieveStaffByEmail(email);
              if(staff != null)
              {
                  if(staff.getPassword().equals(password))
                      return staff;
                  else
                      throw new InvalidLoginCredentialException("Wrong user password");
              }
              else{
                  throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
              }
            
        }catch(StaffNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }

    }
    
    @Override
    public Staff createStaff(Staff newStaff)
    {
        em.persist(newStaff);
        em.flush();
        em.refresh(newStaff);
        return newStaff;
        
    }
    
    @Override
    public Staff retrieveStaffByEmail(String email) throws StaffNotFoundException
    {
        Query query = em.createQuery("SELECT s FROM Staff s WHERE s.email = :staffEmail");
        query.setParameter("staffEmail", email);

        if(query.getResultList().size() >0){
            return (Staff)query.getSingleResult();
        }
        else
            throw new StaffNotFoundException("Invalid email address");
 
        
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Staff;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    public Staff login(String email, String password)
    {
        Staff staff = em.find(Staff.class, email);
        if(staff != null)
        {
            if(staff.getPassword().equals(password))
                return staff;
        }
        
        return null;
    }
}

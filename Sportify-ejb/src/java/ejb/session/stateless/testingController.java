/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.testingEntity;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author shanw
 */
@Local(testingControllerLocal.class)
@Remote(testingControllerRemote.class)
@Stateless
public class testingController implements testingControllerRemote, testingControllerLocal {

    @PersistenceContext(unitName = "Sportify-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public testingEntity createTest(testingEntity newTest)
    {
        em.persist(newTest);
        em.flush();
        em.refresh(newTest);
        
        return newTest;
    }
    
}

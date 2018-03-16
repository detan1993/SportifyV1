/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Images;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author David
 */
@Local(ImageControllerLocal.class)
@Remote(ImageControllerRemote.class)
@Stateless
public class ImageController implements ImageControllerRemote, ImageControllerLocal {

    @PersistenceContext(unitName = "Sportify-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public Images createNewImage(Images newImage)
    {
        em.persist(newImage);
        em.flush();
        em.refresh(newImage);
        return newImage;
        
    }
    
    
}

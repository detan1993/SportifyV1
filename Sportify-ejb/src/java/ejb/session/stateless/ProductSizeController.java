/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ProductSize;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author David
 */
@Local(ProductSizeControllerLocal.class)
@Remote(ProductSizeControllerRemote.class)
@Stateless
public class ProductSizeController implements ProductSizeControllerRemote, ProductSizeControllerLocal {

    @PersistenceContext(unitName = "Sportify-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public ProductSize createSizeForProduct(ProductSize newSizeForProduct)
    {
        
        em.persist(newSizeForProduct);
        em.flush();
        em.refresh(newSizeForProduct);
        return newSizeForProduct;
    }
    
    @Override
    public ProductSize retrieveSingleProductSize(long id){
      Query query = em.createQuery("SELECT p FROM ProductSize p WHERE p.id=:id");
      query.setParameter("id", id);
      return (ProductSize)query.getResultList().get(0);
    }
    
    @Override
    public void updateSizeForProduct(ProductSize size){
        System.out.println("Id: " + size.getId() + " Size: " + size.getSize() + " Qty: " + size.getQty());
        
        if(size.getId() != null){
            ProductSize toUpdate = retrieveSingleProductSize(size.getId());
            toUpdate.setSize(size.getSize());
            toUpdate.setQty(size.getQty());
            em.flush();
        }else{
            createSizeForProduct(size);
        }
    }
    
    
}

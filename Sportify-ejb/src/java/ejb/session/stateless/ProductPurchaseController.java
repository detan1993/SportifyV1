/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ProductPurchase;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author David
 */
@Stateless
@Local(ProductPurchaseControllerLocal.class)
@Remote(ProductPurchaseControllerRemote.class)
public class ProductPurchaseController implements ProductPurchaseControllerRemote, ProductPurchaseControllerLocal {

    @PersistenceContext(unitName = "Sportify-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
   public ProductPurchase createProductPurchase (ProductPurchase newProductPurchase){
       
       em.persist(newProductPurchase);
       em.flush();
       em.refresh(newProductPurchase);
       return newProductPurchase;
   }
}

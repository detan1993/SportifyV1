/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Product;
import java.util.List;
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
@Stateless
@Local(ProductControllerLocal.class)
@Remote(ProductControllerRemote.class)
public class ProductController implements ProductControllerRemote, ProductControllerLocal {

    @PersistenceContext(unitName = "Sportify-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Product CreateNewProduct(Product newProduct)
    {
        em.persist(newProduct);
        em.flush();
        em.refresh(newProduct);
        return newProduct;
    }
    
    @Override
    public List<Product> retrieveProduct(){
      Query query = em.createQuery("SELECT p FROM Product p");
      return query.getResultList();
        
    }
    
    //udpate product here
    
    
    //delete product here
    
    
    
}

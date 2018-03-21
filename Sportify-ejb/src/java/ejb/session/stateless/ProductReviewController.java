/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Product;
import entity.ProductReview;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author shanw
 */
@Stateless
public class ProductReviewController implements ProductReviewControllerRemote, ProductReviewControllerLocal {
    @PersistenceContext(unitName = "Sportify-ejbPU")
    private EntityManager em;
    
    public void persist(Object object) {
        em.persist(object);
    }
    
    @EJB(name = "ProductControllerLocal")
    private ProductControllerLocal productControllerLocal;
    
    @Override
    public ProductReview CreateNewProductReview (ProductReview newProductReview)
    {
        em.persist(newProductReview);
        em.flush();
        em.refresh(newProductReview);
        return newProductReview;
    }
    
    @Override
    public List<ProductReview> retrieveProductReviewsByProductId (int productId){
        Product p = productControllerLocal.retrieveSingleProduct(productId);
        return p.getProductReviews();
    }
    
    
}
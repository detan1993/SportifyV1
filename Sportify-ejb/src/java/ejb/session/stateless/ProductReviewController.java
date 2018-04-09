/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerOrder;
import entity.Product;
import entity.ProductReview;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    public ProductReview CreateNewProductReview(ProductReview newProductReview) {
        em.persist(newProductReview);
        em.flush();
        em.refresh(newProductReview);
        return newProductReview;
    }
    

    @Override
    public List<ProductReview> retrieveProductReviewsByProductId(int productId) {
        Product p = productControllerLocal.retrieveSingleProduct(productId);
        return p.getProductReviews();
    }
    
    @Override
    public void updateProductReview(ProductReview productreview){
        em.merge(productreview);
    }
    
    @Override
    public ProductReview getCustomerOrderProductReview (long productId, long customerOrderId){
        Query query = em.createQuery("SELECT p FROM ProductReview p WHERE p.product.id=:productId AND p.customerOrder.id=:customerOrderId");
        query.setParameter("productId", productId);
        query.setParameter("customerOrderId", customerOrderId);
        try {
            ProductReview productReview = (ProductReview) query.getResultList().get(0);
            if (productReview != null) {
                return productReview;
            }
        } catch (Exception ex) {
            System.err.print("NULL Reviews");
        }

        return null;
    }

    @Override
    public String retrieveCustomerOrderProductReview(long productId, long customerOrderId) {
        Query query = em.createQuery("SELECT p FROM ProductReview p WHERE p.product.id=:productId AND p.customerOrder.id=:customerOrderId");
        query.setParameter("productId", productId);
        query.setParameter("customerOrderId", customerOrderId);
        try {
            ProductReview productReview = (ProductReview) query.getResultList().get(0);
            if (productReview != null) {
                return productReview.getReview();
            }
        } catch (Exception ex) {
            System.err.print("NULL Reviews");
        }

        return null;
    }
    
    @Override
    public int retrieveCustomerOrderProductRating(long productId, long customerOrderId) {
        Query query = em.createQuery("SELECT p FROM ProductReview p WHERE p.product.id=:productId AND p.customerOrder.id=:customerOrderId");
        query.setParameter("productId", productId);
        query.setParameter("customerOrderId", customerOrderId);
        try {
            ProductReview productReview = (ProductReview) query.getResultList().get(0);
            if (productReview != null) {
                return productReview.getRating();
            }
        } catch (Exception ex) {
            System.err.print("NULL rating");
        }

        return -1;
    }

    @Override
    public int getAverageProductRating(long productId) {
        Query query = em.createQuery("SELECT p FROM ProductReview p WHERE p.product.id=:productId");
        query.setParameter("productId", productId);
        int totalRating = 0;
        int average = 0;
        try {
            List<ProductReview> productReviews = query.getResultList();

            if (productReviews != null) {
                for (ProductReview prodRev : productReviews) {
                    totalRating += prodRev.getRating();
                }

                average = totalRating / productReviews.size();
            }

        } catch (Exception ex) {
            System.err.print("NULL ProductReviews while getting average rating");
            return -1;
        }
        return average;
    }
}

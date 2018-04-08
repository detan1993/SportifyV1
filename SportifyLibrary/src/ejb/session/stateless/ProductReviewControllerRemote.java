/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;


import entity.ProductReview;
import java.util.List;

public interface ProductReviewControllerRemote {
    public ProductReview CreateNewProductReview (ProductReview newProductReview);
    public List<ProductReview> retrieveProductReviewsByProductId (int productId);
    public String retrieveCustomerOrderProductReview (long productId, long customerOrderId);
    public int retrieveCustomerOrderProductRating(long productId, long customerOrderId);
    public int getAverageProductRating(long productId);
}

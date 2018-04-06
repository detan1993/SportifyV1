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

public interface ProductReviewControllerLocal {
    public ProductReview CreateNewProductReview (ProductReview newProductReview);
    public List<ProductReview> retrieveProductReviewsByProductId (int productId);
    public ProductReview getProductReview(Product product, CustomerOrder customerOrder);
}

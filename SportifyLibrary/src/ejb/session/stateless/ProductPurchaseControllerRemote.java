/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerOrder;
import entity.ProductPurchase;

public interface ProductPurchaseControllerRemote {
    public ProductPurchase createProductPurchase(ProductPurchase newProductPurchase);
    public ProductPurchase retrieveProductPurchase(long id);
    public ProductPurchase updateProductPurchaseWithOrder(long ppId, CustomerOrder newOrder);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ProductSize;

public interface ProductSizeControllerRemote {

    public ProductSize createSizeForProduct(ProductSize newSizeForProduct);
    public ProductSize retrieveSingleProductSize(long id);
    public void updateSizeForProduct(ProductSize size);
}

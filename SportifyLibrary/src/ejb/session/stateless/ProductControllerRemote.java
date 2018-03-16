/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Product;
import java.util.List;

public interface ProductControllerRemote {
    public Product CreateNewProduct(Product newProduct);
    public List<Product> retrieveProduct();
    public Product retrieveSingleProduct(int productId);
    public List<Product> retrieveProductsRunningLow();
}

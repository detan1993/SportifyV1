/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Product;
import entity.ProductSize;
import java.util.List;

public interface ProductControllerRemote {
    public Product CreateNewProduct(Product newProduct);
    public List<Product> retrieveProduct();
    public List<Product> retrieveProductIncludingInactive();
    public List<Product> retrieveActiveProductOrderById();
    public List<Product> Ws_retrieveActiveProductOrderById();
    public Product retrieveSingleProduct(long productId);
    public List<Product> retrieveProductsByTeam(String team);
    public List<Product> retrieveProductsByCountry(String country);
    public List<Product> retrieveProductsRunningLow();
    public List<List<String>> retrieveCountriesAndTeams();
    public void updateProduct(Product newProductInfo);
    public void deleteProduct(Product product);
    public List<Product> getAllProducts();
}

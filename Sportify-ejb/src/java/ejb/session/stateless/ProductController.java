/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Product;
import entity.ProductSize;
import java.util.ArrayList;
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
    
    @Override
    public Product retrieveSingleProduct(long productId){
      Query query = em.createQuery("SELECT p FROM Product p WHERE p.id=:productId");
      query.setParameter("productId", productId);
      return (Product)query.getResultList().get(0);
    }
    
    //udpate product here
    
    //delete product here
    
    
    /*
     * 
     * Gets a list of products with sizes quantity <=10 , does not return sizes with >10
     */
    @Override
    public List<Product> retrieveProductsRunningLow(){
        List<Product> allProductList = retrieveProduct();
        List<Product> lowQuantityList = new ArrayList<Product>();
        
        //Create a new product to store sizes with <=10
        Product newProduct;
        for(Product p : allProductList){
            
            //Get the product information
            newProduct = p;
            
            //Destroy all Sizes first
            newProduct.setSizes(new ArrayList<ProductSize>());
            for(ProductSize s : p.getSizes()){
                if(s.getQty()<=10){
                    //Add size to new Product so that this new product contains only sizes<=10 and still retains product information
                    newProduct.getSizes().add(s);
                }
            }
            if(newProduct.getSizes().size() >0){
                lowQuantityList.add(newProduct);
                newProduct = null;
            }
        }
        
        return lowQuantityList;
    }
    
    @Override
    public void updateProduct(Product newProductInfo)
    {
        //getting product ID
        Product productToUpdate = retrieveSingleProduct(newProductInfo.getId());
        productToUpdate.setCountry(newProductInfo.getCountry());
        productToUpdate.setDescription(newProductInfo.getDescription());
        productToUpdate.setGender(newProductInfo.getGender());
        productToUpdate.setImages(newProductInfo.getImages());
        productToUpdate.setPrice(newProductInfo.getPrice());
        productToUpdate.setProductCode(newProductInfo.getProductCode());
        productToUpdate.setProductName(newProductInfo.getProductName());
        productToUpdate.setSizes(newProductInfo.getSizes());
        productToUpdate.setTeam(newProductInfo.getTeam());
        
        em.flush();
        
    }
    
    
}

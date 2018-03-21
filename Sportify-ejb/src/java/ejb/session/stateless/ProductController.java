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
    
    @Override
    public List<List<String>> retrieveCountriesAndTeams(){
        List<List<String>> countryAndTeamList = new ArrayList<List<String>>();
        
        Query query = em.createQuery("SELECT p.country,p.team FROM Product p ORDER BY p.country");
        List<Object[]> results = query.getResultList();
        
        for(Object[] o : results){
            boolean isCountryIn = false;
            boolean isTeamIn = true;
            String country = (String)o[0];
            String team = (String)o[1];
            
            for(int i=0;i<countryAndTeamList.size();i++){
                if(countryAndTeamList.get(i).indexOf(country) ==0){
                    isCountryIn = true;
                    if(countryAndTeamList.get(i).indexOf(team) <0){
                        isTeamIn = false;
                        countryAndTeamList.get(i).add(team);
                    }
                }
            }
            
            if(isCountryIn ==false){
                List<String> newEntry = new ArrayList<String>();
                newEntry.add(country);
                newEntry.add(team);
                countryAndTeamList.add(newEntry);
            }
        }
        
        return countryAndTeamList;
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
        
        for(Product p : allProductList){
            
            System.out.println("*********************Product: " + p.getProductName());
            
            for(ProductSize s : p.getSizes()){
                
                System.out.println("*********************Product size: " + s.getSize() + " quantity: " + s.getQty());
                
                if(s.getQty()>10){
                    p.getSizes().remove(s);
                }
            }
        }
        
        return allProductList;
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
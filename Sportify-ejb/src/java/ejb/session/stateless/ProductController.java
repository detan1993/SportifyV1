/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Images;
import entity.Product;
import entity.ProductPurchase;
import entity.ProductReview;
import entity.ProductSize;
import java.util.ArrayList;
import java.util.Iterator;
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
    public Product CreateNewProduct(Product newProduct) {
        System.out.println("Controller - CreateNewProduct()");
        
        for(Images image: newProduct.getImages()){
            System.out.println(image.getImagePath());
            em.persist(image);
            em.flush();
            em.refresh(image);
        }
        
        em.persist(newProduct);
        em.flush();
        em.refresh(newProduct);
        
        
        
        return newProduct;
    }

    @Override
    public List<Product> retrieveProduct() {
        Query query = em.createQuery("SELECT p FROM Product p WHERE p.status='A' ORDER BY p.team");
        return query.setMaxResults(5).getResultList();
    }

    @Override
    public List<Product> retrieveProductIncludingInactive(){
        Query query = em.createQuery("SELECT p FROM Product p");
        return query.getResultList();
    }
    
    @Override
    public List<Product> retrieveActiveProductOrderById(){
        Query query = em.createQuery("SELECT p FROM Product p WHERE p.status='A'");
        return query.getResultList();
    }
    
    //Avoid infinite json loop for web service call
    @Override
    public List<Product> Ws_retrieveActiveProductOrderById(){
        Query query = em.createQuery("SELECT p FROM Product p WHERE p.status='A'");
        List<Product> ps = query.getResultList();
        for(Product p : ps){
            p.setProductReviews(new ArrayList<ProductReview>());
            p.setProductsPurchase(new ArrayList<ProductPurchase>());
        }
        
        return ps;
    }

    @Override
    public Product retrieveSingleProduct(long productId) {
        Query query = em.createQuery("SELECT p FROM Product p WHERE p.id=:productId");
        query.setParameter("productId", productId);
        return (Product) query.getResultList().get(0);
    }

    @Override
    public List<Product> retrieveProductsByTeam(String team) {
        Query query = em.createQuery("SELECT p FROM Product p WHERE p.team=:team AND p.status='A' ORDER BY p.team");
        query.setParameter("team", team);
        return query.getResultList();
    }

    @Override
    public List<Product> retrieveProductsByCountry(String country) {
        Query query = em.createQuery("SELECT p FROM Product p WHERE p.country=:country  AND p.status='A' ORDER BY p.team");
        query.setParameter("country", country);
        return query.getResultList();
    }

    @Override
    public List<List<String>> retrieveCountriesAndTeams() {
        List<List<String>> countryAndTeamList = new ArrayList<List<String>>();

        Query query = em.createQuery("SELECT p.country,p.team FROM Product p WHERE p.status='A' ORDER BY p.country, p.team");
        List<Object[]> results = query.getResultList();

        for (Object[] o : results) {
            boolean isCountryIn = false;
            boolean isTeamIn = true;
            String country = (String) o[0];
            String team = (String) o[1];

            for (int i = 0; i < countryAndTeamList.size(); i++) {
                if (countryAndTeamList.get(i).indexOf(country) == 0) {
                    isCountryIn = true;
                    if (countryAndTeamList.get(i).indexOf(team) < 0) {
                        isTeamIn = false;
                        countryAndTeamList.get(i).add(team);
                    }
                }
            }

            if (isCountryIn == false) {
                List<String> newEntry = new ArrayList<String>();
                newEntry.add(country);
                newEntry.add(team);
                countryAndTeamList.add(newEntry);
            }
        }

        return countryAndTeamList;
    }

    // Gets a list of products with sizes quantity <=10 , does not return sizes with >10
    @Override
    public List<Product> retrieveProductsRunningLow() {
        List<Product> allProductList = retrieveActiveProductOrderById();

        //Using iterator as forloop cannot remove sizes while looping through it
        System.out.println("******************************************Begin retrieve products running low");

        Iterator<Product> i = allProductList.iterator();
        while (i.hasNext()) {
            Product p = i.next();
            System.out.println("Product Id : " + p.getId() + " Name: " + p.getProductName() + " Sizes: " + p.getSizes().size());

            Iterator<ProductSize> is = p.getSizes().iterator();
            while (is.hasNext()) {
                ProductSize ps = is.next();

                System.out.println("Size: " + ps.getSize() + " Qty: " + ps.getQty());
                if (ps.getQty() > 10) {
                    is.remove();
                }
            }
        }
        
        Iterator<Product> i2 = allProductList.iterator();
        while(i2.hasNext()){
            Product p = i2.next();
            if(p.getSizes().size() <=0){
                i2.remove();
            }
        }

        return allProductList;
    }

    @Override
    public void updateProduct(Product newProductInfo) {
        //getting product ID
        
        for(Images productImage : newProductInfo.getImages()){
            System.out.println(productImage.getImagePath());
            em.persist(productImage);
            em.flush();
            em.refresh(productImage);
        }
        
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

    @Override
    public void deleteProduct(Product product) {
        Product toBeDeleted = retrieveSingleProduct(product.getId());
        toBeDeleted.setStatus("I");
        em.flush();
    }

    @Override
    public List<Product> getAllProducts() {
        Query query = em.createQuery("SELECT p FROM Product p WHERE p.status='A' ORDER BY p.team");
          System.out.println("Product Controller getAllPRoducts retrieve: " + query.getResultList().size());
        return query.getResultList();
    }

}

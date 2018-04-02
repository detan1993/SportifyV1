/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.customer.managedbean;

import ejb.session.stateless.ProductControllerLocal;
import entity.Product;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author JiongYi
 */
@Named(value = "customerCheckOutManagedBean")
@SessionScoped
public class CustomerCheckOutManagedBean implements Serializable{

    
    @EJB
    private ProductControllerLocal productcontroller;
    
    private List<Product> a ;
    
    public CustomerCheckOutManagedBean(){
        a= new ArrayList<>();
    }
    
    @PostConstruct
    public void postConstruct()
    {
        setA(productcontroller.retrieveProduct());
        
    }
    
//    public List<Product>retrieveProducts(){
//        List<Product>productList = new ArrayList();
//        productList = productcontroller.retrieveProduct();
//        return productList;
//    }
//    

    /**
     * @return the a
     */
    public List<Product> getA() {
        return a;
    }

    /**
     * @param a the a to set
     */
    public void setA(List<Product> a) {
        this.a = a;
    }
}

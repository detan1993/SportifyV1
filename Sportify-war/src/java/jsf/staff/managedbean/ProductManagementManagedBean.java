/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.staff.managedbean;

import ejb.session.stateless.ProductControllerLocal;
import entity.Product;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author David
 */
@Named(value = "productManagementManagedBean")
@ViewScoped
public class ProductManagementManagedBean implements Serializable{

    @EJB(name = "ProductControllerLocal")
    private ProductControllerLocal productControllerLocal;

    /**
     * Creates a new instance of ProductManagementManagedBean
     */
    
    
    
    
    private List<Product> products;
    private List<Product> lowStockProducts;
    private Product newProduct;
    private Product selectedProductsToView;
    private Product selectedProductsToUpdate;
    private List<Product> filteredProducts;
    private List<Product> filteredLowStockProducts; //this might not be needed. 

    
    public ProductManagementManagedBean() {
    
        products = new ArrayList<>();
        lowStockProducts = new ArrayList<>();
        newProduct = new Product();
        filteredProducts = new ArrayList<>();
        filteredLowStockProducts = new ArrayList<>();
    }
    
    
    @PostConstruct
    public void postConstruct(){
        
        //retrieve list of products
        products = productControllerLocal.retrieveProduct();
        
        //put the products into the filter list . This is for the facelet view 
        filteredProducts = products;
        lowStockProducts = productControllerLocal.retrieveProductsRunningLow();
        filteredLowStockProducts =  lowStockProducts;
        
    }
    
    

    public void createNewProduct(ActionEvent event)
    {
        try
        {
            Product newProductRecord = productControllerLocal.CreateNewProduct(newProduct);
            //add newly created product to products and filter list.
            products.add(newProductRecord);
            filteredProducts.add(newProductRecord);
            
            //destroy the new product data
            newProduct = new Product();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New product created successfully (Product ID: " + newProductRecord.getId() + ")", null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new product: " + ex.getMessage(), null));
        }
    }
    
    
    
    public void updateProduct(ActionEvent event)
    {
        
        try{
            
            productControllerLocal.updateProduct(selectedProductsToUpdate);
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New product created successfully (Product ID: " + selectedProductsToUpdate.getId() + ")", null));
            
        }catch(Exception ex){
              FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new product: " + ex.getMessage(), null));
        }
        
    
    }
    

    /**
     * @return the products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * @param products the products to set
     */
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    /**
     * @return the newProduct
     */
    public Product getNewProduct() {
        return newProduct;
    }

    /**
     * @param newProduct the newProductEntity to set
     */
    public void setNewProductEntity(Product newProduct) {
        this.newProduct = newProduct;
    }

    /**
     * @return the selectedProductsToView
     */
    public Product getSelectedProductsToView() {
        return selectedProductsToView;
    }

    /**
     * @param selectedProductsToView the selectedProductsToView to set
     */
    public void setSelectedProductsToView(Product selectedProductsToView) {
        this.selectedProductsToView = selectedProductsToView;
    }

    /**
     * @return the selectedProductsToUpdate
     */
    public Product getSelectedProductsToUpdate() {
        return selectedProductsToUpdate;
    }

    /**
     * @param selectedProductsToUpdate the selectedProductsToUpdate to set
     */
    public void setSelectedProductsToUpdate(Product selectedProductsToUpdate) {
        this.selectedProductsToUpdate = selectedProductsToUpdate;
    }

    /**
     * @return the filteredProducts
     */
    public List<Product> getFilteredProducts() {
        return filteredProducts;
    }

    /**
     * @param filteredProducts the filteredProducts to set
     */
    public void setFilteredProducts(List<Product> filteredProducts) {
        this.filteredProducts = filteredProducts;
    }

    /**
     * @return the lowStockProducts
     */
    public List<Product> getLowStockProducts() {
        return lowStockProducts;
    }

    /**
     * @param lowStockProducts the lowStockProducts to set
     */
    public void setLowStockProducts(List<Product> lowStockProducts) {
        this.lowStockProducts = lowStockProducts;
    }

    /**
     * @return the filteredLowStockProducts
     */
    public List<Product> getFilteredLowStockProducts() {
        return filteredLowStockProducts;
    }

    /**
     * @param filteredLowStockProducts the filteredLowStockProducts to set
     */
    public void setFilteredLowStockProducts(List<Product> filteredLowStockProducts) {
        this.filteredLowStockProducts = filteredLowStockProducts;
    }
    
}

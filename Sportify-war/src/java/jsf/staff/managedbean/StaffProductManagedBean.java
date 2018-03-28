/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.staff.managedbean;

import ejb.session.stateless.ProductControllerLocal;
import ejb.session.stateless.ProductSizeControllerLocal;
import entity.Product;
import entity.ProductSize;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author shanw
 */
@Named(value = "staffProductManagedBean")
@ViewScoped
public class StaffProductManagedBean  implements Serializable{

    @EJB(name = "ProductControllerLocal")
    private ProductControllerLocal productControllerLocal;
    
    @EJB(name = "ProductSizeControllerLocal")
    private ProductSizeControllerLocal productSizeControllerLocal;
    
    private List<Product> products;
    private List<Product> lowStockProducts;
    private Product newProduct;
    private Product selectedProductsToView;
    private Product selectedProductsToUpdate;
    private Product selectedProductToDelete;
    private List<Product> filteredProducts;
    private List<Product> filteredLowStockProducts; //this might not be needed. 
    
    public StaffProductManagedBean() {
        products = new ArrayList<>();
        lowStockProducts = new ArrayList<>();
        newProduct = new Product();
        filteredProducts = new ArrayList<>();
        filteredLowStockProducts = new ArrayList<>();
    }
    
    
    @PostConstruct
    public void postConstruct(){
        
        //retrieve list of products
        products = productControllerLocal.retrieveProductIncludingInactive();
        newProduct = new Product();
        ProductSize s = new ProductSize("",0);
        newProduct.getSizes().add(s);
        
        System.out.println("POST CONSTRUCT: " + newProduct.getSizes().size());
        
        //put the products into the filter list . This is for the facelet view 
        filteredProducts = products;
        lowStockProducts = productControllerLocal.retrieveProductsRunningLow();
        filteredLowStockProducts =  lowStockProducts;
        
    }
    
    public void createNewProduct(ActionEvent event){
        try
        {
            for(ProductSize ps : newProduct.getSizes()){
                productSizeControllerLocal.createSizeForProduct(ps);
            }
            
            Product newProductRecord = productControllerLocal.CreateNewProduct(newProduct);
            products.add(newProductRecord);
            filteredProducts.add(newProductRecord);
            
            newProduct = new Product();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New product created successfully (Product ID: " + newProductRecord.getId() + ")", null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new product: " + ex.getMessage(), null));
        }
    }
    public void updateProduct(){
        try{
            
            productControllerLocal.updateProduct(selectedProductsToView);
            for(ProductSize ps : selectedProductsToView.getSizes()){
                productSizeControllerLocal.updateSizeForProduct(ps);
            }
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product updated successfully (Product ID: " + selectedProductsToView.getId() + ")", null));
            
        }catch(Exception ex){
              FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating the product: " + ex.getMessage(), null));
        }
    }  
    public void handleFileUpload(FileUploadEvent event) {
        
        /*
        PROF METHOD
        
        try
        {
            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1") + System.getProperty("file.separator") + event.getFile().getFileName();

            System.err.println("********** Demo03ManagedBean.handleFileUpload(): File name: " + event.getFile().getFileName());
            System.err.println("********** Demo03ManagedBean.handleFileUpload(): newFilePath: " + newFilePath);

            File file = new File(newFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = event.getFile().getInputstream();

            while (true)
            {
                a = inputStream.read(buffer);

                if (a < 0)
                {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,  "File uploaded successfully", ""));
        }
        catch(IOException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  "File upload error: " + ex.getMessage(), ""));
        }
        
        */
        
        /*
        Original method
        
        try{
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String name = fmt.format(new Date())+ event.getFile().getFileName().substring(event.getFile().getFileName().lastIndexOf('.'));
            
            
            String relativePath="/resources/images/";
          String absolutePath=   FacesContext.getCurrentInstance().getExternalContext().getRealPath(relativePath);
            System.out.println(absolutePath);
            File file = new File(absolutePath + name);
            
            
            
            InputStream is = event.getFile().getInputstream();
            OutputStream out = new FileOutputStream(file);
            byte buf[] = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0)
                out.write(buf, 0, len);
            is.close();
            out.close();
            
            FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        
        }catch(Exception ex){
            ex.printStackTrace();
            FacesMessage message = new FacesMessage("Error", event.getFile().getFileName() + " has some problems.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }*/
    } 
    public void handleFileUpload_create(FileUploadEvent event){
        try{
            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1") + System.getProperty("file.separator") + event.getFile().getFileName();
            System.out.println("File Path: " + newFilePath);

            File file = new File(newFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = event.getFile().getInputstream();

            while (true)
            {
                a = inputStream.read(buffer);

                if (a < 0)
                {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();
            
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,  "File uploaded successfully", ""));
        }catch(Exception ex){
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  "File upload error: " + ex.getMessage(), ""));
        }
        
    }
    public void deleteProduct(){
        System.out.println("To delete Product id: " + selectedProductToDelete.getId());
        System.out.println("Before deleting size is : " + products.size());
        try{
            productControllerLocal.deleteProduct(selectedProductToDelete);
            products = productControllerLocal.retrieveProductIncludingInactive();
            System.out.println("After deleting size is : " + products.size());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product deleted successfully", null));
        }catch(Exception ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting the product: " + ex.getMessage(), null));
        }
    }
    
    
    public void addProductSize_create(ActionEvent event){
        System.out.println("Total Sizes: " + newProduct.getSizes().size());
        newProduct.getSizes().add(new ProductSize());
    }
    public void addProductSize_update(ActionEvent event){
        System.out.println("Total Sizes: " + selectedProductsToView.getSizes().size());
        selectedProductsToView.getSizes().add(new ProductSize());
    }
    public void removeProductSize_create(int index){
        newProduct.getSizes().remove(index);
    }
    public void removeProductSize_update(int index){
        selectedProductsToView.getSizes().remove(index);
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
    public void setNewProduct(Product newProduct) {
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
     * @return the selectedProductToDelete
     */
    public Product getSelectedProductToDelete() {
        return selectedProductToDelete;
    }

    
    /**
     * @param selectedProductToDelete the selectedProductToDelete to set
     */
    public void setSelectedProductToDelete(Product selectedProductToDelete) {
        this.selectedProductToDelete = selectedProductToDelete;
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

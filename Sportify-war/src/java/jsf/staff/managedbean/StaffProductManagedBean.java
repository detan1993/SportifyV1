/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.staff.managedbean;

import ejb.session.stateless.ProductControllerLocal;
import ejb.session.stateless.ProductSizeControllerLocal;
import entity.Images;
import entity.Product;
import entity.ProductPurchase;
import entity.ProductReview;
import entity.ProductSize;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

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
    
    private List<UploadedFile> create_imageList;
    private List<UploadedFile> update_imageList;
    
    public StaffProductManagedBean() {
        products = new ArrayList<>();
        lowStockProducts = new ArrayList<>();
        newProduct = new Product();
        filteredProducts = new ArrayList<>();
        create_imageList = new ArrayList<UploadedFile>();
        update_imageList = new ArrayList<UploadedFile>();
    }
    
    
    @PostConstruct
    public void postConstruct(){
        
        //retrieve list of products
        products = productControllerLocal.retrieveProductIncludingInactive();
        System.out.println("PRoducts retrieved: " + products.size());
        newProduct = new Product();
        ProductSize s = new ProductSize("",0);
        newProduct.getSizes().add(s);
        
        System.out.println("POST CONSTRUCT: " + newProduct.getSizes().size());
        
        //put the products into the filter list . This is for the facelet view 
        filteredProducts = products;
        lowStockProducts = productControllerLocal.retrieveProductsRunningLow();
        
        create_imageList = new ArrayList<UploadedFile>();
        update_imageList = new ArrayList<UploadedFile>();

        
    }
    
    public void createNewProduct(ActionEvent event){
        try
        {
            System.out.println("Create product starting");
            if(validateProduct(newProduct)){
                newProduct.setImages(new ArrayList<Images>());
                for(ProductSize ps : newProduct.getSizes()){
                    productSizeControllerLocal.createSizeForProduct(ps);
                }
                
                
                System.out.println("Number of images: " + create_imageList.size());
                for(UploadedFile f : create_imageList){
                    String filePath = handleFileUpload(f);
                    Images newImage = new Images();
                    newImage.setImagePath(filePath);
                    newProduct.getImages().add(newImage);
                    System.out.println(newImage.getImagePath());
                }
            
                newProduct.setDateCreated(new Date());
                newProduct.setGender("M");
                newProduct.setProductReviews(new ArrayList<ProductReview>());
                newProduct.setProductsPurchase(new ArrayList<ProductPurchase>());
                Product newProductRecord = productControllerLocal.CreateNewProduct(newProduct);
                products.add(newProductRecord);

               
                create_imageList = new ArrayList<UploadedFile>();
                lowStockProducts = productControllerLocal.retrieveProductsRunningLow();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New product created successfully (Product ID: " + newProductRecord.getId() + ")", null));
                 newProduct = new Product();
            }else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter Valid Product Size ", null));
            }
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new product: " + ex.getMessage(), null));
        }
    }
    public void updateProduct(){
        try{
            System.out.println("Update product started");
            if(validateProduct(selectedProductsToView)){
                
                 for(ProductSize ps : selectedProductsToView.getSizes()){
                    System.out.println(ps.getId() + " " + ps.getSize() + " " + ps.getQty());
                    productSizeControllerLocal.updateSizeForProduct(ps);
                }

                selectedProductsToView.setImages(new ArrayList<Images>());
                handleFileDelete(selectedProductsToView);
                System.out.println("Number of images: " + update_imageList.size());
                for(UploadedFile f : update_imageList){
                    String filePath = handleFileUpload(f);
                    Images newImage = new Images();
                    newImage.setImagePath(filePath);
                    selectedProductsToView.getImages().add(newImage);
                    System.out.println(newImage.getImagePath());
                }
                
                productControllerLocal.updateProduct(selectedProductsToView);
               
                
                update_imageList =  new ArrayList<UploadedFile>();
                
                products = productControllerLocal.retrieveProductIncludingInactive();
                lowStockProducts = productControllerLocal.retrieveProductsRunningLow();
                FacesContext.getCurrentInstance().addMessage("update_messages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Product updated successfully (Product ID: " + selectedProductsToView.getId() + ")", null));
            }else{
                System.out.println("Update validation failed");
                  FacesContext.getCurrentInstance().addMessage("update_messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter Valid Product Size ",null));
            }
           
            
        }catch(Exception ex){
              FacesContext.getCurrentInstance().addMessage("update_messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating the product: " + ex.getMessage(), null));
        }
    }  
    
    public String handleFileUpload(UploadedFile uploadedFile){
        try{
            //System.out.println(FacesContext.getCurrentInstance().getExternalContext().getRealPath("//resources//images") + "//");
            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1") + "/" + uploadedFile.getFileName();
           // String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getResource("//resources//images").toString();
           //String newFilePath = this.getClass().getResource("/images").toString();
           System.out.println("File Path: " + newFilePath);

            File file = new File(newFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = uploadedFile.getInputstream();

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
            
        System.out.println("File on upload click: " + newFilePath);
        newFilePath = "/staffUploadedFiles/" + uploadedFile.getFileName();
        //System.out.println("File on upload click: " + newFilePath);
            return newFilePath;
        }catch(Exception ex){
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,  "File upload error: " + ex.getMessage(), ""));
            return null;
        }
        
    }
    public void handleFileUpload_Upload(FileUploadEvent event){
        System.out.println("upload_upload()");
        create_imageList.add(event.getFile());
        System.out.println("Upload_upload: " + create_imageList.size());
    }
    public void handleFileUpload_UploadUpdate(FileUploadEvent event){
        System.out.println("upload_uploadUpdate()");
        update_imageList.add(event.getFile());
        System.out.println("upload_uploadUpdate: " + update_imageList.size());
    }
    public void handleFileDelete(Product updateProduct){
        
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
    
    public String calculateAverageRating(Product p){
        System.out.println("Start calculating");
        double averageRating = 0;
        double totalRating =0;
        double totalReviews =0;
        for(ProductReview r : p.getProductReviews()){
            //System.out.println("Review: " + r.getRating());
            totalRating +=r.getRating();
            totalReviews++;
        }
        
        
        averageRating = totalRating/totalReviews;
        //System.out.println("Total Rating: " + totalRating ); 
        //System.out.println("Total Review: " + totalReviews); 
        //System.out.println("Average Rating: " + averageRating ); 
        
        if(totalRating ==0){
            return "Nil";
        }
        
        return String.valueOf(averageRating);
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
    
    public boolean validateProduct(Product p){
        boolean isValidSize = true;
        for(ProductSize s : p.getSizes()){
            System.out.println("Size: " + s.getSize());
            if(s.getSize().length() <=0){
                System.out.println("Size:" + s.getSize() + " " + s.getQty() + " has length<=0");
                isValidSize = false;
                break;
            }
        }
      
        System.out.println("Valid: " + isValidSize);
        return isValidSize;
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

    public List<UploadedFile> getCreate_imageList() {
        return create_imageList;
    }

    public void setCreate_imageList(List<UploadedFile> create_imageList) {
        this.create_imageList = create_imageList;
    }
    
    
    
}

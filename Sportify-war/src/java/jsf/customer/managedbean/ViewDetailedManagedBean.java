package jsf.customer.managedbean;

import ejb.session.stateless.ProductControllerLocal;
import entity.Images;
import entity.Product;
import entity.ProductSize;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;

@Named(value = "viewDetailedManagedBean")
@ViewScoped
public class ViewDetailedManagedBean implements Serializable{

    @EJB
    private ProductControllerLocal productController;

    private Product product;
    private List<String> images;
    private String selectedImage;
    private List<ProductSize> sizesAvailable;

    @PostConstruct
    public void postConstruct() {
        
        product = new Product();
        images = new ArrayList<>();
        setSizesAvailable(new ArrayList<>());
        
        List<Images> getProdImages = new ArrayList<Images>();
        
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String productId = request.getParameter("productId");
        
        
        try {
            long productIdLong = Long.parseLong(productId);
            if (productId != null && !productId.equals("")) {
                product = productController.retrieveSingleProduct(productIdLong);
                
                 //get product sizes for this product
                setSizesAvailable(product.getSizes());
                
                //get images for this product
                if (product != null){
                    if (product.getImages() != null){
                        getProdImages = product.getImages();
                        
                        int skipFirstImage = 0;
                        for (Images image: getProdImages){
                            if (skipFirstImage == 0){}
                            else{images.add(image.getImagePath());}
                           skipFirstImage++;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving Product Id. " + ex.getMessage(), null));
        }
    }
    
    public void showSelectedImage(){
        
    }

    public ViewDetailedManagedBean() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(String selectedImage) {
        this.selectedImage = selectedImage;
    }

    public List<ProductSize> getSizesAvailable() {
        return sizesAvailable;
    }

    public void setSizesAvailable(List<ProductSize> sizesAvailable) {
        this.sizesAvailable = sizesAvailable;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.customer.managedbean;

import ejb.session.stateless.ProductControllerLocal;
import entity.Product;
import entity.ProductReview;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author JiongYi
 */
@Named(value = "writeReviewManagedBean")
@ViewScoped
public class CustomerWriteReviewManagedBean implements Serializable {

    @EJB
    private ProductControllerLocal productController;
    private List<ProductReview>productReviews;
    private Integer productrating;
    private String productreview;
    
    @PostConstruct
    public void postConstruct() {
        
        
    }
    
   public void submitReview(){
       
   }
    /**
     * @return the productReviews
     */
    public List<ProductReview> getProductReviews() {
        return productReviews;
    }

    /**
     * @param productReviews the productReviews to set
     */
    public void setProductReviews(List<ProductReview> productReviews) {
        this.productReviews = productReviews;
    }

    /**
     * @return the productrating
     */
    public Integer getProductrating() {
        return productrating;
    }

    /**
     * @param productrating the productrating to set
     */
    public void setProductrating(Integer productrating) {
        this.productrating = productrating;
    }

    /**
     * @return the productreview
     */
    public String getProductreview() {
        return productreview;
    }

    /**
     * @param productreview the productreview to set
     */
    public void setProductreview(String productreview) {
        this.productreview = productreview;
    }
}

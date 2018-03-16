/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author shanw
 */
@Entity
public class ProductReview implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private double rating;
    private String review;
    @Temporal(TemporalType.TIMESTAMP)
    private Date reviewDate;
    
    private String staffResponse;
    @Temporal(TemporalType.TIMESTAMP)
    private Date staffResponseDate;
    
    @ManyToOne
    private Product product;
    @ManyToOne
    private CustomerOrder customerOrder;

    public ProductReview() {
    }

    public ProductReview(double rating, String review, Date reviewDate, String staffResponse, Date staffResponseDate, Product product, CustomerOrder customerOrder) {
        this.rating = rating;
        this.review = review;
        this.reviewDate = reviewDate;
        this.staffResponse = staffResponse;
        this.staffResponseDate = staffResponseDate;
        this.product = product;
        this.customerOrder = customerOrder;
    }

   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getStaffResponse() {
        return staffResponse;
    }

    public void setStaffResponse(String staffResponse) {
        this.staffResponse = staffResponse;
    }

    public Date getStaffResponseDate() {
        return staffResponseDate;
    }

    public void setStaffResponseDate(Date staffResponseDate) {
        this.staffResponseDate = staffResponseDate;
    }
    
    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductReview)) {
            return false;
        }
        ProductReview other = (ProductReview) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ProductReview[ id=" + id + " ]";
    }
    
}

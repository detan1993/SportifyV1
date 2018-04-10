/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author shanw
 */
@Entity
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String productCode;
    private String productName;
    @Column (length = 4000)
    private String description;
    private double price;
    private String team;
    private String gender;
    private String country;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    private String status;
    
    @OneToMany
    private List<ProductSize> sizes;
    
    @OneToMany(mappedBy="product")
    private List<ProductReview> productReviews;
    
    @OneToMany(mappedBy="productPurchase")
    private List<ProductPurchase> productsPurchase;
    
    @OneToMany
    private List<Images> images;

    public Product() {
        this.sizes = new ArrayList<ProductSize>();
        this.images = new ArrayList<Images>();
        this.status = "A";
    }

    public Product(String productCode, String productName, String description, double price, String team, String gender, String country, Date dateCreated, List<ProductReview> productReviews, List<Images> images, List<ProductSize> sizes) {
        this.productCode = productCode;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.team = team;
        this.gender = gender;
        this.country = country;
        this.dateCreated = dateCreated;
        this.sizes = sizes;
        this.productReviews = productReviews;
        
        this.images = images;
        this.status="A";
    }

   

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public List<Images> getImages() {
        return images;
    }

    public void setImages(List<Images> images) {
        this.images = images;
    }

    public List<ProductReview> getProductReviews() {
        return productReviews;
    }

    public void setProductReviews(List<ProductReview> productReviews) {
        this.productReviews = productReviews;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Product[ id=" + id + " ]";
    }

    /**
     * @return the sizes
     */
    public List<ProductSize> getSizes() {
        return sizes;
    }

    /**
     * @param sizes the sizes to set
     */
    public void setSizes(List<ProductSize> sizes) {
        this.sizes = sizes;
    }

    /**
     * @return the productsPurchase
     */
    public List<ProductPurchase> getProductsPurchase() {
        return productsPurchase;
    }

    /**
     * @param productsPurchase the productsPurchase to set
     */
    public void setProductsPurchase(List<ProductPurchase> productsPurchase) {
        this.productsPurchase = productsPurchase;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author shanw
 */
@Entity
public class CustomerOrder implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private double totalAmount;
    private double totalPointsAwarded;
    @Temporal(TemporalType.TIMESTAMP)
    private Date datePaid;
    private String deliveryStatus;

    @ManyToOne
    private Customer customer;
    @OneToOne
    private CustomerVoucher customerVoucher;
    @OneToMany(mappedBy="customerOrder")
    private List<ProductReview> productReviews;
    @ManyToMany
    private List<Product> products;

    public CustomerOrder() {
    }

    public CustomerOrder(double totalAmount, double totalPointsAwarded, Date datePaid, String deliveryStatus, Customer customer, CustomerVoucher customerVoucher, List<ProductReview> productReviews, List<Product> products) {
        this.totalAmount = totalAmount;
        this.totalPointsAwarded = totalPointsAwarded;
        this.datePaid = datePaid;
        this.deliveryStatus = deliveryStatus;
        this.customer = customer;
        this.customerVoucher = customerVoucher;
        this.productReviews = productReviews;
        this.products = products;
    }

    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTotalPointsAwarded() {
        return totalPointsAwarded;
    }

    public void setTotalPointsAwarded(double totalPointsAwarded) {
        this.totalPointsAwarded = totalPointsAwarded;
    }

    public Date getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(Date datePaid) {
        this.datePaid = datePaid;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    

    public CustomerVoucher getCustomerVoucher() {
        return customerVoucher;
    }

    public void setCustomerVoucher(CustomerVoucher customerVoucher) {
        this.customerVoucher = customerVoucher;
    }

    public List<ProductReview> getProductReviews() {
        return productReviews;
    }

    public void setProductReviews(List<ProductReview> productReviews) {
        this.productReviews = productReviews;
    }

    
    
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
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
        if (!(object instanceof CustomerOrder)) {
            return false;
        }
        CustomerOrder other = (CustomerOrder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CustomerOrder[ id=" + id + " ]";
    }
    
}

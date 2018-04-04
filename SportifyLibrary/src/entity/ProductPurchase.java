/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author David
 */
@Entity
public class ProductPurchase implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private double pricePurchase;
    private int qtyPurchase;
    
    @ManyToOne
    private CustomerOrder order;
    
    @ManyToOne
    private Product productPurchase;

    public ProductPurchase() {
    }

    
    
    
    public ProductPurchase(double pricePurchase, int qtyPurchase, CustomerOrder order, Product productPurchase) {
        this.pricePurchase = pricePurchase;
        this.qtyPurchase = qtyPurchase;
        this.order = order;
        this.productPurchase = productPurchase;
    }

    
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof ProductPurchase)) {
            return false;
        }
        ProductPurchase other = (ProductPurchase) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ProductPurchase[ id=" + id + " ]";
    }

    /**
     * @return the pricePurchase
     */
    public double getPricePurchase() {
        return pricePurchase;
    }

    /**
     * @param pricePurchase the pricePurchase to set
     */
    public void setPricePurchase(double pricePurchase) {
        this.pricePurchase = pricePurchase;
    }

    /**
     * @return the qtyPurchase
     */
    public int getQtyPurchase() {
        return qtyPurchase;
    }

    /**
     * @param qtyPurchase the qtyPurchase to set
     */
    public void setQtyPurchase(int qtyPurchase) {
        this.qtyPurchase = qtyPurchase;
    }

    /**
     * @return the order
     */
    public CustomerOrder getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(CustomerOrder order) {
        this.order = order;
    }

    /**
     * @return the productPurchase
     */
    public Product getProductPurchase() {
        return productPurchase;
    }

    /**
     * @param productPurchase the productPurchase to set
     */
    public void setProductPurchase(Product productPurchase) {
        this.productPurchase = productPurchase;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author David
 */
@XmlRootElement
@XmlType(name = "retrieveCustomerAccountRsp", propOrder = {
    "fullname" , "email" , "address" , "loyaltyPoint"
})

public class RetrieveCustomerAccountRsp implements Serializable {
    
     
    private static final long serialVersionUID = 1L;
    

    private String fullname;
    private String email;
    private String address;
    private int loyaltyPoint;

    public RetrieveCustomerAccountRsp(String fullname, String email, String address, int loyaltyPoint) {
        this.fullname = fullname;
        this.email = email;
        this.address = address;
        this.loyaltyPoint = loyaltyPoint;
    }


    public RetrieveCustomerAccountRsp() {
    }

   
    /**
     * @return the fullname
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * @param fullname the fullname to set
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the loyaltyPoint
     */
    public int getLoyaltyPoint() {
        return loyaltyPoint;
    }

    /**
     * @param loyaltyPoint the loyaltyPoint to set
     */
    public void setLoyaltyPoint(int loyaltyPoint) {
        this.loyaltyPoint = loyaltyPoint;
    }



}
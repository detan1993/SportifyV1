/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Afy
 */

@XmlRootElement
@XmlType(name = "AddNewCustomerReq", propOrder = {
   "firstName", "lastName", "phoneNum", "gender", "address", "zipCode", "dateOfBirth", "email", "password", "loyaltyPoints", "dateRegistered"
})

public class AddNewCustomerReq {
    
    private String firstName;
    private String lastName;
    private String phoneNum;
    private String gender;
    private String address;
    private String zipCode;
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    private String email;
    private String password;
    private int loyaltyPoints;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateRegistered;
    
    public AddNewCustomerReq(){
        
    }

    public AddNewCustomerReq(String firstName, String lastName, String phoneNum, String gender, String address, String zipCode, Date dateOfBirth, String email, String password, int loyaltyPoints, Date dateRegistered) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNum = phoneNum;
        this.gender = gender;
        this.address = address;
        this.zipCode = zipCode;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.password = password;
        this.loyaltyPoints = loyaltyPoints;
        this.dateRegistered = dateRegistered;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }
}

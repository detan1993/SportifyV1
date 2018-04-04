/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.helperClass;

import java.io.File;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


/**
 *
 * @author David
 */
public class TopTenCustomer {
    
    
    public static final Comparator<TopTenCustomer> BY_NO_PURCHASE = new compareByNoOfPurchase();
    public static final Comparator<TopTenCustomer> BY_AVG_PURCHASE = new  compareByAverage();
    public static final Comparator<TopTenCustomer> BY_TOTAL_PURCHASE  =  new compareByPurchase();
    private int rank;
    private String email;
    private String fullName;
    private double totalPurchase;
    private int totalNoOfPurchase;
    private String accountCreated;
    private int loyaltyPoint;
    private double averagePurchasePerTransaction;
    private List<TopCustomerProduct> totalProducts;

    public TopTenCustomer() {
    }

    public TopTenCustomer(String email, String fullName, double totalPurchase, int totalNoOfPurchase, String accountCreated, double averagePurchasePerTransaction, List<TopCustomerProduct> totalProducts) {
        this.email = email;
        this.fullName = fullName;
        this.totalPurchase = totalPurchase;
        this.totalNoOfPurchase = totalNoOfPurchase;
        this.accountCreated = accountCreated;
        this.averagePurchasePerTransaction = averagePurchasePerTransaction;
        this.totalProducts = totalProducts;
    }

    public TopTenCustomer(int rank, String email,double totalPurchase, int totalNoOfPurchase, double averagePurchasePerTransaction) {
        this.rank = rank;
        this.email = email;
        this.totalPurchase = totalPurchase;
        this.totalNoOfPurchase = totalNoOfPurchase;
        this.averagePurchasePerTransaction = averagePurchasePerTransaction;
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
     * @return the fullName
     */
    public String getFullName() {
        System.out.println("******** GETTING FULL NAME");
        return fullName;
    }

    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return the totalPurchase
     */
    public double getTotalPurchase() {
        return totalPurchase;
    }

    /**
     * @param totalPurchase the totalPurchase to set
     */
    public void setTotalPurchase(double totalPurchase) {
        this.totalPurchase = totalPurchase;
    }

    /**
     * @return the totalNoOfPurchase
     */
    public int getTotalNoOfPurchase() {
        return totalNoOfPurchase;
    }

    /**
     * @param totalNoOfPurchase the totalNoOfPurchase to set
     */
    public void setTotalNoOfPurchase(int totalNoOfPurchase) {
        this.totalNoOfPurchase = totalNoOfPurchase;
    }

    /**
     * @return the accountCreated
     */
    public String getAccountCreated() {
        return accountCreated;
    }

    /**
     * @param accountCreated the accountCreated to set
     */
    public void setAccountCreated(String accountCreated) {
        this.accountCreated = accountCreated;
    }

    /**
     * @return the averagePurchasePerTransaction
     */
    public double getAveragePurchasePerTransaction() {
        return averagePurchasePerTransaction;
    }

    /**
     * @param averagePurchasePerTransaction the averagePurchasePerTransaction to set
     */
    public void setAveragePurchasePerTransaction(double averagePurchasePerTransaction) {
        this.averagePurchasePerTransaction = averagePurchasePerTransaction;
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
    
    
//    @Override
//    public int compareTo(TopTenCustomer otherCustomer) {
//        int otherPurchase = otherCustomer.getTotalNoOfPurchase();
//
//        return otherPurchase - this.totalNoOfPurchase;
//    }

  @Override
  public String toString(){
      
      return " Email : " + this.email + " noPUrchase " + this.totalNoOfPurchase + " ";
  }

    /**
     * @return the rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * @param rank the rank to set
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * @return the totalProducts
     */
    public List<TopCustomerProduct> getTotalProducts() {
        return totalProducts;
    }

    /**
     * @param totalProducts the totalProducts to set
     */
    public void setTotalProducts(List<TopCustomerProduct> totalProducts) {
        this.totalProducts = totalProducts;
    }

  
   private static class compareByNoOfPurchase implements Comparator<TopTenCustomer> {
       
       @Override
       public int compare(TopTenCustomer custA, TopTenCustomer custB){
           // v.name is a String, and a String object is Comparable
           return custB.getTotalNoOfPurchase() - custA.getTotalNoOfPurchase();
        }
     }
   
   
   private static class compareByAverage implements Comparator<TopTenCustomer> {
       
       @Override
       public int compare(TopTenCustomer custA, TopTenCustomer custB){
           // v.name is a String, and a String object is Comparable
           return (int)custB.getAveragePurchasePerTransaction() - (int)custA.getAveragePurchasePerTransaction();
        }
     }
   
    private static class compareByPurchase implements Comparator<TopTenCustomer> {
       
       @Override
       public int compare(TopTenCustomer custA, TopTenCustomer custB){
           // v.name is a String, and a String object is Comparable
           return (int)custB.getTotalPurchase() - (int)custA.getTotalPurchase();
        }
     }
   

     
}

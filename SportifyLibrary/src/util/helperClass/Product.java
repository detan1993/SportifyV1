/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.helperClass;

/**
 *
 * @author David
 */
public class Product {
    
    private String teamName;
    private double totalPurchase;

    public Product() {
    }

    public Product(String teamName,double totalPurchase) {
        this.teamName = teamName;
        this.totalPurchase = totalPurchase;
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
     * @return the teamName
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     * @param teamName the teamName to set
     */
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

  
    
}

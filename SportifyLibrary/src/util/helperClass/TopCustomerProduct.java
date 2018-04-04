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
public class TopCustomerProduct {
    
     private String teamName;
    private int totalQtyPurchase;

    public TopCustomerProduct() {
    }

    public TopCustomerProduct(String teamName, int totalQtyPurchase) {
        this.teamName = teamName;
        this.totalQtyPurchase = totalQtyPurchase;
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

    /**
     * @return the totalQtyPurchase
     */
    public int getTotalQtyPurchase() {
        return totalQtyPurchase;
    }

    /**
     * @param totalQtyPurchase the totalQtyPurchase to set
     */
    public void setTotalQtyPurchase(int totalQtyPurchase) {
        this.totalQtyPurchase = totalQtyPurchase;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.helperClass;

import java.util.Comparator;

/**
 *
 * @author David
 */
public class TopProductByCode {
    
    public static final Comparator<TopProductByCode> BY_NO_PURCHASE = new compareByNoOfPurchase();
    public static final Comparator<TopProductByCode> BY_AVG_PURCHASE = new  compareByAverage();
    public static final Comparator<TopProductByCode> BY_TOTAL_PURCHASE  =  new  compareByPurchase();
    private int rank;
    private String productCode;
    private int quantityOrdered;
    private double amountPurchased;
    private double averageProfit;

    public TopProductByCode() {
    }

    public TopProductByCode(String productCode, int quantityOrdered, double amountPurchased) {
        this.productCode = productCode;
        this.quantityOrdered = quantityOrdered;
        this.amountPurchased = amountPurchased;
    }

    
    
    /**
     * @return the productCode
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * @param productCode the productCode to set
     */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    /**
     * @return the quantityOrdered
     */
    public int getQuantityOrdered() {
        return quantityOrdered;
    }

    /**
     * @param quantityOrdered the quantityOrdered to set
     */
    public void setQuantityOrdered(int quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
    }

    /**
     * @return the amountPurchased
     */
    public double getAmountPurchased() {
        return amountPurchased;
    }

    /**
     * @param amountPurchased the amountPurchased to set
     */
    public void setAmountPurchased(double amountPurchased) {
        this.amountPurchased = amountPurchased;
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
     * @return the averageProfit
     */
    public double getAverageProfit() {
        return averageProfit;
    }

    /**
     * @param averageProfit the averageProfit to set
     */
    public void setAverageProfit(double averageProfit) {
        this.averageProfit = averageProfit;
    }
    
      private static class compareByNoOfPurchase implements Comparator<TopProductByCode> {
       
       @Override
       public int compare(TopProductByCode prodA, TopProductByCode prodB){
           // v.name is a String, and a String object is Comparable
           return prodB.getQuantityOrdered() - prodA.getQuantityOrdered();
        }
     }
      
      
       private static class compareByAverage implements Comparator<TopProductByCode> {
       
       @Override
       public int compare(TopProductByCode prodA, TopProductByCode prodB){
           // v.name is a String, and a String object is Comparable
           return (int)prodB.getAverageProfit() - (int)prodA.getAverageProfit();
        }
     }
   
    private static class compareByPurchase implements Comparator<TopProductByCode> {
       
       @Override
       public int compare(TopProductByCode prodA, TopProductByCode prodB){
           // v.name is a String, and a String object is Comparable
             return (int)prodB.getAmountPurchased()- (int)prodA.getAmountPurchased();
        }
     }
   
    
    
}

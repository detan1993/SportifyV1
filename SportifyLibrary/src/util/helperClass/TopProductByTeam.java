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
public class TopProductByTeam {
    
    public static final Comparator<TopProductByTeam> BY_NO_PURCHASE = new compareByNoOfPurchase();
    public static final Comparator<TopProductByTeam> BY_AVG_PURCHASE = new compareByAverage();
    public static final Comparator<TopProductByTeam> BY_TOTAL_PURCHASE  =  new  compareByPurchase();
    private int rank;
    private String teamName;
    private int quantityOrdered;
    private double amountPurchased;
    private double averageProfit;

    public TopProductByTeam() {
    }

    public TopProductByTeam(String teamName, int quantityOrdered, double amountPurchased) {
        this.teamName = teamName;
        this.quantityOrdered = quantityOrdered;
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
    
    
          private static class compareByNoOfPurchase implements Comparator<TopProductByTeam> {
       
       @Override
       public int compare(TopProductByTeam prodA, TopProductByTeam prodB){
           // v.name is a String, and a String object is Comparable
           return prodB.getQuantityOrdered() - prodA.getQuantityOrdered();
        }
     }
      
      
       private static class compareByAverage implements Comparator<TopProductByTeam> {
       
       @Override
       public int compare(TopProductByTeam prodA, TopProductByTeam prodB){
           // v.name is a String, and a String object is Comparable
           return (int)prodB.getAverageProfit() - (int)prodA.getAverageProfit();
        }
     }
   
    private static class compareByPurchase implements Comparator<TopProductByTeam> {
       
       @Override
       public int compare(TopProductByTeam prodA, TopProductByTeam prodB){
           // v.name is a String, and a String object is Comparable
             return (int)prodB.getAmountPurchased()- (int)prodA.getAmountPurchased();
        }
     }
   

}

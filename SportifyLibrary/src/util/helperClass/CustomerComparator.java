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
public class CustomerComparator extends TopTenCustomer implements Comparable<CustomerComparator> {
    
    @Override
    public int compareTo(CustomerComparator otherCust){
        int other = otherCust.getTotalNoOfPurchase();
    
        return other - this.getTotalNoOfPurchase();
}
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerOrder;
import java.util.List;
import util.helperClass.TopTenCustomer;

/**
 *
 * @author shanw
 */
public interface CustomerOrderControllerLocal {
     public CustomerOrder CreateNewCustomerOrder(CustomerOrder newCustomerOrder);
     public List<CustomerOrder> RetrieveAllCustomerOrder();
     public List<TopTenCustomer> RetrieveTopTenCustomersByOrder();
     public List<TopTenCustomer> RetrieveTopTenCustomerByOrderByRanger(String dateFron, String dateTo);
 
}

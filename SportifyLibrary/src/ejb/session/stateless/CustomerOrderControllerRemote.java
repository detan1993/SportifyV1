/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerOrder;
import java.util.List;
import util.helperClass.TopTenCustomer;

public interface CustomerOrderControllerRemote {

    public CustomerOrder CreateNewCustomerOrder(CustomerOrder newCustomerOrder);

    public List<CustomerOrder> GetCustomerOrder(long customerId);

    public List<CustomerOrder> RetrieveAllCustomerOrder();

    public List<TopTenCustomer> RetrieveTopTenCustomersByOrder();

    public List<TopTenCustomer> RetrieveTopTenCustomerByOrderByRanger(String dateFron, String dateTo);
}

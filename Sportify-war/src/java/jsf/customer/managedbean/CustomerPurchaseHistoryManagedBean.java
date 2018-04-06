package jsf.customer.managedbean;

import ejb.session.stateless.CustomerOrderControllerRemote;
import entity.Customer;
import entity.CustomerOrder;
import entity.Product;
import entity.ProductPurchase;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "customerPurchaseHistoryManagedBean")
@ViewScoped
public class CustomerPurchaseHistoryManagedBean implements Serializable {

    @EJB
    private CustomerOrderControllerRemote customerOrderController;

    private List<CustomerOrder> getCustOrders;
    private Customer customer;
    private Map<CustomerOrder, List<ProductPurchase>> productOrderDetails = new HashMap();
    private Map<CustomerOrder, List<ProductPurchase>> productsOnHold = new HashMap();
    private Product product;

    @PostConstruct
    public void postConstruct() {

        customer = (Customer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
        getCustOrders = customerOrderController.GetCustomerOrder(customer.getId());

        //get past transactions
        for (CustomerOrder order : getCustOrders) {
            List<ProductPurchase> productPurchaseDetails = order.getProductPurchase();
            
            if (order.getDeliveryStatus().equalsIgnoreCase("Delivered")) {
                productOrderDetails.put(order, productPurchaseDetails);
            } else {
                productsOnHold.put(order, productPurchaseDetails);
            }
        }
    }

    public String shopNowRedirect() {
        return "products?faces-redirect=true";
    }

    public CustomerPurchaseHistoryManagedBean() {
    }

    public List<CustomerOrder> getGetCustOrders() {
        return getCustOrders;
    }

    public void setGetCustOrders(List<CustomerOrder> getCustOrders) {
        this.getCustOrders = getCustOrders;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Map<CustomerOrder, List<ProductPurchase>> getProductOrderDetails() {
        return productOrderDetails;
    }

    public void setProductOrderDetails(Map<CustomerOrder, List<ProductPurchase>> productOrderDetails) {
        this.productOrderDetails = productOrderDetails;
    }

    public Map<CustomerOrder, List<ProductPurchase>> getProductsOnHold() {
        return productsOnHold;
    }

    public void setProductsOnHold(Map<CustomerOrder, List<ProductPurchase>> productsOnHold) {
        this.productsOnHold = productsOnHold;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

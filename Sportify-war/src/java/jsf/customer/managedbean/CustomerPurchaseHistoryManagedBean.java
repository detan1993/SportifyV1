package jsf.customer.managedbean;

import ejb.session.stateless.CustomerOrderControllerLocal;
import ejb.session.stateless.ProductReviewControllerLocal;
import entity.Customer;
import entity.CustomerOrder;
import entity.Product;
import entity.ProductPurchase;
import entity.ProductReview;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;

@Named(value = "customerPurchaseHistoryManagedBean")
@ViewScoped
public class CustomerPurchaseHistoryManagedBean implements Serializable {

    @EJB
    private CustomerOrderControllerLocal customerOrderController;

    @EJB
    private ProductReviewControllerLocal productReviewController;

    private List<CustomerOrder> getCustOrders;
    private Customer customer;
    private Map<CustomerOrder, List<ProductPurchase>> productOrderDetails = new HashMap();
    private Map<CustomerOrder, List<ProductPurchase>> productsOnHold = new HashMap();
    private Product product;
    private ProductReview productReview;
    private int tabindex;
    private boolean dialogVisible;

    @PostConstruct
    public void postConstruct() {
        setDialogVisible(false);
        setTabindex(0);
        customer = (Customer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");
        System.out.println("Customer id : " + customer.getId());

        try {
            getCustOrders = customerOrderController.GetCustomerOrder(customer.getId());

            //get past transactions 
            if (getCustOrders != null) {
                for (CustomerOrder order : getCustOrders) {
                    List<ProductPurchase> productPurchaseDetails = order.getProductPurchase();

                    if (order.getDeliveryStatus().equalsIgnoreCase("Delivered")) {
                        productOrderDetails.put(order, productPurchaseDetails);
                    } else {
                        productsOnHold.put(order, productPurchaseDetails);
                    }
                }
            }
        } catch (Exception ex) {

        }
        //productReview = productReviewController.getProductReview();
        try {
            int tab = (Integer) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("tab");
            setTabindex(tab);
            dialogVisible = true;
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            Map<String, Object> sessionMap = externalContext.getSessionMap();
            sessionMap.remove("currentItemCart");
            //RequestContext.getCurrentInstance().update("mytabview:transcurr");
        } catch (Exception ex) {

        }

    }

    public void loadPrds() {
        customer = (Customer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentCustomer");

        try {
            getCustOrders = customerOrderController.GetCustomerOrder(customer.getId());
            //get past transactions 
            if (getCustOrders != null) {
                for (CustomerOrder order : getCustOrders) {
                    List<ProductPurchase> productPurchaseDetails = order.getProductPurchase();

                    if (order.getDeliveryStatus().equalsIgnoreCase("Delivered")) {
                        productOrderDetails.put(order, productPurchaseDetails);
                    } else {
                        productsOnHold.put(order, productPurchaseDetails);
                    }
                }
            }
        } catch (Exception ex) {

        }
    }

    public String retrieveCustomerOrderProductReview(long productId, long customerOrderId) {
        String productReviewStr = productReviewController.retrieveCustomerOrderProductReview(productId, customerOrderId);
        return productReviewStr;
//        if (productReview == null){
//            return "";
//        }
//        System.out.println("MY REVIEW: " + productReview.getReview());
//        return productReview.getReview();
    }

    public int retrieveCustomerOrderProductRating(long productId, long customerOrderId) {
        int productRatingInt = productReviewController.retrieveCustomerOrderProductRating(productId, customerOrderId);
        return productRatingInt;
//        if (productReview == null){
//            return 0;
//        }
//        System.out.println("RATING: " + productReview.getRating());
//        return productReview.getRating();
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

    public ProductReview getProductReview() {
        return productReview;
    }

    public void setProductReview(ProductReview productReview) {
        this.productReview = productReview;
    }

    /**
     * @return the tabindex
     */
    public int getTabindex() {
        return tabindex;
    }

    /**
     * @param tabindex the tabindex to set
     */
    public void setTabindex(int tabindex) {
        this.tabindex = tabindex;
    }

    /**
     * @return the dialogVisible
     */
    public boolean isDialogVisible() {
        return dialogVisible;
    }

    /**
     * @param dialogVisible the dialogVisible to set
     */
    public void setDialogVisible(boolean dialogVisible) {
        this.dialogVisible = dialogVisible;
    }
}

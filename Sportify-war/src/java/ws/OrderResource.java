/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import ejb.session.stateless.CustomerControllerLocal;
import ejb.session.stateless.CustomerOrderControllerLocal;
import ejb.session.stateless.CustomerVoucherControllerLocal;
import ejb.session.stateless.ProductControllerLocal;
import ejb.session.stateless.ProductPurchaseControllerLocal;
import ejb.session.stateless.VoucherControllerLocal;
import entity.Customer;
import entity.CustomerOrder;
import entity.CustomerVoucher;
import entity.Product;
import entity.ProductPurchase;
import entity.Voucher;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import ws.datamodel.AddCustomerOrderReq;

/**
 *
 * @author shanw
 */
@Path("Order")
public class OrderResource {
    ProductControllerLocal productControllerLocal = lookupProductControllerLocal();
    CustomerControllerLocal customerControllerLocal =  lookupCustomerControllerLocal();
    CustomerOrderControllerLocal customerOrderControllerLocal = lookupCustomerOrderControllerLocal();
    VoucherControllerLocal voucherControllerLocal = lookupVoucherControllerLocal();
    CustomerVoucherControllerLocal customerVoucherControllerLocal = lookupCustomerVoucherControllerLocal();
    ProductPurchaseControllerLocal productPurchaseControllerLocal = lookupProductPurchaseControllerLocal();
            
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("addCustomerOrder")
    public Response addCustomerOrder(JAXBElement<AddCustomerOrderReq> jaxbaddCustomerOrderReq)
    {
        try
        {
            System.out.println("********** customerOrder()");
            if(jaxbaddCustomerOrderReq != null && jaxbaddCustomerOrderReq.getValue() != null){
                AddCustomerOrderReq customerOrderReq = jaxbaddCustomerOrderReq.getValue();
                
                long customerId = customerOrderReq.getCustomerId();
                Customer customer = customerControllerLocal.retrieveCustomer(customerId);
                
                double total = customerOrderReq.getTotalAmount();
                Date datePaid = customerOrderReq.getDatePaid();
                
                List<ProductPurchase> productPurchases = customerOrderReq.getProductPurchases();
                
                //Create Product Purchase
                for(ProductPurchase pp : productPurchases){
                    Product p = productControllerLocal.retrieveSingleProduct(pp.getProductPurchase().getId());
                    pp.setProductPurchase(p);
                    System.out.println("ProductPurchase: " + pp.getId() + " " + pp.getPricePurchase() + " " + pp.getQtyPurchase());
                    productPurchaseControllerLocal.createProductPurchase(pp);
                }
                
                //Create New Order
                CustomerOrder newOrder = customerOrderControllerLocal.CreateNewCustomerOrder(customer, total, datePaid, productPurchases);;
                if(customerOrderReq.getVoucherCode().length() >0){
                     Voucher voucher = voucherControllerLocal.retrieveVoucher(customerOrderReq.getVoucherCode());
                     CustomerVoucher vc = customerVoucherControllerLocal.retrieveCustomerVoucher(customer,voucher);
                     //Link vc to order
                }
                
                //Update Product Purchase to Order
                for(ProductPurchase pp : productPurchases){
                    productPurchaseControllerLocal.updateProductPurchaseWithOrder(pp.getId(), newOrder);
                }
                
               
                if(newOrder != null){
                    System.out.println("Order created successfully");
                    return Response.status(Response.Status.OK).build();
                }else{
                    System.out.println("error in creating newOrder");
                    return Response.status(Response.Status.BAD_REQUEST).entity("Error in creating new customer").build();
                }
            }else{
                return Response.status(Response.Status.BAD_REQUEST).entity("Jaxb is null").build();
            }
        }
        catch(Exception ex)
        {
             return Response.status(Response.Status.BAD_REQUEST).entity("catch Exception").build();
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
    
      private ProductControllerLocal lookupProductControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ProductControllerLocal) c.lookup("java:global/Sportify/Sportify-ejb/ProductController!ejb.session.stateless.ProductControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    private CustomerControllerLocal lookupCustomerControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CustomerControllerLocal) c.lookup("java:global/Sportify/Sportify-ejb/CustomerController!ejb.session.stateless.CustomerControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    private VoucherControllerLocal lookupVoucherControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (VoucherControllerLocal) c.lookup("java:global/Sportify/Sportify-ejb/VoucherController!ejb.session.stateless.VoucherControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private CustomerOrderControllerLocal lookupCustomerOrderControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CustomerOrderControllerLocal) c.lookup("java:global/Sportify/Sportify-ejb/CustomerOrderController!ejb.session.stateless.CustomerOrderControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    private CustomerVoucherControllerLocal lookupCustomerVoucherControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CustomerVoucherControllerLocal) c.lookup("java:global/Sportify/Sportify-ejb/CustomerVoucherController!ejb.session.stateless.CustomerVoucherControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
      private ProductPurchaseControllerLocal lookupProductPurchaseControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ProductPurchaseControllerLocal) c.lookup("java:global/Sportify/Sportify-ejb/ProductPurchaseController!ejb.session.stateless.ProductPurchaseControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}

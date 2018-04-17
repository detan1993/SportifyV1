/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import ejb.session.stateless.CustomerVoucherControllerLocal;
import ejb.session.stateless.VoucherControllerLocal;
import entity.Voucher;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ws.datamodel.RetrieveCustomerVoucherRsp;

/**
 *
 * @author shanw
 */
@Path("Voucher")
public class VoucherResource {
    VoucherControllerLocal voucherControllerLocal  = lookupVoucherControllerLocal();
    CustomerVoucherControllerLocal customerVoucherControllerLocal = lookupCustomerVoucherControllerLocal();

    public VoucherResource() {
    }
            
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getAllCustomerVoucher/{customerId}")
    public Response getAllCustomerVoucher(@PathParam("customerId") long customerId)
    {
        try
        {
            System.out.println("********** getAllCustomerVoucher()");
            
            List<Voucher> voucherList =  customerVoucherControllerLocal.Ws_retrieveUnusedCustomerVouchersByCustomerId(customerId);
            for(Voucher v : voucherList){
                v.setCustomerVouchers(null);
            }
            
            if(voucherList == null)
                return Response.status(Response.Status.BAD_REQUEST).build();
 
             System.out.println("********** voucherList IS NOT NULL: has-> " + voucherList.size());
            return Response.status(Response.Status.OK).entity(new RetrieveCustomerVoucherRsp(voucherList)).build();
 
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
            
    /**
     * PUT method for updating or creating an instance of AccountResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
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
    private CustomerVoucherControllerLocal lookupCustomerVoucherControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CustomerVoucherControllerLocal) c.lookup("java:global/Sportify/Sportify-ejb/CustomerVoucherController!ejb.session.stateless.CustomerVoucherControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}

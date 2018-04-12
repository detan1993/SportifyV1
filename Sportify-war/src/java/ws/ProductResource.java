/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import ejb.session.stateless.ProductControllerLocal;
import entity.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ws.datamodel.RetrieveActiveProductsRsp;

/**
 *
 * @author shanw
 */
@Path("Product")
public class ProductResource {
    ProductControllerLocal productControllerLocal = lookupProductControllerLocal();

    public ProductResource() {
    }
    
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getAllActiveProducts")
    public Response getAllActiveProducts()
    {
        try
        {
            System.out.println("********** getAllActiveProducts()");
            List<Product> productList =  productControllerLocal.Ws_retrieveActiveProductOrderById();
            
            if(productList == null)
                return Response.status(Response.Status.BAD_REQUEST).build();
 
             System.out.println("********** PRODUCTLIST IS NOT NULL");
            return Response.status(Response.Status.OK).entity(new RetrieveActiveProductsRsp(productList)).build();
 
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

    private ProductControllerLocal lookupProductControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ProductControllerLocal) c.lookup("java:global/Sportify/Sportify-ejb/ProductController!ejb.session.stateless.ProductControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}

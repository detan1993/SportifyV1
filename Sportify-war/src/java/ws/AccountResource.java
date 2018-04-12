/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import ejb.session.stateless.CustomerControllerLocal;
import entity.Customer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.InvalidLoginCredentialException;
import ws.datamodel.RetrieveCustomerAccountRsp;

/**
 * REST Web Service
 *
 * @author David
 */
@Path("Account")
public class AccountResource {

    CustomerControllerLocal customerController = lookupCustomerControllerLocal();

    @Context
    private UriInfo context;
    
    
    

    /**
     * Creates a new instance of AccountResource
     */
    public AccountResource() {
    }

 
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("login/{username}/{password}")
    public Response login(@PathParam("username") String email, @PathParam("password") String password )
    {
        try
        {
           
            System.out.println("********** Login Attempt" + email + " password = " +  password);
            Customer information = customerController.login(email, password);
            
            if(information == null)
                return Response.status(Response.Status.BAD_REQUEST).build();
 
             System.out.println("********** INFORMAITION IS NOT NULL: " + information.getId());
            return Response.status(Response.Status.OK).entity(new RetrieveCustomerAccountRsp( information.getId(), information.getFirstName() + " " + information.getLastName() ,information.getEmail(), information.getAddress(), information.getLoyaltyPoints())).build();
 
        }
         catch(InvalidLoginCredentialException ex)
        {
           // ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            System.out.println("********* INVALID LOGIN CREDENTIAL *");
            return Response.status(Response.Status.BAD_REQUEST).build();
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

    private CustomerControllerLocal lookupCustomerControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CustomerControllerLocal) c.lookup("java:global/Sportify/Sportify-ejb/CustomerController!ejb.session.stateless.CustomerControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}

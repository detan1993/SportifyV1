/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import ejb.session.stateless.testingControllerLocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author David
 */
@Path("sportify")
public class SportifyResource {

    testingControllerLocal testingController = lookuptestingControllerLocal();

    @Context
    private UriInfo context;

    
    
    
    
    /**
     * Creates a new instance of SportifyResource
     */
    public SportifyResource() {
    }

    /**
     * Retrieves representation of an instance of ws.SportifyResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of SportifyResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }

    private testingControllerLocal lookuptestingControllerLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (testingControllerLocal) c.lookup("java:global/Sportify/Sportify-ejb/testingController!ejb.session.stateless.testingControllerLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}

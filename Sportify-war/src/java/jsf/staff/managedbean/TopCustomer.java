/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.staff.managedbean;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author David
 */
@Named(value = "topCustomer")
@ViewScoped
public class TopCustomer implements Serializable  {

    /**
     * Creates a new instance of TopCustomer
     */
    
    private String test;
    
    public TopCustomer() {
    }

    @PostConstruct
    public void postConstruct(){
        
        test = FacesContext.getCurrentInstance()
                                 .getExternalContext()
                                 .getRequestParameterMap()
                                 .get("testing");
        
 
    }
    /**
     * @return the test
     */
    public String getTest() {
        return test;
    }

    /**
     * @param test the test to set
     */
    public void setTest(String test) {
        this.test = test;
    }
    
}

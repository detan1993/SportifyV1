/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.staff.managedbean;

import ejb.session.stateless.StaffControllerLocal;
import entity.Staff;
import java.io.IOException;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author David
 */
@Named(value = "staffIndexManagedBean")
@RequestScoped
public class StaffIndexManagedBean {

    @EJB(name = "StaffControllerLocal")
    private StaffControllerLocal staffControllerLocal;

    
    private String username;
    private String password;
    
    
    public StaffIndexManagedBean() {
    }
    
    
    public void login(ActionEvent event) throws IOException
    {
        
        System.out.println("***********Excuting login in Staff Index Managed Bean");
        try
        {
            Staff currentStaffEntity = staffControllerLocal.login(username, password);
            FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("staffIsLogin", true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentStaffEntity", currentStaffEntity);
            FacesContext.getCurrentInstance().getExternalContext().redirect("staffHome.xhtml");            
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid login credential: " + ex.getMessage(), null));
        }
    }
    
    
    
    public void logout(ActionEvent event) throws IOException
    {
        ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
        FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
}

package jsf.customer.managedbean;

import ejb.session.stateless.CustomerControllerLocal;
import java.io.IOException;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import entity.Customer;
import org.primefaces.context.RequestContext;
import util.exception.InvalidLoginCredentialException;

@Named(value = "customerLoginManagedBean")
@RequestScoped
public class CustomerLoginManagedBean {

    @EJB
    private CustomerControllerLocal customerController;
    
    private String email;
    private String password;

    public CustomerLoginManagedBean() {
    }
    
     public void login(ActionEvent event) throws IOException
    {
        try
        {
            Customer currentCustomer = customerController.login(email, password);
            FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isLogin", true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentCustomer", currentCustomer);
            FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml"); 
        }
        catch(InvalidLoginCredentialException ex)
        {
            RequestContext.getCurrentInstance().execute("PF('loginDialog').show()");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid email or password", null));
        }
    }
    
    public void logout(ActionEvent event) throws IOException
    {
        ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
        FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml");
//        String currentPage = FacesContext.getCurrentInstance().getViewRoot().getViewId() + "?faces-redirect=true";
//        FacesContext.getExternalContext().redirect(currentPage);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
}

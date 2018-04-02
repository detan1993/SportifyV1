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
import java.util.ArrayList;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import util.exception.InvalidLoginCredentialException;

@Named(value = "customerLoginManagedBean")
@RequestScoped
public class CustomerLoginManagedBean {

    @EJB
    private CustomerControllerLocal customerController;

    private String email;
    private String password;
    private int numOfCartItems = 0;

    public CustomerLoginManagedBean() {
    }

    public void login(ActionEvent event) throws IOException {
        try {
            Customer currentCustomer = customerController.login(email, password);
            FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isLogin", true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentCustomer", currentCustomer);

            //check if tempProductId exist for redirecting to the same page
            Boolean productIdExist = (Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tempProductId");
            String productIdStrExist = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tempProductIdStr");
            
            //check if tempTeamName exist for redirecting to the same page
            Boolean productTeamNameExist = (Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tempTeamName");
            String productTeamNameStrExist = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tempTeamNameStr");

            if (productIdExist != null) {
                if (productIdExist) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("?faces-redirect=true&productId=" + productIdStrExist);
                } else {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("?faces-redirect=true");
                }
            }else{
                if (productTeamNameExist != null){
                    if (productTeamNameExist){
                         FacesContext.getCurrentInstance().getExternalContext().redirect("?faces-redirect=true&teamName=" + productTeamNameStrExist);
                    }
                }
            }

//            FacesContext.getCurrentInstance().getExternalContext().redirect("?faces-redirect=true");
//            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        } catch (InvalidLoginCredentialException ex) {
            RequestContext.getCurrentInstance().execute("PF('loginDialog').show()");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid email or password", null));
        }
    }

    public void logout(ActionEvent event) throws IOException {
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
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

    public int getNumOfCartItems() {
        ArrayList<String[]> checkIfCartExist = (ArrayList<String[]>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentItemCart");
        if (checkIfCartExist != null) {
            numOfCartItems = checkIfCartExist.size();
        }
        return numOfCartItems;
    }

    public void setNumOfCartItems(int numOfCartItems) {
        this.numOfCartItems = numOfCartItems;
    }

}

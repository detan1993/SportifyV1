package jsf.customer.managedbean;

import ejb.session.stateless.CustomerControllerLocal;
import entity.Customer;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import util.exception.CustomerSignUpException;

@Named(value = "signUpManagedBean")
@RequestScoped
public class SignUpManagedBean {

    @EJB
    private CustomerControllerLocal customerController;
    private Customer customerEntity;
    
    public SignUpManagedBean() {
        customerEntity = new Customer();
    }

    public void createNewCustomer()
    {
        try
        {
            Customer customer = customerController.createNewCustomer(customerEntity);
            customerEntity = new Customer();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Thank you " + customer.getFirstName() + " for signing up!  Login to start shopping!", null));
        }
        catch(CustomerSignUpException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while signing up: " + ex.getMessage(), null));
        }
    }
    
    public Customer getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(Customer customerEntity) {
        this.customerEntity = customerEntity;
    }
    
}

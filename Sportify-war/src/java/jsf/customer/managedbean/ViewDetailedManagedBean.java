package jsf.customer.managedbean;

import ejb.session.stateless.ProductControllerLocal;
import entity.Product;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@Named(value = "viewDetailedManagedBean")
@RequestScoped
public class ViewDetailedManagedBean {

    @EJB
    private ProductControllerLocal productController;

    private Product product;

    @PostConstruct
    public void postConstruct() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String productCode = request.getParameter("productCode");
    }

    public ViewDetailedManagedBean() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

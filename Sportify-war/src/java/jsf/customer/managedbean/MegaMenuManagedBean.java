package jsf.customer.managedbean;

import ejb.session.stateless.ProductControllerLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.menu.DefaultMenuColumn;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

@Named(value = "megaMenuManagedBean")
@ViewScoped
public class MegaMenuManagedBean implements Serializable {

    private MenuModel model;
    private DefaultSubMenu rootMenu;
    
    @EJB
    private ProductControllerLocal productControllerLocal;
    private List<List<String>> menuItems;
    
    @PostConstruct
    public void postConstruct() {
        model = new DefaultMenuModel();
        rootMenu = new DefaultSubMenu("FOOTBALL JERSEYS");
        model.addElement(rootMenu);

        menuItems = productControllerLocal.retrieveCountriesAndTeams();
        List<String> teams;
        
        for (int i = 0; i < menuItems.size(); i++) {
            String country = "";
            teams = new ArrayList();
            for (int j = 0; j < menuItems.get(i).size(); j++ ){
                if (j == 0){
                    country = menuItems.get(i).get(j);
                }else{
                    teams.add(menuItems.get(i).get(j));
                }                
            }
            addMenu(country, teams);
        }
    }

    public void addMenu(String label, List<String> items) {

        DefaultMenuColumn column = new DefaultMenuColumn();

        //a submenu inside column
        DefaultSubMenu theColumnMenu = new DefaultSubMenu(label);
        column.addElement(theColumnMenu);

        for (String item : items) {
            DefaultMenuItem mi = new DefaultMenuItem(item);
            mi.setUrl("products.xhtml?teamName=" + item);
            theColumnMenu.addElement(mi);
        }
        //the main menu has columns instead of items
        rootMenu.addElement(column);
    }

    public MenuModel getMenuModel() {
        return model;
    }
    
    public MegaMenuManagedBean() {
    }
    
}

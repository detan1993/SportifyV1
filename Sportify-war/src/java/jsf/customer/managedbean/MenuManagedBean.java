package jsf.customer.managedbean;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultMenuColumn;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import javax.annotation.PostConstruct;

@ManagedBean
@ViewScoped
public class MenuManagedBean implements Serializable {
    
    private MenuModel model;
    private DefaultSubMenu rootMenu;

    @PostConstruct
    public void postConstruct() {
        model = new DefaultMenuModel();
        rootMenu = new DefaultSubMenu("FOOTBALL JERSEYS");
        model.addElement(rootMenu);
        addMenu("ENGLAND", "Arsenal", "Chelsea", "Manu", "Man City");
        addMenu("SPAIN", "Real Madrid", "Barca", "Atletico", "Getafe");
        addMenu("GERMANY", "Bayern");
        addMenu("FRANCE", "PSG", "Lyon");
    }

    public void addMenu(String label, String... items) {

        DefaultMenuColumn column = new DefaultMenuColumn();

        //a submenu inside column
        DefaultSubMenu theColumnMenu = new DefaultSubMenu(label);
        column.addElement(theColumnMenu);

        for (Object item : items) {
            DefaultMenuItem mi = new DefaultMenuItem(item);
            mi.setUrl("#");
            theColumnMenu.addElement(mi);
        }
        //the main menu has columns instead of items
        rootMenu.addElement(column);
    }

    public MenuModel getMenuModel() {
        return model;
    }
   
    public MenuManagedBean() {
    }   
}

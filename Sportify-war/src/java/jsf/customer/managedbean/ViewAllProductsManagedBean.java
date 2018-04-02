package jsf.customer.managedbean;

import ejb.session.stateless.ProductControllerLocal;
import entity.Product;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

@Named(value = "viewAllProductsManagedBean")
@ViewScoped
public class ViewAllProductsManagedBean implements Serializable {

    @EJB
    private ProductControllerLocal productController;

    private List<Product> products;
    private Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
    private String countrySelected;
    private Map<String, String> countries;
    private String teamSelected;
    private Map<String, String> teams;

    public ViewAllProductsManagedBean() {
        products = new ArrayList<>();
        countries = new HashMap<String, String>();
    }

    @PostConstruct
    public void postConstruct() {
        
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String teamName = request.getParameter("teamName");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tempTeamName", true);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tempTeamNameStr", teamName);
        
        if (teamName != null && !teamName.equals("")){
            products = productController.retrieveProductsByTeam(teamName);
        }
        else{
            products = productController.retrieveProduct();
        }
        
        //for populating dropdown filter 
        List<List<String>> countriesAndTeams = productController.retrieveCountriesAndTeams();
        for (int i = 0; i < countriesAndTeams.size(); i++) {
            String country = "";
            Map<String, String> map = new HashMap<String, String>();
            for (int j = 0; j < countriesAndTeams.get(i).size(); j++) {
                String team = "";
                if (j == 0) {
                    country = countriesAndTeams.get(i).get(j);
                    countries.put(country, country);
                } else {
                    team = countriesAndTeams.get(i).get(j);
                    map.put(team, team);
                }
            }
            getData().put(country, map);
        }

        System.out.println(Collections.singletonList(data));
    }

    public void filterByCountry() {
        if (countrySelected != null && !countrySelected.equals("")) {
            teams = getData().get(countrySelected);
            products = productController.retrieveProductsByCountry(countrySelected);

        } else {
            teams = new HashMap<String, String>();
            products = productController.retrieveProduct();
        }

        System.out.println(countrySelected);
        System.out.println(teamSelected);
    }

    public void filterByTeam() {
        System.out.println(teamSelected);
        if (teamSelected != null && !teamSelected.equals("")) {
            products = productController.retrieveProductsByTeam(teamSelected);
        } else {
            if (countrySelected != null && !countrySelected.equals("")) {
                products = productController.retrieveProductsByCountry(countrySelected);
            }
        }
    }
    
    public String exploreRedirect(long id){
        return "detailedProduct?faces-redirect=true&productId=" + id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getCountrySelected() {
        return countrySelected;
    }

    public void setCountrySelected(String countrySelected) {
        this.countrySelected = countrySelected;
    }

    public String getTeamSelected() {
        return teamSelected;
    }

    public void setTeamSelected(String teamSelected) {
        this.teamSelected = teamSelected;
    }

    public Map<String, String> getCountries() {
        return countries;
    }

    public void setCountries(Map<String, String> countries) {
        this.countries = countries;
    }

    public Map<String, String> getTeams() {
        return teams;
    }

    public void setTeams(Map<String, String> teams) {
        this.teams = teams;
    }

    public Map<String, Map<String, String>> getData() {
        return data;
    }

    public void setData(Map<String, Map<String, String>> data) {
        this.data = data;
    }

}

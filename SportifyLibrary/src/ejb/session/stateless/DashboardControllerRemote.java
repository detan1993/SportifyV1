/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import java.util.List;
import util.helperClass.CountrySales;



public interface DashboardControllerRemote {

      public List<List<CountrySales>> retrieveAllSalesByMonths();

    public List<String[]> retrieveSalesByTeamAndCountry();

    public List<String> getCountries();
    
}

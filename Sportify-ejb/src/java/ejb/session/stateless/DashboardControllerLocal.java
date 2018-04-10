/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import java.util.List;
import util.helperClass.CountrySales;
import util.helperClass.PerformanceBoard;
import util.helperClass.TopProductByCode;
import util.helperClass.TopProductByTeam;

public interface DashboardControllerLocal {

    public List<List<CountrySales>> retrieveAllSalesByMonths();

    public List<String[]> retrieveSalesByTeamAndCountry();

    public List<String> getCountries();
     public List<TopProductByCode> getProductsSumByQuantityPurchaseByProductCode();
      public List<TopProductByTeam> getProductsSumByQuantityPurchaseByTeam();
      
    public List<PerformanceBoard> getPerformanceInformation();
       public List<List<CountrySales>> retrieveAllSalesByMonthsByRange(String dateFrom, String dateTo);
        public List<String[]> retrieveSalesByTeamAndCountryByRange(String dateFrom, String dateTo);
}

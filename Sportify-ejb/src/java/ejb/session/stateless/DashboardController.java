/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerOrder;
import entity.Product;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.helperClass.CountrySales;
import util.helperClass.TopProductByCode;
import util.helperClass.TopProductByTeam;

/**
 *
 * @author David
 */
@Remote(DashboardControllerRemote.class)
@Local(DashboardControllerLocal.class)
@Stateless
public class DashboardController implements DashboardControllerRemote, DashboardControllerLocal {

    @PersistenceContext(unitName = "Sportify-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public List<List<CountrySales>> retrieveAllSalesByMonths() {

        double totalSales = 0;
        int month = 0;
        int comparingMonth = 0;

        // List<CountrySales>[] salesRecord =  new Array[];
        List<List<CountrySales>> salesRecord = new ArrayList<List<CountrySales>>();
        List<CountrySales> countrySales = new ArrayList<>();

        System.out.println("**********  REtrieve All sales By MOnths");

        Query query = em.createNativeQuery("SELECT p.country, Month(p.dateCreated), SUM(p.price) FROM Product p GROUP by MONTH(p.dateCreated), p.country ORDER by MONTH(p.dateCreated)");
        List<Object[]> salesRecords = query.getResultList();

        //by right shoyld be travelling now hardcord first.
        try {

            System.out.println("******** sakes records is " + salesRecords.size());

            for (Object[] obj : salesRecords) {

                String country = (String) obj[0];
                Double sales = (Double) obj[2];
                System.out.println("********** country is " + country + " Month is " + (Integer) obj[1] + " sum of price is " + sales);

                month = (Integer) obj[1];
                if (comparingMonth == 0) {
                    comparingMonth = month;
                    System.out.println(" ********comporatingMOnth == 0.  Comparing month is now " + comparingMonth);

                } else if (comparingMonth != 0 && comparingMonth != month) {
                    //  System.out.println(" ********comporatingMOnth != 0.  Comparing month is now " + month );
                    countrySales.add(new CountrySales("Total", totalSales, new DateFormatSymbols().getMonths()[comparingMonth - 1])); //some up sales for comparing montsh which is month -1 
                    salesRecord.add(countrySales); // add the sales from month-1 to salesRecord.
                    totalSales = 0; //reset
                    comparingMonth = month;
                    countrySales = new ArrayList<>();
                }

                totalSales += sales;
                System.out.println("********* Adding country sales");
                countrySales.add(new CountrySales(country, sales, new DateFormatSymbols().getMonths()[month - 1]));

            }

            countrySales.add(new CountrySales("Total", totalSales, new DateFormatSymbols().getMonths()[month - 1])); //some up sales for comparing montsh which is month -1 
            salesRecord.add(countrySales); // do final adding.

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (int i = 0; i < salesRecord.size(); i++) {
            List<CountrySales> a = salesRecord.get(i);
            for (CountrySales b : a) {
                System.out.println("Counry is " + b.getCountryName() + " Sales is " + b.getSales() + " month is " + b.getMonth());

            }

        }

        retrieveSalesByTeamAndCountry();
        return salesRecord;
    }

    
    @Override
    public List<String[]> retrieveSalesByTeamAndCountry() {

        Double totalSales = 0.0;
        try {

            List<String[]> teamSales = new ArrayList<>();
            Query query = em.createNativeQuery("SELECT p.team, p.country , SUM(p.price)  FROM Product p WHERE YEAR(p.dateCreated) = 2018 GROUP BY p.country, p.team");
            List<Object[]> salesRecords = query.getResultList();

            for (int i = 0; i < salesRecords.size(); i++) {
                String[] record = new String[3];
                record[0] = (String) salesRecords.get(i)[0];
                System.out.println("record[0]" + record[0]);
                record[1] = (String) salesRecords.get(i)[1];
                System.out.println("record[1]" + record[1]);
                totalSales = (Double) salesRecords.get(i)[2];            
                record[2] = totalSales.toString();
                System.out.println("record[2]" + record[2]);
                teamSales.add(record);
            }
            
             return teamSales;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return null;

    }

    @Override
    public List<String> getCountries() {
        List<String> countries = new ArrayList<>();
        Query q = em.createQuery("SELECT DISTINCT(p.country) FROM Product p ORDER BY p.country");
        countries = q.getResultList();

        for (int i = 0; i < countries.size(); i++) {
            System.out.println("Countru is " + countries.get(i));
        }

        return countries;
    }
    
    @Override
    public List<TopProductByCode> getProductsSumByQuantityPurchaseByProductCode(){
        
 
        List<String> productCodes = new ArrayList<>();
        List<CustomerOrder> orders = new ArrayList<>();
        List<TopProductByCode> topTenProductByCode = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#.##");
        HashMap<String, Integer> productCodeQuantityPair = new HashMap<>();
        HashMap<String, Double>  productCodeSalesPair = new HashMap<>();
        Query q = em.createQuery("SELECT o FROM CustomerOrder o");
        orders = q.getResultList();
        
        for(int i=0; i<orders.size(); i++){ //top 10 
            
           List<Product> products = new ArrayList<>();
           products = orders.get(i).getProducts();
           
           for(Product p : products)
           {
               String productCode = p.getProductCode();
               double pricePurchase = p.getPrice();
               
               if(productCodeSalesPair.get(productCode) == null)
               {
                   System.out.println("*******************product code : " + productCode +  " Code is null" + " QTY 1");
                   productCodes.add(productCode);
                   productCodeSalesPair.put(productCode, pricePurchase); // this must change
                   productCodeQuantityPair.put(productCode, 1);
                   
               }else if(productCodeSalesPair.get(productCode) != null){ // exisiting product code
                   
                 System.out.println("******************* product Code : " + productCode + "Code is not null. QTY = " +  (productCodeQuantityPair.get(productCode) + 1 ));
                 System.out.println("******************* product Code : " + productCode + "Code is not null. Purchase is = " +  (productCodeSalesPair.get(productCode) + pricePurchase ));
                   productCodeQuantityPair.put(productCode, productCodeQuantityPair.get(productCode) + 1);
                   productCodeSalesPair.put(productCode, productCodeSalesPair.get(productCode) + pricePurchase);
               }
           }
            
        }
        
        System.out.println("***************** POPULATE BACK THE DATA TO TOP PRODUCY BY CODE");
        
        for(int i=0; i<productCodes.size(); i++){
            
            String productCode = productCodes.get(i);
            topTenProductByCode.add(new TopProductByCode(productCode, productCodeQuantityPair.get(productCode) , Double.parseDouble(df.format(productCodeSalesPair.get(productCode)))));            
        }
        
        Collections.sort(topTenProductByCode, TopProductByCode.BY_TOTAL_PURCHASE);
        
        //set the rank
        for(int i =0; i<topTenProductByCode.size(); i++ ){
            topTenProductByCode.get(i).setRank(i+1);
            double tPurchase = topTenProductByCode.get(i).getAmountPurchased();
            double tTotal = topTenProductByCode.get(i).getQuantityOrdered();
            double avgProfit = Double.parseDouble(df.format(tPurchase/tTotal));
            topTenProductByCode.get(i).setAverageProfit(avgProfit);
        }
 
        
        return topTenProductByCode;
    }
    
    
    
    @Override
     public List<TopProductByTeam> getProductsSumByQuantityPurchaseByTeam(){
        
 
        List<String> teamNames = new ArrayList<>();
        List<CustomerOrder> orders = new ArrayList<>();
        List<TopProductByTeam> topTenProductByTeam = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#.##");
        HashMap<String, Integer> productTeamQuantityPair = new HashMap<>();
        HashMap<String, Double>  productTeamSalesPair = new HashMap<>();
        Query q = em.createQuery("SELECT o FROM CustomerOrder o");
        orders = q.getResultList();
        
        for(int i=0; i<orders.size(); i++){ //top 10 
            
           List<Product> products = new ArrayList<>();
           products = orders.get(i).getProducts();
           
           for(Product p : products)
           {
               String productTeam = p.getTeam();
               double pricePurchase = p.getPrice();
               
               if(productTeamSalesPair.get(productTeam) == null)
               {
                   System.out.println("*******************product Team : " + productTeam +  " Team is null" + " QTY 1");
                   teamNames.add(productTeam);
                   productTeamSalesPair.put(productTeam, pricePurchase); // this must change
                   productTeamQuantityPair.put(productTeam, 1);
                   
               }else if(productTeamSalesPair.get(productTeam) != null){ // exisiting product code
                   
                 System.out.println("******************* product Team : " + productTeam + " Team is not null. QTY = " +  (productTeamQuantityPair.get(productTeam) + 1 ));
                 System.out.println("******************* product Team : " + productTeam + " Team is not null. Purchase is = " +  (productTeamSalesPair.get(productTeam) + pricePurchase ));
                   productTeamQuantityPair.put(productTeam, productTeamQuantityPair.get(productTeam) + 1);
                   productTeamSalesPair.put(productTeam, productTeamSalesPair.get(productTeam) + pricePurchase);
               }
           }
            
        }
        
        System.out.println("***************** POPULATE BACK THE DATA TO TOP PRODUCY BY CODE");
        
        for(int i=0; i<teamNames.size(); i++){
            
            String productTeam = teamNames.get(i);
           // System.out.println("***************product Team " + productTeam + " purcahses : " + )
            topTenProductByTeam.add(new TopProductByTeam(productTeam, productTeamQuantityPair.get(productTeam) , Double.parseDouble(df.format(productTeamSalesPair.get(productTeam)))));            
        }
        
        Collections.sort(topTenProductByTeam, TopProductByTeam.BY_TOTAL_PURCHASE);
        
        //set the rank
        for(int i =0; i<topTenProductByTeam.size(); i++ ){
            topTenProductByTeam.get(i).setRank(i+1);
            double tPurchase = topTenProductByTeam.get(i).getAmountPurchased();
            double tTotal = topTenProductByTeam.get(i).getQuantityOrdered();
            double avgProfit = Double.parseDouble(df.format(tPurchase/tTotal));
            topTenProductByTeam.get(i).setAverageProfit(avgProfit);
        }
 
        
        return topTenProductByTeam;
    }
        
    
//    private void getProductsSumBy

}

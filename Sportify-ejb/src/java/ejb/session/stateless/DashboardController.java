/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerOrder;
import entity.Product;
import entity.ProductPurchase;
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
import util.helperClass.PerformanceBoard;
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

        List<List<CountrySales>> countrySalesByMonths = new ArrayList<>();
        try {

            Query getMonths = em.createQuery("SELECT DISTINCT(SUBSTRING(o.datePaid, 6,2)) FROM CustomerOrder o ORDER BY o.datePaid" );
            List<String> salesMonths = getMonths.getResultList();
          //  System.out.println("********* sales months is " + salesMonths.size() );
            
            for(int i =0; i<salesMonths.size(); i++){
                
                List<CountrySales> salesByCountry = new ArrayList<>();
                String month = salesMonths.get(i);
     
                System.out.println("************ MONTH IS " + month);
                
             Query getCountrySalesByMonth = em.createQuery("SELECT o FROM CustomerOrder o WHERE  SUBSTRING(o.datePaid , 1,4) =:year AND SUBSTRING(o.datePaid , 6,2) =:month  ORDER BY o.datePaid");
             getCountrySalesByMonth.setParameter("year", "2018");
             getCountrySalesByMonth.setParameter("month", month);
             List<CustomerOrder> orders = getCountrySalesByMonth.getResultList();
     
            System.out.println("******** sales records is " + orders.size());
             HashMap<String,Double> countrySalesPair = new HashMap<>();
             List<String> teamCountry = new ArrayList<>();
             
           
             for (int j = 0; j < orders.size(); j++) {
             List<ProductPurchase> products = orders.get(j).getProductPurchase();
                   
             for(ProductPurchase product : products ){
                  
                  System.out.println("**************** Product Purchase name " + product.getProductPurchase().getProductName() );
                 
                       String countryName =  product.getProductPurchase().getCountry();
                       double purchaseAmount = product.getPricePurchase();
                       
                       if(countrySalesPair.get(countryName) ==  null){
                           System.out.println("*******************Country is : " + countryName + " team name is " + product.getProductPurchase().getTeam()  + " amount Purchase " + purchaseAmount);
                           teamCountry.add(countryName); //add new country of the team
                           countrySalesPair.put(countryName, purchaseAmount);
                       }else if(countrySalesPair.get(countryName) !=  null) //previously added to hashmap
                       {
                           
                          System.out.println("*******************NOT NULL Country is : " + countryName + " team name is " + product.getProductPurchase().getTeam()  + " amount Purchase " + purchaseAmount);
                           countrySalesPair.put(countryName, countrySalesPair.get(countryName) + purchaseAmount );
                       }
                      
              }
                       
             }
             
             
             for(int countryName=0; countryName<teamCountry.size(); countryName++){
                 
                 String monthsInWords = salesMonths.get(i);
                 int monthInt  = 0;
                  if(monthsInWords.substring(0,1).equals("0"))
                  {
                   monthInt = Integer.parseInt(monthsInWords.substring(1));
                   monthsInWords = new DateFormatSymbols().getMonths()[monthInt-1];
                  //salesMonths.get(i).substring(1);
                  }
                  else 
                  {
                      monthInt = Integer.parseInt(monthsInWords.substring(0));
                      monthsInWords = new DateFormatSymbols().getMonths()[monthInt-1];
                  }
                   
                 System.out.println("$$$$$$$$$$$$$$$$$$$ country name : " +  teamCountry.get(countryName)  + " sales($) " + countrySalesPair.get(teamCountry.get(countryName)) + " Months " + monthsInWords );
                 salesByCountry.add(new CountrySales(teamCountry.get(countryName) , countrySalesPair.get(teamCountry.get(countryName)) , monthsInWords ));
                   
             }
             
             
             countrySalesByMonths.add(salesByCountry);

            }
            
        }catch(Exception ex){
            
        }
        return countrySalesByMonths;
    }

    
    @Override
    public List<String[]> retrieveSalesByTeamAndCountry() {

        Double totalSales = 0.0;
        try {

            List<String[]> teamSales = new ArrayList<>();
            //Query query = em.createQuery("SELECT p.team, p.country , SUM(p.price)  FROM Product p WHERE SUBSTRING(p.dateCreated,1,4) = :sYear GROUP BY p.country, p.team");
            //query.setParameter("sYear", "2018");
            
            Query query = em.createQuery("SELECT p FROM Product p ORDER BY p.country");
            List<Product> products = query.getResultList();
            HashMap<String, Double> teamSalesPair = new HashMap<>();
            List<String> team = new ArrayList<>();
            List<String> country = new ArrayList<>();
            System.err.println("profuct Size " + products.size());
            
             Query q = em.createQuery("SELECT  p.productPurchase.team  , p.productPurchase.country , SUM(p.pricePurchase * p.qtyPurchase)    FROM ProductPurchase p  GROUP BY p.productPurchase.country, p.productPurchase.team ");
             
             List<Object[]> salesRecords = q.getResultList();

            for (int i = 0; i < salesRecords.size(); i++) {
                 
                System.out.println("******** TOTAL PURCAHSE FROM  " +  (String)salesRecords.get(i)[0] + " is " + (Double)salesRecords.get(i)[2] ) ;
              
    
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
        
        List<TopProductByCode> topTenProductByCode = new ArrayList<>();
        try{
            
     
 
        List<String> productCodes = new ArrayList<>();
        List<CustomerOrder> orders = new ArrayList<>();

        DecimalFormat df = new DecimalFormat("#.##");
        HashMap<String, Integer> productCodeQuantityPair = new HashMap<>();
        HashMap<String, Double>  productCodeSalesPair = new HashMap<>();
        Query q = em.createQuery("SELECT o FROM CustomerOrder o");
        orders = q.getResultList();
        System.out.println("************ orders size is " + orders.size());
        for(int i=0; i<orders.size(); i++){ //top 10 
            
           List<ProductPurchase> products = new ArrayList<>();
           
               products = orders.get(i).getProductPurchase();
        
           
           for(ProductPurchase p : products)
           {
               System.out.println("************ ProductPurchase p : products " + p.getProductPurchase().getProductCode());
               String productCode = p.getProductPurchase().getProductCode();
               double pricePurchase = p.getPricePurchase() * p.getQtyPurchase();
               int qtyPurchase = p.getQtyPurchase();
               
               if(productCodeSalesPair.get(productCode) == null)
               {
                   System.out.println("*******************product code : " + productCode +  " Code is null" + " QTY 1");
                   productCodes.add(productCode);
                   productCodeSalesPair.put(productCode, pricePurchase); 
                   productCodeQuantityPair.put(productCode, qtyPurchase);
                   
               }else if(productCodeSalesPair.get(productCode) != null){ // exisiting product code
                   
                 System.out.println("******************* product Code : " + productCode + "Code is not null. QTY = " +  (productCodeQuantityPair.get(productCode) + qtyPurchase ));
                 System.out.println("******************* product Code : " + productCode + "Code is not null. Purchase is = " +  (productCodeSalesPair.get(productCode) + pricePurchase ));
                   productCodeQuantityPair.put(productCode, productCodeQuantityPair.get(productCode) + qtyPurchase);
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
        
        }
        catch(Exception ex){
         ex.printStackTrace();
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
        System.out.println("************ orders size is " + orders.size());
        for(int i=0; i<orders.size(); i++){ //top 10 
            
           List<ProductPurchase> products = new ArrayList<>();
           products = orders.get(i).getProductPurchase();
           
           for(ProductPurchase p : products)
           {
               String productTeam = p.getProductPurchase().getTeam();
               int qtyPurchase =  p.getQtyPurchase();
               double pricePurchase = p.getPricePurchase() * qtyPurchase;
               
               
               if(productTeamSalesPair.get(productTeam) == null)
               {
                   System.out.println("*******************product Team : " + productTeam +  " Team is null" + " QTY 1");
                   teamNames.add(productTeam);
                   productTeamSalesPair.put(productTeam, pricePurchase); 
                   productTeamQuantityPair.put(productTeam, qtyPurchase);
                   
               }else if(productTeamSalesPair.get(productTeam) != null){ // exisiting product code
                   
                 System.out.println("******************* product Team : " + productTeam + " Team is not null. QTY = " +  (productTeamQuantityPair.get(productTeam) + qtyPurchase ));
                 System.out.println("******************* product Team : " + productTeam + " Team is not null. Purchase is = " +  (productTeamSalesPair.get(productTeam) + pricePurchase ));
                   productTeamQuantityPair.put(productTeam, productTeamQuantityPair.get(productTeam) + qtyPurchase);
                   productTeamSalesPair.put(productTeam, productTeamSalesPair.get(productTeam) + pricePurchase);
               }
           }
            
        }
        
        System.out.println("***************** POPULATE BACK THE DATA TO TOP PRODUCY BY Nmae");
        
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
     
     
    @Override
     public List<PerformanceBoard> getPerformanceInformation(){
         
         List<PerformanceBoard> boards = new ArrayList<>();
         int noOfBoard = 4;
         
         for(int i=0; i<noOfBoard; i++){
             
             
             if(i ==0 )
             {
                boards.add(customerBoard());
                 
             }
             
             
         }
         
         
         return boards;
         
     }
     private PerformanceBoard customerBoard(){
      
      PerformanceBoard customer = null;
      try{
           int totalCustomer = 0;
         
         Query query = em.createQuery("SELECT COUNT(c.id) FROM Customer c");
         List<Integer> countCustomer = query.getResultList();
         totalCustomer = countCustomer.get(0);
         
      //   query = em.createQuery("SELECT COUNT(*) FROM Customer c WHERE  ")
          
      }catch(Exception ex){
          
      }
       return customer;
     }
        
    
//    private void getProductsSumBy

}

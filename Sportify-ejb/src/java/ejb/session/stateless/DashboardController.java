/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerOrder;
import entity.CustomerVoucher;
import entity.Product;
import entity.ProductPurchase;
import entity.Voucher;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
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

    @EJB(name = "CustomerVoucherControllerLocal")
    private CustomerVoucherControllerLocal customerVoucherControllerLocal;

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public List<List<CountrySales>> retrieveAllSalesByMonths() {

        List<List<CountrySales>> countrySalesByMonths = new ArrayList<>();
        try {

            Query getMonths = em.createQuery("SELECT DISTINCT(SUBSTRING(o.datePaid, 6,2)) FROM CustomerOrder o ORDER BY o.datePaid");
            List<String> salesMonths = getMonths.getResultList();
            //  System.out.println("********* sales months is " + salesMonths.size() );

            for (int i = 0; i < salesMonths.size(); i++) {

                List<CountrySales> salesByCountry = new ArrayList<>();
                String month = salesMonths.get(i);

                System.out.println("************ MONTH IS " + month);

                Query getCountrySalesByMonth = em.createQuery("SELECT o FROM CustomerOrder o WHERE  SUBSTRING(o.datePaid , 1,4) =:year AND SUBSTRING(o.datePaid , 6,2) =:month  ORDER BY o.datePaid");
                getCountrySalesByMonth.setParameter("year", "2018");
                getCountrySalesByMonth.setParameter("month", month);
                List<CustomerOrder> orders = getCountrySalesByMonth.getResultList();

                System.out.println("******** sales records is " + orders.size());
                HashMap<String, Double> countrySalesPair = new HashMap<>();
                List<String> teamCountry = new ArrayList<>();

                for (int j = 0; j < orders.size(); j++) {
                    List<ProductPurchase> products = orders.get(j).getProductPurchase();
                    CustomerOrder custOrder = orders.get(j);
                    System.out.println("CUSTOMER ORDER id = " + orders.get(j).getId());
                    //get voucher for this order
                    
                    CustomerVoucher custVoucher = custOrder.getCustomerVoucher();
                    Voucher voucher = null;
                    boolean deductVoucherAmount = false;
                    if (custVoucher != null) {
                        voucher = custVoucher.getVoucher();
                    }

                    for (ProductPurchase product : products) {

                        System.out.println("**************** Product Purchase name " + product.getProductPurchase().getProductName());

                        String countryName = product.getProductPurchase().getCountry();
                        int qtyPurchase = product.getQtyPurchase();
                        double purchaseAmount = product.getPricePurchase() * qtyPurchase;
                         double percentageAmount = 0;
                         double amountAfterDeduction = 0;
                         
                        //check if order have voucher
                        if (voucher != null) {
                            if (deductVoucherAmount == false) {
                                System.out.print("**** THERE IS VOUCHER FOR CUSTOMER ORDER ID: " + custOrder.getId());
                                percentageAmount = (voucher.getVoucherValue() / 100.0) * purchaseAmount;
                                amountAfterDeduction = purchaseAmount - percentageAmount;
                                purchaseAmount = amountAfterDeduction;
                                deductVoucherAmount = true;
                            }
                        }

                        if (countrySalesPair.get(countryName) == null) {

                            System.out.println("*******************Country is : " + countryName + " team name is " + product.getProductPurchase().getTeam() + " amount Purchase " + purchaseAmount);
                            teamCountry.add(countryName); //add new country of the team
                            countrySalesPair.put(countryName, purchaseAmount);

                        } else if (countrySalesPair.get(countryName) != null) //previously added to hashmap
                        {

                            System.out.println("*******************NOT NULL Country is : " + countryName + " team name is " + product.getProductPurchase().getTeam() + " amount Purchase " + purchaseAmount);
                            countrySalesPair.put(countryName, countrySalesPair.get(countryName) + purchaseAmount);
                        }
                    }
                }

                for (int countryName = 0; countryName < teamCountry.size(); countryName++) {

                    String monthsInWords = salesMonths.get(i);
                    int monthInt = 0;
                    if (monthsInWords.substring(0, 1).equals("0")) {
                        monthInt = Integer.parseInt(monthsInWords.substring(1));
                        monthsInWords = new DateFormatSymbols().getMonths()[monthInt - 1];
                        //salesMonths.get(i).substring(1);
                    } else {
                        monthInt = Integer.parseInt(monthsInWords.substring(0));
                        monthsInWords = new DateFormatSymbols().getMonths()[monthInt - 1];
                    }

                    System.out.println("$$$$$$$$$$$$$$$$$$$ country name : " + teamCountry.get(countryName) + " sales($) " + countrySalesPair.get(teamCountry.get(countryName)) + " Months " + monthsInWords);
                    salesByCountry.add(new CountrySales(teamCountry.get(countryName), countrySalesPair.get(teamCountry.get(countryName)), monthsInWords));

                }

                countrySalesByMonths.add(salesByCountry);

            }

        } catch (Exception ex) {

        }
        return countrySalesByMonths;
    }

    @Override
    public List<List<CountrySales>> retrieveAllSalesByMonthsByRange(String dateFrom, String dateTo) {

        List<List<CountrySales>> countrySalesByMonths = new ArrayList<>();
        try {

            System.out.println("START " + dateFrom + " End " + dateTo);
            Query getMonths = em.createQuery("SELECT DISTINCT(SUBSTRING(o.datePaid, 6,2)) FROM CustomerOrder o WHERE SUBSTRING(o.datePaid,1,10) BETWEEN :start AND :end ORDER BY o.datePaid");
            getMonths.setParameter("start", dateFrom);
            getMonths.setParameter("end", dateTo);

            List<String> salesMonths = getMonths.getResultList();
            //  System.out.println("********* sales months is " + salesMonths.size() );

            for (int i = 0; i < salesMonths.size(); i++) {

                List<CountrySales> salesByCountry = new ArrayList<>();
                String month = salesMonths.get(i);

                System.out.println("************ MONTH IS " + month);

                Query getCountrySalesByMonth = em.createQuery("SELECT o FROM CustomerOrder o WHERE  SUBSTRING(o.datePaid , 1,4) =:year AND SUBSTRING(o.datePaid , 6,2) =:month  ORDER BY o.datePaid");
                getCountrySalesByMonth.setParameter("year", "2018");
                getCountrySalesByMonth.setParameter("month", month);
                List<CustomerOrder> orders = getCountrySalesByMonth.getResultList();

                System.out.println("******** sales records is " + orders.size());
                HashMap<String, Double> countrySalesPair = new HashMap<>();
                List<String> teamCountry = new ArrayList<>();

                for (int j = 0; j < orders.size(); j++) {
                    List<ProductPurchase> products = orders.get(j).getProductPurchase();

                    for (ProductPurchase product : products) {

                        System.out.println("**************** Product Purchase name " + product.getProductPurchase().getProductName());

                        String countryName = product.getProductPurchase().getCountry();
                        double purchaseAmount = product.getPricePurchase();

                        if (countrySalesPair.get(countryName) == null) {
                            System.out.println("*******************Country is : " + countryName + " team name is " + product.getProductPurchase().getTeam() + " amount Purchase " + purchaseAmount);
                            teamCountry.add(countryName); //add new country of the team
                            countrySalesPair.put(countryName, purchaseAmount);
                        } else if (countrySalesPair.get(countryName) != null) //previously added to hashmap
                        {

                            System.out.println("*******************NOT NULL Country is : " + countryName + " team name is " + product.getProductPurchase().getTeam() + " amount Purchase " + purchaseAmount);
                            countrySalesPair.put(countryName, countrySalesPair.get(countryName) + purchaseAmount);
                        }

                    }

                }

                for (int countryName = 0; countryName < teamCountry.size(); countryName++) {

                    String monthsInWords = salesMonths.get(i);
                    int monthInt = 0;
                    if (monthsInWords.substring(0, 1).equals("0")) {
                        monthInt = Integer.parseInt(monthsInWords.substring(1));
                        monthsInWords = new DateFormatSymbols().getMonths()[monthInt - 1];
                        //salesMonths.get(i).substring(1);
                    } else {
                        monthInt = Integer.parseInt(monthsInWords.substring(0));
                        monthsInWords = new DateFormatSymbols().getMonths()[monthInt - 1];
                    }

                    System.out.println("$$$$$$$$$$$$$$$$$$$ country name : " + teamCountry.get(countryName) + " sales($) " + countrySalesPair.get(teamCountry.get(countryName)) + " Months " + monthsInWords);
                    salesByCountry.add(new CountrySales(teamCountry.get(countryName), countrySalesPair.get(teamCountry.get(countryName)), monthsInWords));

                }

                countrySalesByMonths.add(salesByCountry);

            }

        } catch (Exception ex) {

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

                System.out.println("******** TOTAL PURCAHSE FROM  " + (String) salesRecords.get(i)[0] + " is " + (Double) salesRecords.get(i)[2]);

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
    public List<String[]> retrieveSalesByTeamAndCountryByRange(String dateFrom, String dateTo) {

        Double totalSales = 0.0;
        System.out.println("retrieveSalesByTeamAndCountryByRange START " + dateFrom + " End " + dateTo);

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

            Query q = em.createQuery("SELECT  p.productPurchase.team  , p.productPurchase.country , SUM(p.pricePurchase * p.qtyPurchase) FROM ProductPurchase p WHERE SUBSTRING(p.order.datePaid , 1,10) BETWEEN :start AND :end  GROUP BY p.productPurchase.country, p.productPurchase.team ");
            q.setParameter("start", dateFrom);
            q.setParameter("end", dateTo);
            List<Object[]> salesRecords = q.getResultList();

            for (int i = 0; i < salesRecords.size(); i++) {

                System.out.println("******** TOTAL PURCAHSE FROM  " + (String) salesRecords.get(i)[0] + " is " + (Double) salesRecords.get(i)[2]);

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
    public List<TopProductByCode> getProductsSumByQuantityPurchaseByProductCode() {

        List<TopProductByCode> topTenProductByCode = new ArrayList<>();
        try {

            List<String> productCodes = new ArrayList<>();
            List<CustomerOrder> orders = new ArrayList<>();

            DecimalFormat df = new DecimalFormat("#.##");
            HashMap<String, Integer> productCodeQuantityPair = new HashMap<>();
            HashMap<String, Double> productCodeSalesPair = new HashMap<>();
            Query q = em.createQuery("SELECT o FROM CustomerOrder o");
            orders = q.getResultList();
            System.out.println("************ orders size is " + orders.size());
            for (int i = 0; i < orders.size(); i++) { //top 10 

                List<ProductPurchase> products = new ArrayList<>();

                products = orders.get(i).getProductPurchase();

                for (ProductPurchase p : products) {
                    System.out.println("************PC ProductPurchase p : products " + p.getProductPurchase().getProductCode());
                    String productCode = p.getProductPurchase().getProductCode();
                    double pricePurchase = p.getPricePurchase() * p.getQtyPurchase();
                    int qtyPurchase = p.getQtyPurchase();

                    if (productCodeSalesPair.get(productCode) == null) {
                        System.out.println("*******************PC product code : " + productCode + " Code is null" + " QTY 1");
                        productCodes.add(productCode);
                        productCodeSalesPair.put(productCode, pricePurchase);
                        productCodeQuantityPair.put(productCode, qtyPurchase);

                    } else if (productCodeSalesPair.get(productCode) != null) { // exisiting product code

                        System.out.println("*******************PC product Code : " + productCode + "Code is not null. QTY = " + (productCodeQuantityPair.get(productCode) + qtyPurchase));
                        System.out.println("*******************PC product Code : " + productCode + "Code is not null. Purchase is = " + (productCodeSalesPair.get(productCode) + pricePurchase));
                        productCodeQuantityPair.put(productCode, productCodeQuantityPair.get(productCode) + qtyPurchase);
                        productCodeSalesPair.put(productCode, productCodeSalesPair.get(productCode) + pricePurchase);
                    }
                }

            }

            System.out.println("*****************PC POPULATE BACK THE DATA TO TOP PRODUCY BY CODE. Size is " + productCodes.size());

            for (int i = 0; i < productCodes.size(); i++) {

                String productCode = productCodes.get(i);
                topTenProductByCode.add(new TopProductByCode(productCode, productCodeQuantityPair.get(productCode), Double.parseDouble(df.format(productCodeSalesPair.get(productCode)))));
            }

            Collections.sort(topTenProductByCode, TopProductByCode.BY_TOTAL_PURCHASE);

            //set the rank
            for (int i = 0; i < topTenProductByCode.size(); i++) {
                topTenProductByCode.get(i).setRank(i + 1);
                double tPurchase = topTenProductByCode.get(i).getAmountPurchased();
                double tTotal = topTenProductByCode.get(i).getQuantityOrdered();
                double avgProfit = Double.parseDouble(df.format(tPurchase / tTotal));
                topTenProductByCode.get(i).setAverageProfit(avgProfit);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return topTenProductByCode;
    }

    @Override
    public List<TopProductByTeam> getProductsSumByQuantityPurchaseByTeam() {

        List<String> teamNames = new ArrayList<>();
        List<CustomerOrder> orders = new ArrayList<>();
        List<TopProductByTeam> topTenProductByTeam = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#.##");
        HashMap<String, Integer> productTeamQuantityPair = new HashMap<>();
        HashMap<String, Double> productTeamSalesPair = new HashMap<>();
        Query q = em.createQuery("SELECT o FROM CustomerOrder o");
        orders = q.getResultList();
        System.out.println("************ orders size is " + orders.size());
        for (int i = 0; i < orders.size(); i++) { //top 10 

            List<ProductPurchase> products = new ArrayList<>();
            products = orders.get(i).getProductPurchase();

            for (ProductPurchase p : products) {
                String productTeam = p.getProductPurchase().getTeam();
                int qtyPurchase = p.getQtyPurchase();
                double pricePurchase = p.getPricePurchase() * qtyPurchase;

                if (productTeamSalesPair.get(productTeam) == null) {
                    System.out.println("*******************PT product Team : " + productTeam + " Team is null" + " QTY 1");
                    teamNames.add(productTeam);
                    productTeamSalesPair.put(productTeam, pricePurchase);
                    productTeamQuantityPair.put(productTeam, qtyPurchase);

                } else if (productTeamSalesPair.get(productTeam) != null) { // exisiting product code

                    System.out.println("*******************PT product Team : " + productTeam + " Team is not null. QTY = " + (productTeamQuantityPair.get(productTeam) + qtyPurchase));
                    System.out.println("*******************PT product Team : " + productTeam + " Team is not null. Purchase is = " + (productTeamSalesPair.get(productTeam) + pricePurchase));
                    productTeamQuantityPair.put(productTeam, productTeamQuantityPair.get(productTeam) + qtyPurchase);
                    productTeamSalesPair.put(productTeam, productTeamSalesPair.get(productTeam) + pricePurchase);
                }
            }

        }

        System.out.println("***************** POPULATE BACK THE DATA TO TOP PRODUCY BY Nmae");

        for (int i = 0; i < teamNames.size(); i++) {

            String productTeam = teamNames.get(i);
            System.out.println("***************PT product Team " + productTeam + " purcahses : " + Double.parseDouble(df.format(productTeamSalesPair.get(productTeam))));
            topTenProductByTeam.add(new TopProductByTeam(productTeam, productTeamQuantityPair.get(productTeam), Double.parseDouble(df.format(productTeamSalesPair.get(productTeam)))));
        }

        Collections.sort(topTenProductByTeam, TopProductByTeam.BY_TOTAL_PURCHASE);

        //set the rank
        for (int i = 0; i < topTenProductByTeam.size(); i++) {
            topTenProductByTeam.get(i).setRank(i + 1);
            double tPurchase = topTenProductByTeam.get(i).getAmountPurchased();
            double tTotal = topTenProductByTeam.get(i).getQuantityOrdered();
            double avgProfit = Double.parseDouble(df.format(tPurchase / tTotal));
            topTenProductByTeam.get(i).setAverageProfit(avgProfit);
        }

        return topTenProductByTeam;
    }

    @Override
    public List<PerformanceBoard> getPerformanceInformation() {

        List<PerformanceBoard> boards = new ArrayList<>();
        int noOfBoard = 4;

        for (int i = 0; i < noOfBoard; i++) {

            if (i == 0) {
                System.out.println("****************** GETTONG CUSTOMER BOARD");
                boards.add(customerBoard());
            } else if (i == 1) {
                System.out.println("****************** GETTONG SALES BOARD");
                boards.add(salesBoard());
            } else if (i == 2) {
                boards.add(orderBoard());
            } else if (i == 3) {
                boards.add(customerSatisfactionBoard());
            }

        }

        return boards;

    }

    private PerformanceBoard customerBoard() {

        System.out.println("******************CUSTOMER BOARD");
        PerformanceBoard customer = new PerformanceBoard();
        try {
            DecimalFormat df = new DecimalFormat("#0.00");
            long totalRegisteredCustomer = 0;
            long totalRegisteredCustomer_CurrentMonth = 0;
            double percentage_increase = 0.0;

            Date today = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(today);
            int currentMonth = cal.get(Calendar.MONTH) + 1;
            String currentMonthInString = String.valueOf(currentMonth);
            if (currentMonth < 10) {
                currentMonthInString = "0" + currentMonthInString;
            }

            List<Long> countCustomer = new ArrayList<>();

            Query query = em.createQuery("SELECT COUNT(c.id) FROM Customer c");
            countCustomer = query.getResultList();
            totalRegisteredCustomer = countCustomer.get(0);
            //  SELECT o FROM CustomerOrder o WHERE  SUBSTRING(o.datePaid , 1,4) =:year AND SUBSTRING(o.datePaid , 6,2) =:month  ORDER BY o.datePaid
            query = em.createQuery("SELECT COUNT(c.id) FROM Customer c WHERE SUBSTRING(c.dateRegistered ,6,2) =:month");
            query.setParameter("month", currentMonthInString);
            countCustomer = query.getResultList();

            totalRegisteredCustomer_CurrentMonth = countCustomer.get(0);

            customer.setTitleInformation("CUSTOMER INFORMATION");
            customer.setTotalInformation("TOTAL CUSTOMER");
            System.out.println("****************** tota; customer " + String.valueOf(totalRegisteredCustomer));
            customer.setTotalValue(String.valueOf(totalRegisteredCustomer));
            customer.setCurrentMonthInformation(String.valueOf(totalRegisteredCustomer_CurrentMonth) + " NEW CUSTOMER(S)");
            //
            percentage_increase = ((double) totalRegisteredCustomer_CurrentMonth / totalRegisteredCustomer) * 100;
            System.out.println("************** pecentage increase " + percentage_increase);
            // customer.setFigurePercentage(figurePercentage + "%");
            if (percentage_increase > 0) {
                customer.setFigureInformation(df.format(percentage_increase) + "% Of Total Customer");
                //"CUSTOMER INCREASE BY " + 
                customer.setFigureStatus("Increase");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return customer;
    }

    private PerformanceBoard salesBoard() {

        System.out.println("******************SALES BOARD");
        PerformanceBoard sales = new PerformanceBoard();
        try {
            DecimalFormat df = new DecimalFormat("#0.00");
            double totalSales = 0;
            double totalSales_CurrentMonth = 0;
            double totalSales_PrevMonth = 0;
            double percentage_increase = 0.0;
            double percentage_diff = 0.0;
            Date today = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(today);
            int currentMonth = cal.get(Calendar.MONTH) + 1;
            int prevMonth = currentMonth - 1;
            if (prevMonth == 0) {
                prevMonth = 12; //last month is december   
            }

            String currentMonthInString = String.valueOf(currentMonth);
            String prevMonthInString = String.valueOf(prevMonth);
            if (currentMonth < 10) {
                currentMonthInString = "0" + currentMonthInString;
            }
            if (prevMonth < 10) {
                prevMonthInString = "0" + prevMonthInString;
            }

            System.out.println("******Current month is " + currentMonthInString + " PREV  is " + prevMonthInString);
            List<Double> salesSum = new ArrayList<>();

            Query query = em.createQuery("SELECT SUM(o.totalAmount) FROM CustomerOrder o");
            salesSum = query.getResultList();
            totalSales = salesSum.get(0);
            //  SELECT o FROM CustomerOrder o WHERE  SUBSTRING(o.datePaid , 1,4) =:year AND SUBSTRING(o.datePaid , 6,2) =:month  ORDER BY o.datePaid
            query = em.createQuery("SELECT SUM(o.totalAmount) FROM CustomerOrder o WHERE SUBSTRING(o.datePaid ,6,2) =:month");
            query.setParameter("month", currentMonthInString);
            System.out.println("*********** QUERY IS " + query.getResultList());
            salesSum = query.getResultList();

            if (salesSum.get(0) != null) {
                totalSales_CurrentMonth = salesSum.get(0);
            } else {
                totalSales_CurrentMonth = 0;
            }

            query = em.createQuery("SELECT SUM(o.totalAmount) FROM CustomerOrder o WHERE SUBSTRING(o.datePaid ,6,2) =:month");
            query.setParameter("month", prevMonthInString);
            salesSum = query.getResultList();
            if (salesSum.get(0) != null) {
                totalSales_PrevMonth = salesSum.get(0);
            } else {
                totalSales_PrevMonth = 0;
            }

            sales.setTitleInformation("SALES INFORMATION");
            sales.setTotalInformation("TOTAL SALES");
            System.out.println("****************** TOTAL SALES " + totalSales);
            System.out.println("****************** Curent SALES " + totalSales_CurrentMonth);
            System.out.println("****************** Prev SALES " + totalSales_PrevMonth);
            sales.setTotalValue("$" + df.format(totalSales));
            sales.setCurrentMonthInformation("$" + df.format(totalSales_CurrentMonth) + " SALES THIS MONTH");
            //
            percentage_diff = totalSales_PrevMonth - totalSales_CurrentMonth;
            if (percentage_diff > 0) {
                System.out.println("****************** SALES DECREASE BY  " + df.format(percentage_diff / totalSales_PrevMonth * 100));
                sales.setFigureInformation(df.format(percentage_diff / totalSales_PrevMonth * 100) + "% VS Previous Month");
                //SALES DECREASE BY 
                sales.setFigureStatus("Decrease");
            } else if (percentage_diff < 0) {
                System.out.println("****************** SALES INCREASE BY  " + df.format(percentage_diff / totalSales_PrevMonth * 100 * -1));
                sales.setFigureInformation(df.format(percentage_diff / totalSales_PrevMonth * 100 * -1) + "% VS Previous MOnth");
                sales.setFigureStatus("Increase");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sales;
    }

    private PerformanceBoard orderBoard() {

        System.out.println("******************ORDER BOARD");
        PerformanceBoard order = new PerformanceBoard();
        try {
            DecimalFormat df = new DecimalFormat("#0.00");
            long totalOrder = 0;
            long totalOrder_CurrentMonth = 0;
            long totalOrder_PrevMonth = 0;
            double percentage_diff = 0.0;

            Date today = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(today);
            int currentMonth = cal.get(Calendar.MONTH) + 1;
            int prevMonth = currentMonth - 1;
            if (prevMonth == 0) {
                prevMonth = 12; //last month is december   
            }

            String currentMonthInString = String.valueOf(currentMonth);
            String prevMonthInString = String.valueOf(prevMonth);
            if (currentMonth < 10) {
                currentMonthInString = "0" + currentMonthInString;
            }
            if (prevMonth < 10) {
                prevMonthInString = "0" + prevMonthInString;
            }

            List<CustomerOrder> allOrders = new ArrayList<>();
            List<CustomerOrder> currentMonthOrder = new ArrayList<>();
            List<CustomerOrder> prevMonthOrder = new ArrayList<>();

            List<Double> salesSum = new ArrayList<>();

            Query query = em.createQuery("SELECT o FROM CustomerOrder o");
            allOrders = query.getResultList();

            if (!allOrders.isEmpty()) {

                for (CustomerOrder o : allOrders) {
                    //SELECT SUM(p.qtyPurchase) FROM ProductPurchase p WHERE p.`ORDER_ID` = 8
                    Query getQuantiyPurchase = em.createQuery("SELECT SUM(p.qtyPurchase) FROM ProductPurchase p WHERE  p.order.id = :customerOrderId ");
                    getQuantiyPurchase.setParameter("customerOrderId", o.getId());
                    List<Long> qtyPurchase = getQuantiyPurchase.getResultList();
                    totalOrder += qtyPurchase.get(0);

                }
            }

            query = em.createQuery("SELECT o FROM CustomerOrder o WHERE SUBSTRING(o.datePaid,6,2) =:cMonth");
            query.setParameter("cMonth", currentMonthInString);
            currentMonthOrder = query.getResultList();

            if (!currentMonthOrder.isEmpty()) {
                for (CustomerOrder o : currentMonthOrder) {
                    //SELECT SUM(p.qtyPurchase) FROM ProductPurchase p WHERE p.`ORDER_ID` = 8
                    Query getQuantiyPurchase = em.createQuery("SELECT SUM(p.qtyPurchase) FROM ProductPurchase p WHERE  p.order.id = :customerOrderId ");
                    getQuantiyPurchase.setParameter("customerOrderId", o.getId());
                    List<Long> qtyPurchase = getQuantiyPurchase.getResultList();
                    totalOrder_CurrentMonth += qtyPurchase.get(0);

                }
            }

            query = em.createQuery("SELECT o FROM CustomerOrder o WHERE SUBSTRING(o.datePaid,6,2) =:cMonth");
            query.setParameter("cMonth", prevMonthInString);
            prevMonthOrder = query.getResultList();

            if (!prevMonthOrder.isEmpty()) {
                for (CustomerOrder o : prevMonthOrder) {
                    //SELECT SUM(p.qtyPurchase) FROM ProductPurchase p WHERE p.`ORDER_ID` = 8
                    Query getQuantiyPurchase = em.createQuery("SELECT SUM(p.qtyPurchase) FROM ProductPurchase p WHERE  p.order.id = :customerOrderId ");
                    getQuantiyPurchase.setParameter("customerOrderId", o.getId());
                    List<Long> qtyPurchase = getQuantiyPurchase.getResultList();
                    totalOrder_PrevMonth += qtyPurchase.get(0);

                }
            }

            order.setTitleInformation("ORDER INFORMATION");
            order.setTotalInformation("TOTAL PRODUCTS ORDERED");
            System.out.println("****************** TOTAL ORDER" + totalOrder);
            System.out.println("****************** Curent ORDER " + totalOrder_CurrentMonth);
            System.out.println("****************** Prev ORDER " + totalOrder_PrevMonth);
            order.setTotalValue(String.valueOf((int) totalOrder));
            order.setCurrentMonthInformation(String.valueOf((int) totalOrder_CurrentMonth) + " ORDER(S) THIS MONTH");
            //
            percentage_diff = totalOrder_PrevMonth - totalOrder_CurrentMonth;
            if (percentage_diff > 0) {
                System.out.println("****************** ORDER DECREASE BY  " + df.format(percentage_diff / totalOrder_PrevMonth * 100));
                order.setFigureInformation(df.format(percentage_diff / totalOrder_PrevMonth * 100) + "% VS Previous Month");
                //SALES DECREASE BY 
                order.setFigureStatus("Decrease");
            } else if (percentage_diff < 0) {
                System.out.println("****************** Order INCREASE BY  " + df.format(percentage_diff / totalOrder_PrevMonth * 100 * -1));
                order.setFigureInformation(df.format(percentage_diff / totalOrder_PrevMonth * 100 * -1) + "% VS Previous Month");
                order.setFigureStatus("Increase");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return order;
    }

    private PerformanceBoard customerSatisfactionBoard() {

        System.out.println("******************Rating BOARD");
        PerformanceBoard customerSatisfaction = new PerformanceBoard();
        try {

            //SELECT count(*) FROM productreview ;
            DecimalFormat df = new DecimalFormat("#0.0");
            long noOfRating = 0;
            long noOfRating_aboveFour = 0;
            long noOfRating__belowFour = 0;
            double percentage_diff = 0.0;
            double aboveFour_Percentage = 0.0;

            Date today = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(today);
            int currentMonth = cal.get(Calendar.MONTH) + 1;
            int prevMonth = currentMonth - 1;
            if (prevMonth == 0) {
                prevMonth = 12; //last month is december   
            }

            String currentMonthInString = String.valueOf(currentMonth);
            String prevMonthInString = String.valueOf(prevMonth);
            if (currentMonth < 10) {
                currentMonthInString = "0" + currentMonthInString;
            }
            if (prevMonth < 10) {
                prevMonthInString = "0" + prevMonthInString;
            }

            List<Double> rating = new ArrayList<>();
            List<Long> no_Rating = new ArrayList<>();
            List<Long> no_RatingAboveFour = new ArrayList<>(); //include 4
            List<Long> no_RatingBelowFour = new ArrayList<>();
            String ratingMessage = "";
            boolean hasRating = false;
            double averageRating = 0;

            Query query = em.createQuery("SELECT AVG(p.rating) FROM ProductReview p");
            String custSatisfaction = "";
            rating = query.getResultList();
            if (rating.get(0) != null) {
                averageRating = rating.get(0);
                hasRating = true;
            } else {
                averageRating = 0;
                ratingMessage = "No Rating From Customer!";
            }

            if (averageRating >= 3 && averageRating < 4) {
                ratingMessage = "Customer Satisfaction Level Is Good";
            } else if (averageRating >= 4) {
                ratingMessage = "Customer Satisfaction Level Is Excellent";
            } else if (averageRating < 3 && hasRating) {
                ratingMessage = "Customer Satisfaction Level Is Bad";
            }

            query = em.createQuery("SELECT COUNT(p.id) FROM ProductReview p");
            no_Rating = query.getResultList();
            noOfRating = no_Rating.get(0);

            query = em.createQuery("SELECT COUNT(p.id) FROM ProductReview p WHERE p.rating >= 4 ");
            no_RatingAboveFour = query.getResultList();
            noOfRating_aboveFour = no_RatingAboveFour.get(0);

            query = em.createQuery("SELECT COUNT(p.id) FROM ProductReview p WHERE p.rating < 4 ");
            no_RatingBelowFour = query.getResultList();
            noOfRating__belowFour = no_RatingBelowFour.get(0);

            customerSatisfaction.setTitleInformation("CUSTOMER SATISFACTION");
            customerSatisfaction.setTotalInformation("AVERAGE RATING");
            customerSatisfaction.setTotalValue(df.format(averageRating));
            System.out.println("**************** above four % " + noOfRating_aboveFour);
            aboveFour_Percentage = (double) noOfRating_aboveFour / noOfRating;
            double ratingAbove4_Perc = (double) noOfRating_aboveFour / noOfRating * 100;

            customerSatisfaction.setCurrentMonthInformation(df.format((double) noOfRating_aboveFour / noOfRating * 100) + "% RATE ABOVE 3");
            customerSatisfaction.setFigureInformation(ratingMessage);
            customerSatisfaction.setFigureStatus("na");
            //
            /*  percentage_diff = totalOrder_PrevMonth - totalOrder_CurrentMonth;
         if(percentage_diff > 0 )
         {
           System.out.println("****************** ORDER DECREASE BY  " + df.format(percentage_diff/totalOrder_PrevMonth * 100));
           order.setFigureInformation(df.format(percentage_diff/totalOrder_PrevMonth * 100) + "% VS Previous Month" );
           //SALES DECREASE BY 
           order.setFigureStatus("Decrease");
        }
         else if(percentage_diff < 0 ){
           System.out.println("****************** Order INCREASE BY  " + df.format(percentage_diff/totalOrder_PrevMonth * 100 * -1));
           order.setFigureInformation(df.format(percentage_diff/totalOrder_PrevMonth * 100 * -1) + "% VS Previous Month" );
           order.setFigureStatus("Increase");
         }*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return customerSatisfaction;

    }

}

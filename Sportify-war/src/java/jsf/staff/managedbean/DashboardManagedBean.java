/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.staff.managedbean;

import ejb.session.stateless.CustomerControllerLocal;
import ejb.session.stateless.CustomerOrderControllerRemote;
import ejb.session.stateless.DashboardControllerLocal;
import ejb.session.stateless.ProductControllerLocal;
import entity.Customer;
import entity.Product;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;
import util.helperClass.CountrySales;
import util.helperClass.TopTenCustomer;
import javax.faces.application.FacesMessage;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.chart.BarChartModel;
import util.exception.CustomerSignUpException;
import util.helperClass.PerformanceBoard;
import util.helperClass.TopCustomerProduct;
import util.helperClass.TopProductByCode;
import util.helperClass.TopProductByTeam;

/**
 *
 * @author David
 */
@Named(value = "dashboardManagedBean")
@ViewScoped
public class DashboardManagedBean implements Serializable {

    @EJB(name = "CustomerControllerLocal")
    private CustomerControllerLocal customerControllerLocal;

    @EJB(name = "CustomerOrderControllerLocal")
    private CustomerOrderControllerRemote customerOrderControllerLocal;

    @EJB(name = "ProductControllerLocal")
    private ProductControllerLocal productControllerLocal;

    @EJB(name = "DashboardControllerLocal")
    private DashboardControllerLocal dashboardControllerLocal;

    /**
     * Creates a new instance of DashboardManagedBean
     */
    final String[] monthsArray = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    ;
    private LineChartModel salesLineChart;

    private HorizontalBarChartModel horizontalRating;

    private Date salesDate;
    private Date currentDate;
    private DashboardModel dashboardPosition;
    private List<Product> products;
    private Product selectedProducts;

    //Top customer. 
    private List<TopTenCustomer> topCustomers;
    private String selectFilterTopCustomer;
    private TopTenCustomer selectedCustomer;
    private Date customerFrom;
    private Date customerTo;
    private PieChartModel customerProductPie;

    //Top product
    private StreamedContent productFile;
    private List<TopProductByCode> topProductsCode;
    private List<TopProductByTeam> topProductsTeam;
    private String productGroupByType;
    private String selectFilterTopProduct;

    //overview
    private List<String> totalSalesByTeamFilter;
    private BarChartModel stackBarModel;
    private PieChartModel salesByTeamPieModel;
    private List<String[]> totalSalesByTeam;
    private List<List<CountrySales>> totalSalesByMonthsCountry;
    private String selectedTotalSalesByTeamPieChart;
    private List<PerformanceBoard> boardsInformation;
    private int number;

    public DashboardManagedBean() {
        salesByTeamPieModel = new PieChartModel();
        totalSalesByMonthsCountry = new ArrayList<>();
        currentDate = new Date();
        totalSalesByTeam = new ArrayList<>();
        totalSalesByTeamFilter = new ArrayList<>();
        products = new ArrayList<>();
        selectedProducts = new Product();
        topCustomers = new ArrayList<>();
        selectedCustomer = new TopTenCustomer();
        customerProductPie = new PieChartModel();
        InputStream stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/images/image_ss_1.jpg");
        productFile = new DefaultStreamedContent(stream, "image/jpg", "test.jpg");
        topProductsCode = new ArrayList<>();
        topProductsTeam = new ArrayList<>();
        productGroupByType = "PC";
        selectFilterTopProduct = "TP";
        boardsInformation = new ArrayList<>();

        //overview
    }

    @PostConstruct
    public void postConstruct() {

        try {

            boardsInformation = dashboardControllerLocal.getPerformanceInformation();
            topProductsCode = dashboardControllerLocal.getProductsSumByQuantityPurchaseByProductCode();
            topProductsTeam = dashboardControllerLocal.getProductsSumByQuantityPurchaseByTeam();
            //  topProductsCode =  topProductsCode.subList(0, 10);
            topCustomers = customerOrderControllerLocal.RetrieveTopTenCustomersByOrder();
            //  customerOrderControllerLocal.RetrieveAllCustomerOrder();
            totalSalesByMonthsCountry = dashboardControllerLocal.retrieveAllSalesByMonths();
            totalSalesByTeam = dashboardControllerLocal.retrieveSalesByTeamAndCountry();
            totalSalesByTeamFilter = dashboardControllerLocal.getCountries();
            products = productControllerLocal.retrieveProduct();

            dashboardPosition = new DefaultDashboardModel();
            DashboardColumn column1 = new DefaultDashboardColumn();
            DashboardColumn column2 = new DefaultDashboardColumn();

            column1.addWidget("barchartToggle");

            column1.addWidget("salesCountry");
            column2.addWidget("stackChart");

            dashboardPosition.addColumn(column1);
            dashboardPosition.addColumn(column2);
            //   createLineModels();
            createRatingBarModels();
            salesByTeamPieModel = getSalesByTeamPieChart(0);
            createCountrySalesStackBarModel();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private BarChartModel initCountrySalesStackBarModel() {
        BarChartModel model = new BarChartModel();
        int noOfCountry = totalSalesByTeamFilter.size();
        HashMap<String, ChartSeries> countrySeriesPair = new HashMap<>();
        DecimalFormat df = new DecimalFormat("#0.00");
        ChartSeries countrySeries = new ChartSeries();
        List<ChartSeries> stackBarChartSeries = new ArrayList<>();

        System.out.println("*************** INITIALIZING STACK BAR");
        try {

            for (int i = 0; i < noOfCountry; i++) {

                countrySeries = new ChartSeries();
                String countryName = totalSalesByTeamFilter.get(i);
                System.out.println("*************** COUNRY Name " + countryName);
                countrySeries.setLabel(countryName); //country Name
                countrySeriesPair.put(countryName, countrySeries);

            }

            for (int totalCountrySales = 0; totalCountrySales < totalSalesByMonthsCountry.size(); totalCountrySales++) {

                int countrySize = totalSalesByMonthsCountry.get(totalCountrySales).size();
                List<CountrySales> sales = totalSalesByMonthsCountry.get(totalCountrySales);
                // System.out.println("*************** Inside the TOTALSALES BY COUNTRY. Size " + countrySize);
                String countryNameFromSalesRecord = "";
                String salesInString = "";
                String month = "";
                List<String> countryHasSales = new ArrayList<>();
                for (int i = 0; i < countrySize; i++) {

                    countryNameFromSalesRecord = sales.get(i).getCountryName();
                    salesInString = sales.get(i).getSales().toString();
                    month = sales.get(i).getMonth();
                    // System.out.println("*************** COUNRY Name " + countryNameFromSalesRecord + " salesinString " + salesInString + " MONTH : " + month);
                    countrySeries = countrySeriesPair.get(countryNameFromSalesRecord);
                    // System.out.println("COUNTRY LABEL " + countrySeries.getLabel());
                    countrySeries.set(month, Double.parseDouble(salesInString));
                    countryHasSales.add(countryNameFromSalesRecord);

                    countrySeriesPair.put(countryNameFromSalesRecord, countrySeries); //update chartSeries Value.

                }

                if (countrySize != noOfCountry) { //there are some country thats has no sales in that particular month

                    for (int country = 0; country < noOfCountry; country++) {

                        if (!countryHasSales.contains(totalSalesByTeamFilter.get(country))) {

                            //    System.out.println("********* Country " + totalSalesByTeamFilter.get(country) + " has no sales in Month " + month);
                            countrySeries = countrySeriesPair.get(totalSalesByTeamFilter.get(country));
                            countrySeries.set(month, 0);
                            countrySeriesPair.put(totalSalesByTeamFilter.get(country), countrySeries);
                        }

                    }

                }

            }

            //add value to model
            for (int i = 0; i < noOfCountry; i++) {
                String countryName = totalSalesByTeamFilter.get(i);
                model.addSeries(countrySeriesPair.get(countryName));
                /*System.out.println("COUNTRY LABEL " + countrySeriesPair.get(countryName).getLabel());
           System.out.println("COUNTRY DATA " + countrySeriesPair.get(countryName).getData().get("January"));
            System.out.println("COUNTRY DATA " + countrySeriesPair.get(countryName).getData().get("February"));
           System.out.println("COUNTRY DATA " + countrySeriesPair.get(countryName).getData().get("March"));*/

            }

            //model.addSeries(italy);
            // model.addSeries(spain);
            //  model.addSeries(england);
            model.setStacked(true);
            model.setExtender("barExtender");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return model;
    }

    private void createCountrySalesStackBarModel() {
        setBarModel(initCountrySalesStackBarModel());

        getStackBarModel().setTitle("Sales By Country Per Month");
        getStackBarModel().setLegendPosition("se");

        Axis xAxis = getStackBarModel().getAxis(AxisType.X);
        xAxis.setLabel("Month");

        Axis yAxis = getStackBarModel().getAxis(AxisType.Y);
        yAxis.setLabel("Sales($)");
        yAxis.setMin(0);

    }

    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Product Selected", ((Product) event.getObject()).getId().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void searchTopCustomer(ActionEvent actionEvent) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (customerFrom == null || customerTo == null) {
            addMessage("From and To Date is required for searching");
        } else {

            System.out.println("Date selected from " + df.format(customerFrom));
            System.out.println("Date selected to " + df.format(customerTo));
            topCustomers = customerOrderControllerLocal.RetrieveTopTenCustomerByOrderByRanger(df.format(customerFrom), df.format(customerTo));
            //call session bean here.

        }

    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    /* private void createLineModels() {

        Axis yAxis;

        setSalesLineChart(initCategoryModel());
        getSalesLineChart().setTitle("Sportify Sales By Months");
        getSalesLineChart().setLegendPosition(LegendPlacement.OUTSIDE.toString());
        getSalesLineChart().setShowPointLabels(true);
        yAxis = getSalesLineChart().getAxis(AxisType.Y);
       // getSalesLineChart().getAxes().put(AxisType.X, new CategoryAxis("Years"));
        yAxis.setMin(0);
        yAxis.setMax(100);
    }*/
    public void handleReorder(DashboardReorderEvent event) {
        FacesMessage message = new FacesMessage();
        message.setSeverity(FacesMessage.SEVERITY_INFO);
        message.setSummary("Reordered: " + event.getWidgetId());
        message.setDetail("Item index: " + event.getItemIndex() + ", Column index: " + event.getColumnIndex() + ", Sender index: " + event.getSenderColumnIndex());

        //addMessage(message);
    }

    private void createLineModels() {

        if (!totalSalesByMonthsCountry.isEmpty()) {
            Axis yAxis;
            salesLineChart = initCategoryModel();
            salesLineChart.setTitle("Product Sales by Month Year 2018");
            salesLineChart.setLegendPosition("se");
            salesLineChart.setShowPointLabels(true);
            salesLineChart.getAxes().put(AxisType.X, new CategoryAxis("Months"));
            yAxis = salesLineChart.getAxis(AxisType.Y);
            yAxis.setLabel("Sales($)");
            yAxis.setMin(0);

        }

    }

    private LineChartModel initCategoryModel() {

        LineChartModel model = new LineChartModel();
        DecimalFormat formatter = new DecimalFormat("#0.00");
        String monthName = "";
        Double salesAmount = 0.0;

        List<LineChartSeries> countries = new ArrayList<>();

        int totalCountry = totalSalesByMonthsCountry.get(0).size(); //this will let me know the number of country per each month
        System.out.println("****** TOTAL Country is " + totalCountry);

        for (int i = 0; i < totalCountry; i++) {

            System.out.println("Total country is " + totalCountry);

            LineChartSeries c = new LineChartSeries();
            String countryName = totalSalesByMonthsCountry.get(0).get(i).getCountryName();

            if (!countryName.equals("Total")) {
                c.setLabel(countryName + " League");
            } else {
                c.setLabel(countryName);
            }

            for (int j = 0; j < totalSalesByMonthsCountry.size(); j++) {
                System.out.println("j is" + j);

                monthName = totalSalesByMonthsCountry.get(j).get(i).getMonth();
                salesAmount = totalSalesByMonthsCountry.get(j).get(i).getSales();
                System.out.println("Country name in managedb bean is " + countryName + " month is " + monthName + " sales is" + salesAmount);
                //populate chart for i index country firs
                c.set(monthName, Double.parseDouble(formatter.format(salesAmount)));
            }

            c.setSmoothLine(true);

            countries.add(c);
        }

        for (LineChartSeries country : countries) {

            model.addSeries(country);

        }

        model.setSeriesColors("black,FFA500, 3363FF");

//      /*  for (String monthsArray1 : monthsArray) {
//            Object salesByMonth = totalSalesByMonths.get(monthsArray1);
//            if (salesByMonth != null) {
//                System.out.println("Sales is " +  (Double)salesByMonth);
//                england.set(monthsArray1, (Double)salesByMonth);
//            }
//        }*/
        //  LineChartSeries england = new LineChartSeries();
        // england.setLabel("England League ");      
        //        england.set("2014-01-01", 51);
//        england.set("2014-02-01", 22);
//        england.set("2014-03-01", 65);
//        england.set("2014-04-01", 74);
//        england.set("2014-05-01", 80);
//        england.set("2014-06-01", 82); 
//        
//        england.set("2014-01-01", 51);
//        england.set("2014-02-01", 22);
//        england.set("2014-03-01", 65);
//        england.set("2014-04-01", 74);
//        england.set("2014-05-01", 80);
//        england.set("2014-06-01", 82); 
//        
//        /*england.set("Jan" , 1890.20);
//        england.set("Feb" , 2674.11);
//        england.set("Mar" , 3193.82);
//        england.set("Apr" , 3312.89);
//        england.set("May" ,   3210.20);
//        england.set("Jun" ,  4121.71);
//        england.set("Jul" , 5513.66)*/;
//        england.setSmoothLine(true);
//
//        LineChartSeries spain = new LineChartSeries();
//        spain.setLabel("Spain League Product");
//       spain.set("2014-01-01", 90);
//        spain.set("2014-02-01", 89);
//        spain.set("2014-03-01", 76);
//        spain.set("2014-04-01", 60);
//        spain.set("2014-05-01", 64);
//        spain.set("2014-06-01", 70); 
//       
//       /* spain.set("Jan" , 3890.20);
//        spain.set("Feb" , 4674.11);
//        spain.set("Mar" , 2193.82);
//        spain.set("Apr" , 4312.89);
//        spain.set("May" ,   4210.20);
//        spain.set("Jun" ,  2121.71);
//        spain.set("Jul" , 6513.66);*/
//         
//        spain.setSmoothLine(true);
//        dateModel.addSeries(england);
//        dateModel.addSeries(spain);
//
//        dateModel.setSeriesColors("black,FFA500,FFA500,FFA500,A30303");
//        dateModel.setMouseoverHighlight(true);
//        //    dateModel.setSeriesColors("58BA27,FFCC33,F74A4A,black,A30303");      
//        dateModel.getAxis(AxisType.Y).setLabel("Sales($)");
//        DateAxis axis = new DateAxis("Dates");
//        axis.setTickAngle(-50);
//        axis.setMin("2013-12-01");
//        axis.setMax("2014-06-01");
//        axis.setTickFormat("%b, %y");
//        dateModel.getAxes().put(AxisType.X, axis);
//        return dateModel;
        /* for (String monthsArray1 : monthsArray) {
            Object salesByMonth = totalSalesByMonths.get(monthsArray1);
            if (salesByMonth != null) {
                System.out.println("Sales is " +  (Double)salesByMonth);
                england.set(monthsArray1, (Double)salesByMonth);
            }

        }
        england.setSmoothLine(true);


        LineChartSeries spain = new LineChartSeries();
        spain.setLabel("Spain League ");
        spain.set("Jan" , 3890.20);
        spain.set("Feb" , 4674.11);
        spain.set("Mar" , 2193.82);
        spain.set("Apr" , 4312.89);
        spain.set("May" ,   4210.20);
        spain.set("Jun" ,  2121.71);
        spain.set("Jul" , 6513.66);*/
 /*spain.setSmoothLine(true);
         
        model.addSeries(england);
        model.addSeries(spain);
        model.setSeriesColors("black,FFA500");*/
        return model;
    }

    public PieChartModel getSalesByTeamPieChart(int teamIndex) {

        int sizeOfTeam = totalSalesByTeam.size();
        String defaultCountry = totalSalesByTeamFilter.get(teamIndex);  //need to change abit
        System.out.println("********** COUNTRY SELECTED IS " + defaultCountry);
        for (int i = 0; i < sizeOfTeam; i++) {

            String[] teamInfo = totalSalesByTeam.get(i);
            if (teamInfo[1].equals(defaultCountry)) //default country means the first row from the database query
            {
                salesByTeamPieModel.getData().put(teamInfo[0], Double.parseDouble(teamInfo[2]));

            }

        }

        /*   int random1 = (int) (Math.random() * 1000);
        int random2 = (int) (Math.random() * 1000);
        int random3 = (int) (Math.random() * 1000);
        int random4 = (int) (Math.random() * 1000);
        int random5 = (int) (Math.random() * 1000);
        int random6 = (int) (Math.random() * 1000);

        livePieModel.getData().put("Arsenal", random1);
        livePieModel.getData().put("Chealsea", random2);
        livePieModel.getData().put("LiverPool", random3);
        livePieModel.getData().put("Man City", random4);
        livePieModel.getData().put("Man United", random5);
        livePieModel.getData().put("Tottenham", random6);*/
        salesByTeamPieModel.setTitle("Sales($) By Clubs");

        salesByTeamPieModel.setLegendPosition("e");
        salesByTeamPieModel.setExtender("pieExtender");
        //  livePieModel.

        return salesByTeamPieModel;
    }

    private void createRatingBarModels() {
        horizontalRating = new HorizontalBarChartModel();

        ChartSeries goods = new ChartSeries();
        goods.setLabel("Good");
        goods.set("2004", 50);
        goods.set("2005", 96);
        goods.set("2006", 44);
        goods.set("2007", 55);
        goods.set("2008", 25);

        ChartSeries average = new ChartSeries();
        average.setLabel("Average");
        average.set("2004", 52);
        average.set("2005", 60);
        average.set("2006", 82);
        average.set("2007", 35);
        average.set("2008", 120);

        ChartSeries bad = new ChartSeries();
        bad.setLabel("Average");
        bad.set("2004", 52);
        bad.set("2005", 60);
        bad.set("2006", 82);
        bad.set("2007", 35);
        bad.set("2008", 120);

        horizontalRating.addSeries(goods);
        horizontalRating.addSeries(average);
        horizontalRating.addSeries(bad);

        horizontalRating.setTitle("Product Rating");
        horizontalRating.setLegendPosition("se");
        horizontalRating.setStacked(true);

        Axis xAxis = horizontalRating.getAxis(AxisType.X);
        xAxis.setLabel("No Of Rating");
        xAxis.setMin(0);
        xAxis.setMax(200);

        Axis yAxis = horizontalRating.getAxis(AxisType.Y);
        yAxis.setLabel("Rating Category");
    }

    /**
     * @return the salesLineChart
     */
    public LineChartModel getSalesLineChart() {
        return salesLineChart;
    }

    /**
     * @param salesLineChart the salesLineChart to set
     */
    public void setSalesLineChart(LineChartModel salesLineChart) {
        this.salesLineChart = salesLineChart;
    }

    /**
     * @return the horizontalRating
     */
    public HorizontalBarChartModel getHorizontalRating() {
        return horizontalRating;
    }

    /**
     * @param horizontalRating the horizontalRating to set
     */
    public void setHorizontalRating(HorizontalBarChartModel horizontalRating) {
        this.horizontalRating = horizontalRating;
    }

    /**
     * @return the salesDate
     */
    public Date getSalesDate() {
        return salesDate;
    }

    /**
     * @param salesDate the salesDate to set
     */
    public void setSalesDate(Date salesDate) {
        this.salesDate = salesDate;
    }

    /**
     * @return the currentDate
     */
    public Date getCurrentDate() {
        return currentDate;
    }

    /**
     * @param currentDate the currentDate to set
     */
    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    /**
     * @return the totalSalesByMonthsCountry
     */
    public List<List<CountrySales>> getTotalSalesByMonthsCountry() {
        return totalSalesByMonthsCountry;
    }

    /**
     * @param totalSalesByMonthsCountry the totalSalesByMonthsCountry to set
     */
    public void setTotalSalesByMonthsCountry(List<List<CountrySales>> totalSalesByMonthsCountry) {
        this.totalSalesByMonthsCountry = totalSalesByMonthsCountry;
    }

    /**
     * @return the totalSalesByTeam
     */
    public List<String[]> getTotalSalesByTeam() {
        return totalSalesByTeam;
    }

    /**
     * @param totalSalesByTeam the totalSalesByTeam to set
     */
    public void setTotalSalesByTeam(List<String[]> totalSalesByTeam) {
        this.totalSalesByTeam = totalSalesByTeam;
    }

    /**
     * @return the totalSalesByTeamFilter
     */
    public List<String> getTotalSalesByTeamFilter() {
        return totalSalesByTeamFilter;
    }

    /**
     * @param totalSalesByTeamFilter the totalSalesByTeamFilter to set
     */
    public void setTotalSalesByTeamFilter(List<String> totalSalesByTeamFilter) {
        this.totalSalesByTeamFilter = totalSalesByTeamFilter;
    }

    /**
     * @return the dashboardPosition
     */
    public DashboardModel getDashboardPosition() {
        return dashboardPosition;
    }

    /**
     * @param dashboardPosition the dashboardPosition to set
     */
    public void setDashboardPosition(DashboardModel dashboardPosition) {
        this.dashboardPosition = dashboardPosition;
    }

    /**
     * @return the products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * @param products the products to set
     */
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    /**
     * @return the selectedProducts
     */
    public Product getSelectedProducts() {
        return selectedProducts;
    }

    /**
     * @param selectedProducts the selectedProducts to set
     */
    public void setSelectedProducts(Product selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    /**
     * @return the topCustomers
     */
    public List<TopTenCustomer> getTopCustomers() {
        return topCustomers;
    }

    /**
     * @param topCustomers the topCustomers to set
     */
    public void setTopCustomers(List<TopTenCustomer> topCustomers) {
        this.topCustomers = topCustomers;
    }

    public void onTopCustomerFilterChange() {
        System.out.println("SELECTED VALUE IS " + selectFilterTopCustomer);
        //List<Product>

        if (selectFilterTopCustomer.equals("TQTY")) {
            Collections.sort(topCustomers, TopTenCustomer.BY_NO_PURCHASE);
        } else if (selectFilterTopCustomer.equals("AP")) {
            Collections.sort(topCustomers, TopTenCustomer.BY_AVG_PURCHASE);
        } else if (selectFilterTopCustomer.equals("TP")) {
            Collections.sort(topCustomers, TopTenCustomer.BY_TOTAL_PURCHASE);
        }

        for (int reassignRank = 1; reassignRank <= topCustomers.size(); reassignRank++) {
            topCustomers.get(reassignRank - 1).setRank(reassignRank);
        }

    }

    public void onTopProductGroupBy() {
        System.out.println("SELECTED GROUP VALUE IS " + productGroupByType + " SELECTED DDL IS " + selectFilterTopProduct);

        if (productGroupByType.equals("PT")) //product code
        {
            if (selectFilterTopProduct.equals("TP")) {
                Collections.sort(topProductsTeam, TopProductByTeam.BY_TOTAL_PURCHASE);
            } else if (selectFilterTopProduct.equals("TQTY")) //total qty
            {
                Collections.sort(topProductsTeam, TopProductByTeam.BY_NO_PURCHASE);
            } else if (selectFilterTopProduct.equals("AP")) {
                Collections.sort(topProductsTeam, TopProductByTeam.BY_AVG_PURCHASE);
            }

            for (int reassignRank = 1; reassignRank <= topProductsTeam.size(); reassignRank++) {
                topProductsTeam.get(reassignRank - 1).setRank(reassignRank);
            }

        } else if (productGroupByType.equals("PC")) {
            if (selectFilterTopProduct.equals("TP")) {
                Collections.sort(topProductsCode, TopProductByCode.BY_TOTAL_PURCHASE);
            } else if (selectFilterTopProduct.equals("TQTY")) //total qty
            {
                Collections.sort(topProductsCode, TopProductByCode.BY_NO_PURCHASE);
            } else if (selectFilterTopProduct.equals("AP")) {
                Collections.sort(topProductsCode, TopProductByCode.BY_AVG_PURCHASE);
            }

            for (int reassignRank = 1; reassignRank <= topProductsCode.size(); reassignRank++) {
                topProductsCode.get(reassignRank - 1).setRank(reassignRank);
            }

        }

    }

    /**
     * @return the selectFilterTopCustomer
     */
    public String getSelectFilterTopCustomer() {
        return selectFilterTopCustomer;
    }

    /**
     * @param selectFilterTopCustomer the selectFilterTopCustomer to set
     */
    public void setSelectFilterTopCustomer(String selectFilterTopCustomer) {
        this.selectFilterTopCustomer = selectFilterTopCustomer;
    }

    /**
     * @return the selectedCustomer
     */
    public TopTenCustomer getSelectedCustomer() {
        return selectedCustomer;
    }

    /**
     * @param selectedCustomer the selectedCustomer to set
     */
    public void setSelectedCustomer(TopTenCustomer selectedCustomer) {
        System.out.println("SET SELECTEC " + selectedCustomer);
        this.selectedCustomer = selectedCustomer;
        populatedProfileChart();
    }

    /**
     * @return the customerFrom
     */
    public Date getCustomerFrom() {
        return customerFrom;
    }

    /**
     * @param customerFrom the customerFrom to set
     */
    public void setCustomerFrom(Date customerFrom) {
        this.customerFrom = customerFrom;
    }

    /**
     * @return the customerTo
     */
    public Date getCustomerTo() {
        return customerTo;
    }

    /**
     * @param customerTo the customerTo to set
     */
    public void setCustomerTo(Date customerTo) {
        this.customerTo = customerTo;
    }

    public void populatedProfileChart() {
        System.out.println("************************Populating Profile " + selectedCustomer.getFullName());

        setCustomerProductPie(new PieChartModel());
        try {
            List<TopCustomerProduct> custProducts = selectedCustomer.getTotalProducts();
            System.out.println("************************PselectedCustomer.getTotalProducts() " + custProducts.size());

            for (TopCustomerProduct p : custProducts) {
                getCustomerProductPie().set(p.getTeamName(), p.getTotalQtyPurchase());
            }

            getCustomerProductPie().setTitle("Total No. of Purchase by Team");
            getCustomerProductPie().setLegendPosition("e");
            getCustomerProductPie().setFill(true);
            getCustomerProductPie().setShowDataLabels(true);
            getCustomerProductPie().setDiameter(205);
            getCustomerProductPie().setExtender("pieProfileExtender");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * @return the customerProductPie
     */
    public PieChartModel getCustomerProductPie() {
        return customerProductPie;
    }

    /**
     * @param customerProductPie the customerProductPie to set
     */
    public void setCustomerProductPie(PieChartModel customerProductPie) {
        this.customerProductPie = customerProductPie;
    }

    /**
     * @return the productFile
     */
    public StreamedContent getProductFile() {
        return productFile;
    }

    /**
     * @param productFile the productFile to set
     */
    public void setProductFile(StreamedContent productFile) {
        this.productFile = productFile;
    }

    /**
     * @return the topProductsCode
     */
    public List<TopProductByCode> getTopProductsCode() {
        return topProductsCode;
    }

    /**
     * @param topProductsCode the topProductsCode to set
     */
    public void setTopProductsCode(List<TopProductByCode> topProductsCode) {
        this.topProductsCode = topProductsCode;
    }

    /**
     * @return the productGroupByType
     */
    public String getProductGroupByType() {
        return productGroupByType;
    }

    /**
     * @param productGroupByType the productGroupByType to set
     */
    public void setProductGroupByType(String productGroupByType) {
        this.productGroupByType = productGroupByType;
    }

    /**
     * @return the topProductsTeam
     */
    public List<TopProductByTeam> getTopProductsTeam() {
        return topProductsTeam;
    }

    /**
     * @param topProductsTeam the topProductsTeam to set
     */
    public void setTopProductsTeam(List<TopProductByTeam> topProductsTeam) {
        this.topProductsTeam = topProductsTeam;
    }

    /**
     * @return the selectFilterTopProduct
     */
    public String getSelectFilterTopProduct() {
        return selectFilterTopProduct;
    }

    /**
     * @param selectFilterTopProduct the selectFilterTopProduct to set
     */
    public void setSelectFilterTopProduct(String selectFilterTopProduct) {
        this.selectFilterTopProduct = selectFilterTopProduct;
    }

    /**
     * @return the stackBarModel
     */
    public BarChartModel getStackBarModel() {
        return stackBarModel;
    }

    /**
     * @param stackBarModel the stackBarModel to set
     */
    public void setBarModel(BarChartModel stackBarModel) {
        this.stackBarModel = stackBarModel;
    }

    /**
     * @return the selectedTotalSalesByTeamPieChart
     */
    public String getSelectedTotalSalesByTeamPieChart() {
        return selectedTotalSalesByTeamPieChart;
    }

    /**
     * @param selectedTotalSalesByTeamPieChart the
     * selectedTotalSalesByTeamPieChart to set
     */
    public void setSelectedTotalSalesByTeamPieChart(String selectedTotalSalesByTeamPieChart) {
        this.selectedTotalSalesByTeamPieChart = selectedTotalSalesByTeamPieChart;
    }

    public void onPieChartTeamSalesSelected() {

        salesByTeamPieModel = new PieChartModel();
        System.out.println("************* SELECTED PIE VALUE " + selectedTotalSalesByTeamPieChart);
        for (int i = 0; i < totalSalesByTeamFilter.size(); i++) {

            System.out.println("TOTAL COUNTRY " + totalSalesByTeamFilter.get(i));
            if (totalSalesByTeamFilter.get(i).equals(selectedTotalSalesByTeamPieChart)) {

                getSalesByTeamPieChart(i);
                break;
            }
        }

    }

    public void buttonOnAction(ActionEvent actionEvent) {

        System.out.println("************ CREATE NEW CYSTOMER ");

        try {
         Calendar cal = Calendar.getInstance();
         cal.set(1981, Calendar.MAY, 8);
        Date bdaeDate5 =  cal.getTime() ;
  
        
           Customer newCustomer12 = new Customer("TESTING", "Tan", "93119083", "Male", "Pasir Risk Park st 21", "310110", bdaeDate5, "dan132ya31@gmail.com", "12345678", 0, new Date());


            customerControllerLocal.createNewCustomer(newCustomer12);
        } catch (CustomerSignUpException ex) {
            ex.printStackTrace();
        }

    }

    public void timerUpdate() {
        boardsInformation = dashboardControllerLocal.getPerformanceInformation();
        //  number++;
        System.out.println("*********** NUMBER " + number);
    }

    /**
     * @return the salesByTeamPieModel
     */
    public PieChartModel getSalesByTeamPieModel() {
        return salesByTeamPieModel;
    }

    /**
     * @param salesByTeamPieModel the salesByTeamPieModel to set
     */
    public void setSalesByTeamPieModel(PieChartModel salesByTeamPieModel) {
        this.salesByTeamPieModel = salesByTeamPieModel;
    }

    /**
     * @return the boardsInformation
     */
    public List<PerformanceBoard> getBoardsInformation() {
        //  System.out.println("********* GETING VALUE");
        return boardsInformation;
    }

    /**
     * @param boardsInformation the boardsInformation to set
     */
    public void setBoardsInformation(List<PerformanceBoard> boardsInformation) {
        this.boardsInformation = boardsInformation;
    }

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
    }

}

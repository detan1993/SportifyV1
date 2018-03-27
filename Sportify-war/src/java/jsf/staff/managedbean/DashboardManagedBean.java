/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.staff.managedbean;

import ejb.session.stateless.DashboardControllerLocal;
import ejb.session.stateless.ProductControllerLocal;
import entity.Product;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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

/**
 *
 * @author David
 */
@Named(value = "dashboardManagedBean")
@ViewScoped
public class DashboardManagedBean implements Serializable {

    @EJB(name = "ProductControllerLocal")
    private ProductControllerLocal productControllerLocal;

    @EJB(name = "DashboardControllerLocal")
    private DashboardControllerLocal dashboardControllerLocal;
    
    

    /**
     * Creates a new instance of DashboardManagedBean
     */
    
    
    
    final String [] monthsArray = {"Jan" , "Feb" , "Mar" , "Apr" , "May" , "Jun" , "Jul" , "Aug" , "Sep" , "Oct" , "Nov" , "Dec" }; ;
    private LineChartModel salesLineChart;
    private PieChartModel livePieModel;
    private HorizontalBarChartModel horizontalRating;
    private List<List<CountrySales>> totalSalesByMonthsCountry;

    private List<String[]> totalSalesByTeam;
    private List<String> totalSalesByTeamFilter;
    private Date salesDate;
    private Date currentDate;
    private DashboardModel dashboardPosition;
    private List<Product> products ;
    private Product selectedProducts;
    
   

    public DashboardManagedBean() {
        livePieModel = new PieChartModel();
        totalSalesByMonthsCountry = new ArrayList<>();
        currentDate = new Date();
        totalSalesByTeam = new ArrayList<>();
        totalSalesByTeamFilter = new ArrayList<>();
        products = new ArrayList<>();
        selectedProducts = new Product();
        
       
    }

    @PostConstruct
    public void postConstruct() {
        
        try{
            
        
            totalSalesByMonthsCountry = dashboardControllerLocal.retrieveAllSalesByMonths();
       totalSalesByTeam = dashboardControllerLocal.retrieveSalesByTeamAndCountry();
       totalSalesByTeamFilter = dashboardControllerLocal.getCountries();
        setProducts(productControllerLocal.retrieveProduct());
       
        dashboardPosition = new DefaultDashboardModel();
        DashboardColumn column1 = new DefaultDashboardColumn();
        DashboardColumn column2 = new DefaultDashboardColumn();

         
        column1.addWidget("barchartToggle");
         
        column1.addWidget("salesCountry");
        column2.addWidget("stackChart");
         

        dashboardPosition.addColumn(column1);
        dashboardPosition.addColumn(column2);
        createLineModels();
        createRatingBarModels();
        livePieModel = getLiveComparisonPieChart();
       
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        
    }
    
    
      public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Product Selected", ((Product) event.getObject()).getId().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
      public void click() {
        System.out.println("Date selected is " + salesDate );
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
         
         if(!totalSalesByMonthsCountry.isEmpty())
         {
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
        

        
        int totalCountry =  totalSalesByMonthsCountry.get(0).size(); //this will let me know the number of country per each month
        System.out.println("****** TOTAL Country is " + totalCountry);
        
     
        
        for(int i=0; i<totalCountry; i++)
        {
            
            System.out.println("Total country is " + totalCountry);
            
            LineChartSeries c = new LineChartSeries();
            String countryName = totalSalesByMonthsCountry.get(0).get(i).getCountryName();
            
            if(!countryName.equals("Total"))
                  c.setLabel(countryName + " League");
            else
                 c.setLabel(countryName);
                       
          
            
            for(int j=0; j< totalSalesByMonthsCountry.size(); j++)
            {
                  System.out.println("j is" + j);
            
                  monthName = totalSalesByMonthsCountry.get(j).get(i).getMonth();
                  salesAmount = totalSalesByMonthsCountry.get(j).get(i).getSales();
                  System.out.println("Country name in managedb bean is " + countryName + " month is " + monthName + " sales is" + salesAmount);
                //populate chart for i index country firs
                 c.set( monthName , Double.parseDouble(formatter.format(salesAmount)));
            }
            
            
            c.setSmoothLine(true);
            
            countries.add(c);
        }
        
        
        for(LineChartSeries country : countries){
            
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

    public PieChartModel getLiveComparisonPieChart() {
        
        
        int sizeOfTeam = totalSalesByTeam.size();
        String defaultCountry = totalSalesByTeamFilter.get(0);  //need to change abit
        for(int i =0; i<sizeOfTeam; i++)
        {
            
            String [] teamInfo = totalSalesByTeam.get(i);
            if(teamInfo[1].equals(defaultCountry))  //default country means the first row from the database query
            {
                livePieModel.getData().put(teamInfo[0], Double.parseDouble(teamInfo[2]));
                
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

        livePieModel.setTitle("Sales($) By Clubs");
        livePieModel.setLegendPosition("se");
      //  livePieModel.

        return livePieModel;
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
     * @return the livePieModel
     */
    public PieChartModel getLivePieModel() {
        return livePieModel;
    }

    /**
     * @param livePieModel the livePieModel to set
     */
    public void setLivePieModel(PieChartModel livePieModel) {
        this.livePieModel = livePieModel;
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

}

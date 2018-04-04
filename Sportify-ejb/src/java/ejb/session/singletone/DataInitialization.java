/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singletone;

import ejb.session.stateless.CustomerControllerLocal;
import ejb.session.stateless.CustomerOrderControllerRemote;
import ejb.session.stateless.ImageControllerLocal;
import ejb.session.stateless.ProductControllerLocal;
import ejb.session.stateless.ProductPurchaseControllerLocal;
import ejb.session.stateless.ProductSizeControllerLocal;
import ejb.session.stateless.StaffControllerLocal;
import entity.Customer;
import entity.CustomerOrder;
import entity.Images;
import entity.Product;
import entity.ProductPurchase;
import entity.ProductSize;
import entity.Staff;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.CustomerSignUpException;

/**
 *
 * @author David
 */
@Startup
@Singleton
@LocalBean
public class DataInitialization {

    @EJB(name = "ProductPurchaseControllerLocal")
    private ProductPurchaseControllerLocal productPurchaseControllerLocal;

    @EJB(name = "CustomerOrderControllerLocal")
    private CustomerOrderControllerRemote customerOrderControllerLocal;

    @EJB(name = "StaffControllerLocal")
    private StaffControllerLocal staffControllerLocal;

    @EJB(name = "ProductSizeControllerLocal")
    private ProductSizeControllerLocal productSizeControllerLocal;

    @EJB(name = "CustomerControllerLocal")
    private CustomerControllerLocal customerControllerLocal;

    @EJB(name = "ProductControllerLocal")
    private ProductControllerLocal productControllerLocal;

    @EJB(name = "ImageControllerLocal")
    private ImageControllerLocal imageControllerLocal;
    
    
    
    

    @PersistenceContext(unitName = "Sportify-ejbPU")
    private EntityManager em;

    public DataInitialization() {
    }

    public void persist(Object object) {
        em.persist(object);
    }

    @PostConstruct
    public void postConstruct() {

        try {

            System.out.println("post construct");
            if (productControllerLocal.retrieveProduct().isEmpty()) {

                initializeData();
            }

        } catch (Exception ex) {

        }

    }

    public void initializeData() {

        //Create new Customer . Date of birth format is DD-MM-YYYY
        Customer newCustomer1 = new Customer("Jon", "Tan", "Address 1 Avenue 3", "30-01-2018", "Daviddetan93@gmail.com", "12345678", 0);
        Customer newCustomer2 = new Customer("Paul" ,"Ang", " Address 2 Avenue 4" , "30-02-2018" , "paul80@gmail.com" , "12345678" , 0);
        Customer newCustomer3 = new Customer("Alan" , "Chua" , "Address 3 avenue 21" , " 02-03-2018" , "alan_chua@gmail.com" , "12345678" , 0);
        Customer newCustomer4 = new Customer("Steven" , "Lim" , "AMK 21 ave 21" , " 04-03-2018" , "setve@gmail.com" , "12345678" , 0);
        
        try {
            newCustomer1 = customerControllerLocal.createNewCustomer(newCustomer1);
            newCustomer2 = customerControllerLocal.createNewCustomer(newCustomer2);
            newCustomer3 = customerControllerLocal.createNewCustomer(newCustomer3);
            newCustomer4 = customerControllerLocal.createNewCustomer(newCustomer4);
            

        } catch (CustomerSignUpException ex) {

        }

        //create new Staff 
        Staff newStaff = new Staff("David", "Tan", "E0002311@u.nus.edu", "12345678", "Manager");
        staffControllerLocal.createStaff(newStaff);
        Staff newSalesStaff = new Staff("Jiong Yi", "Lee", "jy@Sportify.com", "12345678", "Sales");
        staffControllerLocal.createStaff(newSalesStaff);

        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        /*
         Each Jersey have 4 pictures 
         example Chealse_Main.PNG   , Chealsea_Sub1.PNG , Chealsea_Sub2.PNG , Chealsea_Sub3.png

         For each country we show 6 teams. Each team we display 2 jersey (home and away). Hence for each country we have in total 12 products.
         */

        List<Images> manuHome = new ArrayList<>();
        Images manuProductHome_Main = new Images("images/products/manu_home.jpg");
        manuHome.add(imageControllerLocal.createNewImage(manuProductHome_Main));
        Images manuProductHome_Sub1 = new Images("TeamName_Sub1");
        manuHome.add(imageControllerLocal.createNewImage(manuProductHome_Sub1));
        Images manuProductHome_Sub2 = new Images("TeamName_Sub2");
        manuHome.add(imageControllerLocal.createNewImage(manuProductHome_Sub2));
        Images manuProductHome_Sub3 = new Images("TeamName_Sub2");
        manuHome.add(imageControllerLocal.createNewImage(manuProductHome_Sub3));

        List<ProductSize> manuHomeSize = new ArrayList<>();
        manuHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 10)));
        manuHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("L", 10)));
        manuHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("M", 10)));

        cal.set(2018, Calendar.JANUARY, 23); //Year, month and day of month
        date = cal.getTime();

        Product manuHomeProduct = new Product("Manu001", "Man United Home 2017/18", "Description", 249.90, "Man United", "Male", "England", date, null, manuHome, manuHomeSize);
        manuHomeProduct = productControllerLocal.CreateNewProduct(manuHomeProduct);

        List<Images> sevillaHome = new ArrayList<>();
        Images sevillaProductHome_Main = new Images("images/products/sevilla_home.jpg");
        sevillaHome.add(imageControllerLocal.createNewImage(sevillaProductHome_Main));
        Images sevillaProductHome_Sub1 = new Images("TeamName_Sub1");
        sevillaHome.add(imageControllerLocal.createNewImage(sevillaProductHome_Sub1));
        Images sevillaProductHome_Sub2 = new Images("TeamName_Sub2");
        sevillaHome.add(imageControllerLocal.createNewImage(sevillaProductHome_Sub2));
        Images sevillaProductHome_Sub3 = new Images("TeamName_Sub2");
        sevillaHome.add(imageControllerLocal.createNewImage(sevillaProductHome_Sub3));

        List<ProductSize> sevillaHomeSize = new ArrayList<>();
        sevillaHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 10)));
        sevillaHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("L", 10)));
        sevillaHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("M", 10)));

        cal.set(2018, Calendar.JANUARY, 23); //Year, month and day of month
        date = cal.getTime();

        Product sevillaHomeProduct = new Product("Sevilla001", "Sevilla Home 2017/18", "Description", 79.90, "Sevilla", "Male", "Spain", date, null, sevillaHome, sevillaHomeSize);
        sevillaHomeProduct = productControllerLocal.CreateNewProduct(sevillaHomeProduct);

        List<Images> liverpoolHome = new ArrayList<>();
        Images liverpoolProductHome_Main = new Images("images/products/liverpool_home.jpg");
        liverpoolHome.add(imageControllerLocal.createNewImage(liverpoolProductHome_Main));
        Images liverpoolProductHome_Sub1 = new Images("TeamName_Sub1");
        liverpoolHome.add(imageControllerLocal.createNewImage(liverpoolProductHome_Sub1));
        Images liverpoolProductHome_Sub2 = new Images("TeamName_Sub2");
        liverpoolHome.add(imageControllerLocal.createNewImage(liverpoolProductHome_Sub2));
        Images liverpoolroductHome_Sub3 = new Images("TeamName_Sub2");
        liverpoolHome.add(imageControllerLocal.createNewImage(liverpoolroductHome_Sub3));

        List<ProductSize> liverpoolHomeSize = new ArrayList<>();
        liverpoolHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 10)));
        liverpoolHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("L", 10)));
        liverpoolHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("M", 10)));

        cal.set(2018, Calendar.FEBRUARY, 23); //Year, month and day of month
        date = cal.getTime();

        Product liverpoolHomeProduct = new Product("Liverpool001", "Liverpool Home 2017/18", "Description", 349.90, "Liverpool", "Male", "England", date, null, liverpoolHome, liverpoolHomeSize);
        liverpoolHomeProduct = productControllerLocal.CreateNewProduct(liverpoolHomeProduct);

        List<Images> atlMadridHome = new ArrayList<>();
        Images atlMadridroductHome_Main = new Images("images/products/altl_home.jpg");
        atlMadridHome.add(imageControllerLocal.createNewImage(atlMadridroductHome_Main));
        Images atlMadridProductHome_Sub1 = new Images("TeamName_Sub1");
        atlMadridHome.add(imageControllerLocal.createNewImage(atlMadridProductHome_Sub1));
        Images atlMadridProductHome_Sub2 = new Images("TeamName_Sub2");
        atlMadridHome.add(imageControllerLocal.createNewImage(atlMadridProductHome_Sub2));
        Images atlMadridProductHome_Sub3 = new Images("TeamName_Sub2");
        atlMadridHome.add(imageControllerLocal.createNewImage(atlMadridProductHome_Sub3));

        List<ProductSize> atlMadridHomeSize = new ArrayList<>();
        atlMadridHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 10)));
        atlMadridHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("L", 10)));
        atlMadridHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("M", 10)));

        cal.set(2018, Calendar.FEBRUARY, 23); //Year, month and day of month
        date = cal.getTime();

        Product atlMadridHomeProduct = new Product("AtleticoMadrid001", "Atletico Madrid Home 2017/18", "Description", 599.90, "Atletico Madrid", "Male", "Spain", date, null, atlMadridHome, atlMadridHomeSize);
        atlMadridHomeProduct = productControllerLocal.CreateNewProduct(atlMadridHomeProduct);

        //Arsenal home
        List<Images> arsenalHome = new ArrayList<>();
        Images ArsenalProductHome_Main = new Images("images/products/ars_home.jpg");
        arsenalHome.add(imageControllerLocal.createNewImage(ArsenalProductHome_Main));
        Images ArsenalProductHome_Sub1 = new Images("images/products/ars_home_1.jpg");
        arsenalHome.add(imageControllerLocal.createNewImage(ArsenalProductHome_Sub1));
        Images ArsenalProductHome_Sub2 = new Images("images/products/ars_home_2.jpg");
        arsenalHome.add(imageControllerLocal.createNewImage(ArsenalProductHome_Sub2));
        Images ArsenalProductHome_Sub3 = new Images("images/products/ars_home_3.jpg");
        arsenalHome.add(imageControllerLocal.createNewImage(ArsenalProductHome_Sub3));

        List<ProductSize> arsenalHomeSize = new ArrayList<>();
        arsenalHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 10)));
        arsenalHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("L", 10)));
        arsenalHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("M", 10)));

        cal.set(2018, Calendar.MARCH, 14); //Year, month and day of month
        date = cal.getTime();

        Product ArsenalHomeProduct = new Product("ARS0001", "Arsenal Home 2017/18", "Description", 159.90, "Arsenal", "Male", "England", date, null, arsenalHome, arsenalHomeSize);
        ArsenalHomeProduct = productControllerLocal.CreateNewProduct(ArsenalHomeProduct);

        //Arsenal Away
        List<Images> arsenalAway = new ArrayList<>();
        Images ArsenalProductAway_Main = new Images("images/products/ars_away.jpg");
        arsenalAway.add(imageControllerLocal.createNewImage(ArsenalProductAway_Main));
        Images ArsenalProductAway_Sub1 = new Images("images/products/ars_away_2.jpg");
        arsenalAway.add(imageControllerLocal.createNewImage(ArsenalProductAway_Sub1));
        Images ArsenalProductAway_Sub2 = new Images("TeamName_Sub2");
        arsenalAway.add(imageControllerLocal.createNewImage(ArsenalProductAway_Sub2));
        Images ArsenalProductAway_Sub3 = new Images("TeamName_Sub2");
        arsenalAway.add(imageControllerLocal.createNewImage(ArsenalProductAway_Sub3));

        List<ProductSize> arsenalAwaySize = new ArrayList<>();
        arsenalAwaySize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 10)));
        arsenalAwaySize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("L", 15)));
        arsenalAwaySize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("M", 5)));

        cal.set(2018, Calendar.FEBRUARY, 1); //Year, month and day of month
        date = cal.getTime();

        Product ArsenalAwayProduct = new Product("ARS0002", "Arsenal Away 2017/18", "Description", 149.50, "Arsenal", "Male", "England", date, null, arsenalAway, arsenalAwaySize);
        ArsenalAwayProduct = productControllerLocal.CreateNewProduct(ArsenalAwayProduct);

        List<Images> barcaHome = new ArrayList<>();
        Images BarcaProductHome_Main = new Images("images/products/barca_home.jpg");
        barcaHome.add(imageControllerLocal.createNewImage(BarcaProductHome_Main));
        Images BarcaProductHome_Sub1 = new Images("TeamName_Sub1");
        barcaHome.add(imageControllerLocal.createNewImage(BarcaProductHome_Sub1));
        Images BarcaProductHome_Sub2 = new Images("TeamName_Sub2");
        barcaHome.add(imageControllerLocal.createNewImage(BarcaProductHome_Sub2));
        Images BarcaProductHome_Sub3 = new Images("TeamName_Sub2");
        barcaHome.add(imageControllerLocal.createNewImage(BarcaProductHome_Sub3));

        List<ProductSize> barcaHomeSize = new ArrayList<>();
        barcaHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 10)));
        barcaHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("L", 10)));
        barcaHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("M", 10)));

        cal.set(2018, Calendar.APRIL, 1); //Year, month and day of month
        date = cal.getTime();

        Product BarcaHomeProduct = new Product("Barca0001", "Barcelona Home 2017/18", "Description", 200.90, "Barcelona", "Male", "Spain", date, null, barcaHome, barcaHomeSize);
         BarcaHomeProduct = productControllerLocal.CreateNewProduct(BarcaHomeProduct);

        //Barca Away
        List<Images> barcaAway = new ArrayList<>();
        Images BarcaProductAway_Main = new Images("images/products/barca_away.jpg");
        barcaAway.add(imageControllerLocal.createNewImage(BarcaProductAway_Main));
        Images Barca_ProductAway_Sub1 = new Images("TeamName_Sub1");
        barcaHome.add(imageControllerLocal.createNewImage(Barca_ProductAway_Sub1));
        Images Barca_ProductAway_Sub2 = new Images("TeamName_Sub2");
        barcaAway.add(imageControllerLocal.createNewImage(Barca_ProductAway_Sub2));
        Images Barca_ProductAway_Sub3 = new Images("TeamName_Sub2");
        barcaAway.add(imageControllerLocal.createNewImage(Barca_ProductAway_Sub3));

        List<ProductSize> barcaAwaySize = new ArrayList<>();
        barcaAwaySize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 10)));
        barcaAwaySize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("L", 10)));
        barcaAwaySize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("M", 10)));

        cal.set(2018, Calendar.APRIL, 20); //Year, month and day of month
        date = cal.getTime();

        Product BarcaAwayProduct = new Product("BAR0002", "Barcelona Away 2017/18", "Description", 111.80, "Barcelona", "Male", "Spain", date, null, barcaAway, barcaAwaySize);
        BarcaAwayProduct = productControllerLocal.CreateNewProduct(BarcaAwayProduct);

        //Chelsea Home
        List<Images> chelseaHome = new ArrayList<>();
        Images ChealseaProductHome_Main = new Images("images/products/chel_home.jpg");
        chelseaHome.add(imageControllerLocal.createNewImage(ChealseaProductHome_Main));
        Images ChealseaProductHome_Sub1 = new Images("TeamName_Sub1");
        chelseaHome.add(imageControllerLocal.createNewImage(ChealseaProductHome_Sub1));
        Images ChealseaProductHome_Sub2 = new Images("TeamName_Sub2");
        chelseaHome.add(imageControllerLocal.createNewImage(ChealseaProductHome_Sub2));
        Images ChealseaProductHome_Sub3 = new Images("TeamName_Sub2");
        chelseaHome.add(imageControllerLocal.createNewImage(ChealseaProductHome_Sub3));

        List<ProductSize> chelseaHomeSize = new ArrayList<>();
        chelseaHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 10)));
        chelseaHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("L", 10)));
        chelseaHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("M", 10)));

        cal.set(2018, Calendar.MARCH, 14); //Year, month and day of month
        date = cal.getTime();

        Product ChelseaHomeProduct = new Product("CHL0001", "Chelsea Home 2017/18", "Description", 129.90, "Chelsea", "Male", "England", date, null, chelseaHome, chelseaHomeSize);
       ChelseaHomeProduct = productControllerLocal.CreateNewProduct(ChelseaHomeProduct);

        //Chelsea Away
        List<Images> chelseaAway = new ArrayList<>();
        Images ChealseaProductAway_Main = new Images("images/products/chel_away.jpg");
        chelseaAway.add(imageControllerLocal.createNewImage(ChealseaProductAway_Main));
        Images ChealseaProductAway_Sub1 = new Images("TeamName_Sub1");
        chelseaAway.add(imageControllerLocal.createNewImage(ChealseaProductAway_Sub1));
        Images ChealseaProductAway_Sub2 = new Images("TeamName_Sub2");
        chelseaAway.add(imageControllerLocal.createNewImage(ChealseaProductAway_Sub2));
        Images ChealseaProductAway_Sub3 = new Images("TeamName_Sub2");
        chelseaAway.add(imageControllerLocal.createNewImage(ChealseaProductAway_Sub3));

        List<ProductSize> chelseaAwaySize = new ArrayList<>();
        chelseaAwaySize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 20)));
        chelseaAwaySize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("S", 10)));
        chelseaAwaySize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("M", 15)));

        cal.set(2018, Calendar.FEBRUARY, 28); //Year, month and day of month
        date = cal.getTime();

        Product ChelseaAwayProduct = new Product("CHL0002", "Chelsea Away 2017/18", "Description", 100, "Chelsea", "Male", "England", date, null, chelseaAway, chelseaAwaySize);
       ChelseaAwayProduct = productControllerLocal.CreateNewProduct(ChelseaAwayProduct);

        //Real Madrid Home
        List<Images> rmHome = new ArrayList<>();
        Images RmProductHome_Main = new Images("images/products/rm_home.jpg");
        rmHome.add(imageControllerLocal.createNewImage(RmProductHome_Main));
        Images Rm_ProductHome_Sub1 = new Images("TeamName_Sub1");
        rmHome.add(imageControllerLocal.createNewImage(Rm_ProductHome_Sub1));
        Images Rm_ProductHome_Sub2 = new Images("TeamName_Sub2");
        rmHome.add(imageControllerLocal.createNewImage(Rm_ProductHome_Sub2));
        Images Rm_ProductHome_Sub3 = new Images("TeamName_Sub2");
        rmHome.add(imageControllerLocal.createNewImage(Rm_ProductHome_Sub3));

        List<ProductSize> rmHomeSize = new ArrayList<>();
        rmHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 10)));
        rmHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("L", 10)));
        rmHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("M", 10)));

        cal.set(2018, Calendar.FEBRUARY, 12); //Year, month and day of month
        date = cal.getTime();

        Product RmHomeProduct = new Product("RM0001", "Real Madrid Home 2017/18", "Description", 59.90, "Real Madrid", "Male", "Spain", date, null, rmHome, rmHomeSize);
       RmHomeProduct = productControllerLocal.CreateNewProduct(RmHomeProduct);

        //Real Madrid Away
        List<Images> rmAway = new ArrayList<>();
        Images RmProductAway_Main = new Images("images/products/rm_away.jpg");
        rmAway.add(imageControllerLocal.createNewImage(RmProductAway_Main));
        Images Rm_ProductAway_Sub1 = new Images("TeamName_Sub1");
        rmAway.add(imageControllerLocal.createNewImage(Rm_ProductAway_Sub1));
        Images Rm_ProductAway_Sub2 = new Images("TeamName_Sub2");
        rmAway.add(imageControllerLocal.createNewImage(Rm_ProductAway_Sub2));
        Images Rm_ProductAway_Sub3 = new Images("TeamName_Sub2");
        rmAway.add(imageControllerLocal.createNewImage(Rm_ProductAway_Sub3));

        List<ProductSize> rmAwaySize = new ArrayList<>();
        rmAwaySize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 10)));
        rmAwaySize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("L", 10)));
        rmAwaySize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("M", 10)));

        cal.set(2018, Calendar.MARCH, 1); //Year, month and day of month
        date = cal.getTime();

        Product RmAwayProduct = new Product("RM0002", "Real Madrid Away 2017/18", "Description", 59.90, "Real Madrid", "Male", "Spain", date, null, rmAway, rmAwaySize);
       RmAwayProduct = productControllerLocal.CreateNewProduct(RmAwayProduct);
        
        //AC Milan Home
        List<Images> acMilanHome = new ArrayList<>();
        Images acMilanProductHome_Main = new Images("images/products/acmilan_home.jpg");
        acMilanHome.add(imageControllerLocal.createNewImage(acMilanProductHome_Main));
        Images acMilan_ProductHome_Sub1 = new Images("TeamName_Sub1");
        acMilanHome.add(imageControllerLocal.createNewImage(acMilan_ProductHome_Sub1));
        Images acMilan_ProductHome_Sub2 = new Images("TeamName_Sub2");
        acMilanHome.add(imageControllerLocal.createNewImage(acMilan_ProductHome_Sub2));
        Images acMilan_ProductHome_Sub3 = new Images("TeamName_Sub2");
        acMilanHome.add(imageControllerLocal.createNewImage(acMilan_ProductHome_Sub3));

        List<ProductSize> acMilanHomeSize = new ArrayList<>();
        acMilanHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 5)));
        acMilanHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("L", 7)));
        acMilanHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("S", 9)));

        cal.set(2018, Calendar.MARCH, 26); //Year, month and day of month
        date = cal.getTime();

        Product AcMilanHomeProduct = new Product("ACM0001", "AC Milan Home 2017/18", "Description", 59.90, "AC Milan", "Male", "Italy", date, null, acMilanHome, acMilanHomeSize);
       AcMilanHomeProduct = productControllerLocal.CreateNewProduct(AcMilanHomeProduct);

        //Chealse Home
        /*Images ChealseaProductHome_Main = new Images("<TeamName>_Main example ");
        Images ChealseaProductHome_Sub1 = new Images("TeamName_Sub1");
        Images ChealseaProductHome_Sub2 = new Images("TeamName_Sub2");
        Images ChealseaProductHome_Sub3 = new Images("TeamName_Sub2");

        //Chealse Away
        Images ChealseaProductAway_Main = new Images("<TeamName>_Main example ");
        Images ChealseaProductAway_Sub1 = new Images("TeamName_Sub1");
        Images ChealseaProductAway_Sub2 = new Images("TeamName_Sub2");
        Images ChealseaProductAway_Sub3 = new Images("TeamName_Sub2");*/
 /*    List<Images> chealseaHome = new ArrayList<>();
        Images ChealseaProductHome_Main = new Images("<TeamName>_Main example ");
        chealseaHome.add(imageControllerLocal.createNewImage(ChealseaProductHome_Main));
        Images ChealseaProductHome_Sub1 = new Images("TeamName_Sub1");
        chealseaHome.add(imageControllerLocal.createNewImage(ChealseaProductHome_Sub1));
        Images ChealseaProductHome_Sub2 = new Images("TeamName_Sub2");
        chealseaHome.add(imageControllerLocal.createNewImage(ChealseaProductHome_Sub2));
        Images ChealseaProductHome_Sub3 = new Images("TeamName_Sub2");
        chealseaHome.add(imageControllerLocal.createNewImage(ChealseaProductHome_Sub3));*/
        //Liverpoll Home
        /*   Images LiverpoolProductHome_Main = new Images("<TeamName>_Main example ");
         Images LiverpoolProductHome_Sub1 = new Images("TeamName_Sub1");
         Images LiverpoolProductHome_Sub2 = new Images("TeamName_Sub2");
         Images LiverpoolProductHome_Sub3 = new Images("TeamName_Sub2");*/
 /*  //LiverPool Away
         
          Images LiverpoolProductAway_Main = new Images("<TeamName>_Main example ");
         Images LiverpoolProductAway_Sub1 = new Images("TeamName_Sub1");
         Images LiverpoolProductAway_Sub2 = new Images("TeamName_Sub2");
         Images LiverpoolProductAway_Sub3 = new Images("TeamName_Sub2");
         
         
         //Mancity Home
         Images MancityProductHome_Main = new Images("<TeamName>_Main example ");
         Images MancityProductHome_Sub1 = new Images("TeamName_Sub1");
         Images MancityProductHome_Sub2 = new Images("TeamName_Sub2");
         Images MancityProductHome_Sub3 = new Images("TeamName_Sub2");
         
         //Mancity Away
         Images MancityProductAway_Main = new Images("<TeamName>_Main example ");
         Images MancityProductAway_Sub1 = new Images("TeamName_Sub1");
         Images MancityProductAway_Sub2 = new Images("TeamName_Sub2");
         Images MancityProductAway_Sub3 = new Images("TeamName_Sub2");
         
         
        //ManU home
        Images ManupPoductHome_Main = new Images("<TeamName>_Main example ");
         Images ManuProductHome_Sub1 = new Images("TeamName_Sub1");
         Images ManuProductHome_Sub2 = new Images("TeamName_Sub2");
         Images ManuProductHome_Sub3 = new Images("TeamName_Sub2");
         
         //Manu Away
          Images ManuproductAway_Main = new Images("<TeamName>_Main example ");
         Images ManuProductAway_Sub1 = new Images("TeamName_Sub1");
         Images ManuProductAway_Sub2 = new Images("TeamName_Sub2");
         Images ManuProductAway_Sub3 = new Images("TeamName_Sub2");
         
         
         //Hospur Home
          Images HospurProductHome_Main = new Images("<TeamName>_Main example ");
         Images HospurProductHome_Sub1 = new Images("TeamName_Sub1");
         Images HospurProductHome_Sub2 = new Images("TeamName_Sub2");
         Images HospurProductHome_Sub3 = new Images("TeamName_Sub2");
         
         
         //Hospur Away
          Images HospurProductAway_Main = new Images("<TeamName>_Main example ");
         Images HospurProductAway_Sub1 = new Images("TeamName_Sub1");
         Images HospurProductAway_Sub2 = new Images("TeamName_Sub2");
         Images HospurProductAway_Sub3 = new Images("TeamName_Sub2");*/
        try {

            //Customer 1
            
           // Custom
            
     /*       List<Product> cust1Feb = new ArrayList<>();
            cust1Feb.add(ChelseaAwayProduct);
            List<Product> cust1Mar = new ArrayList<>();
            cust1Mar.add(atlMadridHomeProduct);
            cust1Mar.add(RmAwayProduct);
            
     
            
            //Customer 3
           
            List<Product> cust3Feb = new ArrayList<>();
            cust3Feb.add(atlMadridHomeProduct);
            List<Product> cust3Mar = new ArrayList<>();
            cust3Mar.add(ArsenalAwayProduct);
            cust3Mar.add(ArsenalHomeProduct);
            cust3Mar.add(liverpoolHomeProduct);
            cust3Mar.add(RmHomeProduct);
            cust3Mar.add(manuHomeProduct);
            cust3Mar.add(BarcaAwayProduct);
            

            cal.set(2018, Calendar.JANUARY, 26); //Year, month and day of month
            Date dateJan = cal.getTime();
            cal.set(2018, Calendar.FEBRUARY, 26);
            Date dateFeb = cal.getTime();
            cal.set(2018, Calendar.MARCH, 26);
            Date dateMar = cal.getTime(); */
            
           /// CustomerOrder customer1 =  
            List<ProductPurchase> cust1purchase = new ArrayList<>();
             List<ProductPurchase> cust2purchase = new ArrayList<>();
              List<ProductPurchase> cust3purchase = new ArrayList<>();
           // cust1Jan.add(new ProductPurchase(0, 0, order, RmHomeProduct))
    
            //cust1Jan.add(RmHomeProduct);
           // cust1Jan.add(AcMilanHomeProduct);
           
             cal.set(2018, Calendar.JANUARY, 26); //Year, month and day of month
            Date dateJan = cal.getTime();
            cal.set(2018, Calendar.FEBRUARY, 26);
            Date dateFeb = cal.getTime();
            cal.set(2018, Calendar.MARCH, 26);
            Date dateMar = cal.getTime();
            
            CustomerOrder cust1OrderJan = new CustomerOrder(160.90, 20, dateJan, "Delivered", newCustomer1);
            cust1OrderJan = customerOrderControllerLocal.CreateNewCustomerOrder(cust1OrderJan);
            ProductPurchase p1 = productPurchaseControllerLocal.createProductPurchase((new ProductPurchase(80.45 , 2  , cust1OrderJan, RmHomeProduct )));
            customerOrderControllerLocal.addProductPurchase(cust1OrderJan.getId() , p1);
                    
            
           CustomerOrder cust1OrderFeb = new CustomerOrder(100.0, 20, dateFeb, "Delivered", newCustomer1);
           cust1OrderFeb =   customerOrderControllerLocal.CreateNewCustomerOrder(cust1OrderFeb);
            customerOrderControllerLocal.addProductPurchase(cust1OrderFeb.getId() , productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(100.0, 1, cust1OrderFeb, ChelseaAwayProduct)));
           CustomerOrder cust1OrderMar = new CustomerOrder(659.80, 100, dateMar, "Delivered", newCustomer1);
           cust1OrderMar =   customerOrderControllerLocal.CreateNewCustomerOrder(cust1OrderMar);
           customerOrderControllerLocal.addProductPurchase(cust1OrderMar.getId(),productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(300.00 ,1, cust1OrderMar , RmAwayProduct )));
           customerOrderControllerLocal.addProductPurchase(cust1OrderMar.getId(),  productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(359.80, 1, cust1OrderMar, atlMadridHomeProduct)));

          //  customerOrderControllerLocal.CreateNewCustomerOrder(cust1OrderMar);
            
            cal.set(2018, Calendar.JANUARY, 28); //Year, month and day of month
            dateJan = cal.getTime();
            cal.set(2018, Calendar.FEBRUARY, 27);
            dateFeb = cal.getTime();
            cal.set(2018, Calendar.MARCH, 30);
            dateMar = cal.getTime();
            

            
            CustomerOrder cust2OrderJan = new CustomerOrder(352.35, 20, dateJan, "Delivered", newCustomer2);
            cust2OrderJan = customerOrderControllerLocal.CreateNewCustomerOrder(cust2OrderJan);
            customerOrderControllerLocal.addProductPurchase(cust2OrderJan.getId(),productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(80.45 , 1  , cust2OrderJan, RmHomeProduct )));
            customerOrderControllerLocal.addProductPurchase(cust2OrderJan.getId(),productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(66.45 , 1  , cust2OrderJan, AcMilanHomeProduct )));
            customerOrderControllerLocal.addProductPurchase(cust2OrderJan.getId(), productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(95.00 , 1  , cust2OrderJan, ChelseaAwayProduct )));
            customerOrderControllerLocal.addProductPurchase(cust2OrderJan.getId(),productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(110.45 , 1  , cust2OrderJan, ChelseaHomeProduct )));
                                               
            
           CustomerOrder cust2OrderFeb = new CustomerOrder(250.0, 20, dateFeb, "Delivered", newCustomer2);
           cust2OrderFeb =   customerOrderControllerLocal.CreateNewCustomerOrder(cust2OrderFeb);
           customerOrderControllerLocal.addProductPurchase(cust2OrderFeb.getId(),productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(125.0, 2, cust2OrderFeb, BarcaAwayProduct)));
           
           CustomerOrder cust2OrderMar = new CustomerOrder(659.80, 100, dateMar, "Delivered", newCustomer2);
           cust2OrderMar =   customerOrderControllerLocal.CreateNewCustomerOrder(cust2OrderMar);
             customerOrderControllerLocal.addProductPurchase(cust2OrderMar.getId(),productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(300.00 ,1, cust2OrderMar , RmAwayProduct )));
              customerOrderControllerLocal.addProductPurchase(cust2OrderMar.getId(),productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(359.80, 1, cust2OrderMar, atlMadridHomeProduct)));

                
           cal.set(2018, Calendar.FEBRUARY, 21);
            dateFeb = cal.getTime();
            cal.set(2018, Calendar.MARCH, 14);
            dateMar = cal.getTime();
            
            
            
           CustomerOrder cust3OrderFeb = new CustomerOrder(719.60, 100, dateFeb, "Delivered", newCustomer3);
           cust3OrderFeb =   customerOrderControllerLocal.CreateNewCustomerOrder(cust3OrderFeb);
           customerOrderControllerLocal.addProductPurchase(cust3OrderFeb.getId(), productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(359.80, 2, cust3OrderFeb, atlMadridHomeProduct)));
           
           CustomerOrder cust3OrderMar = new CustomerOrder(1133.40, 100, dateMar, "Delivered", newCustomer3);
           cust3OrderMar =   customerOrderControllerLocal.CreateNewCustomerOrder(cust3OrderMar);
            customerOrderControllerLocal.addProductPurchase(cust3OrderMar.getId(), productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(100.0 ,1, cust3OrderMar , ArsenalAwayProduct )));
            customerOrderControllerLocal.addProductPurchase(cust3OrderMar.getId(),productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(120.90, 2, cust3OrderMar, ArsenalHomeProduct)));
           customerOrderControllerLocal.addProductPurchase(cust3OrderMar.getId(), productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(90.90, 2, cust3OrderMar, liverpoolHomeProduct)));
          customerOrderControllerLocal.addProductPurchase(cust3OrderMar.getId(), productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(179.90, 2, cust3OrderMar, manuHomeProduct)));
        customerOrderControllerLocal.addProductPurchase(cust3OrderMar.getId(), productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(125.0, 2, cust3OrderMar, BarcaAwayProduct)));
            


            
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singletone;

import ejb.session.stateless.CustomerControllerLocal;
import ejb.session.stateless.ImageControllerLocal;
import ejb.session.stateless.ProductControllerLocal;
import ejb.session.stateless.ProductSizeControllerLocal;
import ejb.session.stateless.StaffControllerLocal;
import entity.Customer;
import entity.Images;
import entity.Product;
import entity.ProductSize;
import entity.Staff;
import java.util.ArrayList;
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
        try {
            Customer newCustomer = new Customer("Jon", "Tan", "Address 1 Avenue 3", "30-01-2017", "Daviddetan93@gmail.com", "12345678", 0);
            customerControllerLocal.createNewCustomer(newCustomer);
        } catch (CustomerSignUpException ex) {
            
        }

        //create new Staff 
        Staff newStaff = new Staff("David", "Tan", "E0002311@u.nus.edu", "12345678", "Manager");
        staffControllerLocal.createStaff(newStaff);
        Staff newSalesStaff = new Staff("Jiong Yi", "Lee", "jy@Sportify.com", "12345678", "Sales");
        staffControllerLocal.createStaff(newSalesStaff);
        
        /*
         Each Jersey have 4 pictures 
         example Chealse_Main.PNG   , Chealsea_Sub1.PNG , Chealsea_Sub2.PNG , Chealsea_Sub3.png

         For each country we show 6 teams. Each team we display 2 jersey (home and away). Hence for each country we have in total 12 products.
         */
        //Arsenal home
        List<Images> arsenalHome = new ArrayList<>();
        Images ArsenalProductHome_Main = new Images("<TeamName>_Main example ");
        arsenalHome.add(imageControllerLocal.createNewImage(ArsenalProductHome_Main));
        Images ArsenalProductHome_Sub1 = new Images("TeamName_Sub1");
        arsenalHome.add(imageControllerLocal.createNewImage(ArsenalProductHome_Sub1));
        Images ArsenalProductHome_Sub2 = new Images("TeamName_Sub2");
        arsenalHome.add(imageControllerLocal.createNewImage(ArsenalProductHome_Sub2));
        Images ArsenalProductHome_Sub3 = new Images("TeamName_Sub2");
        arsenalHome.add(imageControllerLocal.createNewImage(ArsenalProductHome_Sub3));

        List<ProductSize> arsenalHomeSize = new ArrayList<>();
        arsenalHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 10)));
        arsenalHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("L", 10)));
        arsenalHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("M", 10)));

        Product ArsenalHomeProduct = new Product("ARS0001", "Arsenal Home 2017/18", "Description", 99.90, "Arsenal", "Male", "England", new Date(), null, arsenalHome, arsenalHomeSize);
        productControllerLocal.CreateNewProduct(ArsenalHomeProduct);

        //Arsenal Away
        List<Images> arsenalAway = new ArrayList<>();
        Images ArsenalProductAway_Main = new Images("<TeamName>_Main example ");
        arsenalAway.add(imageControllerLocal.createNewImage(ArsenalProductAway_Main));
        Images ArsenalProductAway_Sub1 = new Images("TeamName_Sub1");
        arsenalAway.add(imageControllerLocal.createNewImage(ArsenalProductAway_Sub1));
        Images ArsenalProductAway_Sub2 = new Images("TeamName_Sub2");
        arsenalAway.add(imageControllerLocal.createNewImage(ArsenalProductAway_Sub2));
        Images ArsenalProductAway_Sub3 = new Images("TeamName_Sub2");
        arsenalAway.add(imageControllerLocal.createNewImage(ArsenalProductAway_Sub3));

        List<ProductSize> arsenalAwaySize = new ArrayList<>();
        arsenalAwaySize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 10)));
        arsenalAwaySize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("L", 15)));
        arsenalAwaySize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("M", 5)));

        Product ArsenalAwayProduct = new Product("ARS0002", "Arsenal Away 2017/18", "Description", 99.90, "Arsenal", "Male", "England", new Date(), null, arsenalAway, arsenalAwaySize);
        productControllerLocal.CreateNewProduct(ArsenalAwayProduct);

        //Chealse Home
        Images ChealseaProductHome_Main = new Images("<TeamName>_Main example ");
        Images ChealseaProductHome_Sub1 = new Images("TeamName_Sub1");
        Images ChealseaProductHome_Sub2 = new Images("TeamName_Sub2");
        Images ChealseaProductHome_Sub3 = new Images("TeamName_Sub2");

        //Chealse Away
        Images ChealseaProductAway_Main = new Images("<TeamName>_Main example ");
        Images ChealseaProductAway_Sub1 = new Images("TeamName_Sub1");
        Images ChealseaProductAway_Sub2 = new Images("TeamName_Sub2");
        Images ChealseaProductAway_Sub3 = new Images("TeamName_Sub2");

        //Liverpoll Home
        /*    Images LiverpoolProductHome_Main = new Images("<TeamName>_Main example ");
         Images LiverpoolProductHome_Sub1 = new Images("TeamName_Sub1");
         Images LiverpoolProductHome_Sub2 = new Images("TeamName_Sub2");
         Images LiverpoolProductHome_Sub3 = new Images("TeamName_Sub2");
         
         
         //LiverPool Away
         
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
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}

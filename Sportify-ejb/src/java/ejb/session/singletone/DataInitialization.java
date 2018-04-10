/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singletone;

import ejb.session.stateless.CustomerControllerLocal;
import ejb.session.stateless.CustomerOrderControllerRemote;
import ejb.session.stateless.CustomerVoucherControllerLocal;
import ejb.session.stateless.ImageControllerLocal;
import ejb.session.stateless.ProductControllerLocal;
import ejb.session.stateless.ProductPurchaseControllerLocal;
import ejb.session.stateless.ProductReviewControllerRemote;
import ejb.session.stateless.ProductSizeControllerLocal;
import ejb.session.stateless.StaffControllerLocal;
import ejb.session.stateless.VoucherControllerLocal;
import entity.Customer;
import entity.CustomerOrder;
import entity.CustomerVoucher;
import entity.Images;
import entity.Product;
import entity.ProductPurchase;
import entity.ProductReview;
import entity.ProductSize;
import entity.Staff;
import entity.Voucher;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

    @EJB(name = "ProductReviewControllerLocal")
    private ProductReviewControllerRemote productReviewControllerLocal;

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

    @EJB(name = "VoucherControllerLocal")
    private VoucherControllerLocal voucherControllerLocal;

    @EJB(name = "CustomerVoucherControllerLocal")
    private CustomerVoucherControllerLocal customerVoucherControllerLocal;

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
            initializeData();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void initializeData() {

        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        Date date2 = new Date();
        Date date3 = new Date();
        Date date4 = new Date();

        cal.set(2018, Calendar.JANUARY, 1); //Year, month and day of month
        date = cal.getTime();
        cal.set(2018, Calendar.FEBRUARY, 1);
        date2 = cal.getTime();
        cal.set(2018, Calendar.MARCH, 1);
        date3 = cal.getTime();
        cal.set(2018, Calendar.APRIL, 1);
        date4 = cal.getTime();

        Date bdaeDate = new Date();
        cal.set(1993, Calendar.MAY, 13);
        bdaeDate = cal.getTime();

        Date bdaeDate2 = new Date();
        cal.set(1994, Calendar.MARCH, 13);
        bdaeDate2 = cal.getTime();

        Date bdaeDate3 = new Date();
        cal.set(1992, Calendar.DECEMBER, 1);
        bdaeDate3 = cal.getTime();

        Date bdaeDate4 = new Date();
        cal.set(1989, Calendar.JULY, 3);
        bdaeDate4 = cal.getTime();

        Date bdaeDate5 = new Date();
        cal.set(1981, Calendar.MAY, 8);
        bdaeDate5 = cal.getTime();

        //Create new Customer . Date of birth format is DD-MM-YYYY
        Customer newCustomer1 = new Customer("Jon", "Tan", "8565656", "Male", "Address 1 Avenue 3", "123456", bdaeDate, "Daviddetan93@gmail.com", "12345678", 0, date);
        Customer newCustomer2 = new Customer("Paul", "Ang", "6545454", "Male", " Address 2 Avenue 4", "546162", bdaeDate2, "paul80@gmail.com", "12345678", 0, date);
        Customer newCustomer3 = new Customer("Alan", "Chua", "837373733", "Male", "Address 3 avenue 21", "318282", bdaeDate3, "alan_chua@gmail.com", "12345678", 0, date2);
        Customer newCustomer4 = new Customer("Steven", "Lim", "454545", "Female", "AMK 21 ave 21", "310292", bdaeDate4, "setve@gmail.com", "12345678", 0, date2);
        Customer newCustomer5 = new Customer("Liew Shan", "Wei", "83737453733", "Male", "Yishun Blk 22", "453121", bdaeDate5, "shanwLiew@gmail.com", "12345678", 0, date3);
        Customer newCustomer6 = new Customer("Danny", "Poo", "837365573733", "Female", "AMK 21 ave 23", "310293", bdaeDate3, "danny@gmail.com", "12345678", 0, date3);
        Customer newCustomer7 = new Customer("Dennis", "Ang", "837373733", "Male", "Kovan 21 Street 1", "541213", bdaeDate2, "dennis@gmail.com", "12345678", 0, date3);
        Customer newCustomer8 = new Customer("Derian", "Lim", "fdff", "Female", "Punggo1 ave 51", "453123", bdaeDate4, "derian@gmail.com", "12345678", 0, date3);
        Customer newCustomer9 = new Customer("Jason", "Ang", "837373733", "Male", "Jurong East 45", "103021", bdaeDate5, "jason@gmail.com", "12345678", 0, date3);
        Customer newCustomer10 = new Customer("Katie", "Perry", "837373733", "Female", "Serangoo North ave 45", "6262523", bdaeDate4, "katPer@gmail.com", "12345678", 0, date4);
        Customer newCustomer11 = new Customer("Steve", "Tan", "837373733", "Male", "Tampiness st 21 block 21", "372626", bdaeDate, "steveTan@gmail.com", "12345678", 0, date4);

        try {
            newCustomer1 = customerControllerLocal.createNewCustomer(newCustomer1);
            newCustomer2 = customerControllerLocal.createNewCustomer(newCustomer2);
            newCustomer3 = customerControllerLocal.createNewCustomer(newCustomer3);
            newCustomer4 = customerControllerLocal.createNewCustomer(newCustomer4);
            newCustomer5 = customerControllerLocal.createNewCustomer(newCustomer5);
            newCustomer6 = customerControllerLocal.createNewCustomer(newCustomer6);
            newCustomer7 = customerControllerLocal.createNewCustomer(newCustomer7);
            newCustomer8 = customerControllerLocal.createNewCustomer(newCustomer8);
            newCustomer9 = customerControllerLocal.createNewCustomer(newCustomer9);
            newCustomer10 = customerControllerLocal.createNewCustomer(newCustomer10);
            newCustomer11 = customerControllerLocal.createNewCustomer(newCustomer11);

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
        List<Images> manuHome = new ArrayList<>();
        Images manuProductHome_Main = new Images("images/products/eng_manu_main.jpg");
        manuHome.add(imageControllerLocal.createNewImage(manuProductHome_Main));
        Images manuProductHome_Sub1 = new Images("images/products/eng_manu_1.jpg");
        manuHome.add(imageControllerLocal.createNewImage(manuProductHome_Sub1));
        Images manuProductHome_Sub2 = new Images("images/products/eng_manu_2.jpg");
        manuHome.add(imageControllerLocal.createNewImage(manuProductHome_Sub2));
        Images manuProductHome_Sub3 = new Images("images/products/eng_manu_3.jpg");
        manuHome.add(imageControllerLocal.createNewImage(manuProductHome_Sub3));

        List<ProductSize> manuHomeSize = new ArrayList<>();
        manuHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 153)));
        manuHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("L", 68)));
        manuHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("M", 45)));

        cal.set(2018, Calendar.JANUARY, 23); //Year, month and day of month
        date = cal.getTime();

        Product manuHomeProduct = new Product("Manu001", "Man United Home 2017/18", "Manchester United is one of the biggest clubs in the world, and they have surely made some interesting signings this season. The legacy from Ferguson has been hard to deal with, but maybe this is the season when things change. In our Manchester United collection, you will find the newest shirts - both home and away. You know what’s interesting? You can also choose your own customized printing on the back!", 249.90, "Manchester United", "Male", "England", date, null, manuHome, manuHomeSize);
        manuHomeProduct = productControllerLocal.CreateNewProduct(manuHomeProduct);

        List<Images> sevillaHome = new ArrayList<>();
        Images sevillaProductHome_Main = new Images("images/products/spain_sevilla_main.jpg");
        sevillaHome.add(imageControllerLocal.createNewImage(sevillaProductHome_Main));
        Images sevillaProductHome_Sub1 = new Images("images/products/spain_sevilla_1.jpg");
        sevillaHome.add(imageControllerLocal.createNewImage(sevillaProductHome_Sub1));
        Images sevillaProductHome_Sub2 = new Images("images/products/spain_sevilla_2.jpg");
        sevillaHome.add(imageControllerLocal.createNewImage(sevillaProductHome_Sub2));
        Images sevillaProductHome_Sub3 = new Images("images/products/spain_sevilla_3.jpg");
        sevillaHome.add(imageControllerLocal.createNewImage(sevillaProductHome_Sub3));
        Images sevillaProductHome_Sub4 = new Images("images/products/spain_sevilla_4.jpg");
        sevillaHome.add(imageControllerLocal.createNewImage(sevillaProductHome_Sub4));
        Images sevillaProductHome_Sub5 = new Images("images/products/spain_sevilla_5.jpg");
        sevillaHome.add(imageControllerLocal.createNewImage(sevillaProductHome_Sub5));

        List<ProductSize> sevillaHomeSize = new ArrayList<>();
        sevillaHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 55)));
        sevillaHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("L", 28)));
        sevillaHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("M", 2)));

        cal.set(2018, Calendar.JANUARY, 23); //Year, month and day of month
        date = cal.getTime();

        Product sevillaHomeProduct = new Product("Sevilla001", "Sevilla Home 2017/18", "Sevilla’s style of playing football have always been exciting. We give you the opportunity to get this shirt right here at Sportify. All you have to do is choose your size favourite player on the shirt or print your own name and number. We offer fast delivery, which means that you get your Sevilla shirt within a week after your ordered it.", 79.90, "Sevilla", "Male", "Spain", date, null, sevillaHome, sevillaHomeSize);
        sevillaHomeProduct = productControllerLocal.CreateNewProduct(sevillaHomeProduct);

        List<Images> liverpoolHome = new ArrayList<>();
        Images liverpoolProductHome_Main = new Images("images/products/eng_liv_main.jpg");
        liverpoolHome.add(imageControllerLocal.createNewImage(liverpoolProductHome_Main));
        Images liverpoolProductHome_Sub1 = new Images("images/products/eng_liv_1.jpg");
        liverpoolHome.add(imageControllerLocal.createNewImage(liverpoolProductHome_Sub1));
        Images liverpoolProductHome_Sub2 = new Images("images/products/eng_liv_2.jpg");
        liverpoolHome.add(imageControllerLocal.createNewImage(liverpoolProductHome_Sub2));
        Images liverpoolroductHome_Sub3 = new Images("images/products/eng_liv_3.jpg");
        liverpoolHome.add(imageControllerLocal.createNewImage(liverpoolroductHome_Sub3));
        Images liverpoolroductHome_Sub4 = new Images("images/products/eng_liv_4.jpg");
        liverpoolHome.add(imageControllerLocal.createNewImage(liverpoolroductHome_Sub4));

        List<ProductSize> liverpoolHomeSize = new ArrayList<>();
        liverpoolHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 5)));
        liverpoolHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("L", 24)));
        liverpoolHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("M", 12)));

        cal.set(2018, Calendar.FEBRUARY, 23); //Year, month and day of month
        date = cal.getTime();

        Product liverpoolHomeProduct = new Product("Liverpool001", "Liverpool Home 2017/18", "Liverpool FC is one of the most famous English clubs. They are on the way to the semi-finals of the champions league this season. If you want to support Liverpool, you can find home shirt and away jersey right here at Sportify. We offer the newest shirt in different sizes. So, no matter what your heart desire, Sportify offers it. Find your new Liverpool shirt right here!", 349.90, "Liverpool", "Male", "England", date, null, liverpoolHome, liverpoolHomeSize);
        liverpoolHomeProduct = productControllerLocal.CreateNewProduct(liverpoolHomeProduct);

        List<Images> atlMadridHome = new ArrayList<>();
        Images atlMadridroductHome_Main = new Images("images/products/spain_atleti_main.jpg");
        atlMadridHome.add(imageControllerLocal.createNewImage(atlMadridroductHome_Main));
        Images atlMadridProductHome_Sub1 = new Images("images/products/spain_atleti_1.jpg");
        atlMadridHome.add(imageControllerLocal.createNewImage(atlMadridProductHome_Sub1));
        Images atlMadridProductHome_Sub2 = new Images("images/products/spain_atleti_2.jpg");
        atlMadridHome.add(imageControllerLocal.createNewImage(atlMadridProductHome_Sub2));
        Images atlMadridProductHome_Sub3 = new Images("images/products/spain_atleti_3.jpg");
        atlMadridHome.add(imageControllerLocal.createNewImage(atlMadridProductHome_Sub3));
        Images atlMadridProductHome_Sub4 = new Images("images/products/spain_atleti_4.jpg");
        atlMadridHome.add(imageControllerLocal.createNewImage(atlMadridProductHome_Sub4));

        List<ProductSize> atlMadridHomeSize = new ArrayList<>();
        atlMadridHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 2)));
        atlMadridHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("L", 9)));
        atlMadridHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("M", 27)));

        cal.set(2018, Calendar.FEBRUARY, 23); //Year, month and day of month
        date = cal.getTime();

        Product atlMadridHomeProduct = new Product("AtleticoMadrid001", "Atletico Madrid Home 2017/18", "Atletico Madrid is one of the regular contenders for the Spanish Football throne. With one league title and two Champions League finals within three years, it is one of the most respectable clubs. Their head coach Diego Simeone is a big part of the success. Explore our selection of Atletico Madrid home shirt and away jersey!", 599.90, "Atletico Madrid", "Male", "Spain", date, null, atlMadridHome, atlMadridHomeSize);
        atlMadridHomeProduct = productControllerLocal.CreateNewProduct(atlMadridHomeProduct);

        //Arsenal home
        List<Images> arsenalHome = new ArrayList<>();
        Images ArsenalProductHome_Main = new Images("images/products/eng_ars_main.jpg");
        arsenalHome.add(imageControllerLocal.createNewImage(ArsenalProductHome_Main));
        Images ArsenalProductHome_Sub1 = new Images("images/products/eng_ars_1.jpg");
        arsenalHome.add(imageControllerLocal.createNewImage(ArsenalProductHome_Sub1));
        Images ArsenalProductHome_Sub2 = new Images("images/products/eng_ars_2.jpg");
        arsenalHome.add(imageControllerLocal.createNewImage(ArsenalProductHome_Sub2));
        Images ArsenalProductHome_Sub3 = new Images("images/products/eng_ars_3.jpg");
        arsenalHome.add(imageControllerLocal.createNewImage(ArsenalProductHome_Sub3));
        Images ArsenalProductHome_Sub4 = new Images("images/products/eng_ars_4.jpg");
        arsenalHome.add(imageControllerLocal.createNewImage(ArsenalProductHome_Sub4));

        List<ProductSize> arsenalHomeSize = new ArrayList<>();
        arsenalHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 100)));
        arsenalHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("L", 22)));
        arsenalHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("M", 113)));

        cal.set(2018, Calendar.MARCH, 14); //Year, month and day of month
        date = cal.getTime();

        Product ArsenalHomeProduct = new Product("ARS0001", "Arsenal Home 2017/18", "Looking for an Arsenal shirt? Are you a true Gunner? Good news! We have the latest Arsenal shirt, including printing of name and number. You will find both the home and away jersey. No matter what you are looking for, Sportify offers it!", 159.90, "Arsenal", "Male", "England", date, null, arsenalHome, arsenalHomeSize);
        ArsenalHomeProduct = productControllerLocal.CreateNewProduct(ArsenalHomeProduct);

        //Arsenal Away
        List<Images> arsenalAway = new ArrayList<>();
        Images ArsenalProductAway_Main = new Images("images/products/eng_ars_away_main.jpg");
        arsenalAway.add(imageControllerLocal.createNewImage(ArsenalProductAway_Main));
        Images ArsenalProductAway_Sub1 = new Images("images/products/eng_ars_away_1.jpg");
        arsenalAway.add(imageControllerLocal.createNewImage(ArsenalProductAway_Sub1));
        Images ArsenalProductAway_Sub2 = new Images("images/products/eng_ars_away_2.jpg");
        arsenalAway.add(imageControllerLocal.createNewImage(ArsenalProductAway_Sub2));
        Images ArsenalProductAway_Sub3 = new Images("images/products/eng_ars_away_3.jpg");
        arsenalAway.add(imageControllerLocal.createNewImage(ArsenalProductAway_Sub3));
        Images ArsenalProductAway_Sub4 = new Images("images/products/eng_ars_away_4.jpg");
        arsenalAway.add(imageControllerLocal.createNewImage(ArsenalProductAway_Sub4));

        List<ProductSize> arsenalAwaySize = new ArrayList<>();
        arsenalAwaySize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 13)));
        arsenalAwaySize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("L", 19)));
        arsenalAwaySize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("M", 54)));

        cal.set(2018, Calendar.FEBRUARY, 1); //Year, month and day of month
        date = cal.getTime();

        Product ArsenalAwayProduct = new Product("ARS0002", "Arsenal Away 2017/18", "Looking for an Arsenal shirt? Are you a true Gunner? Good news! We have the latest Arsenal shirt, including printing of name and number. You will find both the home and away jersey. No matter what you are looking for, Sportify offers it!", 149.50, "Arsenal", "Male", "England", date, null, arsenalAway, arsenalAwaySize);
        ArsenalAwayProduct = productControllerLocal.CreateNewProduct(ArsenalAwayProduct);

        List<Images> barcaHome = new ArrayList<>();
        Images BarcaProductHome_Main = new Images("images/products/spain_barca_main.jpg");
        barcaHome.add(imageControllerLocal.createNewImage(BarcaProductHome_Main));
        Images BarcaProductHome_Sub1 = new Images("images/products/spain_barca_1.jpg");
        barcaHome.add(imageControllerLocal.createNewImage(BarcaProductHome_Sub1));
        Images BarcaProductHome_Sub2 = new Images("images/products/spain_barca_2.jpg");
        barcaHome.add(imageControllerLocal.createNewImage(BarcaProductHome_Sub2));
        Images BarcaProductHome_Sub3 = new Images("images/products/spain_barca_3.jpg");
        barcaHome.add(imageControllerLocal.createNewImage(BarcaProductHome_Sub3));

        List<ProductSize> barcaHomeSize = new ArrayList<>();
        barcaHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 36)));
        barcaHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("L", 38)));
        barcaHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("M", 58)));

        cal.set(2018, Calendar.APRIL, 1); //Year, month and day of month
        date = cal.getTime();

        Product BarcaHomeProduct = new Product("Barca0001", "Barcelona Home 2017/18", "FC Barcelona, or the Blaugrana, is one of the world's biggest football clubs in the world. With stars like Messi, Neymar and Suarez in the team, they are always expected to win titles and trophies. Buy your Barcelona Football Jersey here at Sportify with fast delivery! And don't forget that you can customize your name and number on the back too!", 200.90, "Barcelona", "Male", "Spain", date, null, barcaHome, barcaHomeSize);
        BarcaHomeProduct = productControllerLocal.CreateNewProduct(BarcaHomeProduct);

        //Barca Away
        List<Images> barcaAway = new ArrayList<>();
        Images BarcaProductAway_Main = new Images("images/products/spain_barca_away_main.jpg");
        barcaAway.add(imageControllerLocal.createNewImage(BarcaProductAway_Main));
        Images Barca_ProductAway_Sub1 = new Images("images/products/spain_barca_away_1.jpg");
        barcaHome.add(imageControllerLocal.createNewImage(Barca_ProductAway_Sub1));
        Images Barca_ProductAway_Sub2 = new Images("images/products/spain_barca_away_2.jpg");
        barcaAway.add(imageControllerLocal.createNewImage(Barca_ProductAway_Sub2));
        Images Barca_ProductAway_Sub3 = new Images("images/products/spain_barca_away_3.jpg");
        barcaAway.add(imageControllerLocal.createNewImage(Barca_ProductAway_Sub3));
        Images Barca_ProductAway_Sub4 = new Images("images/products/spain_barca_away_4.jpg");
        barcaAway.add(imageControllerLocal.createNewImage(Barca_ProductAway_Sub4));

        List<ProductSize> barcaAwaySize = new ArrayList<>();
        barcaAwaySize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 4)));
        barcaAwaySize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("L", 11)));
        barcaAwaySize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("M", 10)));

        cal.set(2018, Calendar.APRIL, 20); //Year, month and day of month
        date = cal.getTime();

        Product BarcaAwayProduct = new Product("BAR0002", "Barcelona Away 2017/18", "FC Barcelona, or the Blaugrana, is one of the world's biggest football clubs in the world. With stars like Messi, Neymar and Suarez in the team, they are always expected to win titles and trophies. Buy your Barcelona Football Jersey here at Sportify with fast delivery! And don't forget that you can customize your name and number on the back too!", 111.80, "Barcelona", "Male", "Spain", date, null, barcaAway, barcaAwaySize);
        BarcaAwayProduct = productControllerLocal.CreateNewProduct(BarcaAwayProduct);

        //Chelsea Home
        List<Images> chelseaHome = new ArrayList<>();
        Images ChealseaProductHome_Main = new Images("images/products/eng_chels_main.jpg");
        chelseaHome.add(imageControllerLocal.createNewImage(ChealseaProductHome_Main));
        Images ChealseaProductHome_Sub1 = new Images("images/products/eng_chels_1.jpg");
        chelseaHome.add(imageControllerLocal.createNewImage(ChealseaProductHome_Sub1));
        Images ChealseaProductHome_Sub2 = new Images("images/products/eng_chels_2.jpg");
        chelseaHome.add(imageControllerLocal.createNewImage(ChealseaProductHome_Sub2));
        Images ChealseaProductHome_Sub3 = new Images("images/products/eng_chels_3.jpg");
        chelseaHome.add(imageControllerLocal.createNewImage(ChealseaProductHome_Sub3));

        List<ProductSize> chelseaHomeSize = new ArrayList<>();
        chelseaHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 10)));
        chelseaHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("L", 12)));
        chelseaHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("M", 24)));

        cal.set(2018, Calendar.MARCH, 14); //Year, month and day of month
        date = cal.getTime();

        Product ChelseaHomeProduct = new Product("CHL0001", "Chelsea Home 2017/18", "Chelsea is one of the most successful clubs in England. Year after year, they continue to be involved in the fight for top spot. In our Chelsea collection you can find both the Chelsea home shirt and the Chelsea away jersey - so you can support your team. Get them now on Sportify!", 129.90, "Chelsea", "Male", "England", date, null, chelseaHome, chelseaHomeSize);
        ChelseaHomeProduct = productControllerLocal.CreateNewProduct(ChelseaHomeProduct);

        //Chelsea Away
        List<Images> chelseaAway = new ArrayList<>();
        Images ChealseaProductAway_Main = new Images("images/products/eng_chels_away_main.jpg");
        chelseaAway.add(imageControllerLocal.createNewImage(ChealseaProductAway_Main));
        Images ChealseaProductAway_Sub1 = new Images("images/products/eng_chels_away_1.jpg");
        chelseaAway.add(imageControllerLocal.createNewImage(ChealseaProductAway_Sub1));
        Images ChealseaProductAway_Sub2 = new Images("images/products/eng_chels_away_2.jpg");
        chelseaAway.add(imageControllerLocal.createNewImage(ChealseaProductAway_Sub2));
        Images ChealseaProductAway_Sub3 = new Images("images/products/eng_chels_away_3.jpg");
        chelseaAway.add(imageControllerLocal.createNewImage(ChealseaProductAway_Sub3));
        Images ChealseaProductAway_Sub4 = new Images("images/products/eng_chels_away_4.jpg");
        chelseaAway.add(imageControllerLocal.createNewImage(ChealseaProductAway_Sub4));

        List<ProductSize> chelseaAwaySize = new ArrayList<>();
        chelseaAwaySize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 27)));
        chelseaAwaySize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("S", 13)));
        chelseaAwaySize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("M", 17)));

        cal.set(2018, Calendar.FEBRUARY, 28); //Year, month and day of month
        date = cal.getTime();

        Product ChelseaAwayProduct = new Product("CHL0002", "Chelsea Away 2017/18", "Chelsea is one of the most successful clubs in England. Year after year, they continue to be involved in the fight for top spot. In our Chelsea collection you can find both the Chelsea home shirt and the Chelsea away jersey - so you can support your team. Get them now on Sportify!", 100, "Chelsea", "Male", "England", date, null, chelseaAway, chelseaAwaySize);
        ChelseaAwayProduct = productControllerLocal.CreateNewProduct(ChelseaAwayProduct);

        //Real Madrid Home
        List<Images> rmHome = new ArrayList<>();
        Images RmProductHome_Main = new Images("images/products/spain_rm_main.jpg");
        rmHome.add(imageControllerLocal.createNewImage(RmProductHome_Main));
        Images Rm_ProductHome_Sub1 = new Images("images/products/spain_rm_1.jpg");
        rmHome.add(imageControllerLocal.createNewImage(Rm_ProductHome_Sub1));
        Images Rm_ProductHome_Sub2 = new Images("images/products/spain_rm_2.jpg");
        rmHome.add(imageControllerLocal.createNewImage(Rm_ProductHome_Sub2));
        Images Rm_ProductHome_Sub3 = new Images("images/products/spain_rm_3.jpg");
        rmHome.add(imageControllerLocal.createNewImage(Rm_ProductHome_Sub3));
        Images Rm_ProductHome_Sub4 = new Images("images/products/spain_rm_4.jpg");
        rmHome.add(imageControllerLocal.createNewImage(Rm_ProductHome_Sub4));

        List<ProductSize> rmHomeSize = new ArrayList<>();
        rmHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 10)));
        rmHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("L", 13)));
        rmHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("M", 5)));

        cal.set(2018, Calendar.FEBRUARY, 12); //Year, month and day of month
        date = cal.getTime();

        Product RmHomeProduct = new Product("RM0001", "Real Madrid Home 2017/18", "Los Merengues, Los Blancos, Los Galácticos ... Real Madrid go by many nicknames. But what they are most known for is their fantastic football. Club Legend, Zinedine Zidane is now steering the team from the dugout and off the field, managing big stars like Cristiano Ronaldo and Gareth Bale. Sportify have the latest jerseys and you can choose to add a name and number on the back of your new shirt. Shop today with fast delivery!", 59.90, "Real Madrid", "Male", "Spain", date, null, rmHome, rmHomeSize);
        RmHomeProduct = productControllerLocal.CreateNewProduct(RmHomeProduct);

        //Real Madrid Away
        List<Images> rmAway = new ArrayList<>();
        Images RmProductAway_Main = new Images("images/products/spain_rm_away_main.jpg");
        rmAway.add(imageControllerLocal.createNewImage(RmProductAway_Main));
        Images Rm_ProductAway_Sub1 = new Images("images/products/spain_rm_away_1.jpg");
        rmAway.add(imageControllerLocal.createNewImage(Rm_ProductAway_Sub1));
        Images Rm_ProductAway_Sub2 = new Images("images/products/spain_rm_away_2.jpg");
        rmAway.add(imageControllerLocal.createNewImage(Rm_ProductAway_Sub2));
        Images Rm_ProductAway_Sub3 = new Images("images/products/spain_rm_away_3.jpg");
        rmAway.add(imageControllerLocal.createNewImage(Rm_ProductAway_Sub3));
        Images Rm_ProductAway_Sub4 = new Images("images/products/spain_rm_away_4.jpg");
        rmAway.add(imageControllerLocal.createNewImage(Rm_ProductAway_Sub4));

        List<ProductSize> rmAwaySize = new ArrayList<>();
        rmAwaySize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 1)));
        rmAwaySize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("L", 16)));
        rmAwaySize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("M", 13)));

        cal.set(2018, Calendar.MARCH, 1); //Year, month and day of month
        date = cal.getTime();

        Product RmAwayProduct = new Product("RM0002", "Real Madrid Away 2017/18", "Los Merengues, Los Blancos, Los Galácticos ... Real Madrid go by many nicknames. But what they are most known for is their fantastic football. Club Legend, Zinedine Zidane is now steering the team from the dugout and off the field, managing big stars like Cristiano Ronaldo and Gareth Bale. Sportify have the latest jerseys and you can choose to add a name and number on the back of your new shirt. Shop today with fast delivery!", 59.90, "Real Madrid", "Male", "Spain", date, null, rmAway, rmAwaySize);
        RmAwayProduct = productControllerLocal.CreateNewProduct(RmAwayProduct);

        //AC Milan Home
        List<Images> acMilanHome = new ArrayList<>();
        Images acMilanProductHome_Main = new Images("images/products/italy_acmilan_main.jpg");
        acMilanHome.add(imageControllerLocal.createNewImage(acMilanProductHome_Main));
        Images acMilan_ProductHome_Sub1 = new Images("images/products/italy_acmilan_1.jpg");
        acMilanHome.add(imageControllerLocal.createNewImage(acMilan_ProductHome_Sub1));
        Images acMilan_ProductHome_Sub2 = new Images("images/products/italy_acmilan_2.jpg");
        acMilanHome.add(imageControllerLocal.createNewImage(acMilan_ProductHome_Sub2));
        Images acMilan_ProductHome_Sub3 = new Images("images/products/italy_acmilan_3.jpg");
        acMilanHome.add(imageControllerLocal.createNewImage(acMilan_ProductHome_Sub3));
        Images acMilan_ProductHome_Sub4 = new Images("images/products/italy_acmilan_4.jpg");
        acMilanHome.add(imageControllerLocal.createNewImage(acMilan_ProductHome_Sub4));

        List<ProductSize> acMilanHomeSize = new ArrayList<>();
        acMilanHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("XL", 5)));
        acMilanHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("L", 7)));
        acMilanHomeSize.add(productSizeControllerLocal.createSizeForProduct(new ProductSize("S", 9)));

        cal.set(2018, Calendar.MARCH, 26); //Year, month and day of month
        date = cal.getTime();

        Product AcMilanHomeProduct = new Product("ACM0001", "AC Milan Home 2017/18", "AC Milan is an Italian football club with fantastic history. During the early 90s, the club was dominating European football with an incredibly strong defensive line. The AC Milan football shirt carries a great legacy and has been worn by many fantastic players over the years. If you are looking to get your hands on the AC Milan football shirt then you can naturally do so here on Sportify. Get them now!", 59.90, "AC Milan", "Male", "Italy", date, null, acMilanHome, acMilanHomeSize);
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
            ProductPurchase p1 = productPurchaseControllerLocal.createProductPurchase((new ProductPurchase(80.45, 2, cust1OrderJan, RmHomeProduct)));
            customerOrderControllerLocal.addProductPurchase(cust1OrderJan.getId(), p1);

            CustomerOrder cust1OrderFeb = new CustomerOrder(100.0, 20, dateFeb, "Delivered", newCustomer1);
            cust1OrderFeb = customerOrderControllerLocal.CreateNewCustomerOrder(cust1OrderFeb);
            customerOrderControllerLocal.addProductPurchase(cust1OrderFeb.getId(), productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(100.0, 1, cust1OrderFeb, ChelseaAwayProduct)));
            CustomerOrder cust1OrderMar = new CustomerOrder(659.80, 100, dateMar, "Delivered", newCustomer1);
            cust1OrderMar = customerOrderControllerLocal.CreateNewCustomerOrder(cust1OrderMar);
            customerOrderControllerLocal.addProductPurchase(cust1OrderMar.getId(), productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(300.00, 1, cust1OrderMar, RmAwayProduct)));
            customerOrderControllerLocal.addProductPurchase(cust1OrderMar.getId(), productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(359.80, 1, cust1OrderMar, atlMadridHomeProduct)));

            //  customerOrderControllerLocal.CreateNewCustomerOrder(cust1OrderMar);
            cal.set(2018, Calendar.JANUARY, 28); //Year, month and day of month
            dateJan = cal.getTime();
            cal.set(2018, Calendar.FEBRUARY, 27);
            dateFeb = cal.getTime();
            cal.set(2018, Calendar.MARCH, 30);
            dateMar = cal.getTime();

            // create product reviews for customer 1
            ProductReview prodReview1 = new ProductReview(3, "I like it just like that!", dateFeb, ChelseaAwayProduct, cust1OrderFeb);
            ProductReview prodReview2 = new ProductReview(4, "I don't like the design of the shirt!", dateMar, RmAwayProduct, cust1OrderMar);
            ProductReview prodReview6 = new ProductReview(4, "Atleti GO GOGO!", dateMar, atlMadridHomeProduct, cust1OrderMar);

            prodReview1 = productReviewControllerLocal.CreateNewProductReview(prodReview1);
            prodReview2 = productReviewControllerLocal.CreateNewProductReview(prodReview2);
            prodReview6 = productReviewControllerLocal.CreateNewProductReview(prodReview6);

            ChelseaAwayProduct.getProductReviews().add(prodReview1);
            RmAwayProduct.getProductReviews().add(prodReview2);
            atlMadridHomeProduct.getProductReviews().add(prodReview6);

            cust1OrderFeb.getProductReviews().add(prodReview1);
            cust1OrderMar.getProductReviews().add(prodReview2);
            cust1OrderMar.getProductReviews().add(prodReview6);

            //Create and allocate voucher
            List<CustomerVoucher> cvlist = newCustomer1.getCustomerVouchers();
            Date voucherexpiry = new GregorianCalendar(2019, 5, 25).getTime();
            Voucher voucher1 = new Voucher(20.00, 5, "AXDA314A", "Voucher", new Date(), voucherexpiry, cvlist);
            voucherControllerLocal.createNewVoucher(voucher1);
            CustomerVoucher cv = customerVoucherControllerLocal.createNewCustomerVoucher(new CustomerVoucher(null, newCustomer1, voucher1));
            cv.setVoucher(voucher1);
            cvlist.add(cv);

            CustomerOrder cust2OrderJan = new CustomerOrder(352.35, 20, dateJan, "Delivered", newCustomer2);
            cust2OrderJan = customerOrderControllerLocal.CreateNewCustomerOrder(cust2OrderJan);
            customerOrderControllerLocal.addProductPurchase(cust2OrderJan.getId(), productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(80.45, 1, cust2OrderJan, RmHomeProduct)));
            customerOrderControllerLocal.addProductPurchase(cust2OrderJan.getId(), productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(66.45, 1, cust2OrderJan, AcMilanHomeProduct)));
            customerOrderControllerLocal.addProductPurchase(cust2OrderJan.getId(), productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(95.00, 1, cust2OrderJan, ChelseaAwayProduct)));
            customerOrderControllerLocal.addProductPurchase(cust2OrderJan.getId(), productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(110.45, 1, cust2OrderJan, ChelseaHomeProduct)));

            CustomerOrder cust2OrderFeb = new CustomerOrder(250.0, 20, dateFeb, "Delivered", newCustomer2);
            cust2OrderFeb = customerOrderControllerLocal.CreateNewCustomerOrder(cust2OrderFeb);
            customerOrderControllerLocal.addProductPurchase(cust2OrderFeb.getId(), productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(125.0, 2, cust2OrderFeb, BarcaAwayProduct)));

            CustomerOrder cust2OrderMar = new CustomerOrder(659.80, 100, dateMar, "Delivered", newCustomer2);
            cust2OrderMar = customerOrderControllerLocal.CreateNewCustomerOrder(cust2OrderMar);
            customerOrderControllerLocal.addProductPurchase(cust2OrderMar.getId(), productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(300.00, 1, cust2OrderMar, RmAwayProduct)));
            customerOrderControllerLocal.addProductPurchase(cust2OrderMar.getId(), productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(359.80, 1, cust2OrderMar, atlMadridHomeProduct)));

            cal.set(2018, Calendar.FEBRUARY, 21);
            dateFeb = cal.getTime();
            cal.set(2018, Calendar.MARCH, 14);
            dateMar = cal.getTime();

            // create product reviews for customer 2
            ProductReview prodReview3 = new ProductReview(5, "Awesome product!", dateFeb, ChelseaAwayProduct, cust2OrderJan);
            ProductReview prodReview4 = new ProductReview(2, "Real Madrid sucks!", dateFeb, RmAwayProduct, cust2OrderJan);

            prodReview3 = productReviewControllerLocal.CreateNewProductReview(prodReview3);
            prodReview4 = productReviewControllerLocal.CreateNewProductReview(prodReview4);

            ChelseaAwayProduct.getProductReviews().add(prodReview3);
            RmAwayProduct.getProductReviews().add(prodReview4);

            cust2OrderJan.getProductReviews().add(prodReview3);
            cust2OrderJan.getProductReviews().add(prodReview4);

            CustomerOrder cust3OrderFeb = new CustomerOrder(719.60, 100, dateFeb, "Delivered", newCustomer3);
            cust3OrderFeb = customerOrderControllerLocal.CreateNewCustomerOrder(cust3OrderFeb);
            customerOrderControllerLocal.addProductPurchase(cust3OrderFeb.getId(), productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(359.80, 2, cust3OrderFeb, atlMadridHomeProduct)));

            CustomerOrder cust3OrderMar = new CustomerOrder(1133.40, 100, dateMar, "Delivered", newCustomer3);
            cust3OrderMar = customerOrderControllerLocal.CreateNewCustomerOrder(cust3OrderMar);
            customerOrderControllerLocal.addProductPurchase(cust3OrderMar.getId(), productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(100.0, 1, cust3OrderMar, ArsenalAwayProduct)));
            customerOrderControllerLocal.addProductPurchase(cust3OrderMar.getId(), productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(120.90, 2, cust3OrderMar, ArsenalHomeProduct)));
            customerOrderControllerLocal.addProductPurchase(cust3OrderMar.getId(), productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(90.90, 2, cust3OrderMar, liverpoolHomeProduct)));
            customerOrderControllerLocal.addProductPurchase(cust3OrderMar.getId(), productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(179.90, 2, cust3OrderMar, manuHomeProduct)));
            customerOrderControllerLocal.addProductPurchase(cust3OrderMar.getId(), productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(125.0, 2, cust3OrderMar, BarcaAwayProduct)));

            cal.set(2018, Calendar.APRIL, 1);
            Date dateApril = cal.getTime();
            CustomerOrder cust4OrderApril = new CustomerOrder(129.90, 100, dateApril, "Delivered", newCustomer4);
            cust4OrderApril = customerOrderControllerLocal.CreateNewCustomerOrder(cust4OrderApril);
            customerOrderControllerLocal.addProductPurchase(cust4OrderApril.getId(), productPurchaseControllerLocal.createProductPurchase(new ProductPurchase(129.90, 1, cust4OrderApril, ChelseaHomeProduct)));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}

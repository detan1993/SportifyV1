/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.customer.managedbean;

import ejb.session.stateless.CustomerControllerLocal;
import ejb.session.stateless.CustomerOrderControllerLocal;
import ejb.session.stateless.CustomerVoucherControllerLocal;
import ejb.session.stateless.ProductControllerLocal;
import ejb.session.stateless.ProductPurchaseControllerLocal;
import ejb.session.stateless.ProductSizeControllerLocal;
import ejb.session.stateless.VoucherControllerLocal;
import entity.Customer;
import entity.CustomerOrder;
import entity.CustomerVoucher;
import entity.Product;
import entity.ProductPurchase;
import entity.ProductSize;
import entity.Voucher;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

/**
 *
 * @author JiongYi
 */
@Named(value = "customerCheckOutManagedBean")
@ViewScoped
public class CustomerCheckOutManagedBean implements Serializable{

    
    @EJB
    private ProductControllerLocal productcontroller;
    @EJB
    private CustomerOrderControllerLocal customerordercontroller;
    @EJB
    private VoucherControllerLocal vouchercontroller;
    @EJB
    private CustomerVoucherControllerLocal customervouchercontroller;
    @EJB
    private CustomerControllerLocal customercontroller;
    @EJB
    private ProductPurchaseControllerLocal productpurchasecontroller;
    @EJB
    private ProductSizeControllerLocal productsizecontroller;
    private String subtotaldisplay;
    private String totaldisplay;
    private Customer loggedincustomer;
    private String promoCode;
   
    //For viewing of the address
    private String addrbtnval;
    private boolean displayedit;
    private boolean displayconfirm;
    private int activetab;
    
    //If customer has applied available voucher
    private Voucher appliedvoucher;
    
    //Billing address
    private boolean samebillingaddr;
    private boolean showsamebillingaddr;
    
    private List<String[]> cartitems;
    
    public CustomerCheckOutManagedBean(){
       
    }
    
    @PostConstruct
    public void postConstruct()
    {
        setCartitems(loadShoppingCartItems());
        setLoggedincustomer(loadCustomer());
        setAddrbtnval("Edit");
        setDisplayconfirm(true);
        setActivetab(1);
        //setShowsamebillingaddr(true);
        setSamebillingaddr(true);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        try {
           String promosub = (String)sessionMap.get("promosubtotal");
           if (Double.parseDouble(promosub)> 0){
               subtotaldisplay = promosub;
               totaldisplay = (String)sessionMap.get("promototal");
               appliedvoucher = (Voucher)sessionMap.get("appliedvoucher");
           }
        
        }
        catch (Exception ex){
            
        }
    }
    
    public List<String[]> loadShoppingCartItems(){
        double subtotal = 0.0;
        double total = 0.0;
        List<String[]>shoppingCartItems = new ArrayList<>();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        ArrayList<String[]> cartitems = (ArrayList<String[]>)sessionMap.get("currentItemCart");
        
        if (cartitems!=null){
        for (int i = 0; i < cartitems.size(); i++){
          String [] sessioncartitem = new String [8];
          Product p = productcontroller.retrieveSingleProduct(Long.parseLong(cartitems.get(i)[0]));
          ProductSize ps = productsizecontroller.retrieveSingleProductSize(Long.parseLong(cartitems.get(i)[1])); //[1] is size id, [1] becomes size
          sessioncartitem[0] = cartitems.get(i)[0];
          sessioncartitem[1] = cartitems.get(i)[1];
          sessioncartitem[2] = cartitems.get(i)[2];
          sessioncartitem[3] = p.getProductName();
          sessioncartitem[4] = p.getProductCode();
          sessioncartitem[5] = String.valueOf(p.getPrice());
          sessioncartitem[6] = p.getImages().get(0).getImagePath();
          sessioncartitem[7] = ps.getSize();
          shoppingCartItems.add(sessioncartitem);
          subtotal = subtotal + Double.parseDouble(sessioncartitem[5])*Integer.parseInt(sessioncartitem[2]);
          total = subtotal + 5.00;
          setSubtotaldisplay(String.format("%.2f", subtotal));
          setTotaldisplay(String.format("%.2f", total));          
        }
        }
      
        return shoppingCartItems;    
    }
    
    //Load shipping address details
    public Customer loadCustomer(){
         ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
         Map<String, Object> sessionMap = externalContext.getSessionMap();
         Customer c = (Customer)sessionMap.get("currentCustomer");       
         return c;
    }
    
    //Make order when confirm order is pressed
    public void makeOrder(){
          double total = Double.parseDouble(getTotaldisplay());
          double pointsawarded = 0.10 * total;
          Date datepaid = new Date();
          ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
          Map<String, Object> sessionMap = externalContext.getSessionMap();
          Customer c = (Customer)sessionMap.get("currentCustomer");      
          CustomerOrder customerorder = customerordercontroller.CreateNewCustomerOrder(new CustomerOrder(total,pointsawarded,datepaid,"Pending",c));
          //Loop and insert product purchase for each item in shopping cart
          for (int i = 0; i <cartitems.size();i++){
             String [] cartitem = new String[7]; 
             cartitem = cartitems.get(i);
             ProductSize ps = productsizecontroller.retrieveSingleProductSize(Long.parseLong(cartitem[7]));
             double pricepurchase = Double.parseDouble(cartitem[5]);
             int qtypurchase = Integer.parseInt(cartitem[2]);
             ps.setQty(ps.getQty() - qtypurchase);
             productsizecontroller.updateSizeForProduct(ps);
             Product p = productcontroller.retrieveSingleProduct(Long.parseLong(cartitem[0]));
             ProductPurchase productpurchase = productpurchasecontroller.createProductPurchase(new ProductPurchase(pricepurchase,qtypurchase,customerorder,p));
             
          }
          sendEmail();
          if (appliedvoucher!=null){  
             CustomerVoucher cv = customervouchercontroller.retrieveCustomerVoucher(c, appliedvoucher);
             customervouchercontroller.useCustomerVoucher(customerorder,appliedvoucher,cv);
          }
           try {
          Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
          flash.put("tab", 1);
          FacesContext.getCurrentInstance().getExternalContext().redirect("customerTransactionHistory.xhtml");
          //Display msg in home page and clear session
          
          }
          catch (Exception ex){
              
          }
    } 
    
    
    public void removeCartItem(String [] cartitem){
        for(int i = 0; i < cartitems.size(); i ++){
            if (cartitem[0].equals(cartitems.get(i)[0])){
                //If in current list remove it
                cartitems.remove(i);
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                Map<String, Object> sessionMap = externalContext.getSessionMap();
                sessionMap.put("currentItemCart", cartitems);
            }
         }
    }
  
    public void confirmAddress(){
        //If edit button is pressed
        if (addrbtnval.equals("Edit")){
            //edit function, update customer values
            setAddrbtnval("Save and continue");
            setDisplayedit(true);
            setDisplayconfirm(false);
            setActivetab(1); 
        }
        else { 
            //If confirm button is pressed
             loggedincustomer.setFirstName(loggedincustomer.getFirstName());
             loggedincustomer.setLastName(loggedincustomer.getLastName());
             loggedincustomer.setAddress(loggedincustomer.getAddress());
             loggedincustomer.setZipCode(loggedincustomer.getZipCode());
             loggedincustomer.setPhoneNum(loggedincustomer.getPhoneNum());
             customercontroller.updateCustomer(loggedincustomer);
             ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
             Map<String, Object> sessionMap = externalContext.getSessionMap();
             sessionMap.put("loggedincustomer", loggedincustomer);
             setAddrbtnval("Edit");
             setDisplayedit(false);
             setDisplayconfirm(true);
             
        }
       
    }
    
    public void toggleBillingAddress(){
        if (samebillingaddr==true){
            //Let user fill in new billing address
            samebillingaddr = true;
            showsamebillingaddr = false;
        }
        else {
            samebillingaddr = false;
            showsamebillingaddr = true;
        }
    }
    
    public void proceedPayment(){ 
         setActivetab(0);
         setDisplayconfirm(false);
         RequestContext.getCurrentInstance().scrollTo("paymentform:paymentAccordion");
    }
    
    public void checkPromoCode(){
        FacesContext context = FacesContext.getCurrentInstance();
        String email ="";
        try {
             email = loggedincustomer.getEmail();
        }
        catch (Exception ex){
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Plesae log in to apply promo codes!"));
            return;
        }
        try {     
        
        if (Double.parseDouble(subtotaldisplay) > 0){
        Voucher v = vouchercontroller.retrieveCustomerVoucher(promoCode, email);
        if (v.getId() != null){
        appliedvoucher = v;
        double promovalue = v.getVoucherValue();
        double discountsubtotal = ((100 - promovalue)/100) * Double.parseDouble(this.subtotaldisplay);
        double discounttotal = ((100 - promovalue)/100) * Double.parseDouble(this.totaldisplay);
        this.setSubtotaldisplay(String.format("%.2f", discountsubtotal));
        this.setTotaldisplay(String.format("%.2f", discounttotal));
        subtotaldisplay = String.format("%.2f", discountsubtotal);
        totaldisplay = String.format("%.2f", discounttotal) ;
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("promosubtotal", subtotaldisplay);
        sessionMap.put("promototal", totaldisplay);
        sessionMap.put("appliedvoucher", appliedvoucher);
        context.addMessage(null, new FacesMessage("Successful",  "Your Promo of " + v.getVoucherValue()  + "% has been applied!"));
        }
        else {        
             context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Invalid Promo code!"));
        }
        }
        }
        catch (Exception ex){
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "You do not have any products in your shopping cart!"));
            return;
        }
    }
    
    public void sendEmail(){
        final String username = "smurfalt1202@gmail.com";
		final String password = "hearthstone";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("smurfalt1202@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("joankhor1202@hotmail.com"));
			message.setSubject("Your order has been confirmed!");
			message.setText("Dear " + loggedincustomer.getFirstName() + ","
				+ "\n\n We have received your order");

			Transport.send(message);

			System.out.println("Done");

		}  catch (MessagingException e) {
			throw new RuntimeException(e);
		}
    }
    
    /**
     * @return the cartitems
     */
    public List<String[]> getCartitems() {
        return cartitems;
    }

    /**
     * @param cartitems the cartitems to set
     */
    public void setCartitems(List<String[]> cartitems) {
        this.cartitems = cartitems;
    }

    /**
     * @return the subtotaldisplay
     */
    public String getSubtotaldisplay() {
        return subtotaldisplay;
    }

    /**
     * @param subtotaldisplay the subtotaldisplay to set
     */
    public void setSubtotaldisplay(String subtotaldisplay) {
        this.subtotaldisplay = subtotaldisplay;
    }

    /**
     * @return the totaldisplay
     */
    public String getTotaldisplay() {
        return totaldisplay;
    }

    /**
     * @param totaldisplay the totaldisplay to set
     */
    public void setTotaldisplay(String totaldisplay) {
        this.totaldisplay = totaldisplay;
    }

    /**
     * @return the loggedincustomer
     */
    public Customer getLoggedincustomer() {
        return loggedincustomer;
    }

    /**
     * @param loggedincustomer the loggedincustomer to set
     */
    public void setLoggedincustomer(Customer loggedincustomer) {
        this.loggedincustomer = loggedincustomer;
    }


    /**
     * @return the promoCode
     */
    public String getPromoCode() {
        return promoCode;
    }

    /**
     * @param promoCode the promoCode to set
     */
    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    /**
     * @return the editPressed
     */
    

    /**
     * @return the addrbtnval
     */
    public String getAddrbtnval() {
        return addrbtnval;
    }

    /**
     * @param addrbtnval the addrbtnval to set
     */
    public void setAddrbtnval(String addrbtnval) {
        this.addrbtnval = addrbtnval;
    }

    /**
     * @return the displayedit
     */
    public boolean isDisplayedit() {
        return displayedit;
    }

    /**
     * @param displayedit the displayedit to set
     */
    public void setDisplayedit(boolean displayedit) {
        this.displayedit = displayedit;
    }

    /**
     * @return the displayconfirm
     */
    public boolean isDisplayconfirm() {
        return displayconfirm;
    }

    /**
     * @param displayconfirm the displayconfirm to set
     */
    public void setDisplayconfirm(boolean displayconfirm) {
        this.displayconfirm = displayconfirm;
    }

    /**
     * @return the activetab
     */
    public int getActivetab() {
        return activetab;
    }

    /**
     * @param activetab the activetab to set
     */
    public void setActivetab(int activetab) {
        this.activetab = activetab;
    }

    /**
     * @return the samebillingaddr
     */
    public boolean isSamebillingaddr() {
        return samebillingaddr;
    }

    /**
     * @param samebillingaddr the samebillingaddr to set
     */
    public void setSamebillingaddr(boolean samebillingaddr) {
        this.samebillingaddr = samebillingaddr;
    }

    /**
     * @return the showsamebillingaddr
     */
    public boolean isShowsamebillingaddr() {
        return showsamebillingaddr;
    }

    /**
     * @param showsamebillingaddr the showsamebillingaddr to set
     */
    public void setShowsamebillingaddr(boolean showsamebillingaddr) {
        this.showsamebillingaddr = showsamebillingaddr;
    }

    /**
     * @return the appliedvoucher
     */
    public Voucher getAppliedvoucher() {
        return appliedvoucher;
    }

    /**
     * @param appliedvoucher the appliedvoucher to set
     */
    public void setAppliedvoucher(Voucher appliedvoucher) {
        this.appliedvoucher = appliedvoucher;
    }

   
    /**
     * @return the subtotal
     */
    
   
}

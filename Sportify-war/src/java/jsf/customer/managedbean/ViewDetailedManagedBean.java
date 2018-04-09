package jsf.customer.managedbean;

import ejb.session.stateless.CustomerOrderControllerLocal;
import ejb.session.stateless.ProductControllerLocal;
import ejb.session.stateless.ProductReviewControllerLocal;
import entity.Customer;
import entity.CustomerOrder;
import entity.Images;
import entity.Product;
import entity.ProductPurchase;
import entity.ProductReview;
import entity.ProductSize;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;

@Named(value = "viewDetailedManagedBean")
@ViewScoped
public class ViewDetailedManagedBean implements Serializable {

    @EJB
    private ProductControllerLocal productController;
    @EJB
    private ProductReviewControllerLocal productreviewcontroller;
    @EJB 
    private CustomerOrderControllerLocal customerordercontroller;
    private Product product;
    private List<String> images;
    private String selectedImage;
    private String sizeSelected;
    private String quantity = "1";
    private Map<String, String> sizeDropDown = new HashMap<String, String>();
    private ArrayList<String[]> checkIfCartExist;
    private double priceOnChange;
    private List<ProductReview> productReviews;
    private Integer productrating;
    private String productreview;
    private String writeReviewVal;
    private boolean hasOwnReview;

    @PostConstruct
    public void postConstruct() {

        product = new Product();
        images = new ArrayList<>();
        productReviews = new ArrayList<>();

        //sizesAvailable = new ArrayList<>();
        List<Images> getProdImages = new ArrayList<Images>();

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String productId = request.getParameter("productId");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tempProductId", true);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tempProductIdStr", productId);

        try {
            long productIdLong = Long.parseLong(productId);
            if (productId != null && !productId.equals("")) {
                product = productController.retrieveSingleProduct(productIdLong);
                priceOnChange = product.getPrice();

                //get product sizes for this product                 
                List<ProductSize> prodSize = new ArrayList<>();
                // List<String> sizesAvailable;
                prodSize = product.getSizes();

                for (ProductSize p : prodSize) {
                    //sizesAvailable.add(p.getSize());
                    long prodSizeId = p.getId();
                    String prodSizeIdStr = String.valueOf(prodSizeId);
                    sizeDropDown.put(p.getSize(), prodSizeIdStr); //label, value
                }

                //get images for this product
                if (product != null) {
                    if (product.getImages() != null) {
                        getProdImages = product.getImages();

                        int skipFirstImage = 0;
                        for (Images image : getProdImages) {
                            if (skipFirstImage == 0) {
                            } else {
                                images.add(image.getImagePath());
                            }
                            skipFirstImage++;
                        }
                    }
                    writeReviewVal = "Write a review";
                    //get reviews for this product
                    productReviews = productreviewcontroller.retrieveProductReviewsByProductId(product.getId());
                    defaultUserReview();
                }

            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while retrieving Product Id. " + ex.getMessage(), null));
        }
    }

    public void qtyOnChange() {
        System.out.println("Inside method...");
        if (quantity != null && !quantity.equals("")) {
            int qty = Integer.parseInt(quantity);
            priceOnChange = qty * product.getPrice();
            System.out.println("Inside if...");
        }

    }

    public void addToCart() {

        checkIfCartExist = (ArrayList<String[]>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentItemCart");
        ArrayList<String[]> itemsAdded = new ArrayList<>();

        if (checkIfCartExist != null) {
            Iterator<String[]> i = checkIfCartExist.iterator();
            //shoppingCart exist
            System.out.println("Cart Session exist");
            boolean updatingCurrentItem = false;

            while (i.hasNext()) {
                String[] item = i.next();

                System.out.println("Compare prodId : " + item[0] + " = " + product.getId().toString());
                System.out.println("Compare prodSizeId : " + item[1] + " = " + sizeSelected);
                if (item[0].equals(product.getId().toString()) && item[1].equals(sizeSelected)) {
                    int prevQuantity = Integer.parseInt(item[2]);
                    System.out.println("PrevQuantity: " + prevQuantity);
                    int newQuantity = prevQuantity + Integer.parseInt(quantity);
                    System.out.println("New Quantity: " + newQuantity);
                    i.remove();

                    String[] newItemDetail = new String[3];
                    newItemDetail[0] = product.getId().toString();
                    newItemDetail[1] = sizeSelected;
                    newItemDetail[2] = String.valueOf(newQuantity);
                    checkIfCartExist.add(newItemDetail);
                    updatingCurrentItem = true;
                    break;
                }
            }

            if (updatingCurrentItem == false) {
                checkIfCartExist.add(newItem());
            }

            System.out.println("Arraylist count size session cart: " + checkIfCartExist.size());
            for (String[] temp : checkIfCartExist) {
                System.out.println(Arrays.toString(temp));
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Item Added Successfully!", "Product: " + product.getProductName()));

        } else {
            System.out.println("Cart Session does not exist");

            itemsAdded.add(newItem());
            FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentItemCart", itemsAdded);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentItemCartExist", true);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Item Added Successfully!", "Product: " + product.getProductName()));

            System.out.println("Arrylist count size of itemsAdded: " + itemsAdded.size());
            for (String[] temp : itemsAdded) {
                System.out.println(Arrays.toString(temp));
            }
        }
    }

    public void notifyOnComplete() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Item Added Successfully!", "Product: " + product.getProductName()));
    }

    public String[] newItem() {
        String[] cartItems = new String[3];
        cartItems[0] = product.getId().toString();
        cartItems[1] = sizeSelected;
        cartItems[2] = quantity;
        return cartItems;
    }

    public void checkLoggedIn(){
       FacesContext context = FacesContext.getCurrentInstance();   
       RequestContext reqcontext = RequestContext.getCurrentInstance();
       ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
       Map<String, Object> sessionMap = externalContext.getSessionMap();
       Customer c = (Customer)sessionMap.get("currentCustomer");   
       if (c==null){
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Plesae log in to write a review!"));
       }
       else {
           //popup dialog
           reqcontext.execute("PF('writeReviewDialog').show()");
       }
    }
    
    public void defaultUserReview(){
       ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
       Map<String, Object> sessionMap = externalContext.getSessionMap();
       Customer c = (Customer)sessionMap.get("currentCustomer");  
       if (c==null){
           return;
       }
       List<CustomerOrder> colist = customerordercontroller.GetCustomerOrder(c.getId());
       long coid = 0;
       for (int i=0; i <colist.size(); i++){
            List<ProductPurchase> pp = colist.get(i).getProductPurchase();
             for (int j = 0; j < pp.size(); j++){
                 if (pp.get(j).getProductPurchase().getId() == product.getId()){
                     coid = colist.get(i).getId();
                     ProductReview pr = productreviewcontroller.getCustomerOrderProductReview(product.getId(), coid);
                     if (pr==null){
                         return;
                     }
                     productreview = pr.getReview();
                     productrating = pr.getRating();
                     writeReviewVal = "Edit my review";
                     return;
             }
             }         
    }
    }
    
    
    public void createReview(){
       FacesContext context = FacesContext.getCurrentInstance();   
       RequestContext reqcontext = RequestContext.getCurrentInstance();
       ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
       Map<String, Object> sessionMap = externalContext.getSessionMap();
       Customer c = (Customer)sessionMap.get("currentCustomer");   
       List<CustomerOrder> colist = customerordercontroller.GetCustomerOrder(c.getId());
       long coid = 0;
       for (int i=0; i <colist.size(); i++){
            List<ProductPurchase> pp = colist.get(i).getProductPurchase();
             for (int j = 0; j < pp.size(); j++){
                 if (pp.get(j).getProductPurchase().getId() == product.getId()){
                     coid = colist.get(i).getId();
                     break;
             }
             }
       }
       if (coid == 0){
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "You can only review products you have purchased!"));
            reqcontext.execute("PF('writeReviewDialog').hide();");
            return;
       }
       String hasreview = productreviewcontroller.retrieveCustomerOrderProductReview(product.getId(), coid);
       if (hasreview != null){
           //existing review
           ProductReview pr = productreviewcontroller.getCustomerOrderProductReview(product.getId(), coid);
           pr.setRating(productrating);
           pr.setReview(productreview);
           productreviewcontroller.updateProductReview(pr);
           for (int i = 0; i < productReviews.size(); i++){
               if (productReviews.get(i).getId() == pr.getId()){
                   productReviews.remove(i);
                   productReviews.add(i,pr);
                    reqcontext.execute("PF('writeReviewDialog').hide();");
                   return;
               }
           }
           
       } 
       else {
         //get customer order that contains the product
         for (int i = 0; i < colist.size(); i++){
             List<ProductPurchase> pp = colist.get(i).getProductPurchase();
             for (int j = 0; j < pp.size(); j++){
                 if (pp.get(j).getProductPurchase().getId() == product.getId()){
                     ProductReview pr = productreviewcontroller.CreateNewProductReview(new ProductReview(productrating,productreview,new Date(),product,colist.get(i))); 
                      productReviews.add(pr);
                      productreviewcontroller.updateProductReview(pr);
                      writeReviewVal = "Edit my review";
                      reqcontext.execute("PF('writeReviewDialog').hide();");
                      return;
                 }
             }
         }
       }
     
    }
    
    
    
    public String checkOutRedirect() {
        return "customerCheckout?faces-redirect=true";
    }

    public ViewDetailedManagedBean() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(String selectedImage) {
        this.selectedImage = selectedImage;
    }

    public String getSizeSelected() {
        return sizeSelected;
    }

    public void setSizeSelected(String sizeSelected) {
        this.sizeSelected = sizeSelected;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Map<String, String> getSizeDropDown() {
        return sizeDropDown;
    }

    public void setSizeDropDown(Map<String, String> sizeDropDown) {
        this.sizeDropDown = sizeDropDown;
    }

    public ArrayList<String[]> getCheckIfCartExist() {
        return checkIfCartExist;
    }

    public void setCheckIfCartExist(ArrayList<String[]> checkIfCartExist) {
        this.checkIfCartExist = checkIfCartExist;
    }

    public double getPriceOnChange() {
        return priceOnChange;
    }

    public void setPriceOnChange(double priceOnChange) {
        this.priceOnChange = priceOnChange;
    }

    public List<ProductReview> getProductReviews() {
        return productReviews;
    }

    public void setProductReviews(List<ProductReview> productReviews) {
        this.productReviews = productReviews;
    }

    /**
     * @return the productrating
     */
    public Integer getProductrating() {
        return productrating;
    }

    /**
     * @param productrating the productrating to set
     */
    public void setProductrating(Integer productrating) {
        this.productrating = productrating;
    }

    /**
     * @return the productreview
     */
    public String getProductreview() {
        return productreview;
    }

    /**
     * @param productreview the productreview to set
     */
    public void setProductreview(String productreview) {
        this.productreview = productreview;
    }

    /**
     * @return the hasOwnReview
     */
    public boolean isHasOwnReview() {
        return hasOwnReview;
    }

    /**
     * @param hasOwnReview the hasOwnReview to set
     */
    public void setHasOwnReview(boolean hasOwnReview) {
        this.hasOwnReview = hasOwnReview;
    }

    /**
     * @return the writeReviewVal
     */
    public String getWriteReviewVal() {
        return writeReviewVal;
    }

    /**
     * @param writeReviewVal the writeReviewVal to set
     */
    public void setWriteReviewVal(String writeReviewVal) {
        this.writeReviewVal = writeReviewVal;
    }
}

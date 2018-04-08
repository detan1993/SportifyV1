package jsf.customer.managedbean;

import ejb.session.stateless.ProductControllerLocal;
import entity.Images;
import entity.Product;
import entity.ProductReview;
import entity.ProductSize;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;

@Named(value = "viewDetailedManagedBean")
@ViewScoped
public class ViewDetailedManagedBean implements Serializable {

    @EJB
    private ProductControllerLocal productController;

    private Product product;
    private List<String> images;
    private String selectedImage;
    private String sizeSelected;
    private String quantity = "1";
    private Map<String, String> sizeDropDown = new HashMap<String, String>();
    private ArrayList<String[]> checkIfCartExist;
    private double priceOnChange;
    private List<ProductReview> productReviews;

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

                    //get reviews for this product
                    productReviews = product.getProductReviews();
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
}

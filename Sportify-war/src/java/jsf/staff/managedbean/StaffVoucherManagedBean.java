/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.staff.managedbean;

import ejb.session.stateless.CustomerControllerLocal;
import ejb.session.stateless.CustomerVoucherControllerLocal;
import ejb.session.stateless.VoucherControllerLocal;
import entity.Customer;
import entity.CustomerVoucher;
import entity.Voucher;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author shanw
 */
@Named(value = "staffVoucherManagedBean")
@ViewScoped
public class StaffVoucherManagedBean implements Serializable{
    @EJB(name="voucherControllerLocal")
    private VoucherControllerLocal voucherControllerLocal;
    
    @EJB(name="CustomerControllerLocal")
    private CustomerControllerLocal customerControllerLocal;
    
    @EJB(name="customerVoucherControllerLocal")
    private CustomerVoucherControllerLocal customerVoucherControllerLocal;

    private List<Voucher> vouchers;
    private Voucher newVoucher;
    private Voucher selectedVoucher;
    private List<Voucher> filteredVouchers;
    
    private boolean isSpecific;
    private Map<String,Integer> monthList;
    private int selectedMonth;
    
    private int totalVoucherAmount;
    private int currentVoucherAmount;
    private int duration;
    private double usesPerDay;
     
    public StaffVoucherManagedBean() {
        vouchers = new ArrayList<Voucher>();
        newVoucher = new Voucher();
        filteredVouchers = new ArrayList<Voucher>();
        monthList = new HashMap<String,Integer>();
    }
    
    @PostConstruct
    public void postConstruct(){
        vouchers = voucherControllerLocal.retrieveVouchers();
        newVoucher = new Voucher();
        filteredVouchers = vouchers;
        isSpecific = false;
        
        //Populate month list
        monthList.put("January",1);
        monthList.put("February",2);
        monthList.put("March",3);
        monthList.put("April",4);
        monthList.put("May",5);
        monthList.put("June",6);
        monthList.put("July",7);
        monthList.put("August",8);
        monthList.put("September",9);
        monthList.put("October",10);
        monthList.put("November",11);
        monthList.put("December",12);
    }

    public void createNewVoucher(ActionEvent event){
        try
        {
            System.out.println(" Selected month: " + selectedMonth);
            Date now = new Date();
            newVoucher.setDateCreated(now);
            voucherControllerLocal.createNewVoucher(newVoucher);
            List<Customer> customersToBeGivenVouchers = new ArrayList<Customer>();
            CustomerVoucher cv = new CustomerVoucher();
            
            if(isSpecific){
                customersToBeGivenVouchers = customerControllerLocal.retrieveCustomerByMonth(selectedMonth);
            }else{
                customersToBeGivenVouchers = customerControllerLocal.retrieveCustomer();
            }
            
            for(Customer c : customersToBeGivenVouchers){
                System.out.println("Customer Voucher: " + c.getFirstName() + " " + c.getLastName());
                cv.setCustomer(c);
                cv.setVoucher(newVoucher);
                for(int i=0;i<newVoucher.getQuantity();i++){
                      customerVoucherControllerLocal.createNewCustomerVoucher(cv);
                }
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New voucher created successfully (Voucher ID: " + newVoucher.getId() + ")", null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new voucher: " + ex.getMessage(), null));
        }
    }
    public void populateVoucherDetail(){
        System.out.println("Populate voucher details: " + selectedVoucher.getId());
        List<CustomerVoucher> cvList = customerVoucherControllerLocal.retrieveCustomerVouchersByVoucherId(selectedVoucher.getId());
        totalVoucherAmount = cvList.size();
        currentVoucherAmount = customerVoucherControllerLocal.retrieveUnusedCustomerVouchersByVoucherId(selectedVoucher.getId()).size();
        System.out.println(cvList.size() + " " + customerVoucherControllerLocal.retrieveUnusedCustomerVouchersByVoucherId(selectedVoucher.getId()).size());
        Calendar dateCreated = Calendar.getInstance();
        dateCreated.setTime(selectedVoucher.getDateCreated());
        Calendar dateExpired = Calendar.getInstance();
        dateExpired.setTime(selectedVoucher.getDateExpired());
        
        
        Calendar dateCreatedDay = Calendar.getInstance();
        dateCreatedDay.set(Calendar.YEAR, dateCreated.get(Calendar.YEAR));
        dateCreatedDay.set(Calendar.MONTH, dateCreated.get(Calendar.MONTH));
        dateCreatedDay.set(Calendar.DAY_OF_MONTH, dateCreated.get(Calendar.DAY_OF_MONTH));
        
        Calendar dateExpiredDay = Calendar.getInstance();
        dateExpiredDay.set(Calendar.YEAR, dateExpired.get(Calendar.YEAR));
        dateExpiredDay.set(Calendar.MONTH, dateExpired.get(Calendar.MONTH));
        dateExpiredDay.set(Calendar.DAY_OF_MONTH, dateExpired.get(Calendar.DAY_OF_MONTH));
        
        
        duration =  (int)TimeUnit.MILLISECONDS.toDays(Math.abs(dateExpiredDay.getTimeInMillis() - dateCreatedDay.getTimeInMillis()));
        usesPerDay = (totalVoucherAmount - currentVoucherAmount)/duration;
    }
    
    
    public List<Voucher> getVouchers() {
        return vouchers;
    }

    public void setVouchers(List<Voucher> vouchers) {
        this.vouchers = vouchers;
    }

    public Voucher getNewVoucher() {
        return newVoucher;
    }

    public void setNewVoucher(Voucher newVoucher) {
        this.newVoucher = newVoucher;
    }

    public Voucher getSelectedVoucher() {
        return selectedVoucher;
    }

    public void setSelectedVoucher(Voucher selectedVoucher) {
        this.selectedVoucher = selectedVoucher;
    }

    public List<Voucher> getFilteredVouchers() {
        return filteredVouchers;
    }

    public void setFilteredVouchers(List<Voucher> filteredVouchers) {
        this.filteredVouchers = filteredVouchers;
    }

    public VoucherControllerLocal getVoucherControllerLocal() {
        return voucherControllerLocal;
    }

    public void setVoucherControllerLocal(VoucherControllerLocal voucherControllerLocal) {
        this.voucherControllerLocal = voucherControllerLocal;
    }

    public boolean getIsSpecific() {
        return isSpecific;
    }

    public void setIsSpecific(boolean isSpecific) {
        this.isSpecific = isSpecific;
    }

    public Map<String, Integer> getMonthList() {
        return monthList;
    }

    public void setMonthList(Map<String, Integer> monthList) {
        this.monthList = monthList;
    }

    public int getSelectedMonth() {
        return selectedMonth;
    }

    public void setSelectedMonth(int selectedMonth) {
        this.selectedMonth = selectedMonth;
    }

    public int getTotalVoucherAmount() {
        return totalVoucherAmount;
    }

    public void setTotalVoucherAmount(int totalVoucherAmount) {
        this.totalVoucherAmount = totalVoucherAmount;
    }

    public int getCurrentVoucherAmount() {
        return currentVoucherAmount;
    }

    public void setCurrentVoucherAmount(int currentVoucherAmount) {
        this.currentVoucherAmount = currentVoucherAmount;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getUsesPerDay() {
        return usesPerDay;
    }

    public void setUsesPerDay(double usesPerDay) {
        this.usesPerDay = usesPerDay;
    }
    
    
    
}

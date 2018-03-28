/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.staff.managedbean;

import ejb.session.stateless.VoucherControllerLocal;
import entity.Voucher;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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

    private List<Voucher> vouchers;
    private Voucher newVoucher;
    private Voucher selectedVoucher;
    private List<Voucher> filteredVouchers;
     
    public StaffVoucherManagedBean() {
        vouchers = new ArrayList<Voucher>();
        newVoucher = new Voucher();
        filteredVouchers = new ArrayList<Voucher>();
    }
    
    @PostConstruct
    public void postConstruct(){
        vouchers = voucherControllerLocal.retrieveVouchers();
        newVoucher = new Voucher();
        filteredVouchers = vouchers;
        
    }

     public void createNewVoucher(ActionEvent event){
        try
        {
            

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New voucher created successfully (Voucher ID: " + newVoucher.getId() + ")", null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new voucher: " + ex.getMessage(), null));
        }
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
    
    
    
}

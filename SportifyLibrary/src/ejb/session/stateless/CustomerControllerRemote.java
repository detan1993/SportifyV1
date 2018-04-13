/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import java.util.List;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerSignUpException;
import util.exception.InvalidLoginCredentialException;

public interface CustomerControllerRemote {
    public Customer createNewCustomer(Customer newCustomer) throws CustomerSignUpException;
    public List<Customer> retrieveCustomer();
    public Customer login(String email, String password) throws InvalidLoginCredentialException;
    public Customer retrieveCustomer(String email) throws CustomerNotFoundException;
    public Customer retrieveCustomer(long id);
    List<Customer> retrieveCustomerByMonth(int month);
    public boolean checkEmailAlreadyExist(String email);
    public Customer updateCustomer(Customer c);
}

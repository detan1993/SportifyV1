/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Staff;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;

public interface StaffControllerLocal {
    public Staff login(String email, String password) throws InvalidLoginCredentialException;
    public Staff createStaff(Staff newStaff);
    public Staff retrieveStaffByEmail(String email) throws StaffNotFoundException;
}

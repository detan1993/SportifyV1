/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CustomerOrder;
import entity.Product;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.helperClass.TopTenCustomer;

/**
 *
 * @author shanw
 */
@Stateless
public class CustomerOrderController implements CustomerOrderControllerRemote, CustomerOrderControllerLocal {

    @PersistenceContext(unitName = "Sportify-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public CustomerOrder CreateNewCustomerOrder(CustomerOrder newCustomerOrder) {

        em.persist(newCustomerOrder);
        em.flush();
        em.refresh(newCustomerOrder);
        return newCustomerOrder;
    }

    @Override
    public List<CustomerOrder> RetrieveAllCustomerOrder() {
        Query query = em.createQuery("SELECT p FROM CustomerOrder p");
        return query.getResultList();
    }

    @Override
    public List<TopTenCustomer> RetrieveTopTenCustomersByOrder() {

        List<TopTenCustomer> custInformation = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#.##");
        try {

            //SELECT  SUM(o.`TOTALAMOUNT`) AS "TOTAL BY MONTH" FROM customerorder o GROUP BY SUBSTRING(o.`DATEPAID` , 6,2)  order by  o.`DATEPAID`;
            Query selectAgregatedCustomerRecord = em.createQuery("SELECT  p.customer.email , SUM(p.totalAmount) , COUNT(p.id) , AVG(p.totalAmount) FROM CustomerOrder p GROUP BY p.customer.email ORDER BY  SUM(p.totalAmount) DESC");

            List<Object[]> sumRecord = selectAgregatedCustomerRecord.getResultList();
            for (int topCustomer = 0; topCustomer < sumRecord.size() && topCustomer < 10; topCustomer++) { //select top 10

                String email = sumRecord.get(topCustomer)[0].toString();
                Double totalPurchase = Double.parseDouble(df.format(sumRecord.get(topCustomer)[1]));
                int totalQtyPurchase = Integer.parseInt(sumRecord.get(topCustomer)[2].toString());
                Double avgPurchase = Double.parseDouble(df.format(sumRecord.get(topCustomer)[3]));

                TopTenCustomer customer = new TopTenCustomer(topCustomer + 1, email, totalPurchase, totalQtyPurchase, avgPurchase);
                custInformation.add(customer);

                System.out.println("Email is" + email + "Total is " + totalPurchase + " Total Quantity is" + totalQtyPurchase + " AVG is " + avgPurchase);

            }

            System.out.println("************ size of cust is? " + custInformation.size());
            getNonAgreegatedValue(custInformation);
            /*    for (int otherInformation = 0;  otherInformation<custInformation.size(); otherInformation++) {
                
                System.out.println("************** Populate customer products purchase data");
                Query customerProductInformation = em.createQuery("SELECT p FROM CustomerOrder p WHERE p.customer.email = :cEmail");
                customerProductInformation.setParameter("cEmail",custInformation.get(otherInformation).getEmail());
                 
                List<CustomerOrder> co = customerProductInformation.getResultList();
                
                for(int k=0; k<co.size(); k++){
                    
                    HashMap<Integer, String> idCountryPair = new HashMap<>();
                    List<Product> products = co.get(k).getProducts();
                    for(int product =0; product<products.size(); product++)
                    {
                        
                    }
                    
                     Query q = em.createQuery("SELECT p FROM Product p WHERE p. = :cEmail");
                }
                 
                 
                System.out.println("************** data is " +  (otherRecord.get(0)[0].toString()) + " " + (otherRecord.get(0)[1].toString()));
                 custInformation.get(otherInformation).setFullName((otherRecord.get(0)[0].toString()) + " " + (otherRecord.get(0)[1].toString()));
                 custInformation.get(otherInformation).setLoyaltyPoint(Integer.parseInt(otherRecord.get(0)[2].toString()));
                 custInformation.get(otherInformation).setAccountCreated(otherRecord.get(0)[3].toString());
            }*/

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return custInformation;

    }

    @Override
    public List<TopTenCustomer> RetrieveTopTenCustomerByOrderByRanger(String dateFron, String dateTo) {
        List<TopTenCustomer> custInformation = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#.##");
        try {

            //SELECT  SUM(o.`TOTALAMOUNT`) AS "TOTAL BY MONTH" FROM customerorder o GROUP BY SUBSTRING(o.`DATEPAID` , 6,2)  order by  o.`DATEPAID`;
            Query selectAgregatedByFilter = em.createQuery("SELECT  p.customer.email , SUM(p.totalAmount) , COUNT(p.id) , AVG(p.totalAmount) FROM CustomerOrder p WHERE SUBSTRING(p.datePaid,1,10) BETWEEN :start AND :end  GROUP BY p.customer.email ORDER BY  SUM(p.totalAmount) DESC");
            selectAgregatedByFilter.setParameter("start", dateFron);
            selectAgregatedByFilter.setParameter("end", dateTo);
            
        //    SELECT SUM(p.totalAmount) , SUBSTRING(p.`DATEPAID` , 6,2) FROM CustomerOrder p WHERE SUBSTRING(p.`DATEPAID` , 1,10) BETWEEN  '2018-01-26' AND '2018-03-26'  GROUP BY SUBSTRING(p.`DATEPAID` , 6,2) ORDER BY  SUM(p.totalAmount) DESC
            List<Object[]> sumRecord = selectAgregatedByFilter.getResultList();
            for (int topCustomer = 0; topCustomer < sumRecord.size() && topCustomer < 10; topCustomer++) { //select top 10

                String email = sumRecord.get(topCustomer)[0].toString();
                Double totalPurchase = Double.parseDouble(df.format(sumRecord.get(topCustomer)[1]));
                int totalQtyPurchase = Integer.parseInt(sumRecord.get(topCustomer)[2].toString());
                Double avgPurchase = Double.parseDouble(df.format(sumRecord.get(topCustomer)[3]));

                TopTenCustomer customer = new TopTenCustomer(topCustomer + 1, email, totalPurchase, totalQtyPurchase, avgPurchase);
                custInformation.add(customer);

                System.out.println("Email is" + email + "Total is " + totalPurchase + " Total Quantity is" + totalQtyPurchase + " AVG is " + avgPurchase);

            }
            
             getNonAgreegatedValue(custInformation);
            
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        
        return custInformation;

    }

    private void getNonAgreegatedValue(List<TopTenCustomer> custInformation) {

        for (int otherInformation = 0; otherInformation < custInformation.size(); otherInformation++) {

            System.out.println("************** Populate non agregate data");
            Query nonAggregatedInformation = em.createQuery("SELECT c.firstName , c.lastName , c.loyaltyPoints , c.dateOfBirth FROM Customer c WHERE c.email = :cEmail");
            nonAggregatedInformation.setParameter("cEmail", custInformation.get(otherInformation).getEmail());
            List<Object[]> otherRecord = nonAggregatedInformation.getResultList();

            System.out.println("************** data is " + (otherRecord.get(0)[0].toString()) + " " + (otherRecord.get(0)[1].toString()));
            custInformation.get(otherInformation).setFullName((otherRecord.get(0)[0].toString()) + " " + (otherRecord.get(0)[1].toString()));
            custInformation.get(otherInformation).setLoyaltyPoint(Integer.parseInt(otherRecord.get(0)[2].toString()));
            custInformation.get(otherInformation).setAccountCreated(otherRecord.get(0)[3].toString());
        }

    }
}

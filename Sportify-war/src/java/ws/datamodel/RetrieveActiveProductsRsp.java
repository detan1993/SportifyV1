/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.Product;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author shanw
 */

@XmlRootElement
@XmlType(name = "retrieveCustomerAccountRsp", propOrder = {
    "productList"
})

public class RetrieveActiveProductsRsp {
    private List<Product> productList;

    public RetrieveActiveProductsRsp() {
    }

    public RetrieveActiveProductsRsp(List<Product> productList) {
        this.productList = productList;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
    
    
}

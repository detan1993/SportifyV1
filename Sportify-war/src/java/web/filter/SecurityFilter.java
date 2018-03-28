/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.filter;

import entity.Staff;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author David
 */


@WebFilter(filterName = "SecurityFilter", urlPatterns = {"/*"})
public class SecurityFilter implements Filter {
    
    FilterConfig filterConfig;
    
    private static final String CONTEXT_ROOT = "/Sportify-war";
    
   

    public void init(FilterConfig filterConfig) throws ServletException
    {
        this.filterConfig = filterConfig;
    }
    
      public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        HttpSession httpSession = httpServletRequest.getSession(true);
        String requestServletPath = httpServletRequest.getServletPath();
        String userType = "";
        Boolean staffIsLogin = false;
        Boolean customerIsLogin = false;
  
        System.out.println("********** DO FILTER ");
        
        
        if(requestServletPath.substring(1,6).equals("staff"))
        {
            userType = "staff";
            if(httpSession.getAttribute("staffIsLogin") == null)
             {
                 httpSession.setAttribute("staffIsLogin", false);
             }

            staffIsLogin= (Boolean)httpSession.getAttribute("staffIsLogin");         
        }
        else //not staff
        {
            userType = "customer";
            if(httpSession.getAttribute("customerIsLogin") == null)
             {
                 httpSession.setAttribute("customerIsLogin", false);
             }

            customerIsLogin= (Boolean)httpSession.getAttribute("customerIsLogin");     
        }
   
        
        if(userType.equals("staff"))
        {
            //check staff exclude login 
            System.out.println("********** DO FILTER - CHECK STAFF : staffIsLogin" + staffIsLogin);
            System.out.println("********** DO FILTER - CHECK STAFF : requestServletPath" + requestServletPath);
            
            //special case
              if(requestServletPath.equals("/staffLogin.xhtml") && staffIsLogin)
                  httpServletResponse.sendRedirect(CONTEXT_ROOT + "/staffHome.xhtml");
            
              if(!excludeLoginCheckStaff(requestServletPath))
              {
                  if(staffIsLogin == true)
                  {
                      Staff currentStaffEntity = (Staff)httpSession.getAttribute("currentStaffEntity");
                
                      if(checkAccessRight(requestServletPath, currentStaffEntity.getStaffRole()))
                      {
                          chain.doFilter(request, response);
                      }
                      else
                      {
                          httpServletResponse.sendRedirect(CONTEXT_ROOT + "/staffError.xhtml");
                      }
                  }
                  else
                  {
                      //System.out.println("Navigating to staff Login." + (Boolean)httpSession.getAttribute("staffIsLogin") + "");
                      httpServletResponse.sendRedirect(CONTEXT_ROOT + "/staffLogin.xhtml");
                  }
              }
              else
              {
                  System.out.println("********** DO FILTER : Exclude filter ");
                  chain.doFilter(request, response);
              }
            
            
        }
        else if (userType.equals("customer"))
        {
            //checl customer exclude login
            if(!excludeLoginCheckCustomer(requestServletPath))
              {
                  if(customerIsLogin == true)
                  {
                   //   Staff currentStaffEntity = (Staff)httpSession.getAttribute("currentStaffEntity");
                
                      if(checkCustomerAccessRight(requestServletPath))
                      {
                          chain.doFilter(request, response);
                      }
                      else
                      {
                          httpServletResponse.sendRedirect(CONTEXT_ROOT + "/error.xhtml");
                      }
                  }
                  else
                  {
                      //System.out.println("Navigating to staff Login." + (Boolean)httpSession.getAttribute("staffIsLogin") + "");
                      httpServletResponse.sendRedirect(CONTEXT_ROOT + "/home.xhtml");
                  }
              }
              else
              {
                  chain.doFilter(request, response);
              }
        }
          
      
    }



    public void destroy()
    {

    }
  

    private Boolean excludeLoginCheckStaff(String path)
    {
        if(path.equals("/staffLogin.xhtml") || path.equals("/staffError.xhtml") || path.equals("/staffProduct.xhtml") || path.equals("/staffTest.xhtml")
                || path.startsWith("/javax.faces.resource/staffUploadedFiles") || path.startsWith("/staffUploadedFiles")
                )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

   public Boolean excludeLoginCheckCustomer(String path){
       
        if(path.equals("/home.xhtml") ||  path.equals("/error.xhtml") || path.equals("/products.xhtml") || path.equals("/detailedProduct.xhtml") ||
            path.startsWith("/images") ||
            path.startsWith("/javax.faces.resource"))
        {
            return true;
        }
        else
        {
            return false;
        }
   }


   /* private Boolean checkAccessRight(String path)
    {
        if(path.startsWith("/userPortal"))
        {
            return true;
        }
        else
        {
            String accessRight = path.replaceAll(".xhtml", "");
            accessRight = accessRight.substring(1);

            return true;
        }
    }*/
    
   
      private Boolean checkAccessRight(String path, String accessRight)
    {
        if(accessRight.equals("Manager"))
        {
            if(path.equals("/staffHome.xhtml") ||
                    path.equals("/staffDashboard.xhtml") ||  path.equals("/staffProduct.xhtml") ||
                    path.equals("/staffVoucher.xhtml"))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if(accessRight.equals("Sales"))
        {
            if(path.equals("/staffHome.xhtml") ||
                    path.equals("/staffProduct.xhtml") ||
                    path.equals("/staffVoucher.xhtml"))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        
        return false;
    }


    private Boolean checkCustomerAccessRight(String path)
    {
        
        if(path.equals("/home.xhtml"))
        {
            return true;
        }
        else
        {
            return false;
        }
        
    }
    
    
}

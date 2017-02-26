/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.action;

import com.opensymphony.xwork2.ActionSupport;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.interceptor.ServletRequestAware;

/**
 *
 * @author Administrator
 */
public class HomeAction extends ActionSupport  implements  
        ServletRequestAware{
     private HttpServletRequest servletRequest; 
     private int totalreceived; 
     private int totalSent;
     
    public HomeAction() {
    }
    
    public String execute() throws Exception {
     String email=(String)  servletRequest.getSession().getAttribute("loggedInUser");
     totalreceived=Tools.Query.getCountReceivedfile(email);
     totalSent=Tools.Query.getCountTransferredfile(email);
     
     return SUCCESS;
    }
     @Override
   public void setServletRequest(HttpServletRequest servletRequest) {  
        this.servletRequest = servletRequest;  
  
    }  

    public int getTotalreceived() {
        return totalreceived;
    }

    public void setTotalreceived(int totalreceived) {
        this.totalreceived = totalreceived;
    }

    public int getTotalSent() {
        return totalSent;
    }

    public void setTotalSent(int totalSent) {
        this.totalSent = totalSent;
    }

}

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
public class ChangePasswordAction extends ActionSupport implements  
        ServletRequestAware{
     private HttpServletRequest servletRequest; 
    private String oldPass;
    private  String newPass;
    
    public ChangePasswordAction() {
    }
    
    public String execute() throws Exception {
         String email=(String)  servletRequest.getSession().getAttribute("loggedInUser");
         int i=Tools.Query.changePassword(email, oldPass, newPass);
        if(i>0)
        {
         servletRequest.getSession().setAttribute("successMsg", "Password has been successfully updated.");
               return SUCCESS;   
        }
        else
        {
            servletRequest.getSession().setAttribute("errMsg", "Old password is not correct ! Please Try Again.");
               return INPUT;
        }
    }
        @Override
   public void setServletRequest(HttpServletRequest servletRequest) {  
        this.servletRequest = servletRequest;  
  
    }  

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.action;

import Model.StudentBean;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;

/**
 *
 * @author Administrator
 */
public class SignUpAction extends ActionSupport implements SessionAware{
    private StudentBean sb=null;
    
    public SessionMap<String, Object> getSessionMap() {
        return sessionMap;
    }

    public void setSessionMap(SessionMap<String, Object> sessionMap) {
        this.sessionMap = sessionMap;
    }
     private SessionMap<String,Object> sessionMap;  
     public void setSession(Map<String, Object> map) {  
    sessionMap=(SessionMap)map;  
} 

    public SignUpAction() {
    }
    
    public String execute() throws Exception {
        int count=Tools.Query.getCountEmail(sb.getEmail());
        if(count>0)
          {
         addActionError("This Email ID is already registered ");
         return INPUT;
          }
        int j=Tools.Query.signUpStudent(sb);
       if(j>0){
            StudentBean sbean=Tools.Query.getStudentInfo(sb.getEmail());
            String name_display=sbean.getFullName();
             sessionMap.put("loggedInUser",sb.getEmail());
             sessionMap.put("name",name_display.toUpperCase());
             Map sess=ActionContext.getContext().getSession();
            sess.put("login", true);
       
         
     return SUCCESS;
     }
        addActionError("Something Goes Wrong ! Plese Try Again");
       return INPUT;
    }
    
      

    public StudentBean getSb() {
        return sb;
    }

    public void setSb(StudentBean sb) {
        this.sb = sb;
    }
    }
    


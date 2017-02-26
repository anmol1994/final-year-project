package com.action;

import Model.StudentBean;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;
public class MyAction extends ActionSupport implements SessionAware
{
    private String username;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    private String errorMsg;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

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

    public String execute(){
       
        int i=Tools.Query.validateUser(username, password);
        if(i>0){
            StudentBean sb=Tools.Query.getStudentInfo(username);
            String name_display=sb.getFullName();
             sessionMap.put("loggedInUser",username);
             sessionMap.put("name",name_display.toUpperCase());
           // ServletActionContext.getRequest().getSession().setAttribute("loggedInUser", username);
            Map sess=ActionContext.getContext().getSession();
            sess.put("login", true);
            return SUCCESS;
             }
        
        errorMsg="Invalid username/password";
        return INPUT;
    }

    public String logout(){
        addActionMessage("You are successfully logged out!");
         ServletActionContext.getRequest().getSession().removeAttribute("loggedInUser"); 
          ServletActionContext.getRequest().getSession().removeAttribute("username");
        Map sess=ActionContext.getContext().getSession();
        sess.remove("login");
        return SUCCESS;
    }

}
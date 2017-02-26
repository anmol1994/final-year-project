/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.action;

import Model.FileTransferBean;
import Tools.CryptoUtils;
import Tools.QRCode;
import Tools.SendEmail;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;

/**
 *
 * @author Administrator
 */
public class TransferFilesAction extends ActionSupport implements  
        ServletRequestAware, SessionAware{
    
    
     private File userImage;  
    private String userImageContentType;  
    private String userImageFileName; 
     private HttpServletRequest servletRequest;  
     private FileTransferBean ftb;
     private List<FileTransferBean> sentFiles;
      private List<FileTransferBean> receivedFiles;
     
    
    public TransferFilesAction() {
        
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
    public String execute() throws Exception {
         try {  
             
             int checkUser=Tools.Query.getCountEmail(ftb.getToEmail());
             if(checkUser>0)
             {
        String filePath = servletRequest.getSession().getServletContext().getRealPath("/").concat("UserDocs");  
          if(ftb.getCode().trim().length()!=16)
          {
         sessionMap.put("exMessage", "Please Enter 16 digit security code . ");
         return INPUT;
          }
        System.out.println("Image Location:" + filePath);//see the server console for actual location  
        File fileToCreate = new File(filePath, this.userImageFileName);  
        String ext= FilenameUtils.getExtension(fileToCreate.getName());
        Random rand = new Random();
        int minimum=10000;
        int maximum=99999;
        String fromEmail=(String)sessionMap.get("loggedInUser");
        int randomNum = minimum + rand.nextInt((maximum - minimum) + 1);
        String encryptedFileName=randomNum+fromEmail+".encrypted";
        File encryptedFile = new File(filePath,encryptedFileName);      
        CryptoUtils.encrypt(ftb.getCode(), userImage, encryptedFile); 
         String qrFileName=encryptedFileName+"_QR.png";
         String qrFilePath=filePath+"/"+qrFileName;
         QRCode.generateBarCode(qrFilePath, ftb.getCode());         
            FileTransferBean ftBean=new FileTransferBean();
            ftBean.setDescription(ftb.getDescription());
            ftBean.setToEmail(ftb.getToEmail());
            ftBean.setCode(ftb.getCode());
            ftBean.setFromEmail(fromEmail);
            ftBean.setExtension(ext);
            ftBean.setFileNameActual(userImageFileName);
            ftBean.setFilePath(encryptedFileName);  
            
            int j=Tools.Query.addNewFile(ftBean);
            
            if(j>0)
            {
                SendEmail.sendMail(ftb.getToEmail(), qrFilePath,qrFileName);
            }
            sentFiles=Tools.Query.getMyTransferList(fromEmail);
            sessionMap.put("successMessage", "File Successfully Transfered.");
             }
             else
             {
                sessionMap.put("exMessage", "Invalid Sender ID.");
         return INPUT;  
             }
         }
         catch(Exception ex)
         {
             sessionMap.remove("successMessage");
             
              sessionMap.put("exMessage", ex.getMessage());
             System.out.println("IOException : "+ex.getMessage());
         }
        return SUCCESS;  
    }
    
    
    
    public String fileTransferList() throws Exception {
         
      
        String fromEmail=(String)sessionMap.get("loggedInUser");
        sentFiles=Tools.Query.getMyTransferList(fromEmail);

        
        return SUCCESS;  
    }
    
    public String fileReceivedList() throws Exception {
         
      
        String toEmail=(String)sessionMap.get("loggedInUser");
        receivedFiles=Tools.Query.getMyReceivedList(toEmail);

        
        return SUCCESS;  
    }

    public File getUserImage() {
        return userImage;
    }

    public void setUserImage(File userImage) {
        this.userImage = userImage;
    }

    public String getUserImageContentType() {
        return userImageContentType;
    }

    public void setUserImageContentType(String userImageContentType) {
        this.userImageContentType = userImageContentType;
    }

    public String getUserImageFileName() {
        return userImageFileName;
    }

    public void setUserImageFileName(String userImageFileName) {
        this.userImageFileName = userImageFileName;
    }

    @Override
   public void setServletRequest(HttpServletRequest servletRequest) {  
        this.servletRequest = servletRequest;  
  
    }  

    public FileTransferBean getFtb() {
        return ftb;
    }

    public void setFtb(FileTransferBean ftb) {
        this.ftb = ftb;
    }

    public List<FileTransferBean> getSentFiles() {
        return sentFiles;
    }

    public void setSentFiles(List<FileTransferBean> sentFiles) {
        this.sentFiles = sentFiles;
    }

    public List<FileTransferBean> getReceivedFiles() {
        return receivedFiles;
    }

    public void setReceivedFiles(List<FileTransferBean> receivedFiles) {
        this.receivedFiles = receivedFiles;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.action;

import Model.FileTransferBean;
import Tools.CryptoException;
import Tools.CryptoUtils;
import Tools.NewFileInputStream;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.interceptor.ServletRequestAware;

/**
 *
 * @author Administrator
 */
public class DownloadAction extends ActionSupport implements  
        ServletRequestAware{
     private String fileName;
     private int fileId;
     private String code;
     
      private HttpServletRequest servletRequest;  
     
     
 
    public DownloadAction() {
    }
    
   private InputStream fileInputStream;
	
	public InputStream getFileInputStream() {
		return fileInputStream;
	}

	public String execute() throws Exception {
           
           FileTransferBean ftb=Tools.Query.getFileDetil(fileId);
           
           if(code.equals(ftb.getCode()))
           {
            String filePath = servletRequest.getSession().getServletContext().getRealPath("/").concat("UserDocs");  
            File inputfile=new File(filePath, ftb.getFilePath());
            
          String outputFilename=fileId+"_"+ftb.getFileNameActual();
           File outputFile = new File(filePath,outputFilename);
            CryptoUtils.decrypt(code, inputfile, outputFile);
            fileName=outputFilename; 
	   fileInputStream =new NewFileInputStream(outputFile);
           if(fileInputStream==null)
           {
               servletRequest.getSession().setAttribute("inputErr", "Something Happen Wrong ! File cnnot be downloaded.");
               return INPUT;
           }
           
         
           
           return SUCCESS;
           }
           else
           {
               servletRequest.getSession().setAttribute("inputErr", "Security Code Mismatch Error ! Please Try Again.");
               return INPUT;
           }
	    
	}

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }
      @Override
   public void setServletRequest(HttpServletRequest servletRequest) {  
        this.servletRequest = servletRequest;  
  
    }  

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
   
   
}

package com.asb.snapshot.servlet;

import com.asb.snapshot.utils.FileManager;
import com.asb.snapshot.utils.Syslog;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: yanjieli
 * Date: 15-5-15
 * Time: 下午1:18
 * To change this template use File | Settings | File Templates.
 */
public class FileUploadServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //
        try{
            if(request.getContentLength()>1){
                InputStream in=request.getInputStream();
//                File f=new File("d:/temp",request.getParameter("filename"));
                File f=new File(FileManager.getInstance("").getSourceSnapshotPath(),request.getParameter("filename"));
                FileOutputStream o=new FileOutputStream(f);
                byte b[]=new byte[1024];
                int n;
                while((n=in.read(b))!=-1){
                    o.write(b,0,n);
                }
                o.close();
                in.close();
                Syslog.syslog("success:File upload success!");
            }
            else{
                Syslog.syslog("warning:No file found!");
            }
        }
        catch(IOException e){
            Syslog.syslog("error:file upload error!");
        }
        response.sendRedirect(request.getContextPath()+"/index.jsp?uploaded=true");
    }
}

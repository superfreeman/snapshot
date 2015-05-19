package com.asb.snapshot.servlet;

import com.asb.snapshot.bean.SnapshotConfig;
import com.asb.snapshot.utils.FileManager;
import com.asb.snapshot.utils.SpringContextUtils;
import com.asb.snapshot.utils.Syslog;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: yanjieli
 * Date: 15-5-12
 * Time: 下午1:24
 * 描述：该文件主要用于创建页面显示树的xml格式文件
 * description:create xml file for tree
 */
public class CreatDataServlet extends HttpServlet {
    SnapshotConfig config = (SnapshotConfig) SpringContextUtils.getBean("config");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("/index.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("/index.html");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //super.service(req, resp);    //To change body of overridden methods use File | Settings | File Templates.
        if(createDataXml(request,response)){
            response.sendRedirect(request.getContextPath()+"/index.jsp");
        }else{
            response.sendRedirect(request.getContextPath()+"/error.html");
        }

    }

    public boolean createDataXml(HttpServletRequest request, HttpServletResponse response){

        FileManager filemanager = FileManager.getInstance(config.getBaseDir());
        filemanager.createDir(request.getRealPath("/data"));
        File fout = new File(request.getRealPath("/data/data.xml"));

        File file[] = filemanager.readFile(FileManager.getInstance(config.getBaseDir()).getXmlFilePath());
        BufferedWriter bw =null;
//		Map<String, Map<String, List>> map = new HashMap<String, Map<String, List>>();
        try {
            bw = new BufferedWriter(new FileWriter(fout));
            bw.write("<?xml version='1.0' encoding='iso-8859-1'?>");
            bw.newLine();
            bw.write("<tree id=\"0\">");
            bw.newLine();
            if(file!=null) {
                for (File f : file) {
                    bw.write("  <item text=\"" + f.getName() + "\" id=\"" + f.getName() + "\" open=\"1\" im0=\"tombs.gif\" im1=\"tombs.gif\" im2=\"iconSafe.gif\" call=\"1\" select=\"1\">");
                    bw.newLine();
                    File[] fileversion = filemanager.readFile(FileManager.getInstance(config.getBaseDir()).getXmlFilePath() + "/" + f.getName());
                    for (File ff : fileversion) {
                        bw.write("    <item text=\"" + ff.getName() + "\" id=\"" + f.getName() + ":" + ff.getName() + "\" im0=\"folderClosed.gif\" im1=\"folderOpen.gif\" im2=\"folderClosed.gif\">");
                        bw.newLine();
                        File[] fileEnb = filemanager.readFile(FileManager.getInstance(config.getBaseDir()).getXmlFilePath() + "/"
                                + f.getName() + "/" + ff.getName());
                        for (File fff : fileEnb) {
                            bw.write("        <item text=\"" + fff.getName() + "\" id=\"" + f.getName() + ":" + ff.getName() + ":" + fff.getName() + "\" im0=\"book_titel.gif\" im1=\"book_titel.gif\" im2=\"book_titel.gif\"/>");
                            bw.newLine();
                        }
                        bw.write("    </item>");
                        bw.newLine();
                        bw.flush();
                    }
                    bw.write("  </item>");
                    bw.newLine();
                    bw.flush();
                }
            }
            bw.write("</tree>");
            bw.flush();
            //Syslog.syslog("success: create data.xml over!");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally{
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;

    }
}

package com.asb.snapshot.servlet;

import com.asb.snapshot.ParseManager;
import com.asb.snapshot.ParseSnapshot;
import com.asb.snapshot.bean.SnapshotConfig;
import com.asb.snapshot.utils.FileManager;
import com.asb.snapshot.utils.SpringContextUtils;
import com.asb.snapshot.utils.Syslog;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.LocatorEx;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: yanjieli
 * Date: 15-5-13
 * Time: 下午2:36
 * To change this template use File | Settings | File Templates.
 */
public class StartServlet extends HttpServlet {
    private static String baseDir="";
    private static boolean parseFlag=false;
    @Override
    public void init() throws ServletException {

        SnapshotConfig snapshotConfig = (SnapshotConfig) SpringContextUtils.getBean("config");
        baseDir=snapshotConfig.getBaseDir();
        Syslog.syslog("info:the base directory is ==>"+baseDir);
        FileManager filemanager = FileManager.getInstance(snapshotConfig.getBaseDir());
        {
            filemanager.createDir(FileManager.getInstance(baseDir).getOutputSnapshotPath());
            filemanager.createDir(FileManager.getInstance(baseDir).getXmlFilePath());
            filemanager.createDir(FileManager.getInstance(baseDir).getLogFilePath());
            filemanager.createDir(FileManager.getInstance(baseDir).getSourceSnapshotPath());

        }

        parseFlag = snapshotConfig.isParseFlag();
        if (parseFlag) {
            Syslog.syslog("info:the parse snapshot file is starting ");
            new Thread(new ParseManager()).start();
        }

    }


    @Override
    public void destroy() {
        Syslog.syslog("success:exit the filemanager process! ");
        Thread.interrupted();
    }
}

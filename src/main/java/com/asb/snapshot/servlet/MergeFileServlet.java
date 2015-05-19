package com.asb.snapshot.servlet;

import com.asb.snapshot.utils.FileManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: yanjieli
 * Date: 15-5-13
 * Time: 下午5:32
 * To change this template use File | Settings | File Templates.
 */
public class MergeFileServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String target = request.getParameter("chuanchuan");
        Map map=parseReadStr(target,request);
        if (FileManager.getInstance("").mergeFile(map)){
            String  filename=(String)map.get("targetfile");
            String  path=(String)map.get("target");
//            response.setContentType(fileminitype);
            response.setHeader("Location",path);
//            response.setHeader("Cache-Control", "max-age=" + cacheTime);
            response.setHeader("Content-Disposition", "attachment; filename=" + filename); //filename应该是编码后的(utf-8)
//            response.setContentLength(filelength);
            OutputStream outputStream = response.getOutputStream();
            InputStream inputStream = new FileInputStream(path);
            byte[] buffer = new byte[1024];
            int i = -1;
            while ((i = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, i);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            outputStream = null;

            File f=new File(path);
            f.delete();
        }else{
            System.out.println("false!");
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }

    }

    /**
     *
     * @param readStr
     * @return
     */
    public static Map<String,String> parseReadStr(String readStr,HttpServletRequest request) {
        Pattern pattern = Pattern.compile("^<soap><source>(.*)</source><target>(.*)</target></soap>$");
        Matcher matcher = pattern.matcher(readStr);
        Map<String,String> map=new HashMap<String,String>();
        if (matcher.find()){
            map.put("source",matcher.group(1));
            map.put("target",request.getRealPath(matcher.group(2)));
            map.put("targetfile",(matcher.group(2)));
        }
        return map;
    }
}

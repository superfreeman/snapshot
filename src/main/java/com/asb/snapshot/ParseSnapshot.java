package com.asb.snapshot;

import com.asb.snapshot.bean.SnapshotConfig;
import com.asb.snapshot.utils.FileManager;
import com.asb.snapshot.utils.SpringContextUtils;
import com.asb.snapshot.utils.Syslog;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: yanjieli
 * Date: 15-5-13
 * Time: 下午2:24
 * To change this template use File | Settings | File Templates.
 */
public class ParseSnapshot implements Runnable{
    SnapshotConfig config = (SnapshotConfig) SpringContextUtils.getBean("config");

    private final static String END_ELEMENT="^\\s*</ENBEquipment>\\s*$";
    private final static String START_ELEMENT="^\\s*<ENBEquipment\\s*id\\S{2}\\W*\\w*\\S{1}\\s*\\w*\\S*\\s*\\W*\\w*\\S*\\s*\\w*\\S{2}\\w*\\W*\\S*>\\s*$";
    public final static String EQUIPID="id";
    public final static String VERSION="version";

    private String line=null;
    private StringBuffer sb=null;
    private String enbid=null;
    private String version=null;
    private String xmlpath=null;
    //    private InputStream inputStream=null;
    private String snapshotFilename="";
    private File file=null;

    /**
     * construct method
     * @param f
     * @param filename
     */

    public ParseSnapshot(File f,String filename){
        this.file=f;
        this.snapshotFilename=filename;
        xmlpath=FileManager.getInstance("").getXmlFilePath()+File.separator+this.snapshotFilename+"/";
        FileManager.getInstance(config.getBaseDir()).createDir(xmlpath);
    }

    /**
     * parse the snapshot and save to small piece xml files
     */

    public synchronized void parse() {
        if (!Thread.interrupted()) {
            try {
                Thread.sleep(1000);
//                Syslog.syslog("path:" + xmlpath);
                InputStream inputStream = new FileInputStream(this.file);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream));
                boolean fileFlag = false;
                while ((line = reader.readLine()) != null) {
                    if (line.matches(START_ELEMENT)) {
                        enbid = getAttribute(line, EQUIPID);
                        version = getAttribute(line, VERSION);
                        sb = new StringBuffer("");
                        fileFlag = true;
                    }
                    if (fileFlag) {
                        sb.append(line).append("\n");
                    }
                    if (line.matches(END_ELEMENT)) {
                        fileFlag = false;
                    }
                    if (!fileFlag && sb != null) {
                        FileOutputStream fosxml;
                        FileManager.getInstance(config.getBaseDir()).createDir(xmlpath + version);
                        File f = new File(xmlpath + version + "/" + enbid
                                + ".xml");
                        if (!f.exists()) {

                            fosxml = new FileOutputStream(f);
                            fosxml.write(sb.toString().getBytes());
                            fosxml.close();
                        } else {
                            Thread.sleep(100);
                        }
                        sb = null;
                    }
                }
                reader.close();
                inputStream.close();
                Thread.sleep(1000);

            } catch (FileNotFoundException e) {
                Syslog.syslog("error:cann't find the resource file,"
                        + e.getMessage());
//				FileManager.snapshot_fileList.remove(this.snapshotFilename);
//				break;
            } catch (IOException e) {
                Syslog.syslog("error:" + e.getMessage());
//				FileManager.snapshot_fileList.remove(this.snapshotFilename);
//				break;

            } catch (InterruptedException e) {
                Syslog.syslog("over:" + e.getMessage());
//				FileManager.snapshot_fileList.remove(this.snapshotFilename);
//				break;
            }
        }
        FileManager.snapshot_fileList.remove(this.snapshotFilename);
    }

    /**
     * query the attribute from a string
     * for example:<ENBEquipment id="WA000000" model="ENB" version="LR_14_03_L">
     * we can find the value of id,model,version by the method
     * getAttribute("<ENBEquipment id="WA000000" model="ENB" version="LR_14_03_L">","id")
     * getAttribute("<ENBEquipment id="WA000000" model="ENB" version="LR_14_03_L">","model")
     * getAttribute("<ENBEquipment id="WA000000" model="ENB" version="LR_14_03_L">","version")
     * @param lineElement
     * @param attribute
     * @return
     */

    public static String getAttribute(String lineElement,String attribute){
        Pattern pattern = Pattern.compile(attribute + "=\\S{1}(\\w*)\\S{1}");
        Matcher matcher = pattern.matcher(lineElement);
        if(matcher.find())
            return matcher.group(1);
        else
            return null;
    }

    public void run() {
        parse();
    }

}
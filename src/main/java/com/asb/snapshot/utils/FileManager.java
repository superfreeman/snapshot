package com.asb.snapshot.utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: yanjieli
 * Date: 15-5-12
 * Time: 下午1:52
 * To change this template use File | Settings | File Templates.
 */
public class FileManager {

    private String sourceSnapshotPath = this.baseDir + "/snapshot";
    private String xmlFilePath = this.baseDir + "/xml";
    private String outputSnapshotPath = this.baseDir + "/target";
    private String logFilePath="/log";


    public static final int BUFSIZE = 1024 * 8;
    public  final static String realPath = System.getProperty("user.dir");
    public static List<String> snapshot_fileList=new ArrayList<String>();
    private String baseDir;

    private FileManager(String baseDir) {
        if (baseDir == null || "".equals(baseDir)) {
            this.baseDir = "./";
        } else {
            this.setBaseDir(baseDir);
            createDir(this.getBaseDir());
        }
        sourceSnapshotPath = this.baseDir + "/snapshot";
        xmlFilePath = this.baseDir + "/xml";
        outputSnapshotPath = this.baseDir + "/target";
        logFilePath = this.baseDir + "/log";
    }

    private static FileManager instance;

    public static FileManager getInstance(String baseDir) {
        if(instance==null) {
            instance = new FileManager(baseDir);
            return instance;
        }else {
            return instance;
        }
    }

    /**
     * if file is not exist ,just create it
     *
     * @param filename
     * @return
     */
    public boolean createFile(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            return true;
        } else {
            try {
                return file.createNewFile();
            } catch (IOException e) {
                return false;
            }
        }
    }

    /**
     * if dirctory is not exist,just create it!
     *
     * @param pathname
     * @return
     */
    public boolean createDir(String pathname) {
        File dir = new File(pathname);
        if (dir.exists()) {
            return true;
        }
        String path[] = pathname.split("/");
        StringBuffer sb = new StringBuffer("");
        try {
            for (String p : path) {
                sb.append(p).append(File.separator);
                new File(sb.toString()).mkdir();
            }
        } catch (Exception e) {
            return false;
        }
        Syslog.syslog("success:system has create the dirctory==>"+pathname);
        return true;
    }

    /**
     * read the files in the dirctory
     *
     * @param soureDir
     * @return
     */
    public File[] readFile(String soureDir) {
        File file = new File(soureDir);
        File f[] = file.listFiles();
        return f;
    }

    public boolean writeFile(String line, File file) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(line);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * merge the source files to one target file
     * @param outFile
     * @param files
     */
    public static boolean mergeFiles(String outFile, String[] files) {
        FileChannel outChannel = null;
        Syslog.syslog("begin:Merge " + Arrays.toString(files) + " into " + outFile);
        try {
            //outFile=System.getProperty("user.dir")+outFile.replaceAll("./","/");
            outChannel = new FileOutputStream(outFile).getChannel();
            // begin to load snapshot
            outChannel.write(ByteBuffer.wrap("<?xml version='1.0' encoding='UTF-8'?>\n<snapshot>\n".getBytes()));
            for (String f : files) {
//                f=System.getProperty("user.dir")+f.replaceAll("./","/");
                @SuppressWarnings({ "resource"})
                FileChannel fc = new FileInputStream(f).getChannel();
                ByteBuffer bb = ByteBuffer.allocate(BUFSIZE);
                while (fc.read(bb) != -1) {
                    bb.flip();
                    outChannel.write(bb);
                    bb.clear();
                }
                fc.close();
            }
            outChannel.write(ByteBuffer.wrap("</snapshot>\n".getBytes()));
            Syslog.syslog("success:Merge " + Arrays.toString(files) + " into " + outFile+" over!");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        } finally {
            try {
                if (outChannel != null) {
                    outChannel.close();
                }
            } catch (IOException ignore) {
                return false;
            }
        }
        return true;
    }

    public boolean mergeFile(Map map) {
        if (map == null) {
            return false;
        }
        try {
            String source = (String) (map.get("source") == null ? "" : map
                    .get("source"));
            String target = (String) (map.get("target") == null ? "" : map
                    .get("target"));
//            target = this.getOutputSnapshotPath() + File.separator + target;
            String[] sources = source.split(",");
            List<String> sourcelst = new ArrayList<String>();
            for (String str : sources) {
                if (str.matches(".*xml$")) {
                    str = xmlFilePath + File.separator
                            + str.replace(":", File.separator);
                    sourcelst.add(str);
                }
            }
            if(sourcelst.size()<1)return false;
            String sourcefiles[]=new String[sourcelst.size()];
            sourcelst.toArray(sourcefiles);
            return mergeFiles(target,sourcefiles);
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
//		String files[] = { "D:\\test\\maventest\\utils\\xml\\WA000000.xml",
//				"D:\\test\\maventest\\utils\\xml\\WA000001.xml" };
//		String outputfile = "D:\\test\\maventest\\utils\\xml\\snapshot.xml";
//		mergeFiles(outputfile, files);
        String str="./xml/snapshot-lteran-2024-05-26-10-56-08-1/LR_13_03/SimuENB200.xml";
        str=System.getProperty("user.dir")+str.replace("./", "/");
        System.out.println(str);


        // System.out.println(FileManager.CONTEXTPATH);
    }


    public String getSourceSnapshotPath() {
        return sourceSnapshotPath;
    }

    public void setSourceSnapshotPath(String sourceSnapshotPath) {
        this.sourceSnapshotPath = sourceSnapshotPath;
    }

    public String getXmlFilePath() {
        return xmlFilePath;
    }

    public void setXmlFilePath(String xmlFilePath) {
        this.xmlFilePath = xmlFilePath;
    }

    public String getOutputSnapshotPath() {
        return outputSnapshotPath;
    }

    public void setOutputSnapshotPath(String outputSnapshotPath) {
        this.outputSnapshotPath = outputSnapshotPath;
    }

    public String getLogFilePath() {
        return logFilePath;
    }

    public void setLogFilePath(String logFilePath) {
        this.logFilePath = logFilePath;
    }


    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir=baseDir;
        //To change body of created methods use File | Settings | File Templates.
    }


}

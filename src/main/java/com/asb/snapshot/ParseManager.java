package com.asb.snapshot;

import com.asb.snapshot.utils.FileManager;
import com.asb.snapshot.utils.Syslog;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: yanjieli
 * Date: 15-5-13
 * Time: 下午4:25
 * To change this template use File | Settings | File Templates.
 */
public class ParseManager implements Runnable {

    @Override
    public void run() {
        //To change body of implemented methods use File | Settings | File Templates.
        while (!Thread.interrupted()) {

            FileManager.getInstance("");
            File file[] = FileManager.getInstance("").readFile(FileManager.getInstance("").getSourceSnapshotPath());
            if (file == null ||file.length==0) {
                Syslog.syslog("warning: there is no snapshot file in the snapshot dirctory,parse snapshot file will delay for 10 seconds!");
                try {
                    for (int i = 0; i < 10; i++) {
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
                //System.exit(-1);
            }
            for (File f : file) {
                String filename = f.getName();
                if (filename.endsWith(".xcm")) {
                    filename = filename.replaceAll(".xcm$", "");

                    if (!FileManager.snapshot_fileList.contains(filename)) {
                        Thread t = null;
                        FileManager.snapshot_fileList.add(filename);
                        t = new Thread(new ParseSnapshot(f, filename),
                                f.getName());
                        t.start();
                    }
                }
            }
            //if the files has been parsed ,sleep 10 seconds and go on to parse it
            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

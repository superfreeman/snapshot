package com.asb.snapshot.bean;

/**
 * Created with IntelliJ IDEA.
 * User: yanjieli
 * Date: 15-5-13
 * Time: 下午2:01
 * To change this template use File | Settings | File Templates.
 */
public class SnapshotConfig {
    private String baseDir;
    private boolean parseFlag;

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public boolean isParseFlag() {
        return parseFlag;
    }

    public void setParseFlag(boolean parseFlag) {
        this.parseFlag = parseFlag;
    }
}

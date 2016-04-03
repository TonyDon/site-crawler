/*
 * @(#)InfoRecord.java 2016年4月3日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.app.sitecrawler.dto;

import java.io.File;
import java.util.List;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2016年4月3日
 * </pre>
 */
public class InfoRecord {
    
    private String recordMd5Value;
    
    private String srcUrl;
    
    private String title ;
    
    private String summary;
    
    private String content;
    
    private List<String> imgs;
    
    private File tempFile;
    
    private List<String> remoteImgUrls;
    
    private boolean existError = false;
    
    private int tryTimes = 0;

    
    public String getSrcUrl() {
        return srcUrl;
    }

    

    public void setSrcUrl(String srcUrl) {
        this.srcUrl = srcUrl;
    }

    
    public String getTitle() {
        return title;
    }

    
    public void setTitle(String title) {
        this.title = title;
    }

    
    public String getSummary() {
        return summary;
    }

    
    public void setSummary(String summary) {
        this.summary = summary;
    }

    
    public String getContent() {
        return content;
    }

    
    public void setContent(String content) {
        this.content = content;
    }

    
    public List<String> getImgs() {
        return imgs;
    }

    
    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }


    
    public String getRecordMd5Value() {
        return recordMd5Value;
    }


    
    public void setRecordMd5Value(String recordMd5Value) {
        this.recordMd5Value = recordMd5Value;
    }

    
    public File getTempFile() {
        return tempFile;
    }



    
    public void setTempFile(File tempFile) {
        this.tempFile = tempFile;
    }



    
    public List<String> getRemoteImgUrls() {
        return remoteImgUrls;
    }



    
    public void setRemoteImgUrls(List<String> remoteImgUrls) {
        this.remoteImgUrls = remoteImgUrls;
    }



    
    public boolean isExistError() {
        return existError;
    }



    
    public void setExistError(boolean existError) {
        this.existError = existError;
    }



    
    public int getTryTimes() {
        return tryTimes;
    }



    
    public void increaseTryTimes() {
        this.tryTimes++;
    }



    @Override
    public String toString() {
        return "InfoRecord [recordMd5Value=" + recordMd5Value + ", srcUrl=" + srcUrl + ", title=" + title + "]";
    }
}

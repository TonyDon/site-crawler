/*
 * @(#)ClientPostEntity.java 2016年4月3日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.app.sitecrawler.dto;

import java.util.List;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2016年4月3日
 * </pre>
 */
public class ClientPostEntity {

    private String requestId;
    
    private String entityMd5Value;
    
    private InfoRecord singleRecord;
    
    private List<InfoRecord> records;

    
    public String getRequestId() {
        return requestId;
    }

    
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    
    public String getEntityMd5Value() {
        return entityMd5Value;
    }

    
    public void setEntityMd5Value(String entityMd5Value) {
        this.entityMd5Value = entityMd5Value;
    }

    
    public InfoRecord getSingleRecord() {
        return singleRecord;
    }

    
    public void setSingleRecord(InfoRecord singleRecord) {
        this.singleRecord = singleRecord;
    }

    
    public List<InfoRecord> getRecords() {
        return records;
    }

    
    public void setRecords(List<InfoRecord> records) {
        this.records = records;
    }
}

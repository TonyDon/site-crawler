/*
 * @(#)OuterSiteInfoHandlerAction.java 2016年4月3日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.app.sitecrawler.action;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uuola.app.sitecrawler.dto.ClientPostEntity;
import com.uuola.app.sitecrawler.dto.InfoRecord;
import com.uuola.app.sitecrawler.service.RecordQueueManager;
import com.uuola.commons.CollectionUtil;
import com.uuola.commons.JsonUtil;
import com.uuola.txweb.framework.action.BaseAction;


/**
 * <pre>
 * 接受来自phantomjs客户端抓取后的结果处理
 * @author tangxiaodong
 * 创建日期: 2016年4月3日
 * </pre>
 */
@Controller
@RequestMapping("/outerSiteInfoHandler")
public class OuterSiteInfoHandlerAction extends BaseAction {
    
    private static Set<String> existUUIDs = new HashSet<String>();
    
    private static Set<String> existMd5 = new HashSet<String>();

    @RequestMapping(value="/gaosiao", method = RequestMethod.POST)
    @ResponseBody
    public String gaosiao(@RequestBody ClientPostEntity clientPost){
        if(existUUIDs.contains(clientPost.getRequestId())){
            return "doing...";
        };
        existUUIDs.add(clientPost.getRequestId());
        InfoRecord rec = clientPost.getSingleRecord();
        checkAndPushQueue(rec);
        List<InfoRecord> records = clientPost.getRecords();
        if(CollectionUtil.isNotEmpty(records)){
            for(InfoRecord record : records){
                checkAndPushQueue(record);
            }
        }
        return "ok";
    }

    private synchronized void checkAndPushQueue(InfoRecord rec) {
        if(null != rec && !existMd5.contains(rec.getRecordMd5Value())){
            existMd5.add(rec.getRecordMd5Value());
            RecordQueueManager.push(rec);
        }
    }
    
    @RequestMapping(value="/qiqu", method = RequestMethod.POST)
    @ResponseBody
    public String qiqu(@RequestBody ClientPostEntity clientPost){
        if(existUUIDs.contains(clientPost.getRequestId())){
            return "doing...";
        };
        existUUIDs.add(clientPost.getRequestId());
        InfoRecord rec = clientPost.getSingleRecord();
        checkAndPushQueue(rec);
        List<InfoRecord> records = clientPost.getRecords();
        if(CollectionUtil.isNotEmpty(records)){
            for(InfoRecord record : records){
                checkAndPushQueue(record);
            }
        }
        return "ok";
    }
    
    @RequestMapping(value="/queueInfo", method = RequestMethod.GET)
    @ResponseBody
    public String queueInfo(){
        return "curr queue count: " + RecordQueueManager.getCurrCount();
    }
}

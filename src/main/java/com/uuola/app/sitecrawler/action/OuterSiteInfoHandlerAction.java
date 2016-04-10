/*
 * @(#)OuterSiteInfoHandlerAction.java 2016年4月3日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.app.sitecrawler.action;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uuola.app.sitecrawler.component.RecordQueueManager;
import com.uuola.app.sitecrawler.component.RecordRepeaterCrawlManager;
import com.uuola.app.sitecrawler.dto.ClientPostEntity;
import com.uuola.app.sitecrawler.dto.InfoRecord;
import com.uuola.commons.CollectionUtil;
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

    @RequestMapping(value="/resovle", method = RequestMethod.POST)
    @ResponseBody
    public String resovle(@RequestBody ClientPostEntity clientPost){
        log.info("requestId:"+clientPost.getRequestId());
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

    private void checkAndPushQueue(InfoRecord rec) {
        if(null != rec && !RecordRepeaterCrawlManager.exist(rec.getRecordMd5Value())){
            RecordQueueManager.push(rec);
        }
    }
    
    @RequestMapping(value="/queueInfo", method = RequestMethod.GET)
    @ResponseBody
    public String queueInfo(){
        return "curr queue count: " + RecordQueueManager.getCurrCount();
    }
    
}

/*
 * @(#)RecordRepeatBoxCleanTask.java 2016年4月4日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.app.sitecrawler.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uuola.app.sitecrawler.component.RecordRepeaterCrawlManager;


/**
 * <pre>
 * 清理重复抓取记录hash池
 * @author tangxiaodong
 * 创建日期: 2016年4月4日
 * </pre>
 */
public class RepeatBoxCleanTask implements Runnable {

    private Logger log = LoggerFactory.getLogger(RepeatBoxCleanTask.class);
    
    @Override
    public void run() {
        log.info("record crawl box size: "+ RecordRepeaterCrawlManager.size());
        RecordRepeaterCrawlManager.clean();
        log.info("record crawl box clean , done ! ");
    }

}

/*
 * @(#)RecordConsumeTask.java 2016年4月3日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.app.sitecrawler.component;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uuola.app.sitecrawler.dto.InfoRecord;
import com.uuola.commons.listener.WebContext;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2016年4月3日
 * </pre>
 */
public class RecordConsumeTask implements Runnable {
    
    private Logger log = LoggerFactory.getLogger(RecordConsumeTask.class);
    
    private File tempDir;
    
    public RecordConsumeTask(){
        this.tempDir = new File(WebContext.getRealPath("/temp"));
        log.info("use temp dir : " + tempDir.getAbsolutePath() );
    }

    @Override
    public void run() {
        int times = 0;
        for (;;) {
            InfoRecord record = RecordQueueManager.take();
            if (null != record) {
                RecordProcessor.execute(tempDir, record);
                if(record.isExistError() && record.getTryTimes()<3){
                    record.setExistError(false);
                    record.increaseTryTimes();
                    RecordQueueManager.push(record); // 放到队列中再次尝试处理，3次失败后放弃
                }
            } else {
                times++;
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    log.warn("sleep()", e);
                }
            }
            if (times > 10) {
                log.info("try times 10, take emtpy record, exit task!");
                break;
            }
        }
    }

}

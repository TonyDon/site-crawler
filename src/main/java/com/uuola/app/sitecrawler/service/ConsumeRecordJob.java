/*
 * @(#)ConsumeRecordJob.java 2016年4月3日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.app.sitecrawler.service;

import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.uuola.app.sitecrawler.task.RecordConsumeTask;


/**
 * <pre>
 * 抓取记录消费端JOB
 * @author tangxiaodong
 * 创建日期: 2016年4月3日
 * </pre>
 */
@Component("consumeRecordJob")
public class ConsumeRecordJob  implements InitializingBean{
    
    @Autowired
    private TaskScheduler scheduler;
    
    private ScheduledFuture future;
    
    public void cancelTask() {
        if (future != null) {
            future.cancel(true);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        startTask("2 0/3 * * * ?"); // 每隔10分钟触发一次
    }
    
    public void startTask(String cronTime) {
        future = scheduler.schedule(new RecordConsumeTask(), new CronTrigger(cronTime));
    }

}

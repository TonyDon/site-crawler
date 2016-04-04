/*
 * @(#)RecordRepeatBoxCleanJob.java 2016年4月4日
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

import com.uuola.app.sitecrawler.task.RepeatBoxCleanTask;


/**
 * <pre>
 * 清理记录重复抓取记录池缓存
 * @author tangxiaodong
 * 创建日期: 2016年4月4日
 * </pre>
 */
@Component("recordRepeatBoxCleanJob")
public class RecordRepeatBoxCleanJob implements InitializingBean {


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
        startTask("17 12 5 * * ?"); // 每天5点12分17秒触发一次
    }
    
    public void startTask(String cronTime) {
        future = scheduler.schedule(new RepeatBoxCleanTask(), new CronTrigger(cronTime));
    }

}

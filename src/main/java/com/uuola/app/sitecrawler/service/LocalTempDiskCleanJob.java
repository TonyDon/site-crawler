/*
 * @(#)LocalTempDiskCleanJob.java 2016年4月4日
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

import com.uuola.app.sitecrawler.task.TempDiskCleanTask;


/**
 * <pre>
 * 本地磁盘文件清理JOB
 * @author tangxiaodong
 * 创建日期: 2016年4月4日
 * </pre>
 */
@Component("localTempDiskCleanJob")
public class LocalTempDiskCleanJob implements InitializingBean {

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
        startTask("15 5 1 * * ?"); // 每天凌晨1点执行
    }
    
    public void startTask(String cronTime) {
        future = scheduler.schedule(new TempDiskCleanTask(), new CronTrigger(cronTime));
    }

}

/*
 * @(#)RecordQueueManager.java 2016年4月3日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.app.sitecrawler.component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uuola.app.sitecrawler.dto.InfoRecord;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2016年4月3日
 * </pre>
 */
public class RecordQueueManager {

    private static Logger log = LoggerFactory.getLogger(RecordQueueManager.class);
    
    private static Queue<InfoRecord> queue = new ConcurrentLinkedQueue<InfoRecord>();
    
    private static AtomicInteger counter = new AtomicInteger();
    
    /**
     * 将一个对象推入队列中，等待后续处理
     * @param image
     */
    public static void push(InfoRecord record) {
        // 加入到队列尾部
        if (queue.offer(record)) {
            counter.incrementAndGet();
        }else{
            log.error("push() offer error! record:" + record);
        }
    }
    
    /**
     * 拿出一个数据
     * @return
     */
    public static InfoRecord take(){
        // 从队列头部拿取一个元素
        InfoRecord record = queue.poll();
        if(null != record){
            counter.decrementAndGet();
        }
        return record;
    }
    
    /**
     * 得到当前队列元素个数
     * @return
     */
    public static int getCurrCount(){
        return counter.get();
    }
    
}

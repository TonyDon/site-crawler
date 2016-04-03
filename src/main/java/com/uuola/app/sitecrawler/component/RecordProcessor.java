/*
 * @(#)RecordProcessor.java 2016年4月3日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.app.sitecrawler.component;

import java.io.File;

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
public class RecordProcessor {
    
    private static Logger log = LoggerFactory.getLogger(RecordProcessor.class);

    public static void execute(File tempDir, InfoRecord rec){
        log.info("begin execute() for record:" + rec);
        rec.setTempFile(tempDir);
        // 图片预处理
        ImageHandler.process(rec);
        // html 清理
        HtmlHandler.process(rec);
        // post 远端请求
        RemotePostHandler.process(rec);
        log.info("end execute() for record:" + rec);
    }
}

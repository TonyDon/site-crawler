/*
 * @(#)RecordProcessor.java 2016年4月3日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.app.sitecrawler.component;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.uuola.app.sitecrawler.component.handler.HtmlHandler;
import com.uuola.app.sitecrawler.component.handler.ImageDownHandler;
import com.uuola.app.sitecrawler.component.handler.ImagePostHandler;
import com.uuola.app.sitecrawler.component.handler.ImageSizeFixHandler;
import com.uuola.app.sitecrawler.component.handler.RemotePostHandler;
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
    
    private static RestTemplate restTemplate = new RestTemplate();

    public static void execute(File tempDir, InfoRecord rec){
        log.info("begin execute() for record:" + rec);
        rec.setTempFile(tempDir);
        // 图片下载本地
        ImageDownHandler.process(rec);
        // 图片大小，类型修复
        ImageSizeFixHandler.process(rec);
        // 图片上传远端服务器
        ImagePostHandler.process(rec);
        // html 清理, 合并多图到正文中
        HtmlHandler.process(rec);
        // post 远端请求
        RemotePostHandler.process(restTemplate, rec);
        log.info("end execute() for record:" + rec);
    }
}

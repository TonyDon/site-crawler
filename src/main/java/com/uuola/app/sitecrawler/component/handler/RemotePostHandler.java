/*
 * @(#)RemotePostHandler.java 2016年4月3日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.app.sitecrawler.component.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uuola.app.sitecrawler.dto.InfoRecord;
import com.uuola.commons.JsonUtil;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2016年4月3日
 * </pre>
 */
public class RemotePostHandler {
    
    private static Logger log = LoggerFactory.getLogger(RemotePostHandler.class);

    public static void process(InfoRecord rec) {
        if(!rec.isExistError()){
            //TODO post remote action
            log.info("post remote:" + JsonUtil.toJSONString(rec));
        }
    }

}

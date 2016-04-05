/*
 * @(#)RemotePostHandler.java 2016年4月3日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.app.sitecrawler.component.handler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.uuola.app.sitecrawler.constants.Config;
import com.uuola.app.sitecrawler.dto.InfoRecord;
import com.uuola.commons.JsonUtil;
import com.uuola.txweb.framework.action.IConstant;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2016年4月3日
 * </pre>
 */
public class RemotePostHandler {
    
    
    private static Logger log = LoggerFactory.getLogger(RemotePostHandler.class);

    public static void process(RestTemplate restTemplate, InfoRecord rec) {
        if (rec.isExistError()) {
            return;
        }
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("AccessToken", "asdfjklasdkfl");
        try {
            HttpEntity<String> formEntity = new HttpEntity<String>(JsonUtil.toJSONString(rec), headers);
            String response = restTemplate.postForObject(Config.RECORD_POST_URL, formEntity, String.class);
            Map result = JsonUtil.toJsonObject(response, Map.class);
            Object flag = result.get(IConstant.UPDATE_RESULT_ATTR);
            log.info("post record to server, result:" + flag + ", rec.srcUrl:" + rec.getSrcUrl());
        } catch (Exception e) {
            rec.setExistError(true);
            log.error("process() rec.srcUrl:" + rec.getSrcUrl(), e);
        }
    }

}

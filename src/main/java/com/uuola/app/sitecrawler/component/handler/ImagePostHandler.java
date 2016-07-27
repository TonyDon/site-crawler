/*
 * @(#)ImagePostHandler.java 2016年4月4日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.app.sitecrawler.component.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uuola.app.sitecrawler.constants.Config;
import com.uuola.app.sitecrawler.dto.InfoRecord;
import com.uuola.commons.CollectionUtil;
import com.uuola.commons.JsonUtil;
import com.uuola.commons.StringUtil;
import com.uuola.commons.http.HttpUtil;
import com.uuola.txweb.framework.action.IConstant;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2016年4月4日
 * </pre>
 */
public class ImagePostHandler {
    

    private static Logger log = LoggerFactory.getLogger(ImagePostHandler.class);
    
    
    
    public static void process(InfoRecord rec) {
        if (rec.isExistError()) {
            return;
        }
        Map<String, String> localSrcUrl = rec.getLocalSrcUrl();
        if (CollectionUtil.isEmpty(localSrcUrl)) {
            return;
        }
        List<String> remoteImgUrls = new ArrayList<String>(localSrcUrl.size());
        for (Map.Entry<String, String> e : localSrcUrl.entrySet()) {
            if (StringUtil.isNotEmpty(e.getKey())) {
                String remoteUrl = uploadToServer(e.getKey());
                if (StringUtil.isEmpty(remoteUrl)) {
                    log.warn("curr use src img url: " + rec);
                    remoteImgUrls.add(e.getValue());
                } else {
                    remoteImgUrls.add(remoteUrl);
                }
            }
        }
        if (remoteImgUrls.size() > 0) {
            rec.setRemoteImgUrls(remoteImgUrls);
        }
    }

    @SuppressWarnings("rawtypes")
    private static String uploadToServer(String local) {
        File localImage = new File(local);
        // 大于1M的文件使用源图地址直接返回
        if (!localImage.exists() || localImage.length() > Config.UPLOAD_MAX_FILE_SIZE) {
            return null;
        }
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("dir", "image");
        params.put("needThumb", "false");
        params.put("mpfile", localImage);
        Integer timeout = 16000;
        String ret = HttpUtil.doPostAsFormdata(Config.IMAGE_POST_URL, "utf-8", null, params, timeout, timeout);
        if (null == ret) {
            return null;
        }
        if (ret != null && ret.toLowerCase().contains(IConstant.EXCEPTION)) {
            log.warn("uploadToServer() fail: local[" + local + "], message:" + ret);
            return null;
        }
        Map result = JsonUtil.toJsonObject(ret, Map.class);
        String url = (String) result.get("url");
        String message = (String) result.get("message");
        String error = String.valueOf(result.get("error"));
        if ("0".equals(error) && StringUtil.isNotEmpty(url)) {
            return url;
        } else {
            log.warn("uploadToServer() fail: local[" + local + "], message:" + message);
        }
        return null;
    }
}

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

import com.uuola.app.sitecrawler.dto.InfoRecord;
import com.uuola.commons.CollectionUtil;
import com.uuola.commons.JsonUtil;
import com.uuola.commons.StringUtil;
import com.uuola.commons.http.HttpUtil;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2016年4月4日
 * </pre>
 */
public class ImagePostHandler {
    
    private final static String POST_URL = "http://986001.com/txcms-web/uploader/store.json";

    private static Logger log = LoggerFactory.getLogger(ImagePostHandler.class);
    
    public static void process(InfoRecord rec){
        if (rec.isExistError()) {
            return;
        }
        List<String> localImgPaths = rec.getLocalImgPaths();
        if(CollectionUtil.isEmpty(localImgPaths)){
            return ;
        }
        List<String> remoteImgUrls = new ArrayList<String>(localImgPaths.size());
        for(String local : localImgPaths){
            if (StringUtil.isEmpty(local)) {
                continue;
            }
            // post remote image
            String remoteUrl = uploadToServer(local);
            // success add  remoteImgUrls
            if(StringUtil.isEmpty(remoteUrl)){
                log.warn("post img error : " + rec);
                rec.setExistError(true);
                break;
            }
            remoteImgUrls.add(remoteUrl);
        }
        if (remoteImgUrls.size() > 0) {
            rec.setRemoteImgUrls(remoteImgUrls);
        }
    }

    @SuppressWarnings("rawtypes")
    private static String uploadToServer(String local) {
        Map<String, Object> params = new LinkedHashMap<String,Object>();
        params.put("dir", "image");
        params.put("needThumb", "false");
        params.put("mpfile", new File(local));
        String ret = HttpUtil.doPostAsFormdata(POST_URL, "utf-8", null, params, null, null);
        if(null == ret){
            return null;
        }
        Map result = JsonUtil.toJsonObject(ret, Map.class);
        String url = (String)result.get("url");
        String message = (String)result.get("message");
        String error = String.valueOf(result.get("error"));
        if("0".equals(error) && StringUtil.isNotEmpty(url)){
            return url;
        }else{
            log.warn("uploadToServer() fail: local[" + local+"], message:" + message);
        }
        return null;
    }
}

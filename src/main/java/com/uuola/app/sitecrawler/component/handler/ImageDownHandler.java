/*
 * @(#)ImageHandler.java 2016年4月3日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.app.sitecrawler.component.handler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uuola.app.sitecrawler.constants.Config;
import com.uuola.app.sitecrawler.dto.InfoRecord;
import com.uuola.commons.CollectionUtil;
import com.uuola.commons.StringUtil;
import com.uuola.commons.coder.KeyGenerator;
import com.uuola.commons.file.FileUtil;
import com.uuola.commons.http.HttpUtil;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2016年4月3日
 * </pre>
 */
public class ImageDownHandler {
    
    private static Logger log = LoggerFactory.getLogger(ImageDownHandler.class);

    public static void process(InfoRecord rec) {
        if (rec.isExistError()) {
            return;
        }
        List<String> imgUrls = rec.getImgs();
        if (CollectionUtil.isEmpty(imgUrls)) {
            return;
        }
        Map<String, String> localSrcUrl = new LinkedHashMap<String, String>();
        for (String imgUrl : imgUrls) {
            if (StringUtil.isNotEmpty(imgUrl)) {
                // get请求获取img数据
                String localImg = downToDisk(rec, imgUrl);
                if (StringUtil.isEmpty(localImg)) {
                    log.warn("down img error : " + rec);
                    rec.setExistError(true);
                    break;
                }
                localSrcUrl.put(localImg, imgUrl);
            }
        }
        if (localSrcUrl.size() > 0) {
            rec.setLocalSrcUrl(localSrcUrl);
        }
    }
    
    private static String downToDisk(InfoRecord rec, String imgUrl) {
        ByteBuffer byteBuffer = HttpUtil.doGetForBytes(imgUrl, "utf-8", 32000, 32000, 128+Config.UPLOAD_MAX_FILE_SIZE, makeProxyHeaders(rec));
        String imagePath = null;
        OutputStream os = null;
        try {
            byte[] body = byteBuffer.array();
            if (null != body && body.length > 8) {
                File outImage = extractImageFile(rec.getTempFile(), imgUrl);
                os = new BufferedOutputStream(new FileOutputStream(outImage));
                IOUtils.write(body, os);
                imagePath = outImage.getAbsolutePath();
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            IOUtils.closeQuietly(os);
        }
        return imagePath;
    }
    
    private static Map<String, Object> makeProxyHeaders(InfoRecord rec) {
        Map<String, Object> headers = new HashMap<String,Object>();
        headers.put("Referer", getReferer(rec.getSrcUrl()));
        headers.put("Host", getHost(rec.getSrcUrl()));
        headers.put("DNT", "1");
        headers.put("Connection", "keep-alive");
        headers.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
        return headers;
    }
    
    /**
     * ${root}/imgmall.tg.com.cn/group2/M00/00/1E/CgooeVachCSEidIvAAF9XjtddYY346.jpg
     * @param storeDir
     * @param url
     * @param nameFlag 
     * @param currRow 
     * @return
     */
    private static File extractImageFile(File storeDir, String url) {
        String imgName = FileUtil.getFileName(url);
        String extName = FileUtil.getFileExt(imgName);
        if (null == extName || extName.isEmpty()) {
            extName = "jpeg"; // 给默认扩展名
        }
            imgName = KeyGenerator.getUUID() + "." + extName;
        return new File(storeDir, imgName);
    }
    
    public static String getHost(String url) {
        String s1 = url.replaceAll("(http://)|(https://)", "");
        int pos = s1.indexOf('/');
        return s1.substring(0, pos);
    }

    private static String getReferer(String srcUrl) {
        if (null != srcUrl) {
            if (srcUrl.contains("hao123")) {
                return "http://www.hao123.com/";
                
            } else if (srcUrl.contains("sogou")) {
                return "http://www.sogou.com/";
                
            } else if (srcUrl.contains("ifeng")) {
                return "http://news.ifeng.com/";
            }
        }
        return "http://www.baidu.com/";
    }

}

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
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uuola.app.sitecrawler.dto.InfoRecord;
import com.uuola.commons.CollectionUtil;
import com.uuola.commons.StringUtil;
import com.uuola.commons.coder.KeyGenerator;
import com.uuola.commons.file.FileUtil;


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
        List<String> localImgPaths = new ArrayList<String>(imgUrls.size());
        for (String imgUrl : imgUrls) {
            if (StringUtil.isEmpty(imgUrl)) {
                continue;
            }
            // get请求获取img数据
            String localImg = downToDisk(rec, imgUrl);
            if (StringUtil.isEmpty(localImg)) {
                log.warn("down img error : " + rec);
                rec.setExistError(true);
                break;
            }
            localImgPaths.add(localImg);
        }
        if (localImgPaths.size() > 0) {
            rec.setLocalImgPaths(localImgPaths);
        }
    }
    
    private static String downToDisk(InfoRecord rec, String imgUrl) {
        Connection conn = Jsoup.connect(imgUrl);
        conn.referrer(getReferer(rec.getSrcUrl()));
        conn.header("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
        conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        conn.header("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
        conn.header("Connection", "keep-alive");
        conn.header("Host", getHost(rec.getSrcUrl()));
        conn.ignoreContentType(true);
        conn.timeout(8000);
        String imagePath = null;
        OutputStream os = null;
        try {
            Response res = conn.method(Method.GET).execute();
            if (HttpURLConnection.HTTP_OK == res.statusCode()) {
                byte[] body = res.bodyAsBytes();
                if (null != body && body.length > 8) {
                    File outImage = extractImageFile(rec.getTempFile(), imgUrl);
                    os = new BufferedOutputStream(new FileOutputStream(outImage));
                    IOUtils.write(body, os);
                    imagePath = outImage.getAbsolutePath();
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }finally{
            IOUtils.closeQuietly(os);
        }
        return imagePath;
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
            extName = "jpg";
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
            }
        }
        return "http://www.baidu.com/";
    }

}

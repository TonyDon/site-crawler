/*
 * @(#)ImageHandler.java 2016年4月3日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.app.sitecrawler.component;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class ImageHandler {
    
    private static Logger log = LoggerFactory.getLogger(ImageHandler.class);

    public static void process(InfoRecord rec) {
        List<String> imgUrls = rec.getImgs();
        if(CollectionUtil.isEmpty(imgUrls)){
            return ;
        }
        List<String> remoteImgUrls =  new ArrayList<String>(imgUrls.size());
        for(String imgUrl : imgUrls){
            if(StringUtil.isEmpty(imgUrl)){
                continue;
            }
            // get请求获取img数据 
            String localImgUrl = downLocal(rec, imgUrl);
            if(StringUtil.isEmpty(localImgUrl)){
                log.warn("down img error : " + imgUrl);
                rec.setExistError(true);
            }else{
                // post local image to remote image upload 
                log.info("down img success:" + localImgUrl);
                remoteImgUrls.add("remote :" + localImgUrl);
            }
        }
        if(remoteImgUrls.size()>0){
            rec.setRemoteImgUrls(remoteImgUrls);
        }
    }
    
    
    private static String downLocal(InfoRecord rec, String imgUrl) {
        ByteBuffer byteBuffer = HttpUtil.doGetForBytes(imgUrl, "utf-8", 5000, 8000, makeProxyHeaders(rec.getSrcUrl(), imgUrl));
        if (null == byteBuffer) {
            return null;
        }
        byte[] bytes = byteBuffer.array();
        if(null == bytes || bytes.length<8){
            return null;
        }
        File outImage = extractImageFile(rec.getTempFile(), imgUrl);
        String imagePath = null;
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(outImage));
            IOUtils.write(bytes, os);
            os.flush();
            imagePath = outImage.getAbsolutePath();
        } catch (Exception e) {
            log.error("downLocal error {}", ExceptionUtils.getRootCauseMessage(e));
        } finally {
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
            imgName = imgName + "." + extName; // 设置默认后缀名
        }
            imgName = KeyGenerator.getUUID() + "." + extName;
        return new File(storeDir, imgName);
    }

    private static Map<String, Object> makeProxyHeaders(String srcUrl, String url) {
        Map<String, Object> headers = new HashMap<String,Object>();
        headers.put("Referer", getReferer(srcUrl));
        headers.put("Host", getHost(url));
        headers.put("DNT", "1");
        headers.put("Connection", "close");
        headers.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
        return headers;
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

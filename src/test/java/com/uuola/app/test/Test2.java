/*
 * @(#)Test2.java 2016年4月14日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.app.test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.uuola.commons.StringUtil;
import com.uuola.commons.file.FileUtil;
import com.uuola.commons.http.HttpUtil;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2016年4月14日
 * </pre>
 */
public class Test2 {

    /**
     * @param args
     * @throws IOException 
     * @throws MalformedURLException 
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws MalformedURLException, IOException, InterruptedException {
        String url = "http://www.u148.net/article/138414.html";
        Document doc = Jsoup.parse(new URL(url), 3000);
        System.out.println(doc.select("title").text());
        Elements imageEls = doc.select("div.contents p a img");
        for(Element e : imageEls){
            String img = e.absUrl("src");
            if(StringUtil.isNotEmpty(img)){
                System.out.println(downImg(img));
            }
        }
    }

    private static String downImg(String img) {
        ByteBuffer byteBuffer = HttpUtil.doGetForBytes(img, "utf-8", 3000, 3000, 1024*1024, makeProxyHeaders());
        String imagePath = null;
        OutputStream os = null;
        String fileName = FileUtil.getFileName(img);
        try {
            byte[] body = byteBuffer.array();
            if (null != body && body.length > 8) {
                File outImage = new File("C:/Users/tangxiaodong/Desktop/g/u148", fileName);
                os = new BufferedOutputStream(new FileOutputStream(outImage));
                IOUtils.write(body, os);
                imagePath = outImage.getAbsolutePath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
        }
        return imagePath;
    }

    private static Map<String, Object> makeProxyHeaders() {
        Map<String, Object> headers = new HashMap<String,Object>();
        headers.put("Referer", "http://www.u148.net/");
        headers.put("Host", "www.u148.net");
        headers.put("DNT", "1");
        headers.put("Connection", "keep-alive");
        headers.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
        return headers;
    }

}

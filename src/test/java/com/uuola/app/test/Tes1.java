/*
 * @(#)Tes1.java 2016年4月12日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.app.test;



import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.uuola.app.sitecrawler.dto.ClientPostEntity;
import com.uuola.app.sitecrawler.dto.InfoRecord;
import com.uuola.commons.JsonUtil;
import com.uuola.commons.StringUtil;
import com.uuola.commons.coder.KeyGenerator;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2016年4月12日
 * </pre>
 */
public class Tes1 {

    /**
     * @param args
     * @throws IOException 
     * @throws MalformedURLException 
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws MalformedURLException, IOException, InterruptedException {
        // TODO Auto-generated method stub
        String url = "http://pic.people.com.cn/n1/2016/0303/c1016-28167513.html";
        Document doc = Jsoup.parse(new URL(url), 3000);
        Element titleEl = doc.select("div.title>h1").first();
        Element imageEl = doc.select("div#picG img").first();
        Element content = doc.select("div.content p").first();
        Elements nextEls = doc.select("div.page_n>a");
        ClientPostEntity postEntity = new ClientPostEntity();
        InfoRecord rec = new InfoRecord();

        postEntity.setSingleRecord(rec);
        postEntity.setEntityMd5Value(KeyGenerator.getUUID());
        postEntity.setRequestId(KeyGenerator.getUUID());
        
        List<String> imgs = new ArrayList<String>();
        List<String> pContSegment = new ArrayList<String>();
        
        if (null != imageEl) {
            String img = imageEl.absUrl("src");
            if (!img.isEmpty()) {
                imgs.add(img);
            }
            if (null != content && null != content.text()) {
                String cont = content.text();
                rec.setSummary(cont);
                if (cont.length() > 64) {
                    rec.setSummary(cont.substring(0, 64) + "...");
                }
                pContSegment.add("<p>" + cont + "</p>");
            }
        }

        rec.setTitle(null!=titleEl ? titleEl.text() : null);
        rec.setSrcUrl(url);
        rec.setCatId(28);
        rec.setAuthorId(10101L);
        rec.setRecordMd5Value(DigestUtils.md5Hex(url));
        
        for(int i=1; i<nextEls.size()-1; i++){
            Element el = nextEls.get(i);
            Page page = crawlSinglePage(el.absUrl("href"));
            if(StringUtil.isNotEmpty(page.getImg())){
                imgs.add(page.getImg());
                pContSegment.add("<p>$IMGS#"+i+"$</p><p>"+page.getNote()+"</p>");
            }
        }
        
        if(!imgs.isEmpty()){
            rec.setImgs(imgs);
        }
        if(!pContSegment.isEmpty()){
            rec.setContent(StringUtils.join(pContSegment,""));
        }
        String jsonData = JsonUtil.toJSONString(postEntity);
        System.out.println(jsonData);
        Thread.sleep(2000);
        post(jsonData);
    }
    
    private static Page crawlSinglePage(String url) throws MalformedURLException, IOException{
        Document doc = Jsoup.parse(new URL(url), 5000);
        Element imageEl = doc.select("div#picG img").first();
        Element content = doc.select("div.content p").first();
        Page page = new Page();
        page.setImg(imageEl.absUrl("src"));
        page.setNote(content.text());
        return page;
    }
    
    private static void post(String json){
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpEntity<String> formEntity = new HttpEntity<String>(json, headers);
            String response = restTemplate.postForObject("http://986001.com/site-crawler/outerSiteInfoHandler/resovle", formEntity, String.class);
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    

}

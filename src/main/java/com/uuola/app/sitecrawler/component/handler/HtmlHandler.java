/*
 * @(#)HtmlHandler.java 2016年4月3日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.app.sitecrawler.component.handler;

import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uuola.app.sitecrawler.constants.Config;
import com.uuola.app.sitecrawler.dto.InfoRecord;
import com.uuola.commons.StringUtil;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2016年4月3日
 * </pre>
 */
public class HtmlHandler {
    
    private static Logger log = LoggerFactory.getLogger(HtmlHandler.class);
    
    private static Pattern EMOJI_CHAR_REGEX = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE) ;

    public static void process(InfoRecord rec) {
        if (rec.isExistError()) {
            return;
        }
        String summary = rec.getSummary();
        if (StringUtil.isNotEmpty(summary)) {
            String s1 = summary;
            if (summary.contains("<br/>")) {
                s1 = StringUtil.replace(summary, "<br/>", "\r\n");
            }
            if (summary.contains("<br />")) {
                s1 = StringUtil.replace(s1, "<br />", "\r\n");
            }
            if (summary.contains("<br>")) {
                s1 = StringUtil.replace(s1, "<br>", "\r\n");
            }
            rec.setSummary(s1);
        }
        String content = rec.getContent();
        if (StringUtil.isNotEmpty(content)) {
            rec.setContent(Jsoup.clean(content, Whitelist.basicWithImages()));
            rec.setContent(EMOJI_CHAR_REGEX.matcher(rec.getContent()).replaceAll("*"));
        }
        List<String> remoteImgUrls = rec.getRemoteImgUrls();
        // 针对多图，将除第一张主图外的其他图片插入到正文对应的图片占位符中
        if (null != remoteImgUrls && remoteImgUrls.size() > 1) {
            log.info(StringUtil.join(remoteImgUrls, "\r\n"));
            String cont = rec.getContent();
            if (null != cont && cont.contains(Config.CONT_IMG_REPLACE_TAG)) {
                for (int index = 1; index < remoteImgUrls.size(); index++) {
                    String imgTag = String.format("<img src=\"%s\"/>", remoteImgUrls.get(index));
                    String currReplaceTag = Config.CONT_IMG_REPLACE_TAG+index+'$';
                    cont = StringUtil.replace(cont, currReplaceTag, imgTag);
                }
                rec.setContent(cont);
            }
        }
    }

}

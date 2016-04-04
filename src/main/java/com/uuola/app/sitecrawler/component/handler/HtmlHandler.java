/*
 * @(#)HtmlHandler.java 2016年4月3日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.app.sitecrawler.component.handler;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

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

    public static void process(InfoRecord rec) {
        if(rec.isExistError()){
            return ;
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
            rec.setContent(Jsoup.clean(content, Whitelist.basic()));
        }
    }

}

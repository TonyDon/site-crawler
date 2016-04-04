/*
 * @(#)RecordProduceTaesk.java 2016年4月3日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.app.sitecrawler.task;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uuola.commons.listener.WebContext;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2016年4月3日
 * </pre>
 */
public class RecordProduceTask implements Runnable {
    
    private Logger log = LoggerFactory.getLogger(RecordProduceTask.class);

    @Override
    public void run() {
        String currWorkDir = WebContext.getRealPath("/jsbin");
        File wd = new File(currWorkDir);
        exec(new String[]{"D:\\ProgramFiles\\phantomjs\\bin\\pjs.exe", currWorkDir+"\\sogou_haha.js"}, wd);
        exec(new String[]{"D:\\ProgramFiles\\phantomjs\\bin\\pjs.exe", currWorkDir+"\\hao123_gaoxiao.js"}, wd);
    }

    private void exec(String[] args, File workDir){
        try {
            log.info("exec:"+StringUtils.join(args, " "));
            Runtime.getRuntime().exec(args, null, workDir);
        } catch (IOException e) {
            log.error("exec()", e);
        }
    }
}

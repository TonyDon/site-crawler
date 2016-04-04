/*
 * @(#)TempDiskCleanTask.java 2016年4月4日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.app.sitecrawler.task;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uuola.commons.ObjectUtil;
import com.uuola.commons.listener.WebContext;


/**
 * <pre>
 * 清理临时磁盘文件
 * @author tangxiaodong
 * 创建日期: 2016年4月4日
 * </pre>
 */
public class TempDiskCleanTask implements Runnable {

private Logger log = LoggerFactory.getLogger(TempDiskCleanTask.class);
    
    private File tempDir;
    
    public TempDiskCleanTask(){
        this.tempDir = new File(WebContext.getRealPath("/temp"));
        log.info("use temp dir : " + tempDir.getAbsolutePath() );
    }
    

    @Override
    public void run() {
        int deleteNum = 0;
        File[] files = tempDir.listFiles();
        if (ObjectUtil.isNotEmpty(files)) {
            for (File file : files) {
                if (file.exists() && file.isFile()) {
                    if (file.delete()) {
                        deleteNum++;
                    } else {
                        log.warn("delete file fail : " + file.getAbsolutePath());
                    }
                }
            }
        }
        log.info("clean file num:" + deleteNum);
    }

}

/*
 * @(#)Config.java 2016年4月4日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.app.sitecrawler.constants;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2016年4月4日
 * </pre>
 */
public interface Config {

    public final String IMAGE_POST_URL = "http://m.986001.com/uploader/store.json";
    
    public final String RECORD_POST_URL = "http://m.986001.com/spi/record/post.json";
    
    public final int UPLOAD_MAX_FILE_SIZE = 1024*1024 ;
    
    public final int IMG_MAX_WIDTH = 650;
    
    public final long NEED_FIX_MAX_SIZE = 500000;
    
    public final int IMG_FIX_WIDTH = 650;
    
    public final String CONT_IMG_REPLACE_TAG = "$IMGS#" ; // $IMGS#1$, $IMGS#2$ ...
}

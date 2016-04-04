/*
 * @(#)RecordRepeaterCrawlManager.java 2016年4月4日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.app.sitecrawler.component;

import java.util.HashSet;
import java.util.Set;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2016年4月4日
 * </pre>
 */
public class RecordRepeaterCrawlManager {
    
    private static Set<String> hashBox = new HashSet<String>();
    
    public synchronized static boolean exist(String hash){
        boolean exist = hashBox.contains(hash);
        if(!exist){
            hashBox.add(hash);
        }
        return exist;
    }
    
    public static int size(){
        return hashBox.size();
    }
    
    public static void clean(){
        hashBox.clear();
    }
}

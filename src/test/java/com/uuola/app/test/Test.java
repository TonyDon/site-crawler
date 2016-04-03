/*
 * @(#)Test.java 2016年4月3日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.app.test;


/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2016年4月3日
 * </pre>
 */
public class Test {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String url = "https://abc.de/f.jsp";
        String s1 = url.replaceAll("(http://)|(https://)", "");
        int pos = s1.indexOf('/');
        System.out.println(s1.substring(0, pos));
    }

}

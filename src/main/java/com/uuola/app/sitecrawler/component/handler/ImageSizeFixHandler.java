/*
 * @(#)ImageSizeResetHandler.java 2016年4月9日
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.app.sitecrawler.component.handler;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uuola.app.sitecrawler.constants.Config;
import com.uuola.app.sitecrawler.dto.InfoRecord;
import com.uuola.commons.CollectionUtil;
import com.uuola.commons.StringUtil;
import com.uuola.commons.file.FileUtil;
import com.uuola.commons.image.ImageInfo;
import com.uuola.commons.image.ImageUtil;


/**
 * <pre>
 * 图片大小修饰
 * @author tangxiaodong
 * 创建日期: 2016年4月9日
 * </pre>
 */
public class ImageSizeFixHandler {

    private static Logger log = LoggerFactory.getLogger(ImageSizeFixHandler.class);
    
    public static void process(InfoRecord rec) {
        if (rec.isExistError()) {
            return;
        }
        Map<String, String> localSrcUrl = rec.getLocalSrcUrl();
        if (CollectionUtil.isEmpty(localSrcUrl)) {
            return;
        }
        Map<String, String> newlocalSrcUrl = new LinkedHashMap<String, String>();
        for (Map.Entry<String, String> e : localSrcUrl.entrySet()) {
            if (StringUtil.isNotEmpty(e.getKey())) {
                String local = checkAndFixed(e.getKey());
                if (StringUtil.isEmpty(local)) {
                    log.warn("check fixed error :  " + rec);
                } else {
                    newlocalSrcUrl.put(local, e.getValue());
                }
            }
        }
        if(newlocalSrcUrl.size()>0){
            rec.setLocalSrcUrl(newlocalSrcUrl);
        }else{
            rec.setExistError(true);
        }
    }

    /**
     * 判断图片类型，长宽，大小 进行本地文件缩图处理,处理成功返回本地图片文件路径，可能会重新格式化文件后缀名
     * @param key
     */
    public static String checkAndFixed(String imgLocal) {
        File img = new File(imgLocal);
        ImageInfo imgInfo = ImageUtil.detect(img);
        if (null == imgInfo || null == imgInfo.getFormatName()) {
            return null;
        }
        String ext = FileUtil.getFileExt(imgLocal);
        if("gif".equalsIgnoreCase(ext)){
            return imgLocal;
        }
        String fmt = imgInfo.getFormatName();
        int w = imgInfo.getWidth();
        long s = imgInfo.getSize();
        String newImgLocal = imgLocal;
        File currImg = img;
        // 尝试重命名后缀
        if (!fmt.equalsIgnoreCase(ext)) {
            int pos = imgLocal.lastIndexOf(ext);
            newImgLocal = imgLocal.substring(0, pos).concat(fmt);
            currImg = new File(newImgLocal);
            img.renameTo(currImg);
        }
        // webp , gif 和非法的图片不做处理
        if (!"gif".equals(fmt) && (w > Config.IMG_MAX_WIDTH || s > Config.NEED_FIX_MAX_SIZE)) {
            ImageUtil.resize(currImg, null, Config.IMG_FIX_WIDTH, 0, false);
        }
        return newImgLocal;
    }
}

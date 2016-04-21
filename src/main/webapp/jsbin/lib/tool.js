"use strict";
var ImgTools ={};

ImgTools.GetOrgiPicUrl4Sogou = function(url){
	if(!url){
		return '';
	}
	var pos = url.indexOf('&url=');
	if(pos>20){
		return url.substring(pos+5, url.length);
	}
	return url;
};

// http://img6.hao123.com/data/3_fc313ad20d4b88122a0c32bd47cae7ce_430
ImgTools.GetOrgiPicUrl4Hao123= function(url){
	if(!url){
		return '';
	}
	var pos = url.lastIndexOf('_');
	if(pos>20){
		return url.substring(0, pos)+'_0';
	}
	return url;
};

ImgTools.GetOrgiPicUrl4MeiPai= function(url){
	if(!url){
		return '';
	}
	var pos = url.lastIndexOf('!');
	if(pos>20){
		return url.substring(0, pos);
	}
	return url;
};

ImgTools.GetOrgiPicUrl_2345Xiaohua= function(url){
	if(!url){
		return '';
	}
	if(url.indexOf('http')>=0){
		return url;
	}
	return 'http://day.2345.com/'+url;
};
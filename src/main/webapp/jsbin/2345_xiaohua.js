"use strict";
var page = require('webpage').create();
var fs = require('fs');
var settings = {
	operation: 'GET',
	encoding: 'utf8',
	resourceTimeout: 8000,
	headers: {
		"Content-Type": "text/html; charset=utf-8",
		"Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
		"User-Agent": "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:24.0) Gecko/20100101 Firefox/24.0",
		"Host": 'day.2345.com',
		"Referer": 'http://day.2345.com/'
	}
};

var fs_settings = {
	"encoding": "utf8",
	"flag": "w"
};
page.viewportSize = {
	width: 640,
	height: 800
};
page.onResourceRequested = function(requestData, request) {
	if ((/http:\/\/.+?\.css/gi).test(requestData['url']) || requestData.headers['Content-Type'] == 'text/css') {
		console.log('The url of the request is matching. Aborting: ' + requestData['url']);
		request.abort();
	}
	if ((/http:\/\/.+?\.baidu\.com/gi).test(requestData['url'])) {
		console.log('The url of the request is matching. Aborting: ' + requestData['url']);
		request.abort();
	}
//	if ((/.+?(\.jpg|\.gif|\.png)/gi).test(requestData['url']) || requestData.headers['Content-Type'].indexOf('image') !== -1) {
//		console.log('The url of the request is matching. Aborting: ' + requestData['url']);
//		request.abort();
//	}
};

var PostCrawlResult = function(data) {
	var postData = JSON.stringify(data);
	var headers = {
		"Content-Type": "application/json"
	};
	console.log(postData);
	var settings = {
	operation: 'POST',
	encoding: 'utf8',
	resourceTimeout: 8000,
	headers: {
		"Content-Type": "application/json; charset=utf-8"
	},
	data : postData
};
	page.open('http://986001.com/site-crawler/outerSiteInfoHandler/resovle', settings, function(status) {
		if (status !== 'success') {
			console.log('Unable to post!');
		} else {
			console.log(page.content);
		}
		phantom.exit();
	});
};


page.open('http://day.2345.com/index.php?p=2101', settings, function(status) {
	page.injectJs("lib/tool.js");
	page.injectJs("lib/jquery.js");
	page.injectJs("lib/uuid.js");
	page.injectJs("lib/jq.md5.js");
	console.log("Status: " + status);
	if (status === "success") {
		var result = page.evaluate(function() {
			var uuid =  GetUUID();
			var $items = jQuery('div.fleft').find('div.article');
			var rec = [];
			$items.each(function(i) {
				var $item = $(this);
				var $title = $item.find('div.hd h2');
				var title = $title.text();
				var $cont = $item.find('div.item_cnt p');
				var href  = $item.find('div.hd a').attr('href');
				var $img = $item.find('div.item_cnt img');
				var imgs = [];
				var catId = 37; // 37段子，38图片
				var note;
				if ($img) {
					var img = ImgTools.GetOrgiPicUrl_2345Xiaohua($img.attr('data-original'));
					if(img!=''){
						catId = 38 ;
						imgs.push(img);
					}
				}
				if($cont && $cont.text()!=''){
					note = $cont.text();
				}
				if(title!='' && title.length>2){
					rec.push({
						'recordMd5Value': $.md5(title+href),
						'title': title,
						'summary' : note,
						'imgs': imgs,
						'srcUrl': 'http://day.2345.com'+href,
						'catId' : catId,
						'authorId' : 10013
					});
				}
			});
			var clientPost = {};
			clientPost['records'] = rec;
			clientPost['entityMd5Value'] = $.md5(JSON.stringify(rec));
			clientPost['requestId'] = uuid;
			return clientPost;
		});
		PostCrawlResult(result);
	}
});
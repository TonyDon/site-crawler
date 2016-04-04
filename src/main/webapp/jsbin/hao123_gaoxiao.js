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
		"User-Agent": "Mozilla/5.0 (Linux; Android 4.3; Nexus 10 Build/JSS15Q) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.76 Safari/537.36",
		"Host": 'www.hao123.com',
		"Referer": 'http://www.hao123.com/'
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
	if ((/.+?(\.jpg|\.gif|\.png)/gi).test(requestData['url']) || requestData.headers['Content-Type'].indexOf('image') !== -1) {
		console.log('The url of the request is matching. Aborting: ' + requestData['url']);
		request.abort();
	}
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
	page.open('http://986001.com/site-crawler/outerSiteInfoHandler/gaosiao', settings, function(status) {
		if (status !== 'success') {
			console.log('Unable to post!');
		} else {
			console.log(page.content);
		}
		phantom.exit();
	});
};


page.open('http://www.hao123.com/gaoxiao?pn=1', settings, function(status) {
	page.injectJs("tool.js");
	page.injectJs("jquery.js");
	page.injectJs("uuid.js");
	page.injectJs("jq.md5.js");
	console.log("Status: " + status);
	if (status === "success") {
		var result = page.evaluate(function() {
			var uuid =  GetUUID();
			var $items = jQuery('div.gx-items').find('div[selector="item"].gx-item');
			var rec = [];
			$items.each(function(i) {
				var $item = $(this);
				var $title = $item.find('a.title');
				var title = $title.text();
				var href  = $title.attr('href');
				var $img = $item.find('img[selector="pic"]');
				var imgs = [];
				var catId = 37; // 37段子，38图片
				if ($img) {
					var img = ImgTools.GetOrgiPicUrl4Hao123($img.attr('img-src'));
					if(img!=''){
						catId = 38 ;
						imgs.push(img);
					}
				}
				rec.push({
					'recordMd5Value': $.md5(title+href),
					'title': title,
					'imgs': imgs,
					'srcUrl':'http://www.hao123.com/'+href,
					'catId' : catId
				});
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
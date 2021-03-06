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
		"User-Agent": "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1",
		"Host": 'm.haha.sogou.com',
		"Referer": 'http://m.haha.sogou.com/new/'
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
page.onInitialized = function() {
	console.log("page.onInitialized");
};
page.onLoadStarted = function() {
	console.log("page.onLoadStarted");
};
page.onLoadFinished = function() {
	console.log("page.onLoadFinished");
};
page.onUrlChanged = function() {
	console.log("page.onUrlChanged");
};
page.onNavigationRequested = function() {
	console.log("page.onNavigationRequested");
};
page.onRepaintRequested = function() {
	console.log("page.onRepaintRequested");
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
page.onResourceReceived = function(res) {
	console.log('received: ' + res.url);
};
page.onError = function(msg, trace) {
  var msgStack = ['ERROR: ' + msg];
  if (trace && trace.length) {
    msgStack.push('TRACE:');
    trace.forEach(function(t) {
      msgStack.push(' -> ' + t.file + ': ' + t.line + (t.function ? ' (in function "' + t.function +'")' : ''));
    });
  }
  console.error(msgStack.join('\n'));
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
//http://m.haha.sogou.com/img/ http://m.haha.sogou.com/video/
page.open('http://m.haha.sogou.com/text/', settings, function(status) {
	page.injectJs("lib/tool.js");
	page.injectJs("lib/jquery.js");
	page.injectJs("lib/uuid.js");
	page.injectJs("lib/jq.md5.js");
	console.log("Status: " + status);
	if (status === "success") {
		var result = page.evaluate(function() {
			var uuid =  GetUUID();
			var $items = jQuery('div.item_list').find('div.item');
			var rec = [];
			$items.each(function(i) {
				var $item = $(this);
				var $title = $item.find('h2.tit a');
				var title = $title.text();
				var href = $title.attr('href');
				var note = $item.find('p.info a').html();
				var $img = $item.find('p.img>a>img');
				var $vid = $item.find('div.article_video>div>video');
				var imgs = [];
				var catId = 37; // 37段子，38图片,39视频
				var vidurl;
				if ($img) {
					var img = ImgTools.GetOrgiPicUrl4Sogou($img.attr('src'));
					if(img!=''){
						catId = 38 ;
						imgs.push(img);
					}
				}
				if($vid){
					var img = ImgTools.GetOrgiPicUrl4Sogou($vid.attr('poster'));
					if(img!=''){
						imgs.push(img);
					}
					vidurl = $vid.attr('src');
					if(vidurl){
						catId = 39 ;
					}
				}
				rec.push({
					'recordMd5Value': $.md5(title+href),
					'title': title,
					'summary': note || '',
					'imgs': imgs,
					'videoUrl':vidurl || '',
					'srcUrl':'http://m.haha.sogou.com'+href,
					'catId' : catId,
					'authorId' : 10006
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




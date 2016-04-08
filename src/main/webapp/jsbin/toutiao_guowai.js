"use strict";
var page = require('webpage').create();
var fs = require('fs');
var settings = {
	operation: 'GET',
	encoding: 'utf8',
	resourceTimeout: 8000,
	headers: {
		"Content-Type": "text/html; charset=utf-8",
		"Accept": "	text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
		"User-Agent": "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:24.0) Gecko/20100101 Firefox/24.0",
		"Host": 'toutiao.com',
		"Referer": 'http://toutiao.com/',
		"Connection" : 'keep-alive'
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
		//console.log('The url of the request is matching. Aborting: ' + requestData['url']);
		request.abort();
	}
	if ((/http:\/\/.+?\.baidu\.com/gi).test(requestData['url'])) {
		//console.log('The url of the request is matching. Aborting: ' + requestData['url']);
		request.abort();
	}
    if ((/http:\/\/.+?google/gi).test(requestData['url'])) {
	//console.log('The url of the request is matching. Aborting: ' + requestData['url']);
	request.abort();
  }
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

page.open('http://toutiao.com/a6270808606888182017/', settings, function(status) {
	page.injectJs("tool.js");
	page.injectJs("jquery.js");
	page.injectJs("uuid.js");
	page.injectJs("jq.md5.js");
	page.injectJs("common.js");
	console.log("Status: " + status);
	if (status === "success") {
		var result = page.evaluate(function() {
			var tgetUrl = 'http://toutiao.com/a6270808606888182017/';
			var uuid = GetUUID();
			var rec = [];
			var title = $('h1.title').text();
			var $content = $('div.article-content');
			var $summary = $('div.article-content p:lt(3)');
			var $img = $content.find('img:eq(0)');
			var catId = 28; // 28看世界
			var imgs = [];
			var note = '';
			if ($img) {
				var img =  $img.attr('src');
				if (img != '') {
					imgs.push(img);
				}
			}
			if($summary){
				note = ut.cutstr($summary.text(), 100);
			}
			$content.find('img:eq(0)').remove();
			$('div.article-content p:has("img")').addClass('text-center');
			$('div.article-content>img').wrap("<p class='text-center'></p>");
			$content.find('img').removeAttr('img_height').removeAttr('img_width').removeAttr('onerror');
			var content = $content.html().replace(/\s{2,}/g,'');
			rec.push({
				'recordMd5Value' : $.md5(tgetUrl),
				'title' : title,
				'summary' : note,
				'imgs' : imgs,
				'srcUrl' : tgetUrl,
				'catId' : catId,
				'content' : content
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
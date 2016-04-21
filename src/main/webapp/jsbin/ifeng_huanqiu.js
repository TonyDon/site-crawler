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
		"Host": 'news.ifeng.com',
		"Referer": 'http://news.ifeng.com/',
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
	if ((/http:\/\/.+?\.baidu/gi).test(requestData['url'])) {
		//console.log('The url of the request is matching. Aborting: ' + requestData['url']);
		request.abort();
	}
    if ((/http:\/\/.+?google/gi).test(requestData['url'])) {
	//console.log('The url of the request is matching. Aborting: ' + requestData['url']);
	request.abort();
  }
    if ((/http:\/\/.+?revsci/gi).test(requestData['url'])) {
    	//console.log('The url of the request is matching. Aborting: ' + requestData['url']);
    	request.abort();
      }
};
page.onResourceReceived = function(res) {
	var url = res.url
	console.log('received: ' + url.substring(0,32));
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

page.open('http://news.ifeng.com/a/20160419/48504911_0.shtml#p=1', settings, function(status) {
	page.injectJs("lib/tool.js");
	page.injectJs("lib/jquery.js");
	page.injectJs("lib/uuid.js");
	page.injectJs("lib/jq.md5.js");
	page.injectJs("lib/common.js");
	console.log("Status: " + status);
	if (status === "success") {
		var result = page.evaluate(function() {
			var tgetUrl = 'http://news.ifeng.com/a/20160419/48504911_0.shtml#p=1';
			var uuid = GetUUID();
			var rec = [];
			var title = $('div.titL>h1').text();
			var that_listdata = window.G_listdata;
			var catId = 28; // 28看世界
			var imgs = [];
			var note = '';
			var cont = [];
			if(that_listdata && that_listdata.length>0){
				var img = that_listdata[0].big_img;
				if(img && img != ''){
					imgs.push(img);
					note = ut.cutstr(that_listdata[0].title, 100);
					cont.push('<p>'+that_listdata[0].title+'</p>');
				}
				
				for(var i=1; i<that_listdata.length; i++){
					var img = that_listdata[i].big_img;
					var tit = that_listdata[i].title || '';
					if(img && img != ''){
						imgs.push(img);
						cont.push('<p>$IMGS#'+i+'$</p><p>'+tit+'</p>');
					}
				}
			}

			rec.push({
				'recordMd5Value' : $.md5(tgetUrl),
				'title' : title,
				'summary' : note,
				'imgs' : imgs,
				'srcUrl' : tgetUrl,
				'catId' : catId,
				'content' : cont.join(''),
				'authorId' : 10007
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
/**
 * (new Date(ms)).Pattern('yyyy-MM-dd')
 */
;Date.prototype.Pattern=function(fmt){
    var o = {
    "M+" : this.getMonth()+1,
    "d+" : this.getDate(), 
    "h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12,
    "H+" : this.getHours(), 
    "m+" : this.getMinutes(), 
    "s+" : this.getSeconds(), 
    "q+" : Math.floor((this.getMonth()+3)/3), 
    "S" : this.getMilliseconds()
    };
    var week = {"0" : "\u65e5","1" : "\u4e00","2" : "\u4e8c","3" : "\u4e09","4" : "\u56db","5" : "\u4e94","6" : "\u516d"};
    if(/(y+)/.test(fmt)){fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));}
    if(/(E+)/.test(fmt)){fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "\u661f\u671f" : "\u5468") : "")+week[this.getDay()+""]);}
    for(var k in o){
      if(new RegExp("("+ k +")").test(fmt)){
         fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
      }
    }
    return fmt;
};
var ut = {
    e:function(id){var obj = document.getElementById(id);if(va.isnull(obj)){return null;}return obj;},
    n:function(na){return document.getElementsByName(na);},
    v:function(id){return ut.trim(ut.e(id).value);},
    h:function(id){return ut.e(id).innerHTML;},
    html:function(id,s){ut.e(id).innerHTML=s;},
    m:function(s){alert(s);},
    show:function(s){var ida=s.toString().split(' ');for(var i=0; i<ida.length; i++)this.e(ida[i]).style.display='block';},
    hide:function(s){var ida=s.toString().split(' ');for(var i=0; i<ida.length; i++)this.e(ida[i]).style.display='none';},
    swap:function(b){var v=ut.e(b).style.display;ut.e(b).style.display=(v=='block')?'none':'block';},
    trim:function(s){return s.replace(/^\s+|\s+$/g,"");},
    cutstr:function(s,m){return (s.length>m)?(s.substring(0,m-2)+'...'):(s);},
    dec:function(s){return decodeURIComponent(s);},
    enc:function(s){return encodeURIComponent(s);},
    reload:function(s){if(va.isnull(s)){location.reload();}else{location.replace(s);} },
    gopage:function(s){top.location.replace(s);return;},/* location.href=s; */
    get:function(q){
        var v="";var s = location.href;s = s.replace("?","?&").split("&");for(var i=1;i<s.length;i++){if(s[i].indexOf(q+"=")==0)v = s[i].replace(q+"=","");}v = ( typeof(v) == "undefined" )?'':v;if( v.indexOf("#")>=0)v = v.substring(0,v.indexOf("#"));return v;},
    delay:function(f){setTimeout(f,600);},
    wait:function(f,ms){return setTimeout(f,ms);},
    swapsel:function(na){var ds = ut.n(na);for(var i=0; i<ds.length; i++){ds[i].checked=ds[i].checked==true?false:true;}},
    getselv:function(na){var ds = ut.n(na);var sm =[];for(var i=0; i<ds.length; i++){if(ds[i].checked){sm.push(ds[i].value);}}return sm.join('-');},
    lower:function(s){return s.toLowerCase();},
    upper:function(s){return s.toUpperCase();},
    parseMs:function(datestr){
        if(!va.datetime(datestr)){return 0;}
        var d  = new Date();var d1 =  datestr.split(' ');var a = d1[0].split('-');var b = d1[1].split(':');
        d.setFullYear(Number(a[0]), Number(a[1])-1, Number(a[2]));d.setHours(Number(b[0]), Number(b[1]), Number(b[2]), 1);
        return d.getTime();
    },
    parseDate:function(ms,len){if(ms<10){return '';}else{
            var l = len || 19;
            var fmt = "yyyy-MM-dd HH:mm:ss".substring(0,l);
            return (new Date(ms)).Pattern(fmt);
        }},
    getTime:function(){
    var now = new Date();
    var year = now.getFullYear();
    var month = now.getMonth()+1;
    month=(parseInt(month)<10)?('0'+month.toString()):month;
    var day = now.getDate();
    day=(parseInt(day)<10)?('0'+day.toString()):day;
    var wd=["日","一","二","三","四","五","六"];
    return [year,"年",month,"月",day,"日  星期",wd[now.getDay()]].join('');
    },
    outpage:function(pn,r,cmd,pgid,ls,step){
	if(va.isnull(ut.e(pgid)) || !va.mint(pn))return;
        var rn = Number(r);
        var pc =(rn%ls==0)?parseInt(rn/ls):parseInt(rn/ls)+1;
        if(pn>pc || pn<1 || pc==1){ut.html(pgid, '');return;}
        var p1 = (pn==1)?1:(pn-1);
        var p2 = (pn<pc)?(pn+1):pc;
        var fp = (pn<2)?'':['<a href="#p" onclick="',cmd,'(1,',rn,');">首页</a> '].join('');
        var pre = (pn<=2)?'':['<a href="#p" onclick="',cmd,'(',p1,',',rn,');">上一页</a> '].join('');
        var nex = (pn>pc-2)?'':[' <a href="#p" onclick="',cmd,'(',p2,',',rn,');">下一页</a> '].join('');
        var ep = (pn>=pc)?'':['<a href="#p" onclick="',cmd,'(',pc,',',rn,');">末页</a> '].join('');
        var rt =[];
        var u =['<a href="#p" onclick="',cmd,'(@p,',rn,');">@p</a> '].join('');
        var du = '<a href="javascript:;" disabled="disabled" onclick="return false;" class="cur">@p</a> ';
        var go = [' 转到第<input type="text" size="4" onchange="',cmd,'(this.value,',rn,');" value="" maxlength="5"/>页 '].join('');
        if ( (pn<=pc) && (pn>=1) ){
        var k =(parseInt(pn/step))*step +1;
        if( k+step-1 <= pc ){
            for (var j=0 ;j<step; j++){
                if(k==pn){rt.push(du.replace(/@p/g,k));}else{rt.push(u.replace(/@p/g,k));}
                k++;}
        }else{
            for(var i=k ; i<= pc ; i++){ if(i==pn){rt.push(du.replace(/@p/g,i));}else{rt.push(u.replace(/@p/g,i));}}
        }
         }
       var pt =[fp,pre,rt.join(''),' <a href="javascript:;">共',pc,'页</a> ', nex, ep, go].join('') ;
        ut.html(pgid, pt);
},
	r:function(){return Math.random()*100;},
	rndint:function(b,e){return parseInt( b + parseInt( Math.random()*(e-b) ) );},
	doxCall:function(url,func){ 
        $.ajax({cache:false,url:url,dataType:"jsonp",jsonp:"jsonpcall",
                success:function(d){
                    func(d);
    }});},
    getFormData:function(frm){
         if(typeof(frm) == "string"){frm = jQuery(frm);}
	var datas = frm.serializeArray();
	datas =  jQuery.grep(datas, function(e){return e.value != "";});
	return jQuery.param(datas);
    },
    rndChr:function(len){
    	var _map = 'abcdefghijklmnopqrstuvwxyz0123456789';
    	var size = _map.length;
    	var len = len || 16;
    	var s=[];
    	for(var i=0; i<len; i++){
    		s.push(_map.charAt(ut.rndint(0,size)));
    	}
    	return s.join('');
    }
};
/* 格式化数值 */
ut.fmtNumber = function(number,pattern){
    var str            = number.toString();
    var strInt;
    var strFloat;
    var formatInt;
    var formatFloat;
    if(/\./g.test(pattern)){
        formatInt        = pattern.split('.')[0];
        formatFloat        = pattern.split('.')[1];
    }else{
        formatInt        = pattern;
        formatFloat        = null;
    }

    if(/\./g.test(str)){
        if(formatFloat!=null){
            var tempFloat    = Math.round(parseFloat('0.'+str.split('.')[1])*Math.pow(10,formatFloat.length))/Math.pow(10,formatFloat.length);
            strInt        = (Math.floor(number)+Math.floor(tempFloat)).toString();                
            strFloat    = /\./g.test(tempFloat.toString())?tempFloat.toString().split('.')[1]:'0';            
        }else{
            strInt        = Math.round(number).toString();
            strFloat    = '0';
        }
    }else{
        strInt        = str;
        strFloat    = '0';
    }
    if(formatInt!=null){
        var outputInt    = '';
        var zer        = formatInt.match(/0*$/)[0].length;
        var comma        = null;
        if(/,/g.test(formatInt)){
            comma        = formatInt.match(/,[^,]*/)[0].length-1;
        }
        var newReg        = new RegExp('(\\d{'+comma+'})','g');

        if(strInt.length<zero){
            outputInt        = new Array(zer+1).join('0')+strInt;
            outputInt        = outputInt.substr(outputInt.length-zer,zer);
        }else{
            outputInt        = strInt;
        }

        outputInt            = outputInt.substr(0,outputInt.length%comma)+outputInt.substring(outputInt.length%comma).replace(newReg,(comma!=null?',':'')+'$1');
        outputInt            = outputInt.replace(/^,/,'');

        strInt    = outputInt;
    }
    if(formatFloat!=null){
        var outputFloat    = '';
        var zero        = formatFloat.match(/^0*/)[0].length;

        if(strFloat.length<zero){
            outputFloat        = strFloat+new Array(zero+1).join('0');
            //outputFloat        = outputFloat.substring(0,formatFloat.length);
            var outputFloat1    = outputFloat.substring(0,zero);
            var outputFloat2    = outputFloat.substring(zero,formatFloat.length);
            outputFloat        = outputFloat1+outputFloat2.replace(/0*$/,'');
        }else{
            outputFloat        = strFloat.substring(0,formatFloat.length);
        }

        strFloat    = outputFloat;
    }else{
        if(pattern!='' || (pattern=='' && strFloat=='0')){
            strFloat    = '';
        }
    }
    return strInt+(strFloat==''?'':'.'+strFloat);
};
// 哈希客户端输入密码
ut.hashPassKey = function(pass, token){
	if(window.jQuery && pass!='' && token!=''){
		return  $.sha1([$.sha1(pass),token].join('@'));
	}
	return "";
};
/* 格式化缩图地址 */
ut.fmtImageThumb = function(picUrl, thumbWidth){
	var pos = picUrl.lastIndexOf('.');// abc.jpg
	var pre = picUrl.substring(0, pos);// abc
	var ext = picUrl.substring(pos+1); // jpg
	return [pre, thumbWidth, ext].join('.');
};
var va={
    tint:function(s){return (/^[0-9\-]{1,2}$/).test(s);},
    mint:function(s){return (/^[0-9\-]{1,5}$/).test(s);},
    isint:function(s){return (/^[0-9\-]{1,8}$/).test(s);},
    islong:function(s){return (/^[0-9\-]{1,20}$/).test(s);},
    joinstr:function(s){return (/^[0-9a-zA-Z,\-]{1,215}$/).test(s);},
    price:function(s){return (/^([0-9\\-]{1,7})\.([0-9]{1,2})$/).test(s);},
    email:function(s){return (/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/).test(s);},
    key:function(s){return (/^[^\s]{6,20}$/).test(s);},
    name:function(s){return (/^[a-zA-Z0-9\s,\/\-\u4E00-\u9FA5]{2,64}$/).test(s);},
    title:function(s){return (/^[^=<>]{2,64}$/).test(s);},
    tel:function(s){return (/^((\(\d{2,3}\))|(\d{3}\-))?(13|15|18)\d{9}$/).test(s);},
    phone:function(s){return (/^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/).test(s);},
    nick:function(s){return (/^[a-zA-Z0-9\u4E00-\u9FA5]{2,16}$/).test(s);},
    date:function(s){return (/^([0-9]{4})-([0-9]{2})-([0-9]{2})$/).test(s);},
    datetime:function(s){return (/^([0-9]{4})-([0-9]{2})-([0-9]{2})\s([0-9]{1,2}):([0-9]{1,2}):([0-9]{1,2})$/).test(s);},
    pic:function(s){return (/^.+.(jpg|png|gif)$/i).test(s);},
    url:function(s){return (/^http[s]?:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/i).test(s);},
    isnull:function(exp){if(typeof(exp)=="undefined"){return true;}if(exp===0 ){return false;}else if(exp==null || !exp  || exp=='' || exp=='null'){return true;}else{return false;}},
    krate:function(s){return s.replace(/^(?:(?=.{4})(?=.*([a-z])|.)(?=.*([A-Z])|.)(?=.*(\d)|.)(?=.*(\W)|.).*|.*)$/, "$1$2$3$4").length;}
};
var filter={
    html:function(s){
        if(va.isnull(s))return '';
        var t=ut.trim(s);
        return t.replace(/&/g,'&amp;').replace(/\"/g,'&quot;').replace(/\'/g,'&#39;').replace(/。/g,'.').replace(/[，、]{1,}/g,',').replace(/…/g,'.').replace(/；/g,';').replace(/：/g,':').replace(/？/g,'?').replace(/（/g,'(').replace(/）/g,')').replace(/！/g,'!').replace(/[〈<（]{1,}/g,'&lt;').replace(/[〉>）]{1,}/g,'&gt;').replace(/[\r\f\t]{1,}/g,'').replace(/[\n]{1,}/g,'<br>');
    },
    txt:function(s){
      if(va.isnull(s))return '';
      var t=ut.trim(s);
      return t.replace(/&amp;/g,'&').replace(/&quot;/g,'"').replace(/&#39;/g,'\'').replace(/&lt;/g,'<').replace(/&gt;/g,'>').replace(/<br>/g,'\n');
    },
    script:function(s){
        if(va.isnull(s))return '';
        var t=ut.trim(s);
	return t.replace(/<\/?script[^>]*>/gi,"").replace(/(javascript|jscript|vbscript|vbs|xss):/gi,"").replace(/exp(\w+)/gi,"").replace(/on(.{3,20})=/gi,"").replace(/[\r\n\f\t\b]{1,}/g,"");
    },
    clrxml:function(s){
        if(va.isnull(s))return '';var t=ut.trim(s);
        return t.replace(/<[^>]+>/g,"").replace(/[\f\t\b]{1,}/g,"");
    }
};
var J = {
    toJson: function(str) {var a;if(va.isnull(str)){return null;}else{eval('a=' + str.toString() + ';');return a;}},
    toStr: function(obj) {
        switch(typeof(obj))
        {
            case 'object':
                var ret = [];
                if (obj instanceof Array){
                    for (var i = 0, len = obj.length; i < len; i++){ret.push(J.toStr(obj[i]));}
                    return '[' + ret.join(',') + ']';
                }
                else if (obj instanceof RegExp){
                    return obj.toString();
                }else{
                    for (var a in obj){
                        ret.push(a + ':' + J.toStr(obj[a]));
                    }
                    return '{' + ret.join(',') + '}';
                }
            case 'function':return 'function(){}';
            case 'number':return obj.toString();
            case 'string':return "\"" + obj.replace(/(\\|\")/g, "\\$1").replace(/\n|\r|\t/g, function(a) {return ("\n"==a)?"\\n":("\r"==a)?"\\r":("\t"==a)?"\\t":"";}) + "\"";
            case 'boolean':return obj.toString();
            default:return obj.toString();
        }
    }
};
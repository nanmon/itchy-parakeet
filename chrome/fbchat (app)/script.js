/*
	update;
	hasCount number;
	profile _blank;
	onNewMsg(drawAttention);
	autoscrollOnChat;n
	options{
		onNewMsg.OpenChat
		
	}
*/
window.onload=function(){
	var homeport = document.getElementsByClassName("_5lut _5luu")[0].href;
	if(!document.getElementById("messageGroup") && !document.getElementById("composer_form")){	
		document.getElementById("header").style.display = "-webkit-box";
		document.getElementById("viewport").style.top = "42px";
		var msgjwl = document.getElementById("messages_jewel");
		var msgclss = msgjwl.firstChild.className;
		msgjwl.innerHTML = "<a href='/messages' class ="+msgclss+"></a>";
		var rqstjwl = document.getElementById("requests_jewel")
		var rqstclss = rqstjwl.firstChild.className;
		rqstjwl.innerHTML = "<a href='/buddylist' class="+rqstclss+"></a>";
		window.onkeydown = function(e){
			if(e.keyCode==9) e.preventDefault();
		}
	}else{
		var link = document.createElement("link");
		link.rel = "shortcut icon";
		link.href=chrome.extension.getURL("inactive.png");
		document.head.appendChild(link);
		window.scrollTo(0, window.innerHeight);
		var composer = document.getElementById("composerInput");
		composer.focus();
		composer.onkeydown = function(e){
			if(e.keyCode==13){
				e.preventDefault();
				document.getElementById("u_0_8").click();
			}if(e.keyCode==9){
				e.preventDefault();
				if(e.shiftKey) chrome.runtime.sendMessage({changeChat:true, next: false});
				else chrome.runtime.sendMessage({changeChat:true, next: true});
			}
		}
		var msglength = document.getElementById("messageGroup").childNodes.length
		setInterval(function(){
			if(document.getElementById("messageGroup").childNodes.length!=msglength){
				if(document.getElementById("messageGroup").lastChild.firstChild.firstChild.href.indexOf(homeport)==-1){
					chrome.runtime.sendMessage({newmsg: true});
				}
				msglength=document.getElementById("messageGroup").childNodes.length;
				window.scroll(0,document.getElementById("root").scrollHeight);
			}if(document.getElementById("root").firstChild.childNodes[1].firstChild.style.display!="none")
				document.head.lastChild.href=chrome.extension.getURL("active.png");
			else document.head.lastChild.href=chrome.extension.getURL("inactive.png");
		}, 2000)
	}
}

window.onmousemove = function(e){
	if(!e.ctrlKey)
	chrome.runtime.sendMessage({focus: true}, function(resp){
		if(resp.itIs){
			if(e.y>85){
				var wh = window.innerHeight-85;
				var sh = document.getElementById("root").scrollHeight;
				window.scroll(0, (e.y-85)*sh/wh);
			}else window.scroll(0,0);
		}
	});
}

window.ondragstart = function(e){
	e.preventDefault();
}

window.oncontextmenu = function(e){
	e.preventDefault();
}

window.onkeydown = function(e){
	if(e.keyCode==27) window.close();
}

window.onmousedown = function(mde){
	if(!mde.button){
		var blist;
		if(document.getElementById("mobile_buddylist") || document.getElementById("threadlist_rows"))
			setInterval(function(){
					document.getElementById("viewport").style.top = "42px";
			}, 2000);
		else{
			var list = document.getElementById("messageGroup").childNodes[1];
			list.onmouseup=function(e){
				var achild=e.toElement;
				for(;achild.parentNode!=list; achild=achild.parentNode);
				elem = achild;
				if(achild = achild.getElementsByClassName("darkTouch")[0]){
					achild.href = "https://www.facebook.com/"+achild.href;
					achild.target = "_blank";
				}
			}
		}
		if(blist = document.getElementById("mobile_buddylist"))
			UrlMessage(blist, "touchable primary");
		else if(document.getElementById("threadlist_rows")){
			blist = document.getElementById("threadlist_rows").firstChild;
			UrlMessage(blist, "_5b6s")
		}else{
		
		}
	}
}

function UrlMessage(list, a){
	list.onmouseup=function(e){
		var achild=e.toElement;
		for(;achild.parentNode!=list; achild=achild.parentNode);
		elem = achild;
		if(achild = achild.getElementsByClassName(a)[0]){
			var _url= achild.href;
			var _name = elem.getElementsByTagName("strong")[0].textContent;
			achild.href="#";
			chrome.runtime.sendMessage({url: _url, name: _name});
			achild.onmousedown = function(){
				achild.href=_url;
			}
		}
	}
}
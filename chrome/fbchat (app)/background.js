(String.prototype.isInThere = function(str2){
	if(this.indexOf(str2)>-1 || str2.indexOf(this)>-1)
		return true;
});

var deckUrl = "https://m.facebook.com/buddylist";
var ventanas = [];

function addWindow(dtls){
	var doit = true, i=0;
	if(!dtls.name && ventanas[i]) doit = false;
	else if(dtls.name)
		for(i=1; i<ventanas.length; ++i)
			if(ventanas[i].name.isInThere(dtls.name)){
				doit = false;
				break;
			}
	if(doit)
		chrome.app.window.create(
			{
			url: dtls.url,
			type: "panel",
			height: dtls.name ? 330 : 600,
			width: dtls.name ? 270 : 250,
			focused: true
			}, 
			function(win){
				if(dtls.url==deckUrl) ventanas[0]=win;
				else {
					ventanas.push(win);
					ventanas[ventanas.length-1].name = dtls.name;
				}
			}
		);
	else {
		chrome.app.window.get(ventanas[i].id).focus();
	}
}

function onRemove(){
	chrome.app.window.onClosed.addListener(function(win){
		if(ventanas[0] && ventanas[0].id==win) delete ventanas[0];
		else for(var i=1; i<ventanas.length; ++i)
			if(ventanas[i].id==win){
				ventanas.splice(i,1);
				if(!ventanas[i]) --i;
				chrome.windows.update(ventanas[i].id,{focused: true});
				break;
			}
	})
};

function Urlfier(url){
	var rUrl = url.substring(0, url.indexOf("?"));
	return rUrl;
}

function onMessage(){
	chrome.runtime.onMessage.addListener(function(msg, sender, sendResponse){
		if(msg.url) addWindow(msg);
		if(msg.focus){ 
			chrome.windows.get(sender.tab.windowId, function(win){
				sendResponse({itIs: win.focused});
			});
			return true;
		}if(msg.newmsg){ 
			chrome.windows.get(sender.tab.windowId, function(win){
				if(!win.focused){
					chrome.windows.update(sender.tab.windowId, {drawAttention: true});
				}
			});
		}if(msg.changeChat){
			var i=1, changeTo;
			for( ;ventanas[i].id!=sender.tab.windowId; ++i);
			if(msg.next) changeTo = i==ventanas.length-1 ? 1 : i+1;
			else changeTo = i==1 ? ventanas.length-1 : i-1;
			chrome.windows.update(ventanas[changeTo].id, {focused: true});
		}
	});
}

function checkNotifications() {
  var xhr=new XMLHttpRequest();
  xhr.open('GET','https://www.facebook.com/home.php',true);
  xhr.onreadystatechange=function(){
    if(xhr.readyState==4){
      var xmlDoc=xhr.responseText;

      if(xmlDoc.indexOf('notificationsCountValue') > 0){
        var loc=xmlDoc.indexOf('messagesCountValue');
        if(loc>0){
          var myString=xmlDoc.substr(loc, 80);
          var c = (myString.substring(myString.indexOf('>')+1,myString.indexOf('<')));
          chrome.browserAction.setBadgeText({text: (c=="0"? "" : c)});
        }
      }
    }
    else return;
  };
  xhr.send(null);
  //window.clearTimeout(timerVar);
  //timerVar=window.setTimeout(loadData,timerDelay);
  setTimeout(function() {
    checkNotifications();
  }, 3000);
}
////////////////main
chrome.app.runtime.onLaunched.addListener(function(){
	addWindow({url: deckUrl, name: ""});
});
onRemove();
onMessage();
checkNotifications();

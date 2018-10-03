function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_showHideLayers() { //v6.0
  var i,p,v,obj,args=MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) if ((obj=MM_findObj(args[i]))!=null) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v=='hide')?'hidden':v; }
	    if (v!='bool') {
			obj.visibility=v;
		}
		else if (obj.visibility=='visible') {
			obj.visibility='hidden';
		}
		else if (obj.visibility=='hidden' || obj.visibility=='') {
			obj.visibility='visible';
		}
	}
}

function setActiveStyleSheet(title, cat) {
  var i, a, main;
  for(i=0; (a = document.getElementsByTagName("link")[i]); i++) {
    if(a.getAttribute("cat")==cat && a.getAttribute("rel").indexOf("style") != -1 && a.getAttribute("title")) {
      a.disabled = true;
      if(a.getAttribute("cat")==cat &&  a.getAttribute("title") == title) a.disabled = false;
    }
  }
  setActiveStyle(title, cat);
  createCookie(cat, title, 365);
}

function setActiveStyle(title, cat) {
  var i, a;
  for(i=0; (a = document.getElementsByTagName("a")[i]); i++) {
    if(a.getAttribute("cat")==cat && a.getAttribute("id")) {
      if(a.getAttribute("id") == title) {
	    a.className="here"; 
	  }
	  else {
	    a.className=""; 
	  }
    }
  }
  for(i=0; (a = document.getElementsByTagName("select")[i]); i++) {
    if(a.getAttribute("cat")==cat) {
		var options = a.options
	    for (i=0; i < options.length; i++) {
	        if (options[i].value==title)
				options[i].selected = true;
	    }
    }
  }
}

function getActiveStyleSheet(cat) {
  var i, a;
  for(i=0; (a = document.getElementsByTagName("link")[i]); i++) {
    if(a.getAttribute("cat")==cat && a.getAttribute("rel").indexOf("style") != -1 && a.getAttribute("title") && !a.disabled) return a.getAttribute("title");
  }
  return null;
}

function getPreferredStyleSheet(cat) {
  var i, a;
  for(i=0; (a = document.getElementsByTagName("link")[i]); i++) {
    if(a.getAttribute("cat")==cat && a.getAttribute("rel").indexOf("style") != -1
       && a.getAttribute("rel").indexOf("alt") == -1
       && a.getAttribute("title")
       ) return a.getAttribute("title");
  }
  return null;
}

function createCookie(name,value,days) {
  if (days) {
    var date = new Date();
    date.setTime(date.getTime()+(days*24*60*60*1000));
    var expires = "; expires="+date.toGMTString();
  }
  else expires = "";
  document.cookie = name+"="+value+expires+"; path=/";
}

function readCookie(name) {
  var nameEQ = name + "=";
  var ca = document.cookie.split(';');
  for(var i=0;i < ca.length;i++) {
    var c = ca[i];
    while (c.charAt(0)==' ') c = c.substring(1,c.length);
    if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
  }
  return null;
}

function setAllStyleCat() {
	var i, a;
	for(i=0; (a = document.getElementsByTagName("styleSwitcher")[i]); i++) {
		if(a.getAttribute("cat")) {
			var cookie = readCookie(a.getAttribute("cat"));
			var title = cookie ? cookie : getPreferredStyleSheet(a.getAttribute("cat"));
			setActiveStyleSheet(title, a.getAttribute("cat"));
		}
	}
}

function createAllStyleCatCookie() {
	var i, a;
	for(i=0; (a = document.getElementsByTagName("styleSwitcher")[i]); i++) {
		if(a.getAttribute("cat")) {
			var title = getActiveStyleSheet(a.getAttribute("cat"));
			createCookie(a.getAttribute("cat"), title, 365);
		}
	}
}

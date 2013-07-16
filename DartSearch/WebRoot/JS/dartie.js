function openWin(htmUrl,awidth,aheight)
{
var url=htmUrl;
var winName="newWin";
var atop=screen.availHeight - aheight - 50;
var aleft=screen.availWidth - awidth - 50;  
var param0="scrollbars=0,status=0,menubar=0,resizable=2,location=0";  
var params="top=" + atop + ",left=" + aleft + ",width=" + awidth + ",height=" + aheight + "," + param0 ; 
win=window.open(url,winName,params);
win.focus();
}
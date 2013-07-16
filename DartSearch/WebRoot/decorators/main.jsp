<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"  %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<%=request.getContextPath()%>/css/Global.css" rel="stylesheet" type="text/css">
<title><decorator:title default="中医药数据网格" /></title>
<decorator:head />
</head>


<body onload='initPage()'>
<table width="100%" align="top"  border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td bgcolor="ffffff">	     
        <page:applyDecorator page="/title.htm" name="blank" />
    </td>
  </tr>
  <tr width="100%">
    <td bgcolor="ffffff" width="100%">
    	<table width="100%"  border="0" cellpadding="0" cellspacing="0" bgcolor="31430F" >
	      <tr bgcolor="ffffff"  valign="top" id="myCategory">
	        <!--
	        <td width="150" valign="top" >
	           </BR>
	           <page:applyDecorator page="/metaCategory.htm" name="blank" />
	        </td> 
	        //--> 
            
	        <td width="150" valign="top"  class="ms-navframe" >
	           <!--
	           <div id="myNarrow">
	           <img onclick="hideCategory()" onmouseover="this.style.cursor='hand'" src="<%=request.getContextPath()%>/images/I001_LeftNarrow.GIF"/>	           
               </div>
               //-->
	           <page:applyDecorator page="/navigator.htm" name="blank" />
	        </td>
	        <td class="ms-bodyareaframe"><decorator:body /></td>
	         <td width="150" valign="top"  class="navbar" >
	           <page:applyDecorator page="/dartQueryInfo.htm" name="blank" />
	        </td>
	      </tr>
    	</table>
    </td>
  </tr>
<!--  <tr>
    <td bgcolor="ffffff"><page:applyDecorator page="/footer.htm" name="blank" /> </td>
  </tr>-->
</table>
</body>

<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/DwrSession.js'></script>
<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/engine.js'></script>
<script type="text/javascript">
<!--
var reserved=null;
var VAR_NAME='Main_ShowCategory';

function hideCategory()
{
if(reserved==null)
{
  var node= document.getElementById("myCategory");
  reserved=node.removeChild(node.childNodes[0]);
  var narrow= document.getElementById("myNarrow");
  narrow.innerHTML="<img onclick='hideCategory()' onmouseover=this.style.cursor='hand' src='<%=request.getContextPath()%>/images/I001_RightNarrow.GIF'/>";
  DwrSession.putString(null,VAR_NAME,"false");
}
else
{
  var node= document.getElementById("myCategory");
  node.insertBefore(reserved,node.childNodes[0]);  
  reserved=null;
  var narrow= document.getElementById("myNarrow");
  narrow.innerHTML="<img onclick='hideCategory()' onmouseover=this.style.cursor='hand' src='<%=request.getContextPath()%>/images/I001_LeftNarrow.GIF'/>";;
  DwrSession.putString(null,VAR_NAME,"true");
}
}

function f(show)
{
 if(show=="false")
    hideCategory();
}


function initPage()
{
 DwrSession.getString(f,VAR_NAME);
}
//-->
</script>
		
</html>
	
				
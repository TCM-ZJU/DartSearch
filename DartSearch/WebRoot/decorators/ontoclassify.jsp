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

<body>
<table width="100%" align="top"  border="0" cellpadding="0" cellspacing="0">
<!--  <tr>
    <td bgcolor="ffffff">
      <page:applyDecorator page="/banner.htm" name="blank" />
    </td>
  </tr>-->
  <tr>
    <td bgcolor="ffffff">	     
        <page:applyDecorator page="/title.htm" name="blank" />
    </td>
  </tr>
  <tr width="100%">
    <td bgcolor="ffffff" width="100%">
    	<table width="100%"  border="0" cellpadding="0" cellspacing="0" bgcolor="31430F">
	      <tr bgcolor="ffffff"  valign="top">
	        <td width="150" valign="top">
	           <page:applyDecorator page="/metaCategory.cla" name="blank" />
	        </td>    	
	        <td class="ms-bodyareaframe"><decorator:body /></td>
	      </tr>
    	</table>
    </td>
  </tr>
<!--  <tr>
    <td bgcolor="ffffff"><page:applyDecorator page="/footer.htm" name="blank" /> </td>
  </tr>-->
</table>
</body>
</html>
	
				
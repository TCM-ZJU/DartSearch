<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"  %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html xmlns:v="urn:schemas-microsoft-com:vml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<%=request.getContextPath()%>/css/search.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath()%>/css/index.css" rel="stylesheet" type="text/css">
<title><decorator:title default="中医药数据全文检索" /></title>
<decorator:head />
</head>

<body>
<div id="container">
<table width="100%" align="top" valign="top"  border="0" cellpadding="0" cellspacing="0">
<!--  <tr>
    <td bgcolor="ffffff">
      <page:applyDecorator page="/banner.htm" name="blank" />
    </td>
  </tr>-->
  <tr>
	 <td class="ms-bodyareaframe"><decorator:body/></td>
  </tr>
<!--  <tr>
    <td bgcolor="ffffff"><page:applyDecorator page="/footer.htm" name="blank" /> </td>
  </tr>-->
</table>
</div>
</body>

</html>
	
				
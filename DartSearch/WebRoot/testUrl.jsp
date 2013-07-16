<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>Insert title here</title>
</head>
<body>


<%@   page   contentType="text/html;   charset=gb2312"     %>   
  <%@   page   language="java"   import="java.io.*,java.net.*"   errorPage=""   %>   
  <%   
  String   str=request.getParameter("http://www.baidu.com");   
  try{   
  URL   url=new   URL("http://www.baidu.com");   
  BufferedReader   br=new   BufferedReader(new   InputStreamReader(url.openStream()));   
  String   s=br.readLine();   
  while(s!=null){   
  out.println   (s);   
  s=br.readLine();   
  }   
  }   
  catch(Exception   e){   
  out.println(e);   
  }   
  %>  

</body>
</html>
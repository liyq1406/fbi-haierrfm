<%--
*******************************************************************
*    ��������:    main.jsp
*    �����ʶ:
*    ��������:    ���������Ļ�ӭ���档
*    ������ҳ:    �ൺ��
*    ���ݲ���:
*    ��   ��:
*    ��������:    2003-05-29
*    �� �� ��:
*    �޸�����:
*    ��   Ȩ:   leonwoo
*
*
*******************************************************************
--%>
<%@ page contentType="text/html; charset=gb2312"%>
<%@page import="pub.platform.security.OperatorManager"%>
<%@page import="pub.platform.form.config.SystemAttributeNames"%>
<%@page import="pub.platform.db.DBGrid"%>
<%@page import="pub.platform.html.ZtSelect"%>
<%@page import="java.util.*"%>
<%
OperatorManager omgr = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);

    //TODO ���ȼ���Ƿ��Ѵ��ڴ�cookie
    Cookie cookie = new Cookie("usernamecookie", "aaa1");
    cookie.setMaxAge(365 * 24 * 60 * 60); //����365��
    cookie.setPath( request.getContextPath() + "/pages");
    response.addCookie(cookie);
    cookie = new Cookie("passwordcookie", "bbb");
    cookie.setPath("/pages");
    cookie.setMaxAge(365 * 24 * 60 * 60); //����365��
    response.addCookie(cookie);


%>
<html>
  <head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
    <link href="../../css/ccb.css" rel="stylesheet" type="text/css">
  </head>
  <body style="margin:0px;padding:0px" class="Bodydefault">

     asdasd
  </body>
</html>

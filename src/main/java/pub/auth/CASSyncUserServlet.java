package pub.auth;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import platform.repository.dao.PtoperMapper;
import platform.repository.model.Ptoper;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 11-7-29
 * Time: ����3:00
 * To change this template use File | Settings | File Templates.
 */
public class CASSyncUserServlet extends HttpServlet {
    private static Logger logger = LoggerFactory.getLogger(CASSyncUserServlet.class);

    @Autowired
    private PtoperMapper ptoperMapper;

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        doGet(request, response);

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        ServletContext application = getServletContext();
        WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(application);//��ȡspring��context
        ptoperMapper = (PtoperMapper) wac.getBean("ptoperMapper");

        request.setCharacterEncoding("gbk");
        String txncode = request.getParameter("txncode");
        String usercode = request.getParameter("Usercode");
        String fullname = request.getParameter("Fullname");
        String password = request.getParameter("Password");
        String createtime = request.getParameter("CreateTime");
        String updatetime = request.getParameter("UpdateTime");
        String userstate = request.getParameter("UserState");
        String deptcode = request.getParameter("DeptCode");
        String usermail = request.getParameter("Usermail"); //�ɿ�

        if (StringUtils.isEmpty(txncode)
                || StringUtils.isEmpty(usercode)
                || StringUtils.isEmpty(fullname)
                || StringUtils.isEmpty(userstate)
                || StringUtils.isEmpty(deptcode)
                ) {
            response.getOutputStream().print("3");
            return;
        }

        if (!"PTL002".equals(txncode)) {
            response.getOutputStream().print("3");
        }

        try {
            Ptoper ptoper = ptoperMapper.selectByPrimaryKey(usercode);
            if (ptoper == null) {
                ptoper = new Ptoper();
                ptoper.setOperid(usercode);
                //ptoper.setOperpasswd(password);
                ptoper.setDeptid(deptcode);
                ptoper.setOpername(fullname);
                ptoper.setEmail(usermail);
                ptoper.setOperenabled(userstate); //��0��-������ '1'-����
                ptoper.setFillstr50(createtime);
                ptoper.setFillstr80(updatetime);
                ptoperMapper.insert(ptoper);
                logger.info("ͨ��CAS�����û�ͬ����������û�" + usercode + fullname);
                response.getOutputStream().print("1");
            } else {
                ptoper.setOperid(usercode);
                //ptoper.setOperpasswd(password);
                ptoper.setDeptid(deptcode);
                ptoper.setOpername(fullname);
                ptoper.setEmail(usermail);
                ptoper.setOperenabled(userstate); //��0��-������ '1'-����
                ptoper.setFillstr50(createtime);
                ptoper.setFillstr80(updatetime);
                ptoperMapper.updateByPrimaryKeySelective(ptoper);
                logger.info("ͨ��CAS�����û�ͬ���������û���Ϣ" + usercode + fullname);
                response.getOutputStream().print("1");
            }
        } catch (Exception e) {
            response.getOutputStream().print("2");
            logger.error("ͨ��CAS�����û�ͬ�����ִ���" + usercode + fullname, e);
        }
    }
}

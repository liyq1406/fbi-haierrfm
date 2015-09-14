package pub.auth;

import org.jasig.cas.client.util.AssertionHolder;
import org.jasig.cas.client.validation.Assertion;
import pub.platform.form.config.SystemAttributeNames;
import pub.platform.security.OperatorManager;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 11-7-29
 * Time: ����3:00
 * To change this template use File | Settings | File Templates.
 */
public class CASLoginFilter implements Filter {
    public void destroy() {
        //Ŀǰû��Ҫ���ٵĶ���
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        //����û��Ƿ��¼
        boolean isLogin = auth((HttpServletRequest) request);
        String contextPath = ((HttpServletRequest) request).getContextPath();
        if (!isLogin) {
            //û�е�¼��ִ��û�е�¼����
            ((HttpServletResponse) response).sendRedirect(contextPath + "/pages/frame/errorPage.jsp");
        } else {
            ((HttpServletResponse) response).sendRedirect(contextPath + "/pages/frame/homePage.jsp");
        }
        //�Ѿ���¼���û���������
        //chain.doFilter(req, res);
    }

    public void init(FilterConfig filterConfig) throws ServletException {

//        String defaultUserPassword = filterConfig.getInitParameter("defaultUserPassword");
//        cASLogin.setDefaultUserPassword(defaultUserPassword);
    }

    /*����û��Ƿ��¼*/
    public Boolean auth(HttpServletRequest request) {
        Assertion assertion;
        Map userAttributes;
        String username = "";

        try {
            assertion = AssertionHolder.getAssertion();
            userAttributes = assertion.getAttributes();

            /*��һ�ε�¼��CAS��֤���û���������ɹ�֮�󣬻Ὣ�û��������ڴ��У�ҵ��ϵͳ���Ի��*/
            username = assertion.getPrincipal().getName();

            /*ҵ��ϵͳ�õ���ֻ������û��Ƿ������ҵ��ϵͳ��, checkIsExist�����Լ�ʵ��*/
            return checkIsExist(request,username);
        } catch (Exception e) {
            //TODO
            return false;
        }
    }

    private boolean checkIsExist(ServletRequest request, String username) {
        HttpSession session = ((HttpServletRequest) request).getSession();
        OperatorManager om = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);

        if (om == null) {
            om = new OperatorManager();
            session.setAttribute(SystemAttributeNames.USER_INFO_NAME, om);
        }
        try {
            om.setRemoteAddr(request.getRemoteAddr());
            om.setRemoteHost(request.getRemoteHost());
            return om.login4sso(username);
        } catch (Exception e) {
            //TODO
            e.printStackTrace();
        }
        return true;
    }
}

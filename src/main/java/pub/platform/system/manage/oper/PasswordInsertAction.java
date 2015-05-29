package pub.platform.system.manage.oper;

import platform.auth.MD5Helper;
import pub.platform.form.control.Action;
import pub.platform.system.manage.dao.PtOperBean;

public class PasswordInsertAction extends Action {
    PtOperBean operbean = new PtOperBean();

    public int doBusiness() {

        operbean.setOperpasswd(MD5Helper.getMD5String(this.req.getFieldValue("newpwd")));

        this.res.setType(0);

        if (operbean.updateByWhere(" where (deptid='"+this.req.getFieldValue("deptid")+"')and(operid='"+this.req.getFieldValue("operid")+"')") <0){

            this.res.setResult(false);
            this.res.setMessage("更新密码失败！");
            this.dc.close();
            return -1;
        }

        this.res.setResult(true);
        this.res.setMessage("更新密码成功！");
        this.dc.close();
        return 0;
    }


}

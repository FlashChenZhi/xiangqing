package com.auth.service;

import com.util.common.BaseReturnObj;
import com.util.common.LogMessage;
import com.util.common.ReturnObj;
import com.util.common.WmsServiceException;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.Menu;
import com.wms.domain.Role;
import com.wms.domain.User;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiongying on 15/6/19.
 */
@Service
public class AuthService {

    public ReturnObj<Map<String, Object>> getLoginUser(String code) {
        ReturnObj<Map<String, Object>> returnObj = new ReturnObj<Map<String, Object>>();
        try {
            Transaction.begin();

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(User.class);
            criteria.add(Restrictions.eq(User.COL_CODE, code));
            criteria.setMaxResults(1);

            User user = (User) criteria.uniqueResult();

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", user.getCode());
            map.put("name", user.getName());

            Transaction.commit();

            returnObj.setSuccess(true);
            returnObj.setRes(map);
        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());

        }

        return returnObj;
    }

    public ReturnObj<List<Map<String, Object>>> getAuths(String userCode, String menuCode) {
        ReturnObj<List<Map<String, Object>>> returnObj = new ReturnObj<List<Map<String, Object>>>();
        try {
            Transaction.begin();

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(Menu.class);
            Criteria roleC = criteria.createCriteria(Menu.COL_ROLES);
            Criteria userC = roleC.createCriteria(Role.COL_USERS);

            criteria.add(Restrictions.eq(Menu.COL_STATUS, Menu.STATUS_ENABLE));
            roleC.add(Restrictions.eq(Role.COL_STATUS, Role.STATUS_ENABLE));
            userC.add(Restrictions.eq(User.COL_CODE, userCode));
            if ("-1".equals(menuCode)) {
                criteria.add(Restrictions.eq(Menu.COL_TYPE, Menu.TYPE_FIRST_MENU));
            } else {
                criteria.add(Restrictions.eq(Menu.COL_TYPE, Menu.TYPE_SECOND_MENU));
                criteria.add(Restrictions.eq(Menu.COL_PARENT_CODE, menuCode));
            }
            criteria.add(Restrictions.eq(Menu.COL_DIST, Menu.TYPE_DIST_WEB));
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            criteria.addOrder(Order.asc(Menu.COL_SEQ));

            List<Menu> list = criteria.list();

            List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
            for (Menu menu : list) {
                Map<String, Object> menuMap = new HashMap<String, Object>();
                menuMap.put("text", menu.getName());
                if (Menu.TYPE_FIRST_MENU.equals(menu.getType())) {
                    menuMap.put("expanded", false);
                }
                if (Menu.TYPE_SECOND_MENU.equals(menu.getType())) {
                    menuMap.put("leaf", true);
                }
                menuMap.put("id", menu.getCode());
                menuList.add(menuMap);
            }
            Transaction.commit();

            returnObj.setSuccess(true);
            returnObj.setRes(menuList);
        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());

        }

        return returnObj;
    }


    public ReturnObj<Map<String, String>> login(String userCode, String password) {
        ReturnObj<Map<String, String>> returnObj = new ReturnObj<Map<String, String>>();
        try {
            Transaction.begin();

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(User.class);
            criteria.add(Restrictions.eq(User.COL_CODE, userCode));
            criteria.setMaxResults(1);

            User user = (User) criteria.uniqueResult();
            if (user == null) {
                throw new WmsServiceException("用户名或密码不正确");
            } else {
                boolean flag = user.validate(password);
                if (flag) {
                    if (User.STATUS_DISABLE.equals(user.getStatus())) {
                        throw new WmsServiceException("该用户已禁用");
                    }
                } else {
                    throw new WmsServiceException("用户名或密码不正确");
                }
            }

            Map<String, String> map = new HashMap<String, String>();
            map.put("userCode", user.getCode());
            map.put("theme", user.getTheme());

            Transaction.commit();

            returnObj.setSuccess(true);
            returnObj.setRes(map);
        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (WmsServiceException ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(ex.getMessage());

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());

        }

        return returnObj;
    }

    public BaseReturnObj modifyPassword(String userCode, String oldPassword, String newPassword) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {
            Transaction.begin();

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(User.class);
            criteria.add(Restrictions.eq(User.COL_CODE, userCode));
            criteria.setMaxResults(1);
            User user = (User) criteria.uniqueResult();

            if (user == null)
                throw new WmsServiceException("用户不存在");

            if (oldPassword.equals(newPassword))
                throw new WmsServiceException("原密码不能和新密码一致");

            if (!user.getPassword().equals(user.encrypt(oldPassword)))
                throw new WmsServiceException("原密码不正确");

            user.setPassword(user.encrypt(newPassword));

            Transaction.commit();

            returnObj.setSuccess(true);
            returnObj.setMsg("密码修改成功");
        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (WmsServiceException ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(ex.getMessage());

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());

        }

        return returnObj;
    }

}

package com.wms.service;

import com.util.common.HttpMessage;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.UUID;

/**
 * Created by Administrator on 2016/10/14.
 */
public class UserService {
    public HttpMessage login(HttpMessage httpMessage, String userName, String pwd) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction.begin();
        try {
            Criteria criteria = session.createCriteria(User.class).add(Restrictions.eq("userName", userName));
            User user = (User) criteria.uniqueResult();
            String data;
            if (user == null) {
                data = "用户名或密码错误";
                httpMessage.setSuccess(false);

            } else {
//                if (user.getPwd().equals(pwd)) {
//                    data = "登录成功";
//                    httpMessage.setSuccess(true);
//                } else {
//                    data = "用户名或密码错误";
//                    httpMessage.setSuccess(false);
//                }

            }
           // httpMessage.setMsg(data);
            Transaction.commit();
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了");
            e.printStackTrace();
        }

        return httpMessage;
    }

    public HttpMessage updatePassword(String userName, String oldPwd, String password) {
        HttpMessage httpMessage = new HttpMessage();
        try {
            Session session = HibernateUtil.getCurrentSession();
            Transaction.begin();
            Criteria criteria = session.createCriteria(User.class).add(Restrictions.eq("userName", userName));
            User user = (User) criteria.uniqueResult();
            if (user == null) {
                httpMessage.setSuccess(false);
                httpMessage.setMsg(String.format("登录用户%s不存在", userName));
            } else {
//                if (!DigestUtils.sha512Hex(oldPwd + user.getSalt()).equals(user.getPwd())) {
//                    httpMessage.setSuccess(false);
//                    httpMessage.setMsg(String.format("登录用户%s原密码错误", userName));
//                } else {
//                    user.setPwd(DigestUtils.sha512Hex((password + user.getSalt())));
//                    session.update(user);
//                    httpMessage.setSuccess(true);
//                    httpMessage.setMsg("密码修改成功");
//                }
            }
            Transaction.commit();
        } catch (Exception e) {
            Transaction.rollback();
            httpMessage.setSuccess(false);
            httpMessage.setMsg("出错了");
            e.printStackTrace();
        }

        return httpMessage;
    }

    public HttpMessage create(HttpMessage httpMessage, String userName, String pwd) {
        Session session = HibernateUtil.getCurrentSession();
        Transaction.begin();
        try {
            Criteria criteria = session.createCriteria(User.class).add(Restrictions.eq("userName", userName));
            User user = (User) criteria.uniqueResult();
            if (user == null) {
                user = new User();
//                user.setUserName(userName);
//                user.setSalt(UUID.randomUUID().toString());
//                user.setPwd(DigestUtils.sha512Hex((pwd + user.getSalt())));
                session.saveOrUpdate(user);
                httpMessage.setSuccess(true);
                httpMessage.setMsg("注册成功");
            } else {
                httpMessage.setSuccess(false);
                httpMessage.setMsg("用户名已存在");
            }
            Transaction.commit();
        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
        }
        return httpMessage;
    }
}

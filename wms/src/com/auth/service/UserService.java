package com.auth.service;

import com.auth.vo.UserVo;
import com.util.common.*;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.util.pages.Pages;
import com.wms.domain.Role;
import com.wms.domain.User;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by xiongying on 15/6/19.
 */
@Service
public class UserService {

    public PagerReturnObj<List<UserVo>> list(String code, String status, Pages pages) {
        PagerReturnObj<List<UserVo>> returnObj = new PagerReturnObj<List<UserVo>>();
        try {
            Transaction.begin();

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(User.class);

            if (StringUtils.isNotEmpty(code)) {
                criteria.add(Restrictions.eq(User.COL_CODE, code));
            }
            if (StringUtils.isNotEmpty(status)) {
                criteria.add(Restrictions.eq(User.COL_STATUS, status));
            }

            criteria.addOrder(Order.asc(User.COL_ID));

            //获取总行数
            Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
            //获取分页数据
            criteria.setProjection(null);
            criteria.setFirstResult(pages.getFirstRow());
            criteria.setMaxResults(pages.getPageSize());

            List<User> list = criteria.list();

            List<UserVo> vos = new ArrayList<UserVo>(pages.getPageSize());
            UserVo vo;
            for (User user : list) {
                vo = new UserVo();
                vo.setId(user.getId());
                vo.setCode(user.getCode());
                vo.setName(user.getName());
                vo.setStatus(user.getStatus());

                Set<Role> roles = user.getRoles();
                StringBuffer roleNames = new StringBuffer();
                Integer roleId = null;
                for (Role role : roles) {
                    if (roleId == null)
                        roleId = role.getId();
                    roleNames.append(role.getName() + " ");

                }
                if (roleId != null)
                    vo.setRoleId(roleId);
                vo.setRoleNames(roleNames.toString());

                vos.add(vo);
            }

            Transaction.commit();

            returnObj.setSuccess(true);
            returnObj.setRes(vos);
            returnObj.setCount(count);
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

    public BaseReturnObj create(UserVo userVo) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {
            Transaction.begin();

            Role role = Role.getById(userVo.getRoleId());

            User user = User.getByCode(userVo.getCode());
            if (user != null) {
                throw new WmsServiceException(String.format("编号%s用户已经存在", userVo.getCode()));
            }
            user = new User();
            user.setCode(userVo.getCode());
            user.setName(userVo.getName());
            user.setStatus(userVo.getStatus());
            user.getRoles().add(role);

            HibernateUtil.getCurrentSession().save(user);

            user.initPassword();

            Transaction.commit();

            returnObj.setSuccess(true);
            returnObj.setMsg("新建成功");
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

    public BaseReturnObj update(UserVo userVo) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {
            Transaction.begin();

            Role role = Role.getById(userVo.getRoleId());

            User user = User.getById(userVo.getId());
            user.setName(userVo.getName());
            user.setStatus(userVo.getStatus());

            Set<Role> roles = new HashSet<Role>();
            roles.add(role);
            user.setRoles(roles);

            Transaction.commit();

            returnObj.setSuccess(true);
            returnObj.setMsg("修改成功");
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

    public BaseReturnObj updateTheme(String userCode, String theme) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {
            Transaction.begin();

            if (StringUtils.isEmpty(userCode))
                throw new WmsServiceException("用户代码不存在");

            User user = User.getByCode(userCode);

            if (user == null)
                throw new WmsServiceException("用户不存在");

            if (StringUtils.isNotEmpty(theme)) {
                user.setTheme(theme);
            }

            Transaction.commit();

            returnObj.setSuccess(true);
            returnObj.setMsg("主题修改成功");
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

    public BaseReturnObj initPwd(String code) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {

            Transaction.begin();
            User user = User.getByCode(code);
            if (user == null) {
                throw new WmsServiceException("用户不存在");
            }
            user.initPassword();

            Transaction.commit();
            returnObj.setSuccess(true);
            returnObj.setMsg("密码重置成功，新密码" + User.INIT_PASSWORD);

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

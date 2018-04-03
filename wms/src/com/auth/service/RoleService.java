package com.auth.service;

import com.auth.vo.RoleMenuList;
import com.auth.vo.RoleVo;
import com.util.common.*;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.util.pages.Pages;
import com.wms.domain.Menu;
import com.wms.domain.Role;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by xiongying on 15/6/19.
 */
@Service
public class RoleService {

    public PagerReturnObj<List<RoleVo>> list(String status, Pages pages) {
        PagerReturnObj<List<RoleVo>> returnObj = new PagerReturnObj<List<RoleVo>>();
        try {
            Transaction.begin();

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(Role.class);

            if (StringUtils.isNotEmpty(status)) {
                criteria.add(Restrictions.eq(Role.COL_STATUS, status));
            }

            criteria.addOrder(Order.asc(Role.COL_ID));

            //获取总行数
            Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
            //获取分页数据
            criteria.setProjection(null);
            criteria.setFirstResult(pages.getFirstRow());
            criteria.setMaxResults(pages.getPageSize());

            List<Role> list = criteria.list();

            List<RoleVo> vos = new ArrayList<RoleVo>(pages.getPageSize());
            RoleVo vo;
            for (Role role : list) {
                vo = new RoleVo();
                vo.setId(role.getId());
                vo.setName(role.getName());
                vo.setStatus(role.getStatus());

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

    public ReturnObj<List<RoleMenuList>> getMenus(int roleId) {
        ReturnObj<List<RoleMenuList>> returnObj = new ReturnObj<List<RoleMenuList>>();
        try {
            Transaction.begin();


            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(Menu.class);
            Criteria roleC = criteria.createCriteria(Menu.COL_ROLES);
            roleC.add(Restrictions.eq(Role.COL_ID, roleId));

            criteria.addOrder(Order.asc(Menu.COL_TYPE));
            criteria.addOrder(Order.asc(Menu.COL_SEQ));

            List<Menu> menus = criteria.list();

            List<RoleMenuList> list = new ArrayList<RoleMenuList>();
            RoleMenuList roleMenuList;
            for (Menu menu : menus) {
                roleMenuList = new RoleMenuList();
                roleMenuList.setMenuId(menu.getId());

                list.add(roleMenuList);
            }

            Transaction.commit();

            returnObj.setSuccess(true);
            returnObj.setRes(list);
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

    public BaseReturnObj create(RoleVo roleVo, List<RoleMenuList> roleMenuLists) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {
            Transaction.begin();

            List<Integer> menuIds = new ArrayList<Integer>();
            for (RoleMenuList roleMenuList : roleMenuLists)
                menuIds.add(roleMenuList.getMenuId());

            Criteria menuC = HibernateUtil.getCurrentSession().createCriteria(Menu.class);
            menuC.add(Restrictions.in(Menu.COL_ID, menuIds));
            menuC.addOrder(Order.asc(Menu.COL_ID));
            List<Menu> menus = menuC.list();

            String roleName = URLDecoder.decode(roleVo.getName(), "utf-8");

            Role role = Role.getByName(roleName);
            if (role != null) {
                throw new WmsServiceException(String.format("角色%s已经存在", roleName));
            }
            role = new Role();
            role.setName(roleName);
            role.setStatus(roleVo.getStatus());
            role.getMenus().addAll(menus);
            HibernateUtil.getCurrentSession().save(role);

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

    public BaseReturnObj update(RoleVo roleVo) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {
            Transaction.begin();

            Role role = Role.getById(roleVo.getId());
            String roleName = URLDecoder.decode(roleVo.getName(), "utf-8");
            role.setName(roleName);
            role.setStatus(roleVo.getStatus());

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

    public BaseReturnObj authSetting(Integer id, List<RoleMenuList> roleMenuLists) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {
            Transaction.begin();

            Role role = Role.getById(id);

            List<Integer> menuIds = new ArrayList<Integer>();
            for (RoleMenuList roleMenuList : roleMenuLists)
                menuIds.add(roleMenuList.getMenuId());

            Criteria menuC = HibernateUtil.getCurrentSession().createCriteria(Menu.class);
            menuC.add(Restrictions.in(Menu.COL_ID, menuIds));
            menuC.addOrder(Order.asc(Menu.COL_ID));
            List<Menu> menus = menuC.list();

            role.setMenus(new HashSet<Menu>(menus));

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
}

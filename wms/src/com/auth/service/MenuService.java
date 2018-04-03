package com.auth.service;

import com.auth.vo.MenuExcelVo;
import com.auth.vo.MenuVo;
import com.util.common.*;
import com.util.excel.ExcelExportParam;
import com.util.excel.ExcelExportUtils;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.Menu;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by xiongying on 15/6/19.
 */
@Service
public class MenuService {

    public ReturnObj<List<MenuVo>> list(String code, String dist, String status) {
        ReturnObj<List<MenuVo>> returnObj = new ReturnObj<List<MenuVo>>();
        try {
            Transaction.begin();

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(Menu.class);

            if (StringUtils.isNotEmpty(code)) {
                criteria.add(Restrictions.eq(Menu.COL_CODE, code));
            }
            if (StringUtils.isNotEmpty(dist)) {
                criteria.add(Restrictions.eq(Menu.COL_DIST, dist));
            }

            if (StringUtils.isNotEmpty(status)) {
                criteria.add(Restrictions.eq(Menu.COL_STATUS, status));
            }

            criteria.addOrder(Order.asc(Menu.COL_TYPE));
            criteria.addOrder(Order.asc(Menu.COL_SEQ));

            List<Menu> list = criteria.list();

            List<MenuVo> vos = new ArrayList<MenuVo>(list.size());


            Map<Integer, Menu> firstMenuMap = new TreeMap<Integer, Menu>();
            Map<Integer, List<Menu>> secondMenuMap = new TreeMap<Integer, List<Menu>>();
            for (Menu menu : list) {
                if (Menu.TYPE_FIRST_MENU.equals(menu.getType()))
                    firstMenuMap.put(menu.getId(), menu);
                if (Menu.TYPE_SECOND_MENU.equals(menu.getType())) {
                    if (secondMenuMap.get(menu.getParentId()) == null) {
                        List<Menu> sendMenus = new ArrayList<Menu>();
                        sendMenus.add(menu);
                        secondMenuMap.put(menu.getParentId(), sendMenus);
                    } else {
                        List<Menu> sendMenus = secondMenuMap.get(menu.getParentId());
                        sendMenus.add(menu);
                        secondMenuMap.put(menu.getParentId(), sendMenus);
                    }
                }
            }

            for (Integer key : firstMenuMap.keySet()) {
                Menu firstMenu = firstMenuMap.get(key);
                MenuVo firstMenuVo = new MenuVo();
                firstMenuVo.setId(firstMenu.getId());
                firstMenuVo.setDist(firstMenu.getDist());
                firstMenuVo.setType(firstMenu.getType());
                firstMenuVo.setCode(firstMenu.getCode());
                firstMenuVo.setName(firstMenu.getName());
                firstMenuVo.setStatus(firstMenu.getStatus());
                firstMenuVo.setSeq(firstMenu.getSeq());
                vos.add(firstMenuVo);
                List<Menu> secondMenus = secondMenuMap.get(key);
                if (secondMenus != null) {
                    for (Menu secondMenu : secondMenus) {
                        MenuVo secondMenuVo = new MenuVo();
                        secondMenuVo.setId(secondMenu.getId());
                        secondMenuVo.setDist(secondMenu.getDist());
                        secondMenuVo.setType(secondMenu.getType());
                        secondMenuVo.setCode(secondMenu.getCode());
                        secondMenuVo.setName(secondMenu.getName());
                        secondMenuVo.setStatus(secondMenu.getStatus());
                        secondMenuVo.setSeq(secondMenu.getSeq());
                        secondMenuVo.setParentId(secondMenu.getParentId());
                        secondMenuVo.setParentCode(secondMenu.getParentCode());
                        vos.add(secondMenuVo);
                    }
                }
            }

            Transaction.commit();

            returnObj.setSuccess(true);
            returnObj.setRes(vos);
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

    public BaseReturnObj create(MenuVo menuVo) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {
            Transaction.begin();

            Menu menu = Menu.getByCode(menuVo.getCode());
            if (menu != null)
                throw new WmsServiceException(String.format("菜单编号%s已经存在", menuVo.getCode()));
            menu = new Menu();
            menu.setCode(menuVo.getCode());
            menu.setName(menuVo.getName());
            if (Menu.TYPE_SECOND_MENU.equals(menuVo.getType())) {
                Menu parentMenu = Menu.getById(menuVo.getParentId());
                if (parentMenu == null)
                    throw new WmsServiceException("上级菜单不存在");
                menu.setParentId(parentMenu.getId());
                menu.setParentCode(parentMenu.getCode());
            }
            menu.setType(menuVo.getType());
            menu.setDist(menuVo.getDist());
            menu.setSeq(menuVo.getSeq());
            menu.setStatus(menuVo.getStatus());

            Criteria otherMenuC = HibernateUtil.getCurrentSession().createCriteria(Menu.class);
            otherMenuC.add(Restrictions.eq(Menu.COL_TYPE, menu.getType()));
            otherMenuC.add(Restrictions.eq(Menu.COL_DIST, menu.getDist()));
            if (menu.getParentId() != null)
                otherMenuC.add(Restrictions.eq(Menu.COL_PARENT_ID, menu.getParentId()));
            otherMenuC.add(Restrictions.ge(Menu.COL_SEQ, menu.getSeq()));
            otherMenuC.addOrder(Order.asc(Menu.COL_SEQ));
            List<Menu> otherMenus = otherMenuC.list();

            int otherSeq = menu.getSeq() + 1;
            for (Menu otherMenu : otherMenus) {
                otherMenu.setSeq(otherSeq);
                otherSeq++;
            }

            HibernateUtil.getCurrentSession().save(menu);

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

    public BaseReturnObj update(MenuVo menuVo) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {
            Transaction.begin();

            Menu menu = Menu.getById(menuVo.getId());
            menu.setName(menuVo.getName());
            menu.setType(menuVo.getType());
            menu.setDist(menuVo.getDist());
            if (Menu.TYPE_SECOND_MENU.equals(menuVo.getType())) {
                Menu parentMenu = Menu.getById(menuVo.getParentId());
                if (parentMenu == null)
                    throw new WmsServiceException("上级菜单不存在");
                menu.setParentId(parentMenu.getId());
                menu.setParentCode(parentMenu.getCode());
            }
            menu.setSeq(menuVo.getSeq());
            menu.setStatus(menuVo.getStatus());

            Criteria otherMenuC = HibernateUtil.getCurrentSession().createCriteria(Menu.class);
            otherMenuC.add(Restrictions.eq(Menu.COL_TYPE, menu.getType()));
            otherMenuC.add(Restrictions.eq(Menu.COL_DIST, menu.getDist()));
            if (menu.getParentId() != null)
                otherMenuC.add(Restrictions.eq(Menu.COL_PARENT_ID, menu.getParentId()));
            otherMenuC.add(Restrictions.ge(Menu.COL_SEQ, menu.getSeq()));
            otherMenuC.add(Restrictions.not(Restrictions.eq(Menu.COL_ID, menu.getId())));
            otherMenuC.addOrder(Order.asc(Menu.COL_SEQ));
            List<Menu> otherMenus = otherMenuC.list();

            int otherSeq = menu.getSeq() + 1;
            for (Menu otherMenu : otherMenus) {
                otherMenu.setSeq(otherSeq);
                otherSeq++;
            }

            HibernateUtil.getCurrentSession().save(menu);

            Transaction.commit();

            returnObj.setSuccess(true);
            returnObj.setMsg("修改成功");
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


    public ReturnObj<HSSFWorkbook> generateExcelExportFile() {
        ReturnObj<HSSFWorkbook> returnObj = new ReturnObj<HSSFWorkbook>();
        try {
            Transaction.begin();

            Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(Menu.class);
            criteria.add(Restrictions.eq(Menu.COL_STATUS, Menu.STATUS_ENABLE));
            criteria.addOrder(Order.asc(Menu.COL_TYPE));
            criteria.addOrder(Order.asc(Menu.COL_SEQ));

            List<Menu> list = criteria.list();

            List<MenuExcelVo> vos = new ArrayList<MenuExcelVo>(list.size());

            Map<Integer, Menu> firstMenuMap = new TreeMap<Integer, Menu>();
            Map<Integer, List<Menu>> secondMenuMap = new TreeMap<Integer, List<Menu>>();
            for (Menu menu : list) {
                if (Menu.TYPE_FIRST_MENU.equals(menu.getType()))
                    firstMenuMap.put(menu.getId(), menu);
                if (Menu.TYPE_SECOND_MENU.equals(menu.getType())) {
                    if (secondMenuMap.get(menu.getParentId()) == null) {
                        List<Menu> sendMenus = new ArrayList<Menu>();
                        sendMenus.add(menu);
                        secondMenuMap.put(menu.getParentId(), sendMenus);
                    } else {
                        List<Menu> sendMenus = secondMenuMap.get(menu.getParentId());
                        sendMenus.add(menu);
                        secondMenuMap.put(menu.getParentId(), sendMenus);
                    }
                }
            }

            for (Integer key : firstMenuMap.keySet()) {
                Menu firstMenu = firstMenuMap.get(key);
                MenuExcelVo firstMenuVo = new MenuExcelVo();
                firstMenuVo.setId(firstMenu.getId());
                firstMenuVo.setDist(firstMenu.getDistStr());
                firstMenuVo.setType(firstMenu.getTypeStr());
                firstMenuVo.setCode(firstMenu.getCode());
                firstMenuVo.setName(firstMenu.getName());
                firstMenuVo.setStatus(firstMenu.getStatusStr());
                firstMenuVo.setSeq(firstMenu.getSeq());
                vos.add(firstMenuVo);
                List<Menu> secondMenus = secondMenuMap.get(key);
                if (secondMenus != null) {
                    for (Menu secondMenu : secondMenus) {
                        MenuExcelVo secondMenuVo = new MenuExcelVo();
                        secondMenuVo.setId(secondMenu.getId());
                        secondMenuVo.setDist(secondMenu.getDistStr());
                        secondMenuVo.setType(secondMenu.getTypeStr());
                        secondMenuVo.setCode(secondMenu.getCode());
                        secondMenuVo.setName(secondMenu.getName());
                        secondMenuVo.setStatus(secondMenu.getStatusStr());
                        secondMenuVo.setSeq(secondMenu.getSeq());
                        secondMenuVo.setParentId(secondMenu.getParentId());
                        secondMenuVo.setParentCode(secondMenu.getParentCode());
                        vos.add(secondMenuVo);
                    }
                }
            }

            HSSFWorkbook hssfWorkbook = ExcelExportUtils.generateExcelFile(generateExcelExportParams(), vos);

            Transaction.commit();

            returnObj.setSuccess(true);
            returnObj.setRes(hssfWorkbook);
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

    public List<ExcelExportParam> generateExcelExportParams() {
        List<ExcelExportParam> list = new ArrayList<ExcelExportParam>();
        list.add(new ExcelExportParam("menu.id", "id"));
        list.add(new ExcelExportParam("menu.code", "code"));
        list.add(new ExcelExportParam("menu.name", "name"));
        list.add(new ExcelExportParam("menu.parentId", "parentId"));
        list.add(new ExcelExportParam("menu.parentCode", "parentCode"));
        list.add(new ExcelExportParam("menu.type", "type"));
        list.add(new ExcelExportParam("menu.dist", "dist"));
        list.add(new ExcelExportParam("menu.seq", "seq"));
        list.add(new ExcelExportParam("menu.status", "status"));


        return list;
    }

}

package com.auth.action;

import com.auth.service.RoleService;
import com.auth.vo.RoleMenuList;
import com.auth.vo.RoleVo;
import com.util.common.BaseReturnObj;
import com.util.common.PagerReturnObj;
import com.util.common.ReturnObj;
import com.util.pages.GridPages;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xiongying on 15/6/19.
 */
@Controller
@RequestMapping(value = "/role")
public class RoleAction {

    @Resource(name = "roleService")
    private RoleService roleService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public PagerReturnObj<List<RoleVo>> list(String status, GridPages pages) {
        return roleService.list(status, pages);
    }

    @RequestMapping(value = "/getMenus", method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<List<RoleMenuList>> getMenus(int roleId) {
        return roleService.getMenus(roleId);
    }


    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj create(RoleVo roleVo, @RequestBody List<RoleMenuList> roleMenuLists) {
        return roleService.create(roleVo, roleMenuLists);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj update(RoleVo roleVo) {
        return roleService.update(roleVo);
    }

    @RequestMapping(value = "/authSetting", method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj authSetting(Integer id, @RequestBody List<RoleMenuList> roleMenuLists) {
        return roleService.authSetting(id, roleMenuLists);
    }

}

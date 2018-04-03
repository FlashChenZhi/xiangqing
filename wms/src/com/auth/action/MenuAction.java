package com.auth.action;

import com.auth.service.MenuService;
import com.auth.vo.MenuVo;
import com.util.common.BaseReturnObj;
import com.util.common.ReturnObj;
import com.util.excel.ExcelExportUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by xiongying on 15/6/19.
 */
@Controller
@RequestMapping(value = "/menu")
public class MenuAction {

    @Resource(name = "menuService")
    private MenuService menuService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<List<MenuVo>> list(String code, String dist, String status) {
        return menuService.list(code, dist, status);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj create(MenuVo menuVo) {
        return menuService.create(menuVo);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj update(MenuVo menuVo) {
        return menuService.update(menuVo);
    }

//    @RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
//    public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        ReturnObj<HSSFWorkbook> returnObj = menuService.generateExcelExportFile();
//        if (returnObj.isSuccess())
//            ExcelExportUtils.exportExcelToClient(returnObj.getRes(), "菜单一览.xls", response);
//    }

}

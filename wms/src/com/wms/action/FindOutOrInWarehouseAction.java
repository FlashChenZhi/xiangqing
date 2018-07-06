package com.wms.action;

import com.util.common.PagerReturnObj;
import com.util.common.ReturnObj;
import com.wms.service.FindOutOrInWarehouseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/master/FindOutOrInWarehouseAction")
public class FindOutOrInWarehouseAction {
    @Resource
    private FindOutOrInWarehouseService findOutOrInWarehouseService;

    /*
     * @description： 初始化页面商品代码
     */
    @RequestMapping(value = "/getSkuCode",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<List<Map<String,String>>> getSkuCode(){
        return findOutOrInWarehouseService.getSkuCode();
    }
    /*
     * @description： 查找出入库信息
     */
    @RequestMapping(value = "/findOutOrInWarehouse",method = RequestMethod.POST)
    @ResponseBody
    public PagerReturnObj<List<Map<String,Object>>> findOutOrInWarehouse(int current, int defaultPageSize, String productId, String beginDate, String endDate,String type){
        int startIndex = (current-1) * defaultPageSize;
        return findOutOrInWarehouseService.findOutOrInWarehouse(startIndex,defaultPageSize,productId,beginDate,endDate, type);
    }

    /*
     * @description： 导出报表
     */
    @RequestMapping(value = "/exportReport",method = RequestMethod.GET)
    public void exportReport(String beginDate, String endDate, HttpServletResponse response, HttpServletRequest request){
        findOutOrInWarehouseService.exportReport(beginDate, endDate,response,request);
    }
}

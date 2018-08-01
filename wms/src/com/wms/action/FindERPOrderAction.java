package com.wms.action;

import com.util.common.PagerReturnObj;
import com.util.common.ReturnObj;
import com.wms.service.FindERPOrderService;
import com.wms.service.FindOrderDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: ed_chen
 * @Date: Create in 15:45 2018/7/16
 * @Description:
 * @Modified By:
 */
@Controller
@RequestMapping("/master/FindERPOrderAction")
public class FindERPOrderAction {
    @Resource
    private FindERPOrderService findERPOrderService;

    /*
     * @description： 查找订单数据
     */
    @RequestMapping(value = "/FindERPOrderData",method = RequestMethod.POST)
    @ResponseBody
    public PagerReturnObj<List<Map<String,Object>>> FindERPOrderData(int current, int defaultPageSize, String orderNo){
        int startIndex = (current-1) * defaultPageSize;
        return findERPOrderService.FindERPOrderData(startIndex,defaultPageSize,orderNo);
    }

    /*
     * @description： 查找订单详细数据
     *//*
    @RequestMapping(value = "/FindOrderDetail",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<Map<String,Object>> FindOrderDetail(String orderNo){
        return findOrderDetailService.FindOrderDetail(orderNo);
    }*/

}

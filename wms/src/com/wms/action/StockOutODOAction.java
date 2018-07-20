package com.wms.action;

import com.util.common.ReturnObj;
import com.wms.service.StockOutODOService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: ed_chen
 * @Date: Create in 18:10 2018/7/13
 * @Description:
 * @Modified By:
 */
@Controller
@RequestMapping("/master/StockOutODOAction")
public class StockOutODOAction {

    @Resource
    private StockOutODOService stockOutODOService;

    /*
     * @description： 初始化页面商品代码
     */
    @RequestMapping(value = "/getLotNums",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<List<Map<String,String>>> getLotNums(){
        return stockOutODOService.getLotNums();
    }

    /*
     * @description： 初始化页面商品代码
     */
    @RequestMapping(value = "/findNumBySkuAndLotNum",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<List<Map<String,Object>>> findNumBySkuAndLotNum(String skuCode,String lotNum){
        return stockOutODOService.findNumBySkuAndLotNum(skuCode,lotNum);
    }

    /*
     * @description： 初始化订单号
     */
    @RequestMapping(value = "/getOrderNo",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<String> getOrderNo(){
        return stockOutODOService.getOrderNo();
    }

    /*
     * @description： 生成订单
     */
    @RequestMapping(value = "/addOrder",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<String> addOrder(String driver,String orderNo,
                String createPerson,String placeOfArrival,String car,String zhantai,String data){
        return stockOutODOService.addOrder(driver,orderNo,createPerson,placeOfArrival,car,zhantai,data);
    }
}

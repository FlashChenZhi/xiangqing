package com.wms.action;

import com.util.common.PagerReturnObj;
import com.util.common.ReturnObj;
import com.wms.service.FindInventoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author: ed_chen
 * @Date: Create in 23:47 2018/3/4
 * @Description: 查询库存
 * @Modified By:
 */
@Controller
@RequestMapping("/master/FindInventoryAction")
public class FindInventoryAction {
    @Resource
    private FindInventoryService findInventoryService;
    /*
     * @author：ed_chen
     * @date：2018/3/5 10:00
     * @description： 查找库存信息
     * @param startIndex 开始记录数
     * @param defaultPageSize 页面展示数据最大值
     * @param containerNo 托盘号
     * @param locationNo 位置id
     * @param productId 商品代码
     * @return：com.util.common.PagerReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
     */
    @RequestMapping(value = "/findInventory",method = RequestMethod.POST)
    @ResponseBody
    public PagerReturnObj<List<Map<String,Object>>> findInventory(int current, int defaultPageSize,
                                    String containerNo,String locationNo,String productId,String lotNo,
                                    String beginDate, String endDate){
        int startIndex = (current-1) * defaultPageSize;
        return findInventoryService.findInventory(startIndex,defaultPageSize,containerNo,locationNo,productId,lotNo,beginDate,endDate);
    }
    /*
     * @author：ed_chen
     * @date：2018/3/6 12:10
     * @description： 查找库存详情
     * @param skuCode
     * @param current
     * @param defaultPageSize
     * @return：com.util.common.PagerReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
     */
    @RequestMapping(value = "/findInventoryDetails",method = RequestMethod.POST)
    @ResponseBody
    public PagerReturnObj<List<Map<String,Object>>> findInventoryDetails(String skuCode,int current, int defaultPageSize,
                                      String containerNo,String locationNo,String lotNo,String beginDate, String endDate){
        int startIndex = (current-1) * defaultPageSize;
        System.out.println(skuCode);
        PagerReturnObj<List<Map<String,Object>>> result = findInventoryService.findInventoryDetails(skuCode,startIndex,
                defaultPageSize,containerNo,locationNo,lotNo,beginDate, endDate);
        return result;
    }
    /*
     * @author：ed_chen
     * @date：2018/3/5 10:01
     * @description： 获取商品代码
     * @param
     * @return：com.util.common.ReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.String>>>
     */
    @RequestMapping(value = "/getCommodityCode",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<List<Map<String,String>>> getCommodityCode() throws IOException{
        return findInventoryService.getCommodityCode();
    }
    /*
     * @author：ed_chen
     * @date：2018/3/10 15:24
     * @description：删除库存
     * @param inventoryId 库存id
     * @return：com.util.common.ReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.String>>>
     */
    @RequestMapping(value = "/deleteInventory",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<List<Map<String,String>>> deleteInventory(String containerId) throws IOException{
        return findInventoryService.deleteInventory(containerId);
    }

}

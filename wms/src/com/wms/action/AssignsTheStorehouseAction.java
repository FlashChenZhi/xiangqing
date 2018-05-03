package com.wms.action;


import com.util.common.ReturnObj;
import com.wms.service.AssignsTheStorehouseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: ed_chen
 * @Date: Create in 15:17 2018/4/8
 * @Description:
 * @Modified By:
 */
@Controller
@RequestMapping("/master/AssignsTheStorehouseAction")
public class AssignsTheStorehouseAction {
    @Resource
    private AssignsTheStorehouseService assignsTheStorehouseService;
    /*
     * @author：ed_chen
     * @date：2018/4/10 18:23
     * @description：初始化map
     * @param productId
     * @param tier
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    @RequestMapping(value = "/getStorageLocationData.do",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<Map<String, Object>> getStorageLocationData(String productId, String tier){

        return assignsTheStorehouseService.getStorageLocationData(productId,tier);
    }
    /*
     * @author：ed_chen
     * @date：2018/4/10 18:23
     * @description：获取库位信息
     * @param bank
     * @param bay
     * @param level
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    @RequestMapping(value = "/getLocationInfo.do",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<Map<String, Object>> getLocationInfo(String bank, String bay, String level){

        return assignsTheStorehouseService.getLocationInfo(bank,bay,level);
    }
    /*
     * @author：ed_chen
     * @date：2018/4/10 19:26
     * @description：获取下一位货位代码
     * @param bank
     * @param bay
     * @param level
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    @RequestMapping(value = "/getNextAvailableLocation.do",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<Map<String, Object>> getNextAvailableLocation(String bank, String bay, String level){

        return assignsTheStorehouseService.getNextAvailableLocation(bank,bay,level);
    }
    /*
     * @author：ed_chen
     * @date：2018/4/10 19:26
     * @description：获取里面的货位代码
     * @param bank
     * @param bay
     * @param level
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    @RequestMapping(value = "/getAgoUnavailableLocation.do",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<Map<String, Object>> getAgoUnavailableLocation(String bank, String bay, String level){

        return assignsTheStorehouseService.getAgoUnavailableLocation(bank,bay,level);
    }

    /*
     * @author：ed_chen
     * @date：2018/4/10 19:26
     * @description：设定出库任务
     * @param bank
     * @param bay
     * @param level
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    @RequestMapping(value = "/assignsTheStorehouse.do",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<Map<String, Object>> assignsTheStorehouse(String selectLocation){

        return assignsTheStorehouseService.assignsTheStorehouse(selectLocation);
    }
    /*
     * @author：ed_chen
     * @date：2018/4/27 15:13
     * @description：初始化页面商品代码
     * @param
     * @return：com.util.common.ReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.String>>>
     */
    @RequestMapping(value = "/getSkuCode.do",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<List<Map<String,String>>> getSkuCode(){
        return assignsTheStorehouseService.getSkuCode();
    }

}

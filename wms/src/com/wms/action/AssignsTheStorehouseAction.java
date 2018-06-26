package com.wms.action;

import com.util.common.PagerReturnObj;
import com.util.common.ReturnObj;
import com.wms.service.AssignsTheStorehouseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/master/AssignsTheStorehouseAction")
public class AssignsTheStorehouseAction {
    @Resource
    private AssignsTheStorehouseService assignsTheStorehouseService;
    /*
     * @description：初始化map
     */
    @RequestMapping(value = "/getStorageLocationData",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<Map<String, Object>> getStorageLocationData(String productId,String tier){
        return assignsTheStorehouseService.getStorageLocationData(productId,tier);
    }
    /*
     * @description：获取库位信息
     */
    @RequestMapping(value = "/getLocationInfo",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<Map<String, Object>> getLocationInfo(String bank,String bay,String level){
        return assignsTheStorehouseService.getLocationInfo(bank,bay,level);
    }
    /*
     * @description：获取下一位货位代码
     */
    @RequestMapping(value = "/getNextAvailableLocation",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<Map<String, Object>> getNextAvailableLocation(String bank,String bay,String level){
        return assignsTheStorehouseService.getNextAvailableLocation(bank,bay,level);
    }
    /*
     * @description：获取里面的货位代码
     */
    @RequestMapping(value = "/getAgoUnavailableLocation",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<Map<String, Object>> getAgoUnavailableLocation(String bank,String bay,String level){
        return assignsTheStorehouseService.getAgoUnavailableLocation(bank,bay,level);
    }

    /*
     * @description：设定出库任务
     */
    @RequestMapping(value = "/assignsTheStorehouse",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<Map<String, Object>> assignsTheStorehouse(String selectLocation,String stationNo){
        return assignsTheStorehouseService.assignsTheStorehouse(selectLocation,stationNo);
    }

    /*
     * @description：获取出库站台
     */
    @RequestMapping(value = "/getStationNo",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<List<Map<String,String>>> getStationNo(String stationNo){
        return assignsTheStorehouseService.getStationNo(stationNo);
    }

}

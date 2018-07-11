package com.wms.action;

import com.util.common.ReturnObj;
import com.wms.service.AssignsTheStorehouseService;
import com.wms.service.UpdateSkuLotNumService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author: ed_chen
 * @Date: Create in 16:44 2018/7/11
 * @Description:
 * @Modified By:
 */

@Controller
@RequestMapping("/master/updateSkuLotNumAction")
public class UpdateSkuLotNumAction {

    @Resource
    private UpdateSkuLotNumService updateSkuLotNumService;

    /*
     * @description：查询货品初始批次状态
     */
    @RequestMapping(value = "/findSkuCodeLotNum",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<Map<String, Object>> findSkuCodeLotNum(String skuCode){
        return updateSkuLotNumService.findSkuCodeLotNum(skuCode);
    }

    /*
     * @description：更新货品批次状态
     */
    @RequestMapping(value = "/updateSkuCodeLotNum",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<String> updateSkuCodeLotNum(String skuCode,
                                               String isManual,String lotNum){
        return updateSkuLotNumService.updateSkuCodeLotNum(skuCode,isManual,lotNum);
    }
}

package com.wms.action;

import com.util.common.ReturnObj;
import com.wms.service.UpdateAppConnectService;
import com.wms.service.UpdateSkuLotNumService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author: ed_chen
 * @Date: Create in 11:04 2018/8/10
 * @Description:
 * @Modified By:
 */
@Controller
@RequestMapping("/master/updateAppConnectAction")
public class UpdateAppConnectAction {
    @Resource
    private UpdateAppConnectService updateAppConnectService;

    /*
     * @author：ed_chen
     * @date：2018/8/10 11:06
     * @description：
     * @param skuCode
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    @RequestMapping(value = "/getAppConnect",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<Map<String, Object>> getAppConnect(){
        return updateAppConnectService.getAppConnect();
    }

    /*
     * @author：ed_chen
     * @date：2018/8/10 11:06
     * @description：
     * @param skuCode
     * @return：com.util.common.ReturnObj<java.util.Map<java.lang.String,java.lang.Object>>
     */
    @RequestMapping(value = "/updateAppConnect",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<Map<String, Object>> updateAppConnect(String isConnect){
        return updateAppConnectService.updateAppConnect(isConnect);
    }

}

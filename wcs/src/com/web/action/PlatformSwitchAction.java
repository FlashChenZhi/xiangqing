package com.web.action;

import com.asrs.domain.Station;

import com.web.service.PlatformSwitchService;
import com.web.vo.ReturnObj;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value ="/platformSwitch")
public class PlatformSwitchAction {

    @Resource
    private PlatformSwitchService platformSwitchService;
    /**
     * 进入页面查询站台模式，默认下拉框选择该模式
     * @param stationNo 站台
     * @return 模式编号
     */
    @RequestMapping(value = "findPlatformSwitch.do",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<Map<String,Object>> findPlatformSwitch(String stationNo){
        return platformSwitchService.findPlatformSwitch(stationNo);
    }

    /**
     * 站台模式切换更新
     * @param direction 模式
     * @param stationNo 站台ID
     * @return "0"设定成功，"1"设定失败
     */
    @RequestMapping(value = "updatePlatformSwitch.do",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<String> updatePlatformSwitch(String direction, String stationNo){
        return platformSwitchService.updatePlatformSwitch(direction,stationNo);
    }
}

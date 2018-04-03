package com.wms.action;

import com.util.common.HttpMessage;
import com.wms.service.LocationService;
import com.wms.vo.AutoLocationListVo;
import com.wms.vo.BaseReturnObj;
import com.wms.vo.ReturnObj;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2016/12/28.
 */
@Controller
@RequestMapping("/location")
public class LocationAction {

    @Resource
    private LocationService locationService;

    @RequestMapping(value = "/list.do", method = RequestMethod.GET)
    @ResponseBody
    public ReturnObj<List<AutoLocationListVo>> list() {
        return locationService.list();
    }

    @RequestMapping(value = "/offline.do")
    @ResponseBody
    public BaseReturnObj offline(String blockNo) {
        return locationService.offline(blockNo);
    }

    @RequestMapping(value = "/online.do")
    @ResponseBody
    public BaseReturnObj onLine(String blockNo) {
        return locationService.onLine(blockNo);
    }

    @RequestMapping(value = "/removeAbnormal.do")
    @ResponseBody
    public BaseReturnObj removeAbnormal(String blockNo) {
        return locationService.removeAbnormal(blockNo);
    }


    @RequestMapping(value = "/deleteDate.do")
    @ResponseBody
    public BaseReturnObj deleteDate(String blockNo) {
        return locationService.deleteDate(blockNo);
    }

    @RequestMapping(value = "/finish.do")
    @ResponseBody
    public BaseReturnObj finish(String blockNo) {
        return locationService.finish(blockNo);
    }

    @RequestMapping(value = "/loadCar.do")
    @ResponseBody
    public BaseReturnObj loadCar(String blockNo, String nextBlock) {
        return locationService.loadCar(blockNo, nextBlock);
    }

    @RequestMapping(value = "/onCar.do")
    @ResponseBody
    public BaseReturnObj onCar(String blockNo, String nextBlock) {
        return locationService.onCar(blockNo, nextBlock);
    }

    @RequestMapping("searchLocation.do")
    @ResponseBody
    public HttpMessage searchLocation(int currentPage, String locationNo, String bank, String bay, String level) {
        HttpMessage httpMessage = locationService.searchLocation(currentPage, locationNo, bank, bay, level);
        return httpMessage;
    }

    @RequestMapping("searchFirstLocation.do")
    @ResponseBody
    public HttpMessage searchFirstLocation(int currentPage, String locationNo, String bay, String level, String skuType) {
        HttpMessage httpMessage = locationService.searchFirstLocation(currentPage, locationNo, bay, level, skuType);
        return httpMessage;
    }

    @RequestMapping("frozenLocation.do")
    @ResponseBody
    public HttpMessage frozenLocation(String locationNo) {
        HttpMessage httpMessage = locationService.frozenLocation(locationNo, true);
        return httpMessage;
    }

    @RequestMapping("unFrozenLocation.do")
    @ResponseBody
    public HttpMessage unFrozenLocation(String locationNo) {
        HttpMessage httpMessage = locationService.frozenLocation(locationNo, false);
        return httpMessage;
    }

    @RequestMapping("changeLocation.do")
    @ResponseBody
    public HttpMessage changeLocation(String locationNo) {
        HttpMessage httpMessage = locationService.changeLocation(locationNo);
        return httpMessage;
    }


    @RequestMapping("chagneLocationType.do")
    @ResponseBody
    public HttpMessage chagneLocationType(String locationNo, String skuType) {
        HttpMessage httpMessage = locationService.chagneLocationType(locationNo, skuType);
        return httpMessage;
    }
}

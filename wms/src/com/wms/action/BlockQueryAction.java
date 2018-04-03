package com.wms.action;

import com.util.common.HttpMessage;
import com.wms.service.BlockQueryService;
import com.wms.vo.BaseReturnObj;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by wangfan
 * Created on 2017/3/11.
 */
@Controller
@RequestMapping("/block")
public class BlockQueryAction {

    @Resource
    private BlockQueryService blockQueryService;

    @RequestMapping("searchBlock.do")
    @ResponseBody
    public HttpMessage searchLocation(String blockNo) {
        HttpMessage httpMessage = blockQueryService.searchBlock(blockNo);
        return httpMessage;
    }

    @RequestMapping("onLine.do")
    @ResponseBody
    public HttpMessage onLine(String blockNo) {
        HttpMessage httpMessage = blockQueryService.onLine(blockNo, true);
        return httpMessage;
    }

    @RequestMapping("cancelWaiting.do")
    @ResponseBody
    public HttpMessage cancelWaiting(String blockNo) {
        HttpMessage httpMessage = blockQueryService.cancelWaiting(blockNo);
        return httpMessage;
    }


    @RequestMapping("offLine.do")
    @ResponseBody
    public HttpMessage offLine(String blockNo) {
        HttpMessage httpMessage = blockQueryService.onLine(blockNo, false);
        return httpMessage;
    }

    @RequestMapping("init.do")
    @ResponseBody
    public BaseReturnObj init() {
        return blockQueryService.init();
    }

    @RequestMapping("onCar.do")
    @ResponseBody
    public HttpMessage onCar(String blockNo) {
        HttpMessage httpMessage = blockQueryService.onCar(blockNo);
        return httpMessage;
    }

    @RequestMapping("charge.do")
    @ResponseBody
    public HttpMessage charge(String blockNo) {
        HttpMessage httpMessage = blockQueryService.charge(blockNo);
        return httpMessage;
    }

    @RequestMapping("chargeover.do")
    @ResponseBody
    public HttpMessage chargeover(String blockNo) {
        HttpMessage httpMessage = blockQueryService.chargeOver(blockNo);
        return httpMessage;
    }
}

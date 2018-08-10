package com.web.action;

import com.asrs.domain.AsrsJob;
import com.util.common.HttpMessage;
import com.web.service.WebService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by van on 2017/5/10.
 */
@Controller
@RequestMapping("/webService")
public class WebAction {


    @Resource
    private WebService webService;

    @RequestMapping("searchBlock.do")
    @ResponseBody
    public HttpMessage searchLocation(String blockNo) {
        HttpMessage httpMessage = webService.searchBlock(blockNo);
        return httpMessage;
    }

    @RequestMapping("searchMessage.do")
    @ResponseBody
    public HttpMessage searchMessage(int currentPage, String mcKey,String machineNo) {
        HttpMessage httpMessage = webService.searchMessage(currentPage, mcKey,machineNo);
        return httpMessage;
    }

    @RequestMapping("searchMessageLog.do")
    @ResponseBody
    public HttpMessage searchMessageLog(int currentPage, String type, String beginDate, String endDate) {
        HttpMessage httpMessage = webService.searchMsgLog(currentPage, type, beginDate, endDate);
        return httpMessage;
    }
    @RequestMapping("searchSystemLog.do")
    @ResponseBody
    public HttpMessage searchSystemLog(int currentPage, String type, String beginDate, String endDate) {
        HttpMessage httpMessage = webService.searchSysLog(currentPage, type, beginDate, endDate);
        return httpMessage;
    }

    @RequestMapping("searchJob.do")
    @ResponseBody
    public HttpMessage searchJob(int currentPage, AsrsJob asrsJob) {
        HttpMessage httpMessage = webService.asrsJobQuery(currentPage,asrsJob);
        return httpMessage;
    }

    @RequestMapping("sendMsg.do")
    @ResponseBody
    public HttpMessage sendMsg(int id) {
        HttpMessage httpMessage = webService.sendMsg(id);
        return httpMessage;
    }

    @RequestMapping("getMsg.do")
    @ResponseBody
    public HttpMessage getMsg(int id) {
        HttpMessage httpMessage = webService.getMsg(id);
        return httpMessage;
    }



    @RequestMapping("sendMessageHand.do")
    @ResponseBody
    public HttpMessage sendMessageHand(String  message) {
        HttpMessage httpMessage = webService.sendMessageHand(message);
        return httpMessage;
    }


    @RequestMapping("cancelWaiting.do")
    @ResponseBody
    public HttpMessage cancelWaiting(String  blockNo) {
        HttpMessage httpMessage = webService.cancelWaiting(blockNo);
        return httpMessage;
    }

    @RequestMapping("onLine.do")
    @ResponseBody
    public HttpMessage onLine(String  blockNo) {
        HttpMessage httpMessage = webService.onLine(blockNo);
        return httpMessage;
    }


    @RequestMapping("offLine.do")
    @ResponseBody
    public HttpMessage offLine(String  blockNo) {
        HttpMessage httpMessage = webService.offLine(blockNo);
        return httpMessage;
    }


    @RequestMapping("changeLevel.do")
    @ResponseBody
    public HttpMessage changeLevel(String  blockNo,String level) {
        HttpMessage httpMessage = webService.changeLevel(blockNo,level);
        return httpMessage;
    }

    @RequestMapping("moveScar.do")
    @ResponseBody
    public HttpMessage moveScar(String  blockNo) {
        HttpMessage httpMessage = webService.moveScar(blockNo);
        return httpMessage;
    }


    @RequestMapping("addScar.do")
    @ResponseBody
    public HttpMessage addScar(String  blockNo,String level) {
        HttpMessage httpMessage = webService.addScar(blockNo,level);
        return httpMessage;
    }

    @RequestMapping("recovryException.do")
    @ResponseBody
    public HttpMessage recovryException(String  blockNo) {
        HttpMessage httpMessage = webService.recovryException(blockNo);
        return httpMessage;
    }



    @RequestMapping("deleteJob.do")
    @ResponseBody
    public HttpMessage deleteJob(String mckey) {
        HttpMessage httpMessage = webService.deleteJob(mckey);
        return httpMessage;
    }

    @RequestMapping("chargeFinish.do")
    @ResponseBody
    public HttpMessage chargeFinish(String blockNo) {
        HttpMessage httpMessage = webService.chargeFinish(blockNo);
        return httpMessage;
    }

    @RequestMapping("chargeStart.do")
    @ResponseBody
    public HttpMessage chargeStart(String blockNo) {
        HttpMessage httpMessage = webService.chargeStart(blockNo);
        return httpMessage;
    }

    @RequestMapping("onTheMLCar.do")
    @ResponseBody
    public HttpMessage onTheMLCar(String blockNo) {
        HttpMessage httpMessage = webService.onTheMLCar(blockNo);
        return httpMessage;
    }
    @RequestMapping("getTheSCCar.do")
    @ResponseBody
    public HttpMessage getTheSCCar(String blockNo) {
        HttpMessage httpMessage = webService.getTheSCCar(blockNo);
        return httpMessage;
    }
    @RequestMapping("deleteData.do")
    @ResponseBody
    public HttpMessage deleteData(String blockNo) {
        HttpMessage httpMessage = webService.deleteData(blockNo);
        return httpMessage;
    }
}

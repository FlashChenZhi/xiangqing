package com.wms.action;

import com.util.common.HttpMessage;
import com.wms.service.BlockQueryService;
import com.wms.service.MessageQueryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by wangfan
 * Created on 2017/3/11.
 */
@Controller
@RequestMapping("/message")
public class MessageQueryAction {

    @Resource
    private MessageQueryService messageQueryService;

        @RequestMapping("searchMessage.do")
    @ResponseBody
    public HttpMessage searchMessage(int currentPage, String mcKey) {
        HttpMessage httpMessage = messageQueryService.searchMessage(currentPage,mcKey);
        return httpMessage;
    }

    @RequestMapping("sendMsg.do")
    @ResponseBody
    public HttpMessage sendMsg(int id) {
        HttpMessage httpMessage = messageQueryService.sendMsg(id);
        return httpMessage;
    }

}

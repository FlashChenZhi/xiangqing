package com.webservice.action;

import com.util.common.Const;
import com.util.common.ContentUtil;
import com.util.common.HttpMessage;
import com.webservice.serivce.OppleService;
import com.webservice.vo.PushOrderResultVo;
import com.webservice.vo.PushOrderVo;
import com.webservice.vo.RetrievalSuccessVo;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by van on 2017/11/22.
 */
@Controller
@RequestMapping("/opple/order")
public class OppleAction {

    @Resource
    private OppleService oppleService;

    @RequestMapping(value = "pushOrder.do")
    @ResponseBody
    public List<PushOrderResultVo> pushOrder(@RequestBody List<PushOrderVo> pushOrderVos) {

        return oppleService.pushOrder(pushOrderVos);
    }

    @RequestMapping(value = "cancelOrder.do")
    @ResponseBody
    public HttpMessage cancelOrder(String orderNo){
       return oppleService.cancelOrder(orderNo);
    }


}

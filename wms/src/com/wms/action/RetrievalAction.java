package com.wms.action;

import com.util.common.HttpMessage;
import com.wms.vo.AddBatchVo;
import com.wms.service.RetrievalService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by van on 2017/12/14.
 */
@Controller
@RequestMapping("/retrieval")
public class RetrievalAction {

    @Resource
    private RetrievalService retrievalService;

    @RequestMapping("/searchOutSeaRetrieval.do")
    @ResponseBody
    public HttpMessage searchOutSeaRetrieval(String orderNo) {
        return retrievalService.searchOutRetrieval(orderNo);
    }

    @RequestMapping("/addBatch.do")
    @ResponseBody
    public HttpMessage addBatch(AddBatchVo addBatchVo) {
        return retrievalService.addBatch(addBatchVo);
    }

    @RequestMapping("/deleteBatch.do")
    @ResponseBody
    public HttpMessage delete(AddBatchVo addBatchVo) {
        return retrievalService.deleteBatch(addBatchVo);
    }

    @RequestMapping("/searchBatch.do")
    @ResponseBody
    public HttpMessage searchOrderBatch(String orderNo, String itemCode) {
        return retrievalService.searchOrderBatch(orderNo,itemCode);
    }

}

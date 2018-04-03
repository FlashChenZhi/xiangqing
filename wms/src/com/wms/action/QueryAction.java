package com.wms.action;

import com.wms.service.QueryService;
import com.util.common.HttpMessage;
import com.wms.vo.PerformanceVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/10/25.
 */
@Controller
@RequestMapping("/query")
public class QueryAction {


    @Resource
    private QueryService queryService;

    /**
     * 在线任务查询 再显示
     *
     * @return
     */
    @RequestMapping("/onlineTask.do")
    @ResponseBody
    public HttpMessage onlineTask(int currentPage) {
        return queryService.onlineTask(currentPage);
    }

    @RequestMapping("/asrsJobQuery.do")
    @ResponseBody
    public HttpMessage asrsJobQuery(int currentPage) {
        return queryService.asrsJobQuery(currentPage);
    }

    /**
     * 在线任务查询 作业开始
     *
     * @return
     */
    @RequestMapping("/startTask.do")
    @ResponseBody
    public HttpMessage startTask() {
        return queryService.startTask();
    }

    /**
     * 在线任务查询 作业结束
     *
     * @return
     */
    @RequestMapping("/endTask.do")
    @ResponseBody
    public HttpMessage endTask() {
        return queryService.endTask();
    }


    /**
     * 库存查询
     *
     * @param performanceVo
     * @return
     */
    @RequestMapping("/inventory.do")
    @ResponseBody
    public HttpMessage inventoryQuery(PerformanceVo performanceVo) {
        return queryService.inventoryQuery(performanceVo);
    }

    /**
     * 指定出库
     *
     * @param performanceVo
     * @return
     */
    @RequestMapping("/outinventory.do")
    @ResponseBody
    public HttpMessage outinventory(PerformanceVo performanceVo) {
        return queryService.outInventory(performanceVo);
    }

    /**
     * 商品查询
     *
     * @param skuCode,currentPage
     * @return
     */
    @RequestMapping("/searchSku.do")
    @ResponseBody
    public HttpMessage searchSku(String skuCode, int currentPage) {
        return queryService.searchSku(skuCode, currentPage);
    }

}

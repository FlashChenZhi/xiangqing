package com.order.action;

import com.order.service.AsrsService;
import com.order.vo.*;
import com.util.common.BaseReturnObj;
import com.util.common.PagerReturnObj;
import com.util.common.ReturnObj;
import com.util.pages.GridPages;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by van on 2018/1/15.
 */
@Controller
@RequestMapping(value = "/asrs")
public class AsrsAction {

    @Resource
    private AsrsService asrsService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public PagerReturnObj<List<AsrsVo>> searchAsrsjob(SearchAsrsVo vo, GridPages pages) {
        return asrsService.searchAsrsjob(vo, pages);
    }

    @RequestMapping(value = "/searchAsrsJobLog", method = RequestMethod.POST)
    @ResponseBody
    public PagerReturnObj<List<AsrsLogVo>> searchLog(SearchAsrsLogVo vo, GridPages pages) {
        return asrsService.searchAsrsjobLog(vo, pages);
    }

    @RequestMapping(value = "/searchSystemLog", method = RequestMethod.POST)
    @ResponseBody
    public PagerReturnObj<List<SystemLogVo>> searchSystemLog(SearchAsrsLogVo vo, GridPages pages) {
        return asrsService.searchSystemLog(vo, pages);
    }

    @RequestMapping(value = "/finish", method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj finish(String mcKey) {
        return asrsService.finish(mcKey);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj delete(String mcKey) {
        return asrsService.delete(mcKey);
    }

    @RequestMapping(value = "/exceptionRetrieval", method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj exceptionRetrieval(String locationNo) {
        return asrsService.exceptionRetrieval(locationNo);
    }

    @RequestMapping(value = "/duplicatedStorage", method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj duplicatedStorage(String mckey) {
        return asrsService.duplicatedStorage(mckey);
    }


    @RequestMapping(value = "/statistic", method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<List<StatisticVo>> statistic(String beginDate, String endDate, String jobType) {
        return asrsService.statistic(beginDate, endDate, jobType);
    }

}

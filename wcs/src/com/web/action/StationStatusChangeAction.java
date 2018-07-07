package com.web.action;

import com.asrs.domain.Station;

import com.web.service.StationStatusChangeServie;
import com.web.vo.ReturnObj;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/stationStatusChange")
public class StationStatusChangeAction {
    @Resource
    private StationStatusChangeServie stationStatusChangeServie;

    @RequestMapping(value = "findStatusChange.do",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<String> findStatusChange(String stationNo){
        return  stationStatusChangeServie.findStatusChange(stationNo);
     }

    @RequestMapping(value = "updateStatusChange.do",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<String> updateStatusChange(String pattern,String stationNo){
        return  stationStatusChangeServie.updateStatusChange(pattern,stationNo);
    }

    @RequestMapping(value = "findStationStatus.do",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<List<Map<String,Object>>> findStationStatus(int current, int defaultPageSize){
        int startIndex = (current-1)*defaultPageSize;
        return  stationStatusChangeServie.findStationStatus(startIndex,defaultPageSize);
    }
}

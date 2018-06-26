package com.web.action;

import com.asrs.domain.Station;

import com.web.service.StationStatusChangeServie;
import com.web.vo.ReturnObj;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

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
}

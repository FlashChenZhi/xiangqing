package com.master.action;

import com.master.service.PutInStorageService;
import com.master.vo.SkuVo2;
import com.util.common.BaseReturnObj;
import com.util.common.PagerReturnObj;
import com.util.common.ReturnObj;
import com.wms.domain.Sku;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/master/putInStorage")
public class PutInStorageAction {

    @Resource
    private PutInStorageService putInStorageService;
    /**
     * 获取商品代码
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/getCommodityCode",method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<List<Sku>> getCommodityCode(){
        return putInStorageService.getCommodityCode();
    }
    /**
     * 设定任务
     * @param palletCode 托盘号
     * @param stationNo 站台
     * @param skuName 商品名称
     * @param num 数量
     * @return "0"设定成功，"1"设定失败
     * @throws IOException
     */
    @RequestMapping(value = "/addTask",method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj addTask(String palletCode, String stationNo, String skuName,int num) throws IOException{
        palletCode = URLDecoder.decode(palletCode,"utf-8");
        System.out.println("托盘号："+palletCode+";站台："+stationNo+";商品名称："+skuName+";数量："+num);
        return putInStorageService.addTask(palletCode,stationNo,skuName,num);
    }
    /*
     * @author：ed_chen
     * @date：2018/3/4 17:48
     * @description：查询入库设定任务记录
     * @param
     * @return：com.util.common.BaseReturnObj
     */
    @RequestMapping(value = "/findPutInStorageOrder",method = RequestMethod.POST)
    @ResponseBody
    public PagerReturnObj<List<Map<String,Object>>> findPutInStorageOrder(int current, int defaultPageSize){
        int startIndex = (current-1)*defaultPageSize;
        return  putInStorageService.findPutInStorageOrder(startIndex,defaultPageSize);
    }
    /*
     * @author：ed_chen
     * @date：2018/3/10 18:34
     * @description： 删除入库任务
     * @param selectedRowKeysString
     * @return：com.util.common.BaseReturnObj
     */
    @RequestMapping(value = "/deleteTask",method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj deleteTask(String selectedRowKeysString){
        System.out.println(selectedRowKeysString);
        return putInStorageService.deleteTask(selectedRowKeysString);
    }
}

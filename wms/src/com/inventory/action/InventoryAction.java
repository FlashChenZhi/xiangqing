package com.inventory.action;

import com.inventory.service.InventoryService;
import com.inventory.vo.*;
import com.util.common.BaseReturnObj;
import com.util.common.PagerReturnObj;
import com.util.common.ReturnObj;
import com.util.excel.ExcelExportUtils;
import com.util.pages.GridPages;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by van on 2018/1/14.
 */
@Controller
@RequestMapping(value = "/inventory")
public class InventoryAction {

    @Resource
    private InventoryService inventoryService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public PagerReturnObj<List<InventoryVo>> list(SearchInventoryVo searchInventoryVo, GridPages pages) {
        return inventoryService.list(searchInventoryVo, pages);
    }

//    @RequestMapping(value = "/dailylist", method = RequestMethod.POST)
//    @ResponseBody
//    public PagerReturnObj<List<InventoryVo>> dailylist(SearchInventoryVo searchInventoryVo, GridPages pages) {
//        return inventoryService.dailylist(searchInventoryVo, pages);
//    }

    @RequestMapping(value = "/transfer.do", method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj transfer(String barCode,String toLocation) {
        return inventoryService.transfer(barCode, toLocation);
    }


    @RequestMapping(value = "/searchLog", method = RequestMethod.POST)
    @ResponseBody
    public PagerReturnObj<List<InventoryLogVo>> searchLog(SearchInvLogVo vo, GridPages pages) {
        return inventoryService.searchLog(vo, pages);
    }


    @RequestMapping(value = "/inventoryMap", method = RequestMethod.GET)
    @ResponseBody
    public ReturnObj<List<AutoLocationListVo>> inventoryMap() {
        return inventoryService.inventoryMap();
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj transfer(String palletNo) {
        return inventoryService.transfer(palletNo);
    }


    @RequestMapping(value = "/searchTransferSuggest", method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<List<TransferVo>> searchTransferSuggest(String position) {
        return inventoryService.searchTransferSuggest(position);
    }

//    @RequestMapping(value = "/batchTransfer", method = RequestMethod.POST)
//    @ResponseBody
//    public BaseReturnObj batchTransfer(String position, String area, Integer bay, Integer level) {
//        return inventoryService.transfer(position, area, bay, level);
//    }

    @RequestMapping(value = "/frozen", method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj frozen(String barcode, String batchNo, String skuCode) {
        return inventoryService.frozen(barcode, batchNo, skuCode);
    }

    @RequestMapping(value = "/realease", method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj realease(String barcode, String batchNo, String skuCode) {
        return inventoryService.realease(barcode, batchNo, skuCode);
    }

    @RequestMapping(value = "/inventoryWarning", method = RequestMethod.POST)
    @ResponseBody
    public ReturnObj<List<InventoryWarningVo>> inventoryWarning(String type) {
        return inventoryService.inventoryWarning(type);
    }

//    @RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
//    public void exportExcel(SearchInventoryVo vo, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        ReturnObj<HSSFWorkbook> returnObj = inventoryService.generateExcelExportFile(vo);
//        if (returnObj.isSuccess())
//            ExcelExportUtils.exportExcelToClient(returnObj.getRes(), "库存一览.xls", response);
//    }
//
//    @RequestMapping(value = "/exportInventoLog", method = RequestMethod.GET)
//    public void exportInventoLog(SearchInvLogVo vo, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        ReturnObj<HSSFWorkbook> returnObj = inventoryService.exportInventoLog(vo);
//        if (returnObj.isSuccess())
//            ExcelExportUtils.exportExcelToClient(returnObj.getRes(), "库存一览.xls", response);
//    }


//    @RequestMapping(value = "/exportDailyExcel", method = RequestMethod.GET)
//    public void exportDailyExcel(SearchInventoryVo vo, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        ReturnObj<HSSFWorkbook> returnObj = inventoryService.generateDailyExcelExportFile(vo);
//        if (returnObj.isSuccess())
//            ExcelExportUtils.exportExcelToClient(returnObj.getRes(), "库存日结一览.xls", response);
//    }

}

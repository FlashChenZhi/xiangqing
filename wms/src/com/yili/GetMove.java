package com.yili;

import commonservice.CommonService;

/**
 * Created by van on 2017/12/29.
 */
public class GetMove {

    public static void main(String[] args) {
        CommonService service = new CommonService();
        String result = service.getCommonServiceSoap().moveInvLocation("");
        System.out.println(result);

    }
}

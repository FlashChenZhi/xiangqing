package com;

import com.asrs.business.msgProc.MsgProcess;

/**
 * @Author: ed_chen
 * @Date: Create in 12:38 2018/4/26
 * @Description:
 * @Modified By:
 */
public class Test {

    @org.junit.Test
    public void test(){
        //获取类所在的包
        System.out.println(MsgProcess.class.getPackage().getName() );


    }
}


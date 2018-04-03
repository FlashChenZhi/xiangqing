package com.thread.threads.handler;

import com.asrs.domain.Location;
import com.asrs.message.Message35;
import com.thread.blocks.Block;
import com.thread.blocks.SCar;
import com.thread.blocks.Srm;
import org.apache.commons.lang.StringUtils;

/**
 * Created by van on 2017/12/27.
 */
public class SrmHandler extends MsgHandler {

    private Message35 message35;
    private Srm srm;

    public SrmHandler(Message35 msg35, Srm srm) {
        super(msg35);
        this.message35 = msg35;
        this.srm = srm;
    }

    /**
     * 移动
     */
    @Override
    public void move() {
        super.move();
        //升降机移动
        if (message35.Station.equals("0000")) {
            srm.setDock(null);
            srm.setBay(Integer.parseInt(message35.Bay));
            srm.setLevel(Integer.parseInt(message35.Level));
            Location toLoc = Location.getByBankBayLevel(Integer.parseInt(message35.Bank), srm.getBay(), srm.getLevel(), srm.getPosition());
            srm.setActualArea(toLoc.getActualArea());
            if (StringUtils.isNotBlank(srm.getsCarBlockNo())) {
                SCar sCar = (SCar) Block.getByBlockNo(srm.getsCarBlockNo());
                sCar.setBay(srm.getBay());
                sCar.setLevel(srm.getLevel());
                sCar.setActualArea(srm.getActualArea());
            }
        } else {
            srm.setDock(message35.Station);
        }
        srm.setCheckLocation(true);
    }

    /**
     * 移栽取货
     */
    @Override
    public void moveCarryGoods() {
        super.moveCarryGoods();
        srm.generateMckey(message35.McKey);
    }

    /**
     * 移栽卸货
     */
    @Override
    public void moveUnloadGoods() {
        super.moveUnloadGoods();
        srm.clearMckeyAndReservMckey();
    }

    /**
     * 接子车
     */
    @Override
    public void loadCar() {
        super.loadCar();
        SCar sCar = (SCar) Block.getByBlockNo(message35.Station);
        srm.setsCarBlockNo(sCar.getBlockNo());

        if (StringUtils.isNotBlank(sCar.getMcKey())) {
            srm.generateMckey(sCar.getMcKey());
        }

    }

    /**
     * 卸子车
     */
    @Override
    public void unloadCar() {
        super.unloadCar();

        srm.setsCarBlockNo(null);
        srm.clearMckeyAndReservMckey();

    }
}

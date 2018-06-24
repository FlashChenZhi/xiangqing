package com.asrs.business.msgProc.msg35ProServiceImpl;

import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.msgProc.msg35ProcService.Msg35ProcService;
import com.asrs.domain.AsrsJob;
import com.asrs.message.Message35;
import com.thread.blocks.Block;
import com.thread.blocks.MCar;
import com.thread.blocks.SCar;
import com.thread.blocks.Srm;
import org.apache.commons.lang.StringUtils;

/**
 * @Author: ed_chen
 * @Date: Create in 23:31 2018/6/21
 * @Description:
 * @Modified By:
 */
public class Msg35ProcST2STServiceImpl implements Msg35ProcService {
    private Message35 message35;
    private AsrsJob aj;
    private Block block;

    public Msg35ProcST2STServiceImpl(Message35 message35, AsrsJob aj, Block block) {
        this.message35 = message35;
        this.aj = aj;
        this.block = block;
    }

    @Override
    public void sCar35Proc() throws Exception {

    }

    @Override
    public void srm35Proc() throws Exception {
        if (message35.isMoveCarryGoods()) {
            block.generateMckey(message35.McKey);
        } else if (message35.isMoveUnloadGoods()) {
            block.clearMckeyAndReservMckey();
        } else if (message35.isMove()) {
            Srm srm = (Srm) block;
            srm.setLevel(Integer.parseInt(message35.Level));
            srm.setBay(Integer.parseInt(message35.Bay));
            srm.setDock(message35.Station);
            srm.setCheckLocation(true);
            if (StringUtils.isNotBlank(srm.getsCarBlockNo())) {
                SCar sCar = (SCar) Block.getByBlockNo(srm.getsCarBlockNo());
                sCar.setLevel(srm.getLevel());
                sCar.setBay(srm.getBay());
            }
        }
    }

    @Override
    public void mCar35Proc() throws Exception {
        if (message35.isMoveCarryGoods()) {
            block.generateMckey(message35.McKey);
        } else if (message35.isMoveUnloadGoods()) {
            block.clearMckeyAndReservMckey();
        } else if (message35.isMove()) {
            MCar mCar = (MCar) block;
//                                mCar.setLevel(Integer.parseInt(message35.Level));
            mCar.setBay(Integer.parseInt(message35.Bay));
            mCar.setDock(message35.Station);
            mCar.setCheckLocation(true);
            if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                sCar.setLevel(mCar.getLevel());
                sCar.setBay(mCar.getBay());
            }
        }
    }

    @Override
    public void lift35Proc() throws Exception {

    }

    @Override
    public void converyor35Proc() throws Exception {
        if (message35.isMoveCarryGoods()) {
            block.generateMckey(message35.McKey);
        } else if (message35.isMoveUnloadGoods()) {
            block.clearMckeyAndReservMckey();
        }
    }

    @Override
    public void station35Proc() throws Exception {
        if (message35.isMoveCarryGoods()) {
            block.generateMckey(message35.McKey);
            aj.setStatus(AsrsJobStatus.DONE);
        } else if (message35.isMoveUnloadGoods()) {
            block.clearMckeyAndReservMckey();
        }
    }
}

package com.thread.threads.handler;

import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobStatusDetail;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.ReasonCode;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.message.Message35;
import com.asrs.xml.util.XMLUtil;
import com.domain.XMLbean.Envelope;
import com.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.domain.XMLbean.XMLList.ControlArea.Receiver;
import com.domain.XMLbean.XMLList.ControlArea.RefId;
import com.domain.XMLbean.XMLList.ControlArea.Sender;
import com.domain.XMLbean.XMLList.DataArea.DAList.MovementReportDA;
import com.domain.XMLbean.XMLList.DataArea.FromLocation;
import com.domain.XMLbean.XMLList.DataArea.ToLocation;
import com.domain.XMLbean.XMLList.MovementReport;
import com.domain.consts.xmlbean.XMLConstant;
import com.thread.blocks.Block;
import com.thread.blocks.SCar;
import com.thread.blocks.Srm;
import com.util.hibernate.HibernateUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by van on 2017/12/27.
 */
public class SCarHandler extends MsgHandler {

    private SCar sCar;
    private Message35 message35;

    private AsrsJob aj;

    public SCarHandler(Message35 msg35, SCar sCar) {
        super(msg35);
        this.message35 = msg35;
        this.sCar = sCar;
        aj = AsrsJob.getAsrsJobByMcKey(msg35.McKey);
    }

    /**
     * 卸货
     */
    @Override
    public void unloadGoods() {
        super.unloadGoods();
        Location location = Location.getByLocationNo(aj.getToLocation());
        sCar.setOnMCar(null);
        //入库完成，发送消息给wms
        putawayFinish(aj, location);

        sCar.setBank(location.getBank());
        if (aj.getStatus().equals(AsrsJobStatus.DONE)) {
//            aj.delete();
        } else {
            aj.setStatus(AsrsJobStatus.DONE);
        }

        sCar.clearMckeyAndReservMckey();

    }

    /**
     * 上车
     */
    @Override
    public void onCar() {
        super.onCar();
        sCar.setOnMCar(message35.Station);
        sCar.setBank(0);

        Block block1 = Block.getByBlockNo(message35.Station);
        if (block1 instanceof Srm) {
            Srm srm = (Srm) block1;
            if (StringUtils.isNotEmpty(srm.getMcKey())
                    && StringUtils.isBlank(sCar.getReservedMcKey())) {
                //如果子车ReserveMckey为空
                sCar.generateReserveMckey(message35.McKey);
            }
        }
    }

    /**
     * 下车
     */
    @Override
    public void offCar() {
        super.offCar();
        sCar.setBank(Integer.parseInt(message35.Bank));
        sCar.setOnMCar(null);
        sCar.generateMckey(message35.McKey);

    }

    /**
     * 取货
     */
    @Override
    public void pickingGoods() {
        super.pickingGoods();
        sCar.clearMckeyAndReservMckey();
        sCar.setOnMCar(message35.Station);

        Query query = HibernateUtil.getCurrentSession().createQuery("from AsrsJob where type=:ajType and statusDetail=:detail ");
        query.setParameter("ajType", AsrsJobType.RETRIEVAL);
        query.setParameter("detail", AsrsJobStatusDetail.WAITING);
        query.setMaxResults(1);
        AsrsJob asrsJob = (AsrsJob) query.uniqueResult();
        if (asrsJob != null) {
            sCar.setReservedMcKey(asrsJob.getMcKey());
            asrsJob.setStatusDetail(AsrsJobStatusDetail.ACCEPTED);
        }
    }

    private void putawayFinish(AsrsJob aj, Location location) {

        //创建ControlArea控制域对象
        Sender sd = new Sender();
        sd.setDivision(XMLConstant.COM_DIVISION);

        Receiver receiver = new Receiver();
        receiver.setDivision(XMLConstant.WMS_DIVISION);

        RefId ri = new RefId();
        ri.setReferenceId(aj.getWmsMckey());

        ControlArea ca = new ControlArea();
        ca.setSender(sd);
        ca.setReceiver(receiver);
        ca.setRefId(ri);
        ca.setCreationDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        FromLocation fromLocation = new FromLocation();
        List<String> list = new ArrayList<>(3);
        fromLocation.setMHA("");
        list.add("");
        list.add("");
        list.add("");
        fromLocation.setRack(list);

        ToLocation toLocation = new ToLocation();
        // TODO: 2017/5/1 修改货位
        toLocation.setMHA(aj.getFromStation());
        List<String> rack = new ArrayList<>(3);
        rack.add(String.valueOf(location.getBank()));
        rack.add(String.valueOf(location.getBay()));
        rack.add(String.valueOf(location.getLevel()));
        toLocation.setRack(rack);

        //创建MovementReportDA数据域对象
        MovementReportDA mrd = new MovementReportDA();
        mrd.setFromLocation(fromLocation);
        mrd.setStUnitId(aj.getBarcode());
        mrd.setReasonCode(ReasonCode.PUTAWAYFINISHED);
        mrd.setToLocation(toLocation);
        //创建MovementReport响应核心对象
        MovementReport mr = new MovementReport();
        mr.setControlArea(ca);
        mr.setDataArea(mrd);
        //将MovementReport发送给wms
        Envelope el = new Envelope();
        el.setMovementReport(mr);
        try {
            XMLUtil.sendEnvelope(el);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}

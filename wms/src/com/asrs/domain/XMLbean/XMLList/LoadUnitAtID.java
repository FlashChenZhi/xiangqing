package com.asrs.domain.XMLbean.XMLList;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.TransportType;
import com.asrs.domain.XMLbean.Envelope;
import com.asrs.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.asrs.domain.XMLbean.XMLList.ControlArea.RefId;
import com.asrs.domain.XMLbean.XMLList.ControlArea.Sender;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.LoadUnitAtIdDA;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.TransportOrderDA;
import com.asrs.domain.XMLbean.XMLList.DataArea.FromLocation;
import com.asrs.domain.XMLbean.XMLList.DataArea.StUnit;
import com.asrs.domain.XMLbean.XMLList.DataArea.ToLocation;
import com.asrs.domain.XMLbean.XMLProcess;
import com.asrs.domain.consts.xmlbean.XMLConstant;
import com.asrs.xml.util.XMLUtil;
import com.util.common.Const;
import com.util.common.LogWriter;
import com.util.common.LoggerType;
import com.wms.domain.blocks.MCar;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.util.common.DateFormat;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;
import sun.plugin2.message.JavaObjectOpMessage;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-5
 * Time: 下午3:04
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "LoadUnitAtID")
public class LoadUnitAtID extends XMLProcess {


    @XStreamAlias("ControlArea")
    private ControlArea controlArea;

    @XStreamAlias("DataArea")
    private LoadUnitAtIdDA dataArea;

    @OneToOne(targetEntity = ControlArea.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "ControlAreaID", updatable = true)
    public ControlArea getControlArea() {
        return controlArea;
    }

    public void setControlArea(ControlArea controlArea) {
        this.controlArea = controlArea;
    }

    @OneToOne(targetEntity = LoadUnitAtIdDA.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "LoadUnitAtIdDAID", updatable = true)
    public LoadUnitAtIdDA getDataArea() {
        return dataArea;
    }

    public void setDataArea(LoadUnitAtIdDA dataArea) {
        this.dataArea = dataArea;
    }

    @XStreamOmitField
    private int id;

    @Id
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "LOADUNITATID_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public void execute() {

            try {
                Transaction.begin();
                //Session session = HibernateUtil.getCurrentSession();

                String barcode = dataArea.getScanData().replaceAll("_","");
                String stationNo = dataArea.getXMLLocation().getMHA();
                Station station = Station.getNormalStation(stationNo);
                //ReceiptLpnVo view = GetReceiptLPN.getReceipt(barcode);//与上位交互

                Query jobQuery = HibernateUtil.getCurrentSession().createQuery("from Job j where j.fromStation = :station and j.status = :waiting order by j.createDate")
                        .setString("station",stationNo)
                        .setString("waiting", AsrsJobStatus.WAITING)
                        .setMaxResults(1);
                Job job = (Job) jobQuery.uniqueResult();
                if(station!=null && AsrsJobType.PUTAWAY.equals(station.getMode())) {
                    if (job == null) {
                        job=createJob(stationNo);
                    }
                    barcode=job.getContainer();
                    //分配货位，并向队列中压入TransportOrder
                    Location newLocation = getToLocation(stationNo,job,barcode);
                }else {
                    System.out.println("入库站台不是正常状态！");
                }



                 /* job = new Job();
                    job.setToLocation(newLocation);
                    job.setMcKey(ri.getReferenceId());
                    job.setStatus("1");
                    job.setFromStation(fromLocation.getMHA());
                    job.setContainer(barcode);
                    job.setType("01");
                    if(view == null || "1101".equals(stationNo)) {
                        job.setSkuCode(Const.EMPTY_PALLET);
                        job.setLotNum(Const.EMPTY_PALLET);
                    }else{
                        job.setSkuCode(view.getSkuID());
                        job.setLotNum(view.getLotAttr05());
                    }
                    job.setToStation(mCar.getBlockNo());
                    HibernateUtil.getCurrentSession().save(job);*/
                Transaction.commit();
            } catch (Exception e) {
                Transaction.rollback();
                LogWriter.error(LoggerType.XMLMessageInfo, e.getMessage());
                e.printStackTrace();
            }

    }
    /*
     * @author：ed_chen
     * @date：2018/5/3 16:46
     * @description：分配货位，并向队列中压入TransportOrder
     * @param stationNo
     * @param job
     * @param barcode
     * @return：void
     */
    public Location getToLocation(String stationNo,Job job,String barcode) throws Exception{
            //站台判断
            Location newLocation;
            if("1101".equals(stationNo)){
                //入库站台为1101时
                //判断1102是否被禁用
                Station station1 = Station.getNormalStation("1102");
                if(station1!=null){
                    //1102没有被禁用，1101分配1号巷道货位
                    newLocation = Location.getEmptyLocation(job.getSkuCode(),job.getLotNum(),"1");
                }else{
                    //1102被禁用，1101分配1、2号巷道货位
                    newLocation = Location.getEmptyLocation(job.getSkuCode(),job.getLotNum(),"0");
                }
            }else {
                //入库站台为1102时
                //判断1101是否被禁用
                Station station1 = Station.getNormalStation("1101");
                if(station1!=null){
                    //1101没有被禁用，1102分配2号巷道货位
                    newLocation = Location.getEmptyLocation(job.getSkuCode(),job.getLotNum(),"2");
                }else{
                    //1101被禁用，1102分配1、2号巷道货位
                    newLocation = Location.getEmptyLocation(job.getSkuCode(),job.getLotNum(),"0");
                }
            }
            if(newLocation != null) {
                newLocation.setReserved(true);
            }else{
                throw new Exception("空货位不足" );
            }
            //开始
            //创建ControlArea控制域对象
            ControlArea ca = new ControlArea();
            Sender sd = new Sender();
            sd.setDivision(XMLConstant.COM_DIVISION);
            ca.setSender(sd);

            RefId ri = new RefId();
            ri.setReferenceId(Mckey.getNext());
            ca.setRefId(ri);
            ca.setCreationDateTime(new DateFormat().format(new Date(), DateFormat.YYYYMMDDHHMMSS));
            Query query  = HibernateUtil.getCurrentSession().createQuery("from MCar where position =:po and level =:lv");
            query.setParameter("po",newLocation.getPosition());
            query.setParameter("lv",newLocation.getLevel());
            query.setMaxResults(1);
            MCar mCar = (MCar) query.uniqueResult();

            //创建TransportOrderDA数据域对象
            TransportOrderDA toa = new TransportOrderDA();
            toa.setTransportType(TransportType.PUTAWAY);
            ToLocation toLocation = new ToLocation();
            toLocation.setMHA(mCar.getBlockNo());
            List<String> racks = new ArrayList<>();
            racks.add(newLocation.getBank()+"");
            racks.add(newLocation.getBay()+"");
            racks.add(newLocation.getLevel()+"");
            toLocation.setRack(racks);
            toa.setToLocation(toLocation);
            StUnit su = new StUnit();
            su.setStUnitID(barcode);
            toa.setStUnit(su);
            toa.setToLocation(toLocation);

            FromLocation fromLocation = new FromLocation();
            fromLocation.setMHA(dataArea.getXMLLocation().getMHA());
            toa.setFromLocation(fromLocation);

            //创建TransportOrder核心对象
            TransportOrder to = new TransportOrder();
            to.setControlArea(ca);
            to.setDataArea(toa);
            //结束
            Envelope el = new Envelope();
            el.setTransportOrder(to);
            XMLUtil.sendEnvelope(el);
            return newLocation;
    }

    public Job createJob(String stationNo) throws Exception{
        Session session = HibernateUtil.getCurrentSession();
        String barcode = null;
        boolean flag = false;
        for(int i =0;i<10;i++){
            barcode = UUID.randomUUID().toString().substring(0, 15);
            Container container = Container.getByBarcode(barcode);
            if(container==null){
                flag=true;
                break;
            }
        }
        Job job = new Job();
        if(flag){
            session.save(job);
            job.setFromStation(stationNo);
            job.setContainer(barcode);//托盘号
            job.setCreateDate(new Date());
            /*if (zhantai.equals("1101")) {
                job.setToStation("ML01");
            }
            if (zhantai.equals("1301")) {
                job.setToStation("ML02");
            }*/
            job.setType(AsrsJobType.PUTAWAY);
            job.setMcKey(Mckey.getNext());
            job.setStatus(AsrsJobStatus.WAITING);
            job.setSkuCode("test");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //以天数为批次
            String lotNum = sdf.format(new Date());
            job.setLotNum(lotNum);
            JobDetail jobDetail = new JobDetail();
            session.save(jobDetail);
            jobDetail.setJob(job);
            jobDetail.setQty(new BigDecimal(Const.containerQty));//托盘上的货物数量

            InventoryView inventoryView = new InventoryView();
            session.save(inventoryView);

            inventoryView.setPalletCode(barcode);//托盘号
            inventoryView.setQty(new BigDecimal(Const.containerQty));//托盘上的货物数量
            inventoryView.setSkuCode(Const.skuCode); //商品代码
            inventoryView.setSkuName(Const.skuName);//商品名称
            inventoryView.setWhCode(Const.warehouseCode);//仓库代码
            inventoryView.setLotNum(lotNum);//批次号
        }else{
            throw new Exception("托盘号已存在");
        }



        return job;
    }

}

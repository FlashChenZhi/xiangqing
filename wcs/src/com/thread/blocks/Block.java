package com.thread.blocks;

import com.asrs.business.consts.AsrsJobStatusDetail;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.domain.RouteDetail;
import com.util.hibernate.*;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;
import org.hibernate.*;

import javax.persistence.*;
import javax.persistence.Version;
import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
@Entity
@Table(name = "Block")
@DiscriminatorColumn(name = "type")
public abstract class Block {
    protected String blockNo;
    protected boolean waitingResponse;
    private String reservedMcKey;
    protected String plcName;
    protected String mcKey;
    protected String status;
    protected String error;
    private String wareHouse;
    private int version;

    public static final String STATUS_RUN = "1";
    public static final String STATUS_CHARGE = "3";
    public static final String STATUS_CHARGE_OVER = "4";

    public Block(String blockNo, String plcName) {
        this.blockNo = blockNo;
        waitingResponse = false;
        this.plcName = plcName;
    }

    protected Block() {
    }

    @Id
    @Column(name = "blockNo")
    public String getBlockNo() {
        return blockNo;
    }

    public void setBlockNo(String blockNo) {
        this.blockNo = blockNo;
    }

    @Version
    @Column(name = "VERSION")
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Basic
    @Column(name = "MCKEY")
    public String getMcKey() {
        return mcKey;
    }

    public void setMcKey(String mcKey) {
        this.mcKey = mcKey;
    }

    @Basic
    @Column(name = "waitingResponse")
    public boolean isWaitingResponse() {
        return waitingResponse;
    }

    public void setWaitingResponse(boolean waitingResponse) {
        this.waitingResponse = waitingResponse;
    }

    @Basic
    @Column(name = "PLCNAME")
    public String getPlcName() {
        return plcName;
    }

    public void setPlcName(String plcName) {
        this.plcName = plcName;
    }

    @Basic
    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Basic
    @Column(name = "reservedMcKey")
    public String getReservedMcKey() {
        return reservedMcKey;
    }

    public void setReservedMcKey(String reservedMcKey) {
        this.reservedMcKey = reservedMcKey;
    }

    @Basic
    @Column(name = "ERROR")
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Basic
    @Column(name = "WARE_HOUSE")
    public String getWareHouse() {
        return wareHouse;
    }

    public void setWareHouse(String wareHouse) {
        this.wareHouse = wareHouse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Block block = (Block) o;

        if (waitingResponse != block.waitingResponse) return false;
        if (blockNo != null ? !blockNo.equals(block.blockNo) : block.blockNo != null) return false;
//        if (nextBlockNo != null ? !nextBlockNo.equals(block.nextBlockNo) : block.nextBlockNo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = blockNo != null ? blockNo.hashCode() : 0;
        result = 31 * result + (waitingResponse ? 1 : 0);
//        result = 31 * result + (nextBlockNo != null ? nextBlockNo.hashCode() : 0);
        return result;
    }

    public static void main(String[] args) {
        com.util.hibernate.Transaction.begin();
        MCar mCar = (MCar) Block.getByBlockNo("MC02");
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey("0857");
        Lift lift = (Lift) mCar.getNextBlock(asrsJob.getType(), asrsJob.getToStation());
        Transaction.commit();

    }

    @Transient
    public Block getNextBlock(String jobType, String destStation) {
        org.hibernate.Query q = HibernateUtil.getCurrentSession().createQuery("from RouteDetail rd where rd.currentBlockNo = :currentBlockNo and rd.route.toStation = :toStation and rd.route.type = :type"+
                " and rd.route.status=:status")
                .setString("currentBlockNo", getBlockNo())
                .setString("toStation", destStation)
                .setString("type", jobType)
                .setString("status","1");

        List<RouteDetail> rds = q.list();

        if (rds.isEmpty()) {
            return null;
        }

        return getByBlockNo(rds.get(0).getNextBlockNo());
    }

    @Transient
    public Block getPreBlock(String mckey, String jobType) {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mckey);
        org.hibernate.Query q = HibernateUtil.getCurrentSession().createQuery("from RouteDetail  rd where rd.nextBlockNo=:currentBlock " +
                " and rd.route.type=:type and rd.route.fromStation=:fromStation and rd.route.status=:status")
                .setString("currentBlock", getBlockNo())
                .setString("type", jobType)
                .setString("fromStation", asrsJob.getFromStation())
                .setString("status","1");

        List<RouteDetail> rds = q.list();

        if (rds.isEmpty()) {
            return null;
        }

        return getByBlockNo(rds.get(0).getCurrentBlockNo());

    }

    @Transient
    public Block getPreBlock(String mckey, String jobType, String destStation) {
        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mckey);
        org.hibernate.Query q = HibernateUtil.getCurrentSession().createQuery("from RouteDetail  rd where rd.nextBlockNo=:currentBlock " +
                " and rd.route.toStation=:st and rd.route.type=:type and rd.route.fromStation=:fromStation and rd.route.status=:status")
                .setString("currentBlock", getBlockNo())
                .setString("type", jobType)
                .setString("st", asrsJob.getToStation())
                .setString("fromStation", asrsJob.getFromStation())
                .setString("status","1");

        List<RouteDetail> rds = q.list();

        if (rds.isEmpty()) {
            return null;
        }

        return getByBlockNo(rds.get(0).getCurrentBlockNo());

    }

    @Transient
    public static Block getByBlockNo(String blockNo) {
        if (StringUtils.isNotBlank(blockNo)) {
            Session session = HibernateUtil.getCurrentSession();
            return (Block) session.get(Block.class, blockNo);
        } else {
            return null;
        }
    }

    @Transient
    public void clearMckeyAndReservMckey() {
        setMcKey(null);
        setReservedMcKey(null);
    }

    @Transient
    public void generateMckey(String mckey) {
        setMcKey(mckey);
        setReservedMcKey(null);
    }

    @Transient
    public void generateReserveMckey(String mckey) {
        setMcKey(null);
        setReservedMcKey(mckey);
    }

    @Transient
    public Block getPreBlockByJobType(String jobType) {
        org.hibernate.Query q = HibernateUtil.getCurrentSession().createQuery("from RouteDetail  rd where rd.nextBlockNo=:currentBlock " +
                " and rd.route.type=:type  and rd.route.status=:status")
                .setString("currentBlock", getBlockNo())
                .setString("type", jobType)
                .setString("status","1");

        List<RouteDetail> rds = q.list();

        if (rds.isEmpty()) {
            return null;
        }

        return getByBlockNo(rds.get(0).getCurrentBlockNo());
    }

    @Transient
    public Block getPreBlockHasMckey(String jobType) {
        org.hibernate.Query query = HibernateUtil.getCurrentSession().createQuery("select d from RouteDetail d,Block b where d.currentBlockNo = b.blockNo and " +
                "d.nextBlockNo =:cb and b.mcKey is not null and d.route.type=:type and d.route.status=:status order by b.blockNo desc ")
                .setString("cb", getBlockNo()).setString("type", jobType)
                .setString("status","1");

        List<RouteDetail> rds = query.list();

        if (rds.isEmpty()) {
            return null;
        }

        return getByBlockNo(rds.get(0).getCurrentBlockNo());

    }

    @Transient
    public Block getPreBlockHasMckeyByLevel(String jobType,String blockNo) {
        org.hibernate.Query query = HibernateUtil.getCurrentSession().createQuery("select d from RouteDetail d,Block b where d.currentBlockNo = b.blockNo and " +
                "d.nextBlockNo =:cb and b.mcKey is not null and d.route.type=:type and d.route.status=:status order by b.blockNo desc")
                .setString("cb", getBlockNo()).setString("type", jobType)
                .setString("status","1");

        List<RouteDetail> rds = query.list();

        if (rds.isEmpty()) {
            return null;
        }

        return getByBlockNo(rds.get(0).getCurrentBlockNo());

    }
}

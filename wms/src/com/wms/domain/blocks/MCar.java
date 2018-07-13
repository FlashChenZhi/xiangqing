package com.wms.domain.blocks;

import com.asrs.business.consts.AsrsJobStatus;
import com.util.common.Const;
import com.util.hibernate.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/1.
 * 母车
 */
@Entity
@Table(name = "Block")
@DiscriminatorValue(value = "4")
public class MCar extends Block {
    private String sCarBlockNo;
    private boolean liftSide;
    private int bay;
    private int level;
    private String dock;
    private String liftNo;
    private String position; //位置 ： 1左，2:右
    private String actualArea;
    private Boolean checkLocation;//是否校准位置
    private Integer groupNo;
    private String cycle;


    @Basic
    @Column(name = "liftNo")
    public String getLiftNo() {
        return liftNo;
    }

    public void setLiftNo(String liftNo) {
        this.liftNo = liftNo;
    }

    @Basic
    @Column(name = "LEV")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Basic
    @Column(name = "liftSide")
    public boolean isLiftSide() {
        return liftSide;
    }

    public void setLiftSide(boolean liftSide) {
        this.liftSide = liftSide;
    }


    @Basic
    @Column(name = "BAY")
    public int getBay() {
        return bay;
    }

    public void setBay(int bay) {
        this.bay = bay;
    }

    @Basic
    @Column(name = "POSITION")
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Basic
    @Column(name = "DOCK")
    public String getDock() {
        return dock;
    }

    public void setDock(String dock) {
        this.dock = dock;
    }

    @Basic
    @Column(name = "SCARBLOCKNO")
    public String getsCarBlockNo() {
        return sCarBlockNo;
    }

    public void setsCarBlockNo(String sCarBlockNo) {
        this.sCarBlockNo = sCarBlockNo;
    }

    @Basic
    @Column(name = "ACTUALAREA")
    public String getActualArea() {
        return actualArea;
    }

    public void setActualArea(String actuaneArea) {
        this.actualArea = actuaneArea;
    }

    @Basic
    @Column(name = "CHECK_LOCATION")
    public Boolean getCheckLocation() {
        return checkLocation;
    }

    public void setCheckLocation(Boolean checkLocation) {
        this.checkLocation = checkLocation;
    }

    @Basic
    @Column(name = "GROUP_NO")
    public Integer getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(Integer groupNo) {
        this.groupNo = groupNo;
    }

    @Basic
    @Column(name = "CYCLE")
    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }


    @Transient
    public static MCar getMCarByGroupNo(Integer groupNo) {
        Query query = HibernateUtil.getCurrentSession().createQuery("from MCar where groupNo =:groupNo");
        query.setParameter("groupNo", groupNo);
        return (MCar) query.uniqueResult();
    }

    @Transient
    public static MCar getMCarByPosition(String position,int level) {
        org.hibernate.Query query = HibernateUtil.getCurrentSession().createQuery("from MCar where position =:po and level=:lv");
        query.setParameter("po", position);
        query.setParameter("lv",level);
        query.setMaxResults(1);
        return (MCar) query.uniqueResult();

    }

    @Transient
    public static List<Integer> getMCarsByPosition(String position) {
        org.hibernate.Query query = HibernateUtil.getCurrentSession().createQuery("select m.level as lev from MCar m " +
                "where m.position =:po order by m.level asc").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setParameter("po", position);
        List<Map<String,Integer>> list =query.list();
        List<Integer> list1 = new ArrayList<>();
        for(Map<String,Integer> map:list){
            list1.add(map.get("lev"));
        }
        return  list1;

    }

    @Transient
    public static List<Integer> getMCarByHasNotAsrsJob(String position) {
        //查找此区域有小车且没有任务的母车层
        org.hibernate.Query query = HibernateUtil.getCurrentSession().createQuery("select m.level as lev from MCar m,SCar s where not exists(" +
                "select 1 from AsrsJob a where (a.fromStation=m.blockNo or a.toStation=m.blockNo) and " +
                "a.status!=:status ) and m.position=:position and m.groupNo is not null and m.groupNo=s.groupNo " +
                "and s.power>:power order by m.level asc").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setParameter("status", AsrsJobStatus.DONE);
        query.setParameter("position", position);
        query.setParameter("power", Const.LOWER_POWER);
        List<Map<String,Integer>> list =query.list();
        List<Integer> list1 = new ArrayList<>();
        for(Map<String,Integer> map:list){
            list1.add(map.get("lev"));
        }
        return list1;

    }
}

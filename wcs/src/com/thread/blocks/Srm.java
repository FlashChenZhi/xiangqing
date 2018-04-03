package com.thread.blocks;

import com.asrs.domain.Location;
import com.util.hibernate.HibernateUtil;

import javax.persistence.*;

/**
 * Created by van on 2017/8/29.
 * 移动提升机
 */
@Entity
@Table(name = "Block")
@DiscriminatorValue(value = "6")
public class Srm extends Block {

    private String sCarBlockNo;
    private int bay;
    private int level;
    private String dock;
    private String position; //区域
    private String actualArea; //具体位置，堆垛机左右
    private Boolean checkLocation;//是否校准位置
    private Double moveSpead;//移动速度　　/min
    private Double upDownSpead; //升降速度   ／min
    private Double palletMoveSpead;//托盘移栽时间
    private Integer groupNo;//组号，用于子车和提升机，母车绑定
    private String cycle;

    @Basic
    @Column(name = "SCARBLOCKNO")
    public String getsCarBlockNo() {
        return sCarBlockNo;
    }

    public void setsCarBlockNo(String sCarBlockNo) {
        this.sCarBlockNo = sCarBlockNo;
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
    @Column(name = "LEV")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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
    @Column(name = "POSITION")
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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
    @Column(name = "MOVESPEAD")
    public Double getMoveSpead() {
        return moveSpead;
    }

    public void setMoveSpead(Double moveSpead) {
        this.moveSpead = moveSpead;
    }

    @Basic
    @Column(name = "UPDOWNSPEAD")
    public Double getUpDownSpead() {
        return upDownSpead;
    }

    public void setUpDownSpead(Double upDownSpead) {
        this.upDownSpead = upDownSpead;
    }

    @Basic
    @Column(name = "PALLETMOVESPEAD")
    public Double getPalletMoveSpead() {
        return palletMoveSpead;
    }

    public void setPalletMoveSpead(Double palletMoveSpead) {
        this.palletMoveSpead = palletMoveSpead;
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
    public boolean arrive(Location toLocation) {
        if (toLocation!=null && this.getLevel() == toLocation.getLevel() && this.getBay() == toLocation.getBay() && this.getActualArea().equals(toLocation.getActualArea()) && this.getCheckLocation())
            return true;
        return false;
    }


    @Transient
    public static Srm getSrmByGroupNo(Integer groupNo) {
        org.hibernate.Query query = HibernateUtil.getCurrentSession().createQuery("from Srm where groupNo =:groupNo");
        query.setParameter("groupNo", groupNo);
        return (Srm) query.uniqueResult();
    }

    @Transient
    public static Srm getSrmByPosition(String position) {
        org.hibernate.Query query = HibernateUtil.getCurrentSession().createQuery("from Srm where position =:po");
        query.setParameter("po", position);
        return (Srm) query.uniqueResult();

    }
}

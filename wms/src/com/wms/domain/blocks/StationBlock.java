package com.wms.domain.blocks;

import com.util.hibernate.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/10/28.
 */
@Entity
@Table(name = "Block")
@DiscriminatorValue(value = "3")
public class StationBlock extends Block {
    protected String stationNo;
    private Integer groupNo;//组号，绑定出库双站台
    private boolean outLoad;//出库时候货位是否身上

    @Basic
    @Column(name = "OUTLOAD")
    public boolean isOutLoad() {
        return outLoad;
    }

    public void setOutLoad(boolean outLoad) {
        this.outLoad = outLoad;
    }

    @Basic
    @Column(name = "GROUP_NO")
    public Integer getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(Integer group) {
        this.groupNo = group;
    }
    @Basic
    @Column(name = "stationNo")
    public String getStationNo() {
        return stationNo;
    }

    public void setStationNo(String stationNo) {
        this.stationNo = stationNo;
    }

    public static StationBlock getByStationNo(String stationNo) {
        Session session = HibernateUtil.getCurrentSession();
        StationBlock stationBlock = (StationBlock) session.createCriteria(StationBlock.class)
                .add(Restrictions.eq("stationNo", stationNo)).uniqueResult();
        return stationBlock;
    }
}
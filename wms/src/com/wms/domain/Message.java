package com.wms.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wangfan on 2017/1/5.
 */
@Entity
@Table(name = "MESSAGE")
public class Message {
    private int id;
    private String machineNo;
    private String type;
    private String status;
    private Date createDate;
    private String nextBlock;


    public static final String __TYPE = "type";
    public static final String __STATUS = "status";

    public static final String TYPE_OFFLINE = "1";
    public static final String TYPE_ONLINE = "2";
    public static final String TYPE_REMOVE_ABNORMAL = "3";
    public static final String TYPE_DELDATE = "4";
    public static final String TYPE_FINISH = "5";
    public static final String TYPE_ONCAR = "6";
    public static final String TYPE_LOADCAR = "7";


    public static final String STATUS_WAIT = "1";
    public static final String STATUS_SEND = "2";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, length = 8)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "MACHINE_NO")
    public String getMachineNo() {
        return machineNo;
    }

    public void setMachineNo(String machineNo) {
        this.machineNo = machineNo;
    }

    @Basic
    @Column(name = "TYPE")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }


    @Basic
    @Column(name = "NEXT_BLOCK")
    public String getNextBlock() {
        return nextBlock;
    }

    public void setNextBlock(String nextBlock) {
        this.nextBlock = nextBlock;
    }
}

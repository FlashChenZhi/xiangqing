package com.domain.XMLbean.XMLList.DataArea;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.domain.consts.xmlbean.XMLConstant;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-5
 * Time: 下午3:59
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "ScanData")
public class ScanData {
    @XStreamAsAttribute
    @XStreamAlias("ScanFlag")
    private String scanFlag = XMLConstant.LUAI_SCAN_FLAG;

    @XStreamAlias("ItemData")
    private ItemData itemData;

    @Column(name = "scanFlag")
    public String getScanFlag() {
        return scanFlag;
    }

    public void setScanFlag(String scanFlag) {
        this.scanFlag =  scanFlag;
    }

    @OneToOne(targetEntity = ItemData.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "ItemDataID", updatable = true)
    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        this.itemData = itemData;
    }

    @XStreamOmitField
    private int id;

    @Id
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "SCANDATA_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

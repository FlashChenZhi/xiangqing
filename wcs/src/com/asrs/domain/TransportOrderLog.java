package com.asrs.domain;

import javax.persistence.*;

/**
 * Created by van on 2017/6/15.
 */
@Entity
@Table(name = "TRANSPORTORDER")
public class TransportOrderLog {

    private int id;
    private String fromLocation;
    private String toLocation;

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
    @Column(name = "FROMLOCATION")
    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    @Basic
    @Column(name = "TOLOCATION")
    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }
}

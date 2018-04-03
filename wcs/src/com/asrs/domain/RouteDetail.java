package com.asrs.domain;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/11/7.
 */
@Entity
@Table(name = "ROUTEDETAIL")
public class RouteDetail {
    private int _id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, length = 8)
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    private String _currentBlockNo;

    @Column(name = "CURRENTBLOCKNO")
    @Basic
    public String getCurrentBlockNo() {
        return _currentBlockNo;
    }

    public void setCurrentBlockNo(String currentBlockNo) {
        _currentBlockNo = currentBlockNo;
    }

    private String _nextBlockNo;

    @Column(name = "NEXTBLOCKNO")
    @Basic
    public String getNextBlockNo() {
        return _nextBlockNo;
    }

    public void setNextBlockNo(String nextBlockNo) {
        _nextBlockNo = nextBlockNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RouteDetail that = (RouteDetail) o;

        if (_id != that._id) return false;
        if (_currentBlockNo != null ? !_currentBlockNo.equals(that._currentBlockNo) : that._currentBlockNo != null)
            return false;
        if (_nextBlockNo != null ? !_nextBlockNo.equals(that._nextBlockNo) : that._nextBlockNo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _id;
        result = 31 * result + (_currentBlockNo != null ? _currentBlockNo.hashCode() : 0);
        result = 31 * result + (_nextBlockNo != null ? _nextBlockNo.hashCode() : 0);
        return result;
    }

    private Route _route;

    @ManyToOne
    public
    @JoinColumn(name = "ROUTEID", referencedColumnName = "ID") Route getRoute() {
        return _route;
    }

    public void setRoute(Route route) {
        _route = route;
    }
}

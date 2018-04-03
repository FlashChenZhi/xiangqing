package com.wms.domain;

import com.util.hibernate.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.annotations.Cascade;
import org.hibernate.criterion.Restrictions;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by xiongying on 2015/8/16.
 */
@Entity
@Table(name = "MENU")
public class Menu {
    private int id;
    private String code;
    private String name;
    private Integer parentId;
    private String parentCode;
    private String dist;
    private String type;
    private int seq;
    private String status;
    private Date createDate;
    private String createUserId;
    private Date lastUpdateDate;
    private String lastUpdateUserId;
    private int version;

    private Set<Role> roles = new HashSet<Role>();

    public static final String COL_ID = "id";
    public static final String COL_CODE = "code";
    public static final String COL_NAME = "name";
    public static final String COL_PARENT_ID = "parentId";
    public static final String COL_PARENT_CODE = "parentCode";
    public static final String COL_SEQ = "seq";
    public static final String COL_TYPE = "type";
    public static final String COL_DIST = "dist";
    public static final String COL_STATUS = "status";
    public static final String COL_CREATE_DATE = "createDate";
    public static final String COL_CREATE_USER_ID = "createUserId";
    public static final String COL_LAST_UPDATE_DATE = "lastUpdateDate";
    public static final String COL_LAST_UPDATE_USER_IDE = "lastUpdateUserId";
    public static final String COL_VERSION = "version";

    public static final String COL_ROLES = "roles";

    public static final String STATUS_ENABLE = "1";
    public static final String STATUS_DISABLE = "0";

    public static final String TYPE_FIRST_MENU = "1";
    public static final String TYPE_SECOND_MENU = "2";

    //1-web 2-rf 3-车载
    public static final String TYPE_DIST_WEB = "1";
    public static final String TYPE_DIST_RF = "2";
    public static final String TYPE_DIST_CZ = "3";


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
    @Column(name = "CODE")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
        @Column(name = "PARENT_ID")
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }


    @Basic
    @Column(name = "PARENT_CODE")
    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
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
    @Column(name = "DIST")
    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }

    @Basic
    @Column(name = "SEQ")
    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
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
    @Column(name = "CREATE_USER_ID")
    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    @Basic
    @Column(name = "LAST_UPDATE_DATE")
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    @Basic
    @Column(name = "LAST_UPDATE_USER_ID")
    public String getLastUpdateUserId() {
        return lastUpdateUserId;
    }

    public void setLastUpdateUserId(String lastUpdateUserId) {
        this.lastUpdateUserId = lastUpdateUserId;
    }

    @Version
    @Column(name = "VERSION")
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }


    @ManyToMany
    @Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @JoinTable(name = "ROLE_MENU", joinColumns = {@JoinColumn(name = "MENU_ID")}, inverseJoinColumns = {@JoinColumn(name = "ROLE_ID")})
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Menu menu = (Menu) o;

        if (id != menu.id) return false;
        if (seq != menu.seq) return false;
        if (version != menu.version) return false;
        if (code != null ? !code.equals(menu.code) : menu.code != null) return false;
        if (name != null ? !name.equals(menu.name) : menu.name != null) return false;
        if (parentId != null ? !parentId.equals(menu.parentId) : menu.parentId != null) return false;
        if (type != null ? !type.equals(menu.type) : menu.type != null) return false;
        if (status != null ? !status.equals(menu.status) : menu.status != null) return false;
        if (createDate != null ? !createDate.equals(menu.createDate) : menu.createDate != null) return false;
        if (createUserId != null ? !createUserId.equals(menu.createUserId) : menu.createUserId != null) return false;
        if (lastUpdateDate != null ? !lastUpdateDate.equals(menu.lastUpdateDate) : menu.lastUpdateDate != null)
            return false;
        return !(lastUpdateUserId != null ? !lastUpdateUserId.equals(menu.lastUpdateUserId) : menu.lastUpdateUserId != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + seq;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (createUserId != null ? createUserId.hashCode() : 0);
        result = 31 * result + (lastUpdateDate != null ? lastUpdateDate.hashCode() : 0);
        result = 31 * result + (lastUpdateUserId != null ? lastUpdateUserId.hashCode() : 0);
        result = 31 * result + version;
        return result;
    }

    public static Menu getById(int id) {
        Session session = HibernateUtil.getCurrentSession();

        Menu menu = (Menu) session.get(Menu.class, id);

        return menu;
    }

    public static Menu getByCode(String code) {
        Session session = HibernateUtil.getCurrentSession();
        Criteria criteria = session.createCriteria(Menu.class);
        criteria.add(Restrictions.eq(Menu.COL_CODE, code));
        Menu menu = (Menu) criteria.uniqueResult();
        return menu;
    }

    @Transient
    public String getTypeStr() {
        if (Menu.TYPE_FIRST_MENU.equals(this.getType()))
            return "一级菜单";
        if (Menu.TYPE_SECOND_MENU.equals(this.getType()))
            return "二级菜单";
        return "";
    }

    @Transient
    public String getDistStr() {
        if (Menu.TYPE_DIST_WEB.equals(this.getDist()))
            return "WEB";
        if (Menu.TYPE_DIST_RF.equals(this.getDist()))
            return "RF";
        if (Menu.TYPE_DIST_CZ.equals(this.getDist()))
            return "车载";
        return "";
    }

    @Transient
    public String getStatusStr() {
        if (Menu.STATUS_DISABLE.equals(this.getStatus()))
            return "禁用";
        if (Menu.STATUS_ENABLE.equals(this.getStatus()))
            return "启用";

        return "";
    }
}

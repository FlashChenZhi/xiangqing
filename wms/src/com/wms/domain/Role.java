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
@Table(name = "ROLE")
public class Role {
    private int id;
    private String name;
    private String status;
    private Date createDate;
    private String createUserId;
    private Date lastUpdateDate;
    private String lastUpdateUserId;
    private int version;

    private Set<User> users = new HashSet<User>();
    private Set<Menu> menus = new HashSet<Menu>();

    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_STATUS = "status";
    public static final String COL_CREATE_DATE = "createDate";
    public static final String COL_CREATE_USER_ID = "createUserId";
    public static final String COL_LAST_UPDATE_DATE = "lastUpdateDate";
    public static final String COL_LAST_UPDATE_USER_IDE = "lastUpdateUserId";
    public static final String COL_VERSION = "version";

    public static final String COL_USERS = "users";
    public static final String COL_MENUS = "menus";

    public static final String STATUS_ENABLE = "1";
    public static final String STATUS_DISABLE = "0";


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
    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    @JoinTable(name = "USER_ROLE",joinColumns = {@JoinColumn(name = "ROLE_ID")},inverseJoinColumns = {@JoinColumn(name = "USER_ID")})
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @ManyToMany
    @Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @JoinTable(name = "ROLE_MENU",joinColumns = {@JoinColumn(name = "ROLE_ID")},inverseJoinColumns = {@JoinColumn(name = "MENU_ID")})
    public Set<Menu> getMenus() {
        return menus;
    }

    public void setMenus(Set<Menu> menus) {
        this.menus = menus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role user = (Role) o;

        if (id != user.id) return false;
        if (version != user.version) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (createDate != null ? !createDate.equals(user.createDate) : user.createDate != null) return false;
        if (createUserId != null ? !createUserId.equals(user.createUserId) : user.createUserId != null) return false;
        if (lastUpdateDate != null ? !lastUpdateDate.equals(user.lastUpdateDate) : user.lastUpdateDate != null)
            return false;
        return !(lastUpdateUserId != null ? !lastUpdateUserId.equals(user.lastUpdateUserId) : user.lastUpdateUserId != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (createUserId != null ? createUserId.hashCode() : 0);
        result = 31 * result + (lastUpdateDate != null ? lastUpdateDate.hashCode() : 0);
        result = 31 * result + (lastUpdateUserId != null ? lastUpdateUserId.hashCode() : 0);
        result = 31 * result + version;
        return result;
    }

    public static Role getById(int id) {
        Session session = HibernateUtil.getCurrentSession();

        Role role = (Role) session.get(Role.class, id);

        return role;
    }

    public static Role getByName(String name){
        Session session = HibernateUtil.getCurrentSession();
        Criteria criteria = session.createCriteria(Role.class);
        criteria.add(Restrictions.eq(Role.COL_NAME,name));
        Role role = (Role) criteria.uniqueResult();
        return role;
    }
}

package com.wms.domain;

import com.util.hibernate.HibernateUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.annotations.Cascade;
import org.hibernate.criterion.Restrictions;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by xiongying on 2015/8/16.
 */
@Entity
@Table(name = "\"USER\"")
public class User {
    private int id;
    private String code;
    private String name;
    private String password;
    private String status;
    private Date createDate;
    private String createUserId;
    private Date lastUpdateDate;
    private String lastUpdateUserId;
    private int version;
    private String theme = DEFAILT_THEME;

    private Set<Role> roles = new HashSet<Role>();

    public static final String COL_ID = "id";
    public static final String COL_CODE = "code";
    public static final String COL_NAME = "name";
    public static final String COL_PASSWORD = "password";
    public static final String COL_STATUS = "status";
    public static final String COL_CREATE_DATE = "createDate";
    public static final String COL_CREATE_USER_ID = "createUserId";
    public static final String COL_LAST_UPDATE_DATE = "lastUpdateDate";
    public static final String COL_LAST_UPDATE_USER_IDE = "lastUpdateUserId";
    public static final String COL_VERSION = "version";

    public static final String STATUS_ENABLE = "1";
    public static final String STATUS_DISABLE = "0";

    public static final String INIT_PASSWORD = "111111";

    public static final String DEFAILT_THEME = "gray";

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
    @Column(name = "PASSWORD")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    @Basic
    @Column(name = "THEME")
    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    @ManyToMany
    @Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @JoinTable(name = "USER_ROLE", joinColumns = {@JoinColumn(name = "USER_ID")}, inverseJoinColumns = {@JoinColumn(name = "ROLE_ID")})
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

        User user = (User) o;

        if (id != user.id) return false;
        if (version != user.version) return false;
        if (code != null ? !code.equals(user.code) : user.code != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (status != null ? !status.equals(user.status) : user.status != null) return false;
        if (createDate != null ? !createDate.equals(user.createDate) : user.createDate != null) return false;
        if (createUserId != null ? !createUserId.equals(user.createUserId) : user.createUserId != null) return false;
        if (lastUpdateDate != null ? !lastUpdateDate.equals(user.lastUpdateDate) : user.lastUpdateDate != null)
            return false;
        return !(lastUpdateUserId != null ? !lastUpdateUserId.equals(user.lastUpdateUserId) : user.lastUpdateUserId != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (createUserId != null ? createUserId.hashCode() : 0);
        result = 31 * result + (lastUpdateDate != null ? lastUpdateDate.hashCode() : 0);
        result = 31 * result + (lastUpdateUserId != null ? lastUpdateUserId.hashCode() : 0);
        result = 31 * result + version;
        return result;
    }

    @Transient
    public void initPassword() {
        this.setPassword(encrypt(User.INIT_PASSWORD));
    }

    @Transient
    public String encrypt(String password) {
        int salt = this.getId();
        String str = salt + password;
        String restult = DigestUtils.md5Hex(str);
        return restult;
    }

    public static User getById(int id) {
        Session session = HibernateUtil.getCurrentSession();

        User user = (User) session.get(User.class, id);

        return user;
    }

    public static User getByCode(String code) {
        Session session = HibernateUtil.getCurrentSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq(User.COL_CODE, code));

        return (User) criteria.uniqueResult();
    }

    public boolean validate(String inputPassword) {
        String str = encrypt(inputPassword);
        if (this.getPassword().equals(str))
            return true;
        return false;
    }

    public boolean hasRole(String role) {

        Set<Role> roles = getRoles();
        Iterator<Role> iterator = roles.iterator();
        while (iterator.hasNext()) {
            Role role1 = iterator.next();
            Set<Menu> menus = role1.getMenus();
            Iterator<Menu> menuIterator = menus.iterator();
            while (menuIterator.hasNext()) {
                Menu menu = menuIterator.next();
                if (menu.getCode().equals(role)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void main(String[] args) {
        String str = "1" + User.INIT_PASSWORD;
        String restult = DigestUtils.md5Hex(str);

        System.out.println(restult);
    }
}

package zhangchongantest.neu.edu.newtest.Database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

@Entity
public class ClassBean extends BaseBean{

    @Id(autoincrement = true)
    private Long id;

    private long studentId;

    @ToOne(joinProperty = "studentId")
    private StudentBean studentBean;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 576561827)
    private transient ClassBeanDao myDao;

    @Generated(hash = 652645109)
    public ClassBean(Long id, long studentId) {
        this.id = id;
        this.studentId = studentId;
    }

    @Generated(hash = 1395092832)
    public ClassBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getStudentId() {
        return this.studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    @Generated(hash = 1990662241)
    private transient Long studentBean__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 23870362)
    public StudentBean getStudentBean() {
        long __key = this.studentId;
        if (studentBean__resolvedKey == null
                || !studentBean__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StudentBeanDao targetDao = daoSession.getStudentBeanDao();
            StudentBean studentBeanNew = targetDao.load(__key);
            synchronized (this) {
                studentBean = studentBeanNew;
                studentBean__resolvedKey = __key;
            }
        }
        return studentBean;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2121097539)
    public void setStudentBean(@NotNull StudentBean studentBean) {
        if (studentBean == null) {
            throw new DaoException(
                    "To-one property 'studentId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.studentBean = studentBean;
            studentId = studentBean.getId();
            studentBean__resolvedKey = studentId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1398366533)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getClassBeanDao() : null;
    }

}

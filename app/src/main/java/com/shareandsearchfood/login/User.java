package com.shareandsearchfood.login;

import com.shareandsearchfood.shareandsearchfood.Favorite;

import com.shareandsearchfood.shareandsearchfood.HowToDoItTable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.DaoException;
import com.shareandsearchfood.shareandsearchfood.FavoriteDao;
import com.shareandsearchfood.shareandsearchfood.HowToDoItTableDao;


@Entity
public class User {

    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String username;
    @NotNull
    private String email;
    private String password;
    private String photo;
    @NotNull
    private int flag;

    @ToMany(referencedJoinProperty= "userId")
    @OrderBy("date ASC")
    private List<Recipe> receipts;
    @ToMany(referencedJoinProperty= "userId")
    @OrderBy("date ASC")
    private List<Notebook> notes;
    @ToMany(referencedJoinProperty= "userId")
    private List<Favorite> favorites;
    @ToMany(referencedJoinProperty= "userId")
    private List<HowToDoItTable> howToDoIt;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1507654846)
    private transient UserDao myDao;


    @Generated(hash = 179411637)
    public User(Long id, @NotNull String username, @NotNull String email,
            String password, String photo, int flag) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.photo = photo;
        this.flag = flag;
    }

    @Generated(hash = 586692638)
    public User() {
    }


    //falta ligar amigos a conta actual...

    public String getName() {
        return username;
    }

    public String getName(long id) {
        return username;
    }


    public void setName(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public String getPhoto(long id) {
        return photo;
    }


    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 950804180)
    public List<Recipe> getReceipts() {
        if (receipts == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RecipeDao targetDao = daoSession.getRecipeDao();
            List<Recipe> receiptsNew = targetDao._queryUser_Receipts(id);
            synchronized (this) {
                if (receipts == null) {
                    receipts = receiptsNew;
                }
            }
        }
        return receipts;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1409076908)
    public synchronized void resetReceipts() {
        receipts = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 827154304)
    public List<Notebook> getNotes() {
        if (notes == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            NotebookDao targetDao = daoSession.getNotebookDao();
            List<Notebook> notesNew = targetDao._queryUser_Notes(id);
            synchronized (this) {
                if (notes == null) {
                    notes = notesNew;
                }
            }
        }
        return notes;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 2032098259)
    public synchronized void resetNotes() {
        notes = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1756408444)
    public List<Favorite> getFavorites() {
        if (favorites == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FavoriteDao targetDao = daoSession.getFavoriteDao();
            List<Favorite> favoritesNew = targetDao._queryUser_Favorites(id);
            synchronized (this) {
                if (favorites == null) {
                    favorites = favoritesNew;
                }
            }
        }
        return favorites;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 584992353)
    public synchronized void resetFavorites() {
        favorites = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1816243740)
    public List<HowToDoItTable> getHowToDoIt() {
        if (howToDoIt == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            HowToDoItTableDao targetDao = daoSession.getHowToDoItTableDao();
            List<HowToDoItTable> howToDoItNew = targetDao._queryUser_HowToDoIt(id);
            synchronized (this) {
                if (howToDoIt == null) {
                    howToDoIt = howToDoItNew;
                }
            }
        }
        return howToDoIt;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1350598330)
    public synchronized void resetHowToDoIt() {
        howToDoIt = null;
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
    @Generated(hash = 2059241980)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDao() : null;
    }


}

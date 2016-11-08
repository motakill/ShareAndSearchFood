package com.shareandsearchfood.login;

import com.shareandsearchfood.shareandsearchfood.Favorite;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.shareandsearchfood.shareandsearchfood.FavoriteDao;

@Entity
public class Receipt extends User {

    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String title;
    @NotNull
    private String ingredients;
    @NotNull
    private String steps;
    @NotNull
    private String photoReceipt;
    private String calories;
    @NotNull
    private int status;
    private long userId;
    private java.util.Date date;
    @NotNull
    private float rate;
    @NotNull
    private boolean favorite;

    @ToMany(referencedJoinProperty = "receiptId")
    private List<Photo> photos;
    @ToMany(referencedJoinProperty = "receiptId")
    private List<Video> videos;
    @ToMany(referencedJoinProperty= "receiptId")
    private List<Favorite> favorites;



    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1424773800)
    private transient ReceiptDao myDao;


    @Generated(hash = 1850018231)
    public Receipt(Long id, @NotNull String title, @NotNull String ingredients,
            @NotNull String steps, @NotNull String photoReceipt, String calories,
            int status, long userId, java.util.Date date, float rate,
            boolean favorite) {
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
        this.steps = steps;
        this.photoReceipt = photoReceipt;
        this.calories = calories;
        this.status = status;
        this.userId = userId;
        this.date = date;
        this.rate = rate;
        this.favorite = favorite;
    }

    @Generated(hash = 723313403)
    public Receipt() {
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getPhotoReceipt() {
        return photoReceipt;
    }

    public void setPhotoReceipt(String photoReceipt) {
        this.photoReceipt = photoReceipt;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setRate(float rate){
        this.rate = rate;
    }
    public boolean getFavorite(){
        return  favorite;
    }
    public  void setFavorite(boolean favorite){
        this.favorite = favorite;
    }

    public float getRate() {
        return this.rate;
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
    @Generated(hash = 494696234)
    public List<Photo> getPhotos() {
        if (photos == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PhotoDao targetDao = daoSession.getPhotoDao();
            List<Photo> photosNew = targetDao._queryReceipt_Photos(id);
            synchronized (this) {
                if (photos == null) {
                    photos = photosNew;
                }
            }
        }
        return photos;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 781103891)
    public synchronized void resetPhotos() {
        photos = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1659939450)
    public List<Video> getVideos() {
        if (videos == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            VideoDao targetDao = daoSession.getVideoDao();
            List<Video> videosNew = targetDao._queryReceipt_Videos(id);
            synchronized (this) {
                if (videos == null) {
                    videos = videosNew;
                }
            }
        }
        return videos;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1923228979)
    public synchronized void resetVideos() {
        videos = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 878704513)
    public List<Favorite> getFavorites() {
        if (favorites == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FavoriteDao targetDao = daoSession.getFavoriteDao();
            List<Favorite> favoritesNew = targetDao._queryReceipt_Favorites(id);
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
    @Generated(hash = 858617663)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getReceiptDao() : null;
    }

}
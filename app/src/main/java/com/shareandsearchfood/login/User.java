package com.shareandsearchfood.login;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;


@Entity
public class User {

    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String username;
    @NotNull
    private String email;
    //private String password;
    private String photo;
    @NotNull
    private int flag;




    @Generated(hash = 368032225)
    public User(Long id, @NotNull String username, @NotNull String email,
            String photo, int flag) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.photo = photo;
        this.flag = flag;
    }

    @Generated(hash = 586692638)
    public User() {
    }


    

  /**  public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
*/
    //falta ligar amigos a conta actual...

    public String getName() {
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}

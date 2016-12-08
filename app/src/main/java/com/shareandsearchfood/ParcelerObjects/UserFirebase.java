package com.shareandsearchfood.ParcelerObjects;


import java.util.List;
import org.parceler.Parcel;

/**
 * Created by david_000 on 07/12/2016.
 */

@Parcel
public class UserFirebase {

    private String username;
    private String email;
    private String password;
    private String photo;


   // private List<Recipe> receipts;
   // private List<Notebook> notes;
    //private List<Favorite> favorites;
   // private List<HowToDoItTable> howToDoIt;

    public UserFirebase(String username, String email, String password, String photo) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.photo = photo;
    }

    public UserFirebase() {
    }

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

    public void setPhoto(String photo) { this.photo = photo; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public void setPassword(String password) { this.password = password;}

    public String getPassword() { return this.password; }

}

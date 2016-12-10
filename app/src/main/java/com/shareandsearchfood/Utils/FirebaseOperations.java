package com.shareandsearchfood.Utils;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shareandsearchfood.ParcelerObjects.Comments;
import com.shareandsearchfood.ParcelerObjects.HowTo;
import com.shareandsearchfood.ParcelerObjects.Notebook;
import com.shareandsearchfood.ParcelerObjects.Recipe;
import com.shareandsearchfood.ParcelerObjects.User;
import com.shareandsearchfood.shareandsearchfood.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by david_000 on 07/12/2016.
 */


public class FirebaseOperations {
    private static String key;

    //User
    public static void insertGoogleUser(GoogleSignInAccount acct) {
        String personName = acct.getDisplayName();
        String personEmail = acct.getEmail();
        Uri personPhoto = acct.getPhotoUrl();

        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_USERS);

        userRef.child(encodeKey(personEmail))
                .setValue(new User(personName, personEmail, null, personPhoto.toString()));
    }
    public static void insertUser(String personName, String personEmail,
                                  String password, String personPhoto) {
        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_USERS);

        userRef.child(encodeKey(personEmail))
                .setValue(new User(personName, personEmail, password, personPhoto));

    }
    public static void setUserContent(String email, final TextView userTextView,
                                      final ImageView photo, final Context ctx){
        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_USERS);
        userRef.child(FirebaseOperations.encodeKey(email))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if(userTextView != null)
                            userTextView.setText(user.getName());
                        if (user.getPhoto() != null && photo != null) {
                            Image.download(ctx,photo,user.getPhoto());

                        } else if(photo != null)
                            photo.setImageResource(R.drawable.com_facebook_profile_picture_blank_square);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
    public static void storeImageToFirebase(Uri mCurrentPhotoUri, final String email) {
        FirebaseStorage storage= FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://ss-food.appspot.com");
        StorageReference photoRef = storageRef.child("images/users/"+mCurrentPhotoUri.getLastPathSegment());
        UploadTask uploadTask = photoRef.putFile(mCurrentPhotoUri);
        final DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_USERS );

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                userRef.child(encodeKey(email)).child("photo").setValue(downloadUrl.toString());
            }
        });

    }

    //Notebook
    public static void insertNote(String email, String note) {
        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_USERS);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);

        userRef.child(encodeKey(email)).child(Constants.FIREBASE_CHILD_NOTES).push()
                .setValue(new Notebook(note,reportDate));
    }
    public static void removeNote() {
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_USERS);

        userRef.child(encodeKey(mFirebaseUser.getEmail())).child(Constants.FIREBASE_CHILD_NOTES)
        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot querySnapshot) {
                querySnapshot.getRef().removeValue();
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

    //Recipes
    public static void insertRecipe(String title, String ingredients,
                                    String steps, String photoReceipt, String calories,
                                    int status, String userId, float rate,
                                    boolean favorite) {

        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_RECIPES);

        DatabaseReference userRef2 = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_USERS);

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);


        DatabaseReference newRef = userRef.push();
        newRef.setValue(new Recipe(title, ingredients, steps, photoReceipt,
                calories,status, userId, reportDate, rate, favorite,"null"));

        key = newRef.getKey();
        userRef2.child(encodeKey(userId)).child(Constants.FIREBASE_CHILD_RECIPES).child(key)
                .setValue(new Recipe(title, ingredients, steps, photoReceipt,
                calories,status, userId, reportDate, rate, favorite,key));
        newRef.child("recipeId").setValue(key);

    }
    public static void storeRecipePhotoToFirebase(Uri mCurrentPhotoUri, final String email) {
        FirebaseStorage storage= FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://ss-food.appspot.com");
        StorageReference photoRef = storageRef.child("images/recipes/"+mCurrentPhotoUri.getLastPathSegment());
        UploadTask uploadTask = photoRef.putFile(mCurrentPhotoUri);
        final DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_RECIPES );
        final DatabaseReference userRef2 = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_USERS);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                userRef.child(key).child("photoRecipe").setValue(downloadUrl.toString());
                userRef2.child(encodeKey(email)).child(Constants.FIREBASE_CHILD_RECIPES).child(key)
                        .child("photoRecipe").setValue(downloadUrl.toString());

            }
        });

    }

    //HowToDo
    public static void insertHowTo (String userId,String title,
                                    String obs, String photo, String comments,
                                    String videos) {

        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_HOWTO);

        DatabaseReference userRef2 = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_USERS);

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);

        DatabaseReference newRef = userRef.push();
        newRef.setValue(new HowTo(userId,title, obs, photo, comments,
                videos,reportDate));

        key = newRef.getKey();
        userRef2.child(encodeKey(userId)).child(Constants.FIREBASE_CHILD_HOWTO).child(key)
                .setValue(new HowTo(userId,title, obs, photo, comments,
                        videos,reportDate));

        storeHowToPhotoToFirebase(Uri.parse(photo),userId);

    }
    public static void storeHowToPhotoToFirebase(Uri mCurrentPhotoUri, final String email) {
        FirebaseStorage storage= FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://ss-food.appspot.com");
        StorageReference photoRef = storageRef.child("images/howTo/"+mCurrentPhotoUri.getLastPathSegment());
        UploadTask uploadTask = photoRef.putFile(mCurrentPhotoUri);
        final DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_HOWTO);
        final DatabaseReference userRef2 = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_USERS);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                userRef.child(key).child("photo").setValue(downloadUrl.toString());
                userRef2.child(encodeKey(email)).child(Constants.FIREBASE_CHILD_HOWTO).child(key)
                        .child("photo").setValue(downloadUrl.toString());

            }
        });

    }

    //Favorites
    public static void setFavoriteStatus(final String email, final Recipe recipe){
        final DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_USERS);
        final DatabaseReference userRefAux = userRef.child(FirebaseOperations.encodeKey(email));
        userRef.child(FirebaseOperations.encodeKey(email)).child(Constants.FIREBASE_CHILD_FAVORITES)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean encontrou = false;
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Recipe recipeNew = child.getValue(Recipe.class);
                            if (recipeNew != null && recipeNew.getRecipeId().equals(recipe.getRecipeId())
                                    && recipeNew.getFavorite()) {
                                child.getRef().removeValue();
                                encontrou = true;
                            }
                        }
                        if (!encontrou) {
                            userRefAux.child(Constants.FIREBASE_CHILD_FAVORITES)
                                    .child(encodeKey(recipe.getRecipeId())).setValue(recipe);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
    public static  void isChecked(final Recipe recipe, String email, final CheckBox favorite){
        final DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_USERS);
        userRef.child(FirebaseOperations.encodeKey(email)).child(Constants.FIREBASE_CHILD_FAVORITES)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean entrou = false;
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Recipe recipeNew = child.getValue(Recipe.class);
                            if (recipeNew != null && recipeNew.getRecipeId().equals(recipe.getRecipeId())) {
                                recipe.setFavorite(true);
                                favorite.setChecked(true);
                                entrou = true;
                            }
                        }
                        if(!entrou){
                            recipe.setFavorite(false);
                            favorite.setChecked(false);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    //Comments
    public static void insertComment(String recipeID, String email, String comment, String photo) {
        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_RECIPES);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);
        userRef.child(recipeID).child(Constants.FIREBASE_CHILD_NOTES).push()
                .setValue(new Comments(comment,photo,reportDate,email));
        storeCommentPhotoToFirebase(Uri.parse(photo),recipeID);
    }
    public static void storeCommentPhotoToFirebase(Uri mCurrentPhotoUri, final String recipeID) {
        FirebaseStorage storage= FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://ss-food.appspot.com");
        StorageReference photoRef = storageRef.child("images/comments/"+mCurrentPhotoUri.getLastPathSegment());
        UploadTask uploadTask = photoRef.putFile(mCurrentPhotoUri);
        final DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_RECIPES );

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                userRef.child(recipeID).child("photo").setValue(downloadUrl.toString());

            }
        });

    }

    //Utils
    public static String encodeKey(String string){
        return string.replace('.', '%');
    }

}
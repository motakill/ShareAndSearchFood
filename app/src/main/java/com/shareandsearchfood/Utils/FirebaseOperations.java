package com.shareandsearchfood.Utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
                            Tools.ImageDownload(ctx,photo,user.getPhoto());

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
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and ImageDownload URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                userRef.child(encodeKey(email)).child("photo").setValue(downloadUrl.toString());
            }
        });

    }

    //Notebook
    public static void insertNote(String email, TextView note) {
        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_USERS);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);

        userRef.child(encodeKey(email)).child(Constants.FIREBASE_CHILD_NOTES).push()
                .setValue(new Notebook(note.getText().toString(),reportDate));
        note.setText("");
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
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and ImageDownload URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                userRef.child(key).child("photoRecipe").setValue(downloadUrl.toString());
                userRef2.child(encodeKey(email)).child(Constants.FIREBASE_CHILD_RECIPES).child(key)
                        .child("photoRecipe").setValue(downloadUrl.toString());

            }
        });

    }
    public static void totalRecipes(final String userID,final TextView total){
        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_USERS);
        userRef.child(encodeKey(userID)).child(Constants.FIREBASE_CHILD_RECIPES)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot child : dataSnapshot.getChildren())
                    i++;
                total.setText(String.valueOf(i));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
                videos,reportDate,"null"));

        key = newRef.getKey();
        userRef2.child(encodeKey(userId)).child(Constants.FIREBASE_CHILD_HOWTO).child(key)
                .setValue(new HowTo(userId,title, obs, photo, comments,
                        videos,reportDate,key));
        userRef.child("howToID").setValue(key);
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
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and ImageDownload URL.
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
    public static void insertComment(Context ctx, String recipeID, String email
            , TextView comment, Uri photo, String constant) {
        Toast.makeText(ctx,
                "Sending ...",Toast.LENGTH_SHORT).show();

        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(constant);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);
        DatabaseReference newRef = userRef.child(recipeID).child(Constants.FIREBASE_CHILD_COMENTS).push();
        newRef.setValue(new Comments(comment.getText().toString(),null,null,reportDate,email,null));
        String key = newRef.getKey();
        newRef.child("commentID").setValue(key);
        if(photo != null)
            storeCommentPhotoToFirebase(photo,recipeID,key,constant);

        comment.setText("");
    }
    public static void storeCommentPhotoToFirebase(Uri mCurrentPhotoUri, final String recipeID
            , final String commentID, final String constant) {
        FirebaseStorage storage= FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://ss-food.appspot.com");
        StorageReference photoRef = storageRef.child("images/comments/"+mCurrentPhotoUri.getLastPathSegment());
        UploadTask uploadTask = photoRef.putFile(mCurrentPhotoUri);
        final DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(constant );

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and ImageDownload URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                userRef.child(recipeID).child(Constants.FIREBASE_CHILD_COMENTS).child(commentID)
                        .child("photo").setValue(downloadUrl.toString());

            }
        });

    }
    public static String insertCommentVideo(String recipeID, String email, String comment, String constant) {
        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(constant);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);
        DatabaseReference newRef = userRef.child(recipeID).child(Constants.FIREBASE_CHILD_COMENTS).push();
        newRef.setValue(new Comments(comment,null," ",reportDate,email,null));
        String key = newRef.getKey();
        newRef.child("commentID").setValue(key);
        return  key;
    }
    public static  void storeCommnetVideoToFirebase(Uri video, final String userID, final TextView comment
            ,final  String recipeID, final Context ctx
            , final String constant){
        FirebaseStorage storage= FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://ss-food.appspot.com");
        StorageReference videoRef = storageRef.child("videos/comments/"+video.getLastPathSegment());
        UploadTask uploadTask = videoRef.putFile(video);
        final DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(constant);

        Toast.makeText(ctx,
                "Upload in progressn mensage will be send shortly.",Toast.LENGTH_SHORT).show();

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(ctx,
                        "Error Uploading Video. Try Again Please.",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                String commentID = insertCommentVideo(recipeID,userID,comment.getText().toString(),constant);
                userRef.child(recipeID).child(Constants.FIREBASE_CHILD_COMENTS).child(commentID)
                        .child("video").setValue(downloadUrl.toString());
                comment.setText("");
            }
        });
    }

    //Followers
    public static void followUser(String userID, String followerID){
        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_USERS);
        userRef.child(encodeKey(userID)).child(Constants.FIREBASE_CHILD_FOLLOWING).push()
        .setValue(followerID);

        DatabaseReference userRef2 = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_USERS);
        userRef2.child(encodeKey(followerID)).child(Constants.FIREBASE_CHILD_FOLLOWERS).push()
                .setValue(userID);
    }
    public static void unFollowUser(final String userID, final String followerID){
        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_USERS);
        userRef.child(encodeKey(userID)).child(Constants.FIREBASE_CHILD_FOLLOWING)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren())
                            if(child.getValue().equals(followerID))
                                child.getRef().removeValue();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        DatabaseReference userRef2 = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_USERS);
        userRef2.child(encodeKey(followerID)).child(Constants.FIREBASE_CHILD_FOLLOWERS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren())
                            if(child.getValue().equals(userID))
                                child.getRef().removeValue();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
    public static void isFriend(final String userID, final String followerID, final Button button){
        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_USERS);
        userRef.child(FirebaseOperations.encodeKey(userID)).child(Constants.FIREBASE_CHILD_FOLLOWING)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean friend = false;
                        for (DataSnapshot child : dataSnapshot.getChildren())
                            if(child.getValue().equals(followerID))
                                friend = true;
                        if(friend) {
                            button.setText("Unfollow");
                            button.setBackgroundResource(R.drawable.unfollow);
                        }
                        else{
                            button.setText("Follow");
                            button.setBackgroundResource(R.drawable.follow);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
    public static void totalFF(final String userID,final TextView totalFollowers,
                               final TextView totalFollowing){
        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_USERS);
        userRef.child(encodeKey(userID)).child(Constants.FIREBASE_CHILD_FOLLOWERS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                        for (DataSnapshot child : dataSnapshot.getChildren())
                            i++;
                        totalFollowers.setText(String.valueOf(i));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        DatabaseReference userRef2 = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_USERS);
        userRef2.child(encodeKey(userID)).child(Constants.FIREBASE_CHILD_FOLLOWING)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                        for (DataSnapshot child : dataSnapshot.getChildren())
                            i++;
                        totalFollowing.setText(String.valueOf(i));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    //Utils
    public static String encodeKey(String string){
        return string.replace('.', '%');
    }

}
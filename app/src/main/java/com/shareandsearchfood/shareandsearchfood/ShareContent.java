package com.shareandsearchfood.shareandsearchfood;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shareandsearchfood.Utils.FirebaseOperations;
import com.shareandsearchfood.Login.LoginActivity;
import com.shareandsearchfood.shareandsearchfood.CustomOnItemSelectedListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by david_000 on 10/11/2016.
 */

public class ShareContent extends NavBar{
    private LinearLayout mLayout, mLayout2;
    private EditText mEditText, mEditText2;
    private Button mButton, mButton2, deleteIngredients, deleteSteps;
    private AutoCompleteTextView title_receipt;
    private AutoCompleteTextView ingredients;
    private AutoCompleteTextView steps;
    private Spinner categorias, num_people, preparation_time, confection_time;
    private Button saveReceipt;
    private Button pubReceipt;
    private boolean added_ingredients = false;
    private boolean added_steps = false;
    private Uri photoReceipt;
    private static final int PICK_IMAGE = 100;
    private TextView photoName;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    //variaveis com os valores correctos para o Firebase

    private String nova_categoria , new_num_people, new_preparation_time, new_confection_time;
    private int contador_more_ingredients = 0;
    private List<TextView> myEditTextList, myEditTextList2;




    protected Button selectIngredientsButton;

    protected CharSequence[] array_ingredientes_Meat = { "Beef", "Pork", "Chicken","Rice",
                                                        "Cooked potato", "French fries", "Pasta","Bean","Grain", "Onion",
                                                        "Olive oil","Garlic", "Salt" };

    protected CharSequence[] array_ingredientes_Fish = { "Whitefish", "Codfish", "Sardines", "Tuna", "Octopus",
                                                        "Cooked potato","Bean","Grain", "Onion","Olive oil","Garlic", "Salt" };

    protected CharSequence[] array_ingredientes_Seafood = { "Clams", "Shrimp", "Lobster",
                                                            "Salt", "Limon", "Garlic" };

    protected CharSequence[] array_ingredientes_Vegan = { "Tofu", "Tomato", "Lettuce", "Cabbages", "Asparagus", "Potato", "Onion",
                                                        "Rice", "Salt", "Olive oil"};

    protected CharSequence[] array_ingredientes_Drinks = { "Alcool", "Orange", "Apple", "Soda" };

    protected CharSequence[] array_ingredientes_Cakes = { "Eggs", "Sugar", "Salt", "Milk", "Flour", "Butter","Chocolate" };

    protected CharSequence[] array_ingredientes_Snacks = { "Toasties", "Tuna pate", "Sardines pate", "Seafood pate", "Spicy sausage", "Butter" };



    protected ArrayList<CharSequence> selectedIngredients = new ArrayList<CharSequence>();



    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // Initialize Firebase Auth

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // cenas para adicionar mais ingredientes no share
        mLayout = (LinearLayout) findViewById(R.id.layoutIngredientes);
        mEditText = (EditText) findViewById(R.id.ingredients);
        mButton = (Button) findViewById(R.id.moreIngredients);
        mButton.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View v) {
                mLayout.addView(createNewTextView(mEditText.getText().toString()));
                added_ingredients = true;
                mEditText.setText("");
            }
        });


        deleteIngredients = (Button) findViewById(R.id.lessIngredients);
        deleteIngredients.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View v) {
                if(contador_more_ingredients != 0){
                    mLayout.removeViewAt(mLayout.getChildCount() - 1);
                    contador_more_ingredients--;
                }
            }
        });

        TextView textView = new TextView(this);
        textView.setText("More ingredients");

        // cenas para adicionar mais steps no share
        mLayout2 = (LinearLayout) findViewById(R.id.layoutSteps);
        mEditText2 = (EditText) findViewById(R.id.Step_by_Step);
        mButton2 = (Button) findViewById(R.id.moreSteps);
        mButton2.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View v) {
                mLayout2.addView(createNewTextView2(mEditText2.getText().toString()));
                added_steps = true;
                mEditText2.setText("");
            }
        });


        deleteSteps = (Button) findViewById(R.id.lessSteps);
        deleteSteps.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View v) {
                String s = getSteps();
                if(!s.isEmpty()){
                    mLayout2.removeViewAt(mLayout2.getChildCount() - 1);
                }
            }
        });

        TextView textView2 = new TextView(this);
        textView2.setText("More Steps");

        title_receipt = (AutoCompleteTextView) findViewById(R.id.title_receita_share);
        ingredients = (AutoCompleteTextView) findViewById(R.id.ingredients);
        steps = (AutoCompleteTextView) findViewById(R.id.Step_by_Step);

        //categorias da publicação da receita
        categorias = (Spinner) findViewById(R.id.spinner_cat);
        categorias.setOnItemSelectedListener(new CustomOnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                nova_categoria = parent.getItemAtPosition(pos).toString();
              /*  Toast.makeText(parent.getContext(),
                        "OnItemSelectedListener : " + nova_categoria,
                        Toast.LENGTH_SHORT).show();
                        */
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });

        //numero de pessoas
        num_people = (Spinner) findViewById(R.id.spinner_people);
        num_people.setOnItemSelectedListener(new CustomOnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                new_num_people = parent.getItemAtPosition(pos).toString();

                /*Toast.makeText(parent.getContext(),
                        "OnItemSelectedListener : " + new_num_people,
                        Toast.LENGTH_SHORT).show();
                   */
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });

        //tempo de preparação
        preparation_time = (Spinner) findViewById(R.id.spinner_preparation_time);
        preparation_time.setOnItemSelectedListener(new CustomOnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                new_preparation_time = parent.getItemAtPosition(pos).toString();

            /*    Toast.makeText(parent.getContext(),
                        "OnItemSelectedListener : " + new_preparation_time,
                        Toast.LENGTH_SHORT).show();
            */

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });

        //tempo de confeção
        confection_time = (Spinner) findViewById(R.id.spinner_confection_time);
        confection_time.setOnItemSelectedListener(new CustomOnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                new_confection_time = parent.getItemAtPosition(pos).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });


        //opções para selecionar os ingredientes
        selectIngredientsButton = (Button) findViewById(R.id.select_ingredients);
        selectIngredientsButton.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View v) {
                switch(v.getId()) {

                    case R.id.select_ingredients:
                        showSelectIngredientsDialog();
                        break;
                    default:
                        break;

                }
            }
        });

        //Save and Pubb receipts
        saveReceipt = (Button) findViewById(R.id.Save) ;
        pubReceipt = (Button) findViewById(R.id.Publish) ;
        saveReceipt.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View v) {
                saveReceipt(saveReceipt.getId());
            }
        });

        pubReceipt.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View v) {
                saveReceipt(pubReceipt.getId());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                mFirebaseAuth.signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtra("finish", true); // if you are checking for this in your other Activities
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            try{
            Context context = getApplicationContext();
            photoReceipt = data.getData();
            photoName = (TextView) findViewById(R.id.photoName);
            photoName.setText(photoReceipt.getPath());

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), photoReceipt);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 0, bytes);
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
            photoReceipt = Uri.parse(path);
            }catch (Exception e){}

        }

    }

    //nao apagar!!!!!!!
    /*private View.OnClickListener onClick() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mLayout.addView(createNewTextView(mEditText.getText().toString()));
                mLayout2.addView(createNewTextView2(mEditText2.getText().toString()));
                added_ingredients = true;
                added_steps = true;
                mEditText.setText("");
                mEditText2.setText("");

            }
        };
    }*/

    private TextView createNewTextView(String text) {
        final DrawerLayout.LayoutParams lparams = new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.WRAP_CONTENT, DrawerLayout.LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(this);
        textView.setLayoutParams(lparams);
        if (!text.isEmpty()) {
            contador_more_ingredients++;
            textView.setText(text + "; ");
            return textView;
        }
        else {

            textView.setText("");
            return textView;
        }

    }
    private TextView createNewTextView2(String text) {
        final DrawerLayout.LayoutParams lparams = new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.WRAP_CONTENT, DrawerLayout.LayoutParams.WRAP_CONTENT);
        final TextView textView2 = new TextView(this);
        textView2.setLayoutParams(lparams);

        if (!text.isEmpty()) {

            textView2.setText(text + "; ");
            return textView2;
        }
        else {

            textView2.setText("");
            return textView2;
        }
    }
    public void saveReceipt(int buttomId) {

        boolean cancel = false;
        View focusView = null;

        // Store values at the time of the login attempt.
        String title = title_receipt.getText().toString();
        //String name_photo = photoName.toString();


        if (title.isEmpty()) {
            title_receipt.setError(getString(R.string.how_to_do_it_tittle_empty));
            focusView = title_receipt;
            cancel = true;
        }

        else if (selectIngredientsButton.getText().toString().isEmpty()){
            ingredients.setError(getString(R.string.how_to_do_it_tittle_empty));
            focusView = ingredients;
            cancel = true;
        }
        else if (added_steps == false){
            steps.setError(getString(R.string.how_to_do_it_tittle_empty));
            focusView = steps;
            cancel = true;
        }
        // nao sei se funcaaaaaa
        /**  else if (name_photo.isEmpty()){
         Log.d("inchaporca: ", "devia de dar o setError");
         steps.setError(getString(R.string.need_photo));
         focusView = steps;
         cancel = true;
         }
         */
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            if (buttomId == saveReceipt.getId())
                FirebaseOperations.insertRecipe(title_receipt.getText().toString(), getIngredients(),
                        getSteps(), photoReceipt.toString(), null, 0, mFirebaseUser.getEmail(), 0, false);

            else
                FirebaseOperations.insertRecipe(title_receipt.getText().toString(), getIngredients(),
                        getSteps(), photoReceipt.toString(), null, 1, mFirebaseUser.getEmail(), 0, false);

            FirebaseOperations.storeRecipePhotoToFirebase(photoReceipt,mFirebaseUser.getEmail());
            startActivity(new Intent(this, MyProfile.class));
        }
    }
    public void openGalleryShare(View v) {
        Intent gallery =
                new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    private String getIngredients(){
        myEditTextList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        if(!selectIngredientsButton.getText().toString().equals("Click here"))
            sb.append(selectIngredientsButton.getText().toString());

        for( int i = 0; i < mLayout.getChildCount(); i++ )
            if (mLayout.getChildAt(i) instanceof TextView) {
                myEditTextList.add((TextView) mLayout.getChildAt(i));
            }
        for (int i = 0; i < myEditTextList.size(); i++)
            sb.append(myEditTextList.get(i).getText().toString());

        return sb.toString();
    }
        private String getSteps(){
        myEditTextList2 = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for( int i = 0; i < mLayout2.getChildCount(); i++ )
            if( mLayout2.getChildAt( i ) instanceof TextView )
                myEditTextList2.add( (TextView) mLayout2.getChildAt( i ) );

        for (int i = 0; i < myEditTextList2.size(); i++)
            sb.append(myEditTextList2.get(i).getText().toString());

        return sb.toString();
    }

    protected void showSelectIngredientsDialog() {

       if(nova_categoria.equals("Meat")) {
           boolean[] checkedIngredients = new boolean[array_ingredientes_Meat.length];
           int count = array_ingredientes_Meat.length;
           for (int i = 0; i < count; i++)
               checkedIngredients[i] = selectedIngredients.contains(array_ingredientes_Meat[i]);

           DialogInterface.OnMultiChoiceClickListener IngredientsDialogListener = new DialogInterface.OnMultiChoiceClickListener() {

               @Override
               public void onClick(DialogInterface dialog, final int which, boolean isChecked) {

                   if (isChecked) {
                       selectedIngredients.add(array_ingredientes_Meat[which]);
                       AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ShareContent.this);
                       dialogBuilder.setTitle("Ingredient Quantity (Kg/un)");

                       LayoutInflater li = LayoutInflater.from(ShareContent.this);
                       final View myView = li.inflate(R.layout.itementry, null);
                       dialogBuilder.setView(myView);

                       dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int whichButton) {
                               EditText mota = (EditText) myView.findViewById(R.id.titleText1);
                               Toast.makeText(ShareContent.this, "Added " + mota.getText().toString() + " for " + array_ingredientes_Meat[which], Toast.LENGTH_LONG).show();
                               selectedIngredients.add(":" + mota.getText().toString());
                               onChangeSelectedIngredients();
                           }
                       });
                       dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int whichButton) {
                               //pass
                           }
                       });
                       AlertDialog b = dialogBuilder.create();
                       b.show();


                   }
                   else{
                       if(selectedIngredients.size() > 0) {
                           selectedIngredients.remove(selectedIngredients.size() - 1);
                           selectedIngredients.remove(selectedIngredients.size() - 1);
                       }
                       onChangeSelectedIngredients();
                   }
               }
           };

           AlertDialog.Builder builder = new AlertDialog.Builder(this);
           builder.setTitle("Select Ingredients");
           builder.setMultiChoiceItems(array_ingredientes_Meat, checkedIngredients, IngredientsDialogListener);
           AlertDialog dialog = builder.create();
           dialog.show();
       }

       else if(nova_categoria.equals("Fish")) {
            boolean[] checkedIngredients = new boolean[array_ingredientes_Fish.length];
            int count = array_ingredientes_Fish.length;
            for (int i = 0; i < count; i++)
                checkedIngredients[i] = selectedIngredients.contains(array_ingredientes_Fish[i]);

            DialogInterface.OnMultiChoiceClickListener IngredientsDialogListener = new DialogInterface.OnMultiChoiceClickListener() {

                @Override
                public void onClick(DialogInterface dialog, final int which, boolean isChecked) {


                    if (isChecked) {
                        selectedIngredients.add(array_ingredientes_Fish[which]);
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ShareContent.this);

                        //dialogBuilder.setView(R.layout.itementry);
                        dialogBuilder.setTitle("Ingredient Quantity (Kg/un)");

                        LayoutInflater li = LayoutInflater.from(ShareContent.this);
                        final View myView = li.inflate(R.layout.itementry, null);
                        dialogBuilder.setView(myView);

                        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                EditText mota = (EditText) myView.findViewById(R.id.titleText1);
                                Toast.makeText(ShareContent.this, "Added " + mota.getText().toString() + " for " + array_ingredientes_Fish[which], Toast.LENGTH_LONG).show();
                                selectedIngredients.add(":" + mota.getText().toString());
                                onChangeSelectedIngredients();
                            }
                        });
                        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //pass
                            }
                        });
                        AlertDialog b = dialogBuilder.create();
                        b.show();


                    }
                    else{
                        if(selectedIngredients.size() > 0) {
                            selectedIngredients.remove(selectedIngredients.size() - 1);
                            selectedIngredients.remove(selectedIngredients.size() - 1);
                        }
                        onChangeSelectedIngredients();
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Ingredients");
            builder.setMultiChoiceItems(array_ingredientes_Fish, checkedIngredients, IngredientsDialogListener);
            AlertDialog dialog = builder.create();
            dialog.show();
       }

       else if(nova_categoria.equals("Seafood")) {
           boolean[] checkedIngredients = new boolean[array_ingredientes_Seafood.length];
           int count = array_ingredientes_Seafood.length;
           for (int i = 0; i < count; i++)
               checkedIngredients[i] = selectedIngredients.contains(array_ingredientes_Seafood[i]);

           DialogInterface.OnMultiChoiceClickListener IngredientsDialogListener = new DialogInterface.OnMultiChoiceClickListener() {

               @Override
               public void onClick(DialogInterface dialog, final int which, boolean isChecked) {

                   if (isChecked) {
                       selectedIngredients.add(array_ingredientes_Seafood[which]);
                       AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ShareContent.this);

                       //dialogBuilder.setView(R.layout.itementry);
                       dialogBuilder.setTitle("Ingredient Quantity (Kg/un)");

                       LayoutInflater li = LayoutInflater.from(ShareContent.this);
                       final View myView = li.inflate(R.layout.itementry, null);
                       dialogBuilder.setView(myView);

                       dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int whichButton) {
                               EditText mota = (EditText) myView.findViewById(R.id.titleText1);
                               Toast.makeText(ShareContent.this, "Added " + mota.getText().toString() + " for " + array_ingredientes_Seafood[which], Toast.LENGTH_LONG).show();
                               selectedIngredients.add(":" + mota.getText().toString());
                               onChangeSelectedIngredients();
                           }
                       });
                       dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int whichButton) {
                               //pass
                           }
                       });
                       AlertDialog b = dialogBuilder.create();
                       b.show();


                   }
                   else{
                       if(selectedIngredients.size() > 0) {
                           selectedIngredients.remove(selectedIngredients.size() - 1);
                           selectedIngredients.remove(selectedIngredients.size() - 1);
                       }
                       onChangeSelectedIngredients();
                   }
               }
           };

           AlertDialog.Builder builder = new AlertDialog.Builder(this);
           builder.setTitle("Select Ingredients");
           builder.setMultiChoiceItems(array_ingredientes_Seafood, checkedIngredients, IngredientsDialogListener);
           AlertDialog dialog = builder.create();
           dialog.show();
       }

       else if(nova_categoria.equals("Vegan")) {
           final boolean[] checkedIngredients = new boolean[array_ingredientes_Vegan.length];
           int count = array_ingredientes_Vegan.length;
           for (int i = 0; i < count; i++)
               checkedIngredients[i] = selectedIngredients.contains(array_ingredientes_Vegan[i]);

           DialogInterface.OnMultiChoiceClickListener IngredientsDialogListener = new DialogInterface.OnMultiChoiceClickListener() {

               @Override
               public void onClick(DialogInterface dialog, final int which, boolean isChecked) {

                   if (isChecked) {
                       selectedIngredients.add(array_ingredientes_Vegan[which]);
                       AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ShareContent.this);

                       dialogBuilder.setTitle("Ingredient Quantity (Kg/un)");

                       LayoutInflater li = LayoutInflater.from(ShareContent.this);
                       final View myView = li.inflate(R.layout.itementry, null);
                       dialogBuilder.setView(myView);

                       dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int whichButton) {
                               EditText mota = (EditText) myView.findViewById(R.id.titleText1);
                               Toast.makeText(ShareContent.this, "Added " + mota.getText().toString() + " for " + array_ingredientes_Vegan[which], Toast.LENGTH_LONG).show();
                               selectedIngredients.add(":" + mota.getText().toString());
                               onChangeSelectedIngredients();
                           }
                       });
                       dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int whichButton) {
                           }
                       });
                       AlertDialog b = dialogBuilder.create();
                       b.show();


                   }
                   else{
                       if(selectedIngredients.size() > 0) {
                           selectedIngredients.remove(selectedIngredients.size() - 1);
                           selectedIngredients.remove(selectedIngredients.size() - 1);
                       }
                       onChangeSelectedIngredients();
                   }
               }
           };

           AlertDialog.Builder builder = new AlertDialog.Builder(this);
           builder.setTitle("Select Ingredients");
           builder.setMultiChoiceItems(array_ingredientes_Vegan, checkedIngredients, IngredientsDialogListener);
           AlertDialog dialog = builder.create();
           dialog.show();
       }

       else if(nova_categoria.equals("Drinks")) {
           boolean[] checkedIngredients = new boolean[array_ingredientes_Drinks.length];
           int count = array_ingredientes_Drinks.length;
           for (int i = 0; i < count; i++)
               checkedIngredients[i] = selectedIngredients.contains(array_ingredientes_Drinks[i]);

           DialogInterface.OnMultiChoiceClickListener IngredientsDialogListener = new DialogInterface.OnMultiChoiceClickListener() {

               @Override
               public void onClick(DialogInterface dialog, final int which, boolean isChecked) {

                   if (isChecked) {
                       selectedIngredients.add(array_ingredientes_Drinks[which]);
                       AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ShareContent.this);

                       dialogBuilder.setTitle("Ingredient Quantity (Kg/un)");

                       LayoutInflater li = LayoutInflater.from(ShareContent.this);
                       final View myView = li.inflate(R.layout.itementry, null);
                       dialogBuilder.setView(myView);

                       dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int whichButton) {
                               EditText mota = (EditText) myView.findViewById(R.id.titleText1);
                               Toast.makeText(ShareContent.this, "Added " + mota.getText().toString() + " for " + array_ingredientes_Drinks[which], Toast.LENGTH_LONG).show();
                               selectedIngredients.add(":" + mota.getText().toString());
                               onChangeSelectedIngredients();
                           }
                       });
                       dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int whichButton) {
                               //pass
                           }
                       });
                       AlertDialog b = dialogBuilder.create();
                       b.show();


                   }
                   else{
                       if(selectedIngredients.size() > 0) {
                           selectedIngredients.remove(selectedIngredients.size() - 1);
                           selectedIngredients.remove(selectedIngredients.size() - 1);
                       }
                       onChangeSelectedIngredients();
                   }
               }
           };

           AlertDialog.Builder builder = new AlertDialog.Builder(this);
           builder.setTitle("Select Ingredients");
           builder.setMultiChoiceItems(array_ingredientes_Drinks, checkedIngredients, IngredientsDialogListener);
           AlertDialog dialog = builder.create();
           dialog.show();
       }

       else if(nova_categoria.equals("Cakes")) {
           boolean[] checkedIngredients = new boolean[array_ingredientes_Cakes.length];
           int count = array_ingredientes_Cakes.length;
           for (int i = 0; i < count; i++)
               checkedIngredients[i] = selectedIngredients.contains(array_ingredientes_Cakes[i]);

           DialogInterface.OnMultiChoiceClickListener IngredientsDialogListener = new DialogInterface.OnMultiChoiceClickListener() {

               @Override
               public void onClick(DialogInterface dialog, final int which, boolean isChecked) {

                   if (isChecked) {
                       selectedIngredients.add(array_ingredientes_Cakes[which]);
                       AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ShareContent.this);

                       dialogBuilder.setTitle("Ingredient Quantity (Kg/un)");

                       LayoutInflater li = LayoutInflater.from(ShareContent.this);
                       final View myView = li.inflate(R.layout.itementry, null);
                       dialogBuilder.setView(myView);

                       dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int whichButton) {
                               EditText mota = (EditText) myView.findViewById(R.id.titleText1);
                               Toast.makeText(ShareContent.this, "Added " + mota.getText().toString() + " for " + array_ingredientes_Cakes[which], Toast.LENGTH_LONG).show();
                               selectedIngredients.add(":" + mota.getText().toString());
                               onChangeSelectedIngredients();
                           }
                       });
                       dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int whichButton) {
                               //pass
                           }
                       });
                       AlertDialog b = dialogBuilder.create();
                       b.show();


                   }
                   else{
                       if(selectedIngredients.size() > 0) {
                           selectedIngredients.remove(selectedIngredients.size() - 1);
                           selectedIngredients.remove(selectedIngredients.size() - 1);
                       }
                       onChangeSelectedIngredients();
                   }
               }
           };

           AlertDialog.Builder builder = new AlertDialog.Builder(this);
           builder.setTitle("Select Ingredients");
           builder.setMultiChoiceItems(array_ingredientes_Cakes, checkedIngredients, IngredientsDialogListener);
           AlertDialog dialog = builder.create();
           dialog.show();
       }

       else if(nova_categoria.equals("Snacks")) {
           boolean[] checkedIngredients = new boolean[array_ingredientes_Snacks.length];
           int count = array_ingredientes_Snacks.length;
           for (int i = 0; i < count; i++)
               checkedIngredients[i] = selectedIngredients.contains(array_ingredientes_Snacks[i]);

           DialogInterface.OnMultiChoiceClickListener IngredientsDialogListener = new DialogInterface.OnMultiChoiceClickListener() {

               @Override
               public void onClick(DialogInterface dialog, final int which, boolean isChecked) {

                   if (isChecked) {
                       selectedIngredients.add(array_ingredientes_Snacks[which]);
                       AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ShareContent.this);

                       dialogBuilder.setTitle("Ingredient Quantity (Kg/un)");

                       LayoutInflater li = LayoutInflater.from(ShareContent.this);
                       final View myView = li.inflate(R.layout.itementry, null);
                       dialogBuilder.setView(myView);

                       dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int whichButton) {
                               EditText mota = (EditText) myView.findViewById(R.id.titleText1);
                               Toast.makeText(ShareContent.this, "Added " + mota.getText().toString() + " for " + array_ingredientes_Snacks[which], Toast.LENGTH_LONG).show();
                               selectedIngredients.add(":" + mota.getText().toString());
                               onChangeSelectedIngredients();
                           }
                       });
                       dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int whichButton) {
                               //pass
                           }
                       });
                       AlertDialog b = dialogBuilder.create();
                       b.show();


                   }
                   else{
                       if(selectedIngredients.size() > 0) {
                           selectedIngredients.remove(selectedIngredients.size() - 1);
                           selectedIngredients.remove(selectedIngredients.size() - 1);
                       }
                       onChangeSelectedIngredients();
                   }
               }
           };

           AlertDialog.Builder builder = new AlertDialog.Builder(this);
           builder.setTitle("Select Ingredients");
           builder.setMultiChoiceItems(array_ingredientes_Snacks, checkedIngredients, IngredientsDialogListener);
           AlertDialog dialog = builder.create();
           dialog.show();
       }
        else
           Toast.makeText(ShareContent.this,"Category not recognized", Toast.LENGTH_LONG).show();

    }

    protected void onChangeSelectedIngredients() {

        StringBuilder stringBuilder = new StringBuilder();
        for(CharSequence ingredients : selectedIngredients)
            stringBuilder.append(ingredients + ";");
        selectIngredientsButton.setText(stringBuilder.toString());

    }
}

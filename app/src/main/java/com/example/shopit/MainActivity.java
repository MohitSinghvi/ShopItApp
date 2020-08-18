package com.example.shopit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
//import LayoutParams;
//import android.widget.LinearLayout;


public class MainActivity extends AppCompatActivity implements ToolBarFragment.OnFragmentInteractionListener {
    public static final String EXTRA_MESSAGE="com.example.shopit.Message";
//    private GestureDetectorCompat gestureDetectorCompat = null;
    private FusedLocationProviderClient client;
    private static final String TAG = "MainActivity";
    String user_id;
    DatabaseHelper myDB;
    String username;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView ;

    Float scale = 1f;
    Matrix matrix = new Matrix();
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    ImageView display_image;
//    =new DatabaseHelper(this);
//


    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
//            drawer.closeDrawer(GravityCompaty.St);
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        initialize();
        diplayServerProducts();
//        displayproducts(username,query,search);


    }






    public void initialize(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        drawer=findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_add_product:
                        Intent intent = new Intent(getApplicationContext(),AddProduct.class);
                        startActivity(intent);
                        break;

                    default:
                        break;
                }
                return true;
            }
        });
    }




    public void sendMessage(View view){
        Intent intent = new Intent(this,DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.search_text);
        String message=editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE,message );
        startActivity(intent);
    }
    public void goToLoginPage(View view){
        Intent intent = new Intent(this,LoginPage.class);
        startActivity(intent);
    }
    public void goToSignupPage(View view){
        Intent intent = new Intent(this,SignupPage.class);
        startActivity(intent);
    }
    public void showMenu(View view){
        showMenu();
    }
    public void showMenu(){
        LinearLayout mymenu = findViewById(R.id.my_menu);
        mymenu.setVisibility(View.VISIBLE);
    }

    public void hideMenu(){
        LinearLayout mymenu = findViewById(R.id.my_menu);
        mymenu.setVisibility(View.INVISIBLE);
    }
    public void hideMenu(View view){
        hideMenu();
    }
    public void addButton(View view){
        LinearLayout mymenu = (LinearLayout) findViewById(R.id.my_menu);
        Button button= new Button(this);

        String username=Session.getusername(this);
        button.setText("HI "+username);
        mymenu.addView(button);

    }
    public void logoutUser(View view){
        Session.endsession(this);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);

    }

    public void gotoaddProductpage(View view){
        Intent intent =new Intent(this,AddProduct.class)    ;
        startActivity(intent);
    }
    public void gotoshowOrders(View view){
        Intent intent =new Intent(this,showOrders.class)    ;
        startActivity(intent);
    }
    public void search_product(View view){
        TextView searchbox=findViewById(R.id.search_text);
        String search = searchbox.getText().toString()+"'";
        String query="select * from products where prod_name ='"+search;
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("search",query);
        startActivity(intent);
    }
    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }
    public void diplayServerProducts(){
        final FireStoreHelper fh = new FireStoreHelper();
        CollectionReference prod_ref=fh.getProdRef();
        prod_ref.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Context context = getApplicationContext();
                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("MainActivity", document.getId() + " => " + document.getData());
//                            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
//                            }
//                            all_products = task.getResult();


                            for(QueryDocumentSnapshot product:task.getResult()){
                                String prod_id = product.getId();
                                Map<String,Object> product_map = product.getData();



                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                ProductCardFragment prod_card = new ProductCardFragment();

                                final Bundle bundle = new Bundle();
                                bundle.putString("prod_name",(String)product_map.get("prod_name"));
                                bundle.putString("prod_id",prod_id);
                                bundle.putString("prod_brand",(String)product_map.get("prod_brand"));
                                bundle.putString("prod_price",""+product_map.get("prod_price"));
                                StorageReference image=fh.getProductImgRef().child(prod_id+"/"+product_map.get("prod_name")+"0");
//
//
//                                image.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                                    @Override
//                                    public void onSuccess(byte[] bytes) {
//
//                                        byte[] prod_img=bytes;
//                                        bundle.putByteArray("prod_image",prod_img);
////                                        prod_image.setImageBitmap(BitmapFactory.decodeByteArray(prod_img, 0, prod_img.length));
//                                    }
//                                });





                                prod_card.setArguments(bundle);
                                fragmentTransaction.add(R.id.prod_layout,prod_card);
                                fragmentTransaction.commit();




                            }


//                            Toast.makeText(context, (String)all_products[0].getDocuments().get(0).get("prod_name"), Toast.LENGTH_SHORT).show();

                        } else {
                            Log.d("MainActivity", "Error getting documents: ", task.getException());
                            Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


//        QuerySnapshot allProducts=fh.getProducts(this);
//        if(allProducts==null) return;


    }
    void displayproducts(String username,String query,String search){


        myDB = new DatabaseHelper(this);
        LinearLayout prod_layout = (LinearLayout) findViewById(R.id.prod_layout);
//        prod_layout.setBackgroundColor(Color.GRAY);

        final Cursor res= myDB.ShowProducts(query,search);
        while(res.moveToNext()){




//            LinearLayout prod_layout = (LinearLayout) findViewById(R.id.prod_layout);

            LinearLayout single_product =new LinearLayout(this);
            single_product.setBackgroundColor(Color.WHITE);
            LinearLayout prod_content=new LinearLayout(this);
            LinearLayout prod_contents=new LinearLayout(this);
            ImageView prod_image=new ImageView(this);



//        LayoutParams params1;

            single_product.setOrientation(LinearLayout.VERTICAL);
            prod_content.setOrientation(LinearLayout.HORIZONTAL);
            prod_contents.setOrientation(LinearLayout.VERTICAL);

//        single_product.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));



            final TextView prod_name=new TextView(this);

            TextView prod_brand=new TextView(this);
            TextView prod_description=new TextView((this));
            TextView prod_price = new TextView(this);




            final int prod_id=Integer.parseInt(res.getString(res.getColumnIndex("prod_id")));

            final int prod_price_val=Integer.parseInt(res.getString(res.getColumnIndex("prod_price")));


            prod_price.setText("₹ "+Integer.toString(prod_price_val));
            final String prod_name_val=res.getString(res.getColumnIndex("prod_name"));

            prod_name.setText(prod_name_val);
            prod_name.setTextColor(Color.BLACK);
            prod_brand.setText(res.getString(res.getColumnIndex("prod_brand")));
            prod_description.setText(res.getString(res.getColumnIndex("prod_description")));
            byte[] prod_img=res.getBlob(res.getColumnIndex("prod_image"));

            prod_image.setImageBitmap(BitmapFactory.decodeByteArray(prod_img, 0, prod_img.length));
//


            single_product.addView(prod_name);

            single_product.addView(prod_content);
            prod_content.addView(prod_image);

            prod_content.addView(prod_contents);
            prod_contents.addView(prod_brand);
            prod_contents.addView(prod_description);
            prod_contents.addView(prod_price);


            prod_layout.addView(single_product);

            Button button= new Button(this);
            button.setText(" BUY ");


            button.setBackgroundColor(Color.BLACK);
            button.setTextColor(Color.WHITE);

            prod_contents.addView(button);



            ViewGroup.LayoutParams mylayoutParams = prod_contents.getLayoutParams();
            mylayoutParams.width = 500;
            prod_contents.setLayoutParams(mylayoutParams);


            setMargins(prod_image,20,20,20,20);
            setMargins(prod_name,20,20,20,20);
            setMargins(single_product,25,20,25,20);
            setMargins(button,20,20,20,20);
//            setMargins(prod_brand,20,20,20,20);
            setMargins(prod_price,20,20,20,20);
//            button.set
//            button.setPadding(100,100,100,100);
//            prod_image.getLayoutParams().width=300;
//
//            button.setWidth(20);

            android.view.ViewGroup.LayoutParams layoutParams = prod_image.getLayoutParams();
            layoutParams.width = 500;
            layoutParams.height = 500;
            prod_image.setLayoutParams(layoutParams);
            prod_name.setGravity(Gravity.CENTER);
//            single_product.setGravity(Gravity.HORIZONTAL_GRAVITY_MASK);


//            user_id=Session.getuserId(this);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click



//                    String username=Session.getusername(MainActivity.this);
                    if (Session.getusername(MainActivity.this)!=null) {
                        Intent orderintent = new Intent(getBaseContext(), OrderProduct.class);
                        orderintent.putExtra("prod_id", prod_id);
                        orderintent.putExtra("prod_name", prod_name_val);
//                    orderintent.putExtra("user_name",username);
                        orderintent.putExtra("prod_price", prod_price_val);
//                    orderintent.putExtra("user_id",user_id);
                        // currentContext.startActivity(activityChangeIntent);

                        startActivity(orderintent);
                    }
                    else{
                        Intent intent = new Intent(MainActivity.this,LoginPage.class);
                        startActivity(intent);

                    }
                }
            });
        }


    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    void oldStuff(){
        myDB = new DatabaseHelper(this);
//        Button login_button = findViewById(R.id.main_page_login_button);
//        Button signup_button = findViewById(R.id.main_page_signup_button);
        Button logout_button = findViewById(R.id.main_page_logout_button);
        Button addproduct_button = findViewById(R.id.main_page_add_prod_button);
        Button showorders_button = findViewById(R.id.main_page_show_orders);
//        Button greeting = findViewById(R.id.main_page_greeting);
        String query="select * from products";
        String search="";






        if (Session.getusername(this)==null){
//            login_button.setVisibility(View.VISIBLE);
//            signup_button.setVisibility(View.VISIBLE);
            logout_button.setVisibility(View.GONE);


        }
        else{
//            login_button.setVisibility(View.GONE);
//            signup_button.setVisibility(View.GONE);
            logout_button.setVisibility(View.VISIBLE);
//            greeting.setVisibility(View.VISIBLE);
            showorders_button.setVisibility(View.VISIBLE);

//            addproduct_button.setVisibility(View.VISIBLE);

//            greeting.setText("HI "+ myDB.getName(Session.getusername(this))+" !");
            if (Session.getusername(this).equals("admin")){
//                addProducts
                addproduct_button.setVisibility(View.VISIBLE);
                showorders_button.setVisibility(View.VISIBLE);


            }
        }

        Intent intent = getIntent();
        Bundle extras=intent.getExtras();
        if(extras!=null){

            query=intent.getExtras().getString("search");
//            search=intent.getExtras().getString("val");
        }
    }

}







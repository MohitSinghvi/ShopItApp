package com.example.shopit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AddProduct extends AppCompatActivity {

    ImageView prod_img;
    Bitmap imageBitmap;
    DatabaseHelper mydb;
    LinearLayout images_layout;
    List<Bitmap> bitmaps;

    int PICK_IMAGE_MULTIPLE = 1;
//    String imageEncoded;
    List<String> imagesEncodedList;



//    public static final int CAMERA_REQUEST=1;
//    private static int RESULT_LOAD_IMAGE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
//        prod_img=(ImageView)findViewById(R.id.uploaded_prod_img);
        mydb=new DatabaseHelper(this);
        images_layout=findViewById(R.id.add_prod_img_layout);
        bitmaps = new ArrayList<>();

    }
    public void OpenCamera(View view){
//        Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent,CAMERA_REQUEST);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
        }

    }
    public void goBack(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
    public void goBack(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
//            Bitmap bitmap=(Bitmap)data.getExtras().get("data");
//            prod_img.setImageBitmap(bitmap);
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            ImageView prod_image=new ImageView(getApplicationContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(images_layout.getHeight(), images_layout.getHeight());
            lp.weight=1.0f;
            prod_image.setLayoutParams(lp);

            prod_image.setImageBitmap(imageBitmap);
//            ImageView imageView = (ImageView) findViewById(R.id.imgView);
            images_layout.addView(prod_image);
            bitmaps.add(imageBitmap);

//            prod_img.setImageBitmap(imageBitmap);
        }
        else if(requestCode==2 && resultCode==RESULT_OK){


            ClipData clipData = data.getClipData();
            if(clipData!=null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri uri = clipData.getItemAt(i).getUri();
                    try {
                        InputStream is = getContentResolver().openInputStream(uri);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);

                        ImageView prod_image=new ImageView(getApplicationContext());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(images_layout.getHeight(), images_layout.getHeight());
                        lp.weight=1.0f;
                        prod_image.setLayoutParams(lp);

                        imageBitmap=bitmap;
                        prod_image.setImageBitmap(imageBitmap);
//            ImageView imageView = (ImageView) findViewById(R.id.imgView);
                        images_layout.addView(prod_image);

                        bitmaps.add(bitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                Toast.makeText(this, "Count: "+clipData.getItemCount(), Toast.LENGTH_SHORT).show();
            }else{
                Uri uri = data.getData();
                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

//                    LinearLayout images_layout=findViewById(R.id.add_prod_img_layout);

                    ImageView prod_image=new ImageView(getApplicationContext());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(images_layout.getHeight(), images_layout.getHeight());
                    lp.weight=1.0f;
                    prod_image.setLayoutParams(lp);
//                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(200, 200);
//                    prod_image.setLayoutParams(lp);

                    imageBitmap=bitmap;
                    prod_image.setImageBitmap(imageBitmap);
//            ImageView imageView = (ImageView) findViewById(R.id.imgView);
                    images_layout.addView(prod_image);

                    bitmaps.add(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, "Single image selected", Toast.LENGTH_SHORT).show();

            }

        }




    }

    public void AddProduct(View view) throws Exception{
        Toast.makeText(this, "pressed", Toast.LENGTH_SHORT).show();
        TextView prod_name = findViewById(R.id.username);
        TextView prod_brand = findViewById(R.id.prod_brand);
        TextView prod_category = findViewById(R.id.prod_category);
        TextView prod_description = findViewById(R.id.prod_description);
        TextView prod_price = findViewById(R.id.prod_price);


//        imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
//        byte[]image=stream.toByteArray();



        FireStoreHelper fh = new FireStoreHelper();
        fh.addProduct(this,prod_name.getText().toString(),prod_brand.getText().toString(),prod_category.getText().toString(),prod_description.getText().toString(),bitmaps,Integer.parseInt(prod_price.getText().toString()));


//        if(mydb.insertProduct(prod_name.getText().toString(),prod_brand.getText().toString(),prod_category.getText().toString(),prod_description.getText().toString(),image,Integer.parseInt(prod_price.getText().toString()))){
//            showMessage("Success","Product Added Successfully");
//            Thread.sleep(1000);
//
//            goBack();
//
//        }
//        else{
//            showMessage("failed","cannot add");
//        }
    }
    public void showMessage(String title, String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void chooseImage(View view){
//        Intent i = new Intent(
//                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        startActivityForResult(i, RESULT_LOAD_IMAGE);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 2);
//        startActivityForResult(Intent.createChooser(intent,"Select Picture"), RESULT_LOAD_IMAGE);

    }







}

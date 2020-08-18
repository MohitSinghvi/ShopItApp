package com.example.shopit;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireStoreHelper {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference prod_ref=db.collection("products");


    private FirebaseStorage storage= FirebaseStorage.getInstance();
    private StorageReference storageRef=storage.getReference();
    StorageReference productRef=storageRef.child("prod_images");
    QuerySnapshot all_products;
    int count=0;

    public CollectionReference getProdRef(){
        return prod_ref;
    }

    public StorageReference getProductImgRef() {
        return productRef;
    }

    public void addProduct(final Context context, final String prod_name, String prod_brand, String prod_category, String prod_description, final List<Bitmap> bitmaps, int product_price){
        Map<String, Object> product = new HashMap<>();
        product.put("prod_name",prod_name);
        product.put("prod_brand",prod_brand);
        product.put("prod_category",prod_category);
        product.put("prod_description",prod_description);
        product.put("prod_price",product_price);

//        product.put("prod_image",prod_image);
//        String id  = db.collection("products").document().getId();


            db.collection("products").add(product)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {



                            Toast.makeText(context, "YO!", Toast.LENGTH_SHORT).show();
                            for(int i=0;i<bitmaps.size();i++){
                                Toast.makeText(context, ""+count, Toast.LENGTH_SHORT).show();
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                Bitmap bitmap=bitmaps.get(i);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
                                final byte[]prod_image=stream.toByteArray();



                                String id =documentReference.getId();
                                StorageReference imgRef=productRef.child(id+"/"+prod_name+count);
                                count++;
                                UploadTask uploadTask =imgRef.putBytes(prod_image);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle unsuccessful uploads
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                        // ...
                                    }
                                });

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    })

            ;

        }

    }

//    public void



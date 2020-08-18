package com.example.shopit;

//import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ProductImageAdapter extends RecyclerView.Adapter<ProductImageAdapter.MyViewHolder>{

    ArrayList<StorageReference> image_paths;
    Context context;
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView textView;
        public MyViewHolder(@NonNull View v) {
            super(v);
            imageView=v.findViewById(R.id.show_prod_layout_img);
            textView=v.findViewById(R.id.layout_text_view);
        }
    }
    public ProductImageAdapter(Context context, ArrayList<StorageReference> image_paths){
        this.image_paths=image_paths;
        this.context=context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        ImageView imageView =(ImageView) ;

        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.show_prod_image_layout,parent,false));

    }



    @Override
    public void onBindViewHolder(@NonNull final ProductImageAdapter.MyViewHolder holder, int position) {
//        Toast.makeText(context, "Oye Oye Oye!!!!", Toast.LENGTH_SHORT).show();
        image_paths.get(position).getBytes(1024*1024)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
//                        Toast.makeText(context, "Passed!!!!", Toast.LENGTH_SHORT).show();
                        holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                        holder.imageView.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {

                                view.setScaleX(2);
                                view.setScaleY(2);
                                view.setMinimumHeight(600);
                                return true;
                            }
                        });
//                        holder.imageView.

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed!!!!", Toast.LENGTH_SHORT).show();
                    }
                });




//        holder.textView.setText(""+position);
    }

    ProductImageAdapter(){

    }
    @Override
    public int getItemCount() {

        return image_paths.size();
    }
}

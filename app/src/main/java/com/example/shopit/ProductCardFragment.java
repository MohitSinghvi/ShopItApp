package com.example.shopit;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductCardFragment extends Fragment {

    final FireStoreHelper fh = new FireStoreHelper();
    CollectionReference prod_ref=fh.getProdRef();
    public ProductCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_product_card, container, false);


        final String prod_id = getArguments().getString("prod_id");
        TextView prod_name=view.findViewById(R.id.card_prod_name);
        TextView prod_brand=view.findViewById(R.id.card_prod_brand);
        TextView prod_price=view.findViewById(R.id.card_prod_price);
        RatingBar card_rating_bar=view.findViewById(R.id.card_rating_bar);
        TextView card_rating_val=view.findViewById(R.id.card_rating_val);

        final ImageView prod_image = view.findViewById(R.id.card_prod_img);

        prod_name.setText(getArguments().getString("prod_name"));
        prod_brand.setText(getArguments().getString("prod_brand"));
        card_rating_bar.setRating((float) getArguments().getDouble("rating"));
        card_rating_val.setText("("+getArguments().getDouble("rating")+")");
        String prod_price_val=String.format("%,.0f",Double.parseDouble(getArguments().getString("prod_price")));
        prod_price.setText("₹"+prod_price_val);



        StorageReference image=fh.getProductImgRef().child(prod_id+"/"+getArguments().getString("prod_name")+"0");


//Getting images using glide
        GlideApp.with(getContext())
                .load(image)
                .into( prod_image);

//Old way of getting images.
//        image.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//
//                byte[] prod_img=bytes;
//                prod_image.setImageBitmap(BitmapFactory.decodeByteArray(prod_img, 0, prod_img.length));
//
//
////                Toast.makeText(getContext(), "YOYOYO", Toast.LENGTH_SHORT).show();
////                                        prod_image.setImageBitmap(BitmapFactory.decodeByteArray(prod_img, 0, prod_img.length));
//            }
//        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getContext(),ViewProduct.class);
////                Bundle b = new Bundle();
////                b.putString();
//
//
//                intent.putExtra("prod_id",prod_id);
//                startActivity(intent);
                MainActivity.state="other";
                Bundle bundle = new Bundle();
                bundle.putString("prod_id",prod_id);

                FragmentTransaction fragmentTransaction=getParentFragmentManager().beginTransaction();
                ShowChainFragment scf = new ShowChainFragment();

                scf.setArguments(bundle);

                fragmentTransaction.replace(R.id.prod_layout,scf);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });

//        byte[] prod_img = getArguments().getByteArray("prod_image");

        return view;
    }

}

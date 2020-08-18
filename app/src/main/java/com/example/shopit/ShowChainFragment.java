package com.example.shopit;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowChainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowChainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    final FireStoreHelper fh = new FireStoreHelper();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView prod_name_text_view, prod_brand_text_view,prod_desc_tv,prod_category_tv;


    ArrayList<StorageReference> image_paths = new ArrayList<>();
    Button previous_button,forward_button;
    ImageView display_image;
    ViewPager2 image_viewPager;
    ProductImageAdapter productImageAdapter;
    Map<String,Object> product_info;
    TabLayout show_image_tab_layout;


    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private ImageView mImageView;


    String prod_id;
    int image_position=0;

    public ShowChainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowChainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowChainFragment newInstance(String param1, String param2) {
        ShowChainFragment fragment = new ShowChainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        prod_id=getArguments().getString("prod_id");
//        Toast.makeText(getContext(), ""+prod_id, Toast.LENGTH_SHORT).show();
        setImagesPath(prod_id);





//        setImage(0);
//        Toast.makeText(getContext(), getArguments().getString("prod_id"), Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_show_chain, container, false);
        prod_name_text_view=view.findViewById(R.id.show_fragment_prod_name);
        prod_brand_text_view=view.findViewById(R.id.show_fragment_prod_brand);
        prod_desc_tv=view.findViewById(R.id.show_prod_description);
        prod_category_tv=view.findViewById(R.id.show_prod_category);


        CollectionReference prod_ref = fh.getProdRef();
        prod_ref.document(prod_id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            product_info = task.getResult().getData();
                            prod_brand_text_view.setText("by "+(String)product_info.get("prod_brand"));
                            prod_name_text_view.setText((String)product_info.get("prod_name"));
                            prod_desc_tv.setText((String)product_info.get("prod_description"));
                            prod_category_tv.setText((String)product_info.get("prod_category"));

                        }
                    }
                });



        return view;


    }

    @Override
    public void onResume() {
        super.onResume();

    }
    public void loadViewPager(){
        image_viewPager = getView().findViewById(R.id.image_view_pager);
        productImageAdapter = new ProductImageAdapter(getContext(),image_paths);
        image_viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        image_viewPager.setAdapter(productImageAdapter);

        show_image_tab_layout = getView().findViewById(R.id.show_image_tab_layout);
        new TabLayoutMediator(show_image_tab_layout,image_viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//                        tab.setText("" + (position + 1));
                    }
                }
                ).attach();

//        display_image = getActivity().findViewById(R.id.show_prod_layout_img);
//        display_image.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                view.setScaleX(2);
//                view.setScaleY(2);
//                return true;
//            }
//        });


//        Toast.makeText(getContext(), ""+image_paths.size(), Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//
//    }

//    public void showPreviousImage(){
//
//        if(image_position>0){
//            image_position--;
//            setImage(image_position);
//        }
//    }
//    public void showNextImage(){
//        if(image_position<image_paths.size()-1){
//            image_position++;
//            setImage(image_position);
//        }
//    }
    public void setImageSize(){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(display_image.getWidth(),display_image.getWidth());
        display_image.setLayoutParams(layoutParams);

    }

    public void setImagesPath(String prod_id){
        final StorageReference image_list=fh.getProductImgRef().child(prod_id);

        image_list.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for(StorageReference s : listResult.getItems()){
                            image_paths.add(s);
//                            Toast.makeText(getContext(), s.getName(), Toast.LENGTH_SHORT).show();
                        }
//                        Toast.makeText(getContext(), ""+listResult.getItems().size(), Toast.LENGTH_SHORT).show();
                        loadViewPager();
//                        Toast.makeText(getContext(), "NOW: "+image_paths.size(), Toast.LENGTH_SHORT).show();
//                        image_paths.addAll(listResult.getItems());

//                        setImage(0);

//                        setImageSize();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                })

                ;

//        display_image = getView().findViewById(R.id.show_prod_layout_img);
//        display_image.onTouchEvent()
//        mScaleGestureDetector = new ScaleGestureDetector(this,new MainActivity.ScaleListener());


    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return mScaleGestureDetector.onTouchEvent(event);
//    }


//    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
//
//        // when a scale gesture is detected, use it to resize the image
//        @Override
//        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
//            mScaleFactor *= scaleGestureDetector.getScaleFactor();
//            display_image.setScaleX(mScaleFactor);
//            display_image.setScaleY(mScaleFactor);
//            return true;
//        }
//    }





//    public void setImage(int i){
//        display_image  = getActivity().findViewById(R.id.product_display_image_view);
//        image_paths.get(i).getBytes(1024*1024)
//                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
//                    @Override
//                    public void onSuccess(byte[] bytes) {
//                        display_image.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
//                    }
//                });
////        setImageSize();
//
//
//    }




}
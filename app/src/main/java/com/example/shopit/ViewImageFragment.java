package com.example.shopit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.request.target.Target;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.storage.StorageReference;
import com.ortiz.touchview.TouchImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewImageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    final FireStoreHelper fh = new FireStoreHelper();
    CollectionReference prod_ref=fh.getProdRef();

    // TODO: Rename and change types of parameters
    private String prod_id;
    private String prod_name;
    int pos;

    public ViewImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewImageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewImageFragment newInstance(String param1, String param2) {
        ViewImageFragment fragment = new ViewImageFragment();
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
            prod_id = getArguments().getString("prod_id");
            prod_name = getArguments().getString("prod_name");
            pos=getArguments().getInt("image_pos");;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_view_image, container, false);


        StorageReference image=fh.getProductImgRef().child(prod_id+"/"+prod_name+""+pos);
        TouchImageView touchImageView = view.findViewById(R.id.touch_image);
//        GlideApp.with(getContext()).load(image).override(Target.SIZE_ORIGINAL).into(touchImageView);
        GlideApp.with(getContext()).load(image).override(Target.SIZE_ORIGINAL).into(touchImageView);
//        Toast.makeText(getActivity().getBaseContext(), prod_id+", "+prod_name+", "+pos , Toast.LENGTH_SHORT).show();
//        touchImageView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
//        touchImageView.requestLayout();
        ViewGroup.LayoutParams params = touchImageView.getLayoutParams();

        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        touchImageView.requestLayout();
        final ImageButton close = view.findViewById(R.id.close_image_btn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeImage();
            }
        });


        return view;
    }

    public void closeImage(){
        getActivity().onBackPressed();
    }
}
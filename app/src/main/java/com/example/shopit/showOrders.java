package com.example.shopit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class showOrders extends AppCompatActivity {
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db=new DatabaseHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_orders);


        Cursor order_res;

        if (Session.getusername(this).equals("admin")) {

            String query="select * from orders";
            order_res = db.showOrders(query);
        }
        else{
            String query = "select * from orders where username='"+Session.getusername(this)+"'";
            order_res = db.showOrders(query);
        }

        LinearLayout main_layout=findViewById(R.id.mainLayout);
        LinearLayout username_layout = (LinearLayout) findViewById(R.id.username);
        LinearLayout productname_layout = (LinearLayout) findViewById(R.id.productname);
        LinearLayout priceLayout=findViewById(R.id.priceLayout);
//        LinearLayout location_layout = (LinearLayout) findViewById(R.id.location);
        LinearLayout map_layout = (LinearLayout) findViewById(R.id.map);

//        LinearLayout single_product =new LinearLayout(this);

//        LinearLayout metadata =new LinearLayout(this);
//        metadata.setOrientation(LinearLayout.HORIZONTAL);
//        prod_layout.addView(metadata);
//
//        TextView username_meta=new TextView(this);
//        username_meta.setText("USERNAME       ");
//        metadata.addView(username_meta);
//
//        TextView location_meta=new TextView(this);
//        location_meta.setText("  LOCATION     ");
//        metadata.addView(location_meta);
//
//        TextView prod_name_meta=new TextView(this);
//        prod_name_meta.setText("  PRODUCT NAME");
//        metadata.addView(prod_name_meta);

        while(order_res.moveToNext()) {


//            LinearLayout single_product =new LinearLayout(this);
//            single_product.setOrientation(LinearLayout.HORIZONTAL);
//            prod_layout.addView(single_product);


            TextView username_textview=new TextView(this);
            username_textview.setText(order_res.getString(order_res.getColumnIndex("username")));
            username_layout.addView(username_textview);
            username_textview.setHeight(200);


            TextView location_textview=new TextView(this);
            location_textview.setText(order_res.getString(order_res.getColumnIndex("location")));
//            location_textview.setWidth(400);
//            location_layout.addView(location_textview);
            location_textview.setHeight(200);

            Cursor prod_res=db.getProduct(order_res.getInt(order_res.getColumnIndex("prod_id")));




            if( prod_res != null && prod_res.moveToFirst() ){

                TextView prod_name_textview=new TextView(this);
                prod_name_textview.setText(prod_res.getString(prod_res.getColumnIndex("prod_name")));
                prod_name_textview.setHeight(200);
                productname_layout.addView(prod_name_textview);


                TextView price_textview=new TextView(this);
                price_textview.setText(prod_res.getString(prod_res.getColumnIndex("prod_price")));
                price_textview.setHeight(200);
                priceLayout.addView(price_textview);


                prod_res.close();
            }

            Button button= new Button(this);
            button.setText("show");
            button.setBackgroundColor(Color.BLACK);
            button.setTextColor(Color.WHITE);
//            button.setPadding(-20,-20,-20,-20);

            button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            button.setMinHeight(0);
            map_layout.addView(button);

//            LinearLayout space =new LinearLayout(this);
//            single_product.setOrientation(LinearLayout.HORIZONTAL);
//            prod_layout.addView(space);
//            LinearLayout space2 =new LinearLayout(this);
//            single_product.setOrientation(LinearLayout.HORIZONTAL);
//            prod_layout.addView(space2);

//            setMargins(productname_layout,20,20,20,20);
            setMargins(button,10,20,10,20);

            setMargins(productname_layout,20,20,20,20);
            setMargins(username_layout,20,20,20,20);
            setMargins(priceLayout,20,20,20,20);
//            setMargins(locatio,20,20,20,20);
            setMargins(map_layout,20,20,20,20);



            String[] loc=location_textview.getText().toString().split(",");
            final String latitude=loc[0];
            final String longitude=loc[1];


            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getBaseContext(),MapsActivity.class);
                    intent.putExtra("longitude",longitude);
                    intent.putExtra("latitude",latitude);
                    startActivity(intent);

                }
            });







        }








    }

    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }
}

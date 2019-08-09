package com.example.picupload;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends Activity implements View.OnClickListener {
    //Button mUploadDimension;
    LinearLayout linearLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        final Context context = this;
        Intent intent = getIntent();
        Log.i("result", "onCreate: result main" + intent.getStringExtra("sku_id"));
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.second_page_layout);
        linearLayout = findViewById(R.id.home_linear_layout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        //{"breadth": "10", "sku_id": "1234", "sku_desc": "10 Kg Atta", "height": "15", "length": "22", "file_path": "/media/1565318526653.jpg"}
        String sku_id = intent.getStringExtra("sku_id");
        Log.i("resulterre", ""+sku_id);
        String sku_desc = intent.getStringExtra("sku_desc");
        String output_height = intent.getStringExtra("height");
        String output_length = intent.getStringExtra("length");
        String output_breadth = intent.getStringExtra("breadth");

        //
        linearLayout.removeAllViewsInLayout();
        TextView skuDetails = new TextView(this);
        skuDetails.setText("SKU Details");
        linearLayout.addView(skuDetails);

        TextView skuid = new TextView(this);
        skuid.setText("SKU ID : " + sku_id);
        linearLayout.addView(skuid);

        TextView desc = new TextView(this);
        desc.setText("SKU Description : " + sku_desc);
        linearLayout.addView(desc);


        TextView length = new TextView(this);
        length.setText("Length : " + output_length);
        linearLayout.addView(length);


        TextView height = new TextView(this);
        height.setText("Height : " + output_height);
        linearLayout.addView(height);

        TextView breadth = new TextView(this);
        breadth.setText("Breadth : " + output_breadth);
        linearLayout.addView(breadth);

        Button mUploadDimension = new Button(this);
        mUploadDimension.setText("Upload and Exit");
        mUploadDimension.setId(R.id.upload);
        linearLayout.addView(mUploadDimension);




        //
       // setContentView(R.layout.second_page_layout);
        //mUploadDimension = (Button) findViewById(R.id.upload);
        mUploadDimension.setOnClickListener(this);
        //Intent intent = new Intent(context, MainActivity.class);
        Log.i("result", "onCreate: result end");


    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int id = v.getId();
        switch (id) {
            case R.id.upload:
                Toast.makeText(ResultActivity.this, "Dimensions updated Successfully", Toast.LENGTH_LONG).show();
                break;
        }
    }

}

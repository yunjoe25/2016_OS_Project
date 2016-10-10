package com.kjdy.osfilemanagement;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;



public class MainActivity extends Activity {

    Button button_openList;
    ListView externalList_view;
    ListView internalList_view;

    final long perMB = 1048576;
    final double perGB = 1073741824;
    String datavalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_openList = (Button) findViewById(R.id.openDialog);
        button_openList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(getApplicationContext(), FileListViewActivity.class);
                startActivity(j);
            }
        });

        externalList_view = (ListView) findViewById(R.id.external_list_view);

        //External string array[incorrect value]
        String[] external_data = {"Total Size: Not yet calculable" , "Free Space: " +
                "Not yet calculable", "Available Space: Not yet calculable" };

        ArrayAdapter<String> external_directoryList = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, external_data);

        externalList_view.setAdapter(external_directoryList);


        //Internal size view [incorrect value]
        internalList_view = (ListView) findViewById(R.id.internal_list_view);

        String[] internal_data = {"Total Size: " + fix_size(internal_totalSize) + datavalue, "Free Space: "
                + fix_size(internal_freeSize)+ datavalue, "Available Space: " + fix_size(internal_availableSize) + datavalue};

        ArrayAdapter<String> internal_directoryList = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, internal_data);

        internalList_view.setAdapter(internal_directoryList);

    }
    //formatting
    public long fix_size(long size){
        if(size > perGB){
             size /= perGB;
            datavalue = " GB";
        } else if(size > perMB){
            size /= perMB;
            datavalue = " MB";
        }
        return size;
    }
    //back to main screen
    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }

    //External storage size [!!!THIS IS DEVICE STORAGE!!!]
    StatFs statFsE = new StatFs(Environment.getExternalStorageDirectory().getPath());
    long external_totalSize = statFsE.getTotalBytes();
    long external_availableSize = statFsE.getAvailableBytes();
    long external_freeSize = statFsE.getFreeBytes();

    //Internal storage size
    StatFs statFsI = new StatFs(Environment.getExternalStorageDirectory().getPath());
    long internal_totalSize = statFsI.getTotalBytes();
    long internal_availableSize = statFsI.getAvailableBytes();
    long internal_freeSize = statFsI.getFreeBytes();

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflator().inflate(R.menu.menu_main, menu);
//        return true;
//    }
}

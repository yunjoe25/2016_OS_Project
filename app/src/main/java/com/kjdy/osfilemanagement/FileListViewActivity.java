package com.kjdy.osfilemanagement;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FileListViewActivity extends Activity {

    ListView listView;
    Button buttonUp;
    Button buttonCreate;
    TextView textFolder;

    File root;
    public static File currFolder;
    Long size_selected;
    String data_unit;

    private List<String> fileList_with_directory = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        listView = (ListView)findViewById(R.id.list);
        textFolder = (TextView) findViewById(R.id.folderLocation);
        buttonUp = (Button) findViewById(R.id.rootButton);
        buttonCreate = (Button) findViewById(R.id.createButton);

        //clicking root button
        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListDir(currFolder.getParentFile());
            }
        });

        root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        currFolder = root;

        ListDir(currFolder);

        //clicking a list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File selected = new File(fileList_with_directory.get(position));
                size_selected = getFileSize(selected);
                formatSize();

                if (selected.isDirectory()) {
                    ListDir(selected);
                    Toast.makeText(FileListViewActivity.this, selected.toString() +
                            "\n Size: " + size_selected + data_unit, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(FileListViewActivity.this, selected.toString() +
                            "\n Size: " + size_selected + data_unit, Toast.LENGTH_LONG).show();

                }
            }
        });

//Deleting the file
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {
                File f = new File(fileList_with_directory.get(pos));
                String textExtension = f.toString();
                System.out.println(textExtension);

                //directories cannot be removed
                if (!f.isDirectory()) {
                    //if (textExtension.endsWith(".txt") || textExtension.endsWith(".java") || textExtension.endsWith(".jpg")) {

                    new AlertDialog.Builder(FileListViewActivity.this)
                            .setTitle("Remove")
                            .setMessage("Are you sure you want to remove this file?")
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    dialog.cancel();
                                }
                            })
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // delete from ArrayList first

                                    // get file name then delete it
                                    File targetFile = new File(fileList_with_directory.get(pos));
                                    //File file = new File(currFolder.getAbsolutePath());
                                    if (!targetFile.isDirectory()) {
                                        Toast.makeText(getApplicationContext(), currFolder.toString() + " has been removed.", Toast.LENGTH_LONG).show();
                                        targetFile.delete();
                                        // after each item delete, must refresh load so can delete again

                                    }
                                }

                            }).setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    //moveTaskToBack(true);
                    // }
                } else {
                    Toast.makeText(getApplicationContext(), currFolder.toString() + " is directory.", Toast.LENGTH_LONG).show();
                }
                return true;


            }
        });



        // clicking create text button
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreateFileActivity.class);
                startActivity(i);
            }
        });

    }
    //return to main page
        @Override
    public void onBackPressed(){
        //Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(getApplicationContext(),MainActivity.class);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
    //formatting data size
    public void formatSize(){
        if (size_selected > 1048576) {
            size_selected /= 1048576;
            data_unit = "MB";
        } else if (size_selected > 1024) {
            size_selected /= 1024;
            data_unit = "KB";
        } else {
            data_unit = "Byte";
        }
    }

    //listing directories and files
    public void ListDir(File f){
        if(f.exists()) {
            if (f.equals(root)) {
                buttonUp.setEnabled(false);
            } else {
                buttonUp.setEnabled(true);
            }
            currFolder = f;
            textFolder.setText(f.getPath());
            File[] files = f.listFiles();
            fileList_with_directory.clear();
            if (files != null) {
            String trim_filename;
            for (File file : files) {
                trim_filename = file.getPath();
                fileList_with_directory.add(file.getPath());

            }
            }

            ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,android.R.id.text1, fileList_with_directory);
            listView.setAdapter(directoryList);

        } else{
            Toast.makeText(getApplicationContext(), "External location does not exist", Toast.LENGTH_LONG).show();
            //back to mainActivity
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
    }
    // retrieving file size **
    public static long getFileSize(final File file)
    {
        if(file==null||!file.exists())
            return 0;
        if(!file.isDirectory())
            return file.length();
        final List<File> dirs=new LinkedList<File>();
        dirs.add(file);
        long result=0;
        while(!dirs.isEmpty())
        {
            final File dir=dirs.remove(0);
            if(!dir.exists())
                continue;
            final File[] listFiles=dir.listFiles();
            if(listFiles==null||listFiles.length==0)
                continue;
            for(final File child : listFiles)
            {
                result+=child.length();
                if(child.isDirectory())
                    dirs.add(child);
            }
        }
        return result;

    }
}

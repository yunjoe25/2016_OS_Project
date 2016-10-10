package com.kjdy.osfilemanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CreateFileActivity extends Activity {
    EditText filename;
    Button createButton;
    Spinner fileSpinner;
    String fileExtension;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_layout);
        //linking xml and java
        filename = (EditText)findViewById(R.id.fileNameCreate);
        createButton = (Button)findViewById(R.id.confirmCreateButton);
        fileSpinner = (Spinner)findViewById(R.id.fileTypeSpinner);

        List<String> fileCategories = new ArrayList<String>();

        fileCategories.add("");
        fileCategories.add("Text file");
        fileCategories.add("Java file");
        fileCategories.add("HTML file");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,fileCategories);
        fileSpinner.setAdapter(dataAdapter);


        fileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

                if (item == "Text file") {
                    fileExtension = ".txt";
                } else if (item == "Java file") {
                    fileExtension = ".java";
                } else if (item == "HTML file") {
                    fileExtension = ".html";
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(parent.getContext(), "Please select file extension", Toast.LENGTH_LONG).show();

            }
        });

        //getting currFolder from FileListViewActivity
        final File currFolderCreate = FileListViewActivity.currFolder;



        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userFileName = filename.getText().toString();
                File f = new File(currFolderCreate.getAbsolutePath());

                //trying to create files
                try {
                    File userFile = new File(f, userFileName + fileExtension);
                    String nameCheck = userFileName.toString() + fileExtension;


                    //userFileName.toString() is empty string
                    if (!userFile.exists() && (!userFileName.isEmpty())) {
                        //creating new file
                        userFile.createNewFile();
                        //getting current time
                        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                        System.out.println(currentDateTimeString);

                        FileOutputStream file = new FileOutputStream(userFile);
                        //printer
                        PrintWriter pw = new PrintWriter(file);
                        pw.println("Created on "+ currentDateTimeString+ " using OS File Management");
                        pw.flush();
                        pw.close();
                        file.close();

                        //Toast file creation text
                        Toast.makeText(CreateFileActivity.this, userFileName + fileExtension + " Created", Toast.LENGTH_LONG).show();

                        //Back to list
                        Intent i = new Intent(getApplicationContext(), FileListViewActivity.class);
                        startActivity(i);

                    } else if(userFile.exists()){
                        Toast.makeText(CreateFileActivity.this, userFileName + fileExtension + "already exists", Toast.LENGTH_LONG).show();

                    } else if(nameCheck.equals(fileExtension)){
                        Toast.makeText(CreateFileActivity.this, "Please enter a filename",Toast.LENGTH_LONG).show();

                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    System.out.println(e);
                    Toast.makeText(CreateFileActivity.this, userFileName + " File Not Found", Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    Toast.makeText(CreateFileActivity.this, userFileName + e, Toast.LENGTH_LONG).show();
                    System.out.println(e);
                    e.printStackTrace();
                }


            }
        });


    }

}

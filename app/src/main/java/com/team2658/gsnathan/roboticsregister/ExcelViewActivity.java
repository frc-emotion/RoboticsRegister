package com.team2658.gsnathan.roboticsregister;

import android.os.Environment;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.snackbar.Snackbar;
import com.levitnudi.legacytableview.LegacyTableView;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

import static com.levitnudi.legacytableview.LegacyTableView.MESH;


public class ExcelViewActivity extends AppCompatActivity {

    private BottomAppBar bar;

    //contains the titles for the csv file
    private final String[] HEADERS = {"Name", "Grade", "Number", "Email"};

    private String[] data = {}; //array that holds all the data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excelview);

        bar = (BottomAppBar) findViewById(R.id.bar);
        setSupportActionBar(bar);

        //try to load the data of file to array
        try {
            loadData();
        } catch (IOException e) {
            Utils.showToast(e.getMessage(), Toast.LENGTH_SHORT, getApplicationContext());
        }

        createTable(HEADERS, data);
    }

    public void doSomething(View v)
    {
        Snackbar.make(v, "You pressed this button! :)", Snackbar.LENGTH_LONG).show();
    }

    private void createTable(String[] headers, String[] data) {
        //set table title labels
        LegacyTableView.insertLegacyTitle(headers);

        //set table contents as string arrays
        LegacyTableView.insertLegacyContent(data);

        LegacyTableView legacyTableView = (LegacyTableView) findViewById(R.id.legacy_table_view);

        legacyTableView.setTitle(LegacyTableView.readLegacyTitle());
        legacyTableView.setContent(LegacyTableView.readLegacyContent());

        //Add your preferred theme like this:
        legacyTableView.setTheme(MESH);

        //if you want a smaller table, change the padding setting
        legacyTableView.setTablePadding(3);

        //to enable users to zoom in and out:
        legacyTableView.setZoomEnabled(true);
        legacyTableView.setShowZoomControls(false);

        //remember to build your table as the last step
        legacyTableView.build();
    }

    private void loadData() throws IOException {
        //reader object
        CSVReader reader = new CSVReader(new FileReader(fileDirectory()));

        String[] nextLine;
        int counter = 0;
        while ((nextLine = reader.readNext()) != null) {

            //nextLine[] is an array of values from the line
            //merge arrays
            if (counter != 0)
                data = Utils.mergeArrays(data, nextLine);

            counter++;
        }
    }

    //get the file directory
    private String fileDirectory() {
        String baseDir = android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();  //Downloads folder directory
        String fileName = "clubrush_data.csv";
        String filePath = baseDir + File.separator + fileName;
        return filePath;
    }
}

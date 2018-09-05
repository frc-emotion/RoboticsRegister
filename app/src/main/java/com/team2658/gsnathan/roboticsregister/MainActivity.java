package com.team2658.gsnathan.roboticsregister;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.opencsv.CSVWriter;
import com.vistrav.ask.Ask;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MainActivity extends AppCompatActivity {

    private BottomAppBar bar;
    private MaterialBetterSpinner grade_spinner;
    private TextInputEditText name_field;
    private TextInputEditText number_field;
    private TextInputEditText email_field;

    private String baseDir = android.os.Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).toString();
    private DateFormat df = new SimpleDateFormat("MM/dd/yyyy 'at' h:mm z");
    private String date = df.format(Calendar.getInstance().getTime());

    private final String CSV_FILE = "clubrush_data.csv";
    private final String EXCEL_FILE = "clubrush_data_excel.xls";

    //contains the titles for the csv file
    private final String[] HEADERS = {"Name", "Grade", "Number", "Email"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Ask.on(this)
                .id(3)
                .forPermissions(Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .go();
        initViews();
    }

    private void initViews() {
        bar = (BottomAppBar) findViewById(R.id.bar);
        setSupportActionBar(bar);

        String[] grades = getResources().getStringArray(R.array.grade_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, grades);
        grade_spinner = (MaterialBetterSpinner) findViewById(R.id.grade_spinner);
        grade_spinner.setAdapter(adapter);

        name_field = (TextInputEditText) findViewById(R.id.name_input);
        number_field = (TextInputEditText) findViewById(R.id.number_input);
        email_field = (TextInputEditText) findViewById(R.id.email_input);
    }

    public void saveData(View v) {

        String grade = grade_spinner.getText().toString();
        String name = name_field.getText().toString();
        String number = number_field.getText().toString();
        String email = email_field.getText().toString();

        String[] data = {name, grade, number, email};

        String message = "Data has been saved for " + name;
        String message2 = "IOException";

        //write the data
        try {
            writeCSV(data);
            clearData();
            exportExcel();
            sendInvite(email, name);
            Snackbar.make(v, message, Snackbar.LENGTH_LONG).show();
        } catch (IOException e) {
            Snackbar.make(v, message2, Snackbar.LENGTH_LONG).show();
        }


    }

    private void writeCSV(String[] data) throws IOException {
        String filePath = baseDir + File.separator + CSV_FILE;
        File f = new File(filePath);

        //csv writer object
        CSVWriter writer;

        // if file exists
        if (f.exists() && !f.isDirectory()) {
            FileWriter mFileWriter = new FileWriter(filePath, true);
            writer = new CSVWriter(mFileWriter);
        }
        // else create a file with the headers
        else {
            writer = new CSVWriter(new FileWriter(filePath));
            writer.writeNext(HEADERS);  //Write line by line (Array)
        }

        writer.writeNext(data);
        writer.close();
    }

    //method to empty the file, incase if messup. This method is the on click for the button.
    public void clearFile(View v) {
        try {
            clearCsv();
            Utils.showToast("Clear successful", Toast.LENGTH_LONG, getApplicationContext());
        } catch (IOException e) {
            Utils.showToast("Clear failed", Toast.LENGTH_LONG, getApplicationContext());
        }
    }

    //this is the implementation of method above^
    private void clearCsv() throws IOException {
        String filePath = baseDir + File.separator + CSV_FILE;
        File f = new File(filePath);

        //csv writer object
        CSVWriter writer;

        writer = new CSVWriter(new FileWriter(filePath));
        writer.writeNext(HEADERS);  //Write line by line (Array)
        writer.close();
    }

    private void clearData() {
        grade_spinner.getText().clear();
        name_field.getText().clear();
        number_field.getText().clear();
        email_field.getText().clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //display the menu when clicked
        getMenuInflater().inflate(R.menu.bottom_app_bar, menu);
        return true;
    }

    //this method is the onclick of the export method below
    public void exportExcel() {
        try {
            String filePath = baseDir + File.separator + CSV_FILE;
            saveExcel(filePath);
        } catch (IOException e) {
            Utils.showToast("Export failed", Toast.LENGTH_LONG, getApplicationContext());
        }
    }

    //this method exports the .csv to .xls. It will overwrite any existing xls files.
    private void saveExcel(String csvFilePath) throws IOException {
        String excelFilePath = baseDir + File.separator + EXCEL_FILE;

        //converter object
        Converter convert = new Converter(csvFilePath, excelFilePath);
        convert.convertCsvToExcel();
    }

    public void sendInvite(String email, String name) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.
                Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        MyTaskParams params = new MyTaskParams(email, name);

        try {
            new MyAsyncClass().execute(params);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Email failed", Toast.LENGTH_SHORT).show();
        }
    }

    private static class MyTaskParams {
        String email;
        String name;

        MyTaskParams(String email, String name) {
            this.email = email;
            this.name = name;
        }
    }

    class MyAsyncClass extends AsyncTask<MyTaskParams, Void, Void> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.show();
        }

        @Override
        protected Void doInBackground(MyTaskParams... params) {
            try {
                GMailSender sender = new GMailSender("team2658temp@gmail.com", "SoftwareRox1");
                sender.sendMail("Thanks for signing up for Team 2658 " + params[0].name + "!",
                        "Thanks for signing up! An email will be sent out with the meeting date. Please do not reply to this email, reply to frcteam2658@gmail.com.",
                        "team2658temp@gmail.com",
                        params[0].email);
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();
            Toast.makeText(getApplicationContext(), "Email sent", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                clearData();
                return true;
            case R.id.action_share: //shares excel file with scout leader
                try {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();    //strictmode policies
                    StrictMode.setVmPolicy(builder.build());
                    File attachment = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS), EXCEL_FILE);
                    Intent emailIntent = Utils.emailIntent("gokulswamilive@gmail.com", "Excel Data", date, attachment);
                    startActivity(emailIntent);
                } catch (Exception e) {
                    Utils.showToast("Error, could not send email", Toast.LENGTH_SHORT, getApplicationContext());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

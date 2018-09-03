package com.team2658.gsnathan.roboticsregister;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

public class MainActivity extends AppCompatActivity {

    private BottomAppBar bar;
    private MaterialBetterSpinner grade_spinner;
    private TextInputEditText name_field;
    private TextInputEditText number_field;
    private TextInputEditText email_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        bar = (BottomAppBar)findViewById(R.id.bar);
        bar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.clear_data:
                        clearData();
                        return true;
                    default:
                        return true;
                }
            }
        });

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

        String message = "Data has been saved for " + name;

        Snackbar.make(v, message, Snackbar.LENGTH_LONG).show();
    }

    private void clearData() {
        grade_spinner.clearListSelection();
        name_field.clearComposingText();
        number_field.clearComposingText();
        email_field.clearComposingText();
    }
}

package com.example.finalexam2.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.finalexam2.R;
import com.example.finalexam2.models.CurrencyConverter;
import com.example.finalexam2.lib.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private CurrencyConverter cc = new CurrencyConverter();
    private boolean clearAmountAfterCalculation;

    private CheckBox clearChecker;
    private EditText fromCurrencyType;
    private EditText toCurrencyType;
    private EditText fromCurrencyAmount;
    private EditText conversionPercent;
    double convertedAmount;
    double amount;
    double percent;

    // Name of Preference file on device
    private final String keyPrefsName = "PREFS";

    private String keyAutoClear;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        // call the super-class's method to save fields, etc.
        super.onSaveInstanceState(outState);

        // save each checkbox's status so that we can check off those boxes after restore
        outState.putBoolean(keyAutoClear, clearAmountAfterCalculation);
    }

    @Override
    protected void onStop() {
        saveToSharedPref();
        super.onStop();
    }

    private void saveToSharedPref() {
        // Create a SP reference to the prefs file on the device whose name matches keyPrefsName
        // If the file on the device does not yet exist, then it will be created
        SharedPreferences preferences = getSharedPreferences(keyPrefsName, MODE_PRIVATE);

        // Create an Editor object to write changes to the preferences object above
        SharedPreferences.Editor editor = preferences.edit();

        // clear whatever was set last time
        editor.clear();

        // save the settings (clear after calculate)
        saveSettingsToSharedPrefs(editor);

        // apply the changes to the XML file in the device's storage
        editor.apply();
    }

    private void saveSettingsToSharedPrefs(SharedPreferences.Editor editor) {
        // save "autoSave" preference
        editor.putBoolean(keyAutoClear, clearAmountAfterCalculation);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clearAmountAfterCalculation = false;

        setFieldReferencesToResFileValues();
        setupToolbar();
        setupFab();
        setupListeners();
        restoreAppSettingsFromPrefs();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFields(view);
            }
        });
    }

    private void setFields(View view) {
        if(fromCurrencyAmount.getText().toString().isEmpty() ||
                conversionPercent.getText().toString().isEmpty() ||
                fromCurrencyType.getText().toString().isEmpty() ||
                toCurrencyType.getText().toString().isEmpty()
        ) {
            Snackbar.make(view, "MUST FILL IN ALL FIELDS!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        else {
            amount = Double.parseDouble(fromCurrencyAmount.getText().toString());
            percent = Double.parseDouble(conversionPercent.getText().toString());
            cc.setCurrencyFromAmount(amount);
            cc.setConversionPercentage(percent);
            convertedAmount = cc.getConvertedCurrencyAmount();

            Snackbar.make(view, fromCurrencyType.getText() + " " + fromCurrencyAmount.getText()
                    + " = " + toCurrencyType.getText() + " " + convertedAmount, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            // clears all inputs if clearChecker is checked
            if(clearAmountAfterCalculation) {
                fromCurrencyType.setText("");
                toCurrencyType.setText("");
                fromCurrencyAmount.setText("");
                conversionPercent.setText("");
                fromCurrencyType.requestFocus();
            }
        }
    }

    private void setFieldReferencesToResFileValues() {
        // These values are the same strings used in the prefs xml as keys for each pref there
        keyAutoClear = getString(R.string.key_auto_clear);
    }

    private void restoreAppSettingsFromPrefs() {
        // Since this is for reading only, no editor is needed unlike in saveRestoreState
        SharedPreferences preferences = getSharedPreferences(keyPrefsName, MODE_PRIVATE);

        // restore AutoSave preference value
        clearAmountAfterCalculation = preferences.getBoolean(keyAutoClear, true);
    }

    private void setupListeners() {
        fromCurrencyType = findViewById(R.id.from_currency_type_input);
        toCurrencyType = findViewById(R.id.to_currency_type_input);
        fromCurrencyAmount = findViewById(R.id.from_amount);
        conversionPercent = findViewById(R.id.conversion_percent);
        clearChecker = findViewById(R.id.action_clear_amount_after_calculation);
    }

    private void toggleMenuItem(MenuItem item) {
        item.setChecked(!item.isChecked());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_clear_amount_after_calculation).setChecked(clearAmountAfterCalculation);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        if (item.getItemId() == R.id.btn_about) {
            Utils.showInfoDialog(MainActivity.this, R.string.about, R.string.about_text);
            return true;
        }
        else if (item.getItemId() == R.id.btn_info) {
            Utils.showInfoDialog(MainActivity.this, R.string.info, R.string.info_text);
            return true;
        }
        else if (item.getItemId() == R.id.action_clear_amount_after_calculation) {
            toggleMenuItem(item);
            clearAmountAfterCalculation = item.isChecked();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
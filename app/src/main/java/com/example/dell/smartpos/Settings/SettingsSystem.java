package com.example.dell.smartpos.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.Printer.BluetoothSelect;
import com.example.dell.smartpos.R;

import java.util.Locale;

public class SettingsSystem extends AppCompatActivity {

    LinearLayout setting_printer;
    LinearLayout version_no;

    TextView connectedDevice;

    //---------for printer---------//
    String printeraddress = "";
    String printername = "";
    //---------for printer---------//

    SharedPreferences pref;

    String deviceMan = android.os.Build.MANUFACTURER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_system);

        setTitle(R.string.settings_merchant);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize layout
        setting_printer = (LinearLayout) findViewById(R.id.setting_printer);
        version_no = (LinearLayout) findViewById(R.id.version_no);

        connectedDevice = (TextView) findViewById(R.id.connectedDevice);

        // Initialize settings
        pref = getApplicationContext().getSharedPreferences(
                Constants.SETTINGS, MODE_PRIVATE);

        if (!deviceMan.equalsIgnoreCase("SUNMI")) {
            setting_printer.setVisibility(View.GONE);
        }

        //check language
        String lang = pref.getString(Constants.pref_Lang, "");
        changeLang(lang);

        // Initialize printer
        printeraddress = pref.getString(Constants.printer_address, "");
        printername = pref.getString(Constants.printer_name, "");
        if ("".equals(printeraddress) || printeraddress == null) {
            printeraddress = "";
        }
        if ("".equals(printername) || printername == null) {
            printername = "";
        }
        connectedDevice.setText(printername);

        // onCLick printer
        setting_printer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(SettingsSystem.this, BluetoothSelect.class);
                startActivity(intent);
            }
        });
    }

    public void changeLang(String lang) {
        Locale myLocale = null;
        if (lang.equalsIgnoreCase(Constants.LANG_ENG)) {
            myLocale = Locale.ENGLISH;
        } else if (lang.equalsIgnoreCase(Constants.LANG_TRAD)) {
            myLocale = Locale.TRADITIONAL_CHINESE;
        } else if (lang.equalsIgnoreCase(Constants.LANG_SIMP)) {
            myLocale = Locale.SIMPLIFIED_CHINESE;
        }else if (lang.equalsIgnoreCase(Constants.LANG_THAI)) {
            myLocale = new Locale("th");
        }
        Configuration config = new Configuration();
        config.locale = myLocale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    //--Edited 25/07/18 by KJ--//
    @Override
    public void onResume() {
        super.onResume();

        printername = pref.getString(Constants.printer_name, "");

        if ("".equals(printername) || printername == null) {
            printername = "";
        }
        connectedDevice.setText(printername);
    }
    //--done Edited 25/07/18 by KJ--//

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

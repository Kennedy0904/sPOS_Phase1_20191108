package com.example.dell.smartpos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    AlertDialog.Builder dialog;
    AlertDialog alertDialog;

    private TextView tvMerchantName;
    private TextView lastlogin;
    private TextView lastlogintime;

    private BottomNavigationView btmMainNav;

    private FrameLayout mainFrame;
    private PaymentFragment paymentFragment;
    private ProfileFragment profileFragment;
    private HistoryFragment historyFragment;
    private ReportFragment reportFragment;
    private SettingsFragment settingsFragment;
    private SupportFragment supportFragment;

    boolean loginSuccess = false;

    String currCode;
    String merchantId;
    String merchantName;
    String payMethod;
    String rate;
    String fixed;
    String hideSurcharge;
    String partnerlogo;
    String curLang = "";
    String comLang = "";
    String lastlogin1 = "";
    String operatorID;

    private Runnable runnable;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;

    HashMap<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get Merchant Details from Database
        SharedPreferences merdetails = getApplicationContext().getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);
        String rawMerId = (merdetails.getString(Constants.pref_MerID, ""));
        DatabaseHelper db = new DatabaseHelper(this);
        String whereargs[] = {rawMerId};
        String rawCurrCode = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_CURRCODE, Constants.DB_MERCHANT_ID, whereargs);
        String rawPayMethod = merdetails.getString(Constants.pref_pMethod, "");
        String rawRate = merdetails.getString(Constants.pref_Rate, "");
        String rawhideSurcharge = merdetails.getString(Constants.pref_hideSurcharge, "");
        String rawFixed = merdetails.getString(Constants.pref_Fixed, "");
        String rawPartnerlogo = merdetails.getString(Constants.pref_Partnerlogo, "");
        Log.d("OTTO", "Home rawRate:" + rawRate + ",rawFixed:" + rawFixed + ",paymethod:" + rawPayMethod + ",hideSurcharge:" + rawhideSurcharge + ",Partnerlogo:" + rawPartnerlogo);

        // Get operator ID
        String dbuserID = db.getSingleData(Constants.DB_TABLE_ID, Constants.DB_USER_ID, Constants.DB_MERCHANT_ID, whereargs);
        db.close();

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.SESSION_DATA, MODE_PRIVATE);
        lastlogin1 = pref.getString(Constants.pref_last_login, "");

        merchantName = merdetails.getString(Constants.MERNAME, "");

        try {
            DesEncrypter encrypter = new DesEncrypter(merchantName);
            merchantId = encrypter.decrypt(rawMerId);
//			merchantName = encrypter.decrypt(merdetails.getString(Constants.MERNAME, ""));*
            currCode = encrypter.decrypt(rawCurrCode);
            payMethod = encrypter.decrypt(rawPayMethod);
            rate = encrypter.decrypt(rawRate);
            hideSurcharge = encrypter.decrypt(rawhideSurcharge);
            fixed = encrypter.decrypt(rawFixed);

            if(!rawPartnerlogo.equals("")){
                partnerlogo = encrypter.decrypt(rawPartnerlogo);
            }

            if (dbuserID != null) {
                operatorID = encrypter.decrypt(dbuserID);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SharedPreferences prefsettings = getApplicationContext().getSharedPreferences("Settings", MODE_PRIVATE);
        String prefpaygate = prefsettings.getString(Constants.pref_PayGate, Constants.default_paygate);

        if(partnerlogo != null){
            String prefpartnerlogopaygate = prefpaygate;
            String baseUrl = PayGate.getURL_partnerlogo(prefpartnerlogopaygate) + partnerlogo;

            ImageView partnerlogoview = (ImageView)findViewById(R.id.partnerlogo);
            PartnerLogoUtil.setImageToImageView(partnerlogoview, baseUrl, partnerlogo);
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new SmoothActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        View headerView = navigationView.getHeaderView(0);
        tvMerchantName = (TextView) headerView.findViewById(R.id.headerMerchantName);
        tvMerchantName.setText(merchantName);

        // Start Fragment
        initWithFragments(savedInstanceState);

//        if (savedInstanceState == null) {
//            paymentFragment = new PaymentFragment();
//            Bundle paymentArgs = new Bundle();
//            paymentArgs.putString(Constants.CURRCODE, currCode);
//            paymentArgs.putString(Constants.MERNAME, merchantName);
//            paymentArgs.putString(Constants.MERID, merchantId);
//            paymentArgs.putString(Constants.PAYMETHODLIST, payMethod);
//            paymentArgs.putString(Constants.pref_Rate, rate);
//            paymentArgs.putString(Constants.pref_hideSurcharge, hideSurcharge);
//            paymentArgs.putString(Constants.pref_Fixed, fixed);
//            paymentArgs.putString(Constants.OPERATORID, operatorID);
//            paymentFragment.setArguments(paymentArgs);
//            setFragment(paymentFragment);
//        }
    }

    public void initWithFragments(Bundle savedInstanceState) {

        if (findViewById(R.id.main_frame) != null) {

            if (savedInstanceState != null) {
                return;
            }
            // Create a new Fragment to be placed in the activity layout
            ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
//            ViewPagerFragment viewPagerFragment = ViewPagerFragment.newInstance(menuList);
            map = new HashMap<>();
            map.put(Constants.CURRCODE, currCode);
            map.put(Constants.MERNAME, merchantName);
            map.put(Constants.MERID, merchantId);
            map.put(Constants.PAYMETHODLIST, payMethod);
            map.put(Constants.pref_Rate, rate);
            map.put(Constants.pref_hideSurcharge, hideSurcharge);
            map.put(Constants.pref_Fixed, fixed);
            map.put(Constants.OPERATORID, operatorID);

            Bundle args = new Bundle();
            args.putSerializable("map", map);
            viewPagerFragment.setArguments(args);
            setFragment(viewPagerFragment);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            Constants.tabSuccess=0;

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_frame);

            if (fragment instanceof ViewPagerFragment) {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(getString(R.string.exit_app))
                        .setMessage(getString(R.string.are_you_sure))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                android.os.Process.killProcess(android.os.Process.myPid());
                                finish();
                            }
                        })
                        .setNegativeButton(getString(R.string.no), null)
                        .show();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
        onPause();
    }

    // Reload Fragment after navigate back
    @Override
    protected void onResume() {
        super.onResume();

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_frame);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setReorderingAllowed(false);
        ft.detach(currentFragment);
        ft.attach(currentFragment);
        ft.commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

//        paymentFragment = new PaymentFragment();
        profileFragment = new ProfileFragment();
        historyFragment = new HistoryFragment();
        settingsFragment = new SettingsFragment();
        reportFragment = new ReportFragment();
        supportFragment = new SupportFragment();

        int id = item.getItemId();

        if (id == R.id.nav_home) {

            ((SmoothActionBarDrawerToggle) toggle).runWhenIdle(new Runnable() {
                @Override
                public void run() {

                    ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
                    Bundle args = new Bundle();
                    args.putSerializable("map", map);
                    viewPagerFragment.setArguments(args);
                    setFragment(viewPagerFragment);

//                    Bundle paymentArgs = new Bundle();
//                    paymentArgs.putString(Constants.CURRCODE, currCode);
//                    paymentArgs.putString(Constants.MERNAME, merchantName);
//                    paymentArgs.putString(Constants.MERID, merchantId);
//                    paymentArgs.putString(Constants.PAYMETHODLIST, payMethod);
//                    paymentArgs.putString(Constants.pref_Rate, rate);
//                    paymentArgs.putString(Constants.pref_hideSurcharge, hideSurcharge);
//                    paymentArgs.putString(Constants.pref_Fixed, fixed);
//                    paymentArgs.putString(Constants.OPERATORID, operatorID);
//                    paymentFragment.setArguments(paymentArgs);
//                    setFragment(paymentFragment);
                }
            });

            drawer.closeDrawers();

//        } else if (id == R.id.nav_profile) {
//            setFragment(profileFragment);
//            getSupportActionBar().setTitle("Profile");
        } else if (id == R.id.nav_history) {

            ((SmoothActionBarDrawerToggle) toggle).runWhenIdle(new Runnable() {
                @Override
                public void run() {

                    Bundle historyArgs = new Bundle();
                    historyArgs.putString(Constants.MERID, merchantId);
                    historyArgs.putString(Constants.MERNAME, merchantName);
                    historyFragment.setArguments(historyArgs);
                    setFragment(historyFragment);
                }
            });

            drawer.closeDrawers();

        } else if (id == R.id.nav_report) {

            ((SmoothActionBarDrawerToggle) toggle).runWhenIdle(new Runnable() {
                @Override
                public void run() {

                    Bundle reportArgs = new Bundle();
                    reportArgs.putString(Constants.MERID, merchantId);
                    reportArgs.putString(Constants.MERNAME, merchantName);
                    reportFragment.setArguments(reportArgs);
                    setFragment(reportFragment);
                }
            });

            drawer.closeDrawers();


        } else if (id == R.id.nav_settings) {

            dialog = new AlertDialog.Builder(this);
            dialog.setTitle(R.string.input_settingPW);
            Toast.makeText(this, R.string.input_settingPW_msg, Toast.LENGTH_LONG).show();
            //EditText
            final EditText inputSettingPassword = new EditText(this);
            inputSettingPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            inputSettingPassword.setHint(getString(R.string.password));

            dialog.setView(inputSettingPassword);
            dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences(
                            Constants.SETTINGS, MODE_PRIVATE);
                    SharedPreferences.Editor edit = pref.edit();
                    String value = inputSettingPassword.getText().toString().trim();


                    if (value.equals("")) {
                        edit.putString(Constants.pref_setting_mode, "mode3");
                        //  edit.commit();
                        loginSuccess = true;
                    } else if (value.equals(pref.getString(Constants.pref_Mer_SettingPW, Constants.default_settingPW)) || value.equals(pref.getString(Constants.admin_PW, Constants.admin_PW))) {
                        if (value.equals(pref.getString(Constants.pref_Mer_SettingPW, Constants.default_settingPW))) {
                            edit.putString(Constants.pref_setting_mode, "mode2");
                            // edit.commit();
                        } else {
                            edit.putString(Constants.pref_setting_mode, "mode1");

                        }

                        loginSuccess = true;
                    } else {
                        loginSuccess = false;
                        Toast.makeText(MainActivity.this, R.string.error3, Toast.LENGTH_SHORT).show();
                    }
                    edit.commit();

                    if (loginSuccess) {
                        settingsFragment = new SettingsFragment();

                        Bundle settingsArgs = new Bundle();

                        settingsArgs.putString(Constants.MERID, merchantId);
                        settingsArgs.putString(Constants.MERNAME, merchantName);
                        //--Edited 25/07/18 by KJ--//
                        settingsArgs.putString(Constants.PAYMETHODLIST, payMethod);
                        settingsArgs.putString(Constants.CURRCODE, currCode);
                        //--done Edited 25/07/18 by KJ--//
                        settingsFragment.setArguments(settingsArgs);
                        setFragment(settingsFragment);

                        loginSuccess = false;
                    }

                }
            });
            alertDialog = dialog.create();
            alertDialog.show();

            drawer.closeDrawers();

        } else if (id == R.id.nav_support) {
            setFragment(supportFragment);
            Bundle supportArg = new Bundle();
            supportArg.putString(Constants.MERID, merchantId);
            supportArg.putString(Constants.MERNAME, merchantName);
            supportFragment.setArguments(supportArg);
        } else if (id == R.id.nav_logout) {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("Session Data", MODE_PRIVATE);
            SharedPreferences.Editor prefedit = pref.edit();

            prefedit.putBoolean("user_logged_in", false);
            prefedit.commit();

            if (!pref.getBoolean("remember", false)) {
                SharedPreferences merdetails = getApplicationContext().getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);
                SharedPreferences.Editor meredit = merdetails.edit();
                meredit.clear();
                meredit.commit();
                prefedit.clear();
                prefedit.commit();
                meredit.putString(Constants.pref_MerName, merchantName);
                meredit.commit();
            }
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class SmoothActionBarDrawerToggle extends ActionBarDrawerToggle {

        private Runnable runnable;

        public SmoothActionBarDrawerToggle(AppCompatActivity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);

            SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
            String lang = prefsettings.getString(Constants.pref_Lang, Constants.default_language);
            comLang = lang;

            if (!comLang.equalsIgnoreCase(curLang)) {
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                lastlogin = (TextView) findViewById(R.id.textView);
                lastlogin.setText(R.string.last_login);

                lastlogintime = (TextView) findViewById(R.id.lastlogin);
                lastlogintime.setText(lastlogin1);

                Menu menu = navigationView.getMenu();

                MenuItem home = menu.findItem(R.id.nav_home);
                home.setTitle(R.string.home_list);
                MenuItem history = menu.findItem(R.id.nav_history);
                history.setTitle(R.string.history_list);
                MenuItem report = menu.findItem(R.id.nav_report);
                report.setTitle(R.string.report_list);
                MenuItem setting = menu.findItem(R.id.nav_settings);
                setting.setTitle(R.string.settings_list);
                MenuItem support = menu.findItem(R.id.nav_support);
                support.setTitle(R.string.support_list);
                MenuItem logout = menu.findItem(R.id.nav_logout);
                logout.setTitle(R.string.log_out_list);
            }

            if (runnable != null) {
                loginSuccess = false;
                runnable.run();
                runnable = null;
                invalidateOptionsMenu();
            }
        }

        @Override
        public void onDrawerClosed(View view) {
            SharedPreferences prefsettings = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);
            String lang = prefsettings.getString(Constants.pref_Lang, Constants.default_language);
            curLang = lang;

            super.onDrawerClosed(view);
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            if (runnable != null && newState == DrawerLayout.STATE_IDLE) {
                runnable.run();
                runnable = null;
            }
        }

        public void runWhenIdle(Runnable runnable) {
            this.runnable = runnable;
        }
    }

    public void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void restartActivity() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

}

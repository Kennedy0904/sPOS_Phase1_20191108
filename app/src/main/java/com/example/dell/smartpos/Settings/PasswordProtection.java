package com.example.dell.smartpos.Settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.dell.smartpos.Constants;
import com.example.dell.smartpos.R;

public class PasswordProtection extends AppCompatActivity {

    LinearLayout card_refund_pw_protect;
    LinearLayout card_settlement_pw_protect;
    LinearLayout card_void_pw_protect;
    LinearLayout qr_refund_pw_protect;
    LinearLayout qr_void_pw_protect;

    Switch swCardRefund;
    Switch swCardSettlement;
    Switch swCardVoid;
    Switch swQRRefund;
    Switch swQRVoid;

    String cardRefundPw;
    String cardSettlementPw;
    String cardVoidPw;
    String QRRefundPw;
    String QRVoidPw;

    String cardRefundControl;
    String cardSettlementControl;
    String cardVoidControl;
    String QRRefundControl;
    String QRVoidControl;

    Boolean setCardRefundPw;
    Boolean setCardSettlementPw;
    Boolean setCardVoidPw;
    Boolean setQRRefundPw;
    Boolean setQRVoidPw;

    TextView txtCardRefundPw;
    TextView txtCardSettlementPw;
    TextView txtCardVoidPw;
    TextView txtQRRefundPw;
    TextView txtQRVoidPw;

    AlertDialog.Builder dialog;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_protection);

        setTitle(R.string.password_protection);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize layout
        card_refund_pw_protect = (LinearLayout) findViewById(R.id.card_refund_pw_protect);
        card_settlement_pw_protect = (LinearLayout) findViewById(R.id.card_settlement_pw_protect);
        card_void_pw_protect = (LinearLayout) findViewById(R.id.card_void_pw_protect);
        qr_refund_pw_protect = (LinearLayout) findViewById(R.id.qr_refund_pw_protect);
        qr_void_pw_protect = (LinearLayout) findViewById(R.id.qr_void_pw_protect);

        swCardRefund = (Switch) findViewById(R.id.swCardRefund);
        swCardSettlement = (Switch) findViewById(R.id.swCardSettlement);
        swCardVoid = (Switch) findViewById(R.id.swCardVoid);
        swQRRefund = (Switch) findViewById(R.id.swQRRefund);
        swQRVoid = (Switch) findViewById(R.id.swQRVoid);

        txtCardRefundPw = (TextView) findViewById(R.id.txtCardRefundPw);
        txtCardSettlementPw = (TextView) findViewById(R.id.txtCardSettlementPw);
        txtCardVoidPw = (TextView) findViewById(R.id.txtCardVoidPw);
        txtQRRefundPw = (TextView) findViewById(R.id.txtQRRefundPw);
        txtQRVoidPw = (TextView) findViewById(R.id.txtQRVoidPw);

        // Initialize settings
        final SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.SETTINGS, MODE_PRIVATE);

        // Initialize Card Payment Refund
        cardRefundPw = pref.getString(Constants.pref_password_card_refund, Constants.default_protection_password);
        if (cardRefundPw != null) {
            setCardRefundPw = true;
        } else {
            setCardRefundPw = false;
        }

        cardRefundControl = pref.getString(Constants.pref_control_card_refund_password, Constants.default_control_password_protection);
        if (cardRefundControl.equalsIgnoreCase(getString(R.string.on))){
            swCardRefund.setChecked(true);
            txtCardRefundPw.setVisibility(View.VISIBLE);
            if(setCardRefundPw){
                txtCardRefundPw.setText("******");
            }else{
                txtCardRefundPw.setText(R.string.not_set);
            }
        }else{
            txtCardRefundPw.setVisibility(View.INVISIBLE);
            swCardRefund.setChecked(false);
        }

        // Initialize Card Payment Settlement
        cardSettlementPw = pref.getString(Constants.pref_password_card_settlement, Constants.default_protection_password);
        if (cardSettlementPw != null){
            setCardSettlementPw = true;
        } else {
            setCardSettlementPw = false;
        }

        cardSettlementControl = pref.getString(Constants.pref_control_card_settlement_password, Constants.default_control_password_protection);
        if (cardSettlementControl.equalsIgnoreCase(getString(R.string.on))){
            swCardSettlement.setChecked(true);
            txtCardSettlementPw.setVisibility(View.VISIBLE);
            if(setCardSettlementPw){
                txtCardSettlementPw.setText("******");
            }else{
                txtCardSettlementPw.setText(R.string.not_set);
            }
        }else{
            txtCardSettlementPw.setVisibility(View.INVISIBLE);
            swCardSettlement.setChecked(false);
        }

        // Initialize Card Payment Void
        cardVoidPw = pref.getString(Constants.pref_password_card_void, Constants.default_protection_password);
        if (cardVoidPw != null){
            setCardVoidPw = true;
        } else {
            setCardVoidPw = false;
        }

        cardVoidControl = pref.getString(Constants.pref_control_card_void_password, Constants.default_control_password_protection);
        if (cardVoidControl.equalsIgnoreCase(getString(R.string.on))){
            swCardVoid.setChecked(true);
            txtCardVoidPw.setVisibility(View.VISIBLE);
            if(setCardVoidPw){
                txtCardVoidPw.setText("******");
            }else{
                txtCardVoidPw.setText(R.string.not_set);
            }
        }else{
            txtCardVoidPw.setVisibility(View.INVISIBLE);
            swCardVoid.setChecked(false);
        }

        // Initialize QR Payment Refund
        QRRefundPw = pref.getString(Constants.pref_password_qr_refund, Constants.default_protection_password);
        if (QRRefundPw != null){
            setQRRefundPw = true;
        } else {
            setQRRefundPw = false;
        }

        QRRefundControl = pref.getString(Constants.pref_control_qr_refund_password, Constants.default_control_password_protection);
        if (QRRefundControl.equalsIgnoreCase(getString(R.string.on))){
            swQRRefund.setChecked(true);
            txtQRRefundPw.setVisibility(View.VISIBLE);
            if(setQRRefundPw){
                txtQRRefundPw.setText("******");
            }else{
                txtQRRefundPw.setText(R.string.not_set);
            }
        }else{
            txtQRRefundPw.setVisibility(View.INVISIBLE);
            swQRRefund.setChecked(false);
        }

        // Initialize QR Payment Void
        QRVoidPw = pref.getString(Constants.pref_password_qr_void, Constants.default_protection_password);
        if (QRVoidPw != null){
            setQRVoidPw = true;
        } else {
            setQRVoidPw = false;
        }

        QRVoidControl = pref.getString(Constants.pref_control_qr_void_password, Constants.default_control_password_protection);
        if (QRVoidControl.equalsIgnoreCase(getString(R.string.on))){
            swQRVoid.setChecked(true);
            txtQRVoidPw.setVisibility(View.VISIBLE);
            if(setQRVoidPw){
                txtQRVoidPw.setText("******");
            }else{
                txtQRVoidPw.setText(R.string.not_set);
            }
        }else{
            txtQRVoidPw.setVisibility(View.INVISIBLE);
            swQRVoid.setChecked(false);
        }

        // onClick Card Payment Refund (Switch)
        swCardRefund.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String checked;

                if (isChecked) {
                    checked = getString(R.string.on);
                    txtCardRefundPw.setVisibility(View.VISIBLE);
                    if(setCardRefundPw){
                        txtCardRefundPw.setText("******");
                    }else{
                        txtCardRefundPw.setText(R.string.not_set);
                    }
                } else {
                    checked = getString(R.string.off);
                    txtCardRefundPw.setVisibility(View.INVISIBLE);
                }

                SharedPreferences.Editor edit = pref.edit();
                edit.putString(Constants.pref_control_card_refund_password, checked);
                edit.commit();
            }
        });

        // onClick Card Payment Refund (LinearLayout)
        card_refund_pw_protect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if(swCardRefund.isChecked()){

                    dialog = new AlertDialog.Builder(PasswordProtection.this);
                    dialog.setTitle(R.string.input_PW);
                    //EditText
                    final EditText inputCardRefundPw = new EditText(PasswordProtection.this);
                    inputCardRefundPw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    dialog.setView(inputCardRefundPw);

                    if (setCardRefundPw) {
                        inputCardRefundPw.setHint(getString(R.string.unchanged));
                    }
                    dialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub

                            String password = inputCardRefundPw.getText().toString();
                            if ("".equals(password)) {

//                                if(refundQRPw == null){
//                                    txtRefundQRPw.setText(getString(R.string.not_set));
//                                }

                            } else {
                                txtCardRefundPw.setText("******");

                                //edit shared pref data
                                SharedPreferences.Editor edit = pref.edit();
                                edit.putString(Constants.pref_password_card_refund, password);
                                edit.commit();
                            }
                        }
                    });
                    alertDialog = dialog.create();
                    alertDialog.show();
                }
            }

        });

//        // onClick Card Payment Settlement (LinearLayout)
//        card_settlement_pw_protect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String checked = (pref.getString(Constants.pref_control_card_settlement_password, Constants.default_control_password_protection));
//
//                if (checked.equalsIgnoreCase(getString(R.string.on))) {
//                    dialog = new AlertDialog.Builder(PasswordProtection.this);
//                    dialog.setTitle(R.string.input_PW);
//                    //EditText
//                    final EditText inputCardSettlementPw = new EditText(PasswordProtection.this);
//                    inputCardSettlementPw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                    dialog.setView(inputCardSettlementPw);
//
//                    if (setCardSettlementPw) {
//                        inputCardSettlementPw.setHint(getString(R.string.unchanged));
//                    }
//                    dialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface arg0, int arg1) {
//                            // TODO Auto-generated method stub
//
//                            String password = inputCardSettlementPw.getText().toString();
//                            if ("".equals(password)) {
//
////                                if(refundQRPw == null){
////                                    txtRefundQRPw.setText(getString(R.string.not_set));
////                                }
//
//                            } else {
//                                txtCardSettlementPw.setText("******");
//
//                                //edit shared pref data
//                                SharedPreferences.Editor edit = pref.edit();
//                                edit.putString(Constants.pref_password_card_settlement, password);
//                                edit.commit();
//                            }
//                        }
//                    });
//                    alertDialog = dialog.create();
//                    alertDialog.show();
//
//                }else if(checked.equalsIgnoreCase(getString(R.string.off))){
//                    swCardSettlement.setChecked(true);
//                    checked = getString(R.string.on);
//                    if(setCardSettlementPw){
//                        txtCardSettlementPw.setText("******");
//                    }else{
//                        txtCardSettlementPw.setText(R.string.not_set);
//                    }
//                }
//
//                SharedPreferences.Editor edit = pref.edit();
//                edit.putString(Constants.pref_control_card_settlement_password, checked);
//                edit.commit();
//            }
//        });

        // onClick Card Payment Settlement (Switch)
        swCardSettlement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String checked;

                if (isChecked) {
                    checked = getString(R.string.on);
                    txtCardSettlementPw.setVisibility(View.VISIBLE);
                    if(setCardSettlementPw){
                        txtCardSettlementPw.setText("******");
                    }else{
                        txtCardSettlementPw.setText(R.string.not_set);
                    }
                } else {
                    checked = getString(R.string.off);
                    txtCardSettlementPw.setVisibility(View.INVISIBLE);
                }

                SharedPreferences.Editor edit = pref.edit();
                edit.putString(Constants.pref_control_card_settlement_password, checked);
                edit.commit();
            }
        });

        // onClick Card Payment Settlement (LinearLayout)
        card_settlement_pw_protect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(swCardSettlement.isChecked()){
                    dialog = new AlertDialog.Builder(PasswordProtection.this);
                    dialog.setTitle(R.string.input_PW);
                    //EditText
                    final EditText inputCardSettlementPw = new EditText(PasswordProtection.this);
                    inputCardSettlementPw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    dialog.setView(inputCardSettlementPw);

                    if (setCardSettlementPw) {
                        inputCardSettlementPw.setHint(getString(R.string.unchanged));
                    }
                    dialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub

                            String password = inputCardSettlementPw.getText().toString();
                            if ("".equals(password)) {

//                                if(refundQRPw == null){
//                                    txtRefundQRPw.setText(getString(R.string.not_set));
//                                }

                            } else {
                                txtCardSettlementPw.setText("******");

                                //edit shared pref data
                                SharedPreferences.Editor edit = pref.edit();
                                edit.putString(Constants.pref_password_card_settlement, password);
                                edit.commit();
                            }
                        }
                    });
                    alertDialog = dialog.create();
                    alertDialog.show();
                }
            }
        });

        // onClick Card Payment Void (Switch)
        swCardVoid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String checked;

                if (isChecked) {
                    checked = getString(R.string.on);
                    txtCardVoidPw.setVisibility(View.VISIBLE);
                    if(setCardVoidPw){
                        txtCardVoidPw.setText("******");
                    }else{
                        txtCardVoidPw.setText(R.string.not_set);
                    }
                } else {
                    checked = getString(R.string.off);
                    txtCardVoidPw.setVisibility(View.INVISIBLE);
                }

                SharedPreferences.Editor edit = pref.edit();
                edit.putString(Constants.pref_control_card_void_password, checked);
                edit.commit();
            }
        });

        // onClick Card Payment Void (LinearLayout)
        card_void_pw_protect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swCardVoid.isChecked()){
                    dialog = new AlertDialog.Builder(PasswordProtection.this);
                    dialog.setTitle(R.string.input_PW);
                    //EditText
                    final EditText inputCardVoidPw = new EditText(PasswordProtection.this);
                    inputCardVoidPw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    dialog.setView(inputCardVoidPw);

                    if (setCardVoidPw) {
                        inputCardVoidPw.setHint(getString(R.string.unchanged));
                    }
                    dialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub

                            String password = inputCardVoidPw.getText().toString();
                            if ("".equals(password)) {

                            } else {
                                txtCardVoidPw.setText("******");

                                //edit shared pref data
                                SharedPreferences.Editor edit = pref.edit();
                                edit.putString(Constants.pref_password_card_void, password);
                                edit.commit();
                            }
                        }
                    });
                    alertDialog = dialog.create();
                    alertDialog.show();
                }
            }
        });

        // onClick Card Payment Settlement (Switch)
        swQRRefund.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String checked;

                if (isChecked) {
                    checked = getString(R.string.on);
                    txtQRRefundPw.setVisibility(View.VISIBLE);
                    if(setQRRefundPw){
                        txtQRRefundPw.setText("******");
                    }else{
                        txtQRRefundPw.setText(R.string.not_set);
                    }
                } else {
                    checked = getString(R.string.off);
                    txtQRRefundPw.setVisibility(View.INVISIBLE);
                }

                SharedPreferences.Editor edit = pref.edit();
                edit.putString(Constants.pref_control_qr_refund_password, checked);
                edit.commit();
            }
        });

        // onClick QR Payment Refund (LinearLayout)
        qr_refund_pw_protect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swQRRefund.isChecked()){
                    dialog = new AlertDialog.Builder(PasswordProtection.this);
                    dialog.setTitle(R.string.input_PW);
                    //EditText
                    final EditText inputQRRefundPw = new EditText(PasswordProtection.this);
                    inputQRRefundPw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    dialog.setView(inputQRRefundPw);

                    if (setQRRefundPw) {
                        inputQRRefundPw.setHint(getString(R.string.unchanged));
                    }
                    dialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub

                            String password = inputQRRefundPw.getText().toString();
                            if ("".equals(password)) {

                            } else {
                                txtQRRefundPw.setText("******");

                                //edit shared pref data
                                SharedPreferences.Editor edit = pref.edit();
                                edit.putString(Constants.pref_password_qr_refund, password);
                                edit.commit();
                            }
                        }
                    });
                    alertDialog = dialog.create();
                    alertDialog.show();
                }
            }
        });

        // onClick Card Payment Settlement (Switch)
        swQRVoid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String checked;

                if (isChecked) {
                    checked = getString(R.string.on);
                    txtQRVoidPw.setVisibility(View.VISIBLE);
                    if(setQRVoidPw){
                        txtQRVoidPw.setText("******");
                    }else{
                        txtQRVoidPw.setText(R.string.not_set);
                    }
                } else {
                    checked = getString(R.string.off);
                    txtQRVoidPw.setVisibility(View.INVISIBLE);
                }

                SharedPreferences.Editor edit = pref.edit();
                edit.putString(Constants.pref_control_qr_void_password, checked);
                edit.commit();
            }
        });

        // onClick QR Payment Void (LinearLayout)
        qr_void_pw_protect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swQRVoid.isChecked()){
                    dialog = new AlertDialog.Builder(PasswordProtection.this);
                    dialog.setTitle(R.string.input_PW);
                    //EditText
                    final EditText inputQRVoidPw = new EditText(PasswordProtection.this);
                    inputQRVoidPw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    dialog.setView(inputQRVoidPw);

                    if (setQRVoidPw) {
                        inputQRVoidPw.setHint(getString(R.string.unchanged));
                    }
                    dialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub

                            String password = inputQRVoidPw.getText().toString();
                            if ("".equals(password)) {

                            } else {
                                txtQRVoidPw.setText("******");

                                //edit shared pref data
                                SharedPreferences.Editor edit = pref.edit();
                                edit.putString(Constants.pref_password_qr_void, password);
                                edit.commit();
                            }
                        }
                    });
                    alertDialog = dialog.create();
                    alertDialog.show();
                }
            }
        });
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

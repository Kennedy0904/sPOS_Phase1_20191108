package com.example.dell.smartpos;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettlementFragment extends Fragment {

    Button btnSettlement;
    TextView settlementTrx;
    TextView settlementTotalAmt;
    TextView settlementCurrCode;
    TextView settlementBatchNo;

    public SettlementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_settlement, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.settlement));

        settlementBatchNo = (TextView) rootView.findViewById(R.id.settlementBatchNo) ;
        settlementTrx = (TextView) rootView.findViewById(R.id.settlementTrx);


        btnSettlement = (Button) rootView.findViewById(R.id.btnSettlement);
        btnSettlement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CardPaymentSettlementTask cardPaymentSettlementTask = new CardPaymentSettlementTask(getActivity(),
                        getPrefPayGate(), SettlementFragment.this);

                String merchantID = getArguments().getString(Constants.MERID);
                String action = "querySettlement";
                String ProcessingCode = "920000";
                String SystemTraceNo = "000001";
                String batchNo = autoBBLQRBatchID();
                String batchTotal = "1000";

                cardPaymentSettlementTask.execute(
                        ProcessingCode,
                        SystemTraceNo,
                        batchNo,
                        batchTotal,
                        merchantID,
                        "apiuser",
                        "api1234",
                        action);
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    public String getPrefPayGate() {
        SharedPreferences prefsettings = getActivity()
                .getSharedPreferences("Settings", MODE_PRIVATE);
        String prefpaygate = prefsettings.getString(Constants.pref_PayGate,
                Constants.default_paygate);
        return prefpaygate;
    }

    private String autoBBLQRBatchID() {

        SharedPreferences pref = getActivity().getSharedPreferences(Constants.BBLQR_BATCHNO_COUNTER, MODE_PRIVATE);
        int curCounter = pref.getInt("BBLBatchCounter", 0);

        int runningNum = curCounter + 1;
        int length = String.valueOf(curCounter).length();
        String digit = "000000";
        String batchID = digit.substring(0, digit.length() - length) + runningNum;

        return batchID;
    }

    public void incBatchNo(){
        SharedPreferences pref = getContext().getSharedPreferences(Constants.BATCH_COUNTER, MODE_PRIVATE);
        int curCounter = pref.getInt("batchCounter", 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt("batchCounter", curCounter +=1);
        System.out.println("Batch ID = " + String.valueOf(curCounter +=1));
        edit.commit();
    }

    public void resetCardTraceNo(){
        SharedPreferences pref = getContext().getSharedPreferences(Constants.CARDTRACENO_COUNTER, MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt("cardTraceCounter", 0);
        edit.commit();
    }
}

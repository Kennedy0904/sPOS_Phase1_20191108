package com.example.dell.smartpos;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.TreeMap;

import static android.content.Context.MODE_PRIVATE;

public class RefundReportAdapter extends ArrayAdapter<Record> {

    Context context;
    int layoutResourceId;
    Record record;
    ArrayList<Record> data = null;

    String merName = "";
    String paymethod = "";

    RefundReportInterface refundInterface;

    public RefundReportAdapter(Context context, int layoutResourceId, ArrayList<Record> data, String merName, RefundReportInterface refundInterface) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        this.merName = merName;
        this.refundInterface = refundInterface;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        boolean isHeader;

        View row = convertView;
        RefundReportAdapter.RecordHolder holder = null;

        TreeMap<String, String> refund = null;
        ArrayList<Record> ALIPAYHKOFFLRefund = null;
        ArrayList<Record> ALIPAYCNOFFLRefund = null;
        ArrayList<Record> ALIPAYOFFLRefund = null;
        ArrayList<Record> BOOSTOFFLRefund = null;
        ArrayList<Record> GCASHOFFLRefund = null;
        ArrayList<Record> GRABPAYOFFLRefund = null;
        ArrayList<Record> masterRefund = null;
        ArrayList<Record> OEPAYOFFLRefund = null;
        ArrayList<Record> PROMPTPAYOFFLRefund = null;
        ArrayList<Record> UNIONPAYOFFLRefund = null;
        ArrayList<Record> visaRefund = null;
        ArrayList<Record> WECHATOFFLRefund = null;
        ArrayList<Record> WECHATHKOFFLRefund = null;
        ArrayList<Record> WECHATONLRefund = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RefundReportAdapter.RecordHolder();

            //Header
            holder.header = (LinearLayout) row.findViewById(R.id.refundreport_header);
            holder.pMethod = (TextView) row.findViewById(R.id.refundreport_pMethod);
            holder.totalTrx = (TextView) row.findViewById(R.id.refundreport_total_trx);
            holder.totalAmt = (TextView) row.findViewById(R.id.refundreport_total_amt);
            holder.txtInitPosition = (TextView) row.findViewById(R.id.initPosition);
            holder.txtDestPosition = (TextView) row.findViewById(R.id.destPosition);

            //Body
            holder.txtAmount = (TextView) row.findViewById(R.id.refundreport_amount);
            holder.txtMerRequestAmt = (TextView) row.findViewById(R.id.refundreport_merRequestAmt_History);
            holder.txtSurcharge = (TextView) row.findViewById(R.id.refundreport_Surcharge_History);
            holder.txtMref = (TextView) row.findViewById(R.id.refundreport_mref);
            holder.txtPref = (TextView) row.findViewById(R.id.refundreport_pref);
            holder.txtQRNumber = (TextView) row.findViewById(R.id.refundreport_qr_number);
            holder.txtTime = (TextView) row.findViewById(R.id.refundreport_date);
            holder.txtCurrCode = (TextView) row.findViewById(R.id.refundreport_currCode);
            row.setTag(holder);
        } else {
            holder = (RefundReportAdapter.RecordHolder) row.getTag();
        }

        //---------judge is airpay or not-----------//
        SharedPreferences merDetails = context.getSharedPreferences(Constants.USER_DATA, MODE_PRIVATE);
        String hideSurcharge = merDetails.getString(Constants.pref_hideSurcharge, "");
        try {
            DesEncrypter encrypter = new DesEncrypter(merName);
            hideSurcharge = encrypter.decrypt(hideSurcharge);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("OTTO", "+++++++RefundReportAdapter hideSurcharge:" + hideSurcharge);
        //---------judge is airpay or not----------//

        record = data.get(position);
        paymethod = record.payMethod;

        // Modified by Eric 20191011 - Set Payment Method Report Header
        String setHeader = GlobalFunction.setReportHeader(context, paymethod);
        holder.pMethod.setText(setHeader);


        DecimalFormat formatter = new DecimalFormat("#,###,###,##0.00");
        record.amt = formatter.format(Double.parseDouble(record.amt));
        record.merRequestAmt = formatter.format(Double.parseDouble(record.merRequestAmt));
//		 if( ("T".equals(hideSurcharge))&&("WECHATOFFL".equals(paymethod)||"WECHATONL".equals(paymethod)||"ALIPAYOFFL".equals(paymethod)||"ALIPAYCNOFFL".equals(paymethod)) ){
//            holder.txtAmount.setText(record.merRequestAmt);
//        }else{
//            holder.txtAmount.setText(record.amt);
//        }

//        for(int a = 0; a < data.size() ; a++) {
//
//            if (data.get(a).payMethod.equalsIgnoreCase("ALIPAYHKOFFL") ||
//                    data.get(a).payMethod.equalsIgnoreCase("ALIPAYCNOFFL") ||
//                    data.get(a).payMethod.equalsIgnoreCase("ALIPAYOFFL")) {
//
//                data.get(a).payMethod = context.getString(R.string.Alipay_label);
//
//
//            }else if(data.get(a).payMethod.equalsIgnoreCase("WECHATOFFL") ||
//                    data.get(a).payMethod.equalsIgnoreCase("WECHATONL")){
//
//                data.get(a).payMethod = context.getString(R.string.Wechatpay_label);
//
//            }
//
//            holder.pMethod.setText(data.get(a).payMethod);
//        }

        double total = 0;

        int i;
        int positionnow = position;
        int initPosition = 0;
        int destPosition = data.size() - 1;

        if (position == 0) {
            holder.txtInitPosition.setText(String.valueOf(initPosition));
        } else {
            if (position > 0 && !data.get(position).getPaymethod().equals(data.get(position - 1).getPaymethod())) {
                initPosition = position;
                holder.txtInitPosition.setText(String.valueOf(initPosition));
            } else if (position > 0 && data.get(position).getPaymethod().equals(data.get(position - 1).getPaymethod())) {

                while (position > 0 && data.get(position).getPaymethod().equals(data.get(position - 1).getPaymethod())) {
                    position--;
                    initPosition = position;
                }

                holder.txtInitPosition.setText((String.valueOf(initPosition)));
            }
        }

        position = positionnow;

        if (position == data.size() - 1) {
            holder.txtDestPosition.setText(String.valueOf(destPosition));
        } else {

            if (position < data.size() - 1 && !data.get(position).getPaymethod().equals(data.get(position + 1).getPaymethod())) {
                destPosition = position;
                holder.txtDestPosition.setText(String.valueOf(destPosition));
            } else if (position < data.size() - 1 && data.get(position).getPaymethod().equals(data.get(position + 1).getPaymethod())) {

                while (position < data.size() - 1 && data.get(position).getPaymethod().equals(data.get(position + 1).getPaymethod())) {
                    position++;
                    destPosition = position;
                }

                holder.txtDestPosition.setText(String.valueOf(destPosition));
            }
        }

        initPosition = Integer.parseInt(holder.txtInitPosition.getText().toString());
        destPosition = Integer.parseInt(holder.txtDestPosition.getText().toString());

        for (i = initPosition; i <= destPosition; i++) {
            total = total + Double.parseDouble(data.get(i).amt.replaceAll(",", ""));
        }

        holder.totalAmt.setText(record.currency + " " + formatter.format(total));
        holder.totalTrx.setText(String.valueOf(destPosition - initPosition + 1));

        holder.txtAmount.setText(record.amt);
        if ((record.merRequestAmt == null) || ("".equals(record.merRequestAmt))) {
            holder.txtMerRequestAmt.setText(record.amt);
        } else {
            if (Double.valueOf(record.merRequestAmt.replaceAll(",", "")) <= 0) {
                holder.txtMerRequestAmt.setText(record.amt);
            } else {
                holder.txtMerRequestAmt.setText(record.merRequestAmt);
            }
        }
        holder.txtSurcharge.setText(record.Surcharge);
        holder.txtCurrCode.setText(record.currency);
        holder.txtMref.setText(record.merref);
        holder.txtPref.setText(record.paymentRef);
        holder.txtQRNumber.setText(record.cardNo);

        String newtime = record.getOrderdate();
        String date = newtime.substring(0, 8);
        String time = newtime.substring(8, 14);

        StringBuilder sbdate = new StringBuilder(date);
        sbdate.insert(2, "/");
        sbdate.insert(5, "/");
        StringBuilder sbtime = new StringBuilder(time);
        sbtime.insert(2, ":");
        sbtime.insert(5, ":");
        holder.txtTime.setText(sbdate.toString() + " " + sbtime.toString());

        position = positionnow;

        if (position == 0) {

            isHeader = true;

        } else {

            if (data.get(position).getPaymethod().equals(data.get(position - 1).getPaymethod())) {
                isHeader = false;
            } else {
                isHeader = true;
            }
        }

        if (isHeader == true) {
            holder.header.setVisibility(View.VISIBLE);

        } else {
            holder.header.setVisibility(View.GONE);
        }

        refund = new TreeMap<String, String>();
        ALIPAYHKOFFLRefund = new ArrayList<>();
        ALIPAYCNOFFLRefund = new ArrayList<>();
        ALIPAYOFFLRefund = new ArrayList<>();
        BOOSTOFFLRefund = new ArrayList<>();
        GCASHOFFLRefund = new ArrayList<>();
        GRABPAYOFFLRefund = new ArrayList<>();
        masterRefund = new ArrayList<>();
        OEPAYOFFLRefund = new ArrayList<>();
        PROMPTPAYOFFLRefund = new ArrayList<>();
        UNIONPAYOFFLRefund = new ArrayList<>();
        visaRefund = new ArrayList<>();
        WECHATOFFLRefund = new ArrayList<>();
        WECHATHKOFFLRefund = new ArrayList<>();
        WECHATONLRefund = new ArrayList<>();

        int ALIPAYHKOFFLcount = 0;
        double ALIPAYHKOFFLtotal = 0;

        int ALIPAYCNOFFLcount = 0;
        double ALIPAYCNOFFLtotal = 0;

        int ALIPAYOFFLcount = 0;
        double ALIPAYOFFLtotal = 0;

        int BOOSTOFFLcount = 0;
        double BOOSTOFFLtotal = 0;

        int GCASHOFFLcount = 0;
        double GCASHOFFLtotal = 0;

        int GRABPAYOFFLcount = 0;
        double GRABPAYOFFLtotal = 0;

        int masterCount = 0;
        double masterTotal = 0;

        int OEPAYOFFLcount = 0;
        double OEPAYOFFLtotal = 0;

        int PROMPTPAYOFFLcount = 0;
        double PROMPTPAYOFFLtotal = 0;

        int UNIONPAYOFFLCount = 0;
        double UNIONPAYOFFLTotal = 0;

        int visaCount = 0;
        double visaTotal = 0;

        int WECHATOFFLcount = 0;
        double WECHATOFFLtotal = 0;

        int WECHATHKOFFLcount = 0;
        double WECHATHKOFFLtotal = 0;

        int WECHATONLcount = 0;
        double WECHATONLtotal = 0;

        for (int a = 0; a < data.size(); a++) {

            record = data.get(a);
            String amount = formatter.format(Double.parseDouble(record.getamt().replaceAll(",","")));
            record = new Record(record.PayRef(), record.merref(), record.getOrderdate(), record.currency(), amount, record.getSurcharge(), record.getMerRequestAmt(), record.remark(), record.orderstatus(), merName, record.getPayType(), record.getPaymethod()
                    , record.getPayBankId(), record.cardNo(), record.getCardHolder(), record.getSettle(), record.getBankId());

            if (data.get(a).payMethod.equalsIgnoreCase("ALIPAYHKOFFL")) {
                ALIPAYHKOFFLcount++;
                ALIPAYHKOFFLtotal += Double.parseDouble(data.get(a).getamt().replaceAll(",",""));
                ALIPAYHKOFFLRefund.add(record);
            } else if (data.get(a).payMethod.equalsIgnoreCase("ALIPAYCNOFFL")) {
                ALIPAYCNOFFLcount++;
                ALIPAYCNOFFLtotal += Double.parseDouble(data.get(a).getamt().replaceAll(",",""));
                ALIPAYCNOFFLRefund.add(record);
            } else if (data.get(a).payMethod.equalsIgnoreCase("ALIPAYOFFL")) {
                ALIPAYOFFLcount++;
                ALIPAYOFFLtotal += Double.parseDouble(data.get(a).getamt().replaceAll(",",""));
                ALIPAYOFFLRefund.add(record);
            } else if (data.get(a).payMethod.equalsIgnoreCase("BOOSTOFFL")) {
                BOOSTOFFLcount++;
                BOOSTOFFLtotal += Double.parseDouble(data.get(a).getamt().replaceAll(",", ""));
                BOOSTOFFLRefund.add(record);
            } else if (data.get(a).payMethod.equalsIgnoreCase("GCASHOFFL")) {
                GCASHOFFLcount++;
                GCASHOFFLtotal += Double.parseDouble(data.get(a).getamt().replaceAll(",", ""));
                BOOSTOFFLRefund.add(record);
            } else if (data.get(a).payMethod.equalsIgnoreCase("GRABPAYOFFL")) {
                GRABPAYOFFLcount++;
                GRABPAYOFFLtotal += Double.parseDouble(data.get(a).getamt().replaceAll(",", ""));
                GRABPAYOFFLRefund.add(record);
            } else if (data.get(a).payMethod.equalsIgnoreCase("MASTER")) {
                masterCount++;
                masterTotal += Double.parseDouble(data.get(a).getamt().replaceAll(",",""));
                masterRefund.add(record);
            } else if (data.get(a).payMethod.equalsIgnoreCase("OEPAYOFFL")) {
                OEPAYOFFLcount++;
                OEPAYOFFLtotal += Double.parseDouble(data.get(a).getamt().replaceAll(",", ""));
                OEPAYOFFLRefund.add(record);
            } else if (data.get(a).payMethod.equalsIgnoreCase("PROMPTPAYOFFL")) {
                PROMPTPAYOFFLcount++;
                PROMPTPAYOFFLtotal += Double.parseDouble(data.get(a).getamt().replaceAll(",", ""));
                PROMPTPAYOFFLRefund.add(record);
            } else if (data.get(a).payMethod.equalsIgnoreCase("UNIONPAYOFFL")) {
                UNIONPAYOFFLCount++;
                UNIONPAYOFFLTotal += Double.parseDouble(data.get(a).getamt().replaceAll(",", ""));
                UNIONPAYOFFLRefund.add(record);
            } else if (data.get(a).payMethod.equalsIgnoreCase("VISA")) {
                visaCount++;
                visaTotal += Double.parseDouble(data.get(a).getamt().replaceAll(",",""));
                visaRefund.add(record);
            } else if (data.get(a).payMethod.equalsIgnoreCase("WECHATOFFL")) {
                WECHATOFFLcount++;
                WECHATOFFLtotal += Double.parseDouble(data.get(a).getamt().replaceAll(",",""));
                WECHATOFFLRefund.add(record);
            } else if (data.get(a).payMethod.equalsIgnoreCase("WECHATHKOFFL")) {
                WECHATHKOFFLcount++;
                WECHATHKOFFLtotal += Double.parseDouble(data.get(a).getamt().replaceAll(",", ""));
                WECHATHKOFFLRefund.add(record);
            } else if (data.get(a).payMethod.equalsIgnoreCase("WECHATONL")) {
                WECHATONLcount++;
                WECHATONLtotal += Double.parseDouble(data.get(a).getamt().replaceAll(",",""));
                WECHATONLRefund.add(record);
            }
        }
        refund.put("ALIPAYHKOFFLTotalRefund", String.valueOf(ALIPAYHKOFFLcount));
        refund.put("ALIPAYHKOFFLRefundAmt", String.valueOf(ALIPAYHKOFFLtotal));

        refund.put("ALIPAYCNOFFLTotalRefund", String.valueOf(ALIPAYCNOFFLcount));
        refund.put("ALIPAYCNOFFLRefundAmt", String.valueOf(ALIPAYCNOFFLtotal));

        refund.put("ALIPAYOFFLTotalRefund", String.valueOf(ALIPAYOFFLcount));
        refund.put("ALIPAYOFFLRefundAmt", String.valueOf(ALIPAYOFFLtotal));

        refund.put("BOOSTOFFLTotalRefund", String.valueOf(BOOSTOFFLcount));
        refund.put("BOOSTOFFLRefundAmt", String.valueOf(BOOSTOFFLtotal));

        refund.put("GCASHOFFLTotalRefund", String.valueOf(GCASHOFFLcount));
        refund.put("GCASHOFFLRefundAmt", String.valueOf(GCASHOFFLtotal));

        refund.put("GRABPAYOFFLTotalRefund", String.valueOf(GRABPAYOFFLcount));
        refund.put("GRABPAYOFFLRefundAmt", String.valueOf(GRABPAYOFFLtotal));

        refund.put("MasterTotalRefund", String.valueOf(masterCount));
        refund.put("MasterRefundAmt", String.valueOf(masterTotal));

        refund.put("OEPAYOFFLTotalRefund", String.valueOf(OEPAYOFFLcount));
        refund.put("OEPAYOFFLRefundAmt", String.valueOf(OEPAYOFFLtotal));

        refund.put("PROMPTPAYOFFLTotalRefund", String.valueOf(PROMPTPAYOFFLcount));
        refund.put("PROMPTPAYOFFLRefundAmt", String.valueOf(PROMPTPAYOFFLtotal));

        refund.put("UNIONPAYOFFLTotalRefund", String.valueOf(UNIONPAYOFFLCount));
        refund.put("UNIONPAYOFFLRefundAmt", String.valueOf(UNIONPAYOFFLTotal));

        refund.put("VisaTotalRefund", String.valueOf(visaCount));
        refund.put("VisaRefundAmt", String.valueOf(visaTotal));

        refund.put("WECHATOFFLTotalRefund", String.valueOf(WECHATOFFLcount));
        refund.put("WECHATOFFLRefundAmt", String.valueOf(WECHATOFFLtotal));

        refund.put("WECHATHKOFFLTotalRefund", String.valueOf(WECHATHKOFFLcount));
        refund.put("WECHATHKOFFLRefundAmt", String.valueOf(WECHATHKOFFLtotal));

        refund.put("WECHATONLTotalRefund", String.valueOf(WECHATONLcount));
        refund.put("WECHATONLRefundAmt", String.valueOf(WECHATONLtotal));

        //Send transactions collected to TransactionReport
        refundInterface.setRefundTransactions(refund,
                ALIPAYHKOFFLRefund,
                ALIPAYCNOFFLRefund,
                ALIPAYOFFLRefund,
                BOOSTOFFLRefund,
                GCASHOFFLRefund,
                GRABPAYOFFLRefund,
                masterRefund,
                OEPAYOFFLRefund,
                PROMPTPAYOFFLRefund,
                UNIONPAYOFFLRefund,
                visaRefund,
                WECHATOFFLRefund,
                WECHATHKOFFLRefund,
                WECHATONLRefund);

        return row;
    }

    static class RecordHolder {
        TextView pMethod;
        TextView totalTrx;
        TextView totalAmt;
        TextView txtTime;
        TextView txtPref;
        TextView txtCurrCode;
        TextView txtAmount;
        TextView txtMerRequestAmt;
        TextView txtSurcharge;
        TextView txtMref;
        TextView txtQRNumber;
        LinearLayout header;
        TextView txtInitPosition;
        TextView txtDestPosition;
    }
}

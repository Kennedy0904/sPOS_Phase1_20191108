package com.example.dell.smartpos;

import android.content.Context;

public class GlobalFunction {

	// This function used to check the payment type is QR payment type or not
	public static Boolean isQRpMethods(String pMethod) {

		String[] pMethodArray = {"ALIPAYHKOFFL", "ALIPAYOFFL", "BOOSTOFFL", "GCASHOFFL",
				"GRABPAYOFFL", "OEPAYOFFL", "PROMPTPAYOFFL", "UNIONPAYOFFL", "WECHATHKOFFL",
				"WECHATOFFL", "WECHATONL", "WECHATHKOFFL"};

		Boolean isQR = false;

		int i = 0;
		while (i < pMethodArray.length) {
			if (pMethod.equalsIgnoreCase(pMethodArray[i])) {
				isQR = true;
				break;
			}
			//System.out.println("aaapMethod " + i + ": " + pMethodArray[i]);
			i++;
		}

		return isQR;
	}

	// This function used to set the payment method report header inside
	// (Transaction, Refund, Request Refund, Void) Report
	// Note: Modify this function will affect TransactionReportAdapter, RefundReportAdapter,
	// VoidReportAdapter and RequestRefundReportAdapter
	public static String setReportHeader(Context context, String paymethod) {

		String reportHeaderText = "No Payment Method Matched";

		if (paymethod.equalsIgnoreCase("ALIPAYCNOFFL")) {
			reportHeaderText = context.getString(R.string.ALIPAYCNOFFL_label);
		} else if (paymethod.equalsIgnoreCase("ALIPAYHKOFFL")) {
			reportHeaderText = context.getString(R.string.ALIPAYHKOFFL_label);
		} else if (paymethod.equalsIgnoreCase("ALIPAYOFFL")) {
			reportHeaderText = context.getString(R.string.ALIPAYOFFL_label);
		} else if (paymethod.equalsIgnoreCase("BOOSTOFFL")) {
			reportHeaderText = context.getString(R.string.BOOSTOFFL_label);
		} else if (paymethod.equalsIgnoreCase("GCASHOFFL")) {
			reportHeaderText = context.getString(R.string.GCASHOFFL_label);
		} else if (paymethod.equalsIgnoreCase("GRABPAYOFFL")) {
			reportHeaderText = context.getString(R.string.GRABPAYOFFL_label);
		} else if (paymethod.equalsIgnoreCase("MASTER")) {
			reportHeaderText = context.getString(R.string.master_label);
		} else if (paymethod.equalsIgnoreCase("OEPAYOFFL")) {
			reportHeaderText = context.getString(R.string.OEPAYOFFL_label);
		} else if (paymethod.equalsIgnoreCase("PROMPTPAYOFFL")) {
			reportHeaderText = context.getString(R.string.PROMPTPAYOFFL_label);
		} else if (paymethod.equalsIgnoreCase("UNIONPAYOFFL")) {
			reportHeaderText = context.getString(R.string.UNIONPAYOFFL_label);
		} else if (paymethod.equalsIgnoreCase("VISA")) {
			reportHeaderText = context.getString(R.string.visa_label);
		} else if (paymethod.equalsIgnoreCase("WECHATHKOFFL")) {
			reportHeaderText = context.getString(R.string.WECHATHKOFFL_label);
		} else if (paymethod.equalsIgnoreCase("WECHATOFFL")) {
			reportHeaderText = context.getString(R.string.WECHATOFFL_label);
		} else if (paymethod.equalsIgnoreCase("WECHATONL")) {
			reportHeaderText = context.getString(R.string.WECHATONL_label);
		}

		return reportHeaderText;
	}
}

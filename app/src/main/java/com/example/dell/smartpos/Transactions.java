package com.example.dell.smartpos;

import java.util.List;

public class Transactions {

    String total;
    List<Record> records;

    public String getTotal() {
        return total;
    }

    public void setTotal(String totals) {
        this.total = totals;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }
}

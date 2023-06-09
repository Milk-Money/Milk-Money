package com.milkmoney.models;

import java.time.LocalDate;

public class Trade implements Comparable<Trade>{
    private long id;
    private String ticker;
    private String range;
    private String transactionType;
    private LocalDate date;

    private LocalDate reportedDate;
    private Politician politician;

    public Trade() {
    }

    public Trade(String ticker, String range, String transactionType, Politician politician) {
        this.ticker = ticker;
        this.range = range;
        this.transactionType = transactionType;
        this.politician = politician;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Politician getPolitician() {
        return politician;
    }

    public void setPolitician(Politician politician) {
        this.politician = politician;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(LocalDate reportedDate) {
        this.reportedDate = reportedDate;
    }

    @Override
    public int compareTo(Trade trade) {
        if (getDate() == null || trade.getDate() == null) {
            return 0;
        }
        return getDate().compareTo(trade.getDate());
    }
}

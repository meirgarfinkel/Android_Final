package com.example.finalexam2.models;

import com.google.gson.Gson;

public class CurrencyConverter {

    private double currencyFromAmount, conversionPercentage;

    public CurrencyConverter (double currencyFromAmount, double conversionPercentage)
    {
        this.currencyFromAmount = currencyFromAmount;
        this.conversionPercentage = conversionPercentage;
    }

    public CurrencyConverter ()
    {
        this(0,0);
    }

    public double getConvertedCurrencyAmount ()
    {
        if (currencyFromAmount <= 0)
            throw new IllegalStateException ("Currency From Amount must be greater than zero.");

        return currencyFromAmount + (currencyFromAmount * conversionPercentage);
    }

    public double getCurrencyFromAmount ()
    {
        return currencyFromAmount;
    }

    public void setCurrencyFromAmount (double currencyFromAmount)
    {
        this.currencyFromAmount = currencyFromAmount;
    }

    public double getConversionPercentage ()
    {
        return conversionPercentage;
    }

    public void setConversionPercentage (double conversionPercentage)
    {
        this.conversionPercentage = conversionPercentage;
    }

    public static CurrencyConverter getCurrencyConverterObjectFromJSONString (String json)
    {
        return new Gson ().fromJson (json, CurrencyConverter.class);
    }

    public String getJSONStringFromThis()
    {
        return new Gson().toJson (this);
    }
}

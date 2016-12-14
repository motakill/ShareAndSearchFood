package com.shareandsearchfood.ParcelerObjects;

import android.util.Log;

/**
 * Created by tiagomota on 14/12/16.
 */

public class Rate {

    private int people;
    private float value;
    private float rates;

    public Rate (int people, float value, float rates){
        this.people = people;
        this.value = value;
        this.rates = rates;
    }

    public Rate(){

    }
    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public float getValue() {
        return value;
    }

    public float getRates() {
        return rates;
    }

    public void setRates(float rates) {
        this.rates = rates;
    }

    public void setValue(float value) {
       this.value = value;
    }

    public void media(float value){
        people ++;
        rates += value;
        this.value = rates/people;
    }

}

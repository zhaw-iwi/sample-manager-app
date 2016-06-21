package com.svendroid.samplemanagerapp.model;

/**
 * Created by svhe on 05.05.2016.
 */
public class MeasureChild {
    private String[] validValues;
    private Measure[] childMeasure;

    public String[] getValidValues() {
        return validValues;
    }

    public void setValidValues(String[] validValues) {
        this.validValues = validValues;
    }

    public Measure[] getChildMeasure() {
        return childMeasure;
    }

    public void setChildMeasure(Measure[] childMeasure) {
        this.childMeasure = childMeasure;
    }
}

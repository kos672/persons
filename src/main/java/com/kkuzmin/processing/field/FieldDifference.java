package com.kkuzmin.processing.field;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class FieldDifference {

    @Column(name = "FIELD_NAME")
    private String fieldName;

    @Column(name = "PREV_VALUE")
    private String previousValue;

    @Column(name = "NEW_VALUE")
    private String newValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "CLASSIFICATION")
    private Classification classification;

    @Column(name = "SIMILARITY")
    private double similarity;

    public FieldDifference() { }

    public FieldDifference(String fieldName, String previousValue, String newValue, Classification classification, double similarity) {
        this.fieldName = fieldName;
        this.previousValue = previousValue;
        this.newValue = newValue;
        this.classification = classification;
        this.similarity = similarity;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(String previousValue) {
        this.previousValue = previousValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }
}

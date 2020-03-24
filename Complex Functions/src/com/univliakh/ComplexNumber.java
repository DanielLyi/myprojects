package com.univliakh;

public class ComplexNumber {
    private double realPart;
    private double imaginaryPart;

    public ComplexNumber(double real, double imaginary){
        realPart = real;
        imaginaryPart = imaginary;
    }

    public void setRealPart(double realPart) {
        this.realPart = realPart;
    }

    public void setImaginaryPart(double imaginaryPart) {
        this.imaginaryPart = imaginaryPart;
    }

    public double getRealPart() {
        return realPart;
    }

    public double getImaginaryPart() {
        return imaginaryPart;
    }
}

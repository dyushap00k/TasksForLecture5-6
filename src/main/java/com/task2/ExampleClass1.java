package com.task2;

import java.time.LocalDateTime;

public class ExampleClass1 {
    @Property(name = "stringProperty")
    private String myString;
    @Property(name = "numberProperty")
    private int myNumber;
    @Property(name = "timeProperty")
    private LocalDateTime myTime;

    public String getMyString() {
        return myString;
    }

    public int getMyNumber() {
        return myNumber;
    }

    public LocalDateTime getMyTime() {
        return myTime;
    }

    @Override
    public String toString() {
        return "ExampleClass1{" +
                "myString='" + myString + '\'' +
                ", myNumber=" + myNumber +
                ", myTime=" + myTime +
                '}';
    }
}

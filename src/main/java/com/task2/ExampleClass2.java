package com.task2;

import java.time.LocalDateTime;

public class ExampleClass2 {
    private String stringProperty;
    private long numberProperty;
    @Property(format = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime timeProperty;

    public String getStringProperty() {
        return stringProperty;
    }

    public long getNumberProperty() {
        return numberProperty;
    }

    public LocalDateTime getTimeProperty() {
        return timeProperty;
    }
}

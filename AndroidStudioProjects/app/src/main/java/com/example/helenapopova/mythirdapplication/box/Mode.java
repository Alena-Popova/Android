package com.example.helenapopova.mythirdapplication.box;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


public class Mode {
    private String title;
    private String value;
    private String hint;

    public Mode(String title, String value, String hint) {
        this.title = title;
        this.value = value;
        this.hint = hint;
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }

    public String getHint() {
        return hint;
    }
}

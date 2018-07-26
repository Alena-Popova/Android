package com.example.helenapopova.mythirdapplication.box;

import lombok.Data;

@Data
public class Mode {
    private String title;
    private String value;
    private String hint;

    public Mode(String title, String value, String hint) {
        this.title = title;
        this.value = value;
        this.hint = hint;
    }
}

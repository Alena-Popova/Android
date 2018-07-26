package com.example.helenapopova.mythirdapplication.connect;


import com.example.helenapopova.mythirdapplication.R;

import java.util.ArrayList;
import java.util.List;

public class Info {
    private static String[] titlesMode = {
            "mode Work: ",
            "mode Amplification: ",
            "mode Linearity: ",
            "mode Offset: ",
            "mode The ratio 1T: ",
            "mode The ratio 2T: ",
            "mode Course: " ,
            "Incoming bytes: ",
            "Data query operation: "};
    private static char[] operationsMode = {
            '@',
            'A',
            'B',
            'C',
            'D',
            'E',
            'F',
            'C',
            '1'};
    private static String[] hints = {
            "(default 0x40)",
            "(default 0x41)",
            "(default 0x42)",
            "(default 0x43)",
            "(default 0x44)",
            "(default 0x45)",
            "(default 0x46)",
            "(default 12)",
            "(default '1')"};
    //____________________________________


    public static char getNodeFirst() {
        return operationsMode[0];
    }

    public static char getNodeSecond() {
        return operationsMode[1];
    }

    public static char getNodeThird() {
        return operationsMode[2];
    }

    public static char getNodeForth() {
        return operationsMode[3];
    }

    public static char getNodeFifth() {
        return operationsMode[4];
    }

    public static char getNodeSixth() {
        return operationsMode[5];
    }

    public static char getNodeSeventh() {
        return operationsMode[6];
    }

    public static String getModeStringItem(int item) {
        int operation = item - 24;
        String result;
        switch (item) {
            case 64 :
                result = "mode Work - 0x" + operation;
                break;
            case 65 :
                result = "mode Amplification - 0x" + operation;
                break;
            case 66 :
                result = "mode Linearity - 0x"+ operation;
                break;
            case 67 :
                result = "mode Offset - 0x" + operation;
                break;
            case 68 :
                result = "mode The ratio 1T - 0x" + operation;
                break;
            case 69 :
                result = "mode The ratio 2T - 0x" + operation;
                break;
            case 70 :
                result = "mode Course - 0x" + operation;
                break;
            default:
                result = "error #1";
                break;
        }
        return result;
    }

    public static String[] getTitlesMode() {
        return titlesMode;
    }

    public static char[] getOperationsMode() {
        return operationsMode;
    }

    public static String[] getHints() {
        return hints;
    }

    public static int getSize() {
        return titlesMode.length;
    }
}

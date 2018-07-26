package com.example.helenapopova.mythirdapplication.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helenapopova.mythirdapplication.R;
import com.example.helenapopova.mythirdapplication.connect.FTDIconnector;
import com.example.helenapopova.mythirdapplication.connect.Info;

import java.io.IOException;

import lombok.Getter;

public class ButtonLink extends Fragment implements View.OnClickListener {

    private FTDIconnector connectorFTDI;
    EditText textViewSelectMode;
    Button buttonStart;
    Button buttonStop;
    Button buttonSelect;
    View inflatedView;
    TextView temperature;
    TextView acceptStr;
    @Getter
    private byte bufferFTDI[] = new byte[14];


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.button_link, container, false);
        // обработчики нажатия кнопок
        registerButtons(inflatedView);
        // контекстное меню для селекта
        registerForContextMenu(buttonSelect);
        return inflatedView;
    }

    public void registerButtons(View context) {
        buttonStart = (Button) context.findViewById(R.id.button);
        buttonStop = (Button) context.findViewById(R.id.button2);
        buttonSelect = (Button) context.findViewById(R.id.button_for_select_mode);
        temperature = context.findViewById(R.id.temperature_in_data);
        acceptStr = context.findViewById(R.id.accepted_data);
        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
        buttonSelect.setOnClickListener(this);
        buttonSelect.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                onClickStart();
                break;
            case R.id.button2:
                onClickStop();
                break;
            case R.id.button_for_select_mode:
                sendMessageAndGetAnswer();
                break;
            default:
                outputTost("Error #2");
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, Info.getNodeFirst(), 1, "Работа - 0х40");
        menu.add(0, Info.getNodeSecond(), 2, "Усиление - 0х41");
        menu.add(0, Info.getNodeThird(), 3, "Линейность - 0х42");
        menu.add(0, Info.getNodeForth(), 4, "Смещение - 0х43");
        menu.add(0, Info.getNodeFifth(), 5, "Отношение 1Т - 0х44");
        menu.add(0, Info.getNodeSixth(), 6, "Отношение 2Т - 0х45");
        menu.add(0, Info.getNodeSeventh(), 7, "Курс - 0х46");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean result = false;
        TextView textView = inflatedView.findViewById(R.id.mode_data);
        if (item.getItemId() > 63 && item.getItemId() < 71) {
            onClickSend(Character.toString((char) item.getItemId()));
            textView.setText(Info.getModeStringItem(item.getItemId()));
            result = true;
        }
        return result;
    }

    public void onClickStart() {
        connectorFTDI = new FTDIconnector(getActivity());
        try {
            connectorFTDI.init();
        } catch (IOException io) {
            outputTost("io init");
        }
        buttonSelect.setEnabled(true);
    }


    public boolean onClickSend(String operation) {
        boolean result = false;
        if (connectorFTDI != null && operation != null && operation.length() != 0) {
            try {
                byte[] mask = new byte[1];
                mask[0] = (byte) operation.charAt(0);
                connectorFTDI.writeFTDI(mask, 1000);
                result = true;
            } catch (IOException io) {
                outputTost("io init");
            }
        } else {
            outputTost("Open connection, please!");
        }
        return result;
    }

    public void onClickStop() {
        if (connectorFTDI != null) {
            try {
                connectorFTDI.close();
                buttonSelect.setEnabled(false);
            } catch (IOException io) {
                outputTost("already exit");
            }
        } else {
            outputTost("Connection closed yet!");
        }
    }

    public void outputTost(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

//---------------- Принятие данных , все методы :

    public boolean justSendMessage() {
        boolean result = false;
        textViewSelectMode = getActivity().findViewById(R.id.data_operation);
        if (textViewSelectMode != null && textViewSelectMode.length() > 0) {
            result = onClickSend(textViewSelectMode.getText().toString());
        } else {
            outputTost("Enter text, please!");
        }
        return result;
    }

    public void sendMessageAndGetAnswer() {
        try {
            byte[] mask = new byte[1];
            mask[0] = (byte) "1".charAt(0);
            connectorFTDI.writeFTDI(mask, 1000);
            acceptStr.setText(getBufferToStr());
        } catch (IOException io) {
            outputTost("io init");
        }
    }

    public String getBufferToStr() {
        int answer = 0;
        StringBuilder result = new StringBuilder();
        result.append(" ");
        try {
            answer = connectorFTDI.readFTDI(bufferFTDI, 30);
        } catch (IOException io) {
            outputTost("io init");
        }
        for (int i = 0; i < answer; i++) {
            result.append(String.format("%02X",bufferFTDI[i])).append(" ");
        }
        bufferFTDI = new byte[14];
        return result.toString();
    }

}

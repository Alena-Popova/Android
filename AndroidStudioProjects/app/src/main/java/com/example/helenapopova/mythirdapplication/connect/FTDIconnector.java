package com.example.helenapopova.mythirdapplication.connect;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.widget.Toast;

import com.example.helenapopova.mythirdapplication.util.FtdiSerialDriver;
import com.example.helenapopova.mythirdapplication.util.UsbSerialPort;
import com.felhr.usbserial.UsbSerialInterface;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;


import lombok.Getter;

public class FTDIconnector {

    private UsbManager mUsbManager;
    private UsbDevice mDevice;
    private FtdiSerialDriver driver;
    private UsbDeviceConnection connection;
    public final String ACTION_USB_PERMISSION = "com.hariharan.arduinousb.USB_PERMISSION";

    private final Context context;
    private UsbSerialPort usbSerialPort;

    public FTDIconnector(Context forFTDI) {
        this.context = forFTDI;
    }

    public void init() throws IOException {

        mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> usbDevices = mUsbManager.getDeviceList();
        Iterator<UsbDevice> iterator = usbDevices.values().iterator();
        if (!usbDevices.isEmpty()) {
            if (iterator.hasNext()) {
                mDevice = iterator.next();
                PendingIntent pi = PendingIntent.getBroadcast(context, 0,
                        new Intent(ACTION_USB_PERMISSION), 0);
                mUsbManager.requestPermission(mDevice, pi);
                setDevice();
            }
        }
    }

    public int readFTDI(byte[] dest, int timeoutMillis) throws IOException {
        return usbSerialPort.read(dest, timeoutMillis);
    }

    public void writeFTDI(byte[] operation, int timeoutMillis) throws IOException {
        usbSerialPort.write(operation, timeoutMillis);
    }


    private void setDevice() throws IOException {
        if (mDevice != null) {
            driver = new FtdiSerialDriver(mDevice);
            if ( driver != null && !driver.getPorts().isEmpty()) {
                connection = mUsbManager.openDevice(mDevice);
                usbSerialPort = driver.getPorts().get(0);
                usbSerialPort.open(connection);
                usbSerialPort.setParameters(19200, UsbSerialInterface.DATA_BITS_8, UsbSerialInterface.STOP_BITS_1, UsbSerialInterface.PARITY_NONE);
            } else  {
                outputTost("No ports");
            }
        } else {
            outputTost("Dont find device");
        }
    }

    public void close() throws IOException {
       if (usbSerialPort != null) {
           usbSerialPort.close();
       }
    }


    public void outputTost(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}


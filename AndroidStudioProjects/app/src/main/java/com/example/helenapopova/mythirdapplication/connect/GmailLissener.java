package com.example.helenapopova.mythirdapplication.connect;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.widget.Toast;

public class GmailLissener {
    private final Context context;

    public GmailLissener(Context srcContext) {
        context = srcContext;
    }

    public void sendMessageToEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "popova-alena1995@mail.ru" });
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "your email");
        emailIntent.putExtra(Intent.EXTRA_TEXT,
                Html.fromHtml("<b>your body message, please</b>"));
        emailIntent.setType("application/octet-stream");
        context.startActivity(Intent.createChooser(emailIntent, "Send Email"));
    }
}

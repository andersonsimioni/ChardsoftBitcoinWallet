package com.example.chardsoftcryptowallet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import javax.annotation.Nullable;

public class MessageBox {
    private final String Title;
    private final String Message;
    private final View view;
    private final boolean ShowCancelBtn;

    public void showMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.setCancelable(true);

        if(ShowCancelBtn) {
            builder.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        }

        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showMessage(@Nullable Runnable onOkAction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.setCancelable(true);

        if(ShowCancelBtn) {
            builder.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        }

        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if(onOkAction!=null)
                            onOkAction.run();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showMessage(@Nullable Runnable onOkAction, @Nullable Runnable onCancelAction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.setCancelable(true);

        if(ShowCancelBtn) {
            builder.setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            if (onCancelAction != null)
                                onCancelAction.run();
                        }
                    });
        }

        builder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if(onOkAction!=null)
                            onOkAction.run();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public MessageBox(String title, String message, View view) {
        this.Title = title;
        this.Message = message;
        this.view = view;
        this.ShowCancelBtn = false;
    }

    public MessageBox(String title, String message, View view, boolean showCancelBtn) {
        this.Title = title;
        this.Message = message;
        this.view = view;
        this.ShowCancelBtn = showCancelBtn;
    }
}

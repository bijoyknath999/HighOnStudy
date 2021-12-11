package highonstudy.com.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import highonstudy.com.R;


public class DialogUtils {

    public static void showDialogPrompt(Activity activity, String title, String message, String positiveButtonText, String negativeButtonText, boolean isCancellable, final DialogActionListener dialogActionListener) {
        showDialog(activity, title, message, positiveButtonText, negativeButtonText, isCancellable, dialogActionListener);
    }


    private static void showDialog(Activity activity, String title, String message, String positiveButtonText, String negativeButtonText, boolean isCancellable, final DialogActionListener dialogActionListener) {
        if (activity != null) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity, R.style.AlertDialogTheme);
            if (title != null) {
                alertDialogBuilder.setTitle(title);
            }
            alertDialogBuilder.setMessage(message);
            alertDialogBuilder.setCancelable(isCancellable);

            if (dialogActionListener != null) {
                alertDialogBuilder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        dialogActionListener.onPositiveClick();
                    }
                });
            }

            if (dialogActionListener != null) {
                alertDialogBuilder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
            }

            alertDialogBuilder.create().show();
        }
    }


    public interface DialogActionListener {
        void onPositiveClick();
    }

}

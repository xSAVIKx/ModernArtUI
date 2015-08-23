package org.moma.modernartui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/**
 * Created by Iurii Sergiichuk on 23.08.2015.
 */
public class MoreInformationFragment extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Uri uri = Uri.parse(getResources().getString(R.string.url_to_visit));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.more_information_dialog_text).setPositiveButton(R.string.visit_moma_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                getActivity().startActivity(intent);
            }
        }).setNegativeButton(R.string.not_now_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        }).setTitle(R.string.more_information_dialog_title);
        return builder.create();
    }
}

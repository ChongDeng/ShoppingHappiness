package fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chongdeng.jufeng.shoppinghappiness.R;

/**
 * Created by fqyang on 10/31/2017.
 */

public class UploadProgressDialogFragment extends DialogFragment {
    ProgressBar progressBar;
    TextView ProgressBarTv;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_upload_progress_dialog, null);

        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);

        ProgressBarTv = (TextView) v.findViewById(R.id.ProgressBarTv);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Upload")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),"you clicked yes", Toast.LENGTH_SHORT).show();
                    }})
                .create();
    }

    public void UpdateProgress(int percent){
        if(percent <= 100){
            progressBar.setProgress(percent);
            ProgressBarTv.setText(percent + "%");
        }
    }
}
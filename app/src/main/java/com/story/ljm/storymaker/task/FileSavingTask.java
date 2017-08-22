package com.story.ljm.storymaker.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.story.ljm.storymaker.R;
import com.story.ljm.storymaker.manager.FileManager;
import com.story.ljm.storymaker.util.GeneralDialog;

/**
 * Created by ljm on 2017-08-22.
 */

public class FileSavingTask extends AsyncTask<Void, Void, Boolean> {
    private GeneralDialog waiting_dialog;
    private Activity myActivity;
    private String content;
    private FragmentTransaction ft;

    public FileSavingTask(Activity activity, String content, FragmentTransaction ft) {
        myActivity = activity;
        this.content = content.replace("\n","\r\n");
        this.ft = ft;
    }

    @Override
    protected void onPreExecute() {
        waiting_dialog = new GeneralDialog();
        Bundle args = new Bundle();
        args.putInt("mode", GeneralDialog.MODE_TEXT_PROGRESS_BAR_DIALOG);
        args.putString("msg", myActivity.getString(R.string.text_saving));
        waiting_dialog.setArguments(args);
        waiting_dialog.show(ft, null);

    }

    @Override
    protected Boolean doInBackground(Void... params) {
        FileManager fileMgr = new FileManager();
        return fileMgr.saveFile(content);
    }

    @Override
    protected void onPostExecute(Boolean completed) {
        if(completed){
            waiting_dialog.dismiss();
            Toast.makeText(myActivity, myActivity.getString(R.string.text_save_complete), Toast.LENGTH_LONG).show();
        }
    }
}

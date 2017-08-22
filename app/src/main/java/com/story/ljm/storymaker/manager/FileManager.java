package com.story.ljm.storymaker.manager;

import android.os.Environment;

import com.story.ljm.storymaker.abstraction.IFileManager;
import com.story.ljm.storymaker.util.GeneralUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ljm on 2017-08-22.
 */

public class FileManager implements IFileManager{
    private static final String FILE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/StoryMaker";

    @Override
    public boolean saveFile(String content) {
        File file = new File(FILE_DIR);

        if(!file.exists()){
            file.mkdirs();
        }

        FileOutputStream fos;

        try {
            fos = new FileOutputStream(FILE_DIR + "/" + GeneralUtil.getDateTime() + ".txt");
            fos.write(content.getBytes());
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void loadFile() {

    }
}

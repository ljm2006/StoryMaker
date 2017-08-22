package com.story.ljm.storymaker.abstraction;

/**
 * Created by ljm on 2017-08-22.
 */

public interface IFileManager {
    boolean saveFile(String content);
    void loadFile();
}

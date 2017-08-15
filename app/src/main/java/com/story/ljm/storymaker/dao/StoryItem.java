package com.story.ljm.storymaker.dao;

/**
 * Created by ljm on 2017-05-15.
 */

public class StoryItem {
    private int storyID;
    private String title;
    private String content;

    public int getStoryID() {
        return storyID;
    }

    public void setStoryID(int storyID) {
        this.storyID = storyID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

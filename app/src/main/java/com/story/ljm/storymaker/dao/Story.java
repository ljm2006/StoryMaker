package com.story.ljm.storymaker.dao;

import com.story.ljm.storymaker.abstraction.StoryObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ljm on 2017-05-15.
 */

public class Story implements StoryObject {
    private ArrayList<StoryItem> storyItemList;
    private int storyID;
    private boolean hasData = false;

    @Override
    public void parseData(String data) {
        if(!data.equals("")){
            try {
                JSONObject obj = new JSONObject(data);
                JSONArray arr = obj.getJSONArray("StoryList");
                ArrayList<StoryItem> temp_list = new ArrayList<StoryItem>();
                for(int i = 0; i< arr.length(); i++){
                    JSONObject temp_obj = arr.getJSONObject(i);
                    StoryItem item = new StoryItem();
                    item.setStoryID(temp_obj.getInt("storyID"));
                    item.setTitle(temp_obj.getString("title"));
                    item.setContent(temp_obj.getString("content"));
                    temp_list.add(item);
                }

                setStoryItemList(temp_list);
                hasData = true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }else{
            hasData = false;
        }

    }

    @Override
    public void parseListData(String data) {

    }

    public ArrayList<StoryItem> getStoryItemList() {
        return storyItemList;
    }

    public void setStoryItemList(ArrayList<StoryItem> storyItemList) {
        this.storyItemList = storyItemList;
    }

    public int getStoryID() {
        return storyID;
    }

    public void setStoryID(int storyID) {
        this.storyID = storyID;
    }

    public boolean confirmData() {
        return hasData;
    }
}

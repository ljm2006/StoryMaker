package com.story.ljm.storymaker.dao;

import com.story.ljm.storymaker.abstraction.StoryObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ljm on 2017-06-14.
 */

public class Place implements StoryObject {
    private ArrayList<PlaceItem> placeItemList;
    @Override
    public void parseData(String data) {
        if(!data.equals("")){
            try {
                JSONObject obj = new JSONObject(data);
                JSONArray arr = obj.getJSONArray("Place");
                ArrayList<PlaceItem> tempList = new ArrayList<PlaceItem>();
                for(int i = 0; i< arr.length() ; i++){
                    JSONObject tempObj = arr.getJSONObject(i);
                    PlaceItem item = new PlaceItem();
                    item.setResID(tempObj.getInt("resID"));
                    item.setResName(tempObj.getString("resName"));
                    item.setPriority(tempObj.getInt("priority"));
                    item.setComment(tempObj.getString("comment"));

                    tempList.add(item);
                }

                setPlaceItemList(tempList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void parseListData(String data) {

    }

    public ArrayList<PlaceItem> getPlaceItemList() {
        return placeItemList;
    }

    public void setPlaceItemList(ArrayList<PlaceItem> placeItemList) {
        this.placeItemList = placeItemList;
    }
}

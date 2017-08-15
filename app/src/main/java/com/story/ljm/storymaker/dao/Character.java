package com.story.ljm.storymaker.dao;

import com.story.ljm.storymaker.abstraction.StoryObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ljm on 2017-06-14.
 */

public class Character implements StoryObject{
    private ArrayList<CharacterItem> characterItemList;

    @Override
    public void parseData(String data) {
        if(!data.equals("")){
            try {
                JSONObject obj = new JSONObject(data);
                JSONArray arr = obj.getJSONArray("Character");
                ArrayList<CharacterItem> tempList = new ArrayList<CharacterItem>();
                for(int i = 0; i< arr.length() ; i++){
                    JSONObject tempObj = arr.getJSONObject(i);
                    CharacterItem item = new CharacterItem();
                    item.setResID(tempObj.getInt("resID"));
                    item.setResName(tempObj.getString("resName"));
                    item.setPriority(tempObj.getInt("priority"));
                    item.setComment(tempObj.getString("comment"));

                    tempList.add(item);
                }

                setCharacterItemList(tempList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void parseListData(String data) {

    }

    public ArrayList<CharacterItem> getCharacterItemList() {
        return characterItemList;
    }

    public void setCharacterItemList(ArrayList<CharacterItem> characterItemList) {
        this.characterItemList = characterItemList;
    }
}

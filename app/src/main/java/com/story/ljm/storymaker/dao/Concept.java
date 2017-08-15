package com.story.ljm.storymaker.dao;

import com.story.ljm.storymaker.abstraction.StoryObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ljm on 2017-06-14.
 */

public class Concept implements StoryObject {
    private ArrayList<ConceptItem> conceptItemList;
    @Override
    public void parseData(String data) {
        if(!data.equals("")){
            try {
                JSONObject obj = new JSONObject(data);
                JSONArray arr = obj.getJSONArray("Concept");
                ArrayList<ConceptItem> tempList = new ArrayList<ConceptItem>();
                for(int i = 0; i< arr.length() ; i++){
                    JSONObject tempObj = arr.getJSONObject(i);
                    ConceptItem item = new ConceptItem();
                    item.setResID(tempObj.getInt("resID"));
                    item.setResName(tempObj.getString("resName"));
                    item.setPriority(tempObj.getInt("priority"));
                    item.setComment(tempObj.getString("comment"));

                    tempList.add(item);
                }
                setConceptItemList(tempList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void parseListData(String data) {

    }

    public ArrayList<ConceptItem> getConceptItemList() {
        return conceptItemList;
    }

    public void setConceptItemList(ArrayList<ConceptItem> conceptItemList) {
        this.conceptItemList = conceptItemList;
    }
}

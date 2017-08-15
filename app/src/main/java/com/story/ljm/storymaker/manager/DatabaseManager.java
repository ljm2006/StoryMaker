package com.story.ljm.storymaker.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.story.ljm.storymaker.abstraction.IDatabaseManager;
import com.story.ljm.storymaker.loader.DataLoader;
import com.story.ljm.storymaker.util.Constant;
import com.story.ljm.storymaker.util.GeneralUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ljm on 2017-05-15.
 */

public class DatabaseManager implements IDatabaseManager {
    private static DatabaseManager manager;
    private SQLiteDatabase database;
    private Context mContext;

    private DatabaseManager(){

    }

    public static DatabaseManager getInstance(){
        if(manager == null){
            manager = new DatabaseManager();
        }

        return manager;
    }

    public void setContext(Context context){
        mContext = context;
    }

    @Override
    public void openDB() {
        database = mContext.openOrCreateDatabase(Constant.NAME_DB, Context.MODE_PRIVATE, null);

        if(GeneralUtil.IS_DEBUG_MODE)
            GeneralUtil.Log("DB가 생성되었거나 열렸음.");
    }

    @Override
    public void createStoryListTable() {
        database.execSQL(
                "CREATE TABLE IF NOT EXISTS " + Constant.NAME_TABLE_STORY_LIST + "(" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "storyID INTEGER, " +
                        "title TEXT, " +
                        "content MEDIUMTEXT);"

        );
    }

    @Override
    public void createCharacterResourceTable(int storyID) {
        database.execSQL(
                "CREATE TABLE IF NOT EXISTS " + Constant.NAME_TABLE_CHARACTER + Integer.toString(storyID) + "(" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "resID INTEGER, " +
                        "resName TEXT, " +
                        "priority INTEGER, " +
                        "comment TEXT);"

        );
    }

    @Override
    public void createPlaceResourceTable(int storyID) {
        database.execSQL(
                "CREATE TABLE IF NOT EXISTS " + Constant.NAME_TABLE_PLACE + Integer.toString(storyID) + "(" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "resID INTEGER, " +
                        "resName TEXT, " +
                        "priority INTEGER, " +
                        "comment TEXT);"

        );
    }

    @Override
    public void createConceptResourceTable(int storyID) {
        database.execSQL(
                "CREATE TABLE IF NOT EXISTS " + Constant.NAME_TABLE_CONCEPT + Integer.toString(storyID) + "(" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "resID INTEGER, " +
                        "resName TEXT, " +
                        "priority INTEGER, " +
                        "comment TEXT);"

        );
    }

    @Override
    public void insertStoryListTableData(int story_id, String title, String content) {
        ContentValues values = new ContentValues();
        values.put("storyID", story_id);
        values.put("title", title);
        values.put("content", content);
        database.insert(Constant.NAME_TABLE_STORY_LIST, null, values);
    }

    @Override
    public void insertCharacterResourceData(int storyID, int res_id, String resName, int priority, String comment) {
        ContentValues values = new ContentValues();
        values.put("resID", res_id);
        values.put("resName", resName);
        values.put("priority", priority);
        values.put("comment", comment);
        database.insert(Constant.NAME_TABLE_CHARACTER + Integer.toString(storyID), null, values);
        DataLoader.getInstance().loadCharacterData(storyID);
    }

    @Override
    public void insertPlaceResourceData(int storyID, int res_id, String resName, int priority, String comment) {
        ContentValues values = new ContentValues();
        values.put("resID", res_id);
        values.put("resName", resName);
        values.put("priority", priority);
        values.put("comment", comment);
        database.insert(Constant.NAME_TABLE_PLACE + Integer.toString(storyID) , null, values);
        DataLoader.getInstance().loadPlaceData(storyID);
    }

    @Override
    public void insertConceptResourceData(int storyID, int res_id, String resName, int priority, String comment) {
        ContentValues values = new ContentValues();
        values.put("resID", res_id);
        values.put("resName", resName);
        values.put("priority", priority);
        values.put("comment", comment);
        database.insert(Constant.NAME_TABLE_CONCEPT + Integer.toString(storyID), null, values);
        DataLoader.getInstance().loadConceptData(storyID);
    }


    @Override
    public void deleteStoryListTableData(int story_id) {
        database.delete(Constant.NAME_TABLE_STORY_LIST, "storyID = ?", new String[]{Integer.toString(story_id)});
        database.execSQL("DROP TABLE " + (Constant.NAME_TABLE_CHARACTER + Integer.toString(story_id)));
        database.execSQL("DROP TABLE " + (Constant.NAME_TABLE_PLACE + Integer.toString(story_id)));
        database.execSQL("DROP TABLE " + (Constant.NAME_TABLE_CONCEPT + Integer.toString(story_id)));
    }

    @Override
    public void deleteCharacterResourceData(int storyID, int resID) {
        database.delete(Constant.NAME_TABLE_CHARACTER + Integer.toString(storyID), "resID = ?", new String[]{Integer.toString(resID)});
        DataLoader.getInstance().loadCharacterData(storyID);
    }

    @Override
    public void deletePlaceResourceData(int storyID, int resID) {
        database.delete(Constant.NAME_TABLE_PLACE + Integer.toString(storyID), "resID = ?", new String[]{Integer.toString(resID)});
        DataLoader.getInstance().loadPlaceData(storyID);
    }

    @Override
    public void deleteConceptResourceData(int storyID, int resID) {
        database.delete(Constant.NAME_TABLE_CONCEPT + Integer.toString(storyID), "resID = ?", new String[]{Integer.toString(resID)});
        DataLoader.getInstance().loadConceptData(storyID);
    }


    @Override
    public void updateStoryListTableData(int story_id, String title, String content) {
        GeneralUtil.Log("업데이트되는 story의 id : " + Integer.toString(story_id));
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        int count = database.update(Constant.NAME_TABLE_STORY_LIST, values, "storyID = ?", new String[]{Integer.toString(story_id)});
        GeneralUtil.Log("업데이트 됨 :: " + count);
    }

    @Override
    public void updateCharacterResourceData(int storyID, int resID, String resName, int priority, String comment) {
        ContentValues values = new ContentValues();
        values.put("resName", resName);
        values.put("priority", priority);
        values.put("comment", comment);
        database.update(Constant.NAME_TABLE_CHARACTER + Integer.toString(storyID), values, "resID = ?", new String[]{Integer.toString(resID)});
        DataLoader.getInstance().loadCharacterData(storyID);
    }

    @Override
    public void updatePlaceResourceData(int storyID, int resID, String resName, int priority, String comment) {
        ContentValues values = new ContentValues();
        values.put("resName", resName);
        values.put("priority", priority);
        values.put("comment", comment);
        database.update(Constant.NAME_TABLE_PLACE + Integer.toString(storyID), values, "resID = ?", new String[]{Integer.toString(resID)});
        DataLoader.getInstance().loadPlaceData(storyID);
    }

    @Override
    public void updateConceptResourceData(int storyID, int resID, String resName, int priority, String comment) {
        ContentValues values = new ContentValues();
        values.put("resName", resName);
        values.put("priority", priority);
        values.put("comment", comment);
        database.update(Constant.NAME_TABLE_CONCEPT + Integer.toString(storyID), values, "resID = ?", new String[]{Integer.toString(resID)});
        DataLoader.getInstance().loadConceptData(storyID);
    }

    @Override
    public String getQueryResult() {
        String[] columns = new String[]{"_id","storyID","title","content"};
        Cursor c = database.query(Constant.NAME_TABLE_STORY_LIST, columns, null, null, null, null, "_id DESC");
        JSONObject jsonObj_storyList = new JSONObject();
        JSONArray jsonArray_storyList = new JSONArray();
        try {
            if(c != null){
                while(c.moveToNext()){
                    int storyID = c.getInt(1);
                    String title = c.getString(2);
                    String content = c.getString(3);
                    JSONObject obj = new JSONObject();

                    obj.put("storyID", storyID);
                    obj.put("title", title);
                    obj.put("content", content);
                    jsonArray_storyList.put(obj);
                }
                c.close();
                jsonObj_storyList.put("StoryList", jsonArray_storyList);
            }else{
                return "";
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        if(GeneralUtil.IS_DEBUG_MODE)
            GeneralUtil.Log("쿼리된 결과 : " + jsonObj_storyList.toString());

        return jsonObj_storyList.toString();
    }

    @Override
    public String getResourceQuery(String tableName, int story_id) {
        String[] columns = new String[]{"_id","resID","resName","priority","comment"};

        Cursor c;
        if(tableName.equals(Constant.NAME_TABLE_CHARACTER)){
            c = database.query(Constant.NAME_TABLE_CHARACTER + Integer.toString(story_id), columns, null, null, null, null, "priority DESC");
        }else if(tableName.equals(Constant.NAME_TABLE_PLACE)){
            c = database.query(Constant.NAME_TABLE_PLACE + Integer.toString(story_id), columns, null, null, null, null, "priority DESC");
        }else if(tableName.equals(Constant.NAME_TABLE_CONCEPT)){
            c = database.query(Constant.NAME_TABLE_CONCEPT + Integer.toString(story_id), columns, null, null, null, null, "priority DESC");
        }else{
            c = null;
        }

        JSONObject jsonObj_resList = new JSONObject();
        JSONArray jsonArray_resList = new JSONArray();
        try {
            if(c != null){
                while(c.moveToNext()){
                    int resID = c.getInt(1);
                    String resName = c.getString(2);
                    int priority = c.getInt(3);
                    String comment = c.getString(4);

                    JSONObject obj = new JSONObject();

                    obj.put("resID", resID);
                    obj.put("resName", resName);
                    obj.put("priority", priority);
                    obj.put("comment", comment);
                    jsonArray_resList.put(obj);
                }
                c.close();

                if(tableName.equals(Constant.NAME_TABLE_CHARACTER)){
                    jsonObj_resList.put("Character", jsonArray_resList);
                }else if(tableName.equals(Constant.NAME_TABLE_PLACE)){
                    jsonObj_resList.put("Place", jsonArray_resList);
                }else{
                    jsonObj_resList.put("Concept", jsonArray_resList);
                }

            }else{
                return "";
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        if(GeneralUtil.IS_DEBUG_MODE)
            GeneralUtil.Log("쿼리된 결과 : " + jsonObj_resList.toString());

        return jsonObj_resList.toString();
    }

    @Override
    public void copyResourceData(int pastedStoryID, int copiedStoryID) {
        database.beginTransaction();
        String copiedCharacterData = getResourceQuery(Constant.NAME_TABLE_CHARACTER, copiedStoryID);
        String copiedPlaceData = getResourceQuery(Constant.NAME_TABLE_PLACE, copiedStoryID);
        String copiedConceptData = getResourceQuery(Constant.NAME_TABLE_CONCEPT, copiedStoryID);

        try {
            JSONObject obj_char = new JSONObject(copiedCharacterData);
            JSONArray arr_char = obj_char.getJSONArray("Character");
            if(!arr_char.toString().equals("")){
                for(int i = 0; i< arr_char.length(); i++){
                    JSONObject tempObj = arr_char.getJSONObject(i);
                    insertCharacterResourceData(pastedStoryID, GeneralUtil.makeRandomInteger(), tempObj.getString("resName"), tempObj.getInt("priority"), tempObj.getString("comment"));
                }
            }


            JSONObject obj_place = new JSONObject(copiedPlaceData);
            JSONArray arr_place = obj_place.getJSONArray("Place");
            if(!arr_place.toString().equals("")){
                for(int i = 0; i< arr_place.length(); i++){
                    JSONObject tempObj = arr_place.getJSONObject(i);
                    insertPlaceResourceData(pastedStoryID, GeneralUtil.makeRandomInteger(), tempObj.getString("resName"), tempObj.getInt("priority"), tempObj.getString("comment"));
                }
            }



            JSONObject obj_concept = new JSONObject(copiedConceptData);
            JSONArray arr_concept = obj_concept.getJSONArray("Concept");
            if(!arr_concept.toString().equals("")){
                for(int i = 0; i< arr_concept.length(); i++){
                    JSONObject tempObj = arr_concept.getJSONObject(i);
                    insertConceptResourceData(pastedStoryID, GeneralUtil.makeRandomInteger(), tempObj.getString("resName"), tempObj.getInt("priority"), tempObj.getString("comment"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        database.setTransactionSuccessful();
        database.endTransaction();
        //database.execSQL("INSERT INTO " + Constant.NAME_TABLE_CHARACTER + Integer.toString(pastedStoryID) + " SELECT resID,resName,priority,comment FROM " + Constant.NAME_TABLE_CHARACTER + Integer.toString(copiedStoryID));
        DataLoader.getInstance().loadCharacterData(pastedStoryID);
        DataLoader.getInstance().loadPlaceData(pastedStoryID);
        DataLoader.getInstance().loadConceptData(pastedStoryID);

    }
}

package com.story.ljm.storymaker;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.story.ljm.storymaker.fragment.StoryListFragment;
import com.story.ljm.storymaker.fragment.StoryWritingFragment;
import com.story.ljm.storymaker.loader.DataLoader;
import com.story.ljm.storymaker.manager.DatabaseManager;
import com.story.ljm.storymaker.util.GeneralUtil;

public class MasterActivity extends AppCompatActivity{
    private StoryListFragment fragment_story_list;
    private DatabaseManager mDBManager;
    private OnBackTouchListener callback_back_touch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        checkDatabaseCondition();
        openListFragment();
    }

    private void checkDatabaseCondition(){
        mDBManager = DatabaseManager.getInstance();
        mDBManager.setContext(this);
        mDBManager.openDB();
        mDBManager.createStoryListTable();
        //mDBManager.createStoryResourceTable();
        DataLoader.getInstance().loadData();
    }

    public void openListFragment(){
        fragment_story_list = new StoryListFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_story_list, fragment_story_list);
        ft.commit();
    }

    public void openMakingFragment(){
        StoryWritingFragment fragment = new StoryWritingFragment();
        int storyID = GeneralUtil.makeRandomInteger();

        DatabaseManager.getInstance().insertStoryListTableData(storyID, "", "");


        Bundle data = new Bundle();
        data.putInt("storyID", storyID);
        data.putString("title", "");
        data.putString("content", "");

        fragment.setArguments(data);

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.fragment_story_list, fragment).addToBackStack("master").commit();
    }

    public void setOnBackTouchedListener(OnBackTouchListener listener){
        callback_back_touch = listener;
    }

    @Override
    public void onBackPressed() {
        if(callback_back_touch != null){
            callback_back_touch.onBackTouched();
        }else{
            super.onBackPressed();
        }

    }

    public interface OnBackTouchListener{
        void onBackTouched();
    }
}

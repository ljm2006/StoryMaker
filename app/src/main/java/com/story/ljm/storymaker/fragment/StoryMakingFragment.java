package com.story.ljm.storymaker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.story.ljm.storymaker.R;
import com.story.ljm.storymaker.loader.DataLoader;
import com.story.ljm.storymaker.manager.DatabaseManager;
import com.story.ljm.storymaker.util.GeneralUtil;

/**
 * Created by ljm on 2017-05-16.
 */

public class StoryMakingFragment extends Fragment {
    private EditText edit_input_title;
    private Button btn_res_setting;
    private Button btn_making_story;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_story_making, container, false);
        edit_input_title = (EditText) v.findViewById(R.id.edit_input_title);
        btn_res_setting = (Button) v.findViewById(R.id.btn_resource_setting);
        btn_making_story = (Button) v.findViewById(R.id.btn_making_story);
        btn_res_setting.setOnClickListener(makingBtnClickListener);
        btn_making_story.setOnClickListener(makingBtnClickListener);

        return v;
    }


    private View.OnClickListener makingBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_resource_setting:{
                    break;
                }
                case R.id.btn_making_story:{
                    if(edit_input_title.getText().toString().equals("")){
                        Toast.makeText(getActivity(), getString(R.string.hint_input_title), Toast.LENGTH_SHORT).show();
                    }else{
                        int storyID = GeneralUtil.makeRandomInteger();
                        DatabaseManager.getInstance().insertStoryListTableData(storyID, edit_input_title.getText().toString(), "");
                        DataLoader.getInstance().loadData();
                        getActivity().onBackPressed();
                    }
                    break;
                }
            }
        }
    };

}

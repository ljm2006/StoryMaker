package com.story.ljm.storymaker.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.story.ljm.storymaker.R;
import com.story.ljm.storymaker.MasterActivity;
import com.story.ljm.storymaker.dao.Story;
import com.story.ljm.storymaker.dao.StoryItem;
import com.story.ljm.storymaker.loader.DataLoader;
import com.story.ljm.storymaker.manager.DatabaseManager;
import com.story.ljm.storymaker.util.GeneralDialog;
import com.story.ljm.storymaker.util.GeneralUtil;

import java.util.ArrayList;

/**
 * Created by ljm on 2017-05-15.
 */

public class StoryListFragment extends Fragment {
    private ListView list_story_list;
    private ImageView img_new_story;
    private ImageView img_more;
    private ImageView img_delete;
    private StoryListAdapter mAdapter;
    private LinearLayout layout_empty_list;
    private ArrayList<StoryItem> itemList = new ArrayList<StoryItem>();
    private ArrayList<Boolean> itemSelectStateList = new ArrayList<Boolean>();
    private ArrayList<Integer> selectedItemIDList = new ArrayList<Integer>();
    private boolean checkMode_activated = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_story_list, container, false);
        list_story_list = (ListView) v.findViewById(R.id.list_story);

        img_new_story = (ImageView) v.findViewById(R.id.img_new_story);
        img_new_story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MasterActivity)getActivity()).openMakingFragment();
            }
        });

        img_delete = (ImageView) v.findViewById(R.id.img_delete);
        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final GeneralDialog popup = new GeneralDialog();
                FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();

                Bundle args = new Bundle();
                args.putInt("mode", GeneralDialog.MODE_TEXT_CONFIRM_CANCEL_POPUP);
                args.putString("msg", getString(R.string.question_delete_story));
                popup.setArguments(args);
                popup.setOnConfirmButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectedItemIDList.size() > 0){
                            for(int i = 0; i < selectedItemIDList.size() ; i++){
                                DatabaseManager.getInstance().deleteStoryListTableData(selectedItemIDList.get(i));
                            }
                        }
                        //파일을 삭제하면 데이터를 다시 쿼리하여 로딩
                        DataLoader.getInstance().loadData();
                        buildItemList();
                        setSelectAll(false);
                        reFreshList();
                        v.setVisibility(View.GONE);
                        checkMode_activated = false;
                        img_new_story.setVisibility(View.VISIBLE);
                        popup.dismiss();
                        Toast.makeText(getActivity(), "삭제 완료", Toast.LENGTH_SHORT).show();
                    }
                });
                popup.setOnCancelButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup.dismiss();
                    }
                });

                popup.show(tr, null);
            }
        });


        img_more = (ImageView) v.findViewById(R.id.img_more);
        img_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popup = new PopupMenu(getActivity(), v);
                if(!checkMode_activated){
                    getActivity().getMenuInflater().inflate(R.menu.menu_more, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getTitle().toString().equals(getString(R.string.text_select))){
                                Toast.makeText(getActivity(), "선택", Toast.LENGTH_LONG).show();
                                checkMode_activated = true;
                                reFreshList();
                                popup.dismiss();
                                return true;
                            }else if(item.getTitle().toString().equals(getString(R.string.text_info))){
                                Toast.makeText(getActivity(), "정보", Toast.LENGTH_LONG).show();
                                checkMode_activated = false;
                                setSelectAll(false);
                                reFreshList();
                                popup.dismiss();
                                return true;
                            }

                            return false;
                        }
                    });
                }else{
                    getActivity().getMenuInflater().inflate(R.menu.menu_more_cancel, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getTitle().toString().equals(getString(R.string.text_select_cancel))){
                                Toast.makeText(getActivity(), "선택 취소", Toast.LENGTH_LONG).show();
                                checkMode_activated = false;
                                setSelectAll(false);
                                reFreshList();
                                popup.dismiss();
                                return true;
                            }
                            return false;
                        }
                    });
                }


                popup.show();
            }
        });
        layout_empty_list = (LinearLayout) v.findViewById(R.id.layout_empty_list);
        buildItemList();

        ((MasterActivity)getActivity()).setOnBackTouchedListener(new MasterActivity.OnBackTouchListener() {
            @Override
            public void onBackTouched() {
                if(checkMode_activated){
                    checkMode_activated = false;
                    reFreshList();
                }else{
                    ((MasterActivity)getActivity()).setOnBackTouchedListener(null);
                    getActivity().onBackPressed();
                }
            }
        });


        return v;
    }

    public void buildItemList(){
        Story story = DataLoader.getInstance().getStory();
        itemList = story.getStoryItemList();
        if(itemList.size() == 0){
            img_more.setEnabled(false);
            img_delete.setVisibility(View.GONE);
            layout_empty_list.setVisibility(View.VISIBLE);
            list_story_list.setVisibility(View.GONE);
        }else{
            for(int i = 0; i< itemList.size(); i++){
                itemSelectStateList.add(false);
            }
            img_more.setEnabled(true);
            layout_empty_list.setVisibility(View.GONE);
            list_story_list.setVisibility(View.VISIBLE);
            mAdapter = new StoryListAdapter();
            list_story_list.setAdapter(mAdapter);
        }
    }

    public void reFreshList(){
        mAdapter.refresh();
    }
    public void setSelectAll(boolean selected){
        for(int i = 0; i< itemSelectStateList.size(); i++){
            itemSelectStateList.set(i, selected);
        }
    }

    private class StoryListAdapter extends BaseAdapter{
        StoryListAdapter(){

        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewHolder viewHolder;
            if(v == null){
                v = inflater.inflate(R.layout.item_story_list,  null);
                viewHolder = new ViewHolder();
                viewHolder.checkBox = (CheckBox) v.findViewById(R.id.checkBox);
                viewHolder.text_title = (TextView) v.findViewById(R.id.text_title);
                viewHolder.img_edit = (ImageView) v.findViewById(R.id.img_edit);
                v.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) v.getTag();
            }
            final StoryItem item = itemList.get(position);
            viewHolder.checkBox.setTag(item.getStoryID());

            //체크박스가 나타나도록 활성화/비활성화 했을 때 리스트의 변화
            if(checkMode_activated){
                img_new_story.setVisibility(View.GONE);
                viewHolder.checkBox.setVisibility(View.VISIBLE);

                //체크박스를 클릭했을 때 리스너
                viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(v.isSelected()){
                            v.setSelected(false);
                            selectedItemIDList.remove(selectedItemIDList.indexOf((Integer)v.getTag()));
                            if(selectedItemIDList.size() == 0){
                                img_delete.setVisibility(View.GONE);
                            }
                            itemSelectStateList.set(position, false);
                        }else{
                            v.setSelected(true);
                            img_delete.setVisibility(View.VISIBLE);
                            selectedItemIDList.add((Integer) v.getTag());
                            itemSelectStateList.set(position, true);
                        }
                    }
                });
                viewHolder.checkBox.setChecked(itemSelectStateList.get(position));
            }else{
                img_new_story.setVisibility(View.VISIBLE);
                img_delete.setVisibility(View.GONE);
                viewHolder.checkBox.setVisibility(View.GONE);
                viewHolder.checkBox.setChecked(false);
                itemSelectStateList.set(position, false);
            }

            makeCheckedState(viewHolder, position, itemSelectStateList.get(position));

            viewHolder.text_title.setText(item.getTitle());
            viewHolder.img_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(GeneralUtil.IS_DEBUG_MODE)
                        GeneralUtil.Log("테스트 데이터 :: " + item.getStoryID());

                    Bundle data = new Bundle();
                    data.putInt("storyID", item.getStoryID());
                    data.putString("title", item.getTitle());
                    data.putString("content", item.getContent());

                    StoryWritingFragment fragment = new StoryWritingFragment();
                    fragment.setArguments(data);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_story_list, fragment).addToBackStack("master").commit();

                }
            });

            return v;
        }

        private void refresh(){
            notifyDataSetChanged();
        }

        private void makeCheckedState(ViewHolder viewHolder, int position, boolean selected){
            itemSelectStateList.set(position, selected);
            viewHolder.checkBox.setChecked(selected);
            viewHolder.checkBox.setSelected(selected);
        }

        private class ViewHolder{
            CheckBox checkBox;
            TextView text_title;
            ImageView img_edit;
        }
    }


}

package com.story.ljm.storymaker.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.story.ljm.storymaker.MasterActivity;
import com.story.ljm.storymaker.R;
import com.story.ljm.storymaker.dao.CharacterItem;
import com.story.ljm.storymaker.dao.ConceptItem;
import com.story.ljm.storymaker.dao.PlaceItem;
import com.story.ljm.storymaker.loader.DataLoader;
import com.story.ljm.storymaker.manager.DatabaseManager;
import com.story.ljm.storymaker.util.Constant;
import com.story.ljm.storymaker.util.GeneralDialog;
import com.story.ljm.storymaker.util.GeneralUtil;

import java.util.ArrayList;

/**
 * Created by ljm on 2017-05-17.
 */

public class StoryWritingFragment extends Fragment {
    private EditText edit_content;
    private EditText edit_title;
    private int storyID;
    private String title;
    private String content;
    private String resInfo = "";

    private ImageView img_human;
    private ImageView img_place;
    private ImageView img_bulb;
    private ImageView img_more;
    private DrawerLayout drawerLayout;
    private LinearLayout drawer;
    private LinearLayout layout_top;
    private LinearLayout layout_bottom;

    private ArrayList<CharacterItem> characterItemList;
    private ArrayList<PlaceItem> placeItemList;
    private ArrayList<ConceptItem> conceptItemList;

    private DrawerListAdapter mAdapter;
    private ListView list_drawer;
    private boolean isKeyBoardOpen = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_story_writing, container, false);
        Bundle args = getArguments();
        storyID = args.getInt("storyID");
        title = args.getString("title");
        content = args.getString("content");

        DataLoader.getInstance().loadResourceData(storyID);//리소스 데이터 로딩.
        characterItemList = DataLoader.getInstance().getCharacter().getCharacterItemList();
        placeItemList = DataLoader.getInstance().getPlace().getPlaceItemList();
        conceptItemList = DataLoader.getInstance().getConcept().getConceptItemList();

        edit_title = (EditText) v.findViewById(R.id.edit_title);
        edit_title.setText(title);
        edit_content = (EditText) v.findViewById(R.id.edit_content);
        edit_content.setText(content);
        edit_content.setSelection(edit_content.length());


        img_human = (ImageView) v.findViewById(R.id.img_human);
        img_place = (ImageView) v.findViewById(R.id.img_place);
        img_bulb = (ImageView) v.findViewById(R.id.img_bulb);
        img_more = (ImageView) v.findViewById(R.id.img_more_bottom);

        img_human.setOnClickListener(bottomIconClickListener);
        img_place.setOnClickListener(bottomIconClickListener);
        img_bulb.setOnClickListener(bottomIconClickListener);
        img_more.setOnClickListener(bottomIconClickListener);

        drawerLayout = (DrawerLayout) v.findViewById(R.id.layout_drawer);
        drawer = (LinearLayout) v.findViewById(R.id.drawer);
        drawer.bringToFront();
        TextView text_btn_add = (TextView) drawer.findViewById(R.id.text_btn_add);
        text_btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final GeneralDialog dialog = new GeneralDialog();
                Bundle args = new Bundle();
                args.putInt("mode", GeneralDialog.MODE_ADD_RES_DIALOG);
                dialog.setArguments(args);
                //다이얼로그의 등록버튼을 눌렀을 때 리스너.
                dialog.setOnRegisterListener(new GeneralDialog.OnRegisterListener() {
                    @Override
                    public void onRegister(int resID, String resName, int priority, String comment) {
                        if(GeneralUtil.IS_DEBUG_MODE) {
                            GeneralUtil.Log("전달된 데이터 : " + resID + ", " + resName + ", " + priority + ", " + comment);
                        }

                        switch (resInfo){
                            case Constant.NAME_TABLE_CHARACTER:{
                                DatabaseManager.getInstance().insertCharacterResourceData(storyID, resID, resName, priority, comment);
                                characterItemList = DataLoader.getInstance().getCharacter().getCharacterItemList();
                                list_drawer.setAdapter(new DrawerListAdapter(Constant.NAME_TABLE_CHARACTER));
                                break;
                            }
                            case Constant.NAME_TABLE_PLACE:{
                                DatabaseManager.getInstance().insertPlaceResourceData(storyID, resID, resName, priority, comment);
                                placeItemList = DataLoader.getInstance().getPlace().getPlaceItemList();
                                list_drawer.setAdapter(new DrawerListAdapter(Constant.NAME_TABLE_PLACE));
                                break;
                            }
                            case Constant.NAME_TABLE_CONCEPT:{
                                DatabaseManager.getInstance().insertConceptResourceData(storyID, resID, resName, priority, comment);
                                conceptItemList = DataLoader.getInstance().getConcept().getConceptItemList();
                                list_drawer.setAdapter(new DrawerListAdapter(Constant.NAME_TABLE_CONCEPT));
                                break;
                            }
                        }

                        dialog.dismiss();
                    }
                });
                dialog.setOnCancelButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                dialog.show(ft, null);
            }
        });

        list_drawer = (ListView) drawer.findViewById(R.id.list_drawer);
        list_drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (resInfo){
                    case Constant.NAME_TABLE_CHARACTER:{
                        String resName = characterItemList.get(position).getResName();
                        addTextToContent(resName);
                        break;
                    }
                    case Constant.NAME_TABLE_PLACE:{
                        String resName = placeItemList.get(position).getResName();
                        addTextToContent(resName);
                        break;
                    }
                    case Constant.NAME_TABLE_CONCEPT:{
                        String resName = conceptItemList.get(position).getResName();
                        addTextToContent(resName);
                        break;
                    }
                }

                drawerLayout.closeDrawers();

            }
        });
        list_drawer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                switch (resInfo){
                    case Constant.NAME_TABLE_CHARACTER:{
                        String resName = characterItemList.get(position).getResName();
                        String comment = characterItemList.get(position).getComment();
                        showResourceDescriptionDialog(resName, comment);
                        break;
                    }
                    case Constant.NAME_TABLE_PLACE:{
                        String resName = placeItemList.get(position).getResName();
                        String comment = placeItemList.get(position).getComment();
                        showResourceDescriptionDialog(resName, comment);
                        break;
                    }
                    case Constant.NAME_TABLE_CONCEPT:{
                        String resName = conceptItemList.get(position).getResName();
                        String comment = conceptItemList.get(position).getComment();
                        showResourceDescriptionDialog(resName, comment);
                        break;
                    }
                }
                return true;
            }
        });
        ((MasterActivity)getActivity()).setOnBackTouchedListener(new MasterActivity.OnBackTouchListener() {
            @Override
            public void onBackTouched() {
                if(drawerLayout.isDrawerOpen(drawer)){
                    drawerLayout.closeDrawers();
                }else{
                    ((MasterActivity)getActivity()).setOnBackTouchedListener(null);
                    getActivity().onBackPressed();
                }
            }
        });

        setListenerToRootView(v);


        return v;
    }

    @Override
    public void onDetach() {
        if(edit_title.getText().toString().equals("") || edit_title.getText().toString().isEmpty()){
            DatabaseManager.getInstance().updateStoryListTableData(storyID, getString(R.string.text_title_not_named), edit_content.getText().toString());
        }else{
            DatabaseManager.getInstance().updateStoryListTableData(storyID, edit_title.getText().toString(), edit_content.getText().toString());
        }

        DataLoader.getInstance().loadData();
        Toast.makeText(getActivity(),getString(R.string.text_save_complete), Toast.LENGTH_LONG).show();
        super.onDetach();
    }

    private void addTextToContent(String text){
        edit_content.getText().insert(edit_content.getSelectionStart(), text);
        edit_content.setSelection(edit_content.getSelectionStart());
        edit_content.requestFocusFromTouch();
    }

    //키보드가 열려있음을 감지하는 메소드
    private void setListenerToRootView(final View v){
        final View activityRootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if(heightDiff > 100){
                    if(!isKeyBoardOpen){
                        layout_top = (LinearLayout) v.findViewById(R.id.layout_top);
                        LinearLayout.LayoutParams params_top = (LinearLayout.LayoutParams) layout_top.getLayoutParams();
                        params_top.weight = 2;
                        layout_top.setLayoutParams(params_top);

                        layout_bottom = (LinearLayout) v.findViewById(R.id.layout_bottom);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout_bottom.getLayoutParams();
                        params.weight = 2;
                        layout_bottom.setLayoutParams(params);
                    }
                    isKeyBoardOpen = true;
                }else if(isKeyBoardOpen){
                    layout_top = (LinearLayout) v.findViewById(R.id.layout_top);
                    LinearLayout.LayoutParams params_top = (LinearLayout.LayoutParams) layout_top.getLayoutParams();
                    params_top.weight = 1;
                    layout_top.setLayoutParams(params_top);

                    layout_bottom = (LinearLayout) v.findViewById(R.id.layout_bottom);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout_bottom.getLayoutParams();
                    params.weight = 1;
                    layout_bottom.setLayoutParams(params);
                    isKeyBoardOpen = false;
                }
            }
        });
    }

    private void showResourceDescriptionDialog(String resName, String comment){
        final GeneralDialog dialog = new GeneralDialog();
        Bundle args = new Bundle();
        args.putInt("mode", GeneralDialog.MODE_RES_DESC_DIALOG);
        args.putString("resName", resName);
        args.putString("comment", comment);
        dialog.setArguments(args);
        dialog.setOnConfirmButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        dialog.show(ft, null);
    }

    private void openResourceDrawer(String _resInfo, String headerText){
        InputMethodManager inputMgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.hideSoftInputFromWindow(edit_content.getWindowToken(), 0);
        TextView text_drawer_header = (TextView) drawer.findViewById(R.id.text_drawer_header);
        text_drawer_header.setText(headerText);
        list_drawer.setAdapter(new DrawerListAdapter(_resInfo));
        resInfo = _resInfo;
        drawerLayout.openDrawer(drawer);
    }

    private void showBringResourceDialog(){
        final GeneralDialog dialog = new GeneralDialog();
        Bundle args = new Bundle();
        args.putInt("mode", GeneralDialog.MODE_HEADER_LIST_CONFIRM_POPUP);
        args.putInt("storyID", storyID);
        args.putString("header", getString(R.string.text_select_story_coping_resource));
        dialog.setArguments(args);
        dialog.setOnListItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int clickedStoryID = DataLoader.getInstance().getStory().getStoryItemList().get(position).getStoryID();
                if(StoryWritingFragment.this.storyID != clickedStoryID){
                    GeneralUtil.Log("클릭된 스토리 ID값 : " + clickedStoryID);
                    DatabaseManager.getInstance().copyResourceData(StoryWritingFragment.this.storyID, clickedStoryID);
                    characterItemList = DataLoader.getInstance().getCharacter().getCharacterItemList();
                    placeItemList = DataLoader.getInstance().getPlace().getPlaceItemList();
                    conceptItemList = DataLoader.getInstance().getConcept().getConceptItemList();
                    dialog.dismiss();
                    Toast.makeText(getActivity(), getString(R.string.text_complete_resource_copy), Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(), getString(R.string.text_wrong_target), Toast.LENGTH_LONG).show();
                }
            }
        });
        dialog.setOnConfirmButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        dialog.show(ft, null);
    }

    private void showSimpleListDialog(){
        final GeneralDialog dialog = new GeneralDialog();
        Bundle args = new Bundle();
        args.putInt("mode", GeneralDialog.MODE_LIST_DIALOG);
        dialog.setArguments(args);
        dialog.setOnSimpleItemClickListener(new GeneralDialog.OnSimpleItemClickListener() {
            @Override
            public void onDoubleQuotationItemClick(View v) {
                dialog.dismiss();
                showInputSentenceDialog();
            }

            @Override
            public void onBringStoryResourceItemClick(View v) {
                dialog.dismiss();
                showBringResourceDialog();
            }

            @Override
            public void onSaveTxtFileItemClick(View v) {

            }
        });
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        dialog.show(ft, null);
    }

    private void showInputSentenceDialog(){
        final GeneralDialog dialog = new GeneralDialog();
        Bundle args = new Bundle();
        args.putInt("mode", GeneralDialog.MODE_INPUT_SENTENCE_DIALOG);
        dialog.setArguments(args);
        dialog.setOnInputListener(new GeneralDialog.OnInputListener() {
            @Override
            public void onInput(String contents) {
                edit_content.append(contents);
                edit_content.setSelection(edit_content.length());
                dialog.dismiss();
            }
        });
        dialog.setOnCancelButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        dialog.show(ft, null);
    }


    //하단바 아이콘 클릭 리스너
    private View.OnClickListener bottomIconClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_human:{
                    if(drawerLayout.isDrawerOpen(drawer)){
                        drawerLayout.closeDrawers();
                    }else{
                        openResourceDrawer(Constant.NAME_TABLE_CHARACTER, getString(R.string.text_character_list));
                    }

                    break;
                }

                case R.id.img_place:{
                    if(drawerLayout.isDrawerOpen(drawer)){
                        drawerLayout.closeDrawers();
                    }else{
                        openResourceDrawer(Constant.NAME_TABLE_PLACE, getString(R.string.text_place_list));
                    }
                    break;
                }

                case R.id.img_bulb:{
                    if(drawerLayout.isDrawerOpen(drawer)){
                        drawerLayout.closeDrawers();
                    }else{
                        openResourceDrawer(Constant.NAME_TABLE_CONCEPT, getString(R.string.text_concept_list));
                    }
                    break;
                }

                case R.id.img_more_bottom:{
                    showSimpleListDialog();
                    break;
                }
            }
        }
    };

    private class DrawerListAdapter extends BaseAdapter{

        private String resInfo;
        DrawerListAdapter(String resInfo){
            this.resInfo = resInfo;
        }
        @Override
        public int getCount() {
            switch (resInfo){
                case Constant.NAME_TABLE_CHARACTER:{
                    return characterItemList.size();
                }
                case Constant.NAME_TABLE_PLACE:{
                    return placeItemList.size();
                }
                case Constant.NAME_TABLE_CONCEPT:{
                    return conceptItemList.size();
                }
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            switch (resInfo){
                case Constant.NAME_TABLE_CHARACTER:{
                    return characterItemList.get(position);
                }
                case Constant.NAME_TABLE_PLACE:{
                    return placeItemList.get(position);
                }
                case Constant.NAME_TABLE_CONCEPT:{
                    return conceptItemList.get(position);
                }
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            ViewHolder holder;
            if(v == null){
                v = getActivity().getLayoutInflater().inflate(R.layout.item_res_drawer_list, null);
                holder = new ViewHolder();
                holder.text_res_name = (TextView) v.findViewById(R.id.text_res_name);
                holder.img_more = (ImageView) v.findViewById(R.id.img_more);
                holder.img_delete = (ImageView) v.findViewById(R.id.img_delete);
                v.setTag(holder);
            }else{
                holder = (ViewHolder) v.getTag();
            }

            applyItemData(holder, position);

            return v;
        }

        private void applyItemData(ViewHolder holder, final int pos){
            switch (resInfo){
                case Constant.NAME_TABLE_CHARACTER:{
                    holder.text_res_name.setText(characterItemList.get(pos).getResName());
                    holder.img_more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final GeneralDialog dialog = new GeneralDialog();
                            Bundle args = new Bundle();
                            args.putInt("mode", GeneralDialog.MODE_EDIT_RES_DIALOG);
                            args.putInt("resID", characterItemList.get(pos).getResID());
                            args.putString("name", characterItemList.get(pos).getResName());
                            args.putString("comment", characterItemList.get(pos).getComment());
                            args.putInt("priority",characterItemList.get(pos).getPriority());
                            dialog.setArguments(args);
                            dialog.setOnRegisterListener(new GeneralDialog.OnRegisterListener() {
                                @Override
                                public void onRegister(int resID, String resName, int priority, String comment) {
                                    DatabaseManager.getInstance().updateCharacterResourceData(storyID, resID, resName, priority, comment);
                                    characterItemList = DataLoader.getInstance().getCharacter().getCharacterItemList();
                                    notifyDataSetChanged();
                                    dialog.dismiss();
                                }
                            });
                            dialog.setOnCancelButtonClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            dialog.show(ft, null);
                        }
                    });
                    holder.img_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final GeneralDialog popup = new GeneralDialog();
                            Bundle args = new Bundle();
                            args.putInt("mode", GeneralDialog.MODE_TEXT_CONFIRM_CANCEL_POPUP);
                            args.putString("msg", String.format(getString(R.string.question_delete_specific_res), characterItemList.get(pos).getResName()));
                            popup.setArguments(args);
                            popup.setOnConfirmButtonClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatabaseManager.getInstance().deleteCharacterResourceData(storyID, characterItemList.get(pos).getResID());
                                    characterItemList = DataLoader.getInstance().getCharacter().getCharacterItemList();
                                    notifyDataSetChanged();
                                    popup.dismiss();
                                }
                            });
                            popup.setOnCancelButtonClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popup.dismiss();
                                }
                            });

                            FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
                            popup.show(tr, null);
                        }
                    });
                    break;
                }
                case Constant.NAME_TABLE_PLACE:{
                    holder.text_res_name.setText(placeItemList.get(pos).getResName());
                    holder.img_more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final GeneralDialog dialog = new GeneralDialog();
                            Bundle args = new Bundle();
                            args.putInt("mode", GeneralDialog.MODE_EDIT_RES_DIALOG);
                            args.putInt("resID", placeItemList.get(pos).getResID());
                            args.putString("name", placeItemList.get(pos).getResName());
                            args.putString("comment", placeItemList.get(pos).getComment());
                            args.putInt("priority",placeItemList.get(pos).getPriority());
                            dialog.setArguments(args);
                            dialog.setOnRegisterListener(new GeneralDialog.OnRegisterListener() {
                                @Override
                                public void onRegister(int resID, String resName, int priority, String comment) {
                                    DatabaseManager.getInstance().updatePlaceResourceData(storyID, resID, resName, priority, comment);
                                    placeItemList = DataLoader.getInstance().getPlace().getPlaceItemList();
                                    notifyDataSetChanged();
                                    dialog.dismiss();
                                }
                            });
                            dialog.setOnCancelButtonClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            dialog.show(ft, null);
                        }
                    });
                    holder.img_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final GeneralDialog popup = new GeneralDialog();
                            Bundle args = new Bundle();
                            args.putInt("mode", GeneralDialog.MODE_TEXT_CONFIRM_CANCEL_POPUP);
                            args.putString("msg", String.format(getString(R.string.question_delete_specific_res), placeItemList.get(pos).getResName()));
                            popup.setArguments(args);
                            popup.setOnConfirmButtonClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatabaseManager.getInstance().deletePlaceResourceData(storyID, placeItemList.get(pos).getResID());
                                    placeItemList = DataLoader.getInstance().getPlace().getPlaceItemList();
                                    notifyDataSetChanged();
                                    popup.dismiss();
                                }
                            });
                            popup.setOnCancelButtonClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popup.dismiss();
                                }
                            });

                            FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
                            popup.show(tr, null);
                        }
                    });
                    break;
                }
                case Constant.NAME_TABLE_CONCEPT:{
                    holder.text_res_name.setText(conceptItemList.get(pos).getResName());
                    holder.img_more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final GeneralDialog dialog = new GeneralDialog();
                            Bundle args = new Bundle();
                            args.putInt("mode", GeneralDialog.MODE_EDIT_RES_DIALOG);
                            args.putInt("resID", conceptItemList.get(pos).getResID());
                            args.putString("name", conceptItemList.get(pos).getResName());
                            args.putString("comment", conceptItemList.get(pos).getComment());
                            args.putInt("priority",conceptItemList.get(pos).getPriority());
                            dialog.setArguments(args);
                            dialog.setOnRegisterListener(new GeneralDialog.OnRegisterListener() {
                                @Override
                                public void onRegister(int resID, String resName, int priority, String comment) {
                                    DatabaseManager.getInstance().updateConceptResourceData(storyID, resID, resName, priority, comment);
                                    conceptItemList = DataLoader.getInstance().getConcept().getConceptItemList();
                                    notifyDataSetChanged();
                                    dialog.dismiss();
                                }
                            });
                            dialog.setOnCancelButtonClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            dialog.show(ft, null);
                        }
                    });
                    holder.img_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final GeneralDialog popup = new GeneralDialog();
                            Bundle args = new Bundle();
                            args.putInt("mode", GeneralDialog.MODE_TEXT_CONFIRM_CANCEL_POPUP);
                            args.putString("msg", String.format(getString(R.string.question_delete_specific_res), conceptItemList.get(pos).getResName()));
                            popup.setArguments(args);
                            popup.setOnConfirmButtonClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatabaseManager.getInstance().deleteConceptResourceData(storyID, conceptItemList.get(pos).getResID());
                                    conceptItemList = DataLoader.getInstance().getConcept().getConceptItemList();
                                    notifyDataSetChanged();
                                    popup.dismiss();
                                }
                            });
                            popup.setOnCancelButtonClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popup.dismiss();
                                }
                            });

                            FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
                            popup.show(tr, null);
                        }
                    });

                    break;
                }
            }
        }

        private class ViewHolder{
            TextView text_res_name;
            ImageView img_more;
            ImageView img_delete;
        }
    }
}

package com.story.ljm.storymaker.util;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.story.ljm.storymaker.R;
import com.story.ljm.storymaker.dao.StoryItem;
import com.story.ljm.storymaker.loader.DataLoader;

import java.util.ArrayList;

/**
 * Created by ljm on 2017-06-06.
 */

public class GeneralDialog extends DialogFragment {

    public static final int MODE_TEXT_CONFIRM_CANCEL_POPUP = 0;
    public static final int MODE_HEADER_LIST_CONFIRM_POPUP = 1;
    public static final int MODE_ADD_RES_DIALOG = 2;
    public static final int MODE_EDIT_RES_DIALOG = 3;
    public static final int MODE_RES_DESC_DIALOG = 4;
    public static final int MODE_LIST_DIALOG = 5;
    public static final int MODE_INPUT_SENTENCE_DIALOG = 6;

    private int mode;
    private int priority = 3;

    private Button btn_confirm;
    private Button btn_cancel;

    private View.OnClickListener confirmBtnListener;
    private View.OnClickListener cancelBtnListener;
    private OnRegisterListener callback_register;
    private AdapterView.OnItemClickListener itemClickListener;
    private OnSimpleItemClickListener callback_simple_item;
    private OnInputListener callback_input;
    /**
     * 다이얼로그 모드별 추가 args값 요구 설명
     * !!!!중요!!!! 반드시 mode args값을 추가해야한다.
    * 1) Text + 확인 취소 버튼 다이얼로그 => "msg" : 다이얼로그에 들어갈 메시지
     * 버튼들의 리스너는 setter로 구현되어있으니 그걸 호출해서 정의하자.
     * //추가로 생각날 때마다 작성하자!
    * */

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Bundle args = getArguments();
        mode = args.getInt("mode", 0);

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);

        switch (mode){
            case MODE_TEXT_CONFIRM_CANCEL_POPUP:{
                dialog.setContentView(R.layout.dialog_text_popup);
                TextView text_msg = (TextView) dialog.findViewById(R.id.text_msg);
                btn_confirm = (Button) dialog.findViewById(R.id.btn_confirm);
                btn_confirm.setOnClickListener(confirmBtnListener);
                btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
                btn_cancel.setOnClickListener(cancelBtnListener);
                String msg = args.getString("msg");
                text_msg.setText(msg);
                break;
            }
            case MODE_HEADER_LIST_CONFIRM_POPUP:{
                dialog.setContentView(R.layout.dialog_general);
                int storyID = args.getInt("storyID");
                TextView text_header = (TextView) dialog.findViewById(R.id.text_dialog_header);
                text_header.setText(args.getString("header"));
                ListView list_dialog = (ListView) dialog.findViewById(R.id.list_general_dialog);
                list_dialog.setAdapter(new StoryListAdapter());
                list_dialog.setOnItemClickListener(itemClickListener);
                btn_confirm = (Button) dialog.findViewById(R.id.btn_confirm);
                btn_confirm.setText(R.string.text_close);
                btn_confirm.setOnClickListener(confirmBtnListener);
                btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
                btn_cancel.setVisibility(View.GONE);
                break;
            }
            case MODE_ADD_RES_DIALOG:{
                dialog.setContentView(R.layout.dialog_add_resource);
                //back키를 안먹게 하기...
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if(keyCode == KeyEvent.KEYCODE_BACK){
                            return true;
                        }
                        return false;
                    }
                });
                final EditText edit_res_name = (EditText) dialog.findViewById(R.id.edit_res_name);
                final EditText edit_res_comment = (EditText) dialog.findViewById(R.id.edit_res_comment);
                final ImageButton imgBtn_star_1 = (ImageButton) dialog.findViewById(R.id.img_btn_star_1);
                final ImageButton imgBtn_star_2 = (ImageButton) dialog.findViewById(R.id.img_btn_star_2);
                final ImageButton imgBtn_star_3 = (ImageButton) dialog.findViewById(R.id.img_btn_star_3);
                final ImageButton imgBtn_star_4 = (ImageButton) dialog.findViewById(R.id.img_btn_star_4);
                final ImageButton imgBtn_star_5 = (ImageButton) dialog.findViewById(R.id.img_btn_star_5);
                imgBtn_star_1.setSelected(true);
                imgBtn_star_2.setSelected(true);
                imgBtn_star_3.setSelected(true);
                imgBtn_star_4.setSelected(false);
                imgBtn_star_5.setSelected(false);

                View.OnClickListener imgBtnListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()){
                            case R.id.img_btn_star_1:{
                                imgBtn_star_1.setSelected(true);
                                imgBtn_star_2.setSelected(false);
                                imgBtn_star_3.setSelected(false);
                                imgBtn_star_4.setSelected(false);
                                imgBtn_star_5.setSelected(false);
                                priority = 1;
                                break;
                            }
                            case R.id.img_btn_star_2:{
                                imgBtn_star_1.setSelected(true);
                                imgBtn_star_2.setSelected(true);
                                imgBtn_star_3.setSelected(false);
                                imgBtn_star_4.setSelected(false);
                                imgBtn_star_5.setSelected(false);
                                priority = 2;
                                break;
                            }
                            case R.id.img_btn_star_3:{
                                imgBtn_star_1.setSelected(true);
                                imgBtn_star_2.setSelected(true);
                                imgBtn_star_3.setSelected(true);
                                imgBtn_star_4.setSelected(false);
                                imgBtn_star_5.setSelected(false);
                                priority = 3;
                                break;
                            }
                            case R.id.img_btn_star_4:{
                                imgBtn_star_1.setSelected(true);
                                imgBtn_star_2.setSelected(true);
                                imgBtn_star_3.setSelected(true);
                                imgBtn_star_4.setSelected(true);
                                imgBtn_star_5.setSelected(false);
                                priority = 4;
                                break;
                            }
                            case R.id.img_btn_star_5:{
                                imgBtn_star_1.setSelected(true);
                                imgBtn_star_2.setSelected(true);
                                imgBtn_star_3.setSelected(true);
                                imgBtn_star_4.setSelected(true);
                                imgBtn_star_5.setSelected(true);
                                priority = 5;
                                break;
                            }
                        }
                    }
                };

                imgBtn_star_1.setOnClickListener(imgBtnListener);
                imgBtn_star_2.setOnClickListener(imgBtnListener);
                imgBtn_star_3.setOnClickListener(imgBtnListener);
                imgBtn_star_4.setOnClickListener(imgBtnListener);
                imgBtn_star_5.setOnClickListener(imgBtnListener);

                Button btn_register = (Button) dialog.findViewById(R.id.btn_register);
                Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
                btn_register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int resID = GeneralUtil.makeRandomInteger();
                        String resName = edit_res_name.getText().toString();
                        String comment = edit_res_comment.getText().toString();
                        if(resName.equals("") || resName.isEmpty()){
                            Toast.makeText(getActivity(), getString(R.string.text_please_input_name), Toast.LENGTH_LONG).show();
                        }else{
                            callback_register.onRegister(resID, resName, priority, comment);
                        }

                    }
                });
                btn_cancel.setOnClickListener(cancelBtnListener);
                break;
            }
            case MODE_EDIT_RES_DIALOG:{
                dialog.setContentView(R.layout.dialog_add_resource);
                final EditText edit_res_name = (EditText) dialog.findViewById(R.id.edit_res_name);
                final EditText edit_res_comment = (EditText) dialog.findViewById(R.id.edit_res_comment);
                edit_res_name.setText(args.getString("name"));
                edit_res_comment.setText(args.getString("comment"));
                final ImageButton imgBtn_star_1 = (ImageButton) dialog.findViewById(R.id.img_btn_star_1);
                final ImageButton imgBtn_star_2 = (ImageButton) dialog.findViewById(R.id.img_btn_star_2);
                final ImageButton imgBtn_star_3 = (ImageButton) dialog.findViewById(R.id.img_btn_star_3);
                final ImageButton imgBtn_star_4 = (ImageButton) dialog.findViewById(R.id.img_btn_star_4);
                final ImageButton imgBtn_star_5 = (ImageButton) dialog.findViewById(R.id.img_btn_star_5);

                int priority_before = args.getInt("priority");
                priority = priority_before;
                switch (priority_before){
                    case 1:{
                        imgBtn_star_1.setSelected(true);
                        imgBtn_star_2.setSelected(false);
                        imgBtn_star_3.setSelected(false);
                        imgBtn_star_4.setSelected(false);
                        imgBtn_star_5.setSelected(false);
                        break;
                    }
                    case 2:{
                        imgBtn_star_1.setSelected(true);
                        imgBtn_star_2.setSelected(true);
                        imgBtn_star_3.setSelected(false);
                        imgBtn_star_4.setSelected(false);
                        imgBtn_star_5.setSelected(false);
                        break;
                    }
                    case 3:{
                        imgBtn_star_1.setSelected(true);
                        imgBtn_star_2.setSelected(true);
                        imgBtn_star_3.setSelected(true);
                        imgBtn_star_4.setSelected(false);
                        imgBtn_star_5.setSelected(false);
                        break;
                    }
                    case 4:{
                        imgBtn_star_1.setSelected(true);
                        imgBtn_star_2.setSelected(true);
                        imgBtn_star_3.setSelected(true);
                        imgBtn_star_4.setSelected(true);
                        imgBtn_star_5.setSelected(false);
                        break;
                    }
                    case 5:{
                        imgBtn_star_1.setSelected(true);
                        imgBtn_star_2.setSelected(true);
                        imgBtn_star_3.setSelected(true);
                        imgBtn_star_4.setSelected(true);
                        imgBtn_star_5.setSelected(true);
                        break;
                    }
                }

                View.OnClickListener imgBtnListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()){
                            case R.id.img_btn_star_1:{
                                imgBtn_star_1.setSelected(true);
                                imgBtn_star_2.setSelected(false);
                                imgBtn_star_3.setSelected(false);
                                imgBtn_star_4.setSelected(false);
                                imgBtn_star_5.setSelected(false);
                                priority = 1;
                                break;
                            }
                            case R.id.img_btn_star_2:{
                                imgBtn_star_1.setSelected(true);
                                imgBtn_star_2.setSelected(true);
                                imgBtn_star_3.setSelected(false);
                                imgBtn_star_4.setSelected(false);
                                imgBtn_star_5.setSelected(false);
                                priority = 2;
                                break;
                            }
                            case R.id.img_btn_star_3:{
                                imgBtn_star_1.setSelected(true);
                                imgBtn_star_2.setSelected(true);
                                imgBtn_star_3.setSelected(true);
                                imgBtn_star_4.setSelected(false);
                                imgBtn_star_5.setSelected(false);
                                priority = 3;
                                break;
                            }
                            case R.id.img_btn_star_4:{
                                imgBtn_star_1.setSelected(true);
                                imgBtn_star_2.setSelected(true);
                                imgBtn_star_3.setSelected(true);
                                imgBtn_star_4.setSelected(true);
                                imgBtn_star_5.setSelected(false);
                                priority = 4;
                                break;
                            }
                            case R.id.img_btn_star_5:{
                                imgBtn_star_1.setSelected(true);
                                imgBtn_star_2.setSelected(true);
                                imgBtn_star_3.setSelected(true);
                                imgBtn_star_4.setSelected(true);
                                imgBtn_star_5.setSelected(true);
                                priority = 5;
                                break;
                            }
                        }
                    }
                };

                imgBtn_star_1.setOnClickListener(imgBtnListener);
                imgBtn_star_2.setOnClickListener(imgBtnListener);
                imgBtn_star_3.setOnClickListener(imgBtnListener);
                imgBtn_star_4.setOnClickListener(imgBtnListener);
                imgBtn_star_5.setOnClickListener(imgBtnListener);

                Button btn_register = (Button) dialog.findViewById(R.id.btn_register);
                Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
                btn_register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int resID = args.getInt("resID");
                        String resName = edit_res_name.getText().toString();
                        String comment = edit_res_comment.getText().toString();
                        if(resName.equals("") || resName.isEmpty()){
                            Toast.makeText(getActivity(), getString(R.string.text_please_input_name), Toast.LENGTH_LONG).show();
                        }else{
                            callback_register.onRegister(resID, resName, priority, comment);
                        }

                    }
                });
                btn_cancel.setOnClickListener(cancelBtnListener);
                break;
            }
            case MODE_RES_DESC_DIALOG:{
                dialog.setContentView(R.layout.dialog_res_desc);
                String resName = args.getString("resName");
                String comment = args.getString("comment");
                TextView text_resName_desc = (TextView) dialog.findViewById(R.id.text_desc_resName);
                TextView text_comment_desc = (TextView) dialog.findViewById(R.id.text_desc_comment);
                Button btn_close = (Button) dialog.findViewById(R.id.btn_close);
                text_resName_desc.setText(resName);
                text_comment_desc.setText(comment);
                btn_close.setOnClickListener(confirmBtnListener);
                break;
            }
            case MODE_LIST_DIALOG:{
                dialog.setContentView(R.layout.dialog_simple_list);
                LinearLayout item_double_quote = (LinearLayout) dialog.findViewById(R.id.item_double_quotation_sentence);
                LinearLayout item_bring_story_res = (LinearLayout) dialog.findViewById(R.id.item_bring_story_res);
                LinearLayout item_save_txt_file = (LinearLayout) dialog.findViewById(R.id.item_save_txt_file);

                View.OnClickListener simple_listClick_listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()){
                            case R.id.item_double_quotation_sentence:{
                                callback_simple_item.onDoubleQuotationItemClick(v);
                                break;
                            }
                            case R.id.item_bring_story_res:{
                                callback_simple_item.onBringStoryResourceItemClick(v);
                                break;
                            }
                            case R.id.item_save_txt_file:{
                                callback_simple_item.onSaveTxtFileItemClick(v);
                                break;
                            }
                        }
                    }
                };
                item_double_quote.setOnClickListener(simple_listClick_listener);
                item_bring_story_res.setOnClickListener(simple_listClick_listener);
                item_save_txt_file.setOnClickListener(simple_listClick_listener);
                break;
            }
            case MODE_INPUT_SENTENCE_DIALOG:{
                dialog.setContentView(R.layout.dialog_input_sentence);
                //back키를 안먹게 하기...
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if(keyCode == KeyEvent.KEYCODE_BACK){
                            return true;
                        }
                        return false;
                    }
                });
                final EditText edit_input_sentence = (EditText) dialog.findViewById(R.id.edit_input_sentence);
                Button btn_input = (Button) dialog.findViewById(R.id.btn_input);
                Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
                btn_input.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback_input.onInput("\n\"" + edit_input_sentence.getText().toString() + "\"\n");
                        dialog.dismiss();
                    }
                });
                btn_cancel.setOnClickListener(cancelBtnListener);
                break;
            }
        }

        return dialog;
    }


    public void setOnConfirmButtonClickListener(View.OnClickListener listener){
        confirmBtnListener = listener;
    }

    public void setOnCancelButtonClickListener(View.OnClickListener listener){
        cancelBtnListener = listener;
    }

    public void setOnRegisterListener(OnRegisterListener listener){
        callback_register = listener;
    }

    public void setOnListItemClickListener(AdapterView.OnItemClickListener listener){
        itemClickListener = listener;
    }

    public void setOnSimpleItemClickListener(OnSimpleItemClickListener listener){
        callback_simple_item = listener;
    }

    public void setOnInputListener(OnInputListener listener){
        callback_input = listener;
    }

    private class StoryListAdapter extends BaseAdapter{
        ArrayList<StoryItem> storyItemList;
        StoryListAdapter(){
            storyItemList = DataLoader.getInstance().getStory().getStoryItemList();
        }

        @Override
        public int getCount() {
            return storyItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return storyItemList.get(position);
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
                v = getActivity().getLayoutInflater().inflate(R.layout.item_simple_list, null);
                holder = new ViewHolder();
                holder.text_title = (TextView) v.findViewById(R.id.text_title);
                v.setTag(holder);
            }else{
                holder = (ViewHolder) v.getTag();
            }
            holder.text_title.setText(storyItemList.get(position).getTitle());
            return v;
        }

        private class ViewHolder{
            TextView text_title;
        }
    }

    public interface OnRegisterListener{
        void onRegister(int resID, String resName, int priority, String comment);
    }

    public interface OnSimpleItemClickListener{
        void onDoubleQuotationItemClick(View v);
        void onBringStoryResourceItemClick(View v);
        void onSaveTxtFileItemClick(View v);
    }

    public interface OnInputListener{
        void onInput(String contents);
    }
}

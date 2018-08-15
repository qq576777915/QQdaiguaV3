package com.king.YH_Fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.king.qqdaigua.MainActivity;
import com.king.qqdaigua.R;
import com.king.util.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 57677 on 2018/6/21.
 */

public class qun_dialog extends DialogFragment {

    private View view;
    private TextView tv_update;
    private Button bt_submit;
    private Button bt_submit1;
    private Button bt_cancel;
    private TextView tv_update_title;
    private String lock;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_qun, null);
        BindViews();
        if (getDialog() != null) {
            getDialog().setTitle("加群说明");
        }
        return view;
    }

    private void BindViews() {
        tv_update_title = (TextView) view.findViewById(R.id.tv_update_title);
        tv_update = (TextView) view.findViewById(R.id.tv_update);
        bt_submit = (Button) view.findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openURL("http://shang.qq.com/wpa/qunwpa?idkey=1af9f014f1d29b7f61e04baa4c986f22f509a944321addb3ce367cbdc1495f51");
            }
        });
        bt_submit1 = (Button) view.findViewById(R.id.bt_submit1);
        bt_submit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText("597047236");
                Toast.makeText(getContext(), "复制群号成功。", Toast.LENGTH_SHORT).show();
            }
        });
        bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });
    }

    private void openURL(String s) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(s);
        intent.setData(content_url);
        startActivity(intent);
    }
}

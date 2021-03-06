package com.king.YH_Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.king.level_Fragment.level_Fragment;
import com.king.qqdaigua.MainActivity;
import com.king.qqdaigua.R;
import com.king.util.DESUtil;
import com.king.util.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * A simple {@link Fragment} subclass.
 */
public class YH_Fragment extends Fragment {

    protected boolean useThemestatusBarColor = true;//是否使用特殊的标题栏背景颜色，android5
    // .0以上可以设置状态栏背景色，如果不使用则使用透明色值
    protected boolean useStatusBarColor = false;//是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6
    // .0以上可以设置

    //判断网络任务是否完成 上限次数
    public static int sign_dialog_cancel = 2;
    private int sign_dialog = 0;

    //记录请求错误此时
    private int error_sign = 0;

    //第一个Fragment 登录成功传过来的 帐号密码 SID值
    private String user, pwd, sid;
    //0电脑管家-1电脑QQ-2手机QQ-3勋章墙-4QQ音乐-5QQ手游-6空间访客-7微视-8微视_浏览
    private String[] status = new String[9];
    //    记录最近一次点击 开关项目ID
    private int switch_sign;
    //  是否连续打开手Q并设置安卓 0=不 1=是
    private int isDoubleSwtich = 0;


    private ScaleAnimation sa1 = new ScaleAnimation(1, 0, 1, 1,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
    private ScaleAnimation sa2 = new ScaleAnimation(0, 1, 1, 1,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
    private RotateAnimation animation, animation1;
    private ProgressDialog pgDialog, switchDialog, waitDialog;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private View view;
    private TextView tv_userqq;
    private TextView tv_board;
    private TextView tv_lhzt;
//    private TextView tv_bt_jiechu;
    private Button bt_xufei;
    private TextView tv_viplevel;
    private ImageView img_xz;
    private ImageView img_mqq;
    private ImageView img_pcqq;
    private ImageView img_music;
    private ImageView img_guanjia;
    private ImageView img_game;
    private ImageView img_kjfk;
    private ImageView img_weishi;
    private ImageView img_weishi_1;
    private ImageView img_yd;
    private ImageView[] imageViews = new ImageView[9];
    TextView[] textViews = new TextView[9];
    private TextView tv_guanjia;
    private TextView tv_xz;
    private TextView tv_mqq;
    private TextView tv_pcqq;
    private TextView tv_music;
    private TextView tv_game;
    private TextView tv_kjfk;
    private TextView tv_weishi;
    private TextView tv_weishi_1;
    private TextView tv_yd;
    private Button bt_gengxin;
    private Button bt_lougua;
    private RelativeLayout ll_bgbg;
    private LinearLayout ll_moren;
    private Button bt_lou_cancel, bt_lou_submit, bt_kf;
    private RadioGroup group_1;
    private RadioButton group_rb;
    private LinearLayout rl4;
    private Button bt_viplevel;
    private String skin_value;
    private Button bt_newuser;
    private BootstrapButton lhzt_1;

    private List<Badge> badges;

    public YH_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_yh, container, false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        skin_value = preferences.getString("skin_value", "1");
        bindviews();
        setTitle();
        setBoard();
//        setBlack();

        setLevel();
        return view;
    }

    private void setLevel() {
        user = preferences.getString("account", "");
        pwd = preferences.getString("pwd", "");
        String post_url = null;
        try {
            post_url = MainActivity
                    .app_url + MainActivity.app_url_1 + "&info=dginfo3" + MainActivity.app_url_qq + user + "&pwd=" + DESUtil.encry(pwd) + "";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "level");
//            jsonObject.put("qq", user);
//            jsonObject.put("pwd", pwd);
            HttpRequest http = new HttpRequest(post_url, jsonObject.toString(), handler);
            http.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void setBlack() {
//        sid = preferences.getString("sid", "");
        user = preferences.getString("account", "");
        pwd = preferences.getString("pwd", "");
        Log.e("用户中心进入，QQ为", user);
        String post_url = MainActivity
                .app_url + MainActivity.app_url_1 + "&info=dginfo";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "black");
            Log.e("SetBlack", "user:" + user + "--pwd:" + pwd);
            int i = 0;
            while (user.length() == 0) {
                user = preferences.getString("account", "");
                pwd = preferences.getString("pwd", "");
                i++;
                if (i > 10) {
                    break;
                }
            }
            jsonObject.put("qq", user);
            jsonObject.put("pwd", pwd);
            HttpRequest http = new HttpRequest(post_url, jsonObject.toString(), handler);
            http.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setBoard() {
        String post_url = MainActivity.app_url + "ajax/dg.php?ajax=true&star=get";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "board");
            HttpRequest http = new HttpRequest(post_url, jsonObject.toString(), handler);
            http.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 3) {
                try {
                    JSONObject json = new JSONObject((String) msg.obj);
                    String code = json.getString("code");
                    String message = json.getString("error");
                    if (code.equals("0")) {
                        tv_board.setText(message);
                    } else {
                        Toast.makeText(getContext(), "公告获取失败", Toast.LENGTH_SHORT).show();
                    }
                    pgdialog_cancel();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == 4) {
//                    String serverday = preferences.getString("serverday", "");
//                    System.out.println("在线天数" + serverday + "剩余天数" + dgtime);
//                    Double double_1 = Double.parseDouble(serverday);
//                    Double double_2 = Double.parseDouble(dgtime);
//                    Long double_finis = Math.round(Double.parseDouble(String.valueOf
//                            (double_2 +
//                                    double_1 * 1.2)));
//                    System.out.println("积分：" + double_finis);


//                    tv_viplevel.setText("此功能整修中...");
//                    tv_viplevel.setTextColor(Color.GRAY);

            } else if (msg.what == 5) {
                try {
                    JSONObject json = new JSONObject((String) msg.obj);
                    String code = json.getString("code");
                    String cookie = json.getString("cookie");
                    String error = json.getString("error");
                    switchDialog.cancel();
                    if (error.equals("修改代挂成功，21点后操作请第二天提交补挂")) {
                        String word = "";
                        String word1 = textViews[switch_sign].getText().toString();
                        for (int j = 0; j < word1.length() - 3; j++) {
                            word += word1.charAt(j);
                        }
                        if (status[switch_sign].equals("0")) {
                            //正规关闭
                            word += "关闭]";
                            textViews[switch_sign].setText(word);
                            ImgHui(imageViews[switch_sign]);
                            imageViews[switch_sign].startAnimation(animation1);

                            //运动跟随手机
                            if (switch_sign == 2) {
                                tv_yd.setText("QQ运动[已关闭]");
                                ImgHui(img_yd);
                                img_yd.startAnimation(animation1);
                            }
                        } else if (status[switch_sign].equals("1")) {
                            //正规开启
                            if (switch_sign == 2) {
                                //苹果手机开启
                                word += "开启]";
                                textViews[switch_sign].setText(word);
                                imageViews[switch_sign].setImageDrawable(getResources().getDrawable(R
                                        .mipmap.htt_sjqq));
                                ImgLiang(imageViews[switch_sign]);
                                imageViews[switch_sign].startAnimation(animation);

                                if (isDoubleSwtich == 1) {
                                    setMqqStatus();
                                }
                                //运动跟随手机
//                                if (switch_sign == 2) {
//                                    tv_yd.setText("QQ运动[已关闭]");
//                                    ImgHui(img_yd);
//                                    img_yd.startAnimation(animation1);
//                                }
                            } else {
                                //其他项目开启
                                word += "开启]";
                                textViews[switch_sign].setText(word);
                                ImgLiang(imageViews[switch_sign]);
                                imageViews[switch_sign].startAnimation(animation);
                            }
                        } else if (status[switch_sign].equals
                                ("2")) {
                            //安卓手机开启

                            word += "开启]";
                            textViews[switch_sign].setText(word);
                            imageViews[switch_sign].setImageDrawable(getResources().getDrawable(R
                                    .mipmap.htt_sjqq_android));
                            ImgLiang(imageViews[switch_sign]);
                            imageViews[switch_sign].startAnimation(animation);

                            //运动跟随手机
                            if (switch_sign == 2) {
                                tv_yd.setText("QQ运动[已开启]");
                                img_yd.setImageDrawable(getResources().getDrawable(R
                                        .mipmap.htt_yundong));
                                ImgLiang(img_yd);
                                img_yd.startAnimation(animation);
                            }
                        }
                        Toast.makeText(getContext(), "修改成功，立即生效", Toast.LENGTH_SHORT).show();
                    } else {
                        if (error.equals("第一次添加需要扫码!")) {
                            Toast.makeText(getContext(), "新注册用户，请先扫码再开启功能。请截屏保存相册然后用手Q读取相册扫码（如果一直白屏无法获取二维码，请右上角进网页版登录操作）", Toast.LENGTH_LONG)
                                    .show();
                            Log.e("cookie:", cookie);
                            MainActivity.cookie_1 = cookie;
                            new qrlogin_dialog().show(getFragmentManager(), "");
                        } else {
                            if (switch_sign == 6) {
                                Toast.makeText(getContext(), "修改失败，请截屏用手Q读取相册扫码", Toast.LENGTH_LONG)
                                        .show();
                                Log.e("cookie:", cookie);
                                MainActivity.cookie_1 = cookie;
                                new qrlogin_dialog().show(getFragmentManager(), "");
                            } else {
                                Toast.makeText(getContext(), "修改失败，检查是否过期，或者尝试重新登录（如果一直白屏无法获取二维码，请右上角进网页版登录操作）", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == 7) {
                try {
                    JSONObject json = new JSONObject((String) msg.obj);
                    String code = json.getString("code");
//                    String cookie = json.getString("cookie");
//                    String error = json.getString("error");
                    if (code.equals("1")) {
                        Toast.makeText(getContext(), "解除成功", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id
                                .content_main, new YH_Fragment()).commit();
                    } else {
                        Toast.makeText(getContext(), "解除失败，请检查是否过期，尝试续费", Toast.LENGTH_SHORT)
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == 9) {
                try {
                    JSONObject json = new JSONObject((String) msg.obj);
                    String code = json.getString("code");
                    String error = json.getString("error");
                    if (!error.equals("现在的时间是禁止操作的时间[16-10]")) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "补挂提交成功，补挂开始时间16-24点", Toast
                                            .LENGTH_LONG)
                                            .show();
                                }
                            });
                        }
                    } else {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "补挂失败，补挂提交时间10-16点", Toast
                                            .LENGTH_LONG)
                                            .show();
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                waitDialog.cancel();
            } else if (msg.what == 13) {
                try {
                    if (msg.obj.toString().length() < 10) {
                        tv_viplevel.setText("此功能整修中...");
                        tv_viplevel.setTextColor(Color.GRAY);
                        pgdialog_cancel();
                    } else {
                        JSONObject json = new JSONObject((String) msg.obj);
                        String dgtime = json.getString("dgtime");
                        String code = json.getString("code");
                        String adddate_str = json.getString("adddate");
                        String black = json.getString("black");
                        String data = json.getString("data");
                        String vurl = json.getString("vurl");
                        editor = preferences.edit();
                        editor.putString("dgtime", dgtime);
//                    editor.putString("score", double_finis+"");
                        editor.apply();
                        if (code.equals("0")) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date adddate = sdf.parse(adddate_str);
                            Date enddate = sdf.parse(sdf.format(new Date()));
                            int serverday = getGapCount(adddate, enddate);
                            editor = preferences.edit();
                            editor.putString("serverday", serverday + "");
                            editor.apply();
                            System.out.println("在线天数" + serverday + "剩余天数" + dgtime);
                            Double double_1 = Double.parseDouble(String.valueOf(serverday));
                            Double double_2 = Double.parseDouble(dgtime);
                            Long double_finis = Math.round(Double.parseDouble(String.valueOf
                                    (double_2 +
                                            double_1 * 1.2)));
                            System.out.println("积分：" + double_finis);
                            editor = preferences.edit();
                            editor.putString("score", double_finis + "");
                            editor.apply();
                            /***
                             * VIP等级体系
                             已使用   剩余
                             0         30    VIP1    月卡              x  < 36
                             30        90    VIP2    季卡         36 < x < 126
                             120       180    VIP3    半年卡      126 < x < 306
                             300       360   VIP4    年卡         306 < x < 720
                             660       MAX   VIP5                 720 < x

                             VIP 6                 1200 < x <
                             公式：已用天数 * 1.2 + 剩余代挂天数 * 1 = x
                             *
                             */

                            if (double_finis < 36 && double_finis > 0) {
                                tv_viplevel.setText("VIP 1");
                            } else if (double_finis >= 36 && double_finis < 126) {
                                tv_viplevel.setText("VIP 2");
                            } else if (double_finis >= 126 && double_finis < 306) {
                                tv_viplevel.setText("VIP 3");
                            } else if (double_finis >= 306 && double_finis < 720) {
                                tv_viplevel.setText("VIP 4");
                            } else if (double_finis >= 720) {
                                tv_viplevel.setText("VIP 5");
                            } else if (double_finis <= 0) {
                                tv_viplevel.setTextColor(Color.BLACK);
                                tv_viplevel.setText("VIP 0");
                            }
                            for (int i = 0, j = 0; i < 15; i += 2) {
                                status[j++] = data.charAt(i) + "";
                            }
                            if (vurl.length() < 5) {
                                //微视_1 是否填入URL检测
                                status[8] = "0";
                            } else {
                                status[8] = "1";
                            }
                            CheckSwithImg(imageViews, status, textViews);
                            int dgtime_int = Integer.parseInt(dgtime);
                            if (code.equals("0")) {
                                if (!black.equals("0")) {
                                    lhzt_1.setVisibility(View.VISIBLE);
                                    tv_lhzt.setVisibility(View.GONE);
                                    if (black.equals("1")) {
                                        lhzt_1.setText("密码错误");
                                    } else if (black.equals("2")) {
                                        lhzt_1.setText("QQ冻结");
                                    } else if (black.equals("3")) {
                                        lhzt_1.setText("请关闭设备锁");
                                    }
                                } else {
                                    if (Integer.parseInt(dgtime) < 0) {
                                        MainActivity.guoqi_button = "1";
                                        tv_lhzt.setText("已过期");
                                        tv_lhzt.setTextColor(Color.RED);
                                    } else {
                                        MainActivity.guoqi_button = "0";
                                        Time t = new Time();
                                        t.setToNow();
                                        int hour = t.hour;
                                        if (hour >= 0 && hour < 10) {
                                            tv_lhzt.setText("代挂进行中");
                                            tv_lhzt.setTextColor(Color.RED);
                                        } else if (hour >= 10 && hour < 16) {
                                            tv_lhzt.setText("补挂提交中");
                                            tv_lhzt.setTextColor(Color.GRAY);
                                        } else if (hour >= 16 && hour <= 23) {
                                            tv_lhzt.setText("补挂进行中");
                                            tv_lhzt.setTextColor(Color.RED);
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(getContext(), "登录状态失效，请退出重新登录", Toast.LENGTH_SHORT).show();
                            }

                            bt_xufei.setText("续费[剩余" + dgtime + "天]");

                        } else {
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "等级中心获取失败，请尝试重新登录", Toast
                                                .LENGTH_LONG)
                                                .show();
                                    }
                                });
                            }
                        }
                        pgdialog_cancel();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == 11) {
                try {
                    JSONObject json = new JSONObject((String) msg.obj);
                    String code = json.getString("code");
                    if (code.equals("0")) {
                        if (status[2].equals("1")) {
                            mirror_flip(status[2]);
                            status[2] = "2";
                        } else {
                            mirror_flip(status[2]);
                            status[2] = "1";
                        }
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "切换成功", Toast
                                            .LENGTH_LONG)
                                            .show();
                                }
                            });
                        }
                        isDoubleSwtich = 0;
                    } else {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "切换失败，或已经是该状态", Toast
                                            .LENGTH_LONG)
                                            .show();
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                switchDialog.cancel();
            } else {
                error_sign++;
                if (error_sign > 1) {
                    Toast.makeText(getContext(), "网络请求错误", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "网络请求错误，如果多次出现，请尝试右上角进网页版进行此操作。", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    private void bindviews() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        pgDialog = new ProgressDialog(getContext());
                        pgDialog.setTitle("请稍等");
                        pgDialog.setMessage("资源搜寻中，请稍等...");
                        pgDialog.show();
                        pgDialog.setCancelable(false);
                    } catch (Exception e) {

                    }
                }
            });
        }


        animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        animation1 = new RotateAnimation(360, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation
                .RELATIVE_TO_SELF, 0.5f);
        animation1.setDuration(1000);
        animation1.setFillAfter(true);

        lhzt_1 = (BootstrapButton) view.findViewById(R.id.lhzt_1);
        lhzt_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("注意！")
                        .setContentText("您正在尝试解除拉黑，请点击【更新密码】按钮，更新密码自动解除拉黑状态，（请随时保持密码与QQ密码一致）").show();

            }
        });
        bt_viplevel = (Button) view.findViewById(R.id.bt_viplevel);
        bt_viplevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new qun_dialog().show(getFragmentManager(), "");
            }
        });
        bt_newuser = (Button) view.findViewById(R.id.bt_newuser);
        bt_newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new bz_dialogfrg().show(getFragmentManager(), "");
            }
        });
        bt_kf = (Button) view.findViewById(R.id.bt_kf);
        bt_kf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openURL("http://wpa.qq.com/msgrd?v=3&uin=" + MainActivity.app_qq + "&site=qq&menu=yes");
            }
        });
        ll_bgbg = (RelativeLayout) view.findViewById(R.id.ll_bgbg);
        ll_moren = (LinearLayout) view.findViewById(R.id.ll_moren);
        bt_lougua = (Button) view.findViewById(R.id.bt_lougua);
        bt_lougua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_moren.getVisibility() == View.VISIBLE) {
                    ll_moren.setVisibility(View.INVISIBLE);
                    ll_bgbg.setVisibility(View.VISIBLE);
                } else {
                    ll_moren.setVisibility(View.VISIBLE);
                    ll_bgbg.setVisibility(View.INVISIBLE);
                }
            }
        });
        bt_lou_cancel = (Button) view.findViewById(R.id.bt_lou_cancel);
        bt_lou_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_moren.getVisibility() == View.VISIBLE) {
                    ll_moren.setVisibility(View.INVISIBLE);
                    ll_bgbg.setVisibility(View.VISIBLE);
                } else {
                    ll_moren.setVisibility(View.VISIBLE);
                    ll_bgbg.setVisibility(View.INVISIBLE);
                }
            }
        });
        tv_game = (TextView) view.findViewById(R.id.tv_game);
        tv_music = (TextView) view.findViewById(R.id.tv_music);
        tv_pcqq = (TextView) view.findViewById(R.id.tv_pcqq);
        tv_mqq = (TextView) view.findViewById(R.id.tv_mqq);
        tv_xz = (TextView) view.findViewById(R.id.tv_xz);
        tv_kjfk = (TextView) view.findViewById(R.id.tv_kjfk);
        tv_weishi = (TextView) view.findViewById(R.id.tv_weishi);
        tv_weishi_1 = (TextView) view.findViewById(R.id.tv_load);
        tv_guanjia = (TextView) view.findViewById(R.id.tv_guanjia);
        tv_yd = (TextView) view.findViewById(R.id.tv_yd);
        textViews[0] = tv_guanjia;
        textViews[1] = tv_pcqq;
        textViews[2] = tv_mqq;
        textViews[3] = tv_xz;
        textViews[4] = tv_music;
        textViews[5] = tv_game;
        textViews[6] = tv_kjfk;
        textViews[7] = tv_weishi;
        textViews[8] = tv_weishi_1;
        group_1 = (RadioGroup) view.findViewById(R.id.group_1);
        group_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                group_rb = (RadioButton) view.findViewById(group_1
                        .getCheckedRadioButtonId());

            }
        });
        bt_lou_submit = (Button) view.findViewById(R.id.bt_lou_submit);
        bt_lou_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waitDialog.show();
                Log.e("检测Group", "" + group_rb.getText());
                String func = "";
                switch (group_rb.getText().toString()) {
                    case "电脑管家":
                        func = "guanjia";
                        break;
                    case "电脑QQ":
                        func = "pcqq";
                        break;
                    case "手机QQ":
                        func = "mqq";
                        break;
                    case "勋章墙":
                        func = "xunzhang";
                        break;
                    case "QQ音乐":
                        func = "qqmusic";
                        break;
                    case "QQ手游":
                        func = "xunzhang";
                        break;
                    case "空间访客（易冻结）":
                        func = "qqfk";
                        break;
                    case "QQ微视":
                        func = "qqws";
                        break;
                    default:
                        break;
                }
                String post_url = MainActivity
                        .app_url + MainActivity.app_url_1 + "&info=bg";
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("type", "bg");
                    jsonObject.put("qq", user);
                    jsonObject.put("pwd", pwd);
                    jsonObject.put("func", func);
                    HttpRequest http = new HttpRequest(post_url, jsonObject.toString(), handler);
                    http.start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        rl4 = (LinearLayout) view.findViewById(R.id.rl4);
        rl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id
                        .content_main, new level_Fragment()).commit();
            }
        });
        switchDialog = new ProgressDialog(getContext());
        switchDialog.setTitle("修改中");
        switchDialog.setMessage("修改中，请稍等...");
        switchDialog.setCancelable(false);
        waitDialog = new ProgressDialog(getContext());
        waitDialog.setTitle("提交中");
        waitDialog.setMessage("提交中，请稍等...");
        waitDialog.setCancelable(false);
        img_game = (ImageView) view.findViewById(R.id.img_game);
        img_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchDialog.show();
                switch_sign = 5;
                Switchswitch(5);
            }
        });
        img_guanjia = (ImageView) view.findViewById(R.id.img_guanjia);
        img_guanjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchDialog.show();
                switch_sign = 0;
                Switchswitch(0);
            }
        });
        img_music = (ImageView) view.findViewById(R.id.img_music);
        img_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchDialog.show();
                switch_sign = 4;
                Switchswitch(4);
            }
        });
        img_pcqq = (ImageView) view.findViewById(R.id.img_pcqq);
        img_pcqq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchDialog.show();
                switch_sign = 1;
                Switchswitch(1);
            }
        });
        img_mqq = (ImageView) view.findViewById(R.id.img_mqq);
        img_mqq.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setMqqStatus();
                return true;
            }
        });
        img_mqq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchDialog.show();
                switch_sign = 2;
                Switchswitch(2);
            }
        });
        img_xz = (ImageView) view.findViewById(R.id.img_xz);
        img_xz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchDialog.show();
                switch_sign = 3;
                Switchswitch(3);
            }
        });
        img_kjfk = (ImageView) view.findViewById(R.id.img_kjfk);
        img_kjfk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchDialog.show();
                switch_sign = 6;
                Switchswitch(6);
            }
        });
        img_weishi = (ImageView) view.findViewById(R.id.img_weishi);
        img_weishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchDialog.show();
                switch_sign = 7;
                Switchswitch(7);
            }
        });
        img_weishi_1 = (ImageView) view.findViewById(R.id.img_load);
        img_weishi_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch_sign = 8;
                Switchswitch(8);
            }
        });
        img_yd = (ImageView) view.findViewById(R.id.img_yd);
        img_yd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
//                        .setTitleText("注意！")
//                        .setContentText("QQ运动无法直接开关，请将手机在线功能调成安卓将自动开启，设置其他状态自动关闭。").show();

                if (status[2].equals("0") || status[2].equals("1")) {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setCanceledOnTouchOutside(false);
                    sweetAlertDialog
                            .setTitleText("温馨提示")
                            .setContentText("开启运动代挂，将强行开启安卓手Q在线，确认开启吗？")
                            .setCancelText("取消")
                            .setConfirmText("确定")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    if (status[2].equals("1")) {
                                        setMqqStatus();
                                        sDialog.dismiss();
                                    } else if (status[2].equals("0")) {
                                        switchDialog.show();
                                        switch_sign = 2;
                                        isDoubleSwtich = 1;
                                        Switchswitch(2);
                                        sDialog.dismiss();
                                    }
                                }
                            })
                            .show();
                } else {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setCanceledOnTouchOutside(false);
                    sweetAlertDialog
                            .setTitleText("温馨提示")
                            .setContentText("关闭运动代挂，将强行关闭手Q代挂，确认关闭吗？")
                            .setCancelText("取消")
                            .setConfirmText("确定")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    switchDialog.show();
                                    switch_sign = 2;
                                    Switchswitch(2);
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        });
        imageViews[0] = img_guanjia;
        imageViews[1] = img_pcqq;
        imageViews[2] = img_mqq;
        imageViews[3] = img_xz;
        imageViews[4] = img_music;
        imageViews[5] = img_game;
        imageViews[6] = img_kjfk;
        imageViews[7] = img_weishi;
        imageViews[8] = img_weishi_1;
        tv_viplevel = (TextView) view.findViewById(R.id.tv_viplevel);
        bt_gengxin = (Button) view.findViewById(R.id.bt_gengxin);
        bt_gengxin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new gengxin_dialog().show(getFragmentManager(), null);
            }
        });
        bt_xufei = (Button) view.findViewById(R.id.bt_xufei);
        bt_xufei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new xufei_dialog().show(getFragmentManager(), "");
            }
        });
//        tv_bt_jiechu = (TextView) view.findViewById(R.id.tv_bt_jiechu);
//        tv_bt_jiechu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                new jiechu_dialog().show(getFragmentManager(), "");
//                new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
//                        .setTitleText("注意！")
//                        .setContentText("无法直接解除，请点击【更新密码】按钮，进行密码更新自动解除拉黑状态，（请随时保持密码与QQ密码一致）").show();
//
//            }
//        });
        tv_lhzt = (TextView) view.findViewById(R.id.lhzt);
        tv_lhzt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new time_dialog().show(getFragmentManager(), "");
            }
        });
        String sid = preferences.getString("sid", "");
        Log.e("SID1为", sid);
        user = preferences.getString("account", "");
        user = preferences.getString("user_qq", "");
        pwd = preferences.getString("pwd", "");

        tv_userqq = (TextView) view.findViewById(R.id.tv_dqqq);
        tv_userqq.setText("当前QQ：" + user);
        tv_board = (TextView) view.findViewById(R.id.tv_board);
        tv_board.setMovementMethod(ScrollingMovementMethod.getInstance());


        switch (skin_value) {
            //1 = 默认 2 = 绿 3 = 橙 4 = 紫 5 = 彩色
            case "1":
                bt_xufei.setBackgroundResource(R.drawable.king_button);
                bt_newuser.setBackgroundResource(R.drawable.king_button);
                bt_kf.setBackgroundResource(R.drawable.king_button);
                bt_lou_submit.setBackgroundResource(R.drawable.king_button);

                break;
            case "2":

                bt_xufei.setBackgroundResource(R.drawable.king_button_green);
                bt_newuser.setBackgroundResource(R.drawable.king_button_green);
                bt_kf.setBackgroundResource(R.drawable.king_button_green);
                bt_lou_submit.setBackgroundResource(R.drawable.king_button_green);
                break;
            case "3":
                bt_xufei.setBackgroundResource(R.drawable.king_button_orange);
                bt_newuser.setBackgroundResource(R.drawable.king_button_orange);
                bt_kf.setBackgroundResource(R.drawable.king_button_orange);
                bt_lou_submit.setBackgroundResource(R.drawable.king_button_orange);
                break;
            case "4":
                bt_xufei.setBackgroundResource(R.drawable.king_button_pou);
                bt_newuser.setBackgroundResource(R.drawable.king_button_pou);
                bt_kf.setBackgroundResource(R.drawable.king_button_pou);
                bt_lou_submit.setBackgroundResource(R.drawable.king_button_pou);
                break;
            case "5":
                bt_xufei.setBackgroundResource(R.drawable.king_button_colorful);
                bt_newuser.setBackgroundResource(R.drawable.king_button_colorful);
                bt_kf.setBackgroundResource(R.drawable.king_button_colorful);
                bt_lou_submit.setBackgroundResource(R.drawable.king_button_colorful);
                break;
            case "6":
                bt_xufei.setBackgroundResource(R.drawable.king_button_black);
                bt_newuser.setBackgroundResource(R.drawable.king_button_black);
                bt_kf.setBackgroundResource(R.drawable.king_button_black);
                bt_lou_submit.setBackgroundResource(R.drawable.king_button_black);
                break;
            case "7":
                bt_xufei.setBackgroundResource(R.drawable.king_button_red);
                bt_newuser.setBackgroundResource(R.drawable.king_button_red);
                bt_kf.setBackgroundResource(R.drawable.king_button_red);
                bt_lou_submit.setBackgroundResource(R.drawable.king_button_red);
                break;
        }


    }

    private void setMqqStatus() {
        if (status[2].equals("0")) {
            Toast.makeText(getContext(), "切换状态前需要打开该功能哦", Toast.LENGTH_SHORT).show();
            return;
        }
        switchDialog.show();
        String post_url = MainActivity
                .app_url + MainActivity.app_url_1 + "&info=phonetype";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "gengx_phonetype");
            jsonObject.put("qq", user);
            jsonObject.put("pwd", pwd);
            jsonObject.put("status", status[2]);
            HttpRequest http = new HttpRequest(post_url, jsonObject.toString(), handler);
            http.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void pgdialog_cancel() {
        sign_dialog++;
        if (sign_dialog == sign_dialog_cancel) {
            pgDialog.cancel();
        }
    }

    private void ImgHui(ImageView imageView) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);//饱和度 0灰色 100过度彩色，50正常
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        imageView.setColorFilter(filter);
    }

    private void ImgLiang(ImageView imageView) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.reset();
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        imageView.setColorFilter(filter);
    }

    private String switchName(int sign) {
        String switch_name = "";
        switch (sign) {
            case 0:
                switch_name = "guanjia";
                break;
            case 1:
                switch_name = "pcqq";
                break;
            case 2:
                switch_name = "mqq";
                break;
            case 3:
                switch_name = "xunzhang";
                break;
            case 4:
                switch_name = "qqmusic";
                break;
            case 5:
                switch_name = "qqgame";
                break;
            case 6:
                switch_name = "qqfk";
                break;
            case 7:
                switch_name = "qqws";
                break;
        }
        return switch_name;
    }

    private void Switchswitch(int sign) {
        if (sign == 8) {
            new vurl_dialog().show(getFragmentManager(), "");
        } else {
            String switch_name = switchName(sign);
            user = preferences.getString("account", "");
            user = preferences.getString("user_qq", "");
            if (status[sign].equals("0")) {
                status[sign] = "1";
            } else if (status[sign].equals("1") || status[sign].equals("2")) {
                status[sign] = "0";
            }
            String post_url = MainActivity
                    .app_url + MainActivity.app_url_1 + "&info=sw";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", "switch");
                jsonObject.put("qq", user);
                jsonObject.put("pwd", pwd);
                jsonObject.put("id", switch_name);
                jsonObject.put("star", status[sign]);
                HttpRequest http = new HttpRequest(post_url, jsonObject.toString(), handler);
                http.start();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void CheckSwithImg(ImageView[] imageViews, String[] status, TextView[] textView) {
        img_yd.startAnimation(animation);
        //运动图标 单独旋转

        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i].startAnimation(animation);
            switch (status[i]) {
                case "0":
                    ImgHui(imageViews[i]);
                    String word = "";
                    String word1 = textView[i].getText().toString();
                    for (int j = 0; j < word1.length() - 3; j++) {
                        word += word1.charAt(j);
                    }
                    word += "关闭]";
                    textView[i].setText(word);

                    //运动单独设置
                    if (i == 2) {
                        tv_yd.setText("QQ运动[已关闭]");
                        ImgHui(img_yd);
                    }
                    break;
                case "1":
//                    ImgLiang(imageViews[i]);
                    word = "";
                    word1 = textView[i].getText().toString();
                    for (int j = 0; j < word1.length() - 3; j++) {
                        word += word1.charAt(j);
                    }
                    String word2 = word + "关闭]";
                    word += "开启]";
                    textView[i].setText(word);

                    //运动单独设置
                    if (i == 2) {
                        ImgHui(img_yd);
                        tv_yd.setText("QQ运动[已关闭]");
                    }
                    break;
                case "2":
                    imageViews[i].setImageDrawable(getResources().getDrawable(R.mipmap
                            .htt_sjqq_android));

                    word = "";
                    word1 = textView[i].getText().toString();
                    for (int j = 0; j < word1.length() - 3; j++) {
                        word += word1.charAt(j);
                    }
                    word += "开启]";
                    textView[i].setText(word);

                    //运动单独设置
                    if (i == 2) {
                        tv_yd.setText("QQ运动[已开启]");
                    }
                    break;
            }
        }
    }

    private void openURL(String s) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(s);
        intent.setData(content_url);
        startActivity(intent);
    }

    private void mirror_flip(final String status) {
        sa1.setDuration(500);
        sa2.setDuration(500);
        sa1.setAnimationListener(new Animation.AnimationListener() {
            /**
             * 动画开始时调用该方法
             *
             * @Override
             */
            @Override
            public void onAnimationStart(Animation animation) {
            }

            /**
             * 动画重复时调用该方法
             *
             * @Override
             */
            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            /**
             * 动画结束时调用该方法
             *
             * @Override
             */
            @Override
            public void onAnimationEnd(Animation ani) {
                if (status.equals("1")) {
                    imageViews[2].setImageDrawable(getResources().getDrawable(R.mipmap
                            .htt_sjqq_android));
                    Log.e("手Q", "安卓");

//                    运动跟随变化
                    img_yd.setImageDrawable(getResources().getDrawable(R.mipmap
                            .htt_yundong));
                    ImgLiang(img_yd);
                    String word = "QQ运动[已开启]";
                    tv_yd.setText(word);
                    img_yd.startAnimation(animation);

                } else {
                    imageViews[2].setImageDrawable(getResources().getDrawable(R.mipmap
                            .htt_sjqq));
                    Log.e("手Q", "苹果");

//                    //运动跟随变化
                    ImgHui(img_yd);
                    String word = "QQ运动[已关闭]";
                    tv_yd.setText(word);
                    img_yd.startAnimation(animation1);
                }
                imageViews[2].startAnimation(sa2);
            }
        });
        imageViews[2].startAnimation(sa1);
    }

    public void setTitle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getActivity().getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //根据上面设置是否对状态栏单独设置颜色
            if (useThemestatusBarColor) {
                if (skin_value.equals("1")) {
                    getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.blue_title));
                } else if (skin_value.equals("2")) {
                    getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.skin_green));
                } else if (skin_value.equals("3")) {
                    getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.skin_orange));
                } else if (skin_value.equals("4")) {
                    getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.skin_pou));
                } else if (skin_value.equals("5")) {
                    getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.skin_colorful));
                } else if (skin_value.equals("6")) {
                    getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.skin_black));
                } else if (skin_value.equals("7")) {
                    getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.skin_red));
                }
            } else {
                getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getActivity().getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !useStatusBarColor) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    public static int getGapCount(Date startDate, Date endDate) {
        return (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
    }
}
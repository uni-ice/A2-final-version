package com.example.myapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.utils.Utils;

/*
 * Package    :com.example.myapplication
 * ClassName  :AboutActivity
 * Description:about us, about game
 * Data       :2020/5/14 11:39
 */
public class AboutActivity extends BaseActivity {
  private TextView tv_title, tv_msg1, tv_msg2, tv_msg3, tv_msg4;
  private ImageView img;
  private String    game = "如何开始\n" +
    "加载游戏后，点击“开始游戏-选择关卡”即进入游戏。\n" +
    "果蔬连连看\n" +
    "操作方法\n" +
    "手指操作，将图案相同的两张图片用三根以内的直线连在一起就可以消除。\n" +
    "游戏目标\n" +
    "在有限的时间里，要将图片全部消除，才能步入下一关卡，共9个级别，加油通过9关获得最终的胜利。";
  private String    me   = "小游戏这个词的含义其实很简单，他不是一些大的游戏，没有必要花费更多的时间和精力。小游戏是原始的游戏娱乐方式，小游戏本身是为了叫人们在工作，学习后的一种娱乐，休闲的一种方式，不是为了叫玩家为之花费金钱，花费精力，更不是叫玩家为他痴迷。";

  @Override
  int getLayoutID() {
    return R.layout.about_activity;
  }

  @Override
  void initView() {
    img = findViewById(R.id.About_Img);
    tv_title = findViewById(R.id.About_TV);
    tv_msg1 = findViewById(R.id.About_Msg1);
    tv_msg2 = findViewById(R.id.About_Msg2);
    tv_msg3 = findViewById(R.id.About_Msg3);
    tv_msg4 = findViewById(R.id.About_Msg4);
    int type = getIntent().getIntExtra("type", 0);
    if (type == 0) {
      tv_title.setText(Utils.getString(R.string.about_game));
      tv_msg1.setText(Utils.getString(R.string.about_game_text_start));
      tv_msg3.setText(Utils.getString(R.string.about_game_text_function));
      tv_msg2.setText(Utils.getString(R.string.about_game_text_text_one));
      tv_msg4.setText(Utils.getString(R.string.about_game_text_text_two));
    } else {
      tv_title.setText(Utils.getString(R.string.about_us));
      tv_msg2.setText(Utils.getString(R.string.about_us_text));
      tv_msg1.setVisibility(View.GONE);
      tv_msg3.setVisibility(View.GONE);
      tv_msg4.setVisibility(View.GONE);
    }

    img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }
}

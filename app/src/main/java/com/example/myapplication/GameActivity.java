package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.game.GameView;
import com.example.myapplication.sql.SqlUtils;
import com.example.myapplication.utils.Utils;


/*
 * Package    :com.example.myapplication
 * ClassName  :GameActivity
 * Description:game interface
 * Data       :2020/3/25 11:09
 */
public class GameActivity extends BaseActivity {
  private GameView gameView;
  private TextView tv_time, tv_restart, tv_stop, tv_goMain, tv_switchMusic, tv_level, tv_back;
  private long     nowTime     = 0;
  private long     allTime     = 0;
  private int      mDifficulty = 0;
  private boolean  gameState   = true; //true is playing
  private Handler  handler     = new Handler();
  private SqlUtils sqlUtils;
  private Runnable timeRunn    = new Runnable() {
    @Override
    public void run() {
      tv_time.setText(Utils.FormatTime(nowTime));
      if (nowTime <= 0) {
        failure();
        return;
      }
      nowTime -= 1000;
      handler.postDelayed(this, 1000);
    }
  };

  @Override
  int getLayoutID() {
    return R.layout.game_activity;
  }

  @Override
  void initView() {
    sqlUtils = new SqlUtils(mActivity);
    gameView = findViewById(R.id.Game_View);
    tv_level = findViewById(R.id.Game_Level);
    tv_time = findViewById(R.id.Game_Time);
    tv_restart = findViewById(R.id.Game_ReStart);
    tv_stop = findViewById(R.id.Game_stop);
    tv_goMain = findViewById(R.id.Game_goMain);
    tv_back = findViewById(R.id.Game_Back);
    tv_switchMusic = findViewById(R.id.Game_SwitchMusic);
    mDifficulty = getIntent().getIntExtra("number", 1);
    gameView.setDifficulty(mDifficulty);
    initTime();
    gameView.setSuccessListener(new GameView.OnSuccess() {
      @Override
      public void success() {
        showSuccessDialog();
        //all patterns be deleted, stop counting time
        handler.removeCallbacks(timeRunn);
        setCheck();
      }
    });

    tv_goMain.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        utils.UseNamefinishAct(StartGameActivity.class.getName());
        finish();
      }
    });

    tv_back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    tv_restart.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        gameView.reStart();
        initTime();
      }
    });

    tv_stop.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (gameState) {
          handler.removeCallbacks(timeRunn);
          tv_stop.setText(Utils.getString(R.string.resume));
        } else {
          startLooper(1000);
          tv_stop.setText(Utils.getString(R.string.pause));
        }
        gameState = !gameState;
        gameView.setStopState(gameState);
      }
    });

    tv_switchMusic.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        gameView.onPlayCompletion();
      }
    });
  }

  //time up, did not delete all patterns
  void failure() {
    gameView.failure();
    showFailureDialog();
  }

  //restart and reset time
  void initTime() {
    nowTime = 2 * 60 * 1000;
    //ger levels
    if (mDifficulty <= 4)
      nowTime = nowTime - (mDifficulty - 1) * 10 * 1000;
    else if (mDifficulty <= 8) {
      nowTime = nowTime - 30000;
    } else if (mDifficulty <= 10) {
      nowTime = nowTime - (mDifficulty - 5) * 10 * 1000;
    }
    allTime = nowTime;
    startLooper(0);
    setLevel();
  }

  //unlock next level after pass
  void setCheck() {
    int i = Integer.parseInt(sqlUtils.findLevel());
    Log.e("tag", "levels have already passed：" + i + "---" + mDifficulty);
    if ((mDifficulty + 1) > i) {
      Log.e("tag", "whether to account in this function");
      sqlUtils.updateLevel(mDifficulty + 1);
    }
  }

  //start loop
  void startLooper(int time) {
    if (handler != null) {
      handler.removeCallbacks(timeRunn);
      handler.postDelayed(timeRunn, time);
    }
  }

  //pop-up pass hint
  void showSuccessDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("(*^▽^*)");
    builder.setMessage(Utils.getString(R.string.success_one) + ((allTime - nowTime) / 1000) + Utils.getString(R.string.success_two));
    if (mDifficulty >= 10) {
      builder.setPositiveButton(Utils.getString(R.string.restart), new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          gameView.reStart();
          initTime();
        }
      });
    } else {
      builder.setPositiveButton(Utils.getString(R.string.next_level), new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          mDifficulty += 1;
          gameView.setDifficulty(mDifficulty);
          initTime();
        }
      });
    }

    builder.setNegativeButton(Utils.getString(R.string.back), new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        finish();
      }
    });
    AlertDialog alertDialog = builder.create();
    alertDialog.setCancelable(false);
    alertDialog.setCanceledOnTouchOutside(false);
    alertDialog.show();
  }

  //pop-up fail hint
  void showFailureDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("o(╥﹏╥)o");
    builder.setMessage(Utils.getString(R.string.failure));
    builder.setPositiveButton(Utils.getString(R.string.restart), new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        gameView.reStart();
        initTime();
      }
    });
    builder.setNegativeButton(Utils.getString(R.string.back), new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        finish();
      }
    });
    AlertDialog alertDialog = builder.create();
    alertDialog.setCancelable(false);
    alertDialog.setCanceledOnTouchOutside(false);
    alertDialog.show();
  }

  //set level name
  public void setLevel() {
    tv_level.setText(mDifficulty + "");
  }

  @Override
  protected void onResume() {
    super.onResume();
    gameView.onResume();
    startLooper(1000);
  }

  @Override
  protected void onPause() {
    super.onPause();
    gameView.onStop();
    handler.removeCallbacks(timeRunn);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    gameView.onDestroy();
  }
}

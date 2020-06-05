package com.example.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.sql.SqlUtils;
import com.example.myapplication.utils.Utils;


/*
 * Package    :com.example.myapplication
 * ClassName  :StartGameActivity
 * Description:start game (select level)
 * Data       :2020/3/25 11:20
 */
public class StartGameActivity extends BaseActivity {
  private RecyclerView recyclerView;
  private ImageView    img;
  private int          args;//already unlocked
  private MyAdapter    myAdapter;
  private SqlUtils     utils;

  @Override
  int getLayoutID() {
    return R.layout.start_game_activity;
  }

  @Override
  void initView() {
    utils = new SqlUtils(mActivity);
    recyclerView = findViewById(R.id.StartGame_Recy);
    img = findViewById(R.id.StartGame_Exit);
    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    myAdapter = new MyAdapter();
    recyclerView.setAdapter(myAdapter);
    img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    args = Integer.parseInt(utils.findLevel());
    if (myAdapter != null)
      myAdapter.notifyDataSetChanged();
  }

  private class MyAdapter extends RecyclerView.Adapter<MyHolder> {

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
      return new MyHolder(LayoutInflater.from(StartGameActivity.this).inflate(R.layout.item_start_game, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, final int position) {
      myHolder.tv.setText(Utils.getString(R.string.level) + " " + (position + 1));
      if ((position + 1) > args) {//没有开通
        myHolder.tv.setBackgroundResource(R.drawable.main_btn_gray);
      } else
        myHolder.tv.setBackgroundResource(R.drawable.main_btn);
      myHolder.tv.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if ((position + 1) > args) {
            Utils.showToast(Utils.getString(R.string.ban_play));
          } else {
            Intent intent = new Intent(StartGameActivity.this, GameActivity.class);
            intent.putExtra("number", (position + 1));
            startActivity(intent);
          }

        }
      });
    }

    @Override
    public int getItemCount() {
      return 10;
    }
  }

  private class MyHolder extends RecyclerView.ViewHolder {
    private TextView tv;

    MyHolder(@NonNull View itemView) {
      super(itemView);
      tv = (TextView) itemView;
    }
  }


}

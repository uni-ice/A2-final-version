package com.example.myapplication;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.sql.MusicBean;
import com.example.myapplication.sql.SqlUtils;
import com.example.myapplication.sql.MusicUtils;
import com.example.myapplication.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/*
 * Package    :com.example.myapplication
 * ClassName  :MusciActivity
 * Description:BGM
 * Data       :2020/5/26 13:52
 */
public class MusicActivity extends BaseActivity {
  private RecyclerView recyclerView;
  private TextView     tv_add;
  private ImageView    img;
  private SqlUtils     utils;
  private MusicUtils   musicUtils;
  private MyAdapter    mAdapter;
  private MyAdapter    mDialogAdapter;
  private AlertDialog  dialog;


  @Override
  int getLayoutID() {
    return R.layout.music_activity;
  }

  @Override
  void initView() {
    utils = new SqlUtils(this);
    musicUtils = new MusicUtils(this);
    recyclerView = findViewById(R.id.Music_Recy);
    tv_add = findViewById(R.id.Music_Add);
    img = findViewById(R.id.Music_Img);
    mAdapter = new MyAdapter(false);
    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    recyclerView.setAdapter(mAdapter);
    mAdapter.updateList(utils.getAll());
    initBuilder();
    img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    tv_add.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (dialog != null) {
          if (mDialogAdapter.getList() != null && mDialogAdapter.getList().isEmpty())
            mDialogAdapter.updateList(musicUtils.getAll());
          dialog.show();
        }
      }
    });
  }

  void initBuilder() {
    mDialogAdapter = new MyAdapter(true);
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(Utils.getString(R.string.music_library));
    final RecyclerView recy = (RecyclerView) LayoutInflater.from(this).inflate(R.layout.dialog_recy, null, false);
    builder.setView(recy);
    recy.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    recy.setAdapter(mDialogAdapter);


    dialog = builder.create();
  }

  private class MyAdapter extends RecyclerView.Adapter<MyHolder> {

    private List<MusicBean> list;
    private boolean         isClick = false;

    MyAdapter(boolean is) {
      isClick = is;
      list = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
      return new MyHolder(LayoutInflater.from(MusicActivity.this).inflate(R.layout.item_music, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, final int position) {
      myHolder.tv.setText(list.get(position).getName());
      myHolder.tv.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
          delete(list.get(position).getId(), position);
          return true;
        }
      });

      myHolder.tv.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (isClick) {
            MusicBean musicBean = list.get(position);
            boolean add = utils.add(musicBean.getPath(), 0, musicBean.getName());
            if (!add) {
              Utils.showToast("这首歌已经存在了～");
            } else {
              mAdapter.add(musicBean);
              if (dialog != null)
                dialog.dismiss();
            }

          }
        }
      });
    }

    @Override
    public int getItemCount() {
      return list.size();
    }

    void add(MusicBean data) {
      list.add(data);
      notifyItemInserted(list.size());
      notifyItemRangeChanged(list.size(), getItemCount());
    }


    //REMOVE AN ITEM
    void remove(int position) {
      list.remove(position);
      notifyItemRemoved(position);
      if (position != list.size())
        notifyItemRangeChanged(position, list.size() - position);
    }

    //update list
    void updateList(List<MusicBean> ll) {
      int previousSize = list.size();
      list.clear();
      notifyItemRangeRemoved(0, previousSize);
      list.addAll(ll);
      notifyItemRangeInserted(0, ll.size());
    }

    public List<MusicBean> getList() {
      return list;
    }
  }

  private class MyHolder extends RecyclerView.ViewHolder {
    private TextView tv;

    MyHolder(@NonNull View itemView) {
      super(itemView);
      tv = (TextView) itemView;
    }
  }

  //delete
  private void delete(final int id, final int position) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("hint");
    builder.setMessage("delete？");
    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        utils.delete(id);
        mAdapter.remove(position);
      }
    });
    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {

      }
    });
    builder.show();
  }
}

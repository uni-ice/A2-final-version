package com.example.myapplication.game;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.sql.MusicBean;
import com.example.myapplication.sql.SqlUtils;
import com.example.myapplication.utils.SoundPoolUtil;
import com.example.myapplication.utils.SpUtils;
import com.example.myapplication.utils.StringUtils;
import com.example.myapplication.utils.Utils;
import com.example.myapplication.utils.VibrateTool;

import java.io.IOException;
import java.util.List;
import java.util.Random;


public class GameView extends View {
    private int       rows        = 0;
    private int       columns     = 0;
    private int[][][] mData;
    private int       mDifficulty = 0;
    private Random    mRandom;
    private Bitmap    b0, b1, b2, b3, b4, b5, b6, b7;
    private Paint mPaint, mSelPaint;
    private String          selColor   = "#2595e4";
    private int             allLength  = 0;//how many blocks there is
    private int             bitWidth   = 150;  //pic width
    private int             padding    = 10;//margin
    private int             windowWidth;  //window width
    private Point           clickA     = new Point(-1, -1);
    private SoundPoolUtil   soundPoolUtil;
    private boolean         sound      = true;
    private boolean         vibrate    = true;
    private MediaPlayer     mediaPlayer;
    private SqlUtils        utils;
    private List<MusicBean> allMusic;
    private int             musicIndex = 0;
    private boolean         isSuccess  = false;  //pass or not
    private boolean         isFailure  = false;//failed (do not pass)
    private boolean         stopState  = true;//game stop state
    private OnSuccess       successListener;

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mRandom = new Random();
        soundPoolUtil = SoundPoolUtil.getInstance(getContext());
        sound = SpUtils.getBoolean(StringUtils.Sound, true);
        vibrate = SpUtils.getBoolean(StringUtils.Vibrate, true);
        initMediaPlayer();
        initBitmap();
        initPaint();
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GameView);
            mDifficulty = typedArray.getInteger(R.styleable.GameView_num, 0);
            initDifficulty(mDifficulty);
            typedArray.recycle();
        }
        Log.e("tag", "initialization");
    }

    public void setDifficulty(int num) {
        mDifficulty = num;
        isSuccess = false;
        isFailure = false;
        initDifficulty(mDifficulty);
        requestLayout();
    }


    //set difficulty
    private void initDifficulty(int num) {
        if (num == 0)
            return;
        switch (num) {
            case 1:
            case 2:
            case 3:
            case 4: {
                rows = 6;
                columns = 6;
                break;
            }
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10: {
                rows = 8;
                columns = 8;
                break;
            }
        }
        mData = new int[columns][rows][2];
        int size = (rows * columns) / 2;
        allLength = rows * columns;
        int[] mList = new int[size];
        for (int i = 0; i < size; i++) {
            mList[i] = getRandom();
        }
        for (int i = 0; i < mData.length; i++) {
            for (int j = 0; j < mData[i].length; j++) {
                int n = i * rows + j;
                //                if (n >= mList.length)
                //                    n = n / 2;
                //0means set pic
                mData[i][j][0] = mList[n % size];
                mData[i][j][1] = 0;
            }
        }
        initBitmapWidth();
    }

    //initialization pic
    private void initBitmap() {
        b0 = Bitmap.createBitmap(bitWidth, bitWidth, Bitmap.Config.ARGB_8888);
        b1 = Utils.drawable2Bitmap(getContext().getResources().getDrawable(R.mipmap.q0));
        b2 = Utils.drawable2Bitmap(getContext().getResources().getDrawable(R.mipmap.q1));
        b3 = Utils.drawable2Bitmap(getContext().getResources().getDrawable(R.mipmap.q2));
        b4 = Utils.drawable2Bitmap(getContext().getResources().getDrawable(R.mipmap.q3));
        b5 = Utils.drawable2Bitmap(getContext().getResources().getDrawable(R.mipmap.q4));
        b6 = Utils.drawable2Bitmap(getContext().getResources().getDrawable(R.mipmap.q5));
        b7 = Utils.drawable2Bitmap(getContext().getResources().getDrawable(R.mipmap.q6));
    }

    //initialization paint
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor(selColor));
        mPaint.setTextSize(60);

        mSelPaint = new Paint();
        mSelPaint.setColor(Color.parseColor(selColor));
        mSelPaint.setStrokeWidth(5);
    }

    //initialization player
    private void initMediaPlayer() {
        utils = new SqlUtils(getContext());
        mediaPlayer = new MediaPlayer();
        allMusic = utils.getAll();
        play(allMusic.get(musicIndex));
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onPlayCompletion();
            }
        });
    }

    public void onPlayCompletion() {
        musicIndex += 1;
        if (musicIndex >= allMusic.size())
            musicIndex = 0;
        mediaPlayer.stop();
        mediaPlayer.reset();
        play(allMusic.get(musicIndex));
    }

    //play the selected music
    private void play(MusicBean bean) {
        Log.e("tag", "switch music path：" + bean.toString());
        if (sound) {
            try {
                if (bean.getType() == 0) {   //player add
                    mediaPlayer.setDataSource(bean.getPath());
                } else {//application with
                    AssetFileDescriptor fd = getContext().getAssets().openFd(bean.getPath());
                    mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
                }
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //restart
    public void reStart() {
        setDifficulty(mDifficulty);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int height;

        //same way to take width and height data
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = bitWidth * rows;
        }
        //save width and height
        setMeasuredDimension(widthMeasureSpec, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < mData.length; i++) {
            for (int j = 0; j < mData[i].length; j++) {
                int is = mData[i][j][1];
                Bitmap bit = getBitmap(mData[i][j][0]);
                //                int i1 = mData[i][j][0];
                canvas.drawBitmap(bit, (j * bitWidth) + padding + centers,
                        i * bitWidth + padding + centers, mPaint);
                //                canvas.drawText(i1 + "", (j * bitWidth) + padding,
                //                        i * bitWidth + padding + 30, mPaint)
                if (is == 1 && bit != b0) {
                    //up horizontal
                    canvas.drawLine((j * bitWidth) + padding, i * bitWidth + padding,
                            (j * bitWidth) + padding + bitWidth, i * bitWidth + padding, mSelPaint);
                    //left vertical
                    canvas.drawLine((j * bitWidth) + padding, i * bitWidth + padding,
                            (j * bitWidth) + padding, i * bitWidth + padding + bitWidth, mSelPaint);
                    //right vertical
                    canvas.drawLine((j * bitWidth) + padding + bitWidth, i * bitWidth + padding,
                            (j * bitWidth) + padding + bitWidth, i * bitWidth + padding + bitWidth, mSelPaint);
                    //down horizontal
                    canvas.drawLine((j * bitWidth) + padding, i * bitWidth + padding + bitWidth,
                            (j * bitWidth) + padding + bitWidth, i * bitWidth + padding + bitWidth, mSelPaint);
                }
            }
        }
    }

    Bitmap getBitmap(int type) {
        switch (type) {
            case 0:
                return b1;
            case 1:
                return b2;
            case 2:
                return b3;
            case 3:
                return b4;
            case 4:
                return b5;
            case 5:
                return b6;
            case 6:
                return b7;
            case 7:
                return b0;
            default:
                return null;
        }
    }

    private int lastRandom = -1;

    //get random number, avoid too many repeat
    int getRandom() {
        int i = mRandom.nextInt(7);
        if (i == lastRandom)
            return getRandom();
        else
            return i;
    }

    int centers = 0;//Not centered when the number of rows is small Use this parameter to fix

    void initBitmapWidth() {
        windowWidth = Utils.getWindowWidth(getContext());
        bitWidth = (windowWidth) / rows;
        if (bitWidth > b1.getWidth()) {
            centers = (bitWidth - b1.getWidth()) / 2;
        }
        Log.e("tag", "获取屏幕宽度：" + windowWidth + "---" + rows + "---" + bitWidth + "----" + b1.getWidth());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isSuccess || isFailure) {
                Utils.showToast("游戏已经结束了～");
                return true;
            }

            if (!stopState) {
                Utils.showToast("游戏正在暂停～");
                return true;
            }
            int x = (int) event.getX() / bitWidth;
            int y = (int) event.getY() / bitWidth;
            if (clickA.x == -1 && clickA.y == -1) {
                clickA.set(x, y);
                setSelect(clickA.x, clickA.y, 1);
                playClick();
            } else {
                if (judge(clickA.x, clickA.y, x, y)) {
                    Log.e("tag", "执行了移除操作");
                    remove(clickA.x, clickA.y, x, y);
                    clickA.set(-1, -1);
                } else {
                    setSelect(clickA.x, clickA.y, 0);
                    clickA.set(x, y);
                    setSelect(clickA.x, clickA.y, 1);
                    playClick();
                }
            }
            invalidate();
            return true;
        } else
            return super.onTouchEvent(event);
    }

    //et select   1 is selected     0 is not selected
    void setSelect(int x, int y, int type) {
        mData[y][x][1] = type;
    }

    //click to play the BGM
    void playClick() {
        if (sound)
            soundPoolUtil.play(soundPoolUtil.click);
    }

    //remove
    void remove(int x1, int y1, int x2, int y2) {
        if (sound)
            soundPoolUtil.play(soundPoolUtil.remove);
        mData[y1][x1][0] = 7;
        mData[y2][x2][0] = 7;
        invalidate();
        allLength = allLength - 2;
        success();
    }

    //pass
    void success() {
        if (allLength == 0) {
            isSuccess = true;
            if (vibrate)
                VibrateTool.vibrateOnce(getContext(), 1000);
            if (successListener != null)
                successListener.success();
        }
    }

    public void failure() {
        isFailure = true;
        if (vibrate)
            VibrateTool.vibrateOnce(getContext(), 1000);
    }

    //judgement method
    boolean judge(int x1, int y1, int x2, int y2) {
        if (vertical(x1, y1, x2, y2, true))
            return true;
        if (horizon(x1, y1, x2, y2, true))
            return true;
        if (turnOnce(x1, y1, x2, y2, true))
            return true;
        if (turnTwice(x1, y1, x2, y2))
            return true;

        return false;
    }

    //vertical inspection
    boolean vertical(int x1, int y1, int x2, int y2, boolean is) {
        if (x1 == x2 && y1 == y2) {
            return false;
        }
        if (y1 != y2) {
            return false;
        }
        if (mData[y1][x1][0] != mData[y2][x2][0] && is)
            return false;
        int startX = Math.min(x1, x2);
        int endX = Math.max(x1, x2);
        for (int i = startX + 1; i < endX; i++) {
            if (isBlocked(i, y1)) {
                return false;
            }
        }
        return true;
    }


    //horizontal inspection
    boolean horizon(int x1, int y1, int x2, int y2, boolean is) {
        if (x1 == x2 && y1 == y2) {
            return false;
        }
        if (x1 != x2) {
            return false;
        }
        if (mData[y1][x1][0] != mData[y2][x2][0] && is)
            return false;
        int startY = Math.min(y1, y2);
        int endY = Math.max(y1, y2);
        for (int j = startY + 1; j < endY; j++) {
            if (isBlocked(x1, j)) {
                return false;
            }
        }
        return true;
    }


    //determine whether the two pictures clicked are the same
    //one corner judgment
    boolean turnOnce(int x1, int y1, int x2, int y2, boolean is) {
        if (x1 == x2 && y1 == y2) {
            return false;
        }
        if (mData[y1][x1][0] != mData[y2][x2][0] && is)
            return false;
        if (!isBlocked(x2, y1)) {
            if (vertical(x1, y1, x2, y1, false) && horizon(x2, y1, x2, y2, false)) {
                return true;
            }
        }
        if (!isBlocked(x1, y2)) {
            return horizon(x1, y1, x1, y2, false) && vertical(x1, y2, x2, y2, false);
        }
        return false;
    }


    //two corner judgment
    boolean turnTwice(int x1, int y1, int x2, int y2) {
        if (x1 == x2 && y1 == y2) {
            return false;
        }
        if (mData[y1][x1][0] != mData[y2][x2][0])
            return false;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i != x1 && i != x2 && j != y1 && j != y2)) {
                    continue;
                }
                if ((i == x1 && j == y1) || (i == x2 && j == y2)) {
                    continue;
                }
                if (isBlocked(i, j)) {
                    continue;
                }
                if (turnOnce(x1, y1, i, j, false) && (horizon(i, j, x2, y2, false) || vertical(i, j, x2, y2, false))) {
                    return true;
                }
                if ((horizon(x1, y1, i, j, false) || vertical(x1, y1, i, j, false)) && turnOnce(i, j, x2, y2, false)) {
                    return true;
                }
            }
        }
        return false;
    }

    //return true means have obstacle , return false means there is no obstacle
    boolean isBlocked(int x, int y) {
        Log.e("tag", "是否有障碍物：" + "---X：" + x + "---Y:" + y + "---" + mData[y][x][0]);
        return mData[y][x][0] != 7;
    }

    //to judge the interface lose focus and stop playing
    public void onStop() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        }
    }
    //to judge the interface get lose focus and start playing
    public void onResume() {
        if (mediaPlayer != null) {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        }
    }

    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    //eliminate listener
    public void setSuccessListener(OnSuccess successListener) {
        this.successListener = successListener;
    }

    //set stop state
    public void setStopState(boolean stopState) {
        this.stopState = stopState;
    }

    public interface OnSuccess {
        void success();
    }
}
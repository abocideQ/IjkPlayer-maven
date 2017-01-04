package com.example.administrator.breezeemediacontroller.mediacontroller;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;

import com.example.administrator.breezeemediacontroller.mediacontroller.listener.PlayerListener;
import com.example.administrator.breezeemediacontroller.mediacontroller.model.BreezeeModel;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Map;

import tv.danmaku.ijk.media.exo.IjkExoMediaPlayer;
import tv.danmaku.ijk.media.player.AbstractMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * 视频(MediaPlayer)管理类(播放信息管理)
 * 使用IJK、EXOPlayer(韩国EXO组合，MDZZ)
 * Created by Breezee on 2017/01/02.
 */
public class BreezeeVideoManager implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnCompletionListener,
        IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnSeekCompleteListener, IMediaPlayer.OnErrorListener,
        IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnInfoListener {

    public static String TAG = "BreezeeVideoManager";

    //默认显示比例
    public final static int SCREEN_TYPE_DEFAULT = 0; //默认
    public final static int SCREEN_TYPE_16_9 = 1;  //16:9
    public final static int SCREEN_TYPE_4_3 = 2;   //4:3
    private static int TYPE = SCREEN_TYPE_DEFAULT; //显示比例类型

    //是否使用硬解码(默认不使用)
    private static boolean USE_MEDIA_CODEC = false;

    //当前播放的视频宽高宽
    private int currentVideoWidth = 0;  //当前播放的视频宽
    private int currentVideoHeight = 0; //当前播放的视屏的高

    //Player类型(IJK、EXO)
    public final static int IJKPLAYER = 0;  //IJK
    public final static int EXOPLAYER = 1;  //EXO(韩国EXO组合，MDZZ)
    private int videoType = IJKPLAYER;  //Player类型，默认IJK

    //Player实例
    private AbstractMediaPlayer mediaPlayer;    //IJK、EXO
    private Context context;

    //Player控制Hanlder类型
    private PlayerHandler playerHandler;//Player用
    private Handler listenerHandler;//接口回调用
    private WeakReference<PlayerListener> listener;//回调接口
    public static final int HANDLER_PREPARE = 0;    //InitPlayer
    public static final int HANDLER_SETDISPLAY = 1; //setDisplay
    public static final int HANDLER_RELEASE = 2;    //Release
    private int buffterPoint;
    //BreezeeVideoManager实例
    private static BreezeeVideoManager videoManager;

    /*
    * 1.BreezeeVideoManager单例获取
    */
    public static synchronized BreezeeVideoManager instance() {
        if (videoManager == null) {
            videoManager = new BreezeeVideoManager();
        }
        return videoManager;
    }
    /*
    * BreezeeVideoManager初始化
    * */
    public BreezeeVideoManager() {
        mediaPlayer = new IjkMediaPlayer();
        playerHandler = new PlayerHandler((new Handler().getLooper()));
        listenerHandler=new Handler();
    }

    /*
    * 2.设置播放器类型
    * GSYVideoType IJKPLAYER = 0 or IJKEXOPLAYER = 1;
    **/
    public void setVideoType(Context context, int videoType) {
        this.context = context;
        this.videoType = videoType;
    }

    /*
    * PlayerHandler(Player操作)
    * */
    public class PlayerHandler extends Handler {
        public PlayerHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_PREPARE:
                    //Player初始化
                    iniPLayer(msg);
                    break;
                case HANDLER_SETDISPLAY:
                    showDisplay(msg);
                    break;
                case HANDLER_RELEASE:
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                    }
                    //缓存
                    buffterPoint=0;
                    break;
            }
        }
    }

    /*
    * Player初始化
    * */
    private void iniPLayer(Message msg) {
        try {
            currentVideoWidth = 0;
            currentVideoHeight = 0;
            mediaPlayer.release();
            if (videoType == IJKPLAYER) {
                initIJKPlayer(msg);
            } else if (videoType == EXOPLAYER) {
                initEXOPlayer(msg);
            }
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnSeekCompleteListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setOnInfoListener(this);
            mediaPlayer.setOnVideoSizeChangedListener(this);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * IJKPlayer初始化
    * */
    private void initIJKPlayer(Message msg) {
        mediaPlayer = new IjkMediaPlayer();
        //音频初始化
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            if (USE_MEDIA_CODEC) {
                //硬解码
                ((IjkMediaPlayer) mediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
                ((IjkMediaPlayer) mediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);
                ((IjkMediaPlayer) mediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1);
            }
            //设置播放源
            ((IjkMediaPlayer) mediaPlayer).setDataSource(((BreezeeModel) msg.obj).getUrl(), ((BreezeeModel) msg.obj).getMapHeadData());
            //设置循环播放
            mediaPlayer.setLooping(((BreezeeModel) msg.obj).isLooping());
            //设置播放速度(速度不为1时)
            if (((BreezeeModel) msg.obj).getSpeed() != 1 && ((BreezeeModel) msg.obj).getSpeed() > 0) {
                ((IjkMediaPlayer) mediaPlayer).setSpeed(((BreezeeModel) msg.obj).getSpeed());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * EXOPlayer初始化
    * */
    private void initEXOPlayer(Message msg) {
        mediaPlayer = new IjkExoMediaPlayer(context);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(context, Uri.parse(((BreezeeModel) msg.obj).getUrl()), ((BreezeeModel) msg.obj).getMapHeadData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * 0.设置Surface(显示用TextureView与MediaPlayer关联)！！！！！！！！！！！
    * Surface获取方法：
    * 创建TextureView对象，然后实现SurfaceTextureListener接口
    * 1.TextureView mTextureView;
    * 2.implements TextureView.SurfaceTextureListener
    * 3.mTextureView.setSurfaceTextureListener(this);
    * 4. //设置16:9
    *   int screenWeight = Utils.getWindowWidth(getActivity());
    *   ViewGroup.LayoutParams layoutParams = mTextureView.getLayoutParams();
    *   layoutParams.height = screenWeight * 9 / 16;
    *   mTextureView.setLayoutParams(layoutParams);
    * 5.@Override
    *    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
    *       mSurface = new Surface(surface);
    *       initMediaPlayer();
    *   }
    * 6.@Override
    *   public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
    *       mSurface = new Surface(surface);
    *       initMediaPlayer();
    *   }
    * 7.mediaPlayer.setSurface(holder);
    * */
    private void showDisplay(Message msg) {
        if (msg.obj == null && mediaPlayer != null) {
            mediaPlayer.setSurface(null);
        } else {
            Surface holder = (Surface) msg.obj;
            if (mediaPlayer != null && holder.isValid()) {
                //Player设置显示用的Surface
                mediaPlayer.setSurface(holder);
            }
            if (mediaPlayer instanceof IjkExoMediaPlayer) {
                //总时长>30
                if (mediaPlayer != null && mediaPlayer.getDuration() > 30
                        //当前播放位置<总时长
                        && mediaPlayer.getCurrentPosition() < mediaPlayer.getDuration()) {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 20);
                }
            }
        }
    }
    public void prepare(final String url, final Map<String, String> mapHeadData, boolean loop, float speed) {
        if (TextUtils.isEmpty(url))
            return;
        Message msg = new Message();
        msg.what = HANDLER_PREPARE;
        msg.obj = new BreezeeModel(url, mapHeadData, loop, speed);
        playerHandler.sendMessage(msg);
    }

    public void releaseMediaPlayer() {
        Message msg = new Message();
        msg.what = HANDLER_RELEASE;
        playerHandler.sendMessage(msg);
    }

    public void setDisplay(Surface holder) {
        Message msg = new Message();
        msg.what = HANDLER_SETDISPLAY;
        msg.obj = holder;
        playerHandler.sendMessage(msg);
    }

    public PlayerListener listener() {
        if (listener == null)
            return null;
        return listener.get();
    }

    public void setListener(PlayerListener listener) {
        if (listener == null)
            this.listener = null;
        else
            this.listener = new WeakReference<>(listener);
    }

    @Override
    public void onPrepared(IMediaPlayer mp) {
        listenerHandler.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener().onPrepared();
                }
            }
        });
    }

    @Override
    public void onCompletion(IMediaPlayer mp) {
        listenerHandler.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener().onAutoCompletion();
                }
            }
        });
    }

    /*
    * 播放缓冲状态
    * */
    @Override
    public void onBufferingUpdate(IMediaPlayer mp, final int percent) {
        listenerHandler.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    if (percent > buffterPoint) {
                        listener().onBufferingUpdate(percent);
                    } else {
                        listener().onBufferingUpdate(buffterPoint);
                    }
                }
            }
        });
    }
    /*
    * 进度调整完成
    * */
    @Override
    public void onSeekComplete(IMediaPlayer mp) {
        listenerHandler.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener().onSeekComplete();
                }
            }
        });
    }
    /*
    * 错误状态
    * */
    @Override
    public boolean onError(IMediaPlayer mp, final int what, final int extra) {
        listenerHandler.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener().onError(what, extra);
                }
            }
        });
        return true;
    }

    @Override
    public boolean onInfo(IMediaPlayer mp, final int what, final int extra) {
        listenerHandler.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener().onInfo(what, extra);
                }
            }
        });
        return false;
    }
    /*
    * VideoSize变化
    * */
    @Override
    public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
        currentVideoWidth = mp.getVideoWidth();
        currentVideoHeight = mp.getVideoHeight();
        listenerHandler.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener().onVideoSizeChanged();
                }
            }
        });
    }

    /*
     * 暂停播放
     **/
    public static void onPause() {
        if (BreezeeVideoManager.instance().listener() != null) {
            BreezeeVideoManager.instance().listener().onVideoPause();
        }
    }

    /*
     * 恢复播放
     **/
    public static void onResume() {
        if (BreezeeVideoManager.instance().listener() != null) {
            BreezeeVideoManager.instance().listener().onVideoResume();
        }
    }
    /*
    * 获取当前视频高宽
    * */
    public int getCurrentVideoWidth() {
        return currentVideoWidth;
    }
    public int getCurrentVideoHeight() {
        return currentVideoHeight;
    }
    /*
    * 设置显示比例
    * */
    public static void setShowType(int type) {
        TYPE = type;
    }
    public static int getShowType() {
        return TYPE;
    }
    /*
    * 设置是否使用硬解码
    * */
    public static void setUseMediaCodec(boolean ifUseMediaCodec){
        USE_MEDIA_CODEC=ifUseMediaCodec;
    }
    /*
    * 获取MediaPlayer
    * */

    public AbstractMediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}

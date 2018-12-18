package com.mor.eye.ui.video;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mor.eye.R;
import com.mor.eye.util.DisplayUtils;
import com.mor.eye.util.glide.GlideApp;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.model.VideoOptionModel;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;

import java.util.ArrayList;
import java.util.List;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class EyepetizerVideoPlayer extends StandardGSYVideoPlayer {
    private ImageView ivPlay;
    private ImageView ivThumb;
    private LinearLayout ll4GPlay;

    private RelativeLayout mPreviewLayout;
    private TextView mPreviewTime;
    private boolean mOpenPreView;
    private int mPreProgress = -2;
    private boolean mIsFromUser;
    private Context mContext;
    private String mUrl;

    public EyepetizerVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public EyepetizerVideoPlayer(Context context) {
        super(context);
    }

    public EyepetizerVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(final Context context) {
        super.init(context);
        mContext = context;
        ll4GPlay = (LinearLayout) findViewById(R.id.ll_4g_play);
        ivPlay = (ImageView) findViewById(R.id.iv_play);
        ivPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clickStartIcon();
                if (onPlayClickListener != null) {
                    onPlayClickListener.onPlayClick();
                }
            }
        });
        ll4GPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickStartIcon();
            }
        });
        ivThumb = (ImageView) findViewById(R.id.iv_thumb);
        mPreviewLayout = (RelativeLayout) findViewById(R.id.preview_layout);
        mPreviewTime = (TextView) findViewById(R.id.preview_time);

        //  开启后，pause 时拖动进度条，ivThumb 不会立即更新。
        VideoOptionModel videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);
        List<VideoOptionModel> list = new ArrayList<>();
        list.add(videoOptionModel);
        //  关闭播放器缓冲，这个必须关闭，否则会出现播放一段时间后，一直卡主，控制台打印 FFP_MSG_BUFFERING_START
        videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0);
        list.add(videoOptionModel);
        GSYVideoManager.instance().setOptionModelList(list);

        setShowPauseCover(true);
        mReleaseWhenLossAudio = false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_video;
    }

    public void autoPlay() {
        clickStartIcon();
    }

    public void handPlay() {
        clickStartIcon();
    }

    public void show4gPlay() {
        ivPlay.setVisibility(GONE);
        ll4GPlay.setVisibility(VISIBLE);
    }

    public void setmOpenPreView(boolean openPreView) {
        this.mOpenPreView = openPreView;
    }

    private void showThumbImageView() {
        ivThumb.setVisibility(VISIBLE);
    }

    private void hideThumbImageView() {
        ivThumb.setVisibility(GONE);
    }

    public void loadThumbImage(String url) {
        mUrl = url;
        GlideApp.with(mContext).asDrawable()
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .transition(withCrossFade())
                .thumbnail(
                        GlideApp.with(mContext).asDrawable()
                                .load(new ColorDrawable(DisplayUtils.getColor(mContext, R.color.colorBlack)))
                                .dontAnimate()
                )
                .into(ivThumb);
    }

    @Override
    protected void resolveUIState(int state) {
        switch (state) {
            case CURRENT_STATE_NORMAL:
                changeUiToNormal();
                showThumbImageView();
                ivPlay.setVisibility(VISIBLE);
                cancelDismissControlViewTimer();
                break;
            case CURRENT_STATE_PREPAREING:
                changeUiToPreparingShow();
                showThumbImageView();
                ivPlay.setVisibility(GONE);
                setViewShowState(mTopContainer, INVISIBLE);
                setViewShowState(mBottomContainer, INVISIBLE);
                cancelDismissControlViewTimer();
                break;
            case CURRENT_STATE_PLAYING:
                changeUiToPlayingShow();
                hideThumbImageView();
                setViewShowState(mTopContainer, INVISIBLE);
                setViewShowState(mBottomContainer, INVISIBLE);
                ivPlay.setVisibility(GONE);
                ll4GPlay.setVisibility(GONE);
                startDismissControlViewTimer();
                break;
            case CURRENT_STATE_PAUSE:
                changeUiToPauseShow();
                hideThumbImageView();
                ivPlay.setVisibility(VISIBLE);
                startDismissControlViewTimer();
                break;
            case CURRENT_STATE_ERROR:
                changeUiToError();
                hideThumbImageView();
                ivPlay.setVisibility(VISIBLE);
                ll4GPlay.setVisibility(GONE);
                mPreviewLayout.setVisibility(GONE);
                dismissProgressDialog();
                break;
            case CURRENT_STATE_AUTO_COMPLETE:
                changeUiToCompleteShow();
                showThumbImageView();
                ivPlay.setVisibility(VISIBLE);
                ll4GPlay.setVisibility(GONE);
                mPreviewLayout.setVisibility(GONE);
                setViewShowState(mBottomContainer, INVISIBLE);
                cancelDismissControlViewTimer();
                dismissProgressDialog();
                break;
            case CURRENT_STATE_PLAYING_BUFFERING_START:
                changeUiToPlayingBufferingShow();
                hideThumbImageView();
                break;
            default:
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        super.onProgressChanged(seekBar, progress, fromUser);
        if (fromUser && mOpenPreView && (getCurrentState() == CURRENT_STATE_PLAYING || getCurrentState() == CURRENT_STATE_PAUSE)) {
            int time = progress * getDuration() / 100;
            showPreView(time);
            if (GSYVideoManager.instance().getPlayer() != null
                    && mHadPlay && (mOpenPreView)) {
                mPreProgress = progress;
            }
        }

        if (getCurrentState() == CURRENT_STATE_PLAYING || getCurrentState() == CURRENT_STATE_PAUSE) {
            mBottomProgressBar.setProgress(progress);
            mBottomProgressBar.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onCompletion() {
        super.onCompletion();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        super.onStartTrackingTouch(seekBar);
        if (mOpenPreView) {
            mIsFromUser = true;
            mPreviewLayout.setVisibility(VISIBLE);
            mPreProgress = -2;
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (mOpenPreView) {
            if (mPreProgress >= 0) {
                seekBar.setProgress(mPreProgress);
            }
            super.onStopTrackingTouch(seekBar);
            mIsFromUser = false;
            mPreviewLayout.setVisibility(GONE);
        } else {
            super.onStopTrackingTouch(seekBar);
        }
    }

    @Override
    protected void setTextAndProgress(int secProgress) {
        if (mIsFromUser) {
            return;
        }
        super.setTextAndProgress(secProgress);
    }

    @Override
    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
        GSYBaseVideoPlayer gsyBaseVideoPlayer = super.startWindowFullscreen(context, actionBar, statusBar);
        EyepetizerVideoPlayer eyepetizerVideoPlayer = (EyepetizerVideoPlayer) gsyBaseVideoPlayer;
        eyepetizerVideoPlayer.mOpenPreView = mOpenPreView;
        eyepetizerVideoPlayer.loadThumbImage(mUrl);
        eyepetizerVideoPlayer.setOnPlayClickListener(onPlayClickListener);
        return gsyBaseVideoPlayer;
    }

    private void showPreView(int time) {
        mPreviewTime.setText(CommonUtil.stringForTime(time));
    }

    @Override
    protected void touchDoubleUp() {
    }

    @Override
    protected void updateStartImage() {
        if (mStartButton instanceof ImageView) {
            ImageView imageView = (ImageView) mStartButton;
            if (mCurrentState == CURRENT_STATE_PLAYING) {
                imageView.setImageResource(R.mipmap.video_pause);
            } else if (mCurrentState == CURRENT_STATE_ERROR) {
                imageView.setImageResource(R.mipmap.video_play);
            } else {
                imageView.setImageResource(R.mipmap.video_play);
            }
        }
    }

    /**
     * 退出全屏，全屏按钮
     *
     * @return
     */
    @Override
    public int getShrinkImageRes() {
        return R.mipmap.video_out_screen;
    }

    /**
     * 全屏，全屏按钮
     *
     * @return
     */
    @Override
    public int getEnlargeImageRes() {
        return R.mipmap.video_full_screen;
    }

    public int getRotate() {
        return mRotate;
    }

    @Override
    protected void showProgressDialog(float deltaX, String seekTime, int seekTimePosition, String totalTime, int totalTimeDuration) {
        if (mCurrentState != CURRENT_STATE_ERROR && mCurrentState != CURRENT_STATE_AUTO_COMPLETE) {
            mPreviewLayout.setVisibility(VISIBLE);
            mPreviewTime.setText(seekTime + "/" + totalTime);
        }
    }

    @Override
    protected void dismissProgressDialog() {
        mPreviewLayout.setVisibility(GONE);
        mPreviewTime.setText("00:00");
    }

    public void rePlay() {
        if (mCurrentState == CURRENT_STATE_PAUSE) {
            if (mVideoAllCallBack != null && isCurrentMediaListener()) {
                if (mIfCurrentIsFullscreen) {
                    Debuger.printfLog("onClickResumeFullscreen");
                    mVideoAllCallBack.onClickResumeFullscreen(mOriginUrl, mTitle, this);
                } else {
                    Debuger.printfLog("onClickResume");
                    mVideoAllCallBack.onClickResume(mOriginUrl, mTitle, this);
                }
            }
            try {
                GSYVideoManager.instance().getPlayer().start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            setStateAndUi(CURRENT_STATE_PLAYING);
        }
    }

    @Override
    protected void showVolumeDialog(float deltaY, int volumePercent) {
    }

    @Override
    protected void onBrightnessSlide(float percent) {
    }

    @Override
    protected void showBrightnessDialog(float percent) {
    }

    private OnPlayClickListener onPlayClickListener;

    public void setOnPlayClickListener(OnPlayClickListener onPlayClickListener) {
        this.onPlayClickListener = onPlayClickListener;
    }

    public interface OnPlayClickListener {
        void onPlayClick();
    }
}


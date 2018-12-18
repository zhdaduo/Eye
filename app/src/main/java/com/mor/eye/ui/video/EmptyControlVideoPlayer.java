package com.mor.eye.ui.video;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mor.eye.R;
import com.mor.eye.util.DisplayUtils;
import com.mor.eye.util.glide.GlideApp;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.model.VideoOptionModel;
import com.shuyu.gsyvideoplayer.utils.NetworkUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import java.util.ArrayList;
import java.util.List;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class EmptyControlVideoPlayer extends StandardGSYVideoPlayer {
    private ImageView ivPlay;
    private ImageView ivThumb;
    private LinearLayout ll4GPlay;

    private TextView mShowTime;
    private Context mContext;

    public EmptyControlVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public EmptyControlVideoPlayer(Context context) {
        super(context);
    }

    public EmptyControlVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(final Context context) {
        super.init(context);
        mContext = context;
        mShowTime = (TextView) findViewById(R.id.tv_time);
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
        mBackButton.setVisibility(GONE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_video_empty_control;
    }

    public void show4gPlay(){
        ivPlay.setVisibility(GONE);
        ll4GPlay.setVisibility(VISIBLE);
    }

    public void showTime(String time) {
        mShowTime.setText(time);
        mShowTime.setVisibility(VISIBLE);
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
                .dontAnimate()
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
                mShowTime.setVisibility(VISIBLE);
                setViewShowState(mTopContainer, INVISIBLE);
                setViewShowState(mBottomContainer, INVISIBLE);
                cancelDismissControlViewTimer();
                break;
            case CURRENT_STATE_PREPAREING:
                changeUiToPreparingShow();
                showThumbImageView();
                mShowTime.setVisibility(GONE);
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
                mShowTime.setVisibility(GONE);
                ivPlay.setVisibility(GONE);
                ll4GPlay.setVisibility(GONE);
                startDismissControlViewTimer();
                break;
            case CURRENT_STATE_PAUSE:
                changeUiToPauseShow();
                hideThumbImageView();
                setViewShowState(mTopContainer, INVISIBLE);
                setViewShowState(mBottomContainer, INVISIBLE);
                ivPlay.setVisibility(VISIBLE);
                mShowTime.setVisibility(VISIBLE);
                startDismissControlViewTimer();
                break;
            case CURRENT_STATE_ERROR:
                changeUiToError();
                hideThumbImageView();
                ivPlay.setVisibility(VISIBLE);
                ll4GPlay.setVisibility(GONE);
                mShowTime.setVisibility(VISIBLE);
                dismissProgressDialog();
                break;
            case CURRENT_STATE_AUTO_COMPLETE:
                changeUiToCompleteShow();
                showThumbImageView();
                mShowTime.setVisibility(VISIBLE);
                ivPlay.setVisibility(VISIBLE);
                ll4GPlay.setVisibility(GONE);
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
    public void startAfterPrepared() {
        super.startAfterPrepared();
        setViewShowState(mBottomContainer, INVISIBLE);
        setViewShowState(mStartButton, INVISIBLE);
        setViewShowState(mBottomProgressBar, VISIBLE);
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

    @Override
    public void onCompletion() {
        super.onCompletion();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    protected void showWifiDialog() {

    }

    @Override
    protected void touchDoubleUp() {
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

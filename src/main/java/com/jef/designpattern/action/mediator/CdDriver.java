package com.jef.designpattern.action.mediator;

/**
 * CD光驱类，一个同事类
 * @author Jef
 * @date 20180902
 */
public class CdDriver extends BaseColleague {
    public CdDriver(Mediator mediator) {
        super(mediator);
    }

    /**
     * 光驱读出来的视频和音频
     */
    private String videoAndSound;

    /**
     * 获取光驱读取出来的视频和音频
     * @return 光驱读取出来的视频和音频
     */
    public String getVideoAndSound() {
        return videoAndSound;
    }

    public void setVideoAndSound(String videoAndSound) {
        this.videoAndSound = videoAndSound;
    }

    /**
     * 读取光盘，业务方法
     */
    public void readCd(String videoAndSound) {
        this.setVideoAndSound(videoAndSound);
        // 通知主板，自己的状态发生了改变
        this.getMediator().changeed(this);
    }
}

package com.kw.app.chinesemedicine.record;

/**
 * @author wty
 * 录音
 **/
public interface OnRecordChangeListener {

    void onVolumnChanged(int value);
    void onTimeChanged(int recordTime, String localPath);

}

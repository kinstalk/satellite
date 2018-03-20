package com.kinstalk.satellite.domain.agnentmodel;

/**
 * Created by zhangchuanqi on 16/6/21.
 */
public class TimerStatus {
	public static final int RUN_STATUS_INIT = 0;
	public static final int RUN_STATUS_RUNNING = 1;
	public static final int RUN_STATUS_WAIT = 2;
    private int runStatus; 			// 0未运行, 1运行中, 2等待下次运行
    private int commandCode;		// 命令状态 标记停止 (0 默认 1停止)
    private TimerModel newTimer;			// 最新的timer

    public TimerStatus(TimerModel newTimer) {
        this.newTimer = newTimer;
    }

    public int getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(int runStatus) {
        this.runStatus = runStatus;
    }

    public TimerModel getNewTimer() {
        return newTimer;
    }

    public void setNewTimer(TimerModel newTimer) {
        this.newTimer = newTimer;
    }

    public int getCommandCode() {
        return commandCode;
    }

    public void setCommandCode(int commandCode) {
        this.commandCode = commandCode;
    }
}

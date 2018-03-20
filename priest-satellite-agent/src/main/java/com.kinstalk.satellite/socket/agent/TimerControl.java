package com.kinstalk.satellite.socket.agent;

import com.kinstalk.satellite.domain.agnentmodel.TimerModel;

import java.util.HashMap;

/**
 * Created by zhangchuanqi on 16/6/21.
 */
public class TimerControl {

	private static HashMap<Long, TimerRun> mapTimerRun = new HashMap<Long, TimerRun>();


	// 停止任务, 不在运行, 直接停止; 如果正在运行, 运行完后停止
	public static void stop(Long timerId) {
		TimerRun timerRun = mapTimerRun.get(timerId);
		if (timerRun != null) {
			timerRun.stop();
		}
	}


	// 更新timer
	public static void updateTimer(TimerModel t) {
		if(t == null || t.getId() == null) {
			return;
		}
		
		TimerRun timerRun;
		if (mapTimerRun != null) {

			timerRun = mapTimerRun.get(t.getId());
			if (timerRun == null) {
				timerRun = new TimerRun(t);
			} else {
				timerRun.update(t);
			}
			
		} else {
			mapTimerRun = new HashMap<>();
			timerRun = new TimerRun(t);
		}

		// 任务没有运行, 开始运行
		if (timerRun.getStatus().getRunStatus() == 0) {
			timerRun.start(t.getSleepTime());
		} else {
			// sleepTime变更, 使用新的timer
			if (timerRun.isSleepTimeUpdate()) {
				timerRun = new TimerRun(t);
				timerRun.start(t.getSleepTime());
			}
		}

		mapTimerRun.put(t.getId(), timerRun);
	}

}

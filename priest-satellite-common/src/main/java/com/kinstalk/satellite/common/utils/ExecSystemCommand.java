package com.kinstalk.satellite.common.utils;

/**
 * Created by digitZhang on 16/4/18.
 */


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecSystemCommand {
	private static String INPUT_STREAM = "INPUT_STREAM";


	private static String ERROR_STREAM = "ERROR_STREAM";

	private static Logger logger = LoggerFactory.getLogger(ExecSystemCommand.class);

	/**
	 * 返回命令执行结果信息串
	 *
	 * @param command 要执行的命令
	 * @return 第一个为标准信息，第二个为错误信息，如果不存在则相应为空
	 */
	public static String[] exec(String command) {
		//存储返回结果，第一个为标准信息，第二个为错误信息
		String result[] = new String[2];
		try {
			Process process;
			Runtime runtime = Runtime.getRuntime();

			String osName = System.getProperty("os.name").toLowerCase();
			if (osName.contains("windows 9")) {
				process = runtime.exec("command.com /c " + command);
			} else if ((osName.contains("nt"))
					|| (osName.contains("windows 20"))
					|| (osName.contains("windows xp") || (osName.contains("windows vista")))) {

                /*
				 * 开关/C指明后面跟随的字符串是命令，并在执行命令后关闭DOS窗口，使用cmd /?查看帮助
                 */
				process = runtime.exec("cmd.exe /c " + command);
			} else {
				// Linux,Unix
				process = runtime.exec(command);
			}


			Object mutexInstream = new Object();
			Object mutexErrorstream = new Object();
			new ReadThread(process.getInputStream(), INPUT_STREAM, result, mutexInstream)
					.start();
			new ReadThread(process.getErrorStream(), ERROR_STREAM, result, mutexErrorstream)
					.start();
			//确保子线程已启动
			Thread.sleep(20);

            /*
			 * 这里一定要等标准流与错误都读完了后才能继续执行后面的代码，否则外面引用返回的结果可能
             * 为null或空串，所以要等两个线程执行完，这里确保读取的结果已返回。在读取时使用了两个线
             * 程，因为发现在一个线程里读这种流时，有时会阻塞，比如代码实现时先读取标准流，而运行时
             * 命令却执行失败，这时读标准流的动作会阻塞，导致程序最终挂起，先读错误流时如果执行时成
             * 功，这时又可能挂起。还有一个问题就是即使使用两个线程分别读取流，如果不使用同步锁时，也
             * 会有问题：主线程读不到子线程返回的数据，这主要是由于主线读取时子线还没未返回读取到的信
             * 息，又因为两个读线程不能互斥，但又要与主线程同步，所以使用了两个同步锁，这样两个线程谁
             * 先执行互不影响，而且主线程阻塞直到标准信息与错误信息都返回为止
             */
			synchronized (mutexInstream) {
				synchronized (mutexErrorstream) {
                    /*
                     * 导致当前线程等待，如果必要，一直要等到由该 Process 对象表示的进程已经终止
                     * 。如果已终止该子进程，此方法立即返回。如果没有终止该子进程，调用的线程将被
                     * 阻塞，直到退出子进程。
                     * process.waitFor()目的是等待子进程完成后再往下执行，不过这里好像没有什么
                     * 太大的作用除了用来判断返回的状态码外，因为如果程序进到这里表示子线程已执行完
                     * 毕，process子进程理所当然的也已执行完毕，如果子进程process未执行完，我想
                     * 读流的操作肯定会阻塞的。
                     *
                     * 另外，使用process.waitFor()要注的是一定不要在数据流读取前使用，否则线程
                     * 也会挂起，导致该现象的原因可能是该命令的输内容出比较多，而运行窗口的输出缓冲
                     * 区不够大，最后没不能写缓冲引起，所以这里先使用了两个单独的线程去读，这样不管
                     * 数据量有多大，都不会阻塞了。
                     */
					if (process.waitFor() != 0) {
						result[0] = null;
					} else {
						result[1] = null;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * 标准流与错误流读取线程
	 */
	private static class ReadThread extends Thread {
		private InputStream is;
		private String[] resultArr;
		private String type;
		private final Object mutex;

		public ReadThread(InputStream is, String type, String[] resultArr, Object mutex) {
			this.is = is;
			this.type = type;
			this.resultArr = resultArr;
			this.mutex = mutex;
		}


		public void run() {
			synchronized (mutex) {
				try {
//					int readInt = is.read();
					StringBuilder sbu = new StringBuilder();

					int idx;
					byte[] byteArr = new byte[1024 * 10];
					while ((idx = is.read(byteArr)) != -1) {

						byte[] byteData = new byte[idx];
						System.arraycopy(byteArr, 0, byteData, 0, idx);
						if (byteData.length > 1) {
							sbu.append(new String(byteData));
							logger.info(new String(byteData).replaceAll("\n", ""));
						}
					}
//
//                        /*
//                         * 这里读取时我们不要使用字符流与缓冲流，发现执行某些命令时会阻塞，不
//                         * 知道是什么原因。所有这里使用了最原始的流来操作，就不会出现问题。
//                         */
//					while (readInt != -1) {
//						result.add(Byte.valueOf(String.valueOf((byte) readInt)));
//						readInt = is.read();
//					}

//					byte[] byteArr = new byte[result.size()];
//					for (int i = 0; i < result.size(); i++) {
//						byteArr[i] = ((Byte) result.get(i)).byteValue();
//					}

					if (ERROR_STREAM.equals(this.type)) {
						resultArr[1] = sbu.toString();
					} else {
						resultArr[0] = sbu.toString();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}


	public static void main(String args[]) throws Throwable {
	}
}


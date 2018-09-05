package me.modmuss50.ftb.zombies.timer;

/**
 * Created by modmuss50 on 13/02/2017.
 */
public class Timer {

	public static long maxTime = 1200000;

	static long startSystemTime;
	static long countedTime;
	static boolean active;

	public static String getNiceTime() {
		if (!active) {
			return TimerHandler.getNiceTimeFromLong(countedTime);
		}
		return TimerHandler.getNiceTimeFromLong(getTimeDifference());
	}

	public static String getNiceTimeLeft() {
		if (!active) {
			return TimerHandler.getNiceTimeFromLong(maxTime);
		}
		return TimerHandler.getNiceTimeFromLong(maxTime - getTimeDifference());
	}

	public static long getTimeDifference() {
		return System.currentTimeMillis() - startSystemTime + countedTime;
	}

	public static void startTimer(long startTime) {
		startSystemTime = System.currentTimeMillis();
		countedTime = startTime;
		active = true;
	}

	public static void setData(Long startTime, boolean isActive) {
		startSystemTime = System.currentTimeMillis();
		countedTime = startTime;
		active = isActive;
	}

	public static boolean isActive() {
		return active;
	}
}

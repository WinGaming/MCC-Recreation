package mcc.utils;

import java.util.concurrent.TimeUnit;

import mcc.yml.decisiondome.TimerConfig;

public class Timer {

	public static Timer fromConfig(TimerConfig config) {
		return new Timer(config.getTimeunit(), config.getAmount(), config.getVisualDelay());
	}

	/** The amount of milisesconds the timer should run for */
	private long milliseconds;
	
	/** The target time as unix timestamp */
	private long activeTarget;

	private long visualDelay = 0;
	
	public Timer(TimeUnit timeunit, int amount) {
		this(timeunit, amount, 0);
	}

	public Timer(TimeUnit timeunit, int amount, long visualDelay) {
		this.milliseconds = timeunit.toMillis(amount);
		this.visualDelay = visualDelay;
	}
	
	public void start(long start) {
		this.activeTarget = start + this.milliseconds;
	}
	
	/**
	 * Returns the amount of miliseconds left.
	 * @param now The current time in miliseconds
	 * @return the amount of miliseconds left
	 */
	public long remaining(long now) {
		return activeTarget <= 0 ? 0 : activeTarget - now;
	}
	
	/**
	 * Builds and returns a String in the format HH:MM:SS or, iff no hours are left MM:SS.
	 * @param now The current time in miliseconds
	 * @return a String in the format HH:MM:SS or MM:SS
	 */
	public String buildText(long now) {
		long remain = this.remaining(now) / 1000l + this.visualDelay;
		long seconds = remain % 60;
		remain /= 60;
		long minutes = remain % 60;
		remain /= 60;
		long hours = remain;
		
		String result = "";
		if (hours >= 1) result += (hours < 10 ? "0" + hours : hours) + ":";
		/*if (minutes >= 1) */result += (minutes < 10 ? "0" + minutes : minutes) + ":";
		result += (seconds < 10 ? "0" + seconds : seconds);
		
		return result;
	}
}

package mcc.utils;

import java.util.concurrent.TimeUnit;

public class Timer {

	private long milliseconds;
	
	private long activeTarget;
	
	public Timer(TimeUnit timeunit, int amount) {
		this.milliseconds = timeunit.toMillis(amount);
	}
	
	public void start(long start) {
		this.activeTarget = start + this.milliseconds;
	}
	
	public long remaining(long now) {
		return activeTarget <= 0 ? 0 : activeTarget - now;
	}
	
	public String buildText(long now) {
		long remain = this.remaining(now) / 1000l;
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

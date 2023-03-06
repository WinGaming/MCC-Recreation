package mcc.scores;

import java.util.Optional;

public class TimeScore implements Score<TimeScore, Long> {

	private long time;

	@Override
	public String toChatText(Optional<TimeScore> viewerScore) {
		if (viewerScore.isPresent() && !viewerScore.get().equals(this)) {
			// show the difference in the format like -24.5s or +1.2s
			long difference = this.time - viewerScore.get().time;
			String sign = difference < 0 ? "-" : "+";
			difference = Math.abs(difference);

			return sign + (difference / 1000) + "." + (difference / 10 % 100) + "s";
		} else {
			return this.toScoreboardText(viewerScore);
		}
	}

	@Override
	public String toScoreboardText(Optional<TimeScore> viewerScore) {
		StringBuilder timeString = new StringBuilder();

		if (time >= 60 * 1000) {
			timeString.append(time / (60 * 1000)).append(":");
		}

		timeString.append(time / 1000 % 60).append(".");
		timeString.append(time / 10 % 100);

		return timeString.toString();
	}

	@Override
	public int compareTo(TimeScore other) {
		return Long.compare(time, other.time);
	}

	@Override
	public void setScore(Long value) {
		this.time = value;
	}
	
	@Override
	public Long getScore() {
		return this.time;
	}

	public long getTime() {
		return time;
	}
}

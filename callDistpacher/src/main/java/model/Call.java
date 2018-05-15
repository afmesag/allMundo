package model;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Call {

	private static final Integer MIN_DURATION = 5;
	private static final Integer MAX_DURATION = 10;
	private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);
	private Integer id;
	private Integer duration;

	public Call() {
		this.id = ID_GENERATOR.getAndIncrement();
		this.duration = new Random().nextInt(MAX_DURATION - MIN_DURATION + 1) + MIN_DURATION;
	}

	public Integer getId() {
		return id;
	}

	Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}
}

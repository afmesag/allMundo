package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CallTest {

	@BeforeEach
	void setUp() {
		Call.resetIdSequence();
	}

	@Test
	void getId() {
		Call call = new Call();
		assertAll(
				() -> assertEquals(1, call.getId().intValue()),
				() -> {
					for (int i = 0; i < 8; i++)
						new Call();
					Call lastCall = new Call();
					assertEquals(10, lastCall.getId().intValue());
				});
	}

	@Test
	void getDuration() {
		Call call = new Call();
		assertTrue(call.getDuration() >= 5 && call.getDuration() <= 10);
	}
}
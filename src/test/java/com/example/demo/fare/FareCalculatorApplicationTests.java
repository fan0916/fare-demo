package com.example.demo.fare;

import com.example.demo.fare.service.FareCalculateService;
import com.example.demo.fare.store.FareChargeRecordStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FareCalculatorApplicationTests {
	@Autowired
	private FareChargeRecordStore store;
	@Autowired
	private FareCalculateService calculateService;

	@Test
	void contextLoads() {
	}

	@Test
	void baseScenario() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		assertEquals(1, calculateService.calculate(100L, 1,1,sdf.parse("2022-10-03 00:00:00")));
		assertEquals(2, calculateService.calculate(100L, 1,1,sdf.parse("2022-10-03 08:00:00")));
		assertEquals(3, calculateService.calculate(100L, 1,2,sdf.parse("2022-10-15 08:00:00")));
		assertEquals(4, calculateService.calculate(100L, 1,2,sdf.parse("2022-10-15 14:00:00")));
	}

	@Test
	public void exceedCapAndRecover() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		store.store(200L, 1,1,sdf.parse("2022-10-03 12:00:00"), 7);
		assertEquals(1, calculateService.calculate(200L, 1,1,sdf.parse("2022-10-03 12:10:00")));
		assertEquals(1, calculateService.calculate(200L, 1,1,sdf.parse("2022-10-03 17:10:00")));
		assertEquals(2, calculateService.calculate(200L, 1,1,sdf.parse("2022-10-04 17:10:00")));
		store.store(200L, 1,1,sdf.parse("2022-10-03 12:05:00"), 1);
		assertEquals(0, calculateService.calculate(200L, 1,1,sdf.parse("2022-10-03 12:10:00")));
		assertEquals(0, calculateService.calculate(200L, 1,1,sdf.parse("2022-10-03 17:10:00")));

		store.store(300L, 2,1,sdf.parse("2022-09-29 12:00:00"), 88);
		assertEquals(2, calculateService.calculate(300L, 2,1,sdf.parse("2022-10-02 12:10:00")));
		assertEquals(2, calculateService.calculate(300L, 2,1,sdf.parse("2022-10-02 17:10:00")));
		store.store(300L, 2,1,sdf.parse("2022-09-30 12:05:00"), 2);
		assertEquals(0, calculateService.calculate(300L, 2,1,sdf.parse("2022-09-30 12:10:00")));
		assertEquals(0, calculateService.calculate(300L, 2,1,sdf.parse("2022-09-30 17:10:00")));
		assertEquals(2, calculateService.calculate(300L, 2,1,sdf.parse("2022-10-03 12:10:00")));
		assertEquals(3, calculateService.calculate(300L, 2,1,sdf.parse("2022-10-03 17:10:00")));
	}

}

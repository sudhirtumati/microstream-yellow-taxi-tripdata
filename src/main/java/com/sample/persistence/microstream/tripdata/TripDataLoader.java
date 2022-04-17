package com.sample.persistence.microstream.tripdata;

import com.sample.persistence.microstream.domain.DataRoot;
import com.sample.persistence.microstream.domain.TripDetails;
import com.sample.persistence.microstream.events.DataLoadCompleteEvent;
import com.sample.persistence.microstream.repository.storage.StorageManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.UUID;

@Component
@Slf4j
public class TripDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private final StorageManager storageManager;

	private final ApplicationEventPublisher applicationEventPublisher;

	private final Resource resource;

	public TripDataLoader(StorageManager storageManager, ApplicationEventPublisher applicationEventPublisher,
			@Value("classpath:yellow_tripdata_2021-07.csv") Resource resource) {
		this.storageManager = storageManager;
		this.applicationEventPublisher = applicationEventPublisher;
		this.resource = resource;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			loadTripDetails();
		}
		catch (IOException e) {
			throw new RuntimeException("Unable to load trip details csv", e);
		}
	}

	private void loadTripDetails() throws IOException {
		if (storageManager.getRoot() != null) {
			log.info("Trip data is not empty. Data loading skipped");
		}
		else {
			log.info("Trip data is empty. Loading trip data");
			storageManager.getStorageManager().setRoot(new DataRoot());
			storageManager.getStorageManager().storeRoot();
			try (Scanner scanner = new Scanner(resource.getFile())) {
				scanner.nextLine();
				int lineCount = 2;
				while (scanner.hasNextLine()) {
					String[] tripData = scanner.nextLine().split(",");
					try {
						// @formatter:off
						TripDetails tripDetails = TripDetails.builder()
								.tripId(UUID.randomUUID().toString())
								.vendorId(tripData[0])
								.pickupDateTime(LocalDateTime.parse(tripData[1], formatter))
								.dropoffDateTime(LocalDateTime.parse(tripData[2], formatter))
								.passengerCount(getByte(tripData[3]))
								.distance(getFloat(tripData[4]))
								.rateCodeId(getByte(tripData[5]))
								.storeAndFwdFlag(tripData[6].isBlank() ? '\0' : tripData[6].charAt(0))
								.pickupLocationId(tripData[7])
								.dropoffLocationId(tripData[8])
								.paymentType(getByte(tripData[9]))
								.fareAmount(getFloat(tripData[10]))
								.extraAmount(getFloat(tripData[11]))
								.mtaTax(getFloat(tripData[12]))
								.tipAmount(getFloat(tripData[13]))
								.tollAmount(getFloat(tripData[14]))
								.improvementSurcharge(getFloat(tripData[15]))
								.totalAmount(getFloat(tripData[16]))
								.congestionSurcharge(getFloat(tripData[17]))
								.build();
						// @formatter:on
						storageManager.getRoot().getTripDetails().add(tripDetails);
						lineCount++;
					}
					catch (Exception e) {
						throw new RuntimeException("An error occurred while processing line " + lineCount
								+ ": trip data: " + Arrays.toString(tripData), e);
					}
					if (lineCount % 10000 == 0) {
						storageManager.getStorageManager().store(storageManager.getRoot().getTripDetails());
						log.info("{} lines loaded", lineCount);
					}
				}
				storageManager.getStorageManager().storeRoot();
			}
			log.info("Trip data loaded successfully");
		}
		applicationEventPublisher.publishEvent(new DataLoadCompleteEvent());
	}

	private byte getByte(String str) {
		if (str == null || str.isBlank()) {
			return 0;
		}
		else {
			return Byte.parseByte(str);
		}
	}

	private float getFloat(String str) {
		if (str == null || str.isBlank()) {
			return 0;
		}
		else {
			return Float.parseFloat(str);
		}
	}

}

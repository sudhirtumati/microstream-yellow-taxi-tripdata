package com.sample.persistence.microstream.tripdata;

import com.sample.persistence.microstream.domain.TripDetails;
import com.sample.persistence.microstream.events.DataLoadCompleteEvent;
import com.sample.persistence.microstream.repository.storage.StorageManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class MetricsCalculator {

	private final StorageManager storageManager;

	public MetricsCalculator(StorageManager storageManager) {
		this.storageManager = storageManager;
	}

	@EventListener
	public void onApplicationEvent(DataLoadCompleteEvent event) {
		calculateTotalDistance();
		storageManager.getStorageManager().shutdown();
	}

	private void calculateTotalDistance() {
		log.info("Calculating total distance covered");
		BigDecimal totalDistance = storageManager.getRoot().getTripDetails().stream().map(TripDetails::getDistance)
				.map(BigDecimal::valueOf).reduce(BigDecimal.ZERO, BigDecimal::add);
		log.info("Total distance covered: " + totalDistance);
	}

}

package com.sample.persistence.microstream.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TripDetails {

    private String tripId;
    private String vendorId;
    private LocalDateTime pickupDateTime;
    private String pickupLocationId;
    private LocalDateTime dropoffDateTime;
    private String dropoffLocationId;
    private byte passengerCount;
    private float distance;
    private byte rateCodeId;
    private char storeAndFwdFlag;
    private byte paymentType;
    private float fareAmount;
    private float extraAmount;
    private float mtaTax;
    private float tipAmount;
    private float tollAmount;
    private float improvementSurcharge;
    private float totalAmount;
    private float congestionSurcharge;

}

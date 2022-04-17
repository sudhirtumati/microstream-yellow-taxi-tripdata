package com.sample.persistence.microstream.domain;

import lombok.Getter;
import lombok.Setter;
import one.microstream.reference.Lazy;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DataRoot {

	private Lazy<List<TripDetails>> tripDetails;

	public DataRoot() {
		this.tripDetails = Lazy.Reference(new ArrayList<>());
	}

	public List<TripDetails> getTripDetails() {
		return Lazy.get(this.tripDetails);
	}

}

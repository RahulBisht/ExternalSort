package com.rahul.api.comparator;

import com.rahul.api.domain.Record;

import java.util.Comparator;

public class RecordComparator implements Comparator<Record> {

	@Override
	public int compare(Record o1, Record o2) {

		return o1.getId().compareToIgnoreCase(o2.getId());
	}
}

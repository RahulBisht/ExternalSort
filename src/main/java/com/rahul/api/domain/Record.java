package com.rahul.api.domain;

import java.io.Serializable;

public class Record implements Serializable {

	private String id;
	private String name;
	private int fileIndex;

	public Record(String id, String name) {

		this.id = id;
		this.name = name;
	}

	public String getId() {

		return id;
	}

	public int getFileIndex() {

		return fileIndex;
	}

	public void setFileIndex(int fileIndex) {

		this.fileIndex = fileIndex;
	}

	public void setId(String id) {

		this.id = id;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	@Override
	public String toString() {

		return   id + " " +name;
	}
}

package com.rahul.api.sort;

import com.rahul.api.comparator.RecordComparator;
import com.rahul.api.domain.Record;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class ExternalMergeSort implements ExternalSorter {

	private static final int                READ_RECORD_THRESHOLD = 3;
	private static final String             tempDirectory         = System.getProperty("java.io.tmpdir");
	private              List<File>         tempFiles             = new ArrayList<File>();
	private final        Comparator<Record> sorter                = new RecordComparator();

	@Override
	public void sortExternalSource(File input, File outputFile) throws IOException {

		if (input == null || input.isDirectory() || outputFile==null || outputFile.isDirectory()) {
			throw new IllegalArgumentException("Illegal arguments passed to method");
		}
		splitInputFile(input);
		mergeTemporaryFiles(outputFile);

	}

	private void splitInputFile(File in) throws IOException {

		tempFiles.clear();
		BufferedReader br = null;
		List<Record> records = new ArrayList<Record>();

		try {

			br = new BufferedReader(new InputStreamReader(new FileInputStream(in)));
			String line;
			int currChunkSize = 0;
			while ((line = br.readLine()) != null) {

				String[] splitStr = line.split("\\s+");
				Record record = new Record(splitStr[0], splitStr[1]);
				records.add(record);
				currChunkSize += line.length() + 1;

				if (currChunkSize >= READ_RECORD_THRESHOLD) {

					currChunkSize = 0;
					Collections.sort(records, sorter);
					File file = new File(tempDirectory + "temp" + System.currentTimeMillis());
					tempFiles.add(file);
					writeToTempFile(records, new FileOutputStream(file));
					records.clear();
				}
			}
			Collections.sort(records, sorter);
			File file = new File(tempDirectory + "temp" + System.currentTimeMillis());
			tempFiles.add(file);
			writeToTempFile(records, new FileOutputStream(file));
			records.clear();
		} catch (IOException io) {

			throw io;
		} finally {

			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
				}
			}

		}

	}

	private void writeToTempFile(List<Record> records, OutputStream os) throws IOException {

		BufferedWriter writer = null;
		try {

			writer = new BufferedWriter(new OutputStreamWriter(os));
			for (Record s : records) {
				writer.write(s.getId() + " " + s.getName());
				writer.write("\r\n");
			}
			writer.flush();

		} catch (IOException io) {

			throw io;

		} finally {

			if (writer != null) {

				try {
					writer.close();
				} catch (Exception e) {
				}
			}
		}
	}

	private void mergeTemporaryFiles(File outputFile) throws IOException {

		List<BufferedReader> readers = new ArrayList<BufferedReader>();
		BufferedWriter writer = null;
		try {

			PriorityQueue<Record> priorityQueue = new PriorityQueue<Record>(tempFiles.size(), sorter);
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outputFile, false)));

			for (int i = 0; i < tempFiles.size(); i++) {

				BufferedReader reader = new BufferedReader(new FileReader(tempFiles.get(i)));
				readers.add(reader);
				String line = reader.readLine();
				if (line != null) {
					String[] splitStr = line.split("\\s+");
					Record record = new Record(splitStr[0], splitStr[1]);
					record.setFileIndex(i);
					priorityQueue.add(record);
				}
			}

			while (priorityQueue.size() > 0) {

				Record record = priorityQueue.poll();
				writer.write(record.toString());
				writer.write("\r\n");
				String nextLine = readers.get(record.getFileIndex()).readLine();

				if (nextLine != null) {
					String[] splitStr = nextLine.split("\\s+");
					Record recordToQueue = new Record(splitStr[0], splitStr[1]);
					record.setFileIndex(record.getFileIndex());
					priorityQueue.add(recordToQueue);
				}
			}
			writer.flush();
		} catch (IOException io) {

			throw io;
		} finally {

			for (int i = 0; i < readers.size(); i++) {

				try {
					readers.get(i).close();
					tempFiles.get(i).delete();
				} catch (Exception e) {
				}
			}

			try {
				writer.close();
			} catch (Exception e) {
			}
		}
	}
}
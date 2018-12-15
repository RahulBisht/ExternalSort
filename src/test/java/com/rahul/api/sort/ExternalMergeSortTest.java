package com.rahul.api.sort;

import com.rahul.api.domain.Record;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ExternalMergeSortTest {

	private ExternalSorter externalSorter;

	@Before
	public void setup() {

		externalSorter = new ExternalMergeSort();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExternalSortingForDirectoryInput() throws Exception {

		File output = new File(System.getProperty("java.io.tmpdir") + "output.txt");
		externalSorter.sortExternalSource(null, output);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExternalSortingForDirectoryOutput() throws Exception {

		URL url = this.getClass().getClassLoader().getResource("com");
		externalSorter.sortExternalSource(new File(url.getPath()), null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExternalSortingForNullInput() throws Exception {

		File output = new File(System.getProperty("java.io.tmpdir"));
		externalSorter.sortExternalSource(null, output);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExternalSortingForNullOutput() throws Exception {

		URL url = this.getClass().getClassLoader().getResource("rahul.txt");
		externalSorter.sortExternalSource(new File(url.getPath()), null);
	}

	@Test
	public void testExternalSorting() throws Exception {

		URL url = this.getClass().getClassLoader().getResource("rahul.txt");
		File output = new File(System.getProperty("java.io.tmpdir") + "output.txt");
		externalSorter.sortExternalSource(new File(url.getPath()), output);
		assert (output.exists());

		BufferedReader br = null;
		;

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(output)));
			String line;
			List<Record> records = new ArrayList<>();
			while ((line = br.readLine()) != null) {
				String[] splitStr = line.split("\\s+");
				Record record = new Record(splitStr[0], splitStr[1]);
				records.add(record);
			}

			assert (records.get(0).getId().equals("11"));
			assert (records.get(0).getName().equals("djfsd"));
			assert (records.get(1).getId().equals("19"));
			assert (records.get(1).getName().equals("jdf"));
			assert (records.get(2).getId().equals("20"));
			assert (records.get(2).getName().equals("vishal"));
			assert (records.get(3).getId().equals("21"));
			assert (records.get(3).getName().equals("siodids"));
			assert (records.get(4).getId().equals("22"));
			assert (records.get(4).getName().equals("sdfdf"));
			assert (records.get(5).getId().equals("30"));
			assert (records.get(5).getName().equals("rahul"));

		} finally {

			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
				}
			}
		}
	}
}

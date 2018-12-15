package com.rahul.api.sort;

import java.io.File;
import java.io.IOException;

public interface ExternalSorter {

	void sortExternalSource(File input, File output)throws IOException;
}

package com.example.csvreader;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CsvReaderApplication {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		// file from classpath, resources folder
		String filename = "SalesJan2009.csv";
		JFrame frame = createFrame();
		List<List<String>> dataList = readFile(filename);
		JTable table = createJTable(dataList);
		JScrollPane pane = new JScrollPane(table);
		frame.add(pane);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		SpringApplication.run(CsvReaderApplication.class, args);

	}

	private static JFrame createFrame() {
		JFrame frame = new JFrame("JTable example");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(600, 300));
		return frame;
	}

	private static List<List<String>> readFile(String filename) throws FileNotFoundException, IOException {
		File file = getFileFromResources(filename);
		List<List<String>> records = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] values = line.split(";");
				records.add(Arrays.asList(values));
			}
		}
		return records;
	}

	private static JTable createJTable(List<List<String>> data) {
		Integer numColumns = data.get(0).size() - 1;
		Integer numRows = data.size();
		String[] headers = new String[numColumns];
		Object[][] rows = new Object[numRows][numColumns];
		for (int x = 0; x < numRows; x++) {
			for (int y = 0; y < numColumns; y++) {
				// Row = 0 => headers
				if (0 == x) {
					headers[y] = data.get(x).get(y);
				} else {
					List<String> tmpRow = data.get(x);
					rows[x - 1][y] = tmpRow.get(y);
				}
			}
		}
		JTable table = new JTable(rows, headers);
		return table;
	}

	private static File getFileFromResources(String fileName) {

		ClassLoader classLoader = new CsvReaderApplication().getClass().getClassLoader();

		URL resource = classLoader.getResource(fileName);
		if (resource == null) {
			throw new IllegalArgumentException("file is not found!");
		} else {
			return new File(resource.getFile());
		}

	}
}

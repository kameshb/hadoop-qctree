package com.imaginea.qctree;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

/**
 * Table represents either a plain text file or a database table. It has table
 * headers and rows. It represents an in-memory representation of a part of an
 * external table. Caution should be taken while building qc-tree on memory
 * constraints.
 */
public class Table {

  private static final Table baseTable;

  private List<String> dimensions;
  private List<String> measures;

  static {
    Gson gson = new Gson();
    InputStream tableJson = Thread.currentThread().getContextClassLoader()
        .getResourceAsStream("table.json");
    InputStreamReader reader = new InputStreamReader(tableJson);
    baseTable = gson.fromJson(reader, Table.class);
    try {
      reader.close();
    } catch (IOException e) {
      System.err.println("Exception while closing the reader.");
    }
  }
  @Expose
  private final List<Row> rows;

  private Table() {
    rows = new LinkedList<Row>();
  }

  public List<String> getDimensionHeaders() {
    return Collections.unmodifiableList(dimensions);
  }

  public String getDimensionHeaderAt(int idx) {
    return dimensions.get(idx);
  }

  public List<String> getMeasureHeaders() {
    return Collections.unmodifiableList(measures);
  }

  public static Table getTable() {
    return baseTable;
  }

  public void addRow(Row row) {
    rows.add(row);
  }

  public List<Row> getRows() {
    return Collections.unmodifiableList(rows);
  }

  public void clear() {
    rows.clear();
  }
}

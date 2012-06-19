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
 * headers and rows.
 * 
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
  private final List<Cell> rows;

  private Table() {
    rows = new LinkedList<Cell>();
  }

  public List<String> getDimensionHeaders() {
    return Collections.unmodifiableList(dimensions);
  }

  public List<String> getMeasureHeaders() {
    return Collections.unmodifiableList(measures);
  }

  public static Table getTable() {
    return baseTable;
  }

  public void addRow(Cell row) {
    rows.add(row);
  }

  public List<Cell> getRows() {
    return Collections.unmodifiableList(rows);
  }
}

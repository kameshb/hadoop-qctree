package com.imaginea.qctree;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Partition {

  private Map<Integer, Set<String>> dimensions;
  private List<Row> rows = new LinkedList<Row>();

  private static Table baseTable = Table.getTable();
  private static final int DC = baseTable.getDimensionHeaders().size();
  private static final int MC = baseTable.getMeasureHeaders().size();

  private Partition(Map<Integer, Set<String>> dimensions) {
    this.dimensions = dimensions;
  }

  public List<Row> getRows() {
    return rows;
  }

  public static Partition inducedBy(Cell cell) {
    Partition partition = inducedBy(cell, baseTable.getRows());
    // We can safely wipe the table, as we have calculated base partition. 
    // This will reduce the memory foot print.
    baseTable.clear();
    return partition;
  }
  
  public static Partition inducedBy(Cell cell, List<Row> rowList) {
    Map<Integer, Set<String>> dims = new LinkedHashMap<Integer, Set<String>>(DC);
    List<Row> rows = new LinkedList<Row>();

    for (Row row : rowList) {
      if (cell.equals(Cell.ROOT) || cell.covers(row)) {
        rows.add(row);
        String[] colValues = row.getDimensions();
        Set<String> uniqDims;
        for (int i = 0; i < colValues.length; ++i) {
          if (dims.get(Integer.valueOf(i)) == null) {
            uniqDims = new LinkedHashSet<String>();
            dims.put(Integer.valueOf(i), uniqDims);
          }
          dims.get(Integer.valueOf(i)).add(colValues[i]);
        }
      }
    }
    Partition partition = new Partition(dims);
    partition.rows = rows;
    return partition;
  }

  public Map<Integer, List<Double>> getMeasures() {
    Map<Integer, List<Double>> m = new LinkedHashMap<Integer, List<Double>>(MC);
    for (Row row : rows) {
      double[] measures = row.getMeasures();
      List<Double> meas;
      for (int i = 0; i < measures.length; ++i) {
        if (m.get(Integer.valueOf(i)) == null) {
          meas = new LinkedList<Double>();
          m.put(Integer.valueOf(i), meas);
        }
        m.get(Integer.valueOf(i)).add(measures[i]);
      }
    }
    return Collections.unmodifiableMap(m);
  }

  public Set<String> getUniqueColumnValues(int colIndex) {
    return dimensions.get(Integer.valueOf(colIndex));
  }

  public boolean isEmpty() {
    return rows.size() == 0;
  }

  public void clear() {
    rows.clear();
    dimensions.clear();
  }
  
}

package com.imaginea.qctree;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Partition {

  private List<Row> rows;
  private Map<Integer, Set<String>> cols;

  private static Table baseTable = Table.getTable();

  public Partition(List<Row> rows, Map<Integer, Set<String>> cols) {
    this.rows = rows;
    this.cols = cols;
  }

  public static Partition inducedBy(Cell cell) {
    List<Row> rows = new LinkedList<Row>();
    Map<Integer, Set<String>> cols = new LinkedHashMap<Integer, Set<String>>();
    for (Row row : baseTable.getRows()) {
      if (cell.covers(row)) {
        rows.add(row);
        String[] colValues = row.getDimensions();
        Set<String> colList;
        for (int i = 0; i < colValues.length; ++i) {
          if (cols.get(Integer.valueOf(i)) == null) {
            colList = new LinkedHashSet<String>();
            cols.put(Integer.valueOf(i), colList);
          }
          cols.get(Integer.valueOf(i)).add(colValues[i]);
        }
      }
    }
    return new Partition(rows, cols);
  }

  public List<Row> getBaseCells() {
    return Collections.unmodifiableList(rows);
  }

  public Set<String> getUniqueColumnValues(int colIndex) {
    return cols.get(Integer.valueOf(colIndex));
  }

  public boolean isEmpty() {
    return rows.size() == 0;
  }

}

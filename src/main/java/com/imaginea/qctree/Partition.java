package com.imaginea.qctree;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Partition {

  private List<Row> baseCells;
  private Map<Integer, Set<String>> baseCols;

  private static Table baseTable = Table.getTable();

  public Partition(List<Row> cells, Map<Integer, Set<String>> baseCols) {
    this.baseCells = cells;
    this.baseCols = baseCols;
  }

  public static Partition inducedBy(Cell cell) {
    List<Row> rows = new LinkedList<Row>();
    Map<Integer, Set<String>> cols = new LinkedHashMap<Integer, Set<String>>();
    for (Row row : baseTable.getRows()) {
      if (cell.compareTo(row) == 0) {
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
    return Collections.unmodifiableList(baseCells);
  }

  public Set<String> getUniqueColumnValues(int colIndex) {
    return baseCols.get(Integer.valueOf(colIndex));
  }

  public boolean isEmpty() {
    return baseCells.size() == 0;
  }

}

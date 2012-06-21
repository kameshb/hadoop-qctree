package com.imaginea.qctree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QCCube {

  private List<Class> classes;
  private int classId;

  public QCCube() {
    classes = new ArrayList<Class>();
  }

  public List<Class> getClasses() {
    return Collections.unmodifiableList(classes);
  }

  public void construct() {
    Table table = Table.getTable();
    String[] rootStr = new String[table.getDimensionHeaders().size()];
    for (int i = 0; i < rootStr.length; ++i) {
      rootStr[i] = Cell.DIMENSION_VALUE_ANY;
    }
    Cell root = new Cell(rootStr);
    Partition base = new Partition(table.getRows(), table.getColumns());
    DFS(root, base, 0, -1);
  }

  private void DFS(Cell cell, Partition partition, int k, int chdID) {
    Class clazz = new Class(partition);
    clazz.computeAggregateAndGet();
    Cell ub = clazz.upperBoundOf(cell);
    clazz.setLowerBound(new Cell(cell));
    clazz.setClassID(classId);
    clazz.setChildID(chdID);
    ++classId;
    classes.add(clazz);

    for (int j = 0; j < k; ++j) {
      if (cell.getDimensionAt(j) == Cell.DIMENSION_VALUE_ANY
          && ub.getDimensionAt(j) != Cell.DIMENSION_VALUE_ANY) {
        return;
      }
    }

    for (int j = k; j < Table.getTable().getDimensionHeaders().size(); ++j) {
      Cell c = new Cell(ub);
      if (c.getDimensionAt(j) != Cell.DIMENSION_VALUE_ANY) {
        continue;
      }
      for (String column : partition.getUniqueColumnValues(j)) {
        c.setDimensionAt(j, column);
        Partition part = Partition.inducedBy(c);
        if (!part.isEmpty()) {
          DFS(c, part, j, clazz.getClassID());
        }
      }
    }
  }
}

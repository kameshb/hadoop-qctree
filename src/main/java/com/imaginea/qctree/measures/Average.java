package com.imaginea.qctree.measures;

import java.util.List;

import com.imaginea.qctree.Cell;
import com.imaginea.qctree.Row;

public class Average implements Aggregable {

  @Override
  public Double aggregate(List<Row> rows, Cell ub) {
    double sum = 0;
    int noOfEntries = 0;
    for (Row row : rows) {
      if (row.compareTo(ub) != 0) {
        continue;
      }
      for (Double value : row.getMeasures()) {
        sum += value;
        ++noOfEntries;
      }
    }
    return sum / noOfEntries;
  }

}

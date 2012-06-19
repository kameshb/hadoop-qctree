package com.imaginea.qctree.measures;

import java.util.List;

import com.imaginea.qctree.Cell;

public class Average implements Aggregable {

  @Override
  public Double aggregate(List<Cell> cells, Cell ub) {
    double sum = 0;
    int noOfEntries = 0;
    for (Cell cell : cells) {
      if (cell.compareTo(ub) != 0) {
        continue;
      }
      for (Double value : cell.getMeasures().values()) {
        sum += value;
        ++noOfEntries;
      }
    }
    return sum / noOfEntries;
  }
}

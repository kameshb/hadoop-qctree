package com.imaginea.qctree.measures;

import java.util.List;

import com.imaginea.qctree.Row;

public class Average implements Aggregable {

  @Override
  public Double aggregate(List<Row> rows) {
    double sum = 0;
    int noOfEntries = 0;
    for (Row row : rows) {
      for (Double value : row.getMeasures()) {
        sum += value;
        ++noOfEntries;
      }
    }
    return sum / noOfEntries;
  }

}

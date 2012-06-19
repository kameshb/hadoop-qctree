package com.imaginea.qctree.measures;

import java.util.List;

import com.imaginea.qctree.Cell;
import com.imaginea.qctree.Row;

public interface Aggregable {
  public Double aggregate(List<Row> rows, Cell ub);
}

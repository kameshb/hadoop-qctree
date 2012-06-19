package com.imaginea.qctree.measures;

import java.util.List;

import com.imaginea.qctree.Cell;

public interface Aggregable {
  public Double aggregate(List<Cell> cells, Cell ub);
}

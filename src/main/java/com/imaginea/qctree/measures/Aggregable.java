package com.imaginea.qctree.measures;

import java.util.List;

import org.apache.hadoop.io.Writable;

import com.imaginea.qctree.Row;

public interface Aggregable extends Writable {
  public void aggregate(List<Row> rows);

  public double getAggregateValue();
}

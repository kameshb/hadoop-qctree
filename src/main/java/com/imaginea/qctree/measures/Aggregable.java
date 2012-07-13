package com.imaginea.qctree.measures;

import java.util.List;

import org.apache.hadoop.io.Writable;

public interface Aggregable extends Writable {
  public void aggregate(List<Double> measures);

  public double getAggregateValue();
  
  public void accumalate(Aggregable other);
}

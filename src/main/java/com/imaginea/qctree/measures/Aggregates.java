package com.imaginea.qctree.measures;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.Writable;

import com.imaginea.qctree.Row;

public class Aggregates implements Writable {

  private final List<Aggregable> aggregates;

  public Aggregates() {
    aggregates = new ArrayList<Aggregable>();
    aggregates.add(new Average());
  }

  public void addAggregate(Aggregable aggr) {
    aggregates.add(aggr);
  }

  public void compute(List<Row> rows) {
    for (Aggregable aggr : aggregates) {
      aggr.aggregate(rows);
    }
  }

  public List<Aggregable> get() {
    return aggregates;
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    for (Aggregable aggr : aggregates) {
      aggr.readFields(in);
    }
  }

  @Override
  public void write(DataOutput out) throws IOException {
    for (Aggregable aggr : aggregates) {
      aggr.write(out);
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Aggregable aggr : aggregates) {
      sb.append(aggr.getClass().getSimpleName());
      sb.append('=').append(aggr.toString()).append('\n');
    }
    return sb.toString();
  }

}

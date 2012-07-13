package com.imaginea.qctree.measures;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.io.Writable;

public class Aggregates implements Writable {

  private final Map<String, Aggregable> aggregates;

  public Aggregates() {
    aggregates = new LinkedHashMap<String, Aggregable>(5);
    aggregates.put(Average.class.getSimpleName(), new Average());
    aggregates.put(Sum.class.getSimpleName(), new Sum());
    aggregates.put(Maximum.class.getSimpleName(), new Maximum());
    aggregates.put(Minimum.class.getSimpleName(), new Minimum());
    aggregates.put(Count.class.getSimpleName(), new Count());
  }

  public void addAggregate(Aggregable aggr) {
    aggregates.put(aggr.getClass().getSimpleName(), aggr);
  }

  public void compute(Map<Integer, List<Double>> measures) {
    for (Aggregable aggr : aggregates.values()) {
      for (Entry<Integer, List<Double>> measure : measures.entrySet()) {
        aggr.aggregate(measure.getValue());
      }
    }
  }

  public Map<String, Aggregable> get() {
    return aggregates;
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    for (Aggregable aggr : aggregates.values()) {
      aggr.readFields(in);
    }
  }

  @Override
  public void write(DataOutput out) throws IOException {
    for (Aggregable aggr : aggregates.values()) {
      aggr.write(out);
    }
  }

  public void accumalate(Aggregates other) {
    for (Entry<String, Aggregable> aggr : aggregates.entrySet()) {
      Aggregable otherAggr = other.aggregates.get(aggr.getKey());
      aggr.getValue().accumalate(otherAggr);
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Entry<String, Aggregable> aggr : aggregates.entrySet()) {
      sb.append(aggr.getKey()).append('=');
      sb.append(aggr.getValue().toString());
      sb.append('\n');
    }
    return sb.substring(0, sb.length() - 1);
  }
}

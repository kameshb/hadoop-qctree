package com.imaginea.qctree.measures;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.WritableUtils;

import com.imaginea.qctree.Row;

public class Average implements Aggregable {

  private static final Log LOG = LogFactory.getLog(Average.class);

  private int noOfrows;
  private double sum;

  @Override
  public void aggregate(List<Row> rows) {
    LOG.info("Computing Average Aggregate");

    double sum = 0;
    int noOfEntries = 0;
    for (Row row : rows) {
      for (Double value : row.getMeasures()) {
        sum += value;
        ++noOfEntries;
      }
    }
    this.sum = sum;
    this.noOfrows = noOfEntries;
  }

  @Override
  public void readFields(DataInput in) throws IOException {
    noOfrows = WritableUtils.readVInt(in);
    sum = in.readDouble();
  }

  @Override
  public void write(DataOutput out) throws IOException {
    WritableUtils.writeVInt(out, noOfrows);
    out.writeDouble(sum);
  }

  @Override
  public void accumalate(Aggregable other) {
    Average otherAvg = (Average) other;
    this.sum += otherAvg.sum;
    this.noOfrows += otherAvg.noOfrows;
  }

  @Override
  public double getAggregateValue() {
    return sum / noOfrows;
  }

  @Override
  public String toString() {
    return String.valueOf(sum / noOfrows);
  }

}

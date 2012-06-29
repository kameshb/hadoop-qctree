package com.imaginea.qctree.measures;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.WritableUtils;

import com.imaginea.qctree.Row;

public class Average implements Aggregable {

  private int noOfrows;
  private double sum; 

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
    this.sum = sum;
    this.noOfrows = noOfEntries;
    return sum / noOfEntries;
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

}

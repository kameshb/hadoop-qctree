package com.imaginea.qctree.measures;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.imaginea.qctree.Row;

public class Sum implements Aggregable {

  private static final Log LOG = LogFactory.getLog(Sum.class);
  private double sum;

  @Override
  public void readFields(DataInput in) throws IOException {
    sum = in.readDouble();
  }

  @Override
  public void write(DataOutput out) throws IOException {
    out.writeDouble(sum);
  }

  @Override
  public void aggregate(List<Row> rows) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Computing Sum Aggregate");
    }
    double sum = 0;
    for (Row row : rows) {
      for (double value : row.getMeasures()) {
        sum += value;
      }
    }
    this.sum = sum;
  }

  @Override
  public double getAggregateValue() {
    return sum;
  }

  @Override
  public void accumalate(Aggregable other) {
    Sum otherSum = (Sum) other;
    this.sum += otherSum.sum;
  }

  @Override
  public String toString() {
    return String.valueOf(sum);
  }

}

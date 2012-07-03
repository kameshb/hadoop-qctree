package com.imaginea.qctree.measures;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.imaginea.qctree.Row;

public class Minimum implements Aggregable {

  private static final Log LOG = LogFactory.getLog(Minimum.class);
  private double min = Double.MAX_VALUE;

  @Override
  public void readFields(DataInput in) throws IOException {
    min = in.readDouble();
  }

  @Override
  public void write(DataOutput out) throws IOException {
    out.writeDouble(min);
  }

  @Override
  public void aggregate(List<Row> rows) {
    LOG.info("Computing Minimum Aggregate");

    for (Row row : rows) {
      for (double value : row.getMeasures()) {
        this.min = Math.min(this.min, value);
      }
    }
  }

  @Override
  public double getAggregateValue() {
    return this.min;
  }

  @Override
  public void accumalate(Aggregable other) {
    Minimum otherMin = (Minimum) other;
    this.min = Math.min(this.min, otherMin.min);
  }

  @Override
  public String toString() {
    return String.valueOf(this.min);
  }

}

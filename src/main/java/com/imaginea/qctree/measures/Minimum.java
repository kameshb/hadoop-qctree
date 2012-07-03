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
  private double min;

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

    double min = Double.MAX_VALUE;
    for (Row row : rows) {
      for (double value : row.getMeasures()) {
        if (value < min) {
          min = value;
        }
      }
    }
    this.min = min;
  }

  @Override
  public double getAggregateValue() {
    return min;
  }

  @Override
  public void accumalate(Aggregable other) {
    Minimum otherMin = (Minimum) other;
    this.min = Math.min(this.min, otherMin.min);
  }
  
  @Override
  public String toString() {
    return String.valueOf(min);
  }

}

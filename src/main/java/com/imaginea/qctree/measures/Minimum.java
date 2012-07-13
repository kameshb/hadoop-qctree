package com.imaginea.qctree.measures;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
  public void aggregate(List<Double> measures) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Computing Minimum Aggregate");
    }
    for (double value : measures) {
      this.min = Math.min(this.min, value);
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

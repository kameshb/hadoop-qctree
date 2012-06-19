package com.imaginea.qctree;

public class Row extends Cell{

  private final double[] measures;
  
  public Row(String[] dims, double[] ms) {
    super(dims);
    measures = new double[ms.length];
    System.arraycopy(ms, 0, measures, 0, ms.length);
  }
  
  public double[] getMeasures() {
    return measures;
  }
  
  @Override
  public String toString() {
    return super.toString();
  }
}

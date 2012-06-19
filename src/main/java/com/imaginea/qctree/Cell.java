package com.imaginea.qctree;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A Cell is a set of dimensions.
 */
public class Cell implements Comparable<Cell> {

  public static final String DIMENSION_VALUE_ANY = "*";

  private final Map<String, String> dimensions;
  private int dimCount;
  private final int numOfDim;
  private final Map<String, Double> measures;
  private int measureCount;
  private final int numOfMeasures;

  public Cell(int numOfDim) {
    this(numOfDim, 0);
  }

  public Cell(int numOfDim, int numOfMeasures) {
    this.numOfDim = numOfDim;
    dimensions = new LinkedHashMap<String, String>(numOfDim);
    this.numOfMeasures = numOfMeasures;
    measures = new LinkedHashMap<String, Double>(numOfMeasures);
  }

  public void addDimension(String dimName, String dimValue) {
    if (++dimCount > numOfDim) {
      throw new RuntimeException("Can't add beyond " + numOfDim + " entries.");
    }
    dimensions.put(dimName, dimValue);
  }

  public void addMeasure(String measureName, Double measureValue) {
    if (++measureCount > numOfMeasures) {
      throw new RuntimeException("Can't add beyond " + numOfMeasures
          + " entries.");
    }
    measures.put(measureName, measureValue);
  }

  public void setDimension(String dimName, String dimValue) {
    if (dimensions.get(dimName) == null) {
      throw new RuntimeException("Can't modify value of a non existant key");
    }
    dimensions.put(dimName, dimValue);
  }

  public Map<String, String> getDimensions() {
    return Collections.unmodifiableMap(dimensions);
  }

  public Map<String, Double> getMeasures() {
    return Collections.unmodifiableMap(measures);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[ ");
    for (Entry<String, String> dim : dimensions.entrySet()) {
      sb.append(dim.getKey()).append("=").append(dim.getValue()).append(' ');
    }
    for (Entry<String, Double> m : measures.entrySet()) {
      sb.append(m.getKey()).append("=").append(m.getValue()).append(' ');
    }
    sb.append("]");
    return sb.toString();
  }

  @Override
  public int compareTo(Cell that) {
    int diff = 0;
    Iterator<String> thatItr = that.dimensions.values().iterator();
    Iterator<String> thisItr = this.dimensions.values().iterator();

    while (thatItr.hasNext()) {
      String thatDim = thatItr.next();
      String thisDim = thisItr.next();
      if (thatDim != DIMENSION_VALUE_ANY && thisDim != DIMENSION_VALUE_ANY) {
        diff = thatDim.compareTo(thisDim);
        if (diff != 0) {
          break;
        }
      }
    }
    return diff;
  }

}

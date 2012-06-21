package com.imaginea.qctree;

/**
 * A Cell is a representation of a set dimension values. Example: (a1, b1, c1)
 * is a cell. Here a1,b1 and c1 are dimension values corresponding to dimensions
 * a, b and c. These dimensions are ordered as per the definition given in
 * table.json. So, the value of a cell at 0th index (Here a1) always corresponds
 * to dimension a.
 */

public class Cell implements Comparable<Cell> {

  public static final String DIMENSION_VALUE_ANY = "*";

  protected final String[] dimensions;

  public Cell(String[] dims) {
    dimensions = new String[dims.length];
    System.arraycopy(dims, 0, dimensions, 0, dims.length);
  }

  public Cell(Cell cell) {
    dimensions = new String[cell.dimensions.length];
    System.arraycopy(cell.dimensions, 0, dimensions, 0, dimensions.length);
  }

  public void setDimensionAt(int index, String dimValue) {
    dimensions[index] = dimValue;
  }

  public String[] getDimensions() {
    return dimensions;
  }

  public String getDimensionAt(int index) {
    return dimensions[index];
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[ ");
    for (String dim : dimensions) {
      sb.append(dim).append(' ');
    }
    sb.append("]");
    return sb.toString();
  }

  @Override
  public int compareTo(Cell that) {
    int diff = 0;
    for (int idx = 0; idx < dimensions.length; ++idx) {
      if (that.dimensions[idx] != DIMENSION_VALUE_ANY
          && dimensions[idx] != DIMENSION_VALUE_ANY) {
        diff = that.dimensions[idx].compareTo(dimensions[idx]);
        if (diff != 0) {
          break;
        }
      }
    }
    return diff;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (obj.getClass() != this.getClass()) {
      return false;
    }
    Cell that = (Cell) obj;
    for (int idx = 0; idx < dimensions.length; ++idx) {
      if (!that.dimensions[idx].equals(this.dimensions[idx])) {
        return false;
      }
    }
    return true;
  }

}

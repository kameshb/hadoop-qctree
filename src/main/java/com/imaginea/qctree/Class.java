package com.imaginea.qctree;

import static com.imaginea.qctree.Cell.DIMENSION_VALUE_ANY;

import java.util.Set;

import com.imaginea.qctree.measures.Aggregates;

public class Class implements Comparable<Class> {
  private int clsID;
  private Class child;
  private Cell ub;
  private Cell lb;
  private Aggregates aggregates = new Aggregates();

  private Partition partition;

  public Class(Partition partition) {
    this.partition = partition;
  }

  public Class() {
  }

  public void setClassID(int clsID) {
    this.clsID = clsID;
  }

  public int getClassID() {
    return this.clsID;
  }

  public void setChild(Class child) {
    this.child = child;
  }

  public Class getChild() {
    return this.child;
  }

  public void setLowerBound(Cell lb) {
    this.lb = lb;
  }

  public Cell getLowerBound() {
    return this.lb;
  }

  public void setUpperBound(Cell ub) {
    this.ub = ub;
  }

  public Cell getUpperBound() {
    return this.ub;
  }

  public Aggregates getAggregates() {
    return this.aggregates;
  }

  public void computeAggregates() {
    aggregates.compute(partition.getMeasures());
  }

  private String getDimensionValueAt(int colIndex) {
    Set<String> values = partition.getUniqueColumnValues(colIndex);
    return values.size() == 1 ? values.iterator().next() : DIMENSION_VALUE_ANY;
  }

  /**
   * Upper bound of a partition will be computed as follows.
   * 
   * 1. For any dimension, if the value of that dimension is non-*, upper bound
   * also will have the same dimension value as that of the input cell.
   * 2.Otherwise, for all the dimensions, for which input cell has value *, we
   * scan all the base cells in the partition, and if there is any value (x)
   * repeated in the partition, we replace the value of the ith dimension of
   * upper bound by repeated value (x).
   * 
   * Ex: For the partition {(a2,b1,c1),(a2,b2,c1),(a1,b1,c2)}, upper bound of
   * the cell (a2, *, *) is (a2, *, c1). Here c1 has appeared in all the tuples
   * but not b1.
   * 
   * @param cell
   *          input cell
   * @return Upper bound of the partition.
   * @throws CloneNotSupportedException
   */
  public Cell upperBoundOf(Cell cell) {
    if (ub != null) {
      return ub;
    }
    ub = new Cell(cell);
    for (int colIndex = 0; colIndex < ub.getDimensions().length; ++colIndex) {
      if (ub.getDimensions()[colIndex] != DIMENSION_VALUE_ANY) {
        continue;
      }
      ub.setDimensionAt(colIndex, getDimensionValueAt(colIndex));
    }
    return ub;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hashcode = 1;
    hashcode = hashcode * prime + lb.hashCode();
    hashcode = hashcode * prime + ub.hashCode();
    return hashcode;
  }

  /*
   * None of the temporary Classes will have the same upper bound and lower
   * bound. So it is safe to check those two properties.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (obj.getClass() != this.getClass()) {
      return false;
    }
    Class that = (Class) obj;

    if (!that.lb.equals(this.lb) || !that.ub.equals(this.ub)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ID : ").append(clsID).append(' ');
    sb.append("Upper Bound : ").append(ub).append(' ');
    sb.append("Lower Bound : ").append(lb).append(' ');
    int chdId = child == null ? -1 : child.getClassID();
    sb.append("Lattice Child : ").append(chdId).append(' ');
    return sb.toString();
  }

  @Override
  public int compareTo(Class that) {
    int diff = this.ub.compareTo(that.ub);
    return diff == 0 ? 1 : diff;
  }

}

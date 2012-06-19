package com.imaginea.qctree;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class TestPartition {

  private Partition partition;

  @Before
  public void setUp() throws Exception {
    Cell cell1 = new Cell(4, 1);
    cell1.addDimension("a", "a2");
    cell1.addDimension("b", "b1");
    cell1.addDimension("c", "c1");
    cell1.addDimension("d", "d1");
    cell1.addMeasure("m", 6d);

    Cell cell2 = new Cell(4, 1);
    cell2.addDimension("a", "a2");
    cell2.addDimension("b", "b1");
    cell2.addDimension("c", "c2");
    cell2.addDimension("d", "d1");
    cell2.addMeasure("m", 12d);

    Cell cell3 = new Cell(4, 1);
    cell3.addDimension("a", "a1");
    cell3.addDimension("b", "b2");
    cell3.addDimension("c", "c2");
    cell3.addDimension("d", "d2");
    cell3.addMeasure("m", 9d);

    List<Cell> cells = new LinkedList<Cell>();
    cells.add(cell1);
    cells.add(cell2);
    cells.add(cell3);
    partition = new Partition("i0", cells);
  }

  @Test
  public void shouldReturnUpperBound() throws Exception {
    Cell cell = new Cell(4);
    cell.addDimension("a", "a2");
    cell.addDimension("b", Cell.DIMENSION_VALUE_ANY);
    cell.addDimension("c", Cell.DIMENSION_VALUE_ANY);
    cell.addDimension("d", Cell.DIMENSION_VALUE_ANY);

    Cell ub = partition.upperBoundOf(cell);
    for (Entry<String, String> dim : ub.getDimensions().entrySet()) {
      if (dim.getKey() == "a") {
        Assert.assertEquals("Value should be a2", "a2", dim.getValue());
      }
      if (dim.getKey() == "b") {
        Assert.assertEquals("Value should be b1", "b1", dim.getValue());
      }
      if (dim.getKey() == "c") {
        Assert.assertEquals("Value should be *", "*", dim.getValue());
      }
      if (dim.getKey() == "d") {
        Assert.assertEquals("Value should be d1", "d1", dim.getValue());
      }
    }
  }

  @Test
  public void shouldComputeAggregate() throws Exception {
    Cell cell = new Cell(4);
    cell.addDimension("a", "a2");
    cell.addDimension("b", Cell.DIMENSION_VALUE_ANY);
    cell.addDimension("c", Cell.DIMENSION_VALUE_ANY);
    cell.addDimension("d", Cell.DIMENSION_VALUE_ANY);

    partition.upperBoundOf(cell);
    double avg = partition.computeAggregateAndGet();
    Assert.assertEquals(9d, avg, 0.0d);
  }
}

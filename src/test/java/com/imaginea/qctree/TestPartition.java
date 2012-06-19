package com.imaginea.qctree;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.imaginea.qctree.Cell.DIMENSION_VALUE_ANY;

public class TestPartition {

  private Partition partition;

  @Before
  public void setUp() throws Exception {
    Row row1 = new Row(new String[] { "a2", "b1", "c1", "d1" },
        new double[] { 6d });
    Row row2 = new Row(new String[] { "a2", "b1", "c2", "d1" },
        new double[] { 12d });
    Row row3 = new Row(new String[] { "a1", "b2", "c2", "d2" },
        new double[] { 9d });

    List<Row> rows = new LinkedList<Row>();
    rows.add(row1);
    rows.add(row2);
    rows.add(row3);
    partition = new Partition("i0", rows);
  }

  @Test
  public void shouldReturnUpperBound() throws Exception {
    Cell cell = new Cell(new String[] { "a2", DIMENSION_VALUE_ANY,
        DIMENSION_VALUE_ANY, DIMENSION_VALUE_ANY });
    Cell ub = partition.upperBoundOf(cell);
    Assert.assertArrayEquals("", new String[] { "a2", "b1",
        DIMENSION_VALUE_ANY, "d1" }, ub.getDimensions());
  }

  @Test
  public void shouldComputeAggregate() throws Exception {
    Cell cell = new Cell(new String[] { "a2", DIMENSION_VALUE_ANY,
        DIMENSION_VALUE_ANY, DIMENSION_VALUE_ANY });
    partition.upperBoundOf(cell);
    double avg = partition.computeAggregateAndGet();
    Assert.assertEquals(9d, avg, 0.0d);
  }
}

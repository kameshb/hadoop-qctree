package com.imaginea.qctree;

import static com.imaginea.qctree.Cell.DIMENSION_VALUE_ANY;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestPartition {

  private static Table table;

  @Before
  public void setUp() throws Exception {
    table = Table.getTable();

    Row row1 = new Row(new String[] { "S1", "P1", "s" }, new double[] { 6d });
    Row row2 = new Row(new String[] { "S1", "P2", "s" }, new double[] { 12d });
    Row row3 = new Row(new String[] { "S2", "P1", "f" }, new double[] { 9d });

    table.addRow(row1);
    table.addRow(row2);
    table.addRow(row3);
  }

  @Test
  public void shouldReturnUpperBound() throws Exception {
    Cell cell = new Cell(new String[] { "S1", DIMENSION_VALUE_ANY,
        DIMENSION_VALUE_ANY });
    Partition partition = Partition.inducedBy(cell);
    Class temp = new Class(partition);
    Cell ub = temp.upperBoundOf(cell);
    Assert.assertArrayEquals("",
        new String[] { "S1", DIMENSION_VALUE_ANY, "s" }, ub.getDimensions());
  }

  @Test
  public void shouldComputeAggregate() throws Exception {
    Cell cell = new Cell(new String[] { "S1", DIMENSION_VALUE_ANY,
        DIMENSION_VALUE_ANY });
    Partition partition = Partition.inducedBy(cell);
    Class temp = new Class(partition);
    double avg = temp.computeAggregateAndGet();
    Assert.assertEquals(9d, avg, 0.0d);
  }
}

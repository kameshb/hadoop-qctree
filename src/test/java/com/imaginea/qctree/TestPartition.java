package com.imaginea.qctree;

import static com.imaginea.qctree.Cell.DIMENSION_VALUE_ANY;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.imaginea.qctree.measures.Aggregable;
import com.imaginea.qctree.measures.Aggregates;
import com.imaginea.qctree.measures.Average;
import com.imaginea.qctree.measures.Count;
import com.imaginea.qctree.measures.Maximum;
import com.imaginea.qctree.measures.Minimum;
import com.imaginea.qctree.measures.Sum;

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

  @After
  public void cleanUp() throws Exception {
    table.clear();
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
  public void shouldReturnUpperBoundNonStartUpperBound() throws Exception {
    table.clear();
    Row row1 = new Row(new String[] { "S1", "P1", "s" }, new double[] { 6d });
    table.addRow(row1);

    Cell cell = new Cell(new String[] { DIMENSION_VALUE_ANY,
        DIMENSION_VALUE_ANY, DIMENSION_VALUE_ANY });
    Partition partition = Partition.inducedBy(cell);
    Class temp = new Class(partition);
    Cell ub = temp.upperBoundOf(cell);
    Assert.assertArrayEquals(new String[] { "S1", "P1", "s" },
        ub.getDimensions());
  }

  @Test
  public void shouldComputeAggregate() throws Exception {
    Cell cell = new Cell(new String[] { "S1", DIMENSION_VALUE_ANY,
        DIMENSION_VALUE_ANY });
    Partition partition = Partition.inducedBy(cell);
    Class temp = new Class(partition);
    temp.computeAggregates();
    Aggregates aggregates = temp.getAggregates();
    Map<String, Aggregable> aggrMap = aggregates.get();

    for (Entry<String, Aggregable> aggr : aggrMap.entrySet()) {
      if (aggr.getKey().equals(Average.class.getSimpleName())) {
        Assert.assertEquals(9d, aggr.getValue().getAggregateValue(), 0d);
      }
      if (aggr.getKey().equals(Sum.class.getSimpleName())) {
        Assert.assertEquals(18d, aggr.getValue().getAggregateValue(), 0d);
      }
      if (aggr.getKey().equals(Maximum.class.getSimpleName())) {
        Assert.assertEquals(12d, aggr.getValue().getAggregateValue(), 0d);
      }
      if (aggr.getKey().equals(Minimum.class.getSimpleName())) {
        Assert.assertEquals(6d, aggr.getValue().getAggregateValue(), 0d);
      }
      if (aggr.getKey().equals(Count.class.getSimpleName())) {
        Assert.assertEquals(2d, aggr.getValue().getAggregateValue(), 0d);
      }
    }
  }
}

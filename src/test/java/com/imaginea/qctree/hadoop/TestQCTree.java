package com.imaginea.qctree.hadoop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.imaginea.qctree.Cell;
import com.imaginea.qctree.Class;
import com.imaginea.qctree.Partition;
import com.imaginea.qctree.Row;
import com.imaginea.qctree.Table;

public class TestQCTree {

  private QCTree tree;

  @Before
  public void setUp() throws IOException {
    Table table = Table.getTable();

    Row row1 = new Row(new String[] { "S1", "P1", "s" }, new double[] { 6d });
    Row row2 = new Row(new String[] { "S1", "P2", "s" }, new double[] { 12d });
    Row row3 = new Row(new String[] { "S2", "P1", "f" }, new double[] { 9d });

    table.addRow(row1);
    table.addRow(row2);
    table.addRow(row3);

    Partition part0 = Partition.inducedBy(new Cell(
        new String[] { "*", "*", "*" }));
    Partition part1 = Partition.inducedBy(new Cell(new String[] { "*", "P1",
        "*" }));
    Partition part2 = Partition.inducedBy(new Cell(new String[] { "S1", "*",
        "*" }));
    Partition part3 = Partition.inducedBy(new Cell(
        new String[] { "*", "*", "s" }));
    Partition part4 = Partition.inducedBy(new Cell(new String[] { "S1", "P1",
        "s" }));
    Partition part5 = Partition.inducedBy(new Cell(new String[] { "*", "P1",
        "s" }));
    Partition part6 = Partition.inducedBy(new Cell(new String[] { "S1", "P2",
        "s" }));
    Partition part7 = Partition.inducedBy(new Cell(new String[] { "*", "P2",
        "*" }));
    Partition part8 = Partition.inducedBy(new Cell(new String[] { "S2", "*",
        "*" }));
    Partition part9 = Partition.inducedBy(new Cell(new String[] { "*", "P1",
        "f" }));
    Partition part10 = Partition.inducedBy(new Cell(new String[] { "*", "*",
        "f" }));

    Class clazz0 = new Class(part0);
    clazz0.setClassID(0);
    clazz0.setUpperBound(new Cell(new String[] { "*", "*", "*" }));
    clazz0.setLowerBound(new Cell(new String[] { "*", "*", "*" }));
    clazz0.setChild(null);
    clazz0.computeAggregates();

    tree = new QCTree(clazz0);

    Class clazz5 = new Class(part1);
    clazz5.setClassID(5);
    clazz5.setUpperBound(new Cell(new String[] { "*", "P1", "*" }));
    clazz5.setLowerBound(new Cell(new String[] { "*", "P1", "*" }));
    clazz5.setChild(clazz0);
    clazz5.computeAggregates();

    tree.add(clazz5);

    Class clazz1 = new Class(part2);
    clazz1.setClassID(1);
    clazz1.setUpperBound(new Cell(new String[] { "S1", "*", "s" }));
    clazz1.setLowerBound(new Cell(new String[] { "S1", "*", "*" }));
    clazz1.setChild(clazz0);
    clazz1.computeAggregates();

    tree.add(clazz1);

    Class clazz9 = new Class(part3);
    clazz9.setClassID(9);
    clazz9.setUpperBound(new Cell(new String[] { "S1", "*", "s" }));
    clazz9.setLowerBound(new Cell(new String[] { "*", "*", "s" }));
    clazz9.setChild(clazz0);
    clazz9.computeAggregates();

    tree.add(clazz9);

    Class clazz2 = new Class(part4);
    clazz2.setClassID(2);
    clazz2.setUpperBound(new Cell(new String[] { "S1", "P1", "s" }));
    clazz2.setLowerBound(new Cell(new String[] { "S1", "P1", "s" }));
    clazz2.setChild(clazz1);
    clazz2.computeAggregates();

    tree.add(clazz2);

    Class clazz6 = new Class(part5);
    clazz6.setClassID(6);
    clazz6.setUpperBound(new Cell(new String[] { "S1", "P1", "s" }));
    clazz6.setLowerBound(new Cell(new String[] { "*", "P1", "s" }));
    clazz6.setChild(clazz5);
    clazz6.computeAggregates();

    tree.add(clazz6);

    Class clazz3 = new Class(part6);
    clazz3.setClassID(3);
    clazz3.setUpperBound(new Cell(new String[] { "S1", "P2", "s" }));
    clazz3.setLowerBound(new Cell(new String[] { "S1", "P2", "s" }));
    clazz3.setChild(clazz1);
    clazz3.computeAggregates();

    tree.add(clazz3);

    Class clazz8 = new Class(part7);
    clazz8.setClassID(8);
    clazz8.setUpperBound(new Cell(new String[] { "S1", "P2", "s" }));
    clazz8.setLowerBound(new Cell(new String[] { "*", "P2", "*" }));
    clazz8.setChild(clazz0);
    clazz8.computeAggregates();

    tree.add(clazz8);

    Class clazz4 = new Class(part8);
    clazz4.setClassID(4);
    clazz4.setUpperBound(new Cell(new String[] { "S2", "P1", "f" }));
    clazz4.setLowerBound(new Cell(new String[] { "S2", "*", "*" }));
    clazz4.setChild(clazz0);
    clazz4.computeAggregates();

    tree.add(clazz4);

    Class clazz7 = new Class(part9);
    clazz7.setClassID(7);
    clazz7.setUpperBound(new Cell(new String[] { "S2", "P1", "f" }));
    clazz7.setLowerBound(new Cell(new String[] { "*", "P1", "f" }));
    clazz7.setChild(clazz5);
    clazz7.computeAggregates();

    tree.add(clazz7);

    Class clazz10 = new Class(part10);
    clazz10.setClassID(10);
    clazz10.setUpperBound(new Cell(new String[] { "S2", "P1", "f" }));
    clazz10.setLowerBound(new Cell(new String[] { "*", "*", "f" }));
    clazz10.setChild(clazz0);
    clazz10.computeAggregates();

    tree.add(clazz10);

  }

  @Test
  public void testShouldPerformSerDeCorectly() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutput out = new DataOutputStream(baos);
    tree.write(out);

    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    DataInput in = new DataInputStream(bais);
    QCTree tree1 = new QCTree();
    tree1.readFields(in);

    Assert.assertEquals(tree, tree1);
  }
}

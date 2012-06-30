package com.imaginea.qctree;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import junitx.framework.ListAssert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestQuotientCube {

  private Table table;

  @Before
  public void setUp() throws Exception {
    table = Table.getTable();
  }

  @After
  public void cleanUp() throws Exception {
    table.clear();
  }

  @Test
  public void shouldConstructQCCubeOnInput1() {

    Row row1 = new Row(new String[] { "S1", "P1", "s" }, new double[] { 6d });
    Row row2 = new Row(new String[] { "S1", "P2", "s" }, new double[] { 12d });
    Row row3 = new Row(new String[] { "S2", "P1", "f" }, new double[] { 9d });

    table.addRow(row1);
    table.addRow(row2);
    table.addRow(row3);

    QCCube cube = QCCube.construct();
    Set<Class> classes = cube.getClasses();
    List<Class> actual = new ArrayList<Class>(classes);

    List<Class> expected = new ArrayList<Class>();

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

    expected.add(clazz0);

    Class clazz5 = new Class(part1);
    clazz5.setClassID(5);
    clazz5.setUpperBound(new Cell(new String[] { "*", "P1", "*" }));
    clazz5.setLowerBound(new Cell(new String[] { "*", "P1", "*" }));
    clazz5.setChild(clazz0);
    clazz5.computeAggregates();

    expected.add(clazz5);

    Class clazz1 = new Class(part2);
    clazz1.setClassID(1);
    clazz1.setUpperBound(new Cell(new String[] { "S1", "*", "s" }));
    clazz1.setLowerBound(new Cell(new String[] { "S1", "*", "*" }));
    clazz1.setChild(clazz0);
    clazz1.computeAggregates();

    expected.add(clazz1);

    Class clazz9 = new Class(part3);
    clazz9.setClassID(9);
    clazz9.setUpperBound(new Cell(new String[] { "S1", "*", "s" }));
    clazz9.setLowerBound(new Cell(new String[] { "*", "*", "s" }));
    clazz9.setChild(clazz0);
    clazz9.computeAggregates();

    expected.add(clazz9);

    Class clazz2 = new Class(part4);
    clazz2.setClassID(2);
    clazz2.setUpperBound(new Cell(new String[] { "S1", "P1", "s" }));
    clazz2.setLowerBound(new Cell(new String[] { "S1", "P1", "s" }));
    clazz2.setChild(clazz1);
    clazz2.computeAggregates();

    expected.add(clazz2);

    Class clazz6 = new Class(part5);
    clazz6.setClassID(6);
    clazz6.setUpperBound(new Cell(new String[] { "S1", "P1", "s" }));
    clazz6.setLowerBound(new Cell(new String[] { "*", "P1", "s" }));
    clazz6.setChild(clazz5);
    clazz6.computeAggregates();

    expected.add(clazz6);

    Class clazz3 = new Class(part6);
    clazz3.setClassID(3);
    clazz3.setUpperBound(new Cell(new String[] { "S1", "P2", "s" }));
    clazz3.setLowerBound(new Cell(new String[] { "S1", "P2", "s" }));
    clazz3.setChild(clazz1);
    clazz3.computeAggregates();

    expected.add(clazz3);

    Class clazz8 = new Class(part7);
    clazz8.setClassID(8);
    clazz8.setUpperBound(new Cell(new String[] { "S1", "P2", "s" }));
    clazz8.setLowerBound(new Cell(new String[] { "*", "P2", "*" }));
    clazz8.setChild(clazz0);
    clazz8.computeAggregates();

    expected.add(clazz8);

    Class clazz4 = new Class(part8);
    clazz4.setClassID(4);
    clazz4.setUpperBound(new Cell(new String[] { "S2", "P1", "f" }));
    clazz4.setLowerBound(new Cell(new String[] { "S2", "*", "*" }));
    clazz4.setChild(clazz0);
    clazz4.computeAggregates();

    expected.add(clazz4);

    Class clazz7 = new Class(part9);
    clazz7.setClassID(7);
    clazz7.setUpperBound(new Cell(new String[] { "S2", "P1", "f" }));
    clazz7.setLowerBound(new Cell(new String[] { "*", "P1", "f" }));
    clazz7.setChild(clazz5);
    clazz7.computeAggregates();

    expected.add(clazz7);

    Class clazz10 = new Class(part10);
    clazz10.setClassID(10);
    clazz10.setUpperBound(new Cell(new String[] { "S2", "P1", "f" }));
    clazz10.setLowerBound(new Cell(new String[] { "*", "*", "f" }));
    clazz10.setChild(clazz0);
    clazz10.computeAggregates();

    expected.add(clazz10);

    ListAssert.assertEquals("Mismatch in construction.", expected, actual);
  }

  @Test
  public void shouldConstructQCCubeOnInput2() {

    Row row1 = new Row(new String[] { "Van", "b", "d1" }, new double[] { 9d });
    Row row2 = new Row(new String[] { "Van", "f", "d2" }, new double[] { 3d });
    Row row3 = new Row(new String[] { "Tor", "b", "d2" }, new double[] { 6d });

    table.addRow(row1);
    table.addRow(row2);
    table.addRow(row3);

    QCCube cube = QCCube.construct();
    Set<Class> classes = cube.getClasses();
    List<Class> actual = new ArrayList<Class>(classes);

    List<Class> expected = new ArrayList<Class>();

    Partition part0 = Partition.inducedBy(new Cell(
        new String[] { "*", "*", "*" }));
    Partition part1 = Partition.inducedBy(new Cell(new String[] { "*", "*",
        "d2" }));
    Partition part2 = Partition.inducedBy(new Cell(
        new String[] { "*", "b", "*" }));
    Partition part3 = Partition.inducedBy(new Cell(new String[] { "Tor", "*",
        "*" }));
    Partition part4 = Partition.inducedBy(new Cell(new String[] { "*", "b",
        "d2" }));
    Partition part5 = Partition.inducedBy(new Cell(new String[] { "Van", "*",
        "*" }));
    Partition part6 = Partition.inducedBy(new Cell(new String[] { "Van", "b",
        "*" }));
    Partition part7 = Partition.inducedBy(new Cell(new String[] { "Van", "*",
        "d1" }));
    Partition part8 = Partition.inducedBy(new Cell(new String[] { "*", "b",
        "d1" }));
    Partition part9 = Partition.inducedBy(new Cell(new String[] { "*", "*",
        "d1" }));
    Partition part10 = Partition.inducedBy(new Cell(new String[] { "Van", "f",
        "*" }));
    Partition part11 = Partition.inducedBy(new Cell(new String[] { "Van", "*",
        "d2" }));
    Partition part12 = Partition.inducedBy(new Cell(new String[] { "*", "f",
        "*" }));

    Class clazz0 = new Class(part0);
    clazz0.setClassID(0);
    clazz0.setUpperBound(new Cell(new String[] { "*", "*", "*" }));
    clazz0.setLowerBound(new Cell(new String[] { "*", "*", "*" }));
    clazz0.setChild(null);
    clazz0.computeAggregates();

    expected.add(clazz0);

    Class clazz12 = new Class(part1);
    clazz12.setClassID(12);
    clazz12.setUpperBound(new Cell(new String[] { "*", "*", "d2" }));
    clazz12.setLowerBound(new Cell(new String[] { "*", "*", "d2" }));
    clazz12.setChild(clazz0);
    clazz12.computeAggregates();

    expected.add(clazz12);

    Class clazz7 = new Class(part2);
    clazz7.setClassID(7);
    clazz7.setUpperBound(new Cell(new String[] { "*", "b", "*" }));
    clazz7.setLowerBound(new Cell(new String[] { "*", "b", "*" }));
    clazz7.setChild(clazz0);
    clazz7.computeAggregates();

    expected.add(clazz7);

    Class clazz6 = new Class(part3);
    clazz6.setClassID(6);
    clazz6.setUpperBound(new Cell(new String[] { "Tor", "b", "d2" }));
    clazz6.setLowerBound(new Cell(new String[] { "Tor", "*", "*" }));
    clazz6.setChild(clazz0);
    clazz6.computeAggregates();

    expected.add(clazz6);

    Class clazz9 = new Class(part4);
    clazz9.setClassID(9);
    clazz9.setUpperBound(new Cell(new String[] { "Tor", "b", "d2" }));
    clazz9.setLowerBound(new Cell(new String[] { "*", "b", "d2" }));
    clazz9.setChild(clazz7);
    clazz9.computeAggregates();

    expected.add(clazz9);

    Class clazz1 = new Class(part5);
    clazz1.setClassID(1);
    clazz1.setUpperBound(new Cell(new String[] { "Van", "*", "*" }));
    clazz1.setLowerBound(new Cell(new String[] { "Van", "*", "*" }));
    clazz1.setChild(clazz0);
    clazz1.computeAggregates();

    expected.add(clazz1);

    Class clazz2 = new Class(part6);
    clazz2.setClassID(2);
    clazz2.setUpperBound(new Cell(new String[] { "Van", "b", "d1" }));
    clazz2.setLowerBound(new Cell(new String[] { "Van", "b", "*" }));
    clazz2.setChild(clazz1);
    clazz2.computeAggregates();

    expected.add(clazz2);

    Class clazz4 = new Class(part7);
    clazz4.setClassID(4);
    clazz4.setUpperBound(new Cell(new String[] { "Van", "b", "d1" }));
    clazz4.setLowerBound(new Cell(new String[] { "Van", "*", "d1" }));
    clazz4.setChild(clazz1);
    clazz4.computeAggregates();

    expected.add(clazz4);

    Class clazz8 = new Class(part8);
    clazz8.setClassID(8);
    clazz8.setUpperBound(new Cell(new String[] { "Van", "b", "d1" }));
    clazz8.setLowerBound(new Cell(new String[] { "*", "b", "d1" }));
    clazz8.setChild(clazz7);
    clazz8.computeAggregates();

    expected.add(clazz8);

    Class clazz11 = new Class(part9);
    clazz11.setClassID(11);
    clazz11.setUpperBound(new Cell(new String[] { "Van", "b", "d1" }));
    clazz11.setLowerBound(new Cell(new String[] { "*", "*", "d1" }));
    clazz11.setChild(clazz0);
    clazz11.computeAggregates();

    expected.add(clazz11);

    Class clazz3 = new Class(part10);
    clazz3.setClassID(3);
    clazz3.setUpperBound(new Cell(new String[] { "Van", "f", "d2" }));
    clazz3.setLowerBound(new Cell(new String[] { "Van", "f", "*" }));
    clazz3.setChild(clazz1);
    clazz3.computeAggregates();

    expected.add(clazz3);

    Class clazz5 = new Class(part11);
    clazz5.setClassID(5);
    clazz5.setUpperBound(new Cell(new String[] { "Van", "f", "d2" }));
    clazz5.setLowerBound(new Cell(new String[] { "Van", "*", "d2" }));
    clazz5.setChild(clazz1);
    clazz5.computeAggregates();

    expected.add(clazz5);

    Class clazz10 = new Class(part12);
    clazz10.setClassID(10);
    clazz10.setUpperBound(new Cell(new String[] { "Van", "f", "d2" }));
    clazz10.setLowerBound(new Cell(new String[] { "*", "f", "*" }));
    clazz10.setChild(clazz0);
    clazz10.computeAggregates();

    expected.add(clazz10);

    ListAssert.assertEquals("Mismatch in construction.", expected, actual);
  }

}

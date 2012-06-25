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
    Class clazz = new Class();
    clazz.setClassID(0);
    clazz.setUpperBound(new Cell(new String[] { "*", "*", "*" }));
    clazz.setLowerBound(new Cell(new String[] { "*", "*", "*" }));
    clazz.setChildID(-1);
    clazz.setAggregate(9);

    expected.add(clazz);

    clazz = new Class();
    clazz.setClassID(5);
    clazz.setUpperBound(new Cell(new String[] { "*", "P1", "*" }));
    clazz.setLowerBound(new Cell(new String[] { "*", "P1", "*" }));
    clazz.setChildID(0);
    clazz.setAggregate(7.5);

    expected.add(clazz);

    clazz = new Class();
    clazz.setClassID(1);
    clazz.setUpperBound(new Cell(new String[] { "S1", "*", "s" }));
    clazz.setLowerBound(new Cell(new String[] { "S1", "*", "*" }));
    clazz.setChildID(0);
    clazz.setAggregate(9);

    expected.add(clazz);

    clazz = new Class();
    clazz.setClassID(9);
    clazz.setUpperBound(new Cell(new String[] { "S1", "*", "s" }));
    clazz.setLowerBound(new Cell(new String[] { "*", "*", "s" }));
    clazz.setChildID(0);
    clazz.setAggregate(9);

    expected.add(clazz);

    clazz = new Class();
    clazz.setClassID(2);
    clazz.setUpperBound(new Cell(new String[] { "S1", "P1", "s" }));
    clazz.setLowerBound(new Cell(new String[] { "S1", "P1", "s" }));
    clazz.setChildID(1);
    clazz.setAggregate(6);

    expected.add(clazz);

    clazz = new Class();
    clazz.setClassID(6);
    clazz.setUpperBound(new Cell(new String[] { "S1", "P1", "s" }));
    clazz.setLowerBound(new Cell(new String[] { "*", "P1", "s" }));
    clazz.setChildID(5);
    clazz.setAggregate(6);

    expected.add(clazz);

    clazz = new Class();
    clazz.setClassID(3);
    clazz.setUpperBound(new Cell(new String[] { "S1", "P2", "s" }));
    clazz.setLowerBound(new Cell(new String[] { "S1", "P2", "s" }));
    clazz.setChildID(1);
    clazz.setAggregate(12);

    expected.add(clazz);

    clazz = new Class();
    clazz.setClassID(8);
    clazz.setUpperBound(new Cell(new String[] { "S1", "P2", "s" }));
    clazz.setLowerBound(new Cell(new String[] { "*", "P2", "*" }));
    clazz.setChildID(0);
    clazz.setAggregate(12);

    expected.add(clazz);

    clazz = new Class();
    clazz.setClassID(4);
    clazz.setUpperBound(new Cell(new String[] { "S2", "P1", "f" }));
    clazz.setLowerBound(new Cell(new String[] { "S2", "*", "*" }));
    clazz.setChildID(0);
    clazz.setAggregate(9);

    expected.add(clazz);

    clazz = new Class();
    clazz.setClassID(7);
    clazz.setUpperBound(new Cell(new String[] { "S2", "P1", "f" }));
    clazz.setLowerBound(new Cell(new String[] { "*", "P1", "f" }));
    clazz.setChildID(5);
    clazz.setAggregate(9);

    expected.add(clazz);

    clazz = new Class();
    clazz.setClassID(10);
    clazz.setUpperBound(new Cell(new String[] { "S2", "P1", "f" }));
    clazz.setLowerBound(new Cell(new String[] { "*", "*", "f" }));
    clazz.setChildID(0);
    clazz.setAggregate(9);

    expected.add(clazz);

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
    Class clazz = new Class();
    clazz.setClassID(0);
    clazz.setUpperBound(new Cell(new String[] { "*", "*", "*" }));
    clazz.setLowerBound(new Cell(new String[] { "*", "*", "*" }));
    clazz.setChildID(-1);
    clazz.setAggregate(6);

    expected.add(clazz);

    clazz = new Class();
    clazz.setClassID(12);
    clazz.setUpperBound(new Cell(new String[] { "*", "*", "d2" }));
    clazz.setLowerBound(new Cell(new String[] { "*", "*", "d2" }));
    clazz.setChildID(0);
    clazz.setAggregate(4.5);

    expected.add(clazz);

    clazz = new Class();
    clazz.setClassID(7);
    clazz.setUpperBound(new Cell(new String[] { "*", "b", "*" }));
    clazz.setLowerBound(new Cell(new String[] { "*", "b", "*" }));
    clazz.setChildID(0);
    clazz.setAggregate(7.5);

    expected.add(clazz);

    clazz = new Class();
    clazz.setClassID(6);
    clazz.setUpperBound(new Cell(new String[] { "Tor", "b", "d2" }));
    clazz.setLowerBound(new Cell(new String[] { "Tor", "*", "*" }));
    clazz.setChildID(0);
    clazz.setAggregate(6);

    expected.add(clazz);

    clazz = new Class();
    clazz.setClassID(9);
    clazz.setUpperBound(new Cell(new String[] { "Tor", "b", "d2" }));
    clazz.setLowerBound(new Cell(new String[] { "*", "b", "d2" }));
    clazz.setChildID(7);
    clazz.setAggregate(6);

    expected.add(clazz);

    clazz = new Class();
    clazz.setClassID(1);
    clazz.setUpperBound(new Cell(new String[] { "Van", "*", "*" }));
    clazz.setLowerBound(new Cell(new String[] { "Van", "*", "*" }));
    clazz.setChildID(0);
    clazz.setAggregate(6);

    expected.add(clazz);

    clazz = new Class();
    clazz.setClassID(2);
    clazz.setUpperBound(new Cell(new String[] { "Van", "b", "d1" }));
    clazz.setLowerBound(new Cell(new String[] { "Van", "b", "*" }));
    clazz.setChildID(1);
    clazz.setAggregate(9);

    expected.add(clazz);

    clazz = new Class();
    clazz.setClassID(4);
    clazz.setUpperBound(new Cell(new String[] { "Van", "b", "d1" }));
    clazz.setLowerBound(new Cell(new String[] { "Van", "*", "d1" }));
    clazz.setChildID(1);
    clazz.setAggregate(9);

    expected.add(clazz);

    clazz = new Class();
    clazz.setClassID(8);
    clazz.setUpperBound(new Cell(new String[] { "Van", "b", "d1" }));
    clazz.setLowerBound(new Cell(new String[] { "*", "b", "d1" }));
    clazz.setChildID(7);
    clazz.setAggregate(9);

    expected.add(clazz);

    clazz = new Class();
    clazz.setClassID(11);
    clazz.setUpperBound(new Cell(new String[] { "Van", "b", "d1" }));
    clazz.setLowerBound(new Cell(new String[] { "*", "*", "d1" }));
    clazz.setChildID(0);
    clazz.setAggregate(9);

    expected.add(clazz);

    clazz = new Class();
    clazz.setClassID(3);
    clazz.setUpperBound(new Cell(new String[] { "Van", "f", "d2" }));
    clazz.setLowerBound(new Cell(new String[] { "Van", "f", "*" }));
    clazz.setChildID(1);
    clazz.setAggregate(3);

    expected.add(clazz);

    clazz = new Class();
    clazz.setClassID(5);
    clazz.setUpperBound(new Cell(new String[] { "Van", "f", "d2" }));
    clazz.setLowerBound(new Cell(new String[] { "Van", "*", "d2" }));
    clazz.setChildID(1);
    clazz.setAggregate(3);

    expected.add(clazz);

    clazz = new Class();
    clazz.setClassID(10);
    clazz.setUpperBound(new Cell(new String[] { "Van", "f", "d2" }));
    clazz.setLowerBound(new Cell(new String[] { "*", "f", "*" }));
    clazz.setChildID(0);
    clazz.setAggregate(3);

    expected.add(clazz);

    ListAssert.assertEquals("Mismatch in construction.", expected, actual);
  }

}

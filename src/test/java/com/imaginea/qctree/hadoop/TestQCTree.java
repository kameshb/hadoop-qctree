package com.imaginea.qctree.hadoop;

import org.junit.Test;

import com.imaginea.qctree.Cell;
import com.imaginea.qctree.Class;

public class TestQCTree {

  @Test
  public void testShouldConstructQCTree() {
    Class root = new Class();
    root.setUpperBound(new Cell(new String[] { "*", "*", "*" }));
    root.setLowerBound(new Cell(new String[] { "*", "*", "*" }));
    root.setChildID(-1);
    root.setClassID(0);
    root.setAggregate(9);
    QCTree tree = new QCTree(root);

    Class clazz = new Class();
    clazz.setUpperBound(new Cell(new String[] { "S1", "P1", "s" }));
    clazz.setLowerBound(new Cell(new String[] { "*", "P1", "s" }));
    clazz.setChildID(5);
    clazz.setClassID(6);
    clazz.setAggregate(6);
    tree.add(clazz);
  }

}

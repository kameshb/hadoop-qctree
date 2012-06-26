package com.imaginea.qctree;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

public class TestCell {

  @Test
  public void testShouldSortCells() {

    Set<Cell> set = new TreeSet<Cell>();
    Cell cell1 = new Cell(new String[] { "S2", "P1", "f" });
    set.add(cell1);
    Cell cell2 = new Cell(new String[] { "*", "*", "*" });
    set.add(cell2);
    Cell cell3 = new Cell(new String[] { "S1", "P1", "s" });
    set.add(cell3);
    Cell cell4 = new Cell(new String[] { "S1", "*", "s" });
    set.add(cell4);
    Cell cell5 = new Cell(new String[] { "S2", "P1", "s" });
    set.add(cell5);
    Cell cell6 = new Cell(new String[] { "*", "P1", "*" });
    set.add(cell6);
    Cell cell7 = new Cell(new String[] { "S2", "P1", "f" });
    set.add(cell7);
    
    Iterator<Cell> setItr = set.iterator();
    Assert.assertEquals(cell2, setItr.next());
    Assert.assertEquals(cell6, setItr.next());
    Assert.assertEquals(cell4, setItr.next());
    Assert.assertEquals(cell3, setItr.next());
    Assert.assertEquals(cell7, setItr.next());
    Assert.assertEquals(cell5, setItr.next());
  }

}

package com.imaginea.qctree.hadoop;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.hadoop.io.Writable;

import com.imaginea.qctree.Cell;
import com.imaginea.qctree.Class;
import com.imaginea.qctree.Table;

public class QCTree implements Writable {

  private final QCNode root;

  public QCTree(Class clazz) {
    root = new QCNode("ALL", Cell.DIMENSION_VALUE_ANY, clazz.getAggregate());
  }

  private QCNode getNode(QCNode temp, final String dimValue) {
    while (temp != null) {
      if (temp.isLeaf()) {
        break;
      } else {
        for (QCNode child : temp.children) {
          if (child.dimValue.equals(dimValue)) {
            temp = child;
            break;
          }
        }
      }
    }
    return temp;
  }

  public boolean add(Class clazz) {
    List<String> headers = Table.getTable().getDimensionHeaders();
    String[] dimensions = clazz.getUpperBound().getDimensions();
    QCNode temp = root;
    QCNode parent = root;
    int idx;

    for (idx = 0; idx < dimensions.length; ++idx) {
      if (dimensions[idx] != Cell.DIMENSION_VALUE_ANY) {
        parent = getNode(temp, dimensions[idx]);
        if (temp != parent) {
          break;
        }
      }
    }

    for (int i = idx; i < dimensions.length; ++i) {
      if (dimensions[i] != Cell.DIMENSION_VALUE_ANY) {
        temp = new QCNode(headers.get(i), dimensions[i], clazz.getAggregate());
        if (parent.isLeaf()) {
          parent.children = new LinkedList<QCTree.QCNode>();
        }
        parent.children.add(temp);
        parent = temp;
      }
    }

    return true;
  }

  @Override
  public void readFields(DataInput in) throws IOException {

  }

  @Override
  public void write(DataOutput out) throws IOException {

  }

  private class QCNode implements Writable {
    private String dimName;
    private String dimValue;
    private double aggregate;
    private List<QCNode> children;

    QCNode(String dimName, String dimValue, double aggregate) {
      this.dimName = dimName;
      this.dimValue = dimValue;
      this.aggregate = aggregate;
    }

    String getDimValue() {
      return this.dimValue;
    }

    boolean isLeaf() {
      return this.children == null;
    }

    // double getAggregate() {
    // return this.aggregate;
    // }

    @Override
    public void readFields(DataInput in) throws IOException {
      dimName = in.readUTF();
      dimValue = in.readUTF();
      aggregate = in.readDouble();
    }

    @Override
    public void write(DataOutput out) throws IOException {
      out.writeUTF(dimName);
      out.writeUTF(dimValue);
      out.writeDouble(aggregate);
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(dimName).append("=").append(dimValue);
      sb.append(':').append(aggregate);
      return sb.toString();
    }
  }

}

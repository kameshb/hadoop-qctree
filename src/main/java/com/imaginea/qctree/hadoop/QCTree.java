package com.imaginea.qctree.hadoop;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;

import com.imaginea.qctree.Cell;
import com.imaginea.qctree.Class;
import com.imaginea.qctree.QCCube;
import com.imaginea.qctree.Table;

/**
 * A QC-tree is a compact representation of the QC-cube. It is a prefix tree,
 * having (dimension value, aggregate) as node.
 * 
 */
public class QCTree implements Writable {

  private static final String NONE = "NONE";
  private final QCNode EMPTY = new QCNode(NONE, NONE, 0.0);
  private final QCNode root;

  public QCTree(Class clazz) {
    root = new QCNode("ALL", Cell.DIMENSION_VALUE_ANY, clazz.getAggregate());
  }

  public QCTree() {
    root = new QCNode();
  }

  public static QCTree build(final QCCube qCube) {
    boolean first = true;
    QCTree tree = null;
    for (Class clazz : qCube.getClasses()) {
      if (first) {
        tree = new QCTree(clazz);
        first = false;
      } else {
        tree.add(clazz);
      }
    }
    return tree;
  }

  /*
   * If the QCTree contains a QCNode with given input dimensions, it returns the
   * same QCNode, otherwise its parent will be returned.
   */
  private QCNode getNode(QCNode temp, String dimName, String dimValue) {
    if (temp != null && !temp.isLeaf()) {
      for (QCNode child : temp.children) {
        if (child.dimName.equals(dimName) && child.dimValue.equals(dimValue)) {
          temp = child;
          break;
        }
      }
    }
    return temp;
  }

  public boolean add(Class clazz) {
    List<String> headers = Table.getTable().getDimensionHeaders();
    String[] dimensions = clazz.getUpperBound().getDimensions();
    QCNode parent = root;
    int idx;

    for (idx = 0; idx < dimensions.length; ++idx) {
      if (dimensions[idx] != Cell.DIMENSION_VALUE_ANY) {
        parent = getNode(parent, headers.get(idx), dimensions[idx]);
        if (!parent.getDimValue().equals(dimensions[idx])) {
          break;
        }
      }
    }

    QCNode temp;
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

  /*
   * De-serializing the QC-tree
   */
  @Override
  public void readFields(DataInput in) throws IOException {

    root.readFields(in);
    in.readBoolean();
    Stack<QCNode> stack = new Stack<QCNode>();
    stack.push(root);

    QCNode temp;
    boolean hasChildren;

    while (!stack.isEmpty()) {
      temp = new QCNode();
      temp.readFields(in);
      hasChildren = in.readBoolean();

      if (temp.getDimValue().equals(NONE)) {
        stack.pop();
        continue;
      }
      stack.peek().children.add(temp);
      if (hasChildren) {
        stack.push(temp);
      }
    }

  }

  public void printTree(QCNode node, StringBuilder sb) {
    if (node.isLeaf()) {
      sb.append(node.toString()).append(' ');
      sb.append(false).append('\n');
      return;
    }

    sb.append(node.toString()).append(' ');
    sb.append(true).append('\n');
    for (QCNode child : node.children) {
      printTree(child, sb);
    }
    sb.append(EMPTY.toString()).append('\n');
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    printTree(root, sb);
    return sb.toString();
  }

  private void serialize(QCNode node, DataOutput out) throws IOException {
    if (node.isLeaf()) {
      node.write(out);
      out.writeBoolean(false);
      return;
    }

    node.write(out);
    out.writeBoolean(true);
    for (QCNode child : node.children) {
      serialize(child, out);
    }
    EMPTY.write(out);
    out.writeBoolean(false);
  }

  /*
   * Serializing the QC-tree
   */
  @Override
  public void write(DataOutput out) throws IOException {
    serialize(root, out);
  }

  private class QCNode implements Writable {
    private String dimName;
    private String dimValue;
    private double aggregate;
    private List<QCNode> children;

    QCNode() {
      children = new LinkedList<QCNode>();
    }

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

    @Override
    public void readFields(DataInput in) throws IOException {
      dimName = WritableUtils.readString(in);
      dimValue = WritableUtils.readString(in);
      aggregate = in.readDouble();
    }

    @Override
    public void write(DataOutput out) throws IOException {
      WritableUtils.writeString(out, dimName);
      WritableUtils.writeString(out, dimValue);
      out.writeDouble(aggregate);
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(dimName).append(" = ").append(dimValue);
      sb.append(" : ").append(aggregate);
      return sb.toString();
    }
  }

}

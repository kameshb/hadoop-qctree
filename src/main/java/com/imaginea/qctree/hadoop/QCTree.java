package com.imaginea.qctree.hadoop;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

  private static final Log LOG = LogFactory.getLog(QCTree.class);
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
    QCTree tree = null;
    Cell last = null;

    for (Class clazz : qCube.getClasses()) {
      if (tree == null) {
        tree = new QCTree(clazz);
        last = clazz.getUpperBound();
      } else if (!last.equals(clazz.getUpperBound())) {
        tree.add(clazz);
        last = clazz.getUpperBound();
      } else {
        tree.addDrillDownLink(clazz);
      }
    }
    return tree;
  }

  /*
   * To add the drill down links, find the two nodes in the tree and add a link
   * between them. FIXME crap code, Kamesh think for any better approach?
   */
  private void addDrillDownLink(Class clazz) {
    Cell chdUB = clazz.getChild().getUpperBound();
    Cell curLB = clazz.getLowerBound();
    LOG.info("Adding drill down link between : " + chdUB + " and " + curLB);

    String[] dimensions = clazz.getUpperBound().getDimensions();
    String[] dim = new String[dimensions.length];
    System.arraycopy(Cell.ROOT.getDimensions(), 0, dim, 0, dim.length);

    int idx = 0;
    while (!(chdUB.getDimensionAt(idx) == Cell.DIMENSION_VALUE_ANY && curLB
        .getDimensionAt(idx) != Cell.DIMENSION_VALUE_ANY)) {
      dim[idx] = dimensions[idx];
      idx++;
    }
    dim[idx] = dimensions[idx];

    QCNode from = getLongestMatchingNode(root, chdUB.getDimensions());
    QCNode to = getLongestMatchingNode(root, dim);
    if (from.ddLink == null) {
      from.ddLink = new LinkedList<QCNode>();
    }
    from.ddLink.add(to);
  }

  private QCNode getLongestMatchingNode(QCNode node, String[] dimensions) {
    List<String> headers = Table.getTable().getDimensionHeaders();
    for (int idx = 0; idx < dimensions.length; ++idx) {
      if (dimensions[idx] != Cell.DIMENSION_VALUE_ANY) {
        node = getNode(node, headers.get(idx), dimensions[idx]);
        if (!node.getDimValue().equals(dimensions[idx])) {
          break;
        }
      }
    }
    return node;
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
    LOG.info("Adding tree edge : " + clazz);
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

//  private boolean areEqual(QCNode n1, QCNode n2) {
//    if (n1.isLeaf() && n2.isLeaf() && !n1.equals(n2)) {
//      return false;
//    }
//    if ((n1.isLeaf() && !n2.isLeaf()) || (n2.isLeaf() && !n1.isLeaf())) {
//      return false;
//    }
//    if (!n1.equals(n2)) {
//      return false;
//    }
//    Iterator<QCNode> itr1 = n1.children.iterator();
//    Iterator<QCNode> itr2 = n2.children.iterator();
//
//    while (itr1.hasNext() && itr2.hasNext()) {
//      return areEqual(itr1.next(), itr2.next());
//    }
//    return true;
//  }

//  @Override
//  public boolean equals(Object obj) {
//    if (obj == this) {
//      return true;
//    }
//    if (obj == null) {
//      return false;
//    }
//    if (obj.getClass() != this.getClass()) {
//      return false;
//    }
//    QCTree that = (QCTree) this;
//    return areEqual(that.root, this.root);
//  }

  private class QCNode implements Writable {
    private String dimName;
    private String dimValue;
    private double aggregate;
    private List<QCNode> children;
    private List<QCNode> ddLink;

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
    public boolean equals(Object obj) {
      if (obj == this) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (obj.getClass() != this.getClass()) {
        return false;
      }
      QCNode that = (QCNode) obj;
      return that.dimName.equals(this.dimName)
          && that.dimValue.equals(this.dimName)
          && (Double.compare(that.aggregate, this.aggregate) == 0);
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

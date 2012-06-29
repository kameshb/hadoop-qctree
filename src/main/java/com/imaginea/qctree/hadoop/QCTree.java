package com.imaginea.qctree.hadoop;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
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
  private static final String EMPTY = "";
  private final QCNode MARKER = new QCNode(Integer.MIN_VALUE, EMPTY, 0.0);
  private final QCNode root;

  public QCTree(Class clazz) {
    root = new QCNode(-1, Cell.DIMENSION_VALUE_ANY, clazz.getAggregate());
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
    for (int idx = 0; idx < dimensions.length; ++idx) {
      if (dimensions[idx] != Cell.DIMENSION_VALUE_ANY) {
        node = getNode(node, idx, dimensions[idx]);
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
  private QCNode getNode(QCNode temp, int dimIdx, String dimValue) {
    if (temp != null && !temp.isLeaf()) {
      for (QCNode child : temp.children) {
        if (child.dimIdx == dimIdx && child.dimValue.equals(dimValue)) {
          temp = child;
          break;
        }
      }
    }
    return temp;
  }

  public boolean add(Class clazz) {
    LOG.info("Adding tree edge : " + clazz);
    String[] dimensions = clazz.getUpperBound().getDimensions();
    QCNode parent = root;
    int idx;

    for (idx = 0; idx < dimensions.length; ++idx) {
      if (dimensions[idx] != Cell.DIMENSION_VALUE_ANY) {
        parent = getNode(parent, idx, dimensions[idx]);
        if (!parent.getDimValue().equals(dimensions[idx])) {
          break;
        }
      }
    }

    QCNode temp;
    for (int i = idx; i < dimensions.length; ++i) {
      if (dimensions[i] != Cell.DIMENSION_VALUE_ANY) {
        temp = new QCNode(i, dimensions[i], clazz.getAggregate());
        if (parent.isLeaf()) {
          parent.children = new TreeSet<QCTree.QCNode>();
        }
        parent.children.add(temp);
        parent = temp;
      }
    }

    return true;
  }

  public QCNode getRoot() {
    return root;
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

      if (temp.equals(MARKER)) {
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
    sb.append(NONE).append('\n');
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
    MARKER.write(out);
    out.writeBoolean(false);
  }

  /*
   * Serializing the QC-tree
   */
  @Override
  public void write(DataOutput out) throws IOException {
    serialize(root, out);
  }

  /*
   * Comparing the trees using level order traversing
   */
  private boolean areEqual(QCNode n1, QCNode n2) {
    Queue<QCNode> queue1 = new LinkedList<QCTree.QCNode>();
    Queue<QCNode> queue2 = new LinkedList<QCTree.QCNode>();

    queue1.offer(n1);
    queue2.offer(n2);

    while (!queue1.isEmpty() && !queue2.isEmpty()) {
      QCNode node1 = queue1.poll();
      if (!node1.isLeaf()) {
        queue1.addAll(node1.children);
      }
      QCNode node2 = queue2.poll();
      if (!node2.isLeaf()) {
        queue2.addAll(node2.children);
      }
      if (!node1.equals(node2)) {
        return false;
      }
    }
    if ((queue1.isEmpty() && !queue2.isEmpty())
        || (queue2.isEmpty() && !queue1.isEmpty())) {
      return false;
    }
    return true;
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
    QCTree that = (QCTree) this;
    return areEqual(that.root, this.root);
  }

  /*
   * While adding nodes to the trees, we add them based on their dimension
   * index. Suppose say dimensions are a b and c (order is important). Suppose
   * say Node n has a child with label b. When another child with label a comes,
   * child a precedes child b, in the child list. This has been designed to
   * achieve best query latency times.
   */
  class QCNode implements WritableComparable<QCNode> {
    private int dimIdx;
    private String dimValue;
    private double aggregate;
    private SortedSet<QCNode> children;
    private List<QCNode> ddLink;

    QCNode() {
      children = new TreeSet<QCNode>();
    }

    QCNode(int dimIdx, String dimValue, double aggregate) {
      this.dimIdx = dimIdx;
      this.dimValue = dimValue;
      this.aggregate = aggregate;
    }

    String getDimValue() {
      return this.dimValue;
    }

    int getDimIdx() {
      return this.getDimIdx();
    }

    double getAggregateValue() {
      return this.aggregate;
    }

    boolean isLeaf() {
      return this.children == null;
    }

    boolean hasDDLinks() {
      return this.ddLink != null;
    }

    @Override
    public void readFields(DataInput in) throws IOException {
      dimIdx = WritableUtils.readVInt(in);
      dimValue = WritableUtils.readString(in);
      aggregate = in.readDouble();
    }

    @Override
    public void write(DataOutput out) throws IOException {
      WritableUtils.writeVInt(out, dimIdx);
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
      return that.dimIdx == this.dimIdx && that.dimValue.equals(this.dimValue)
          && (Double.compare(that.aggregate, this.aggregate) == 0);
    }

    // If dimension indices are same, do stable sort
    @Override
    public int compareTo(QCNode o) {
      int diff = dimIdx - o.dimIdx;
      if (diff == 0) {
        diff = dimValue.compareTo(o.dimValue);
      }
      return diff == 0 ? Double.compare(aggregate, o.aggregate) : diff;
    }

    @SuppressWarnings("unchecked")
    public Set<QCNode> getChildren() {
      return isLeaf() ? Collections.EMPTY_SET : children;
    }
    
    public QCNode getLastChild() {
      return children.last();
    }

    @SuppressWarnings("unchecked")
    public List<QCNode> getDDLinks() {
      return hasDDLinks() ? ddLink : Collections.EMPTY_LIST;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      String header;
      if(dimIdx == -1) {
        header = "ALL";
      } else {
        header = Table.getTable().getDimensionHeaderAt(dimIdx);
      }
      sb.append(header);
      sb.append(" = ").append(dimValue);
      sb.append(" : ").append(aggregate);
      return sb.toString();
    }

  }

}

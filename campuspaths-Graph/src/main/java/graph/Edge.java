package graph;

/**
 * This class represents an immutable directed edge.
 *
 * @spec.specfield label : Some type L  // label of the edge
 * @spec.specfield destination : Some type D        // destination of edge
 */
public class Edge<L, D> {
    private L label; //label
    private D dest; //destination

//    abstraction function:
//        AF(this) = labeled edge
//            label = label
//            destination node = dest
//    rep invariant:
//        label !=null && dest != null

    /**
     * Creates labelled edge
     *
     * @param l label of edge
     * @param d destination of edge
     * @throws IllegalArgumentException if l or d is null
     * @spec.effects constructs a new labeled edge with l and d as its fields
     */
//    L is the type of the label, D is the type of the destination or node
    public Edge(L l, D d) {
        if (l == null || d == null) {
            throw new IllegalArgumentException();
        }
        this.label = l;
        this.dest = d;
        checkRep();
    }

    /**
     * @return the label of this edge object
     */
    public L getLabel() {
        checkRep();
        return this.label;
    }
    /**
     * @return the destination of this edge object
     */
    public D getDest() {
        checkRep();
        return this.dest;
    }

    /**
     * return hashcode of edge
     * @return hashcode of edge
     */
    @Override
    public int hashCode() {
        checkRep();
        return label.hashCode() + dest.hashCode();
    }

    /**
     * Returns true if o is equal with this edge
     *
     * @param o object we compare with
     * @return true if they are equal
     */
    @Override
    public boolean equals(Object o) {
        checkRep();
        if (!(o instanceof Edge<?,?>)) {
            return false;
        }
        Edge<?,?> e = (Edge<?,?>)o;
        checkRep();
        return label.equals(e.label) && dest.equals(e.dest);
    }

    /**
     * Returns string representation of edge object
     *
     * @return string representation of edge object
     */
    @Override
    public String toString() {
        checkRep();
        return getDest()+"(" + this.label +")";
    }
    /**
     * Check if rep invariant holds
     */
    private void checkRep() {
        assert (this.label != null) : "label should never be null.";
        assert (this.dest != null) : "dest should never be null.";
    }


}

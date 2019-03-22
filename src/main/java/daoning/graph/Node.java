package daoning.graph;

import java.util.HashSet;
import java.util.Set;

class Node {
    Set<Neighbour> neighbours;
    // Set<Path> paths;
    Node previous;//出发节点到本节点的最短路径上，离本节点最近的节点
    int shortest;
    int id;

    class Neighbour {//拥有指向外部类对象（即AffiliatedNode的那个node）的指针
        Node node;
        int distance;
        boolean passByAffiliatedNode;//true表示是外部类对象节点的后继节点 or 出发点到当前节点的最短路径上，经过外部类对象节点

        public boolean checkAndSet() {
            Node from = Node.this;
            boolean isSet;
            int potentialShortestValue = from.shortest + distance;
            if (isSet = (node.shortest == 0 || potentialShortestValue < node.shortest)) {
                node.shortest = potentialShortestValue;
                node.setPath(from);
//把to设置为from的后继节点
                passByAffiliatedNode = true;
            }
            return isSet;
        }

        Neighbour theOppositeNeighbour() {
            for (Neighbour n : node.neighbours) {
                if (n.node.equals(Node.this))
                    return n;
            }
            throw new IllegalArgumentException("node's neighbour is NOT symmetrical");//添加节点标识信息
        }

        Node affiliatedNode() {
            return Node.this;
        }

        Neighbour(Node node, int distance) {
            this.node = node;
            this.distance = distance;
        }
    }

    void setPath(Node prev) {
        Node old = previous;
        previous = prev;
        if (old != previous)//old为null时，表示此节点第一次出现在路径上，不会有后继节点，吗？
            resetShortestValueOfAllSuccessors();
    }

    private void resetShortestValueOfAllSuccessors() {
        for (Neighbour n : neighbours) {
            if (n.passByAffiliatedNode) {
                n.node.shortest = n.distance + shortest;
                n.node.resetShortestValueOfAllSuccessors();//risk: 构建出的路径会不会出现环路？？？？
            }
        }
    }

    Node(int id) {
        this.id = id;
        neighbours = new HashSet<>();
    }

    void addNeighbour(Node node, int distance) {
        neighbours.add(new Neighbour(node, distance));
    }

    // static class Path{
    // 	Node destination;
    // 	Node previous;
    // 	int shortest;
    // 	public boolean equals(Object o){
    // 		return destination.equals(o);
    // 	}
    // 	public int hashCode(){
    // 		return destination.hashCode();
    // 	}
    // }
    public String toString() {
        StringBuilder s = new StringBuilder(60);
        s.append(".").append(id).append(":");
        for (Neighbour n : neighbours)
            s.append(' ').append(n.distance).append("~.").append(n.node.id).append(',');
        s.append('\n');
        return s.toString();
    }

    String showPath() {
        StringBuilder s = new StringBuilder(100);
        s.append(shortest).append("@.").append(id);
        Node prev = previous;
        while (null != prev) {
            s.append(" <-.").append(prev.id);
            prev = prev.previous;
        }
        return s.toString();
    }
}
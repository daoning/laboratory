package daoning.graph;

import java.io.IOException;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) throws IOException {
        Node[] graph = ReadExcel.initializeGraph();
        int nodeAmountPlus1 = graph.length;
        int totalEdges = 0;//记录图的有向边总数
        for (int i = 1; i < nodeAmountPlus1; i++) {//0下标的元素不存储
            totalEdges += graph[i].neighbours.size();
            System.out.print(graph[i].toString());
        }

        Node startNode = graph[1];//init
        HashSet<Node.Neighbour> excludedEdges = new HashSet<>(totalEdges);//每条有向边只计算一次
        Deque<Node.Neighbour> awaitedEdges = new LinkedList<>();//待处理的边的双向队列，处理的从头部取出，待处理的在尾部放入
        for (Node.Neighbour n : startNode.neighbours) {
            excludedEdges.add(n.theOppositeNeighbour());
            awaitedEdges.addLast(n);
        }

        Node.Neighbour calculate;
        Node focusedNode;
        while ((calculate = awaitedEdges.pollFirst()) != null) {
            calculate.checkAndSet();
            focusedNode = calculate.node;
            for (Node.Neighbour n : focusedNode.neighbours) {
                if (!n.node.equals(calculate.affiliatedNode())) {
                    if (excludedEdges.add(n))
                        awaitedEdges.addLast(n);//在队列尾部插入
                    else //之前已经计算过边n
                        System.out.println("skip one edge");
                }//else 说明是回头路，不走
            }
        }//calculate all edges loop

        //从startnode到其余各节点的路径展示
        for (int i = 1; i < nodeAmountPlus1; i++)
            System.out.println(graph[i].showPath());
    }
}

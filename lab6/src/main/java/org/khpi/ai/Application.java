package org.khpi.ai;

import org.khpi.ai.model.Graph;
import org.khpi.ai.model.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Application {
    public static void main(String[] args) {
        List<Integer> idealValues = List.of(1, 8, 38, 31, 44, 18, 7, 28, 6, 37, 19, 27, 17, 43, 30, 36, 46, 33, 20, 47,
                21, 32, 39, 48, 5, 42, 24, 10, 45, 35, 4, 26, 2, 29, 34, 41, 16, 22, 3, 23, 14, 25, 13, 11, 12, 15, 40, 9);
        List<Integer> initialValues = new ArrayList<>(idealValues);
        Collections.shuffle(initialValues);

        List<Node> idealTour = idealValues.stream()
                .map(Node::new)
                .collect(Collectors.toList());

        List<Node> initialTour = initialValues.stream()
                .map(Node::new)
                .collect(Collectors.toList());

        Graph graph = new Graph();

        List<Node> shortestTour = graph.findShortestTour(initialTour, idealTour, 100);
        System.out.println("Final tour: " + shortestTour.stream().map(Node::getNodeNumber).collect(Collectors.toList()));
    }
}

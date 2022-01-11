package org.khpi.ai.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Graph {

    public List<Node> findShortestTour(List<Node> initialValues, List<Node> idealTour, int maxNumberOfGenerations) {
        List<Node> currentGeneration = nextGeneration(initialValues, idealTour);

        for (int i = 0; i < maxNumberOfGenerations; i++) {

            if (currentGeneration.equals(idealTour)) {
                System.out.println("Found best rout!");
                return idealTour;
            }

            System.out.println("Generation #" + i + ": " + currentGeneration.stream().map(Node::getNodeNumber).collect(Collectors.toList()));
            currentGeneration = nextGeneration(currentGeneration, idealTour);
        }

        return currentGeneration;
    }

    private List<Node> nextGeneration(List<Node> oldGeneration, List<Node> idealTour) {
        List<Integer> coefficients = computeUtilityFactor(oldGeneration, idealTour);

        if (coefficients.stream().mapToInt(Integer::intValue).sum() == 0) {
            return oldGeneration;
        }

        Map<Integer, Integer> positionToCoefficient = new HashMap<>();

        for (int i = 0; i < coefficients.size(); i++) {
            if (coefficients.get(i) != 0) {
                positionToCoefficient.put(i, coefficients.get(i));
            }
        }

        if (positionToCoefficient.size() < 2) {
            throw new IllegalStateException("The number of nonzero coefficient is odd");
        }

        List<Integer> randomIndexes = new ArrayList<>(getRandomIndexes(coefficients, oldGeneration.size()));
        List<Node> newGeneration = new ArrayList<>();

        for (int i = 0; i < oldGeneration.size(); i++) {

            if (positionToCoefficient.containsKey(i)) {

                if (randomIndexes.isEmpty()) {
                    throw new IllegalStateException("Not enough random indexes");
                }

                Integer index = randomIndexes.get(0);
                newGeneration.add(oldGeneration.get(index));
                randomIndexes.remove(index);
            } else {
                newGeneration.add(oldGeneration.get(i));
            }
        }

        return newGeneration;
    }

    private List<Integer> computeUtilityFactor(List<Node> oldGeneration, List<Node> idealTour) {
        if (oldGeneration.size() != idealTour.size()) {
            String errorMessage = String.format("Sizes of old generation (%s) and ideal tour (%s) are not equals",
                    oldGeneration.size(), idealTour.size());
            throw new IllegalStateException(errorMessage);
        }

        List<Integer> coefficients = new ArrayList<>();

        for (int i = 0; i < oldGeneration.size(); i++) {
            coefficients.add(Math.abs(oldGeneration.get(i).getNodeNumber() - idealTour.get(i).getNodeNumber()));
        }

        return coefficients;
    }

    private List<Integer> getRandomIndexes(List<Integer> coefficients, int size) {

        List<Integer> indexes = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            if (coefficients.get(i) != 0) {
                indexes.add(i);
            }
        }
        Collections.shuffle(indexes);

        return indexes;
    }
}

package com.inti.flyingmathfishapp;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ProbabilityLife {
    private Map<Integer, Double> distribution;
    private double distSum;

    //Same logic in DistributedRandomNumberGenerator.java

    public ProbabilityLife() {
        distribution = new HashMap<>();
    }

    public void addPropability(int value, double distribution) {
        if (this.distribution.get(value) != null) {
            distSum -= this.distribution.get(value);
        }
        this.distribution.put(value, distribution);
        distSum += distribution;
    }

    public int getDistributedRandomPropability() {
        int rand =new Random().nextInt((1 - 0) + 1) + 0;
        double ratio = 1.0f / distSum;
        double tempDist = 0;
        for (Integer i : distribution.keySet()) {
            tempDist += distribution.get(i);
            if (rand / ratio <= tempDist) {
                return i;
            }
        }
        return 0;
    }
}

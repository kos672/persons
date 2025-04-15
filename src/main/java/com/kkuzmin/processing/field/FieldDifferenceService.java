package com.kkuzmin.processing.field;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FieldDifferenceService {

    public Optional<FieldDifference> calculateDifference(String fieldName, String previous, String newValue) {
        if (previous == null && newValue != null) {
            return Optional.of(new FieldDifference(fieldName, null, newValue, Classification.ADDED, 0));
        }
        if (previous != null && newValue == null) {
            return Optional.of(new FieldDifference(fieldName, previous, null, Classification.DELETED, 0));
        }
        if (previous == null && newValue == null) {
            return Optional.empty();
        }

        double dissimilarity = calculateDissimilarity(previous, newValue);
        double similarity = Double.parseDouble(String.format("%.3f", 1 - dissimilarity));

        Classification classification = switch ((int)(similarity * 10)) {
            case 10, 9 -> Classification.HIGH;
            case 8, 7, 6, 5, 4 -> Classification.MEDIUM;
            default -> Classification.LOW;
        };

        return Optional.of(new FieldDifference(fieldName, previous, newValue, classification, similarity));
    }

    private Double calculateDissimilarity(String previous, String current) {
        int levenshteinDistance = calculateLevenshteinDistance(previous, current);
        int maxLength = Math.max(previous.length(), current.length());

        double dissimilarity = (double) levenshteinDistance / maxLength;
        return Double.parseDouble(String.format("%.3f", dissimilarity));
    }

    private int calculateLevenshteinDistance(String previous, String current) {
        int m = previous.length();
        int n = current.length();

        int[][] matrix = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0) {
                    matrix[i][j] = j; // all insertions
                } else if (j == 0) {
                    matrix[i][j] = i; // all deletions
                } else if (previous.charAt(i - 1) == current.charAt(j - 1)) {
                    matrix[i][j] = matrix[i - 1][j - 1]; // no change
                } else {
                    matrix[i][j] = 1 + Math.min(Math.min(matrix[i - 1][j], matrix[i][j - 1]), matrix[i - 1][j - 1]); // substitution, insertion, deletion
                }
            }
        }

        return matrix[m][n];
    }
}

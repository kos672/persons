package com.kkuzmin.processing.field;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FieldDifferenceServiceTest {

    @Autowired
    private FieldDifferenceService fieldDifferenceService;

    @Test
    void calculateDifference_newFieldAddedNoSimilarity() {
        // given
        String fieldName = "company";
        String previousValue = null;
        String newValue = "PKP";

        // when
        Optional<FieldDifference> fieldDifference = fieldDifferenceService.calculateDifference(fieldName, previousValue, newValue);

        // then
        assertTrue(fieldDifference.isPresent());
        assertThat(fieldDifference.get().getFieldName()).isEqualTo(fieldName);
        assertThat(fieldDifference.get().getSimilarity()).isEqualTo(0);
        assertThat(fieldDifference.get().getClassification()).isEqualTo(Classification.ADDED);
    }

    @Test
    void calculateDifference_fieldDeletedNoSimilarity() {
        // given
        String fieldName = "surname";
        String previousValue = "PKP";
        String newValue = null;

        // when
        Optional<FieldDifference> fieldDifference = fieldDifferenceService.calculateDifference(fieldName, previousValue, newValue);

        // then
        assertTrue(fieldDifference.isPresent());
        assertThat(fieldDifference.get().getFieldName()).isEqualTo(fieldName);
        assertThat(fieldDifference.get().getSimilarity()).isEqualTo(0);
        assertThat(fieldDifference.get().getClassification()).isEqualTo(Classification.DELETED);
    }

    @Test
    void calculateDifference_noDifferencePresentAsPreviousAndNewValuesNull() {
        // given
        String fieldName = "name";
        String previousValue = null;
        String newValue = null;

        // when
        Optional<FieldDifference> fieldDifference = fieldDifferenceService.calculateDifference(fieldName, previousValue, newValue);

        // then
        assertTrue(fieldDifference.isEmpty());
    }

    @Test
    void calculateDifference_fieldChangedSimilarityLow() {
        // given
        String fieldName = "company";
        String previousValue = "ABCDEFGH";
        String newValue = "TDD";

        // when
        Optional<FieldDifference> fieldDifference = fieldDifferenceService.calculateDifference(fieldName, previousValue, newValue);

        // then
        assertTrue(fieldDifference.isPresent());
        assertThat(fieldDifference.get().getFieldName()).isEqualTo(fieldName);
        assertThat(fieldDifference.get().getSimilarity()).isEqualTo(0.125);
        assertThat(fieldDifference.get().getClassification()).isEqualTo(Classification.LOW);
    }

    @Test
    void calculateDifference_fieldChangedSimilarityMedium() {
        // given
        String fieldName = "surname";
        String previousValue = "ABCDEFG";
        String newValue = "CFG";

        // when
        Optional<FieldDifference> fieldDifference = fieldDifferenceService.calculateDifference(fieldName, previousValue, newValue);

        // then
        assertTrue(fieldDifference.isPresent());
        assertThat(fieldDifference.get().getFieldName()).isEqualTo(fieldName);
        assertThat(fieldDifference.get().getSimilarity()).isEqualTo(0.429);
        assertThat(fieldDifference.get().getClassification()).isEqualTo(Classification.MEDIUM);
    }

    @Test
    void calculateDifference_fieldChangedSimilarityHigh() {
        // given
        String fieldName = "name";
        String previousValue = "ABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGH";
        String newValue = "ABCDEFGHABCDEFGHABCDEFGHCDEFGHABCDEFGHABCDEFDC";

        // when
        Optional<FieldDifference> fieldDifference = fieldDifferenceService.calculateDifference(fieldName, previousValue, newValue);

        // then
        assertTrue(fieldDifference.isPresent());
        assertThat(fieldDifference.get().getFieldName()).isEqualTo(fieldName);
        assertThat(fieldDifference.get().getSimilarity()).isEqualTo(0.917);
        assertThat(fieldDifference.get().getClassification()).isEqualTo(Classification.HIGH);
    }
}
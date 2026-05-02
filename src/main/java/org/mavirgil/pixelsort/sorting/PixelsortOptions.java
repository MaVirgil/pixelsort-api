package org.mavirgil.pixelsort.sorting;

public record PixelsortOptions(
        SortOrientation sortOrientation,
        SortOrder sortOrder,
        int lowerThreshold,
        int upperThreshold,
        int minSegmentLength
) {}

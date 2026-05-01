package org.mavirgil.pixelsort.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PixelsortOptions {
    private Direction direction;
    private Integer thresholdMin;
    private Integer thresholdMax;
}

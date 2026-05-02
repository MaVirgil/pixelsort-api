package org.mavirgil.pixelsort.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PixelsortOptions {
    private Direction direction;
    private Integer lowerThreshold;
    private Integer upperThreshold;
}

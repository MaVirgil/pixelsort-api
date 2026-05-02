package org.mavirgil.pixelsort.dto;


public enum Direction {
    UP ("up"),
    DOWN ("down"),
    LEFT ("left"),
    RIGHT ("right");

    final String value;

    Direction(String value) {
        this.value = value;
    }
}



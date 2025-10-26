package com.gummybear.desktop.icon;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum IconSize {
    XSMALL(30),
    SMALL(25),
    MEDIUM(20),
    LARGE(15),
    XLARGE(10);

    @Getter
    private final int size; // Represents the amount of icons that appear per row
}

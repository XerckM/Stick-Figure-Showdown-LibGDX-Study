package com.xmdev.sfs.objects;

import com.badlogic.gdx.graphics.Color;

public class FighterChoice {
    public String name;
    public float[] colorValues;
    private Color color;

    public String getName() {
        return name;
    }

    public Color getColor() {
        if (color == null) {
            // if color hasn't been initialized, initialize it using the color values
            color = new Color(colorValues[0], colorValues[1], colorValues[2], 1);
        }
        return color;
    }
}

package net.sssubtlety.automated_crafting;

public class Validator {
    private static int validationKey = Integer.MIN_VALUE;

    private int key;

    public Validator() {
        this.key = validationKey;
    }

    public static void update() {
        validationKey++;
    }

    public boolean invalid() {
        if (key == validationKey) return false;
        else {
            key = validationKey;
            return true;
        }
    }
}

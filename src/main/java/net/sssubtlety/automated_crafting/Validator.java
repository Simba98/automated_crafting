package net.sssubtlety.automated_crafting;

public class Validator {
    private int validationKey = Integer.MIN_VALUE;

    public void update() {
        validationKey++;
    }

    public Validation getValidation() {
        return new Validation();
    }

    public class Validation {
        private int key;

        public Validation() {
            this.key = validationKey;
        }

        public boolean invalid() {
            if (key == validationKey) return false;
            else {
                key = validationKey;
                return true;
            }
        }
    }
}

package mustapelto.deepmoblearning.common.util;

import mustapelto.deepmoblearning.client.gui.GuiSimulationChamber;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class StringAnimator {
    private final LinkedHashMap<GuiSimulationChamber.AnimatedString, AnimatedString> strings;
    private final List<GuiSimulationChamber.AnimatedString> keys;
    private int currentStringIndex;
    private float totalDuration;
    private boolean finished;

    public StringAnimator() {
        strings = new LinkedHashMap<>();
        keys = new ArrayList<>();
        finished = false;
        currentStringIndex = 0;
        totalDuration = 0;
    }

    public void reset() {
        currentStringIndex = 0;
        finished = false;
        strings.values().forEach(AnimatedString::reset);
    }

    public void addString(GuiSimulationChamber.AnimatedString key, String string) {
        addString(key, string, 1, false);
    }

    public void addString(GuiSimulationChamber.AnimatedString key, String string, float speed, boolean loop) {
        AnimatedString newString = new AnimatedString(string, speed, loop);
        strings.put(key, newString);
        keys.add(key);
        totalDuration += newString.getDuration();
    }

    public void setString(GuiSimulationChamber.AnimatedString key, String string) {
        AnimatedString oldString = strings.get(key);
        AnimatedString newString = new AnimatedString(string, oldString.getSpeed(), oldString.isLoop());
        strings.replace(key, newString);
        totalDuration -= oldString.getDuration();
        totalDuration += newString.getDuration();
    }

    public void advance(float amount) {
        if (finished)
            return;

        AnimatedString currentString = getByIndex(currentStringIndex);
        if (currentStringIndex == strings.size() && currentString.isFinished())
            return;

        while (amount > 0) {
            amount = currentString.advance(amount);
            if (currentString.isFinished()) {
                if (currentStringIndex < strings.size() - 1) {
                    currentStringIndex++;
                    currentString = getByIndex(currentStringIndex);
                }
                else {
                    finished = true;
                    break;
                }
            }
        }
    }

    public List<String> getCurrentStrings() {
        List<String> result = new ArrayList<>();
        for (int i = 0; i <= currentStringIndex; i++) {
            result.add(strings.get(keys.get(i)).getString());
        }
        return result;
    }

    public void goToPosition(float position) {
        reset();
        advance(position);
    }

    public void goToRelativePosition(float relativePosition) {
        relativePosition = MathHelper.Clamp(relativePosition, 0, 1);
        goToPosition(relativePosition * totalDuration);
    }

    private AnimatedString getByIndex(int index) {
        return strings.get(keys.get(index));
    }

    public static class AnimatedString {
        private final String string; // The string to animate
        private final float speed; // Speed of animation (in ticks/letter)
        private final float duration; // Duration of animation (in ticks)
        private final float formattedLength; // Length of string when not counting MC formatting characters (e.g. "�d")
        private final boolean loop; // Does the animation loop?

        private float position; // Current position (in ticks)
        private boolean finished; // Has animation finished?


        /**
         * @param string String to animate
         * @param speed Ticks per letter
         * @param loop Does animation loop?
         */
        public AnimatedString(String string, float speed, boolean loop) {
            this.string = string;
            this.speed = speed;
            int tempActualLength = 0;

            // Filter out MC formatting symbols for length calculation
            for (int i = 0; i < string.length(); i++) {
                if (string.charAt(i) != '�') {
                    tempActualLength++; // Count non-formatting chars
                } else {
                    i++; // Skip second char of formatting symbol
                }
            }
            formattedLength = tempActualLength;
            duration = formattedLength * speed;
            this.loop = loop;

            position = 0;
        }

        public float advance(float amount) {
            return goToPosition(position + amount);
        }

        private void finishIfDone() {
            if (position >= duration) {
                if (loop)
                    reset();
                else
                    finished = true;
            }
        }

        public float goToPosition(float position) {
            this.position = Math.min(position, duration);
            finishIfDone();
            return position - duration;
        }

        public String getString() {
            // Get current position in string without formatting codes
            float floatIndex = (position / duration) * formattedLength;

            // Get current position in actual string
            int endIndex = 0;
            int lettersAdded = 0; // counts "actual letters" added to output (excluding formatting characters)
            for (int i = 0; i < string.length() && lettersAdded <= floatIndex; i++) {
                if (string.charAt(i) == '�') { // Start of MC formatting character
                    if (i < string.length() - 1) { // Not currently at last character in string
                        endIndex += 2; // Add this and following character to output
                    }
                    i++; // Skip second part of formatting string
                } else { // Regular character
                    lettersAdded++;
                    endIndex++;
                }
            }
            return string.substring(0, endIndex);
        }

        public float getSpeed() {
            return speed;
        }

        public float getDuration() {
            return duration;
        }

        public boolean isFinished() {
            return finished;
        }

        public boolean isLoop() {
            return loop;
        }

        public void reset() {
            position = 0;
            finished = false;
        }
    }
}

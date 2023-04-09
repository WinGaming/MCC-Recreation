package mcc.utils;

import mcc.display.SpaceFont;

public class WidthLimitedString {
    
    private static final int MIN_SPACE_WIDTH = SpaceFont.getWidthOf(" ");

    public static String buildString(int maxPxWidth, String...texts) {
        final int[] widths = new int[texts.length];
        for (int i = 0; i < texts.length; i++) {
            widths[i] = SpaceFont.getWidthOf(texts[i]);
        }

        int totalWidth = 0;
        for (int width : widths) {
            totalWidth += width;
        }
        
        final int minSpaceWidth = (texts.length - 1) * MIN_SPACE_WIDTH;

        if (minSpaceWidth + totalWidth > maxPxWidth) {
            int longestTextIndex = 0;
            for (int i = 1; i < widths.length; i++) {
                if (widths[i] > widths[longestTextIndex]) {
                    longestTextIndex = i;
                }
            }

            String[] textsCopy = texts.clone();
            textsCopy[longestTextIndex] = textsCopy[longestTextIndex].substring(0, textsCopy[longestTextIndex].length() - 1);
            return buildString(maxPxWidth, textsCopy);
        }

        int availableSpaceWidth = maxPxWidth - totalWidth - minSpaceWidth;
        int spaceCount = texts.length - 1;

        int spaceWidthMod = availableSpaceWidth % spaceCount;
        int eachSpaceWidth = (availableSpaceWidth - spaceWidthMod) / spaceCount;

        int[] spaces = new int[spaceCount];
        for (int i = 0; i < spaceWidthMod; i++) {
            spaces[i] += 1;
        }

        for (int i = 0; i < spaces.length; i++) {
            spaces[i] += eachSpaceWidth;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < texts.length; i++) {
            sb.append(texts[i]);
            if (i < spaces.length) {
                sb.append(SpaceFont.getSpaceString(spaces[i]) + SpaceFont.getSpaceString(MIN_SPACE_WIDTH));
            }
        }

        return sb.toString();
    }
}

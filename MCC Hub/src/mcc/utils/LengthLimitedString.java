package mcc.utils;

import java.util.ArrayList;
import java.util.List;

public class LengthLimitedString {

    private List<Pair<String, Integer>> texts;

    public LengthLimitedString() {
        this.texts = new ArrayList<>();
    }

    public void addText(String text, int minLength) {
        if (minLength > text.length()) throw new IllegalArgumentException("The minimum length of the text is greater than the length of the string.");
        this.texts.add(new Pair<>(text, minLength));
    }

    public String build(int maxLength) {
        List<TextEntryDelta> textsCopy = new ArrayList<>();

        for (int i = 0; i < textsCopy.size(); i++) {
            TextEntryDelta delta = new TextEntryDelta();
            delta.text = texts.get(i).getA();
            delta.minLength = texts.get(i).getB();
            textsCopy.add(delta);
        }

        textsCopy.sort((a, b) -> a.text.length() - b.text.length());

        StringBuilder result = new StringBuilder();

        int currentLength = textsCopy.stream().mapToInt(e -> e.text.length()).sum();
        while (currentLength > maxLength & textsCopy.size() >= 2) {
            int toRemove = currentLength - maxLength;

            TextEntryDelta longestEntry = textsCopy.get(textsCopy.size() - 1);
            int availableRemove = longestEntry.text.length() - textsCopy.get(textsCopy.size() - 2).text.length();

            int remove = Math.min(toRemove, availableRemove);
            longestEntry.text = longestEntry.text.substring(0, longestEntry.text.length() - remove);
            currentLength -= remove;

            if (longestEntry.text.length() <= longestEntry.minLength) {
                result.append(longestEntry.text);
                textsCopy.remove(longestEntry);
            }
        }

        for (TextEntryDelta delta : textsCopy) {
            result.append(delta.text);
        }

        if (result.toString().length() > maxLength) throw new IllegalStateException("The length of the result is greater than the maximum length.");

        return result.toString();
    }

    private class TextEntryDelta {

        public String text;
        public int minLength;

    }
}


// package mcc.utils;

// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// /**
//  * This class is used to shorted parts inside a string.
//  */
// public class LengthLimitedString {
    
//     private int maxLength;
//     private List<Pair<String, Integer>> texts;

//     public LengthLimitedString(int maxLength) {
//         this.texts = new ArrayList<>();
//         this.maxLength = maxLength;
//     }

//     public void addText(String text, int length) {
//         if (length < text.length()) throw new IllegalArgumentException("The min-length of the text is greater than the length of the string.");
//         this.texts.add(new Pair<>(text, length));
//     }

//     public String build() {
//         int currentLength = texts.stream().mapToInt(pair -> pair.getA().length()).sum();
//         int minLength = texts.stream().mapToInt(Pair::getB).sum();

//         if (minLength > maxLength) {
//             throw new IllegalArgumentException("The minimum length of the string is greater than the maximum length.");
//         }

//         final Map<Integer, Integer> removeableChars = new HashMap<>();
//         for (Pair<String, Integer> pair : texts) {
//             int removeable = pair.getA().length() - pair.getB();
//             removeableChars.put(texts.indexOf(pair), removeable);
//         }

//         int toRemove = this.maxLength - currentLength;


//     }

// }

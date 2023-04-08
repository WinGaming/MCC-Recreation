package mcc.display;

// https://github.com/AmberWat/NegativeSpaceFont

public class SpaceFont {
    
    public static int getWidthOf(String text) {
        int width = 0;
        for (int i = 0; i < text.length(); i++) {
            width += getWidthOf(text.charAt(i));
        }
        
        return width;
    }

    public static int getWidthOf(char c) {
        if (c == POS_SPACE_1)   return 1;
        if (c == POS_SPACE_2)   return 2;
        if (c == POS_SPACE_3)   return 3;
        if (c == POS_SPACE_4)   return 4;
        if (c == POS_SPACE_5)   return 5;
        if (c == POS_SPACE_6)   return 6;
        if (c == POS_SPACE_7)   return 7;
        if (c == POS_SPACE_8)   return 8;
        if (c == POS_SPACE_16)  return 16;
        if (c == POS_SPACE_32)  return 32;
        if (c == POS_SPACE_64)  return 64;
        if (c == POS_SPACE_128) return 128;
        if (c == POS_SPACE_256) return 256;
        if (c == POS_SPACE_512) return 512;
        if (c == POS_SPACE_1024)return 1024;
        // if (c == POS_SPACE_MAX) return 2048;

        if (c == NEG_SPACE_1)   return -1;
        if (c == NEG_SPACE_2)   return -2;
        if (c == NEG_SPACE_3)   return -3;
        if (c == NEG_SPACE_4)   return -4;
        if (c == NEG_SPACE_5)   return -5;
        if (c == NEG_SPACE_6)   return -6;
        if (c == NEG_SPACE_7)   return -7;
        if (c == NEG_SPACE_8)   return -8;
        if (c == NEG_SPACE_16)  return -16;
        if (c == NEG_SPACE_32)  return -32;
        if (c == NEG_SPACE_64)  return -64;
        if (c == NEG_SPACE_128) return -128;
        if (c == NEG_SPACE_256) return -256;
        if (c == NEG_SPACE_512) return -512;
        if (c == NEG_SPACE_1024)return -1024;
        // if (c == NEG_SPACE_MAX) return -2048;

        if (c == '!') return 2;
        if (c == '"') return 4;
        if (c == '#') return 6;
        if (c == '$') return 6;
        if (c == '%') return 6;
        if (c == '&') return 6;
        if (c == '\'') return 2;
        if (c == '(') return 4;
        if (c == ')') return 4;
        if (c == '*') return 4;
        if (c == '+') return 6;
        if (c == ',') return 2;
        if (c == '-') return 6;
        if (c == '.') return 2;
        if (c == '/') return 6;
        if (c == '0') return 6;
        if (c == '1') return 6;
        if (c == '2') return 6;
        if (c == '3') return 6;
        if (c == '4') return 6;
        if (c == '5') return 6;
        if (c == '6') return 6;
        if (c == '7') return 6;
        if (c == '8') return 6;
        if (c == '9') return 6;
        if (c == ':') return 2;
        if (c == ';') return 2;
        if (c == '<') return 5;
        if (c == '=') return 6;
        if (c == '>') return 5;
        if (c == '?') return 6;
        if (c == '@') return 7;
        if (c == 'A') return 6;
        if (c == 'B') return 6;
        if (c == 'C') return 6;
        if (c == 'D') return 6;
        if (c == 'E') return 6;
        if (c == 'F') return 6;
        if (c == 'G') return 6;
        if (c == 'H') return 6;
        if (c == 'I') return 4;
        if (c == 'J') return 6;
        if (c == 'K') return 6;
        if (c == 'L') return 6;
        if (c == 'M') return 6;
        if (c == 'N') return 6;
        if (c == 'O') return 6;
        if (c == 'P') return 6;
        if (c == 'Q') return 6;
        if (c == 'R') return 6;
        if (c == 'S') return 6;
        if (c == 'T') return 6;
        if (c == 'U') return 6;
        if (c == 'V') return 6;
        if (c == 'W') return 6;
        if (c == 'X') return 6;
        if (c == 'Y') return 6;
        if (c == 'Z') return 6;
        if (c == '[') return 4;
        if (c == '\\') return 6;
        if (c == ']') return 4;
        if (c == '^') return 6;
        if (c == '_') return 6;
        if (c == '`') return 3;
        if (c == 'a') return 6;
        if (c == 'b') return 6;
        if (c == 'c') return 6;
        if (c == 'd') return 6;
        if (c == 'e') return 6;
        if (c == 'f') return 5;
        if (c == 'g') return 6;
        if (c == 'h') return 6;
        if (c == 'i') return 2;
        if (c == 'j') return 6;
        if (c == 'k') return 5;
        if (c == 'l') return 3;
        if (c == 'm') return 6;
        if (c == 'n') return 6;
        if (c == 'o') return 6;
        if (c == 'p') return 6;
        if (c == 'q') return 6;
        if (c == 'r') return 6;
        if (c == 's') return 6;
        if (c == 't') return 4;
        if (c == 'u') return 6;
        if (c == 'v') return 6;
        if (c == 'w') return 6;
        if (c == 'x') return 6;
        if (c == 'y') return 6;
        if (c == 'z') return 6;
        if (c == '{') return 4;
        if (c == '|') return 2;
        if (c == '}') return 4;
        if (c == '~') return 7;
        if (c == ' ') return 4;

        return 8;
    }

    public static String getSpaceString(int space) {
        int negOffset = space < 0 ? NEG_SPACE_1 - POS_SPACE_1 : 0;
        int sign = space < 0 ? -1 : 1;
        
        int absSpace = Math.abs(space);

        if (absSpace >= 1024) return ((char) (negOffset + POS_SPACE_1024)) + getSpaceString(space - 1024 * sign);
        if (absSpace >= 512)  return ((char) (negOffset + POS_SPACE_512))  + getSpaceString(space - 512 * sign);
        if (absSpace >= 256)  return ((char) (negOffset + POS_SPACE_256))  + getSpaceString(space - 256 * sign);
        if (absSpace >= 128)  return ((char) (negOffset + POS_SPACE_128))  + getSpaceString(space - 128 * sign);
        if (absSpace >= 64)   return ((char) (negOffset + POS_SPACE_64))   + getSpaceString(space - 64 * sign);
        if (absSpace >= 32)   return ((char) (negOffset + POS_SPACE_32))   + getSpaceString(space - 32 * sign);
        if (absSpace >= 16)   return ((char) (negOffset + POS_SPACE_16))   + getSpaceString(space - 16 * sign);
        if (absSpace >= 8)    return ((char) (negOffset + POS_SPACE_8))    + getSpaceString(space - 8 * sign);
        if (absSpace >= 7)    return ((char) (negOffset + POS_SPACE_7))    + getSpaceString(space - 7 * sign);
        if (absSpace >= 6)    return ((char) (negOffset + POS_SPACE_6))    + getSpaceString(space - 6 * sign);
        if (absSpace >= 5)    return ((char) (negOffset + POS_SPACE_5))    + getSpaceString(space - 5 * sign);
        if (absSpace >= 4)    return ((char) (negOffset + POS_SPACE_4))    + getSpaceString(space - 4 * sign);
        if (absSpace >= 3)    return ((char) (negOffset + POS_SPACE_3))    + getSpaceString(space - 3 * sign);
        if (absSpace >= 2)    return ((char) (negOffset + POS_SPACE_2))    + getSpaceString(space - 2 * sign);
        if (absSpace >= 1)    return ((char) (negOffset + POS_SPACE_1))    + getSpaceString(space - 1 * sign);

        return "";
    }

    public static final char POS_SPACE_1    = '\uF821';
    public static final char POS_SPACE_2    = '\uF822';
    public static final char POS_SPACE_3    = '\uF823';
    public static final char POS_SPACE_4    = '\uF824';
    public static final char POS_SPACE_5    = '\uF825';
    public static final char POS_SPACE_6    = '\uF826';
    public static final char POS_SPACE_7    = '\uF827';
    public static final char POS_SPACE_8    = '\uF828';
    public static final char POS_SPACE_16   = '\uF829';
    public static final char POS_SPACE_32   = '\uF82A';
    public static final char POS_SPACE_64   = '\uF82B';
    public static final char POS_SPACE_128  = '\uF82C';
    public static final char POS_SPACE_256  = '\uF82D';
    public static final char POS_SPACE_512  = '\uF82E';
    public static final char POS_SPACE_1024 = '\uF82F';
    public static final char POS_SPACE_MAX  = '\uF820';

    public static final char NEG_SPACE_1    = '\uF801';
    public static final char NEG_SPACE_2    = '\uF802';
    public static final char NEG_SPACE_3    = '\uF803';
    public static final char NEG_SPACE_4    = '\uF804';
    public static final char NEG_SPACE_5    = '\uF805';
    public static final char NEG_SPACE_6    = '\uF806';
    public static final char NEG_SPACE_7    = '\uF807';
    public static final char NEG_SPACE_8    = '\uF808';
    public static final char NEG_SPACE_16   = '\uF809';
    public static final char NEG_SPACE_32   = '\uF80A';
    public static final char NEG_SPACE_64   = '\uF80B';
    public static final char NEG_SPACE_128  = '\uF80C';
    public static final char NEG_SPACE_256  = '\uF80D';
    public static final char NEG_SPACE_512  = '\uF80E';
    public static final char NEG_SPACE_1024 = '\uF80F';
    public static final char NEG_SPACE_MAX  = '\uF800';

}

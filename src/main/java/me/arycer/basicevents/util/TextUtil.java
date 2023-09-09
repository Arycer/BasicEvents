package me.arycer.basicevents.util;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TextUtil {
    public static Text parseText(String input) {
        MutableText result = Text.literal("");
        Style currentStyle = Style.EMPTY;

        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (currentChar == '&') {
                if (i + 1 < input.length()) {
                    char colorCode = input.charAt(i + 1);
                    Formatting formatting = Formatting.byCode(colorCode);
                    if (formatting == null) continue;

                    if (formatting.equals(Formatting.RESET)) {
                        currentStyle = Style.EMPTY;
                    } else if (formatting.isColor()) {
                        currentStyle = currentStyle.withColor(formatting);
                    } else if (formatting.isModifier()) {
                        currentStyle = currentStyle.withFormatting(formatting);
                    }

                    i++;
                }
            } else {
                result.append(Text.literal(String.valueOf(currentChar)).setStyle(currentStyle));
            }
        }

        return result;
    }
}

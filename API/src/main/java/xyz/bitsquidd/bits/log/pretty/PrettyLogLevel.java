/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.log.pretty;

import java.util.List;

public final class PrettyLogLevel {
    private final String emoji;
    private final String prefix;
    private final FormattingComponents prefixFormatting;
    private final FormattingComponents messageFormatting;

    public PrettyLogLevel(String emoji, String prefix, FormattingComponents prefixFormatting, FormattingComponents messageFormatting) {
        this.emoji = emoji;
        this.prefix = prefix;
        this.prefixFormatting = prefixFormatting;
        this.messageFormatting = messageFormatting;
    }

    public String formatMessage(final String message) {
        String timestamp = "";

        StringBuilder prefixBuilder = new StringBuilder(prefix);

        prefixBuilder.insert(0, (emoji + "  "));
        String formattedPrefix = prefixBuilder.toString();

        return String.format(
          "%s %s %s",
          timestamp,
          prefixFormatting.format(formattedPrefix),
          messageFormatting.format(message)
        );
    }


    //region Example implementations
    public static final PrettyLogLevel RED = new PrettyLogLevel(
      "", "",
      FormattingComponents.of(ANSI.Foreground.WHITE, ANSI.Background.BRIGHT_RED, List.of(ANSI.Style.BOLD)),
      FormattingComponents.of(ANSI.Foreground.BRIGHT_RED, null, List.of())
    );

    public static final PrettyLogLevel YELLOW = new PrettyLogLevel(
      "", "",
      FormattingComponents.of(ANSI.Foreground.BLACK, ANSI.Background.YELLOW, List.of(ANSI.Style.BOLD)),
      FormattingComponents.of(ANSI.Foreground.YELLOW, null, List.of())
    );

    public static final PrettyLogLevel GREEN = new PrettyLogLevel(
      "", "",
      FormattingComponents.of(ANSI.Foreground.WHITE, ANSI.Background.GREEN, List.of(ANSI.Style.BOLD)),
      FormattingComponents.of(ANSI.Foreground.GREEN, null, List.of())
    );

    public static final PrettyLogLevel CYAN = new PrettyLogLevel(
      "", "",
      FormattingComponents.of(ANSI.Foreground.WHITE, ANSI.Background.CYAN, List.of(ANSI.Style.BOLD)),
      FormattingComponents.of(ANSI.Foreground.CYAN, null, List.of())
    );

    public static final PrettyLogLevel BLUE = new PrettyLogLevel(
      "", "",
      FormattingComponents.of(ANSI.Foreground.WHITE, ANSI.Background.BLUE, List.of(ANSI.Style.BOLD)),
      FormattingComponents.of(ANSI.Foreground.BLUE, null, List.of())
    );
    //endregion


}


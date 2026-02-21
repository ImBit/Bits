/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.log;

import xyz.bitsquidd.bits.log.pretty.ANSI;
import xyz.bitsquidd.bits.log.pretty.FormattingComponents;
import xyz.bitsquidd.bits.log.pretty.PrettyLogLevel;

import java.util.List;

public record LogType(
  String id,
  int priority,
  PrettyLogLevel logLevel
) {
    public String format(String input) {
        return logLevel.formatMessage(input);
    }


    public static final LogType DEBUG = new LogType(
      "debug",
      1,
      new PrettyLogLevel(
        "🔎", "DEBUG",
        FormattingComponents.of(ANSI.Foreground.WHITE, ANSI.Background.BLUE, List.of(ANSI.Style.BOLD)),
        FormattingComponents.of(ANSI.Foreground.BLUE, null, List.of())
      )
    );

    public static final LogType SUCCESS = new LogType(
      "success",
      10,
      new PrettyLogLevel(
        "✅", "SUCCESS",
        FormattingComponents.of(ANSI.Foreground.WHITE, ANSI.Background.BRIGHT_GREEN, List.of(ANSI.Style.BOLD)),
        FormattingComponents.of(ANSI.Foreground.BRIGHT_GREEN, null, List.of())
      )
    );

    public static final LogType INFO = new LogType(
      "info",
      20,
      new PrettyLogLevel(
        "ℹ️", "INFO",
        FormattingComponents.of(ANSI.Foreground.WHITE, ANSI.Background.BRIGHT_BLUE, List.of(ANSI.Style.BOLD)),
        FormattingComponents.of(ANSI.Foreground.BRIGHT_BLUE, null, List.of())
      )
    );

    public static final LogType WARNING = new LogType(
      "warning",
      50,
      new PrettyLogLevel(
        "⚠️", "WARN",
        FormattingComponents.of(ANSI.Foreground.WHITE, ANSI.Background.BRIGHT_YELLOW, List.of(ANSI.Style.BOLD)),
        FormattingComponents.of(ANSI.Foreground.BRIGHT_YELLOW, null, List.of())
      )
    );

    public static final LogType ERROR = new LogType(
      "error",
      60,
      new PrettyLogLevel(
        "🚨", "ERROR",
        FormattingComponents.of(ANSI.Foreground.WHITE, ANSI.Background.BRIGHT_RED, List.of(ANSI.Style.BOLD)),
        FormattingComponents.of(ANSI.Foreground.BRIGHT_RED, null, List.of())
      )
    );

    public static final LogType FATAL = new LogType(
      "fatal",
      100,
      new PrettyLogLevel(
        "☠️", "FATAL",
        FormattingComponents.of(ANSI.Foreground.WHITE, ANSI.Background.BRIGHT_RED, List.of(ANSI.Style.BOLD)),
        FormattingComponents.of(ANSI.Foreground.WHITE, ANSI.Background.BRIGHT_RED, List.of(ANSI.Style.BOLD))
      )
    );

}

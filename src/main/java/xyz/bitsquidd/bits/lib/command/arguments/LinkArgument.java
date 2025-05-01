package xyz.bitsquidd.bits.lib.command.arguments;

import org.jetbrains.annotations.NotNull;
import xyz.bitsquidd.bits.lib.command.CommandContext;
import xyz.bitsquidd.bits.lib.command.exceptions.ArgumentParseException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class LinkArgument implements CommandArgument<String> {
    public static final LinkArgument INSTANCE = new LinkArgument();

    private final Set<String> allowedDomains;
    private final Pattern urlPattern;

    public LinkArgument(String... allowedDomains) {
        this.allowedDomains = new HashSet<>(Arrays.asList(allowedDomains));

        if (allowedDomains.length > 0) {
            String domainPattern = String.join("|", allowedDomains)
                    .replace(".", "\\.");

            this.urlPattern = Pattern.compile(
                    "^(https?://(?:www\\.)?|www\\.)(?:" + domainPattern + ")(?:/[\\w\\-./]*)*(?:\\?[\\w=&\\-.]*)?$"
            );
        } else {
            this.urlPattern = Pattern.compile(
                    "^(https?://(?:www\\.)?|www\\.)[a-zA-Z0-9\\-]+(?:\\.[a-zA-Z]{2,})+(?:/[\\w\\-./]*)*(?:\\?[\\w=&\\-.]*)?$"
            );
        }
    }

    @Override
    public @NotNull String getTypeName() {
        if (!allowedDomains.isEmpty()) {
            return "Link(" + String.join("|", allowedDomains) + ")";
        }
        return "Link";
    }

    @Override
    public String parse(@NotNull CommandContext context, int startIndex) throws ArgumentParseException {
        if (startIndex >= context.args.length) {
            throw new ArgumentParseException("No URL provided");
        }

        String url = context.args[startIndex];

        if (url.startsWith("www.")) {
            url = "https://" + url;
        }

        if (!urlPattern.matcher(url).matches()) {
            if (!allowedDomains.isEmpty()) {
                throw new ArgumentParseException(
                        "Invalid URL format: '" + url + "'. URL must start with http://, https://, or www. " +
                                "and must be from one of these domains: " + String.join(", ", allowedDomains)
                );
            } else {
                throw new ArgumentParseException(
                        "Invalid URL format: '" + url + "'. URL must start with http://, https://, or www."
                );
            }
        }

        return url;
    }

    @Override
    public boolean canParseArg(@NotNull CommandContext context, int argIndex) {
        if (argIndex >= context.args.length) {
            return false;
        }

        String url = context.args[argIndex];
        if (url.startsWith("www.")) {
            url = "https://" + url;
        }

        return urlPattern.matcher(url).matches();
    }
}
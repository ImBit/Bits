package xyz.bitsquidd.bits.lib.command.argument;

import com.mojang.brigadier.builder.ArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

@NullMarked
public class ArgumentTypeContainer {
    private final String containerName;
    private final List<ArgumentTypeHolder> heldArguments = new ArrayList<>();
    private final List<ArgumentBuilder<CommandSourceStack, ?>> branch = new ArrayList<>();

    public ArgumentTypeContainer(String containerName) {
        this.containerName = containerName;
    }


    public void add(ArgumentTypeHolder holder) {
        String name = holder.argumentName();
        int nameAmount = getNameAmount(name);
        if (nameAmount > 0) {
            holder = new ArgumentTypeHolder(
                  holder.argumentType(),
                  holder.typeSignature(),
                  containerName + "_" + name + "_" + nameAmount
            );
        }

        branch.add(Commands.argument(
              holder.argumentName(),
              holder.argumentType()
        ));

        heldArguments.add(holder);
    }

    public void addAll(ArgumentTypeContainer container) {
        for (ArgumentTypeHolder argumentTypeHolder : container.getHeldArguments()) {
            add(argumentTypeHolder);
        }
    }

    public List<ArgumentTypeHolder> getHeldArguments() {
        return heldArguments;
    }

    public @Nullable ArgumentTypeHolder getArgumentAtIndex(int index) {
        if (index < 0 || index >= heldArguments.size()) return null;
        return heldArguments.get(index);
    }

    private int getNameAmount(String name) {
        int amount = 0;

        for (ArgumentTypeHolder heldArgument : heldArguments) {
            String suffixRemovedName = heldArgument.argumentName().replaceAll("(_\\d+)$", "");
            if (suffixRemovedName.equals(name)) amount++;
        }

        return amount;
    }

    public @Nullable ArgumentBuilder<CommandSourceStack, ?> getLastArgument() {
        if (branch.isEmpty()) return null;
        return branch.getLast();
    }

    public List<ArgumentBuilder<CommandSourceStack, ?>> getBranch() {
        return branch;
    }

}

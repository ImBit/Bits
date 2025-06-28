package xyz.bitsquidd.bits.lib.command.registering;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.bitsquidd.bits.lib.command.AbstractCommand;

import java.util.*;

public abstract class CommandManager {
    private final Plugin plugin;

    private CommandMap commandMap;
    private final Map<String, BitsCommand> registeredCommands = new HashMap<>();
    private final Set<Command> commandSet = new HashSet<>();
    private final Set<String> registeredPermissions = new HashSet<>();

    protected CommandManager(Plugin plugin) {
        this.plugin = plugin;
        initialiseCommandMap();
    }

    private void initialiseCommandMap() {
        try {
            commandMap = Bukkit.getCommandMap();
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to access command map: " + e.getMessage());
        }
    }

    public abstract void registerCommands();

    public void register(AbstractCommand command) {
        if (plugin == null || commandMap == null) {
            throw new IllegalStateException("The commandMap has not been correctly initialized.");
        }

        String name = command.name;
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Command name cannot be empty");
        }

        if (registeredCommands.containsKey(name.toLowerCase())) {
            plugin.getLogger().warning("Command with name '" + name + "' is already registered. Skipping registration.");
            return;
        }

        registerCommandPermission(command);

        BitsCommand bitsCommand = new BitsCommand(
                name,
                command.description,
                "/" + name,
                new ArrayList<>(List.of(command.aliases)),
                command
        );

        registeredCommands.put(name.toLowerCase(Locale.ROOT), bitsCommand);
        commandSet.add(bitsCommand);
        commandMap.register(plugin.getName().toLowerCase(Locale.ROOT), bitsCommand);
    }

    private void registerCommandPermission(AbstractCommand command) {
        if (command.commandPermission.isEmpty()) {
            return;
        }

        PluginManager pm = Bukkit.getPluginManager();
        String permissionName = command.commandPermission;

        if (pm.getPermission(permissionName) != null) {
            return;
        }

        try {
            Permission permission = new Permission(
                    permissionName,
                    command.description.isEmpty() ? "Command permission" : command.description,
                    PermissionDefault.OP
            );

            pm.addPermission(permission);
            registeredPermissions.add(permissionName);

            registerParentPermissions(permissionName);

            plugin.getLogger().info("Registered permission: " + permissionName);

        } catch (Exception e) {
            plugin.getLogger().warning("Failed to register permission '" + permissionName + "': " + e.getMessage());
        }
    }

    private void registerParentPermissions(String permissionName) {
        PluginManager pm = Bukkit.getPluginManager();

        String[] parts = permissionName.split("\\.");
        if (parts.length > 2) {
            StringBuilder parentBuilder = new StringBuilder();

            for (int i = 0; i < parts.length - 1; i++) {
                if (i > 0) parentBuilder.append(".");
                parentBuilder.append(parts[i]);

                String parentPermission = parentBuilder.toString();

                if (pm.getPermission(parentPermission) == null) {
                    Permission parent = new Permission(
                            parentPermission,
                            "Parent permission for " + parentPermission + " commands",
                            PermissionDefault.OP
                    );
                    pm.addPermission(parent);
                    registeredPermissions.add(parentPermission);
                }

                String wildcardPermission = parentPermission + ".*";
                if (pm.getPermission(wildcardPermission) == null) {
                    Permission wildcard = new Permission(
                            wildcardPermission,
                            "Wildcard permission for " + parentPermission + " commands",
                            PermissionDefault.OP
                    );
                    pm.addPermission(wildcard);
                    registeredPermissions.add(wildcardPermission);
                }

                Permission parentPerm = pm.getPermission(parentPermission);
                Permission wildcardPerm = pm.getPermission(wildcardPermission);

                if (parentPerm != null) {
                    parentPerm.getChildren().put(permissionName, true);
                    parentPerm.recalculatePermissibles();
                }

                if (wildcardPerm != null) {
                    wildcardPerm.getChildren().put(permissionName, true);
                    wildcardPerm.recalculatePermissibles();
                }
            }
        }
    }

    public void unregisterAll() {
        CommandMap commandMap = Bukkit.getCommandMap();
        PluginManager pm = Bukkit.getPluginManager();

        registeredCommands.keySet().forEach(s -> {
            Command command = commandMap.getCommand(s);
            if (command != null) {
                command.unregister(commandMap);
            }
        });

        registeredPermissions.forEach(permissionName -> {
            Permission permission = pm.getPermission(permissionName);
            if (permission != null) {
                pm.removePermission(permission);
            }
        });

        registeredCommands.clear();
        commandSet.clear();
        registeredPermissions.clear();

        Bukkit.reloadCommandAliases();
        plugin.getLogger().info("Unregistered all commands and permissions");
    }

    @NotNull
    public Set<Command> getCommands() {
        return Collections.unmodifiableSet(commandSet);
    }

    @Nullable
    public BitsCommand getCommand(String name) {
        return registeredCommands.get(name.toLowerCase(Locale.ROOT));
    }

    @NotNull
    public Set<String> getRegisteredPermissions() {
        return Collections.unmodifiableSet(registeredPermissions);
    }
}
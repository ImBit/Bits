---
name: documentation
description: Guide for creating Javadocs.
---

# Documentation Standards
This document defines how Javadoc should be written across the Bits codebase.
Agents and contributors must follow these standards when adding or modifying public API.

---

## Where to Document
Javadoc is **required** on all `public` and `protected` declarations.

> Platform modules (e.g. `Paper`, `Velocity`) implement API contracts defined in `API`.
> They do not need Javadoc on inherited methods unless a method or class introduces genuinely
> new public surface - for example, a new utility class, a new method not present in the API
> contract, or an overload with different behaviour or parameters.

`@Override` methods outside platform modules do not require Javadoc if the parent documentation
is complete and accurate. When `{@inheritDoc}` is used, it must be a conscious decision - do not
use it as a default. If the overriding method changes behaviour, adds preconditions, or narrows
the contract, write full Javadoc instead.

---

## General Rules
- Write in **British English**, using full sentences with correct punctuation.
    - The **first sentence** is the summary - it appears in overview tables. Keep it concise and specific.
    - If additional explanation is needed, add a blank line followed by `<p>` on its own line, then continue on the next line.
- The `@since` tag is **required** on every public API type and member.
    - Always match the current version in `gradle.properties` under `bits_version`.
- Line length should be limited to 100 characters for readability.
    - If a sentence exceeds this, break it into multiple lines at logical points (e.g., after clauses or before conjunctions).
- All classes are `@NotNull` by default unless otherwise specified. No need to document nullability.
- **Do not** document fields or constants unless they are part of a formally documented constant set.
- **Do not** add Javadoc to `private` members or internal implementation classes.
- Prefer `{@link}` over `{@code}` when referring to other API members. Use `{@code}` only for literals, keywords, or values that are not referenceable types or methods.
- Use `@see` where it would help the reader navigate to related types, methods, or resources.
- When linking to external classes, use the fully qualified classname to avoid extra imports and void ambiguity.

---

## Tag Order
Tags must always appear in this order at the bottom of the comment. Leave a blank line between
each group of tags of a different kind:

```
@param      (one per parameter, in declaration order; include <T> for type parameters)
@return     (for non-void methods)
@throws     (one per exception this method can throw, most specific first)
@deprecated (if applicable)
@see        (if applicable)
@since
```

---

## Element Reference
### Classes and Interfaces
Every public class and interface in `API` must have:

- A **summary sentence** describing its purpose and responsibility.
- A `<p>` continuation block if behaviour, contract, or lifecycle details need explaining.
- A `{@code}` usage example in a `<pre>` block.
- A `@since` tag.

```java
/**
 * Manages the registration and lifecycle of platform-specific command executors.
 *
 * <p>
 * Implementations are responsible for binding annotated command classes to the
 * underlying platform dispatcher. Commands registered through this manager are
 * automatically unregistered on shutdown.
 *
 * <p>
 * Example usage:
 * <pre>{@code
 * CommandManager manager = platform.commandManager();
 * manager.register(new MyCommand());
 * }</pre>
 *
 * @see xyz.bits.command.CommandExecutor
 *
 * @since 0.0.1
 */
public interface CommandManager {
    ...
}
```

---

### Generic Types
Document type parameters using `@param <T>` at the type or method level, placed before any
`@return` tag and in declaration order alongside other `@param` entries.

```java
/**
 * A registry that maps keys to values of a given type.
 *
 * @param <K> the type of keys maintained by this registry
 * @param <V> the type of registered values
 *
 * @since 0.0.10
 */
public interface Registry<K, V> {
    ...
}
```

```java
/**
 * Returns the first value in this registry matching the given predicate.
 *
 * @param <V>       the type of value to search for
 * @param predicate the predicate to test against each value, not null
 *
 * @return the first matching value, or an empty optional if none match
 *
 * @since 0.0.10
 */
<V> Optional<V> findFirst(Predicate<V> predicate);
```

---

### Abstract Classes
Same requirements as interfaces. Additionally, document any `abstract` methods with their
expected contract - what the implementor must guarantee.

```java
/**
 * Base implementation of a platform-aware plugin entry point.
 *
 * <p>
 * Subclasses must implement {@link #onEnable()} and {@link #onDisable()} to perform
 * platform-specific startup and teardown logic. The base class handles manager
 * registration and lifecycle ordering.
 *
 * <p>
 * Example usage:
 * <pre>{@code
 * public class MyPlugin extends BitPlugin {
 *     @Override
 *     protected void onEnable() {
 *         getLogger().info("Plugin enabled.");
 *     }
 * }
 * }</pre>
 *
 * @since 0.0.10
 */
public abstract class BitPlugin {
    ...
}
```

---

### Methods and Constructors
Every public and protected method must have:

- A **summary sentence** stating what the method does (not how).
- `@param` for every parameter - describe the meaning, not just the type. Note null-safety where relevant.
- `@return` for any non-void return - describe what is returned, not just its type.
- `@throws` for **every exception this method can throw**, both checked and unchecked. Document the condition under which each is thrown.
- `@since`.

```java
/**
 * Registers a command executor with this manager.
 *
 * <p>
 * The executor is inspected for annotated command methods at registration time.
 * Duplicate registrations for the same command name will replace the prior binding.
 *
 * @param executor the command executor to register, not null

 * @throws IllegalArgumentException if the executor contains no valid command annotations
 * @throws IllegalStateException    if this manager has already been shut down
 *
 * @since 0.0.10
 */
void register(Object executor);
```

```java
/**
 * Returns the platform-specific logger for this plugin instance.
 *
 * @return the logger for this plugin
 *
 * @since 0.0.10
 */
Logger getLogger();
```

---

### Annotations
Document public annotations only when their purpose or behaviour is non-obvious. A simple
marker annotation with a self-explanatory name does not require Javadoc. When documented,
follow the same class-level standards - summary sentence, `<p>` continuation if needed,
and `@since`.

```java
/**
 * Marks a method as a command handler to be registered by the {@link CommandManager}.
 *
 * <p>
 * The annotated method must be public, non-static, and accept a {@link CommandSender}
 * as its first argument. Methods that do not meet these requirements will be ignored
 * during registration.
 *
 * @since 0.0.10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {
    ...
}
```

---

### Records
Document at the **type level** using `@param` for each component.
Do not add separate Javadoc blocks to the record's accessor methods.

```java
/**
 * Represents an immutable snapshot of a formatted text component and its raw value.
 *
 * @param raw       the unformatted source string, not null
 * @param formatted the platform-formatted component derived from {@code raw}, not null
 *
 * @since 0.0.10
 */
public record TextSnapshot(String raw, Component formatted) {
}
```

---

### Enums
Document at the **type level** only.
Individual constants do not require Javadoc unless a constant has non-obvious behaviour that must be called out.

```java
/**
 * Represents the supported target platforms for Bits module deployment.
 *
 * @since 0.0.10
 */
public enum BitsPlatform {
    PAPER,
    VELOCITY
}
```

If a constant has notable behaviour worth documenting, a single-line comment is acceptable:

```java
public enum BitsPlatform {
    /** Standard Paper/Spigot server environment. */
    PAPER,
    VELOCITY
}
```

---

### Sealed Classes and Interfaces
Document sealed types using the same class-level standards. Do not list permitted subtypes
in the Javadoc - the compiler and IDE surface these automatically. Focus the documentation
on the contract shared across all permitted implementations.

```java
/**
 * Represents the result of a command execution attempt.
 *
 * <p>
 * All permitted subtypes carry a human-readable message accessible via
 * {@link #message()}. Consumers should pattern-match on the concrete subtype
 * to determine the outcome.
 *
 * @since 0.0.10
 */
public sealed interface CommandResult permits CommandResult.Success, CommandResult.Failure {
    ...
}
```

---

### Deprecated API
Mark deprecated elements with both the `@Deprecated` annotation and a `@deprecated` Javadoc
tag. The `@since` value must reflect when the element was **originally introduced**, not when
it was deprecated. Include a separate prose note stating the version in which the element was
deprecated and a `{@link}` to the recommended replacement.

```java
/**
 * Returns the legacy command dispatcher for this platform.
 *
 * @return the legacy dispatcher
 *
 * @deprecated As of 0.0.14, replaced by {@link #commandManager()}, which provides
 *             a unified registration API across all platforms. This method will be
 *             removed in a future release.
 *
 * @since 0.0.1
 */
@Deprecated
LegacyDispatcher legacyDispatcher();
```

---

## `@since` Version Policy
The `@since` value must always match `bits_version` in `gradle.properties` at the time the
element is introduced. Do not backfill speculative versions or copy versions from nearby code.

```properties
# gradle.properties
bits_version=0.0.10
```

```java
@since 0.0.10
```

---

## Forbidden Patterns
The following patterns must not appear in any Javadoc:

| Pattern                                                                    | Why it is forbidden                                        |
|----------------------------------------------------------------------------|------------------------------------------------------------|
| `@param foo the foo` - restating the parameter name                        | Adds no information                                        |
| `@return the result` - restating the return type                           | Adds no information                                        |
| Javadoc on `private` members                                               | Not part of public API surface                             |
| Javadoc in `Paper` or `Velocity` on inherited methods                      | Inherited docs flow from `API`                             |
| `{@inheritDoc}` used as a default on `@Override` methods                   | Must be a conscious decision; write full Javadoc if the    |
|                                                                            | contract changes or the parent doc is insufficient         |
| `<p>` at the start of a continuation sentence (inline)                     | Must be on its own line before the paragraph               |
| `@throws Exception` or `@throws Throwable` without specificity             | Too broad; use the actual thrown type                      |
| Omitting `@throws` for any exception this method can throw                 | All throwable exceptions must be documented                |
| Omitting `@since` on any new public API element                            | Required without exception                                 |
| Copying Javadoc verbatim between overloads without adapting it             | Misleads consumers                                         |
| Using `{@code}` to reference a type or method that could use `{@link}`     | Prefer `{@link}` for navigable references                  |
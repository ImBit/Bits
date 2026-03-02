/*
 * This file is part of Bits, licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2024-2026 ImBit
 *
 * Enjoy the Bits and Bobs :)
 */

package xyz.bitsquidd.bits.wrapper;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * A wrapper class for holding a URL / link as a string. Usually used for command parsing of strings with spaces.
 */
public final class Link {
    public final String value;

    private Link(String value) {
        this.value = value;
    }

    public static Link of(String value) {
        return new Link(value);
    }

    public URL toURL() throws MalformedURLException {
        return URI.create(value).toURL();
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Link other) && other.value.equals(this.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}

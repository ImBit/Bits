/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.paper.location.containable.region;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import xyz.bitsquidd.bits.paper.location.wrapper.BlockPos;
import xyz.bitsquidd.bits.paper.location.wrapper.Locatable;

import java.util.Objects;

public final class EllipsoidRegion extends Region {
    private final BlockPos center;
    private final double radiusX;
    private final double radiusY;
    private final double radiusZ;

    public EllipsoidRegion(World world, BlockPos center, double radiusX, double radiusY, double radiusZ) {
        super(world);
        if (radiusX <= 0 || radiusY <= 0 || radiusZ <= 0) throw new IllegalArgumentException("Radii must be positive");
        this.center = center;
        this.radiusX = radiusX;
        this.radiusY = radiusY;
        this.radiusZ = radiusZ;
    }

    public EllipsoidRegion(World world, BlockPos center, double radius) {
        this(world, center, radius, radius, radius);
    }

    public EllipsoidRegion(Location center, double radiusX, double radiusY, double radiusZ) {
        this(center.getWorld(), BlockPos.of(center), radiusX, radiusY, radiusZ);
    }

    public EllipsoidRegion(Location center, double radius) {
        this(center, radius, radius, radius);
    }

    //region Java Object Overrides
    @Override
    public String toString() {
        return "EllipsoidRegion{center=" + center + ", rx=" + radiusX + ", ry=" + radiusY + ", rz=" + radiusZ + "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, center, radiusX, radiusY, radiusZ);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof EllipsoidRegion other)) return false;
        return Objects.equals(world, other.world) &&
          Objects.equals(center, other.center) &&
          Double.compare(radiusX, other.radiusX) == 0 &&
          Double.compare(radiusY, other.radiusY) == 0 &&
          Double.compare(radiusZ, other.radiusZ) == 0;
    }
    //endregion

    @Override
    public boolean contains(Locatable locatable) {
        if (locatable == null) return false;

        Vector v = locatable.asVector();
        double dx = (v.getX() - center.x) / radiusX;
        double dy = (v.getY() - center.y) / radiusY;
        double dz = (v.getZ() - center.z) / radiusZ;
        return (dx * dx) + (dy * dy) + (dz * dz) <= 1.0;
    }

    @Override
    public BlockPos centre() {
        return center;
    }

    @Override
    public BlockPos min() {
        return BlockPos.of(center.x - radiusX, center.y - radiusY, center.z - radiusZ);
    }

    @Override
    public BlockPos max() {
        return BlockPos.of(center.x + radiusX, center.y + radiusY, center.z + radiusZ);
    }


    @Override
    public EllipsoidRegion expand(double x, double y, double z) {
        return new EllipsoidRegion(world, center, radiusX + x, radiusY + y, radiusZ + z);
    }

    @Override
    public EllipsoidRegion shift(double x, double y, double z) {
        return new EllipsoidRegion(
          world,
          BlockPos.of(center.x + x, center.y + y, center.z + z),
          radiusX, radiusY, radiusZ
        );
    }

    public BlockPos getCenter() {
        return center;
    }

    public double getRadiusX() {
        return radiusX;
    }

    public double getRadiusY() {
        return radiusY;
    }

    public double getRadiusZ() {
        return radiusZ;
    }

}
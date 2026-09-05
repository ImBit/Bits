/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.paper.location.containable.region;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3d;

import xyz.bitsquidd.bits.paper.location.containable.area.visualisation.Center;
import xyz.bitsquidd.bits.paper.location.containable.area.visualisation.Corner;
import xyz.bitsquidd.bits.paper.location.containable.area.visualisation.Edge;
import xyz.bitsquidd.bits.paper.location.containable.area.visualisation.impl.RegionVisualiser;
import xyz.bitsquidd.bits.paper.location.wrapper.BlockPos;
import xyz.bitsquidd.bits.paper.location.wrapper.Locatable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


public final class CylinderRegion extends Region {
    private final BlockPos centerBottom;
    private final double radius;
    private final double height;

    @JsonCreator
    public CylinderRegion(@JsonProperty("world") World world, @JsonProperty("centerBottom") BlockPos centerBottom, @JsonProperty("radius") double radius, @JsonProperty("height") double height) {
        this(world, centerBottom, radius, height, new Quaternionf());
    }

    public CylinderRegion(World world, BlockPos centerBottom, double radius, double height, Quaternionf rotation) {
        super(world, rotation);
        if (radius <= 0) throw new IllegalArgumentException("Radius must be positive");
        if (height <= 0) throw new IllegalArgumentException("Height must be positive");
        this.centerBottom = centerBottom;
        this.radius = radius;
        this.height = height;
    }

    public CylinderRegion(Location centerBottom, double radius, double height) {
        this(centerBottom.getWorld(), BlockPos.of(centerBottom), radius, height);
    }

    /**
     * @since 0.0.26
     **/
    public CylinderRegion(Location centerBottom, double radius, double height, Quaternionf rotation) {
        this(centerBottom.getWorld(), BlockPos.of(centerBottom), radius, height, rotation);
    }

    //region Java Object Overrides
    @Override
    public String toString() {
        return "CylinderRegion{centerBottom=" + centerBottom + ", radius=" + radius + ", height=" + height + ", rotation=" + rotation + "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, centerBottom, radius, height, rotation);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CylinderRegion other)) return false;
        return Objects.equals(world, other.world) &&
          Objects.equals(centerBottom, other.centerBottom) &&
          Double.compare(radius, other.radius) == 0 &&
          Double.compare(height, other.height) == 0 &&
          rotation.equals(other.rotation, 1.0e-6f);
    }
    //endregion

    @Override
    public boolean contains(Locatable locatable) {
        if (locatable == null) return false;

        Vector v = locatable.asVector();
        BlockPos c = center();
        Vector3d local = toLocal(v.getX(), v.getY(), v.getZ(), c.x, c.y, c.z);

        return Math.abs(local.y) <= height / 2.0 && (local.x * local.x + local.z * local.z) <= (radius * radius);
    }

    @Override
    public BlockPos center() {
        return BlockPos.of(centerBottom.x, centerBottom.y + height / 2, centerBottom.z);
    }

    @Override
    public BlockPos min() {
        return BlockPos.of(centerBottom.x - radius, centerBottom.y, centerBottom.z - radius);
    }

    @Override
    public BlockPos max() {
        return BlockPos.of(centerBottom.x + radius, centerBottom.y + height, centerBottom.z + radius);
    }


    @Override
    public Optional<BlockPos> getRandomLocation() {
        double angle = Math.random() * 2 * Math.PI;
        double distance = Math.random() * radius;
        double x = distance * Math.cos(angle);
        double z = distance * Math.sin(angle);
        double y = Math.random() * height - height / 2.0;

        Vector3d local = new Vector3d(x, y, z);
        rotation.transform(local);

        BlockPos c = center();
        return Optional.of(BlockPos.of(c.x + local.x, c.y + local.y, c.z + local.z));
    }

    @Override
    public CylinderRegion expand(double x, double y, double z) {
        // TODO independent radial and axial expansion
        double radialExpand = Math.max(x, z);
        return new CylinderRegion(
          world,
          BlockPos.of(centerBottom.x, centerBottom.y - y, centerBottom.z),
          radius + radialExpand,
          height + y * 2,
          rotation
        );
    }

    @Override
    public CylinderRegion shift(double x, double y, double z) {
        return new CylinderRegion(
          world,
          BlockPos.of(centerBottom.x + x, centerBottom.y + y, centerBottom.z + z),
          radius, height, rotation
        );
    }


    public BlockPos getCenterBottom() {
        return centerBottom;
    }

    public double getRadius() {
        return radius;
    }

    public double getHeight() {
        return height;
    }


    @Override
    protected Set<RegionVisualiser> createVisualiser() {
        Set<RegionVisualiser> visualisers = new HashSet<>();

        int edges = 8;
        BlockPos c = center();

        // TODO: Not rotation-aware.
        visualisers.add(Edge.arc(BlockPos.of(c.x, min().y, c.z), radius, 0, 360, 0, 0));
        visualisers.add(Edge.arc(BlockPos.of(c.x, max().y, c.z), radius, 0, 360, 0, 0));

        for (int i = 0; i < edges; i++) {
            double angle = 2 * Math.PI * i / edges;
            double x = c.x + radius * Math.cos(angle);
            double z = c.z + radius * Math.sin(angle);
            visualisers.add(Edge.straight(
              BlockPos.of(x, min().y, z),
              BlockPos.of(x, max().y, z)
            ));
        }

        visualisers.add(Corner.of(BlockPos.of(c.x, min().y, c.z)));
        visualisers.add(Corner.of(BlockPos.of(c.x, max().y, c.z)));
        visualisers.add(Center.of(c));

        return visualisers;
    }

}

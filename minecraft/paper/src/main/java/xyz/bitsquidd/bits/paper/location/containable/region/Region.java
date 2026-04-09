/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.paper.location.containable.region;

import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.joml.Vector3d;

import xyz.bitsquidd.bits.paper.location.containable.Containable;
import xyz.bitsquidd.bits.paper.location.wrapper.BlockLoc;
import xyz.bitsquidd.bits.paper.location.wrapper.BlockPos;
import xyz.bitsquidd.bits.paper.util.bukkit.runnable.Runnables;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Represents a region in a world: a defined area that can contain locations and blocks.
 * Implementations are immutable.
 *
 * @since 0.0.13
 */
public abstract class Region implements Containable {
    protected final World world;


    protected Region(World world) {
        this.world = world;
    }


    //region Containment
    public final boolean contains(Location location) {
        if (location == null) return false;
        if (location.getWorld() == null || !location.getWorld().equals(world)) return false;

        return contains(BlockPos.of(location));
    }

    //endregion


    //region Blocks

    /**
     * Returns the set of blocks where the center points are contained within this region.
     * <p>
     * Not abstract as it may be optimised by some implementations!
     * This default implementation simply checks all blocks within the bounding box of the region.
     */
    protected Set<BlockLoc> getBlockLocs() {
        Set<BlockLoc> blockLocs = new HashSet<>();

        BlockPos min = min();
        BlockPos max = max();

        int minXi = (int)Math.floor(min.x);
        int minYi = (int)Math.floor(min.y);
        int minZi = (int)Math.floor(min.z);
        int maxXi = (int)Math.ceil(max.x);
        int maxYi = (int)Math.ceil(max.y);
        int maxZi = (int)Math.ceil(max.z);


        for (int x = minXi; x <= maxXi; x++) {
            for (int y = minYi; y <= maxYi; y++) {
                for (int z = minZi; z <= maxZi; z++) {
                    if (contains(BlockPos.of(x + 0.5, y + 0.5, z + 0.5))) blockLocs.add(BlockLoc.of(x, y, z));
                }
            }
        }

        return blockLocs;
    }

    public final Set<Block> getBlocks() {
        return getBlocks(block -> true);
    }

    public final Set<Block> getBlocks(Predicate<Block> filter) {
        return getBlockLocs().stream()
          .map(loc -> loc.asBlock(world))
          .filter(filter)
          .collect(java.util.stream.Collectors.toSet());
    }

    public final CompletableFuture<Set<Block>> getBlocksAsync() {
        return getBlocksAsync(block -> true);
    }

    public final CompletableFuture<Set<Block>> getBlocksAsync(Predicate<BlockData> filter) {
        Set<BlockLoc> blockLocs = getBlockLocs();

        // Group BlockLocs by chunk coordinate
        Map<Long, List<BlockLoc>> byChunk = new HashMap<>();
        for (BlockLoc loc : blockLocs) {
            int chunkX = loc.x() >> 4;
            int chunkZ = loc.z() >> 4;
            long key = ((long)chunkX << 32) | (chunkZ & 0xFFFFFFFFL);
            byChunk.computeIfAbsent(key, k -> new ArrayList<>()).add(loc);
        }

        // For each chunk, load it async and take a snapshot
        List<CompletableFuture<List<BlockLoc>>> futures = byChunk.values().stream()
          .map(locs -> {
              int chunkX = locs.getFirst().x() >> 4;
              int chunkZ = locs.getFirst().z() >> 4;

              return world.getChunkAtAsync(chunkX, chunkZ)
                .thenApplyAsync(chunk -> {
                    ChunkSnapshot snapshot = chunk.getChunkSnapshot();
                    List<BlockLoc> passing = new ArrayList<>();

                    for (BlockLoc loc : locs) {
                        int lx = loc.x() & 0xF;
                        int lz = loc.z() & 0xF;
                        BlockData data = snapshot.getBlockData(lx, loc.y(), lz);
                        if (filter.test(data)) passing.add(loc);
                    }
                    return passing;
                });
          })
          .toList();

        // Combine all passing locs, then hop back to main thread to get refs
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
          .thenApply(v -> futures.stream()
            .flatMap(f -> f.join().stream())
            .collect(Collectors.toSet())
          )
          .thenApplyAsync(
            passingLocs -> passingLocs.stream()
              .map(loc -> world.getBlockAt(loc.x(), loc.y(), loc.z()))
              .collect(Collectors.toSet()),
            Runnables::runOnMainThread
          );
    }


    //endregion


    //region Basic getters
    public final World world() {
        return world;
    }

    public abstract BlockPos centre();

    public abstract BlockPos min();

    public abstract BlockPos max();
    //endregion


    //region Mutators
    public final Region expand(Vector3d amount) {
        return expand(amount.x, amount.y, amount.z);
    }

    public abstract Region expand(double x, double y, double z);


    public final Region shift(Vector3d amount) {
        return shift(amount.x, amount.y, amount.z);
    }

    public abstract Region shift(double x, double y, double z);
    //endregion

}

/*
 * This file is part of a Bit libraries package.
 * Licensed under the GNU Lesser General Public License v3.0.
 *
 * Copyright (c) 2023-2026 ImBit
 */

package xyz.bitsquidd.bits.mc.animation;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import xyz.bitsquidd.bits.lifecycle.builder.Buildable;

public record AnimationPose(
  Vector3f translation,
  Quaternionf rotation,
  Vector3f scale
) {

    /**
     * Linearly interpolates translation and scale, spherically interpolates rotation.
     */
    public static AnimationPose interpolate(AnimationPose a, AnimationPose b, float t) {
        Vector3f translation = new Vector3f(a.translation).lerp(b.translation, t);
        Quaternionf rotation = new Quaternionf(a.rotation).slerp(b.rotation, t);
        Vector3f scale = new Vector3f(a.scale).lerp(b.scale, t);
        return new AnimationPose(translation, rotation, scale);
    }

    public static AnimationPose identity() {
        return builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder implements Buildable<AnimationPose> {
        private Vector3f translation = new Vector3f();
        private Quaternionf rotation = new Quaternionf();
        private Vector3f scale = new Vector3f(1);


        //region Translation
        public Builder addTranslation(float x, float y, float z) {
            this.translation = new Vector3f(x, y, z);
            return this;
        }

        public Builder addTranslation(Vector3f translation) {
            return addTranslation(translation.x, translation.y, translation.z);
        }

        public Builder setTranslation(float x, float y, float z) {
            this.translation.add(x, y, z);
            return this;
        }

        public Builder setTranslation(Vector3f translation) {
            return setTranslation(translation.x, translation.y, translation.z);
        }
        //endregion


        //region Rotation
        public Builder rotation(Quaternionf rotation) {
            this.rotation = new Quaternionf(rotation);
            return this;
        }

        public Builder rotateX(float angle) {
            this.rotation.rotateX(angle);
            return this;
        }

        public Builder setRotateX(float angle) {
            this.rotation.rotateX(angle - this.rotation.getEulerAnglesXYZ(new Vector3f()).x);
            return this;
        }

        public Builder rotateY(float angle) {
            this.rotation.rotateY(angle);
            return this;
        }

        public Builder setRotateY(float angle) {
            this.rotation.rotateY(angle - this.rotation.getEulerAnglesXYZ(new Vector3f()).y);
            return this;
        }

        public Builder rotateZ(float angle) {
            this.rotation.rotateZ(angle);
            return this;
        }

        public Builder setRotateZ(float angle) {
            this.rotation.rotateZ(angle - this.rotation.getEulerAnglesXYZ(new Vector3f()).z);
            return this;
        }
        //endregion


        //region Scale
        public Builder scale(float x, float y, float z) {
            this.scale = new Vector3f(x, y, z);
            return this;
        }

        public Builder scale(Vector3f scale) {
            return scale(scale.x, scale.y, scale.z);
        }

        public Builder scale(float uniform) {
            return scale(uniform, uniform, uniform);
        }
        //endregion


        public AnimationPose build() {
            return new AnimationPose(translation, rotation, scale);
        }

    }

}
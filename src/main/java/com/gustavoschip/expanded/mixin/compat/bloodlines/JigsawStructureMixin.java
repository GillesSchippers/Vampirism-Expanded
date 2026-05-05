/*
 * MIT License
 *
 * Copyright (c) 2026 Gilles Schippers
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.gustavoschip.expanded.mixin.compat.bloodlines;

import com.thedrofdoctoring.bloodlines.world.structures.ZealotShrineStructure;
import java.util.Optional;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Restriction(require = { @Condition(type = Condition.Type.MOD, value = "bloodlines"), @Condition(type = Condition.Type.MOD, value = "sable") })
@Mixin(value = JigsawStructure.class, priority = 1100, remap = true)
public class JigsawStructureMixin {

    @Inject(method = "findGenerationPoint", at = @At("HEAD"), cancellable = true)
    private void onFindGenerationPoint(Structure.GenerationContext context, CallbackInfoReturnable<@NotNull Optional<Structure.GenerationStub>> cir) {
        if (!((Object) this instanceof ZealotShrineStructure)) {
            return;
        }

        try {
            ChunkPos chunkPos = context.chunkPos();
            int midX = chunkPos.getMiddleBlockX();
            int midZ = chunkPos.getMiddleBlockZ();

            int y = context.chunkGenerator().getBaseHeight(midX, midZ, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());

            int minY = context.heightAccessor().getMinBuildHeight();
            int maxY = context.heightAccessor().getMaxBuildHeight();

            if (y < minY || y >= maxY) {
                cir.setReturnValue(Optional.empty());
            }
        } catch (Exception e) {
            cir.setReturnValue(Optional.empty());
        }
    }
}

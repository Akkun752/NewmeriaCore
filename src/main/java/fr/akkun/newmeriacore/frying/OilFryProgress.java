package fr.akkun.newmeriacore.frying;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Per-item-entity frying progress while it sits in a heated oil cauldron. {@code targetTicks} is
 * rolled once (10-15s) the first time the item is progressing; -1 means "not rolled yet". Progress
 * only advances while the item is in oil AND a heat source is present below - it simply isn't
 * incremented otherwise, so losing the heat source pauses (never resets) the timer.
 */
public record OilFryProgress(int progressTicks, int targetTicks) {
    public static final OilFryProgress NONE = new OilFryProgress(0, -1);

    public static final MapCodec<OilFryProgress> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("progress_ticks").forGetter(OilFryProgress::progressTicks),
            Codec.INT.fieldOf("target_ticks").forGetter(OilFryProgress::targetTicks)
    ).apply(instance, OilFryProgress::new));
}

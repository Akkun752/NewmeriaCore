package fr.akkun.newmeriacore.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.akkun.newmeriacore.NewmeriaCore;
import fr.akkun.newmeriacore.entity.Duck;
import net.minecraft.client.model.animal.chicken.AdultChickenModel;
import net.minecraft.client.model.animal.chicken.BabyChickenModel;
import net.minecraft.client.model.animal.chicken.ChickenModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

/**
 * Renders the Duck with the vanilla (temperate) chicken models, just with its own textures. Unlike
 * the vanilla ChickenRenderer this ignores the entity's chicken variant entirely, so a Duck never
 * switches to the cold chicken model.
 *
 * <p>Texture pixel dimensions must match the vanilla models' own UV layout: 64x32 for the adult
 * ones ({@code duck.png}, {@code golden_duck.png}), 16x16 for {@code duck_baby.png}.
 */
public class DuckRenderer extends MobRenderer<Duck, DuckRenderState, ChickenModel> {
    private static final Identifier TEXTURE = texture("duck");
    private static final Identifier BABY_TEXTURE = texture("duck_baby");
    private static final Identifier GOLDEN_TEXTURE = texture("golden_duck");

    private final ChickenModel adultModel;
    private final ChickenModel babyModel;

    public DuckRenderer(EntityRendererProvider.Context context) {
        super(context, new AdultChickenModel(context.bakeLayer(ModelLayers.CHICKEN)), 0.3F);
        this.adultModel = this.model;
        this.babyModel = new BabyChickenModel(context.bakeLayer(ModelLayers.CHICKEN_BABY));
    }

    private static Identifier texture(String name) {
        return Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "textures/entity/duck/" + name + ".png");
    }

    @Override
    public void submit(DuckRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        this.model = state.isBaby ? this.babyModel : this.adultModel;
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    public DuckRenderState createRenderState() {
        return new DuckRenderState();
    }

    @Override
    public void extractRenderState(Duck entity, DuckRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.flap = Mth.lerp(partialTicks, entity.oFlap, entity.flap);
        state.flapSpeed = Mth.lerp(partialTicks, entity.oFlapSpeed, entity.flapSpeed);
        state.golden = entity.isGolden();
    }

    // Only adults can be gilded, so there is no golden baby texture.
    @Override
    public Identifier getTextureLocation(DuckRenderState state) {
        if (state.isBaby) {
            return BABY_TEXTURE;
        }
        return state.golden ? GOLDEN_TEXTURE : TEXTURE;
    }
}

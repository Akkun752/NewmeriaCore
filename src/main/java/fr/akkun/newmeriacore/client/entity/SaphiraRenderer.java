package fr.akkun.newmeriacore.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fr.akkun.newmeriacore.NewmeriaCore;
import net.minecraft.client.model.monster.dragon.EnderDragonModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EnderDragonRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;

/**
 * The vanilla Ender Dragon renderer with Saphira's own skin. Vanilla hardcodes its textures in
 * private constants, so the body of {@code EnderDragonRenderer#submit} is reproduced here with
 * these differences: Saphira's texture, no glowing-eyes layer (vanilla's is purple and there is no
 * Saphira version of it), and no death rays (vanilla's are magenta and private).
 *
 * <p>{@code saphira.png} must follow the vanilla dragon texture layout (256x256).
 */
public class SaphiraRenderer extends EnderDragonRenderer {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "textures/entity/saphira/saphira.png");
    // Vanilla's dissolve mask works on any dragon-shaped texture.
    private static final Identifier EXPLODING_MASK = Identifier.withDefaultNamespace("textures/entity/enderdragon/dragon_exploding.png");
    private static final RenderType DYING_RENDER_TYPE = RenderTypes.entityCutoutDissolve(TEXTURE, EXPLODING_MASK);

    private final EnderDragonModel model;

    public SaphiraRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new EnderDragonModel(context.bakeLayer(ModelLayers.ENDER_DRAGON));
    }

    @Override
    public void submit(EnderDragonRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        float yRot = state.getHistoricalPos(7).yRot();
        float pitch = (float) (state.getHistoricalPos(5).y() - state.getHistoricalPos(10).y());
        poseStack.mulPose(Axis.YP.rotationDegrees(-yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch * 10.0F));
        poseStack.translate(0.0F, 0.0F, 1.0F);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0F, -1.501F, 0.0F);
        if (state.deathTime > 0.0F) {
            int color = ARGB.white(1.0F - state.deathTime / 200.0F);
            submitNodeCollector.submitModel(this.model, state, poseStack, DYING_RENDER_TYPE, state.lightCoords, OverlayTexture.NO_OVERLAY, color, null, state.outlineColor, null);
        } else {
            int overlayCoords = OverlayTexture.pack(0.0F, state.hasRedOverlay);
            submitNodeCollector.submitModel(this.model, state, poseStack, TEXTURE, state.lightCoords, overlayCoords, state.outlineColor, null);
        }
        poseStack.popPose();

        if (state.beamOffset != null) {
            submitCrystalBeams((float) state.beamOffset.x, (float) state.beamOffset.y, (float) state.beamOffset.z,
                    state.ageInTicks, poseStack, submitNodeCollector, state.lightCoords);
        }

        // What the base EntityRenderer#submit does (super.submit would draw the vanilla dragon).
        this.submitNameDisplay(state, poseStack, submitNodeCollector, camera);
    }
}

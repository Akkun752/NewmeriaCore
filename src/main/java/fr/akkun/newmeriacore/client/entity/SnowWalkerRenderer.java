package fr.akkun.newmeriacore.client.entity;

import fr.akkun.newmeriacore.NewmeriaCore;
import fr.akkun.newmeriacore.entity.SnowWalker;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.zombie.BabyZombieModel;
import net.minecraft.client.model.monster.zombie.ZombieModel;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

/**
 * Renders the Snow Walker with the vanilla zombie model, but picks between two textures
 * (see {@link SnowWalker#getTextureVariant()}) instead of always drawing the same one.
 */
public class SnowWalkerRenderer extends AbstractZombieRenderer<SnowWalker, SnowWalkerRenderState, ZombieModel<SnowWalkerRenderState>> {
    private static final Identifier[] TEXTURES = {
            Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "textures/entity/snow_walker/snow_walker_1.png"),
            Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "textures/entity/snow_walker/snow_walker_2.png")
    };

    public SnowWalkerRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ZombieModel<SnowWalkerRenderState>(context.bakeLayer(ModelLayers.ZOMBIE)),
                new BabyZombieModel<>(context.bakeLayer(ModelLayers.ZOMBIE_BABY)),
                ArmorModelSet.bake(ModelLayers.ZOMBIE_ARMOR, context.getModelSet(), ZombieModel::new),
                ArmorModelSet.bake(ModelLayers.ZOMBIE_BABY_ARMOR, context.getModelSet(), BabyZombieModel::new)
        );
    }

    @Override
    public SnowWalkerRenderState createRenderState() {
        return new SnowWalkerRenderState();
    }

    @Override
    public Identifier getTextureLocation(SnowWalkerRenderState state) {
        return TEXTURES[state.textureVariant];
    }

    @Override
    public void extractRenderState(SnowWalker entity, SnowWalkerRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.textureVariant = entity.getTextureVariant();
    }
}

package fr.akkun.newmeriacore.client.entity;

import fr.akkun.newmeriacore.NewmeriaCore;
import fr.akkun.newmeriacore.entity.HellZombie;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.zombie.BabyZombieModel;
import net.minecraft.client.model.monster.zombie.ZombieModel;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.Identifier;

/** Renders the Hell Zombie with the vanilla zombie model, just with its own texture. */
public class HellZombieRenderer extends AbstractZombieRenderer<HellZombie, ZombieRenderState, ZombieModel<ZombieRenderState>> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(NewmeriaCore.MOD_ID, "textures/entity/hell_zombie/hell_zombie.png");

    public HellZombieRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new ZombieModel<ZombieRenderState>(context.bakeLayer(ModelLayers.ZOMBIE)),
                new BabyZombieModel<>(context.bakeLayer(ModelLayers.ZOMBIE_BABY)),
                ArmorModelSet.bake(ModelLayers.ZOMBIE_ARMOR, context.getModelSet(), ZombieModel::new),
                ArmorModelSet.bake(ModelLayers.ZOMBIE_BABY_ARMOR, context.getModelSet(), BabyZombieModel::new)
        );
    }

    @Override
    public ZombieRenderState createRenderState() {
        return new ZombieRenderState();
    }

    @Override
    public Identifier getTextureLocation(ZombieRenderState state) {
        return TEXTURE;
    }
}

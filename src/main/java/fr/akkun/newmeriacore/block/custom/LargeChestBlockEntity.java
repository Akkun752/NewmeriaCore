package fr.akkun.newmeriacore.block.custom;

import fr.akkun.newmeriacore.menu.LargeChestMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class LargeChestBlockEntity extends ChestBlockEntity {
    private final ChestTier tier;

    // The vanilla chest's own counter only recognises players looking at a vanilla ChestMenu, and
    // would shut the lid on anyone using our wider menu. This one replaces it (see the overrides below).
    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level level, BlockPos pos, BlockState blockState) {
            playSound(level, pos, SoundEvents.CHEST_OPEN);
        }

        @Override
        protected void onClose(Level level, BlockPos pos, BlockState blockState) {
            playSound(level, pos, SoundEvents.CHEST_CLOSE);
        }

        @Override
        protected void openerCountChanged(Level level, BlockPos pos, BlockState blockState, int previous, int current) {
            LargeChestBlockEntity.this.signalOpenCount(level, pos, blockState, previous, current);
        }

        @Override
        public boolean isOwnContainer(Player player) {
            return player.containerMenu instanceof LargeChestMenu menu && menu.getContainer() == LargeChestBlockEntity.this;
        }
    };

    public LargeChestBlockEntity(ChestTier tier, BlockPos worldPosition, BlockState blockState) {
        super(tier.blockEntityType(), worldPosition, blockState);
        this.tier = tier;
        // The vanilla chest starts with a 27-slot list.
        this.setItems(NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY));
    }

    @Override
    public int getContainerSize() {
        return this.tier.slotCount();
    }

    @Override
    protected Component getDefaultName() {
        return this.tier.defaultName();
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new LargeChestMenu(this.tier, containerId, inventory, this);
    }

    private static void playSound(Level level, BlockPos pos, SoundEvent event) {
        level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, event, SoundSource.BLOCKS,
                0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public void startOpen(ContainerUser containerUser) {
        if (!this.isRemoved() && !containerUser.getLivingEntity().isSpectator()) {
            this.openersCounter.incrementOpeners(containerUser.getLivingEntity(), this.getLevel(), this.getBlockPos(),
                    this.getBlockState(), containerUser.getContainerInteractionRange());
        }
    }

    @Override
    public void stopOpen(ContainerUser containerUser) {
        if (!this.isRemoved() && !containerUser.getLivingEntity().isSpectator()) {
            this.openersCounter.decrementOpeners(containerUser.getLivingEntity(), this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    @Override
    public List<ContainerUser> getEntitiesWithContainerOpen() {
        return this.openersCounter.getEntitiesWithContainerOpen(this.getLevel(), this.getBlockPos());
    }

    @Override
    public void recheckOpen() {
        if (!this.isRemoved()) {
            this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }
}

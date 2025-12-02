package com.github.eeoun.minecraft.mods.elytra;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElytraAutoGiverMod implements ModInitializer {

    public static final String MOD_ID = "auto-give-elytra";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


    @Override
    public void onInitialize() {
        // 注册玩家跨世界（维度）后的回调
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(this::onPlayerChangedWorld);
    }

    private void onPlayerChangedWorld(ServerPlayer player, ServerLevel origin, ServerLevel destination) {
        LOGGER.info("from {}->{}", origin.dimensionTypeRegistration().getRegisteredName(), destination.dimensionTypeRegistration().getRegisteredName());

        if (!"minecraft:the_end".equals(origin.dimensionTypeRegistration().getRegisteredName())
                || !"minecraft:overworld".equals(destination.dimensionTypeRegistration().getRegisteredName())
        ) {
            return;
        }

        ItemStack elytraItemStack = new ItemStack(Items.ELYTRA);
        if (player == null) return;
        Inventory inventory = player.getInventory();
        if (inventory.contains(elytraItemStack)) {
            return;
        }

        int slot = inventory.getFreeSlot();
        if (slot < 0) {
            LOGGER.info("User Do not have ELYTRA! Try give one but no free space.");
        }

        boolean result = inventory.add(slot, elytraItemStack);
        LOGGER.info("User Do not have ELYTRA! put it in {}:{}", slot, result);

    }
}

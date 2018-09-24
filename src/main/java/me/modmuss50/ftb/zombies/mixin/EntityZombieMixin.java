package me.modmuss50.ftb.zombies.mixin;

import me.modmuss50.fusion.api.Inject;
import me.modmuss50.fusion.api.Mixin;
import me.modmuss50.fusion.api.SRGName;

@Mixin("net.minecraft.entity.monster.EntityZombie")
public abstract class EntityZombieMixin {

    @SRGName("func_190730_o")
    @Inject
    protected boolean shouldBurnInDay() {
        return false;
    }
}

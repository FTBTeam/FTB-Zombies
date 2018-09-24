package me.modmuss50.ftb.zombies.mixin;

import me.modmuss50.fusion.api.IMixinProvider;

public class ZombieMixinProvider implements IMixinProvider {
    @Override
    public Class[] getMixins() {
        return new Class[]{
                EntityZombieMixin.class
        };
    }
}

package fr.akkun.newmeriacore.rpg;

import net.minecraft.network.chat.Component;

public enum RpgStat {
    FORCE("rpg.newmeriacore.stat.force") {
        @Override
        public int level(RpgData data) {
            return data.forceLevel();
        }

        @Override
        public RpgData withLevel(RpgData data, int level) {
            return data.withForceLevel(level);
        }
    },
    RESISTANCE("rpg.newmeriacore.stat.resistance") {
        @Override
        public int level(RpgData data) {
            return data.resistanceLevel();
        }

        @Override
        public RpgData withLevel(RpgData data, int level) {
            return data.withResistanceLevel(level);
        }
    },
    SPEED("rpg.newmeriacore.stat.speed") {
        @Override
        public int level(RpgData data) {
            return data.speedLevel();
        }

        @Override
        public RpgData withLevel(RpgData data, int level) {
            return data.withSpeedLevel(level);
        }
    },
    MAGIC("rpg.newmeriacore.stat.magic") {
        @Override
        public int level(RpgData data) {
            return data.magicLevel();
        }

        @Override
        public RpgData withLevel(RpgData data, int level) {
            return data.withMagicLevel(level);
        }
    };

    private final String translationKey;

    RpgStat(String translationKey) {
        this.translationKey = translationKey;
    }

    public Component displayName() {
        return Component.translatable(translationKey);
    }

    public abstract int level(RpgData data);

    public abstract RpgData withLevel(RpgData data, int level);
}

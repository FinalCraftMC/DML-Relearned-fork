package mustapelto.deepmoblearning.common.mobdata;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mustapelto.deepmoblearning.DMLConfig;
import mustapelto.deepmoblearning.DMLRelearned;
import mustapelto.deepmoblearning.common.enums.EnumLivingMatterType;
import mustapelto.deepmoblearning.common.util.FileHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class MobMetaDataManager {
    private static NonNullList<MobMetaData> mobMetaDataStore;

    public static void init() {
        File jsonFile = new File(FileHelper.configDML, "MobData.json");

        if (!jsonFile.exists()) {
            generateDefaultDataFile(jsonFile);
        }

        mobMetaDataStore = NonNullList.create();

        readMobDataFromFile(jsonFile);
    }

    private static void generateDefaultDataFile(File jsonFile) {
        JsonObject mobDataObject = new JsonObject();
        mobDataObject.add("vanillaHostile", generateVanillaHostileData());
        mobDataObject.add("thermalExpansion", generateThermalExpansionData());
        mobDataObject.add("twilightForest", generateTwilightForestData());
        mobDataObject.add("tinkersConstruct", generateTinkersConstructData());
        mobDataObject.add("matterOverdrive", generateMatterOverdriveData());

        try {
            FileHelper.writeObject(mobDataObject, jsonFile);
        } catch (IOException e) {
            DMLRelearned.logger.error("Could not write default mob config file!");
        }
    }

    private static void readMobDataFromFile(File file) {
        JsonObject mobDataObject;
        try {
            mobDataObject = FileHelper.readObject(file);
        } catch (IOException e) {
            DMLRelearned.logger.error("Could not read mob config file! This will cause the mod to malfunction.");
            return;
        }

        Set<Map.Entry<String, JsonElement>> entrySet = mobDataObject.entrySet();
        entrySet.forEach(entry -> {
            if (!(entry.getValue() instanceof JsonArray))
                return;
            JsonArray contents = (JsonArray) entry.getValue();
            contents.forEach(block -> mobMetaDataStore.add(new MobMetaData(block.getAsJsonObject())));
        });
    }

    public static NonNullList<MobMetaData> getMetaDataList() {
        return mobMetaDataStore;
    }

    public static JsonArray generateVanillaHostileData() {
        JsonArray mobDataArray = new JsonArray();

        mobDataArray.add(MobMetaData.createJsonObject("blaze",
                "minecraft",
                "Blaze", "",
                10,
                EnumLivingMatterType.HELLISH.getName(),
                new String[]{"Bring buckets of water, and watch in despair", "as it evaporates, and everything is on fire.", "You are on fire."},
                256,
                "",
                "minecraft:blaze", "",
                48, 10, 20,
                "", false, 0, 0,
                new String[]{"minecraft:blaze"},
                new String[]{""},
                new String[0]
        ));

        mobDataArray.add(MobMetaData.createJsonObject("creeper",
                "minecraft",
                "Creeper", "",
                10,
                EnumLivingMatterType.OVERWORLDIAN.getName(),
                new String[]{"Will blow up your base if", "left unattended."},
                80,
                "",
                "minecraft:creeper", "",
                42, 5, 5,
                "", false, 0, 0,
                new String[]{"minecraft:creeper"},
                new String[]{""},
                new String[0]
        ));

        mobDataArray.add(MobMetaData.createJsonObject("dragon",
                "minecraft",
                "Ender Dragon", "",
                100,
                EnumLivingMatterType.EXTRATERRESTRIAL.getName(),
                new String[]{"Resides in the End, does not harbor treasure.", "Destroy its crystals and break the cycle!"},
                2560,
                "",
                "minecraft:ender_dragon", "",
                7, 0, -20,
                "", false, 0, 0,
                new String[]{"minecraft:ender_dragon"},
                new String[]{""},
                new String[0]
        ));

        mobDataArray.add(MobMetaData.createJsonObject("enderman",
                "minecraft",
                "Enderman", "Endermen",
                20,
                EnumLivingMatterType.EXTRATERRESTRIAL.getName(),
                new String[]{"Friendly unless provoked, dislikes rain.", "Teleports short distances."},
                512,
                "",
                "minecraft:enderman", "",
                30, 5, 11,
                "", false, 0, 0,
                new String[]{"minecraft:enderman", "minecraft:endermite"},
                new String[]{""},
                new String[0]
        ));

        mobDataArray.add(MobMetaData.createJsonObject("ghast",
                "minecraft",
                "Ghast", "",
                10,
                EnumLivingMatterType.HELLISH.getName(),
                new String[]{"If you hear something that sounds like", "a crying llama, you're probably hearing a ghast."},
                372,
                "",
                "minecraft:ghast", "",
                10, 0, -20,
                "", false, 0, 0,
                new String[]{"minecraft:ghast"},
                new String[]{""},
                new String[0]
        ));

        mobDataArray.add(MobMetaData.createJsonObject("guardian",
                "minecraft",
                "Guardian", "",
                15,
                EnumLivingMatterType.OVERWORLDIAN.getName(),
                new String[]{"Lurking in the oceans.", "Uses some sort of sonar beam as", "a means of attack."},
                340,
                "",
                "minecraft:guardian", "",
                36, 5, -5,
                "", false, 0, 0,
                new String[]{"minecraft:guardian", "minecraft:elder_guardian"},
                new String[]{""},
                new String[0]
        ));

        mobDataArray.add(MobMetaData.createJsonObject("shulker",
                "minecraft",
                "Shulker", "",
                15,
                EnumLivingMatterType.EXTRATERRESTRIAL.getName(),
                new String[]{"Found in End cities.", "Sneaky little buggers."},
                256,
                "",
                "minecraft:shulker", "",
                36, 5, -5,
                "", false, 0, 0,
                new String[]{"minecraft:shulker"},
                new String[]{""},
                new String[0]
        ));

        mobDataArray.add(MobMetaData.createJsonObject("skeleton",
                "minecraft",
                "Skeleton", "",
                10,
                EnumLivingMatterType.OVERWORLDIAN.getName(),
                new String[]{"A formidable archer, which seems to be running", "some sort of cheat engine.", "A shield could prove useful."},
                80,
                "",
                "minecraft:skeleton", "minecraft:bow",
                38, 6, 10,
                "", false, 0, 0,
                new String[]{"minecraft:skeleton", "minecraft:stray", "twilightforest:skeleton_druid"},
                new String[]{""},
                new String[0]
        ));

        mobDataArray.add(MobMetaData.createJsonObject("slime",
                "minecraft",
                "Slime", "",
                8,
                EnumLivingMatterType.OVERWORLDIAN.getName(),
                new String[]{"The bouncing bouncer", "bounces, bounces and bounces", "Bounces and bou- squish! -\"A new slime haiku\""},
                150,
                "",
                "minecraft:slime", "",
                60, 10, -16,
                "", false, 0, 0,
                new String[]{"minecraft:slime", "minecraft:magma_cube"},
                new String[]{""},
                new String[0]
        ));

        mobDataArray.add(MobMetaData.createJsonObject("spider",
                "minecraft",
                "Spider", "",
                8,
                EnumLivingMatterType.OVERWORLDIAN.getName(),
                new String[]{"Nocturnal douchebags, beware!", "Drops strands of string for some reason."},
                80,
                "",
                "minecraft:creeper", "",
                30, 5, 0,
                "minecraft:cave_spider", false, 5, -25,
                new String[]{"minecraft:spider", "minecraft:cave_spider", "twilightforest:hedge_spider", "twilightforest:king_spider"},
                new String[]{""},
                new String[0]
        ));

        mobDataArray.add(MobMetaData.createJsonObject("witch",
                "minecraft",
                "Witch", "Witches",
                13,
                EnumLivingMatterType.OVERWORLDIAN.getName(),
                new String[]{"Affinity with potions and concoctions.", "Likes cats.", "Beware!"},
                120,
                "",
                "minecraft:witch", "minecraft:potion",
                34, 4, 11,
                "", false, 0, 0,
                new String[]{"minecraft:witch"},
                new String[]{""},
                new String[0]
        ));

        mobDataArray.add(MobMetaData.createJsonObject("wither",
                "minecraft",
                "Wither", "",
                150,
                EnumLivingMatterType.EXTRATERRESTRIAL.getName(),
                new String[]{"Do not approach this enemy. Run!", "I mean it has 3 heads, what could", "possibly go wrong?"},
                2048,
                "",
                "minecraft:wither", "",
                22, 3, 18,
                "", false, 0, 0,
                new String[]{"minecraft:wither"},
                new String[]{""},
                new String[0]
        ));

        mobDataArray.add(MobMetaData.createJsonObject("wither_skeleton",
                "minecraft",
                "Wither Skeleton", "",
                10,
                EnumLivingMatterType.HELLISH.getName(),
                new String[]{"Inflicts the Wither effect.", "Bring milk!"},
                880,
                "",
                "minecraft:wither_skeleton", "minecraft:stone_sword",
                33, 5, 10,
                "", false, 0, 0,
                new String[]{"minecraft:wither_skeleton"},
                new String[]{""},
                new String[0]
        ));

        mobDataArray.add(MobMetaData.createJsonObject("zombie",
                "minecraft",
                "Zombie", "",
                10,
                EnumLivingMatterType.OVERWORLDIAN.getName(),
                new String[]{"They go moan in the night.", "Does not understand the need for", "personal space."},
                80,
                "",
                "minecraft:zombie", "",
                35, -2, 6,
                "minecraft:zombie", true, 21, 6,
                new String[]{"minecraft:zombie", "minecraft:husk", "minecraft:zombie_villager", "minecraft:zombie_pigman"},
                new String[]{""},
                new String[0]
        ));

        return mobDataArray;
    }

    private static JsonArray generateThermalExpansionData() {
        JsonArray mobDataArray = new JsonArray();

        mobDataArray.add(MobMetaData.createJsonObject("thermal_elemental",
                "thermalfoundation",
                "Thermal Elemental", "",
                10,
                EnumLivingMatterType.OVERWORLDIAN.getName(),
                new String[]{"Blizzes, Blitzes and Basalzes.", "Siblings of the Blaze.", "Their master really liked words starting with B."},
                256,
                "",
                "thermalfoundation:blizz", "",
                48, 10, 20,
                "", false, 0, 0,
                new String[]{"thermalfoundation:blizz", "thermalfoundation:blitz", "thermalfoundation:basalz"},
                new String[]{""},
                new String[0]
        ));

        return mobDataArray;
    }

    private static JsonArray generateTwilightForestData() {
        JsonArray mobDataArray = new JsonArray();

        mobDataArray.add(MobMetaData.createJsonObject("twilight_forest",
                "twilightforest",
                "Forest creature", "",
                0,
                EnumLivingMatterType.TWILIGHT.getName(),
                new String[]{"Nagas, Liches and flying books.", "What the hell have you walked into?"},
                256,
                "Gain data by defeating non-vanilla mobs in the Naga Courtyard and Lich Tower.",
                "twilightforest:lich", "",
                35, 6, 12,
                "", false, 0, 0,
                new String[]{
                        "twilightforest:naga",
                        "twilightforest:lich_minion",
                        "twilightforest:lich",
                        "twilightforest:death_tome",
                        "twilightforest:swarm_spider"
                },
                new String[]{""},
                new String[0]
        ));

        mobDataArray.add(MobMetaData.createJsonObject("twilight_swamp",
                "twilightforest",
                "Swamp creature", "",
                0,
                EnumLivingMatterType.TWILIGHT.getName(),
                new String[]{"This realm sure could use some building regulations.", "How are you even allowed to build a huge maze", "in your basement!?"},
                256,
                "Gain data by defeating non-vanilla mobs in the Swamp Labyrinth and Hydra Lair.",
                "minecraft:minotaur", "twilightforest:minotaur_axe",
                33, 6, 14,
                "", false, 0, 0,
                new String[]{
                        "twilightforest:minotaur",
                        "twilightforest:minoshroom",
                        "twilightforest:maze_slime",
                        "twilightforest:fire_beetle",
                        "twilightforest:pinch_beetle",
                        "twilightforest:slime_beetle",
                        "twilightforest:hydra"
                },
                new String[]{""},
                new String[0]
        ));

        mobDataArray.add(MobMetaData.createJsonObject("twilight_glacier",
                "twilightforest",
                "Glacier inhabitant", "",
                0,
                EnumLivingMatterType.TWILIGHT.getName(),
                new String[]{"Here you'll find caves with ancient beasts", "and Elsa's wicked distant cousin Aurora.", "(Elsa might \"let it go\", but Aurora sure won't!)"},
                256,
                "Gain data by defeating non-vanilla mobs in the Yeti Lair and Ice Tower.",
                "minecraft:snow_queen", "",
                33, 5, 13,
                "", false, 0, 0,
                new String[]{
                        "twilightforest:yeti_alpha",
                        "twilightforest:yeti",
                        "twilightforest:winter_wolf",
                        "twilightforest:penguin",
                        "twilightforest:snow_guardian",
                        "twilightforest:stable_ice_core",
                        "twilightforest:unstable_ice_core",
                        "twilightforest:snow_queen"
                },
                new String[]{""},
                new String[0]
        ));

        mobDataArray.add(MobMetaData.createJsonObject("twilight_darkwood",
                "twilightforest",
                "Darkwood creature", "",
                0,
                EnumLivingMatterType.TWILIGHT.getName(),
                new String[]{"Spooky scary strongholds send shivers down", "your spine, the Ur-Ghast will shock your", "soul and seal your doom tonight!"},
                256,
                "Gain data by defeating non-vanilla mobs in the Goblin Knight Stronghold and Dark Tower.",
                "twilightforest:ur_ghast", "",
                3, -3, -3,
                "", false, 0, 0,
                new String[]{
                        "twilightforest:redcap",
                        "twilightforest:blockchain_goblin",
                        "twilightforest:kobold",
                        "twilightforest:goblin_knight_lower",
                        "twilightforest:goblin_knight_upper",
                        "twilightforest:helmet_crab",
                        "twilightforest:knight_phantom",
                        "twilightforest:tower_ghast",
                        "twilightforest:tower_broodling",
                        "twilightforest:tower_golem",
                        "twilightforest:tower_termite",
                        "twilightforest:mini_ghast",
                        "twilightforest:ur_ghast"
                },
                new String[]{""},
                new String[0]
        ));

        return mobDataArray;
    }

    private static JsonArray generateTinkersConstructData() {
        JsonArray mobDataArray = new JsonArray();

        mobDataArray.add(MobMetaData.createJsonObject("tinker_slime",
                "tconstruct",
                "Blue Slime", "",
                10,
                EnumLivingMatterType.OVERWORLDIAN.getName(),
                new String[]{"The elusive blue slime. Seemingly a", "part of some sort of power hierarchy,", "since there's a bunch of \"King slimes\" around."},
                256,
                "",
                "tconstruct:blue_slime", "",
                60, 10, -16,
                "", false, 0, 0,
                new String[]{"tconstruct:blue_slime"},
                new String[]{""},
                new String[0]
        ));

        return mobDataArray;
    }

    private static JsonArray generateMatterOverdriveData() {
        JsonArray mobDataArray = new JsonArray();

        mobDataArray.add(MobMetaData.createJsonObject("mo_android",
                "matteroverdrive",
                "Rogue Android", "",
                10,
                EnumLivingMatterType.OVERWORLDIAN.getName(),
                new String[]{"It's not simply an android.", "It's a life form, entirely unique.", "Meep morp."},
                256,
                "",
                "matteroverdrive:rogue_android", "",
                33, 4, 8,
                "", false, 0, 0,
                new String[]{"matteroverdrive:ranged_rogue_android", "matteroverdrive:rogue_android"},
                new String[]{""},
                new String[0]
        ));

        return mobDataArray;
    }
}

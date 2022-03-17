package team.cqr.cqrepoured.init;

import static team.cqr.cqrepoured.util.InjectionUtil.Null;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;
import team.cqr.cqrepoured.CQRMain;
import team.cqr.cqrepoured.entity.bases.AbstractEntityCQR;
import team.cqr.cqrepoured.entity.mobs.EntityCQRBoarman;
import team.cqr.cqrepoured.entity.mobs.EntityCQRDummy;
import team.cqr.cqrepoured.entity.mobs.EntityCQRDwarf;
import team.cqr.cqrepoured.entity.mobs.EntityCQREnderman;
import team.cqr.cqrepoured.entity.mobs.EntityCQRGolem;
import team.cqr.cqrepoured.entity.mobs.EntityCQRGremlin;
import team.cqr.cqrepoured.entity.mobs.EntityCQRIllager;
import team.cqr.cqrepoured.entity.mobs.EntityCQRMandril;
import team.cqr.cqrepoured.entity.mobs.EntityCQRMinotaur;
import team.cqr.cqrepoured.entity.mobs.EntityCQRMummy;
import team.cqr.cqrepoured.entity.mobs.EntityCQRNPC;
import team.cqr.cqrepoured.entity.mobs.EntityCQROgre;
import team.cqr.cqrepoured.entity.mobs.EntityCQROrc;
import team.cqr.cqrepoured.entity.mobs.EntityCQRPirate;
import team.cqr.cqrepoured.entity.mobs.EntityCQRSkeleton;
import team.cqr.cqrepoured.entity.mobs.EntityCQRSpectre;
import team.cqr.cqrepoured.entity.mobs.EntityCQRTriton;
import team.cqr.cqrepoured.entity.mobs.EntityCQRWalker;
import team.cqr.cqrepoured.entity.mobs.EntityCQRZombie;
import team.cqr.cqrepoured.item.ItemAlchemyBag;
import team.cqr.cqrepoured.item.ItemBadge;
import team.cqr.cqrepoured.item.ItemBullBattleAxe;
import team.cqr.cqrepoured.item.ItemCursedBone;
import team.cqr.cqrepoured.item.ItemDungeonPlacer;
import team.cqr.cqrepoured.item.ItemGoldenFeather;
import team.cqr.cqrepoured.item.ItemHookshot;
import team.cqr.cqrepoured.item.ItemLongshot;
import team.cqr.cqrepoured.item.ItemLore;
import team.cqr.cqrepoured.item.ItemMagicBell;
import team.cqr.cqrepoured.item.ItemMobToSpawner;
import team.cqr.cqrepoured.item.ItemPathTool;
import team.cqr.cqrepoured.item.ItemPotionHealing;
import team.cqr.cqrepoured.item.ItemShieldDummy;
import team.cqr.cqrepoured.item.ItemSoulBottle;
import team.cqr.cqrepoured.item.ItemSpawnEggCQR;
import team.cqr.cqrepoured.item.ItemSpawnerConverter;
import team.cqr.cqrepoured.item.ItemSpiderHook;
import team.cqr.cqrepoured.item.ItemSpikedGlove;
import team.cqr.cqrepoured.item.ItemStructureSelector;
import team.cqr.cqrepoured.item.ItemSuperTool;
import team.cqr.cqrepoured.item.ItemTeleportStone;
import team.cqr.cqrepoured.item.ItemUnprotectedPositionTool;
import team.cqr.cqrepoured.item.armor.ItemArmorBull;
import team.cqr.cqrepoured.item.armor.ItemArmorDyable;
import team.cqr.cqrepoured.item.armor.ItemArmorHeavy;
import team.cqr.cqrepoured.item.armor.ItemArmorInquisition;
import team.cqr.cqrepoured.item.armor.ItemArmorSlime;
import team.cqr.cqrepoured.item.armor.ItemArmorSpider;
import team.cqr.cqrepoured.item.armor.ItemArmorTurtle;
import team.cqr.cqrepoured.item.armor.ItemBackpack;
import team.cqr.cqrepoured.item.armor.ItemBootsCloud;
import team.cqr.cqrepoured.item.armor.ItemCrown;
import team.cqr.cqrepoured.item.armor.ItemHelmetDragon;
import team.cqr.cqrepoured.item.gun.ItemBubblePistol;
import team.cqr.cqrepoured.item.gun.ItemBubbleRifle;
import team.cqr.cqrepoured.item.gun.ItemBullet;
import team.cqr.cqrepoured.item.gun.ItemCannonBall;
import team.cqr.cqrepoured.item.gun.ItemFlamethrower;
import team.cqr.cqrepoured.item.gun.ItemMusket;
import team.cqr.cqrepoured.item.gun.ItemMusketKnife;
import team.cqr.cqrepoured.item.gun.ItemRevolver;
import team.cqr.cqrepoured.item.shield.ItemShieldCQR;
import team.cqr.cqrepoured.item.shield.ItemShieldWalkerKing;
import team.cqr.cqrepoured.item.spear.ItemSpearBase;
import team.cqr.cqrepoured.item.staff.ItemStaff;
import team.cqr.cqrepoured.item.staff.ItemStaffFire;
import team.cqr.cqrepoured.item.staff.ItemStaffGun;
import team.cqr.cqrepoured.item.staff.ItemStaffHealing;
import team.cqr.cqrepoured.item.staff.ItemStaffPoison;
import team.cqr.cqrepoured.item.staff.ItemStaffSpider;
import team.cqr.cqrepoured.item.staff.ItemStaffThunder;
import team.cqr.cqrepoured.item.staff.ItemStaffVampiric;
import team.cqr.cqrepoured.item.staff.ItemStaffWind;
import team.cqr.cqrepoured.item.sword.ItemDagger;
import team.cqr.cqrepoured.item.sword.ItemDaggerNinja;
import team.cqr.cqrepoured.item.sword.ItemFakeSwordHealingStaff;
import team.cqr.cqrepoured.item.sword.ItemGreatSword;
import team.cqr.cqrepoured.item.sword.ItemSwordMoonlight;
import team.cqr.cqrepoured.item.sword.ItemSwordSpider;
import team.cqr.cqrepoured.item.sword.ItemSwordSunshine;
import team.cqr.cqrepoured.item.sword.ItemSwordTurtle;
import team.cqr.cqrepoured.item.sword.ItemSwordWalker;

public class CQRItems {

	// Daggers
	public static final Item DAGGER_IRON = Null();
	public static final Item DAGGER_DIAMOND = Null();
	public static final Item DAGGER_NINJA = Null();
	public static final Item DAGGER_MONKING = Null();

	// Swords
	public static final Item SWORD_TURTLE = Null();
	public static final Item SWORD_SPIDER = Null();
	public static final Item SWORD_MOONLIGHT = Null();
	public static final Item SWORD_SUNSHINE = Null();

	// Battle Axes
	public static final Item BATTLE_AXE_BULL = Null();

	// Walker items
	public static final Item SWORD_WALKER = Null();
	public static final Item SHIELD_WALKER_KING = Null();

	// Shields
	public static final Item SHIELD_BULL = Null();
	public static final Item SHIELD_CARL = Null();
	public static final Item SHIELD_DRAGONSLAYER = Null();
	public static final Item SHIELD_FIRE = Null();
	public static final Item SHIELD_GOBLIN = Null();
	public static final Item SHIELD_MONKING = Null();
	public static final Item SHIELD_MOON = Null();
	public static final Item SHIELD_MUMMY = Null();
	public static final Item SHIELD_PIGMAN = Null();
	public static final Item SHIELD_PIRATE = Null();
	public static final Item SHIELD_PIRATE2 = Null();
	public static final Item SHIELD_RAINBOW = Null();
	public static final Item SHIELD_REFLECTIVE = Null();
	public static final Item SHIELD_RUSTED = Null();
	public static final Item SHIELD_SKELETON_FRIENDS = Null();
	public static final Item SHIELD_SPECTER = Null();
	public static final Item SHIELD_SPIDER = Null();
	public static final Item SHIELD_SUN = Null();
	public static final Item SHIELD_TOMB = Null();
	public static final Item SHIELD_TRITON = Null();
	public static final Item SHIELD_TURTLE = Null();
	public static final Item SHIELD_WARPED = Null();
	public static final Item SHIELD_WALKER = Null();
	public static final Item SHIELD_ZOMBIE = Null();

	// Great Swords
	public static final Item GREAT_SWORD_IRON = Null();
	public static final Item GREAT_SWORD_DIAMOND = Null();
	public static final Item GREAT_SWORD_BULL = Null();
	public static final Item GREAT_SWORD_MONKING = Null();

	// Spears
	public static final Item SPEAR_DIAMOND = Null();
	public static final Item SPEAR_IRON = Null();

	// Staves
	public static final Item STAFF = Null();
	public static final Item STAFF_FIRE = Null();
	public static final Item STAFF_VAMPIRIC = Null(); // #TODO DESCRIPTION
	public static final Item STAFF_WIND = Null(); // #TODO DESCRIPTION
	public static final Item STAFF_POISON = Null(); // #TODO DESCRIPTION
	public static final ItemStaffHealing STAFF_HEALING = Null();
	public static final ItemFakeSwordHealingStaff DIAMOND_SWORD_FAKE_HEALING_STAFF = Null();
	public static final Item STAFF_THUNDER = Null();
	public static final Item STAFF_SPIDER = Null();
	public static final Item STAFF_GUN = Null(); // #TODO TEXTURES

	// Guns
	public static final Item REVOLVER = Null();
	public static final Item CAPTAIN_REVOLVER = Null();
	public static final Item MUSKET = Null();
	public static final Item MUSKET_DAGGER_IRON = Null(); // #TODO TEXTURES
	public static final Item MUSKET_DAGGER_DIAMOND = Null(); // #TODO TEXTURES
	public static final Item MUSKET_DAGGER_MONKING = Null(); // #TODO TEXTURES
	public static final Item BULLET_IRON = Null();
	public static final Item BULLET_GOLD = Null();
	public static final Item BULLET_DIAMOND = Null();
	public static final Item BULLET_FIRE = Null();
	public static final Item CANNON_BALL = Null();
	public static final Item FLAMETHROWER = Null(); // #TODO TEXTURES
	public static final Item BUBBLE_PISTOL = Null();
	public static final Item BUBBLE_RIFLE = Null();

	// Hookers
	public static final ItemHookshot HOOKSHOT = Null();
	public static final ItemLongshot LONGSHOT = Null();
	public static final ItemSpiderHook SPIDERHOOK = Null();

	// Single Armor Items
	public static final Item HELMET_DRAGON = Null(); // #TODO Make model centered on head // Abandon for now
	public static final Item BOOTS_CLOUD = Null();
	public static final Item BACKPACK = Null();
	public static final Item SPIKED_GLOVE = Null();
	public static final Item KING_CROWN = Null();

	// Slime Armor Items
	public static final Item HELMET_SLIME = Null();
	public static final Item CHESTPLATE_SLIME = Null();
	public static final Item LEGGINGS_SLIME = Null();
	public static final Item BOOTS_SLIME = Null();

	// Turtle Armor Items
	public static final Item HELMET_TURTLE = Null();
	public static final Item CHESTPLATE_TURTLE = Null();
	public static final Item LEGGINGS_TURTLE = Null();
	public static final Item BOOTS_TURTLE = Null();

	// Bull Armor Items
	public static final Item HELMET_BULL = Null();
	public static final Item CHESTPLATE_BULL = Null();
	public static final Item LEGGINGS_BULL = Null();
	public static final Item BOOTS_BULL = Null();

	// Spider Armor Items
	public static final Item HELMET_SPIDER = Null();
	public static final Item CHESTPLATE_SPIDER = Null();
	public static final Item LEGGINGS_SPIDER = Null();
	public static final Item BOOTS_SPIDER = Null();

	// Inquisition Armor Items
	public static final Item HELMET_INQUISITION = Null();
	public static final Item CHESTPLATE_INQUISITION = Null();
	public static final Item LEGGINGS_INQUISITION = Null();
	public static final Item BOOTS_INQUISITION = Null();

	// Heavy Diamond Armor Items
	public static final Item HELMET_HEAVY_DIAMOND = Null();
	public static final Item CHESTPLATE_HEAVY_DIAMOND = Null();
	public static final Item LEGGINGS_HEAVY_DIAMOND = Null();
	public static final Item BOOTS_HEAVY_DIAMOND = Null();

	// Heavy Iron Armor Items
	public static final Item HELMET_HEAVY_IRON = Null();
	public static final Item CHESTPLATE_HEAVY_IRON = Null();
	public static final Item LEGGINGS_HEAVY_IRON = Null();
	public static final Item BOOTS_HEAVY_IRON = Null();

	// Dyable Iron Armor
	public static final Item HELMET_IRON_DYABLE = Null();
	public static final Item CHESTPLATE_IRON_DYABLE = Null();
	public static final Item LEGGINGS_IRON_DYABLE = Null();
	public static final Item BOOTS_IRON_DYABLE = Null();

	// Dyable Diamond Armor
	public static final Item HELMET_DIAMOND_DYABLE = Null();
	public static final Item CHESTPLATE_DIAMOND_DYABLE = Null();
	public static final Item LEGGINGS_DIAMOND_DYABLE = Null();
	public static final Item BOOTS_DIAMOND_DYABLE = Null();

	// Ingridients
	public static final Item SCALE_TURTLE = Null();
	public static final Item LEATHER_BULL = Null();
	public static final Item HORN_BULL = Null();
	public static final Item BALL_SLIME = Null();
	public static final Item LEATHER_SPIDER = Null();
	public static final Item BONE_MONKING = Null();
	public static final Item GIANT_SPIDER_POISON = Null();
	public static final Item FEATHER_GOLDEN = Null();

	// Other
	public static final Item POTION_HEALING = Null();
	public static final Item TELEPORT_STONE = Null();
	public static final Item CURSED_BONE = Null();

	// Creative
	public static final Item SUPER_TOOL = Null();
	public static final Item STRUCTURE_SELECTOR = Null();
	public static final Item SOUL_BOTTLE = Null();
	public static final Item MOB_TO_SPAWNER_TOOL = Null();
	public static final Item SPAWNER_CONVERTER = Null();
	public static final Item BADGE = Null();
	public static final Item PATH_TOOL = Null();
	public static final Item DUMMY_SHIELD = Null();
	public static final Item ALCHEMY_BAG = Null();
	public static final Item UNPROTECTED_POSITIONS_TOOL = Null();

	@EventBusSubscriber(modid = CQRMain.MODID)
	/*
	public static class EventHandler {

		public static final List<Item> ITEMS = new ArrayList<>();
		public static final List<Item> SPAWN_EGGS = new ArrayList<>();

		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			final Item[] items = {
					setItemName(new ItemDagger(ToolMaterial.IRON, 25), "dagger_iron"),
					setItemName(new ItemDagger(ToolMaterial.DIAMOND, 20), "dagger_diamond"),
					setItemName(new ItemDaggerNinja(CQRMaterials.ToolMaterials.TOOL_NINJA, 15), "dagger_ninja"),
					setItemName(new ItemDagger(CQRMaterials.ToolMaterials.TOOL_MONKING, 10), "dagger_monking"),

					setItemName(new ItemSwordTurtle(CQRMaterials.ToolMaterials.TOOL_TURTLE), "sword_turtle"),
					setItemName(new ItemSwordSpider(CQRMaterials.ToolMaterials.TOOL_SPIDER), "sword_spider"),
					setItemName(new ItemSwordMoonlight(CQRMaterials.ToolMaterials.TOOL_MOONLIGHT), "sword_moonlight"),
					setItemName(new ItemSwordSunshine(CQRMaterials.ToolMaterials.TOOL_SUNSHINE), "sword_sunshine"),

					setItemName(new ItemBullBattleAxe(CQRMaterials.ToolMaterials.TOOL_BULL), "battle_axe_bull"),

					setItemName(new ItemSwordWalker(CQRMaterials.ToolMaterials.TOOL_WALKER), "sword_walker"),
					setItemName(new ItemShieldWalkerKing(), "shield_walker_king"),

					setItemName(new ItemGreatSword(ToolMaterial.IRON, 0.8F, 30), "great_sword_iron"),
					setItemName(new ItemGreatSword(ToolMaterial.DIAMOND, 0.9F, 25), "great_sword_diamond"),
					setItemName(new ItemGreatSword(CQRMaterials.ToolMaterials.TOOL_BULL, 1.0F, 20), "great_sword_bull"),
					setItemName(new ItemGreatSword(CQRMaterials.ToolMaterials.TOOL_MONKING, 2.0F, 20), "great_sword_monking"),

					setItemName(new ItemSpearBase(ToolMaterial.DIAMOND), "spear_diamond"),
					setItemName(new ItemSpearBase(ToolMaterial.IRON), "spear_iron"),

					setItemName(new ItemStaff(), "staff"),
					setItemName(new ItemStaffFire(), "staff_fire"),
					setItemName(new ItemStaffVampiric(), "staff_vampiric"),
					setItemName(new ItemStaffWind(), "staff_wind"),
					setItemName(new ItemStaffPoison(), "staff_poison"),
					setItemName(new ItemStaffHealing(), "staff_healing"),
					setItemNameAndTab(new ItemFakeSwordHealingStaff(ToolMaterial.DIAMOND), "diamond_sword_fake_healing_staff", null),
					setItemName(new ItemStaffThunder(), "staff_thunder"),
					setItemName(new ItemStaffSpider(), "staff_spider"),
					setItemName(new ItemStaffGun(), "staff_gun"),

					setItemName(new ItemRevolver(), "revolver"),
					setItemName(new ItemRevolver(), "captain_revolver"),
					setItemName(new ItemMusket(), "musket"),
					setItemName(new ItemMusketKnife(ToolMaterial.IRON), "musket_dagger_iron"),
					setItemName(new ItemMusketKnife(ToolMaterial.DIAMOND), "musket_dagger_diamond"),
					setItemName(new ItemMusketKnife(CQRMaterials.ToolMaterials.TOOL_MONKING), "musket_dagger_monking"),
					setItemName(new ItemBullet(), "bullet_iron"),
					setItemName(new ItemBullet(), "bullet_gold"),
					setItemName(new ItemBullet(), "bullet_diamond"),
					setItemName(new ItemBullet(), "bullet_fire"),
					setItemName(new ItemCannonBall(), "cannon_ball"),
					setItemName(new ItemFlamethrower(), "flamethrower"),
					setItemName(new ItemBubblePistol(), "bubble_pistol"),
					setItemName(new ItemBubbleRifle(), "bubble_rifle"),

					setItemName(new ItemHookshot(), "hookshot"),
					setItemName(new ItemLongshot(), "longshot"),
					setItemName(new ItemSpiderHook(), "spiderhook"),

					setItemName(new ItemHelmetDragon(CQRMaterials.ArmorMaterials.ARMOR_DRAGON, -1, EquipmentSlotType.HEAD), "helmet_dragon"),
					setItemName(new ItemBootsCloud(CQRMaterials.ArmorMaterials.ARMOR_CLOUD, -1, EquipmentSlotType.FEET), "boots_cloud"),
					setItemName(new ItemBackpack(CQRMaterials.ArmorMaterials.ARMOR_BACKPACK, -1, EquipmentSlotType.CHEST), "backpack"),
					setItemName(new ItemSpikedGlove(), "spiked_glove"),
					setItemName(new ItemCrown(CQRMaterials.ArmorMaterials.ARMOR_CROWN, -1), "king_crown"),

					setItemName(new ItemArmorSlime(CQRMaterials.ArmorMaterials.ARMOR_SLIME, -1, EquipmentSlotType.HEAD), "helmet_slime"),
					setItemName(new ItemArmorSlime(CQRMaterials.ArmorMaterials.ARMOR_SLIME, -1, EquipmentSlotType.CHEST), "chestplate_slime"),
					setItemName(new ItemArmorSlime(CQRMaterials.ArmorMaterials.ARMOR_SLIME, -1, EquipmentSlotType.LEGS), "leggings_slime"),
					setItemName(new ItemArmorSlime(CQRMaterials.ArmorMaterials.ARMOR_SLIME, -1, EquipmentSlotType.FEET), "boots_slime"),

					setItemName(new ItemArmorTurtle(CQRMaterials.ArmorMaterials.ARMOR_TURTLE, -1, EquipmentSlotType.HEAD), "helmet_turtle"),
					setItemName(new ItemArmorTurtle(CQRMaterials.ArmorMaterials.ARMOR_TURTLE, -1, EquipmentSlotType.CHEST), "chestplate_turtle"),
					setItemName(new ItemArmorTurtle(CQRMaterials.ArmorMaterials.ARMOR_TURTLE, -1, EquipmentSlotType.LEGS), "leggings_turtle"),
					setItemName(new ItemArmorTurtle(CQRMaterials.ArmorMaterials.ARMOR_TURTLE, -1, EquipmentSlotType.FEET), "boots_turtle"),

					setItemName(new ItemArmorBull(CQRMaterials.ArmorMaterials.ARMOR_BULL, -1, EquipmentSlotType.HEAD), "helmet_bull"),
					setItemName(new ItemArmorBull(CQRMaterials.ArmorMaterials.ARMOR_BULL, -1, EquipmentSlotType.CHEST), "chestplate_bull"),
					setItemName(new ItemArmorBull(CQRMaterials.ArmorMaterials.ARMOR_BULL, -1, EquipmentSlotType.LEGS), "leggings_bull"),
					setItemName(new ItemArmorBull(CQRMaterials.ArmorMaterials.ARMOR_BULL, -1, EquipmentSlotType.FEET), "boots_bull"),

					setItemName(new ItemArmorSpider(CQRMaterials.ArmorMaterials.ARMOR_SPIDER, -1, EquipmentSlotType.HEAD), "helmet_spider"),
					setItemName(new ItemArmorSpider(CQRMaterials.ArmorMaterials.ARMOR_SPIDER, -1, EquipmentSlotType.CHEST), "chestplate_spider"),
					setItemName(new ItemArmorSpider(CQRMaterials.ArmorMaterials.ARMOR_SPIDER, -1, EquipmentSlotType.LEGS), "leggings_spider"),
					setItemName(new ItemArmorSpider(CQRMaterials.ArmorMaterials.ARMOR_SPIDER, -1, EquipmentSlotType.FEET), "boots_spider"),

					setItemName(new ItemArmorInquisition(CQRMaterials.ArmorMaterials.ARMOR_INQUISITION, -1, EquipmentSlotType.HEAD), "helmet_inquisition"),
					setItemName(new ItemArmorInquisition(CQRMaterials.ArmorMaterials.ARMOR_INQUISITION, -1, EquipmentSlotType.CHEST), "chestplate_inquisition"),
					setItemName(new ItemArmorInquisition(CQRMaterials.ArmorMaterials.ARMOR_INQUISITION, -1, EquipmentSlotType.LEGS), "leggings_inquisition"),
					setItemName(new ItemArmorInquisition(CQRMaterials.ArmorMaterials.ARMOR_INQUISITION, -1, EquipmentSlotType.FEET), "boots_inquisition"),

					setItemName(new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_DIAMOND, -1, EquipmentSlotType.HEAD), "helmet_heavy_diamond"),
					setItemName(new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_DIAMOND, -1, EquipmentSlotType.CHEST), "chestplate_heavy_diamond"),
					setItemName(new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_DIAMOND, -1, EquipmentSlotType.LEGS), "leggings_heavy_diamond"),
					setItemName(new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_DIAMOND, -1, EquipmentSlotType.FEET), "boots_heavy_diamond"),

					setItemName(new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_IRON, -1, EquipmentSlotType.HEAD), "helmet_heavy_iron"),
					setItemName(new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_IRON, -1, EquipmentSlotType.CHEST), "chestplate_heavy_iron"),
					setItemName(new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_IRON, -1, EquipmentSlotType.LEGS), "leggings_heavy_iron"),
					setItemName(new ItemArmorHeavy(CQRMaterials.ArmorMaterials.ARMOR_HEAVY_IRON, -1, EquipmentSlotType.FEET), "boots_heavy_iron"),

					setItemName(new ItemArmorDyable(Material.DIAMOND, -1, EquipmentSlotType.HEAD), "helmet_diamond_dyable"),
					setItemName(new ItemArmorDyable(Material.DIAMOND, -1, EquipmentSlotType.CHEST), "chestplate_diamond_dyable"),
					setItemName(new ItemArmorDyable(Material.DIAMOND, -1, EquipmentSlotType.LEGS), "leggings_diamond_dyable"),
					setItemName(new ItemArmorDyable(Material.DIAMOND, -1, EquipmentSlotType.FEET), "boots_diamond_dyable"),

					setItemName(new ItemArmorDyable(Material.IRON, -1, EquipmentSlotType.HEAD), "helmet_iron_dyable"),
					setItemName(new ItemArmorDyable(Material.IRON, -1, EquipmentSlotType.CHEST), "chestplate_iron_dyable"),
					setItemName(new ItemArmorDyable(Material.IRON, -1, EquipmentSlotType.LEGS), "leggings_iron_dyable"),
					setItemName(new ItemArmorDyable(Material.IRON, -1, EquipmentSlotType.FEET), "boots_iron_dyable"),

					setItemName(new ItemLore(), "scale_turtle"),
					setItemName(new ItemLore(), "leather_bull"),
					setItemName(new ItemLore(), "horn_bull"),
					setItemName(new ItemLore(), "ball_slime"),
					setItemName(new ItemLore(), "leather_spider"),
					setItemName(new ItemLore(), "bone_monking"),
					setItemName(new ItemLore(), "giant_spider_poison"),
					setItemName(new ItemGoldenFeather(), "feather_golden"),

					setItemName(new ItemPotionHealing(), "potion_healing"),
					setItemName(new ItemTeleportStone(), "teleport_stone"),
					setItemName(new ItemCursedBone(), "cursed_bone"),
					setItemName(new ItemMagicBell(), "magic_bell"),

					setItemNameAndTab(new ItemSuperTool(), "super_tool", CQRMain.CQR_CREATIVE_TOOL_TAB),
					setItemNameAndTab(new ItemStructureSelector(), "structure_selector", CQRMain.CQR_CREATIVE_TOOL_TAB),
					setItemNameAndTab(new ItemSoulBottle(), "soul_bottle", CQRMain.CQR_CREATIVE_TOOL_TAB),
					setItemNameAndTab(new ItemMobToSpawner(), "mob_to_spawner_tool", CQRMain.CQR_CREATIVE_TOOL_TAB),
					setItemNameAndTab(new ItemSpawnerConverter(), "spawner_converter", CQRMain.CQR_CREATIVE_TOOL_TAB),
					setItemNameAndTab(new ItemBadge(), "badge", CQRMain.CQR_CREATIVE_TOOL_TAB),
					setItemNameAndTab(new ItemPathTool(), "path_tool", CQRMain.CQR_CREATIVE_TOOL_TAB),
					setItemNameAndTab(new ItemShieldDummy(), "dummy_shield", CQRMain.CQR_CREATIVE_TOOL_TAB),
					setItemNameAndTab(new ItemAlchemyBag(), "alchemy_bag", CQRMain.CQR_CREATIVE_TOOL_TAB),
					setItemNameAndTab(new ItemUnprotectedPositionTool(), "unprotected_positions_tool", CQRMain.CQR_CREATIVE_TOOL_TAB) };

			IForgeRegistry<Item> registry = event.getRegistry();

			for (Item item : items) {
				registry.register(item);
				ITEMS.add(item);
			}

			for (int i = 0; i < ItemShieldCQR.SHIELD_NAMES.length; i++) {
				Item item = setItemName(new ItemShieldCQR(1024, Items.IRON_INGOT), "shield_" + ItemShieldCQR.SHIELD_NAMES[i]);
				registry.register(item);
				ITEMS.add(item);
			}

			for (int i = 0; i <= ItemDungeonPlacer.HIGHEST_ICON_NUMBER; i++) {
				Item item = setItemNameAndTab(new ItemDungeonPlacer(i), "dungeon_placer_d" + i, CQRMain.CQR_DUNGEON_PLACER_TAB);
				registry.register(item);
				ITEMS.add(item);
			}

			registerSpawnEggs(EntityCQRBoarman.class, "boarman", registry);
			registerSpawnEggs(EntityCQRDummy.class, "dummy", registry);
			registerSpawnEggs(EntityCQRDwarf.class, "dwarf", registry);
			registerSpawnEggs(EntityCQREnderman.class, "enderman", registry);
			registerSpawnEggs(EntityCQRGremlin.class, "gremlin", registry);
			registerSpawnEggs(EntityCQRGolem.class, "golem", registry);
			registerSpawnEggs(EntityCQRIllager.class, "illager", registry);
			registerSpawnEggs(EntityCQRMandril.class, "mandril", registry);
			registerSpawnEggs(EntityCQRMinotaur.class, "minotaur", registry);
			registerSpawnEggs(EntityCQRMummy.class, "mummy", registry);
			registerSpawnEggs(EntityCQRNPC.class, "npc", registry);
			registerSpawnEggs(EntityCQROgre.class, "ogre", registry);
			registerSpawnEggs(EntityCQROrc.class, "orc", registry);
			registerSpawnEggs(EntityCQRPirate.class, "pirate", registry);
			registerSpawnEggs(EntityCQRSkeleton.class, "skeleton", registry);
			registerSpawnEggs(EntityCQRSpectre.class, "spectre", registry);
			registerSpawnEggs(EntityCQRTriton.class, "triton", registry);
			registerSpawnEggs(EntityCQRWalker.class, "walker", registry);
			registerSpawnEggs(EntityCQRZombie.class, "zombie", registry);
		}

		private static void registerSpawnEggs(Class<? extends AbstractEntityCQR> entityClass, String entityName, IForgeRegistry<Item> registry) {
			List<Item> spawnEggList = ItemSpawnEggCQR.getItemList(entityClass, entityName);
			for (int i = 0; i < spawnEggList.size(); i++) {
				Item item = setItemNameAndTab(spawnEggList.get(i), "cqr_" + entityName + "_spawn_egg_" + i, CQRMain.CQR_SPAWN_EGG_TAB);
				registry.register(item);
				ITEMS.add(item);
				SPAWN_EGGS.add(item);
			}
		}

		private static Item setItemName(Item item, String name) {
			return setItemNameAndTab(item, name, CQRMain.CQR_ITEMS_TAB);
		}

		private static Item setItemNameAndTab(Item item, String name, @Nullable ItemGroup tab) {
			return item.setRegistryName(CQRMain.MODID, name).setTranslationKey(name).setCreativeTab(tab);
		}

	}
	*/

}

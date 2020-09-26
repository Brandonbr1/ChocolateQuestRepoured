package com.teamcqr.chocolatequestrepoured.objects.items.armor;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Multimap;
import com.teamcqr.chocolatequestrepoured.capability.armor.CapabilityCooldownHandlerHelper;
import com.teamcqr.chocolatequestrepoured.client.init.ModArmorModels;
import com.teamcqr.chocolatequestrepoured.init.CQRItems;
import com.teamcqr.chocolatequestrepoured.objects.entity.EntitySlimePart;
import com.teamcqr.chocolatequestrepoured.util.ItemUtil;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemArmorSlime extends ItemArmor {

	private AttributeModifier health;
	private AttributeModifier knockBack;

	public ItemArmorSlime(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
		super(materialIn, renderIndexIn, equipmentSlotIn);

		this.health = new AttributeModifier("SlimeHealthModifier", 2D, 0);
		this.knockBack = new AttributeModifier("SlimeKnockbackModifier", -0.25D, 0);
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == EntityLiving.getSlotForItemStack(stack)) {
			multimap.put(SharedMonsterAttributes.MAX_HEALTH.getName(), this.health);
			multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), this.knockBack);
		}

		return multimap;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.slime_armor.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	@Nullable
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
		return armorSlot == EntityEquipmentSlot.LEGS ? ModArmorModels.slimeArmorLegs : ModArmorModels.slimeArmor;
	}

	@EventBusSubscriber(modid = Reference.MODID)
	public static class EventHandler {

		@SubscribeEvent
		public static void onLivingHurtEvent(LivingHurtEvent event) {
			EntityLivingBase entity = event.getEntityLiving();

			if (ItemUtil.hasFullSet(entity, ItemArmorSlime.class) && !CapabilityCooldownHandlerHelper.onCooldown(entity, CQRItems.CHESTPLATE_SLIME)) {
				if (!entity.world.isRemote) {
					EntitySlimePart slime = new EntitySlimePart(entity.world, entity);
					double x = entity.posX - 5.0D + 2.5D * slime.getRNG().nextDouble();
					double y = entity.posY;
					double z = entity.posZ - 5.0D + 2.5D * slime.getRNG().nextDouble();
					slime.setPosition(x, y, z);
					entity.world.spawnEntity(slime);
				}
				CapabilityCooldownHandlerHelper.setCooldown(entity, CQRItems.CHESTPLATE_SLIME, 160);
			}
		}

	}

}

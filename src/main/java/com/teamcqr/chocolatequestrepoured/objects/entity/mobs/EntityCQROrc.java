package com.teamcqr.chocolatequestrepoured.objects.entity.mobs;

import com.teamcqr.chocolatequestrepoured.factions.EDefaultFaction;
import com.teamcqr.chocolatequestrepoured.objects.entity.EBaseHealths;
import com.teamcqr.chocolatequestrepoured.objects.entity.ELootTablesNormal;
import com.teamcqr.chocolatequestrepoured.objects.entity.ai.EntityAITorchIgniter;
import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityCQROrc extends AbstractEntityCQR {

	public EntityCQROrc(World worldIn) {
		super(worldIn);
	}

	@Override
	public float getBaseHealth() {
		return EBaseHealths.ORC.getValue();
	}

	@Override
	public EDefaultFaction getDefaultFaction() {
		return EDefaultFaction.GOBLINS;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return ELootTablesNormal.ENTITY_ORC.getLootTable();
	}

	@Override
	public int getTextureCount() {
		return 3;
	}

}

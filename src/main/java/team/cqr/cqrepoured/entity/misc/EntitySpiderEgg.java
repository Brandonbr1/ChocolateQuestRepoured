package team.cqr.cqrepoured.entity.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntitySpiderEgg extends Entity {

	private static final int STAGE_DURATION = 30;
	private static final int STAGE_COUNT = 5;
	private static final ResourceLocation MINION_ID = new ResourceLocation("minecraft", "cave_spider");

	protected static final DataParameter<Integer> STAGE = EntityDataManager.<Integer>createKey(EntitySpiderEgg.class, DataSerializers.VARINT);

	private int currentStageDuration = 0;

	public EntitySpiderEgg(World worldIn) {
		super(worldIn);
		this.setSize(1F, 1F);

	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	protected void entityInit() {
		this.dataManager.register(STAGE, 0);
	}

	@Override
	public void tick() {
		// TODO: Play particles and sound on mob spawning
		if (!this.world.isRemote) {
			this.currentStageDuration++;
			if (this.currentStageDuration > STAGE_DURATION) {
				this.dataManager.set(STAGE, this.getStage() + 1);
				this.currentStageDuration = 0;
			}
			super.tick();
			if (this.getStage() >= STAGE_COUNT) {
				// Destroy yourself and spawn the spider
				Entity spider = EntityList.createEntityByIDFromName(MINION_ID, this.world);
				if (spider != null) {
					spider.setPosition(this.posX, this.posY + 0.5D, this.posZ);
					this.world.spawnEntity(spider);
				}
				this.setDead();
			}
		} else {
			super.tick();
		}
	}

	@Override
	protected void readEntityFromNBT(CompoundNBT compound) {
		this.dataManager.set(STAGE, compound.getInteger("stage"));
		this.currentStageDuration = compound.getInteger("stage_duration");
	}

	@Override
	protected void writeEntityToNBT(CompoundNBT compound) {
		compound.setInteger("stage", this.getStage());
		compound.setInteger("stage_duration", this.currentStageDuration);
	}

	public int getStage() {
		return this.dataManager.get(STAGE);
	}

}

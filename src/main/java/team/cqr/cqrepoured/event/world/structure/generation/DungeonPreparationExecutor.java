package team.cqr.cqrepoured.event.world.structure.generation;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.world.IWorld;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import team.cqr.cqrepoured.CQRMain;

@EventBusSubscriber(modid = CQRMain.MODID)
public class DungeonPreparationExecutor {

	private static final ThreadFactory DEFAULT_THREAD_FACTORY = task -> new Thread(task, "CQR Dungeon Preparation Thread");
	private static final Map<IWorld, ExecutorService> DIM_2_EXECUTOR = new ConcurrentHashMap<>();

	public static Executor getExecutor(IWorld world) {
		return DIM_2_EXECUTOR.computeIfAbsent(world, key -> Executors.newSingleThreadExecutor(DEFAULT_THREAD_FACTORY));
	}

	public static <T> CompletableFuture<T> supplyAsync(IWorld world, Supplier<T> supplier) {
		return CompletableFuture.supplyAsync(supplier, getExecutor(world));
	}

	public static <T> CompletableFuture<T> supplyAsync(IWorld world, Function<IWorld, T> supplier) {
		return CompletableFuture.supplyAsync(() -> supplier.apply(world), getExecutor(world));
	}

	public static <T> CompletableFuture<Void> thenAcceptAsync(IWorld world, CompletableFuture<T> future, Consumer<T> action) {
		return future.thenAcceptAsync(action, getExecutor(world));
	}

	public static <T, R> CompletableFuture<R> thenApplyAsync(IWorld world, CompletableFuture<T> future, Function<T, R> func) {
		return future.thenApplyAsync(func, getExecutor(world));
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onWorldLoadEvent(WorldEvent.Load event) {
		DIM_2_EXECUTOR.computeIfAbsent(event.getWorld(), key -> Executors.newSingleThreadExecutor(DEFAULT_THREAD_FACTORY));
	}

	public static void onWorldUnloadEvent(IWorld iWorld) {
		ExecutorService executor = DIM_2_EXECUTOR.remove(iWorld);
		if (executor == null) {
			return;
		}
		executor.shutdown();
		try {
			if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
				executor.shutdownNow();
				if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
					CQRMain.logger.error("Couldn't shutdown dungeon preparation executor!");
				}
			}
		} catch (InterruptedException e) {
			executor.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}

}

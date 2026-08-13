package fr.renewguard.util;
 
import javafx.application.Platform;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
 
public final class PollingScheduler {
    private static final PollingScheduler INSTANCE = new PollingScheduler();
    private final ScheduledExecutorService executor;
    private final AtomicInteger threadIndex = new AtomicInteger(0);
 
    private PollingScheduler() {
        executor = Executors.newScheduledThreadPool(3, r -> {
            Thread t = new Thread(r, "rg-poll-" + threadIndex.getAndIncrement());
            t.setDaemon(true);
            t.setPriority(Thread.MIN_PRIORITY);
            return t;
        });
    }
 
    public static PollingScheduler getInstance() { return INSTANCE; }
 
    public Handle schedule(Runnable fxTask, long intervalSeconds) {
        return schedule(fxTask, intervalSeconds, intervalSeconds);
    }
 
    public Handle schedule(Runnable fxTask, long initialDelay, long intervalSeconds) {
        ScheduledFuture<?> future = executor.scheduleAtFixedRate(() -> {
            try { Platform.runLater(fxTask); }
            catch (Exception e) { System.err.println("[PollingScheduler] Task error: " + e.getMessage()); }
        }, initialDelay, intervalSeconds, TimeUnit.SECONDS);
        return new FutureHandle(future);
    }
 
    public Handle scheduleOnce(Runnable fxTask, long delaySeconds) {
        ScheduledFuture<?> future = executor.schedule(() -> Platform.runLater(fxTask), delaySeconds, TimeUnit.SECONDS);
        return new FutureHandle(future);
    }
 
    public void shutdown() {
        executor.shutdownNow();
        try { executor.awaitTermination(2, TimeUnit.SECONDS); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
 
    public interface Handle { void cancel(); boolean isCancelled(); }
 
    private record FutureHandle(ScheduledFuture<?> future) implements Handle {
        @Override public void cancel() { future.cancel(false); }
        @Override public boolean isCancelled() { return future.isCancelled(); }
    }
 
    public static Handle compose(Handle... handles) {
        return new Handle() {
            @Override public void cancel() { for (Handle h : handles) h.cancel(); }
            @Override public boolean isCancelled() { for (Handle h : handles) if (!h.isCancelled()) return false; return true; }
        };
    }
}
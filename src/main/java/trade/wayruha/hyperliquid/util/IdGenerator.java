package trade.wayruha.hyperliquid.util;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
  private static final AtomicInteger COUNTER = new AtomicInteger();

  public static int getNextId() {
    return COUNTER.addAndGet(1);
  }
}

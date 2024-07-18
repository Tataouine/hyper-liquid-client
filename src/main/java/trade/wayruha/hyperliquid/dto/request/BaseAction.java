package trade.wayruha.hyperliquid.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BaseAction {
  private final String type;

  public String getType() {
    return this.type;
  }
}

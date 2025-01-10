package trade.wayruha.hyperliquid.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class BaseAction {
  private final String type;

  public String getType() {
    return this.type;
  }
}

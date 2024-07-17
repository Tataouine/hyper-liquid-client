package trade.wayruha.hyperliquid.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum MarginType {
  ISOLATED("isolated"), CROSS("cross");

  @Getter
  @JsonValue
  private final String name;

  MarginType(String name) {
    this.name = name;
  }
}

package trade.wayruha.hyperliquid.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Tpsl {
  TP("tp"), SL("sl");

  @JsonValue
  private final String name;

  Tpsl(String name) {
    this.name = name;
  }
}

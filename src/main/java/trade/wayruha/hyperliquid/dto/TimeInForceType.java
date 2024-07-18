package trade.wayruha.hyperliquid.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum TimeInForceType {
  GTC("Gtc"), IOC("Ioc"), ALO("Alo");

  @JsonValue
  private final String name;

  TimeInForceType(String name) {
    this.name = name;
  }
}

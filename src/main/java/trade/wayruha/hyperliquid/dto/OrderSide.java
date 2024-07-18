package trade.wayruha.hyperliquid.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum OrderSide {
  ASK("A"), BID("B");

  @JsonValue
  private final String name;

  OrderSide(String name) {
    this.name = name;
  }
}

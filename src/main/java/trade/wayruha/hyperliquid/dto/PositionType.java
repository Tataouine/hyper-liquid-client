package trade.wayruha.hyperliquid.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PositionType {
  ONE_WAY("oneWay");

  @Getter
  @JsonValue
  private final String name;
}

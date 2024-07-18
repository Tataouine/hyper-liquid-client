package trade.wayruha.hyperliquid.dto.request;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum InfoRequestType {
  META("meta"), META_ASSET_CONTEXT("metaAndAssetCtxs"), ACCOUNT_STATE("clearinghouseState"), ORDER_STATUS("orderStatus");

  @Getter
  @JsonValue
  private final String name;

  InfoRequestType(String name) {
    this.name = name;
  }
}

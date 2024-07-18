package trade.wayruha.hyperliquid.dto;

import lombok.Value;

@Value
public class EthereumSignature {
  String r;
  String s;
  int v;
}

package trade.wayruha.hyperliquid.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import trade.wayruha.hyperliquid.dto.EthereumSignature;

@Data
@AllArgsConstructor
@JsonPropertyOrder({"action", "nonce", "signature", "vaultAddress"})
public class ExchangeRequest {

  private BaseAction action;

  private long nonce;

  @JsonProperty("signature")
  private EthereumSignature ethereumSignature;

  private String vaultAddress;

  public ExchangeRequest(BaseAction orderAction, long nonce, EthereumSignature ethereumSignature) {
    this.action = orderAction;
    this.nonce = nonce;
    this.ethereumSignature = ethereumSignature;
    this.vaultAddress = null;
  }
}

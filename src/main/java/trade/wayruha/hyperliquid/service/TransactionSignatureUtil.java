package trade.wayruha.hyperliquid.service;

import lombok.experimental.UtilityClass;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Sign;
import org.web3j.crypto.StructuredData;
import org.web3j.crypto.StructuredDataEncoder;
import org.web3j.utils.Numeric;
import trade.wayruha.hyperliquid.dto.EthereumSignature;
import trade.wayruha.hyperliquid.util.ActionSigningUtils;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class TransactionSignatureUtil {

  public static EthereumSignature signStandardL1Action(Map<String, Object> action, String walletPrivateKey, long nonce, boolean isMainNet) throws Exception {
    final ECKeyPair ecKeyPair = ECKeyPair.create(Numeric.toBigInt(walletPrivateKey));

    final String hash = actionHash(action, null, nonce);
    final Map<String, String> phantomAgent = constructPhantomAgent(hash, isMainNet);

    StructuredData.EIP712Message eip712Message = ActionSigningUtils.constructEIP712Message(phantomAgent);

    return signInner(ecKeyPair, eip712Message);
  }

  private static Map<String, String> constructPhantomAgent(String hash, boolean isMainNet) {
    Map<String, String> resultMap = new HashMap<>();
    resultMap.put("source", isMainNet ? "a" : "b");
    resultMap.put("connectionId", hash);

    return resultMap;
  }

  private static String actionHash(Map<String, Object> action, String vaultAddress, long nonce) throws Exception {
    MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
    packMapToMessagePack(action, packer);

    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
    buffer.putLong(nonce);
    packer.writePayload(buffer.array());

    byte[] byteArray = packer.toByteArray();

    if (vaultAddress == null)
      byteArray = ActionSigningUtils.appendByte(byteArray, (byte) 0x00);
    else
      byteArray = ActionSigningUtils.appendByte(byteArray, (byte) 0x01);

    //HASH
    Keccak.Digest256 digest256 = new Keccak.Digest256();
    byte[] hash = digest256.digest(byteArray);

    return Numeric.toHexString(hash);
  }

  private static EthereumSignature signInner(ECKeyPair keyPair, StructuredData.EIP712Message eip712Message) {

    StructuredDataEncoder encoder = new StructuredDataEncoder(eip712Message);
    byte[] encodedMessage = encoder.hashStructuredData();

    Sign.SignatureData signatureData = Sign.signMessage(encodedMessage, keyPair, false);

    return splitSig(signatureData);
  }

  private static EthereumSignature splitSig(Sign.SignatureData sig) {
    String r = Numeric.toHexString(sig.getR());
    String s = Numeric.toHexString(sig.getS());
    int v = sig.getV()[0];

    // Adjust v value
    if (v == 27 || v == 28) {
      return new EthereumSignature(r, s, v);
    } else if (v == 0 || v == 1) {
      return new EthereumSignature(r, s, v + 27);
    } else {
      throw new IllegalArgumentException("Invalid v value: " + v);
    }
  }

  private static void packMapToMessagePack(Map<String, Object> map, MessageBufferPacker packer) throws Exception {
    packer.packMapHeader(map.size());

    for (Map.Entry<String, Object> entry : map.entrySet()) {
      packer.packString(entry.getKey());

      Object value = entry.getValue();
      packValueToMessagePackMap(value, packer);
    }
  }

  private static void packValueToMessagePackMap(Object value, MessageBufferPacker packer) throws Exception {
    if (value instanceof String) {
      packer.packString((String) value);
    } else if (value instanceof Integer) {
      packer.packInt((Integer) value);
    } else if (value instanceof Boolean) {
      packer.packBoolean((Boolean) value);
    } else if (value instanceof Long) {
      packer.packLong((Long) value);
    } else if (value instanceof BigDecimal) {
      packer.packString(((BigDecimal) value).toPlainString());
    } else if (value instanceof Map) {
      packMapToMessagePack((Map<String, Object>) value, packer);
    } else if (value instanceof List) {
      List<?> list = (List<?>) value;
      packer.packArrayHeader(list.size());
      for (Object element : list) {
        packValueToMessagePackMap(element, packer);
      }
    } else {
      throw new IllegalArgumentException("Unsupported data type: " + value.getClass());
    }
  }
}



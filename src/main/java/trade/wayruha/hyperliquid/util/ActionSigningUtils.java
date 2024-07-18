package trade.wayruha.hyperliquid.util;

import org.web3j.crypto.StructuredData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class ActionSigningUtils {

  public static byte[] removeByteValue(byte[] originalArray, byte byteToRemove) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    for (byte b : originalArray) {
      if (b != byteToRemove) {
        outputStream.write(b);
      }
    }

    return outputStream.toByteArray();
  }

  public static byte[] appendByte(byte[] originalArray, byte byteToAdd) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    outputStream.write(originalArray);
    outputStream.write(byteToAdd);

    return outputStream.toByteArray();
  }

  public static StructuredData.EIP712Message constructEIP712Message(Object message) {
    final StructuredData.EIP712Domain eip712Domain = new StructuredData.EIP712Domain("Exchange", "1", "1337", "0x0000000000000000000000000000000000000000", null);

    return new StructuredData.EIP712Message(constructEIP712Types(), "Agent", message, eip712Domain);
  }

  private static HashMap<String, List<StructuredData.Entry>> constructEIP712Types() {
    HashMap<String, List<StructuredData.Entry>> resultHashMap = new HashMap<>();

    //Agent
    List<StructuredData.Entry> agentType = new ArrayList<>();
    agentType.add(new StructuredData.Entry("source", "string"));
    agentType.add(new StructuredData.Entry("connectionId", "bytes32"));
    resultHashMap.put("Agent", agentType);

    //EIP712Domain
    List<StructuredData.Entry> eip712DomainType = new ArrayList<>();
    eip712DomainType.add(new StructuredData.Entry("name", "string"));
    eip712DomainType.add(new StructuredData.Entry("version", "string"));
    eip712DomainType.add(new StructuredData.Entry("chainId", "uint256"));
    eip712DomainType.add(new StructuredData.Entry("verifyingContract", "address"));
    resultHashMap.put("EIP712Domain", eip712DomainType);

    return resultHashMap;
  }

}

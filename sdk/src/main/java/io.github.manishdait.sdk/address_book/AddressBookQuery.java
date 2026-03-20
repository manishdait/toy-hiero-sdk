package io.github.manishdait.sdk.address_book;

import com.hedera.hashgraph.sdk.proto.mirror.NetworkServiceGrpc;
import io.github.manishdait.sdk.Client;
import java.util.ArrayList;
import java.util.List;

public class AddressBookQuery {
  public List<NodeAddress> execute(final Client client) {
    var addressBook = new ArrayList<NodeAddress>();
    var networkStub = NetworkServiceGrpc.newBlockingStub(client.getMirrorChannel());
    var query =
        com.hedera.hashgraph.sdk.proto.mirror.AddressBookQuery.newBuilder()
            .setFileId(
                com.hedera.hashgraph.sdk.proto.FileID.newBuilder()
                    .setShardNum(0)
                    .setRealmNum(0)
                    .setFileNum(102)
                    .build())
            .build();

    networkStub
        .getNodes(query)
        .forEachRemaining(
            (nodeAddress) -> {
              addressBook.add(NodeAddress.fromProto(nodeAddress));
            });

    return addressBook;
  }
}

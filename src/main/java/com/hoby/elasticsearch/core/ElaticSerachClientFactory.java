package com.hoby.elasticsearch.core;

import static java.util.Arrays.asList;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.node.InternalSettingsPreparer;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeValidationException;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.transport.Netty4Plugin;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ElaticSerachClientFactory {
	private static Logger logger = LoggerFactory.getLogger(ElaticSerachClientFactory.class);

	@Value("${elsticSearch.host}")
	private String elsticSearchHost = "127.0.0.1";

	@Value("${elsticSearch.port}")
	private Integer elsticSearchPort = 9300;

	private static final String clusterName = "elasticsearch";

	private Map<String, Client> localClientMap = new HashMap<>();

	private static final Object pathHome = "D:/kibana/elasticsearch-5.4.3";

	private static final Object pathData = "D:/kibana/elasticsearch-5.4.3/data";

	public Client getClient() throws UnknownHostException, NodeValidationException {
		return getClient(null);
	}

	public Client getClient(ClientTypeEnum clientType) throws UnknownHostException, NodeValidationException {
		if (clientType == null) {
			clientType = ClientTypeEnum.TRANSPORT_CLIENT;
		}

		if (localClientMap.get(clientType.name()) == null) {
			if (ClientTypeEnum.TRANSPORT_CLIENT.equals(clientType)) {
				TransportClient transportClient = getTransportClient();
				localClientMap.put(clientType.name(), transportClient);
			} else if (ClientTypeEnum.CLIENT.equals(clientType)) {
				NodeClient nodeClient = getNodeClient();
				localClientMap.put(clientType.name(), nodeClient);
			}
		}
		return localClientMap.get(clientType.name());
	}

	@SuppressWarnings("resource")
	public TransportClient getTransportClient() throws UnknownHostException {
		TransportClient client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(
				new InetSocketTransportAddress(InetAddress.getByName(elsticSearchHost), elsticSearchPort));

		return client;
	}

	@SuppressWarnings("resource")
	public NodeClient getNodeClient() throws UnknownHostException, NodeValidationException {
		Settings preparedSettings = Settings.builder().put(Settings.builder().build()).put("transport.type", "netty4")
				.put("transport.type", "local").put("http.type", "netty4").put("path.home", this.pathHome)
				.put("path.data", this.pathData).put("cluster.name", this.clusterName)
				.put("node.max_local_storage_nodes", 100).put("script.inline", "true")
				.put("tests.jarhell.check", "false").build();
		NodeClient nodeClient = (NodeClient) new TestNode(preparedSettings, asList(Netty4Plugin.class)).start()
				.client();

		return nodeClient;
	}

	public static class TestNode extends Node {
		public TestNode(Settings preparedSettings, Collection<Class<? extends Plugin>> classpathPlugins) {
			super(InternalSettingsPreparer.prepareEnvironment(preparedSettings, null), classpathPlugins);
		}
	}

	/**
	 * on shutdown
	 * 
	 * @param client
	 */
	public void closeClient(TransportClient client) {
		if (client != null) {
			client.close();
		}
	}
}

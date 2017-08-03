package com.hoby.elasticsearch.core;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.node.NodeValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElaticSerachOperationServiceImpl implements ElaticSerachOperationService{
	private static Logger logger = LoggerFactory.getLogger(ElaticSerachOperationServiceImpl.class);

	@Autowired
	ElaticSerachClientFactory elaticSerachClientFactory;

	public Client getClient() {
		try {
			return elaticSerachClientFactory.getClient();//ClientTypeEnum.CLIENT此处用这个有个bug
		} catch (UnknownHostException | NodeValidationException e) {
			logger.error("getClient error", e);
		}
		return null;
	}

	@Override
	public IndexResponse index(String index, String type, String id, String json) throws IOException {
		IndexResponse response = getClient().prepareIndex(index, type, id).setSource(json, XContentType.JSON).get();
        logger.trace("======index response {}",response);
		return response;
	}

	@Override
	public IndexResponse index(String index, String type, String id, Map<String, Object> fieldMap) throws IOException {
		IndexResponse response = getClient().prepareIndex(index, type, id)
				.setSource(ElaticSerachUtil.getXContentBuilder(fieldMap)).get();
        logger.trace("======index response {}",response);
		return response;
	}

	@Override
	public GetResponse get(String index, String type, String id) throws IOException {
		GetResponse response = getClient().prepareGet(index, type, id).get();
        logger.trace("======get response {}",response);
		return response;
	}

}

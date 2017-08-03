package com.hoby.elasticsearch.core;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElaticSerachUtil {
	private static Logger log = LoggerFactory.getLogger(ElaticSerachUtil.class);

	public static XContentBuilder getXContentBuilder(Map<String, Object> fieldMap) throws IOException {
		XContentBuilder builder = jsonBuilder().startObject();

		for (Map.Entry<String, Object> entry : fieldMap.entrySet()) {
			builder.field(entry.getKey(), entry.getValue());
		}

		return builder.endObject();
	}

	/**
	 * https://www.elastic.co/guide/en/elasticsearch/reference/5.5/docs-index_.
	 * html
	 * 
	 * @param response
	 * @return
	 */
	public static boolean isSuccess(IndexResponse response) {
		if (response.getShardInfo().getFailed() > 0) {
			log.warn("some copy is fail response {}", response);
		}
		return response.getShardInfo().getSuccessful() >= 1 ? true : false;
	}
}

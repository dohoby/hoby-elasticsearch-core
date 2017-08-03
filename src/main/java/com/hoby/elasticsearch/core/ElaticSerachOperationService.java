package com.hoby.elasticsearch.core;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;

public interface ElaticSerachOperationService {

	IndexResponse index(String index, String type, String id, String json) throws IOException;

	IndexResponse index(String index, String type, String id, Map<String, Object> fieldMap) throws IOException;

	GetResponse get(String index, String type, String id) throws IOException;

}

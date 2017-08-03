package elasticsearch.test;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hoby.elasticsearch.core.ElaticSerachOperationService;
import com.hoby.elasticsearch.core.ElaticSerachOperationServiceImpl;
import com.hoby.elasticsearch.core.ElaticSerachUtil;

import org.junit.Assert;
import org.junit.Assert.*;;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/elasticsearch-core-test.xml")
public class TestElasticSearchOperation {
	private static Logger logger = LoggerFactory.getLogger(ElaticSerachOperationServiceImpl.class);

	@Autowired
	ElaticSerachOperationService elaticSerachOpService;


	@Test
	public void testIndex() throws IOException {
		testIndex1();
		testIndex2();

	}
	

	@Test
	public void testGet() throws IOException {
		String index = "twitter";
		String type = "tweet";
		String id = "1";
		GetResponse rep = elaticSerachOpService.get(index, type, id);
		//Assert.assertEquals(true, ElaticSerachUtil.isSuccess(rep));
        logger.trace("======GetResponse  {}",rep.getSourceAsMap());

	}

	public void testIndex2() throws IOException {
		String index = "hoby";
		String type = "blog";
		String id = "es1";
		Map<String, Object> fieldMap = new HashMap<>();
		fieldMap.put("user", "hoby");
		fieldMap.put("postDate", new Date());
		fieldMap.put("message", "hoby index Elasticsearch");

		IndexResponse rep = elaticSerachOpService.index(index, type, id, fieldMap);
		Assert.assertEquals(true, ElaticSerachUtil.isSuccess(rep));
	}

	public void testIndex1() throws IOException {
		String index = "twitter";
		String type = "tweet";
		String id = "1";
		Map<String, Object> fieldMap = new HashMap<>();
		fieldMap.put("user", "kimchy");
		fieldMap.put("postDate", new Date());
		fieldMap.put("message", "trying out Elasticsearch");

		IndexResponse rep = elaticSerachOpService.index(index, type, id, fieldMap);
		Assert.assertEquals(true, ElaticSerachUtil.isSuccess(rep));

	}
}

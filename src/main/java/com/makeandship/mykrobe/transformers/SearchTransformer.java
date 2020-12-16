package com.makeandship.mykrobe.transformers;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueStore;

import com.makeandship.mykrobe.converters.DateConverter;
import com.makeandship.mykrobe.models.DebeziumSearchPayload;
import com.makeandship.mykrobe.models.Search;
import com.makeandship.mykrobe.models.SearchKey;
import com.makeandship.mykrobe.models.mongo.MongoBigsi;
import com.makeandship.mykrobe.models.mongo.MongoQuery;
import com.makeandship.mykrobe.models.mongo.MongoSearch;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class SearchTransformer {
	protected KeyValueStore<SearchKey, Search> stateStore;
	protected DebeziumSearchPayload payload;

	public SearchTransformer(KeyValueStore<SearchKey, Search> stateStore, DebeziumSearchPayload payload) {
		this.stateStore = stateStore;
		this.payload = payload;
	}

	protected void buildSearch(Search search, String id, MongoSearch mongoSearch) {
		log.debug(SearchTransformer.class.getName() + "#buildSearch: enter");
		if (mongoSearch.getBigsi() != null) {
			MongoBigsi bigsi = mongoSearch.getBigsi();
			if (bigsi.getQuery() != null) {
				MongoQuery query = bigsi.getQuery();
				search.setBigsiAlt(query.getAlt());
				search.setBigsiGene(query.getGene());
				search.setBigsiPos(query.getPos());
				search.setBigsiRef(query.getRef());
				search.setBigsiSeq(query.getSeq());
				search.setBigsiThreshold(query.getThreshold());
			}
		}

		if (mongoSearch.getCreated() != null) {
			search.setCreated(DateConverter.dateToMysql(mongoSearch.getCreated().getDate()));
		}

		if (mongoSearch.getExpires() != null) {
			search.setExpires(DateConverter.dateToMysql(mongoSearch.getExpires().getDate()));
		}

		if (mongoSearch.getModified() != null) {
			search.setModified(DateConverter.dateToMysql(mongoSearch.getModified().getDate()));
		}

		search.setId(mongoSearch.getId().getOid());
		search.setStatus(mongoSearch.getStatus());
		search.setType(mongoSearch.getType());

		log.debug(SearchTransformer.class.getName() + "#buildSearch: exit");
	}

	public abstract KeyValue<SearchKey, Search> transform();
}

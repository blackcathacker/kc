package org.kuali.kra.award.dao;

import java.util.Collection;

import org.codehaus.jackson.map.annotate.JsonView;
import org.kuali.coeus.sys.framework.rest.JsonViews;

public class SearchResults<T> {

	private Integer totalResults;
	private Collection<T> results;
	
	@JsonView(JsonViews.Summary.class)
	public Integer getTotalResults() {
		return totalResults;
	}
	public void setTotalResults(Integer totalResults) {
		this.totalResults = totalResults;
	}
	@JsonView(JsonViews.Summary.class)
	public Collection<T> getResults() {
		return results;
	}
	public void setResults(Collection<T> results) {
		this.results = results;
	}
	
}

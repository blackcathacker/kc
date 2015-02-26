package org.kuali.coeus.award.summary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.annotate.JsonView;
import org.kuali.coeus.sys.framework.rest.JsonViews;
import org.kuali.kra.award.dao.AwardDao;
import org.kuali.kra.award.dao.SearchResults;
import org.kuali.kra.award.home.Award;
import org.kuali.kra.subaward.service.SubAwardService;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codiform.moo.Moo;
import com.codiform.moo.curry.Translate;
import com.google.common.collect.Maps;

@Controller("awardSummaryController")
public class AwardSummaryController {

	@Autowired
	@Qualifier("businessObjectService")
	private BusinessObjectService businessObjectService;

	@Autowired
	@Qualifier("awardDao")
	private AwardDao awardDao;
	
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public AwardSummaryController() {
		objectMapper.configure(Feature.DEFAULT_VIEW_INCLUSION, false);
		objectMapper.configure(Feature.INDENT_OUTPUT, true);
	}

	@RequestMapping("/v1Jackson/awardSummary")
	public @ResponseBody String getAwardSummaryJackson(@RequestParam(value="updatedSince", required=false) Date updatedSince,
			@RequestParam(value="page", required=false, defaultValue="0") Integer page, @RequestParam(value="numberPerPage", required=false, defaultValue="50") Integer numberPerPage) throws JsonGenerationException, JsonMappingException, IOException {		
		return objectMapper.writerWithView(JsonViews.Summary.class)
				.writeValueAsString(getAwardDao().retrievePopulatedAwardByCriteria(new HashMap<String, Object>(), updatedSince, page, numberPerPage));
	}
		
	@RequestMapping("/v1Map/awardSummary")
	public @ResponseBody Map<String, Object> getAwardSummaryMap(@RequestParam(value="updatedSince", required=false) Date updatedSince,
			@RequestParam(value="page", required=false, defaultValue="0") Integer page, @RequestParam(value="numberPerPage", required=false, defaultValue="50") Integer numberPerPage) {
		List<Map> awards = new ArrayList<>();
		SearchResults<Award> searchResult = getAwardDao().retrievePopulatedAwardByCriteria(new HashMap<String, Object>(), updatedSince, page, numberPerPage);
		for (Award award : searchResult.getResults()) {
			awards.add(award.toSummaryMap());
		}
		Map<String, Object> results = new HashMap<>();
		results.put("totalFound", searchResult.getTotalResults());
		results.put("count", awards.size());
		results.put("awards", awards);
		return results;
	}

	@RequestMapping("/v1Dto/awardSummary")
	public @ResponseBody AwardResults getAwardSummaryDto(@RequestParam(value="updatedSince", required=false) Date updatedSince,
			@RequestParam(value="page", required=false, defaultValue="0") Integer page, @RequestParam(value="numberPerPage", required=false, defaultValue="50") Integer numberPerPage) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		modelMapper.addMappings(new AwardResultsMapper());
		SearchResults<Award> results = getAwardDao().retrievePopulatedAwardByCriteria(new HashMap<String, Object>(), updatedSince, page, numberPerPage);
		return modelMapper.map(results, AwardResults.class);
	}
	
	class AwardResultsMapper extends PropertyMap<SearchResults, AwardResults> {
		protected void configure() {
		    map().setTotalFound(source.getTotalResults());
		    map().setCount(source.getResults().size());
		  }
	}
	
	@RequestMapping("/v1Moo/awardSummary")
	public @ResponseBody AwardResults getAwardSummaryMoo(@RequestParam(value="updatedSince", required=false) Date updatedSince,
			@RequestParam(value="page", required=false, defaultValue="0") Integer page, @RequestParam(value="numberPerPage", required=false, defaultValue="50") Integer numberPerPage) {
		Moo moo = new Moo();
		return Translate.to(AwardResults.class).from(getAwardDao().retrievePopulatedAwardByCriteria(new HashMap<String, Object>(), updatedSince, page, numberPerPage));
	}

	public BusinessObjectService getBusinessObjectService() {
		return businessObjectService;
	}

	public void setBusinessObjectService(BusinessObjectService businessObjectService) {
		this.businessObjectService = businessObjectService;
	}

	public AwardDao getAwardDao() {
		return awardDao;
	}

	public void setAwardDao(AwardDao awardDao) {
		this.awardDao = awardDao;
	}
	
}

package com.fdp.nonoo.support.freemaker.directive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fdp.nonoo.util.FreemarkerUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("paginationDirective")
public class PaginationDirective extends BaseDirective
{
  private static final String PATTERN = "pattern";
  private static final String PAGE_NUMBER= "pageNumber";
  private static final String TOTAL_PAGES = "totalPages";
  private static final String SEGMENT_COUNT = "segmentCount";
  private static final String HAS_PREVIOUS = "hasPrevious";
  private static final String HAS_NEXT = "hasNext";
  private static final String IS_FIRST = "isFirst";
  private static final String IS_LAST = "isLast";
  private static final String PREVIOUS_PAGE_NUMBER = "previousPageNumber";
  private static final String NEXT_PAGE_NUMBER = "nextPageNumber";
  private static final String FIRST_PAGE_NUMBER = "firstPageNumber";
  private static final String LAST_PAGE_NUMBER = "lastPageNumber";
  private static final String SEGMENT = "segment";


@SuppressWarnings({ "unchecked", "rawtypes" })
public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException
  {
    String pattern = (String)FreemarkerUtils.getParameter(PATTERN, String.class, params);
    Integer pageNumber = (Integer)FreemarkerUtils.getParameter(PAGE_NUMBER, Integer.class, params);
    Integer totalPages = (Integer)FreemarkerUtils.getParameter(TOTAL_PAGES, Integer.class, params);
    Integer segmentCount = (Integer)FreemarkerUtils.getParameter(SEGMENT_COUNT, Integer.class, params);
    if ((pageNumber == null) || (pageNumber < 1))
    	pageNumber = 1;
    if ((totalPages == null) || (totalPages < 1))
    	totalPages = 1;
    if ((segmentCount == null) || (segmentCount < 1))
    	   segmentCount = 5;
    boolean bool1 = pageNumber > 1;
    boolean bool2 = pageNumber < totalPages;
    boolean bool3 = pageNumber == 1;
    boolean bool4 = pageNumber.equals(totalPages);
    int i = pageNumber - 1;
    int j = pageNumber + 1;
    int k = 1;
    int l = totalPages;
    int i1 = pageNumber - (int)Math.floor((segmentCount - 1) / 2.0D);
    int i2 = pageNumber + (int)Math.ceil((segmentCount - 1) / 2.0D);
    if (i1 < 1)
      i1 = 1;
    if (i2 > totalPages)
      i2 = totalPages;
    List<Integer> segmentList = new ArrayList<Integer>();
    for (int i3 = i1; i3 <= i2; ++i3)
    	segmentList.add(i3);
    Map<String,Object> map = new HashMap<String,Object>();
    map.put(PATTERN, pattern);
    map.put(PAGE_NUMBER, pageNumber);
    map.put(TOTAL_PAGES, totalPages);
    map.put(SEGMENT_COUNT, segmentCount);
    map.put(HAS_PREVIOUS, (bool1));
    map.put(HAS_NEXT, (bool2));
    map.put(IS_FIRST, (bool3));
    map.put(IS_LAST, (bool4));
    map.put(PREVIOUS_PAGE_NUMBER, i);
    map.put(NEXT_PAGE_NUMBER, j);
    map.put(FIRST_PAGE_NUMBER, k);
    map.put(LAST_PAGE_NUMBER, l);
    map.put(SEGMENT, segmentList);
    setVariable(map, env, body);
  }
}
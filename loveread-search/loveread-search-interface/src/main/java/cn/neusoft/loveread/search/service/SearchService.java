package cn.neusoft.loveread.search.service;

import cn.neusoft.loveread.common.pojo.SearchResult;

public interface SearchService {
    SearchResult search(String keyword, int page, int rows) throws Exception;
}

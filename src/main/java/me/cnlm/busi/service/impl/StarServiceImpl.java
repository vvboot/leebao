package me.cnlm.busi.service.impl;

import com.alibaba.fastjson.JSONArray;
import me.cnlm.busi.dao.StarDao;
import me.cnlm.busi.entity.Star;
import me.cnlm.busi.service.StarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by LONG on 2017/5/27.
 */
@SuppressWarnings("ALL")
@Service
public class StarServiceImpl implements StarService {
    private static final Logger logger = LoggerFactory.getLogger(StarServiceImpl.class);

    @Autowired
    private StarDao starDao;

    @Override
    public JSONArray dropdown() {
        List<Star> list = starDao.dropdown();
        JSONArray array = JSONArray.parseArray(JSONArray.toJSONString(list));
        logger.info("星级下拉列表:{}", array.toJSONString());
        return array;
    }
}

package com.vvboot.end.busi.service.impl;

import com.vvboot.end.busi.dao.ToutiaoMybatisDao;
import com.vvboot.end.busi.dao.article.ArticleCommentMybatisDao;
import com.vvboot.end.busi.dao.article.ArticleCommentReplyMybatisDao;
import com.vvboot.end.busi.dao.article.ArticleMybatisDao;
import com.vvboot.end.busi.dao.common.ApprovalMybatisDao;
import com.vvboot.end.busi.dao.common.OpposeMybatisDao;
import com.vvboot.end.busi.dto.common.OpposeDto;
import com.vvboot.end.busi.entity.common.Oppose;
import com.vvboot.end.busi.service.OpposeService;
import com.vvboot.end.core.exception.CoreException;
import com.vvboot.end.utils.ValidatorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.mapper.BeanMapper;

import javax.validation.Validator;
import java.util.Date;

/**
 * Created by xuan.touch6@qq.com on 2017/4/14.
 */
@SuppressWarnings("ALL")
@Service
public class OpposeServiceImpl implements OpposeService {
    private static final Logger logger = LoggerFactory.getLogger(OpposeServiceImpl.class);

    @Autowired
    private ApprovalMybatisDao approvalMybatisDao;
    @Autowired
    private OpposeMybatisDao opposeMybatisDao;
    @Autowired
    private ArticleMybatisDao articleMybatisDao;
    @Autowired
    private ArticleCommentMybatisDao articleCommentMybatisDao;
    @Autowired
    private ArticleCommentReplyMybatisDao articleCommentReplyMybatisDao;
    @Autowired
    private ToutiaoMybatisDao toutiaoMybatisDao;
    @Autowired
    private Validator validator;


    @Override
    @Transactional
    public int makeOppose(OpposeDto opposeDto) {
        String error = ValidatorUtils.validate(validator, opposeDto);
        if (error != null) {
            throw new CoreException(error);
        }

        int opposeAmount = 0;
        //反对
        Oppose oppose = BeanMapper.map(opposeDto, Oppose.class);
        //插入点赞
        Date time = new Date();
        oppose.setCreateTime(time);
        oppose.setUpdateTime(time);
        int inserted = opposeMybatisDao.addOppose(oppose);
        if (inserted == 1) {
            //插入返回1对象点赞数增加
            switch (oppose.getTargetObject()) {
                case ARTICLE:
                    articleMybatisDao.increaseOpposeAmount(oppose.getObjectId());
                    opposeAmount = articleMybatisDao.findOpposeAmountById(oppose.getObjectId());
                    break;
                case ARTICLE_COMMENT:
                    articleCommentMybatisDao.increaseOpposeAmount(oppose.getObjectId());
                    opposeAmount = articleCommentMybatisDao.findOpposeAmountById(oppose.getObjectId());
                    break;
                case ARTICLE_COMMENT_REPLY:
                    articleCommentReplyMybatisDao.increaseOpposeAmount(oppose.getObjectId());
                    opposeAmount = articleCommentReplyMybatisDao.findOpposeAmountById(oppose.getObjectId());
                    break;
                case NEWS:
                    toutiaoMybatisDao.increaseOpposeAmount(oppose.getObjectId());
                    opposeAmount = toutiaoMybatisDao.findOpposeAmountById(oppose.getObjectId());
                    break;
            }
            return opposeAmount;
        } else {
            throw new CoreException("重复操作");
        }
    }
}
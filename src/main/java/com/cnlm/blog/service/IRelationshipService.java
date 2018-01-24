package com.cnlm.blog.service;

import com.cnlm.blog.modal.vo.RelationshipVoKey;

import java.util.List;

/**
 * @author xiyuxuan
 * @date 2017/3/18
 */
public interface IRelationshipService {
    /**
     * 按住键删除
     *
     * @param cid
     * @param mid
     */
    void deleteById(Integer cid, Integer mid);

    /**
     * 按主键统计条数
     *
     * @param cid
     * @param mid
     * @return 条数
     */
    Long countById(Integer cid, Integer mid);


    /**
     * 保存對象
     *
     * @param relationshipVoKey
     */
    void insertVo(RelationshipVoKey relationshipVoKey);

    /**
     * 根据id搜索
     *
     * @param cid
     * @param mid
     * @return
     */
    List<RelationshipVoKey> getRelationshipById(Integer cid, Integer mid);
}

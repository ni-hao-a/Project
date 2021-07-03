package com.pc.service.service;

import com.pc.service.mapper.DtsGoodsAttributeMapper;
import com.pc.service.domain.DtsGoodsAttribute;
import com.pc.service.domain.DtsGoodsAttributeExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DtsGoodsAttributeService {
    @Resource
    private DtsGoodsAttributeMapper goodsAttributeMapper;

    public List<DtsGoodsAttribute> queryByGid(Integer goodsId) {
        DtsGoodsAttributeExample example = new DtsGoodsAttributeExample();
        example.or().andGoodsIdEqualTo(goodsId).andDeletedEqualTo(false);
        return goodsAttributeMapper.selectByExample(example);
    }

    public void add(DtsGoodsAttribute goodsAttribute) {
        goodsAttribute.setAddTime(LocalDateTime.now());
        goodsAttribute.setUpdateTime(LocalDateTime.now());
        goodsAttributeMapper.insertSelective(goodsAttribute);
    }

    public DtsGoodsAttribute findById(Integer id) {
        return goodsAttributeMapper.selectByPrimaryKey(id);
    }

    public void deleteByGid(Integer gid) {
        DtsGoodsAttributeExample example = new DtsGoodsAttributeExample();
        example.or().andGoodsIdEqualTo(gid);
        goodsAttributeMapper.logicalDeleteByExample(example);
    }
}

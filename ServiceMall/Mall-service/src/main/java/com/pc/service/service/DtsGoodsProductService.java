package com.pc.service.service;

import com.pc.service.mapper.DtsGoodsProductMapper;
import com.pc.service.mapper.ex.GoodsProductMapper;
import com.pc.service.domain.DtsGoodsProduct;
import com.pc.service.domain.DtsGoodsProductExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DtsGoodsProductService {
    @Resource
    private DtsGoodsProductMapper dtsGoodsProductMapper;
    @Resource
    private GoodsProductMapper goodsProductMapper;

    public List<DtsGoodsProduct> queryByGid(Integer gid) {
        DtsGoodsProductExample example = new DtsGoodsProductExample();
        example.or().andGoodsIdEqualTo(gid).andDeletedEqualTo(false);
        return dtsGoodsProductMapper.selectByExample(example);
    }

    public DtsGoodsProduct findById(Integer id) {
        return dtsGoodsProductMapper.selectByPrimaryKey(id);
    }

    public void deleteById(Integer id) {
        dtsGoodsProductMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(DtsGoodsProduct goodsProduct) {
        goodsProduct.setAddTime(LocalDateTime.now());
        goodsProduct.setUpdateTime(LocalDateTime.now());
        dtsGoodsProductMapper.insertSelective(goodsProduct);
    }

    public int count() {
        DtsGoodsProductExample example = new DtsGoodsProductExample();
        example.or().andDeletedEqualTo(false);
        return (int) dtsGoodsProductMapper.countByExample(example);
    }

    public void deleteByGid(Integer gid) {
        DtsGoodsProductExample example = new DtsGoodsProductExample();
        example.or().andGoodsIdEqualTo(gid);
        dtsGoodsProductMapper.logicalDeleteByExample(example);
    }

    public int addStock(Integer id, Short num) {
        return goodsProductMapper.addStock(id, num);
    }

    public int addBrowse(Integer id, Short num) {
        return goodsProductMapper.addBrowse(id, num);// 新增商品流量量
    }

    public int reduceStock(Integer id, Integer goodsId, Short num) {
        goodsProductMapper.addSales(goodsId, num);// 每次需将商品的销售量加下
        return goodsProductMapper.reduceStock(id, num);
    }
}
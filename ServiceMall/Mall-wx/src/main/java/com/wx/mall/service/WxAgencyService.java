package com.wx.mall.service;

import com.pc.service.domain.DtsBrand;
import com.pc.service.domain.DtsGoods;
import com.pc.service.service.DtsAgencyService;
import com.pc.service.service.DtsBrandService;
import com.pc.service.service.DtsGoodsService;
import com.wx.core.qcode.QCodeService;
import com.wx.core.type.AgencyShareTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WxAgencyService {

    @Autowired
    private DtsAgencyService agencyService;

    @Autowired
    private DtsGoodsService goodsService;

    @Autowired
    private DtsBrandService brandService;

    @Autowired
    private QCodeService qCodeService;

    /**
     * 生成代理用户的分享海报
     *
     * @param userId
     * @param type
     * @param shareObjId
     * @return
     */
    public String createAgencyShareUrl(Integer userId, Integer type, Integer shareObjId) {
        String shareUrl = null;
        try {
            if (type.intValue() == AgencyShareTypeEnum.GOODS_SHARE.getType().intValue()) {// 商品
                // 商品信息
                DtsGoods goods = goodsService.findById(shareObjId);
                // 将生成的分享图片到存储空间
                shareUrl = qCodeService.createGoodShareImage(userId, goods.getId().toString(), goods.getPicUrl(),
                        goods.getName(), goods.getCounterPrice(), goods.getRetailPrice());

            } else if (type.intValue() == AgencyShareTypeEnum.BRAND_SHARE.getType().intValue()) {// 入驻店铺
                // 生成店铺的分享URL
                DtsBrand brand = brandService.findById(shareObjId);
                String defaultCategory = brandService.getBrandCategory(brand.getDefaultCategoryId());
                shareUrl = qCodeService.createBrandImage(userId, brand.getId(), brand.getPicUrl(), brand.getName(),
                        defaultCategory);

            } else {// 其他暂时不考虑
            }
        } catch (Exception e) {
            log.error("生成分享海报URL出错：{}", e.getMessage());
            e.printStackTrace();
        }

        agencyService.saveDtsAgencyShare(userId, type, shareObjId, shareUrl);// 代理用户的需要保存记录
        return shareUrl;
    }

    /**
     * 非代理用户的分享海报获取
     *
     * @param type
     * @param shareObjId
     * @return
     */
    public String getShareObjUrl(Integer type, Integer shareObjId) {
        String shareUrl = null;
        if (type.intValue() == AgencyShareTypeEnum.GOODS_SHARE.getType().intValue()) {// 商品
            // 商品信息
            DtsGoods goods = goodsService.findById(shareObjId);
            if (goods != null) {
                shareUrl = goods.getShareUrl();
            }
        } else if (type.intValue() == AgencyShareTypeEnum.BRAND_SHARE.getType().intValue()) {// 入驻店铺
            DtsBrand brand = brandService.findById(shareObjId);
            if (brand != null) {
                shareUrl = brand.getShareUrl();
            }
        } else {// 其他暂时不考虑
        }
        log.info("获取 {} 的分享海报 url {}", AgencyShareTypeEnum.getInstance(type).getDesc(), shareUrl);
        return shareUrl;
    }

    /**
     * 非代理用户的海报分享创建
     *
     * @param type
     * @param shareObjId
     * @return
     */
    public String createShareUrl(Integer type, Integer shareObjId) {
        String shareUrl = null;
        try {
            if (type.intValue() == AgencyShareTypeEnum.GOODS_SHARE.getType().intValue()) {// 商品
                // 商品信息
                DtsGoods goods = goodsService.findById(shareObjId);
                // 将生成的分享图片到存储空间
                shareUrl = qCodeService.createGoodShareImage(null, goods.getId().toString(), goods.getPicUrl(),
                        goods.getName(), goods.getCounterPrice(), goods.getRetailPrice());

                // 更新数据
                goods.setShareUrl(shareUrl);
                goodsService.updateById(goods);
            } else if (type.intValue() == AgencyShareTypeEnum.BRAND_SHARE.getType().intValue()) {// 入驻店铺

                // 生成店铺的分享URL
                DtsBrand brand = brandService.findById(shareObjId);
                String defaultCategory = brandService.getBrandCategory(brand.getDefaultCategoryId());
                shareUrl = qCodeService.createBrandImage(null, brand.getId(), brand.getPicUrl(), brand.getName(),
                        defaultCategory);

                // 更新数据
                brand.setShareUrl(shareUrl);
                brandService.updateById(brand);

            } else {// 其他暂时不考虑
            }
        } catch (Exception e) {
            log.error("生成分享海报URL出错：{}", e.getMessage());
            e.printStackTrace();
        }
        log.info("生成 {} 的分享海报 url {}", AgencyShareTypeEnum.getInstance(type).getDesc(), shareUrl);
        return shareUrl;
    }

}

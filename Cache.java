package org.leafframework.mvc.service;

import org.leafframework.data.dao.mapper.GlobalDAO;
import org.leafframework.util.CommonUtil;
import org.leafframework.util.PojoUtil;
import org.leafframework.util.RETURN;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

/**
 * CopyRright (c)2014-2016 Haerbin Hearglobal Co.,Ltd
 * Project: global
 * Comments:
 * Author:zhxin
 * Create Date:2016/7/15
 * Modified By:
 * Modified Date:
 * Modified Reason:
 */
@Service
public class Cache extends AbstractBusiness{

    public  String cacheChannel;
    @Value("${leaf.global.Cache.chanel}")
    public  void setCacheChannel(String cacheChannel) {
        this.cacheChannel = cacheChannel;
    }


    @Override
    public RETURN execute() throws Exception {
        return null;
    }

    @Override
    public RETURN init() throws Exception {
        return null;
    }

    @Override
    public RETURN query() throws Exception {
        return null;
    }

    @PostConstruct
    public void startupInit() throws Exception {
        logger.debug("startupInit=>"+cacheChannel);
        GlobalDAO globalDAO = (GlobalDAO) this.getDaoFactory().get("globalDAO");

        ////////////////////////缓存区域内存参数开始////////////////////////////
        logger.debug("cacheing global area params");
        List<HashMap> listAreaLv1 =  globalDAO.getGlobalArea(0);
        for(int i = 0; i < listAreaLv1.size(); i++)
        {
            listAreaLv1.get(i);
            int provinceId=(Integer) listAreaLv1.get(i).get("id");
            List<HashMap> listAreaLev2 =  globalDAO.getGlobalArea(provinceId);
            for(int j=0; j<listAreaLev2.size();j++){
                int cityId=(Integer) listAreaLev2.get(j).get("id");
                List<HashMap> listAreaLv3 =  globalDAO.getGlobalArea(cityId);
                listAreaLev2.get(j).put("county",listAreaLv3);
            }
            listAreaLv1.get(i).put("city",listAreaLev2);
        }
        logger.debug(PojoUtil.toJson(listAreaLv1));
        CommonUtil.setCacheContext(listAreaLv1,"area");
        ////////////////////////缓存区域内存参数结束////////////////////////////

        logger.debug(CommonUtil.getCacheContext("area"));
    }

}

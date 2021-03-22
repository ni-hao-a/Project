package com.pc.dictionary.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pc.dictionary.mapper.EduDictionaryMapper;
import com.pc.dictionary.model.CommPage;
import com.pc.dictionary.model.EduDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 学历字典
 */
@RestController
@RequestMapping(value = "/dictionary/v1/rlzy", produces = "application/json")
public class EducationController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private EduDictionaryMapper dictionaryMapper;

    @RequestMapping(value = "/getEduDictionary", method = RequestMethod.POST)
    public PageInfo<EduDictionary> getEduDictionary(@Valid @RequestBody CommPage req) {
        String token = request.getHeader("token");
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<EduDictionary> list = dictionaryMapper.getEduDictionary();
        return new PageInfo<>(list);
    }
}

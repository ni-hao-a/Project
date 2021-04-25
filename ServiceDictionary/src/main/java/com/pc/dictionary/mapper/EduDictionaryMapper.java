package com.pc.dictionary.mapper;

import com.pc.dictionary.model.EduDictionary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EduDictionaryMapper {
    List<EduDictionary> getEduDictionary();
}

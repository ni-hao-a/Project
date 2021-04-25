package com.pc.dictionary.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class CommPage implements Serializable {
    private int pageNum;
    private int pageSize;
}

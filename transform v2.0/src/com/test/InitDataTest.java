package com.test;

import com.main.InitData;
import com.main.cmdable.Sendable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InitDataTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void findCharsetInList() {
        System.out.println(InitData.findCharsetInList("utf8").getCode());
        System.out.println(InitData.findCharsetInList("gbk").getCode());
        System.out.println(InitData.findCharsetInList("utf16").getCode());
    }

    @Test
    public void getClientMapDealClass() {
        System.out.println((InitData.getClientMapDealClass("sendMessage").getName()));
        System.out.println((InitData.getClientMapDealClass("sendfile").getName()));
        System.out.println((InitData.getClientMapDealClass("downloadFiLe").getName()));
    }
}
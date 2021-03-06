package edu.nju.data.daoImpl.http;

import edu.nju.RuanHuApplication;
import edu.nju.data.dao.http.Tss_httpDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

/**
 * Created by Dora on 2016/7/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RuanHuApplication.class)
@WebAppConfiguration
public class Tss_httpDAOImplTest {
    @Test
    public void getDocumentById() throws Exception {
        System.out.println(dao.getDocumentById(Long.parseLong("16072000000")));
    }


    @Test
    public void searchDocumentsByKeyword() throws Exception {
        System.out.println(dao.searchDocumentsByKeyword("ppt",1,1));
    }

    @Test
    public void sendNotification() throws Exception {

    }


    @Autowired
    Tss_httpDAO dao;
}
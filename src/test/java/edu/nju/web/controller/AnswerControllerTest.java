package edu.nju.web.controller;

import edu.nju.RuanHuApplication;
import edu.nju.data.dao.UserDAO;
import edu.nju.data.entity.Answer;
import edu.nju.data.entity.User;
import edu.nju.web.controller.json.AnswerJsonController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by cuihao on 2016/7/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)		//表示继承了SpringJUnit4ClassRunner类
@SpringApplicationConfiguration(classes = RuanHuApplication.class) // 指定我们SpringBoot工程的Application启动类
@WebAppConfiguration
@Rollback
@Transactional
public class AnswerControllerTest {

    @Autowired
    private AnswerJsonController answerJsonController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserDAO userDAO;

    private MockMvc mockMvc;

    private Map<String, Object> sessionMap = new HashMap<>();

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        User user = userDAO.getUserByName("ch");
        sessionMap.put("user",user);
    }

    @Test
    public void tempSave() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/answer/tempSave").sessionAttrs(sessionMap)
                .param("questionId","1").param("markedText","###head"))
                .andExpect(status().isOk());
        HashMap<String,Answer> map = new HashMap<>();
        sessionMap.put("tempAnswer",map);
        mockMvc.perform(MockMvcRequestBuilders.get("/answer/tempSave").sessionAttrs(sessionMap)
                .param("questionId","1").param("markedText","###head2"))
                .andExpect(status().isOk());
        map.put("1",new Answer());
        sessionMap.put("tempAnswer",map);
        mockMvc.perform(MockMvcRequestBuilders.get("/answer/tempSave").sessionAttrs(sessionMap)
                .param("questionId","1").param("markedText","###head3"))
                .andExpect(status().isOk());
    }

    @Test
    public void getTempAnswer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/answer/getTempAnswer").sessionAttrs(sessionMap).param("questionId","1"))
                .andExpect(status().isOk());
        HashMap<String,Answer> map = new HashMap<>();
        sessionMap.put("tempAnswer",map);
        mockMvc.perform(MockMvcRequestBuilders.get("/answer/getTempAnswer").sessionAttrs(sessionMap).param("questionId","1"))
                .andExpect(status().isOk());
        map.put("1",new Answer());
        sessionMap.put("tempAnswer",map);
        mockMvc.perform(MockMvcRequestBuilders.get("/answer/getTempAnswer").sessionAttrs(sessionMap).param("questionId","1"))
                .andExpect(status().isOk());
    }

    @Test
    public void newAnswer() throws Exception {
    }

    @Test
    public void saveAnswer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/answer/saveAnswer").sessionAttrs(sessionMap)
        .param("questionId","1").param("markedText","###head\nhah")).andExpect(status().isOk());
    }

    @Test
    public void markAsSolution() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/answer/markAsSolution").sessionAttrs(sessionMap)
        .param("answerId","1").param("questionId","1")).andExpect(status().isOk());
    }

}
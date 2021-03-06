package edu.nju.data.daoImpl;

import edu.nju.RuanHuApplication;
import edu.nju.data.dao.QuestionDAO;
import edu.nju.data.entity.Question;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by ss14 on 2016/7/12.
 */
@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringApplicationConfiguration(classes = RuanHuApplication.class) // 指定我们SpringBoot工程的Application启动类
@WebAppConfiguration
@Rollback
public class QuestionDAOImplTest {

    @Test
    public void getRelatedWikiItems() throws Exception {

    }

    @Test
    public void getRelatedDocuments() throws Exception {

    }


    @Autowired
    QuestionDAO questionDAO;
    @PersistenceContext
    EntityManager em;


    @Test
    public void save() throws Exception {
        for(int i=1 ; i<35 ;i++){
            Question question = new Question();
            question.setTitle("q"+i);
            question.setContent("c"+i);
            question.setAuthorId(new Long (3));
            questionDAO.save(question);
        }

    }

    @Test
    public void save_id() throws Exception {
        Question question = new Question();
        question.setAuthorId(new Long(1));
       // question.getAuthor().setId(new Long (5));
        long id = questionDAO.save_id(question);
        if(id==0){
            fail();
        }else{
            System.out.println("Last inserted question id : "+ id);
        }

    }

    @Test
    public void save_question() throws Exception {
        Question question = new Question();
        question.setAuthorId(new Long(2));
        Question newQuestion= questionDAO.save_question(question);
        if(newQuestion.getId()==0){
            fail();
        }else{
            System.out.println("Last inserted question id : "+ newQuestion.getId());
        }
    }


    @Test
    public void createQuestion() throws Exception {
        Question question = new Question();
        question.setAuthorId(new Long(2));
        question.setTitle("如何练习外点归刹？求教");
        question.setContent("如题");
        List wikiIDs = new ArrayList();
        List docuIDs = new ArrayList();
        for(long i=1;i<=3;i++){
            wikiIDs.add(i);
            docuIDs.add(2*i-1);
        }

        questionDAO.createQuestion(question , wikiIDs , docuIDs );


    }

    @Test
    public void deleteByQuestionID() throws Exception {
        long id =2;
        Question temp = questionDAO.getQuestionByID(id);
        System.out.println("get questionid: "+temp.getId());
        questionDAO.deleteByQuestionID(id);
        if(questionDAO.getQuestionByID(id)!=null){
            fail("null");
        }
        temp.setId(null);
        questionDAO.save(temp);
    }

    @Test
    public void deleteByAuthorID() throws Exception {
        Long authorid =new Long (1);
        int right=2;
        int result=questionDAO.deleteByAuthorID(authorid);
        if(result!=right){
            fail();
        }

    }

    @Test
    public void update() throws Exception {
        Question question = new Question();
        question.setAuthorId(new Long(2));
        question.setAnswerCount(new Integer(1));
        question.setId(new Long(3));
        questionDAO.update(question);

    }



    @Test
    public void getQuestionByID() throws Exception {
        long id = 1;
        Question question = questionDAO.getQuestionByID(id);
        String title = "q1";
        String content = "c1";
        String name = "ch";
        if(question==null){
            fail();
        }else if (!title.equals(question.getTitle()) ||
                     !content.equals(question.getContent())||!question.getAuthor().getUserName().equals(name)){
            fail();
        }


    }


    @Test
    public void getPaginatedQuestions() throws Exception {

//        int pageNum=1;
//        int default_size=10;
//        List<Question> result = questionDAO.getPaginatedQuestions(pageNum);
//        if(result==null){
//            fail();
//        }else if (result.size()!=default_size){
//            fail();
//        }else if (result.get(0).getId() != 1) {
//                fail();
//
//        }

    }

    @Test
    public void getPaginatedQuestions1() throws Exception {
//        int pageNum=1;
//        int pageSize=15;
//        List<Question> result = questionDAO.getPaginatedQuestions(pageNum,pageSize);
//        if(result==null){
//            fail();
//        }else if (result.size()!=pageSize){
//            fail();
//        }
    }

    /**
     * 按参数排序,默认降序
  //   * @param pageNum
  //    * @param para   OrderByPara: createdAt lastUpdatedAt ....
     * @return
     */
    @Test
    public void getPaginatedQuestions2() throws Exception {
//        int pageNum=1;
//
//        List<Question> result = questionDAO.getPaginatedQuestions(pageNum, OrderByPara.answerCount);
//        if(result==null){
//            fail();
//        }else if (result.size()!=10){
//            fail();
//        }else{
//            for(Question question : result){
//                System.out.println("QuestionID : "+question.getId() +"  "+question.getAnswerCount());
//            }
//        }
    }

    @Test
    public void getPaginatedQuestions3() throws Exception {
//        int pageNum=1;
//        int pageSize=15;
//        List<Question> result = questionDAO.getPaginatedQuestions(pageNum,pageSize, OrderByPara.createdAt);
//        if(result==null){
//            fail();
//        }else if (result.size()!=pageSize){
//            fail();
//        }else{
//            for(Question question : result){
//                System.out.println("QuestionID : "+question.getId() +"  "+question.getCreatedAt());
//            }
//        }
    }

    @Test
    public void getPaginatedQuestions4() throws Exception {
//        int pageNum=1;
//
//        List<Question> result = questionDAO.getPaginatedQuestions
//                    (pageNum, OrderByPara.answerCount, OrderByMethod.ASC);
//        if(result==null){
//            fail();
//        }else if (result.size()!=10){
//            fail();
//        }else{
//            for(Question question : result){
//                System.out.println("QuestionID : "+question.getId() +"  "+question.getAnswerCount());
//            }
//        }
    }

    @Test
    public void getPaginatedQuestions5() throws Exception {
//        int pageNum=1;
//        int pageSize=15;
//        List<Question> result = questionDAO.getPaginatedQuestions
//                (pageNum,pageSize, OrderByPara.createdAt,OrderByMethod.ASC);
//        if(result==null){
//            fail();
//        }else if (result.size()!=pageSize){
//            fail();
//        }else{
//            for(Question question : result){
//                System.out.println("QuestionID : "+question.getId() +"  "+question.getCreatedAt());
//            }
//        }
    }

    @Test
    public void getQuestionByUsername() throws Exception {

//        long userID = 1 ;
//        String username = "ch";
//        int resultSize =5;
//        String q1 = "q1";
//        String q2 = "q2";
//        List<Question> questionList = questionDAO .getQuestionByUsername(username);
//        if(questionList==null){
//            fail("null");
//        }else if ( questionList.size()!=resultSize){
//            fail("size");
//      //  }else if (questionList.get(0).getAuthorId()!=userID){
//       //     fail("userID");
//        }else{
//            for (Question question : questionList){
//
//                System.out.println("questionID: "+question.getId());
//         //       System.out.println("userID: "+question.getAuthorId());
//
//            }
//        }


    }

    @Test
    public void getQuestionCountByUsername() throws Exception {
        String username = "ss14";
        long right=3;
        long result=questionDAO.getQuestionCountByUsername(username);
        if(right!=result){
            fail();
        }
    }

    @Test
    public void getQuestionByUserID() throws Exception {
//        long userID = 1 ;
//        int resultSize =5;
//        String q1 = "q1";
//        String q2 = "q2";
//        List<Question> questionList = questionDAO .getQuestionByUserID(userID);
//        if(questionList==null){
//            fail("null");
//        }else if ( questionList.size()!=resultSize){
//            fail("size");
//        }else{
//            for (Question question : questionList){
//
//                System.out.println("questionID: "+question.getId());
//       //         System.out.println("userID: "+question.getAuthorId());
//
//            }
//        }
    }

    @Test
    public void getQuestionCountByUserID() throws Exception {
        long userid=2;
        long right=3;
        long result=questionDAO.getQuestionCountByUserID(userid);
        if(right!=result){
            fail();
        }
    }

}
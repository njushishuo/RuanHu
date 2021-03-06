package edu.nju.data.daoImpl;

import edu.nju.data.dao.*;
import edu.nju.data.dao.http.Tss_httpDAO;
import edu.nju.data.dao.http.Wiki_httpDAO;
import edu.nju.data.entity.Answer;
import edu.nju.data.entity.api.Document;
import edu.nju.data.entity.api.WikiItem;
import edu.nju.data.util.CommonParas;
import edu.nju.data.util.HQL_Helper.Enums.FromPara;
import edu.nju.data.util.HQL_Helper.Enums.OrderByMethod;
import edu.nju.data.util.HQL_Helper.Enums.OrderByPara;
import edu.nju.data.util.HQL_Helper.Enums.WherePara;
import edu.nju.data.util.HQL_Helper.Impl.QueryHqlMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ss14 on 2016/7/13.
 */
@Repository
@Transactional
public class AnswerDAOImpl implements AnswerDAO {
    @Autowired
    BaseDAO baseDAO;
    @Autowired
    OrderedPageDAO pageDAO;
    @Autowired
    QueryHqlMaker hqlMaker;
    @Autowired
    WikiDAO wikiDAO;
    @Autowired
    DocumentDAO documentDAO;
    @Autowired
    Wiki_httpDAO wiki_httpDAO;
    @Autowired
    Tss_httpDAO docu_httpDAO;

    @PersistenceContext
    EntityManager em;


    private static String tableName = "Answer";

    @Override
    public void save(Answer answer) {
        answer.setId(null);
        baseDAO.insert(answer);
    }

    @Override
    public long save_id(Answer answer) {
        answer.setId(null);
        em.persist(answer);
        em.flush();
        return answer.getId();
    }

    @Override
    public Answer save_answer(Answer answer) {
        answer.setId(null);
        em.persist(answer);
        em.flush();
        return answer;
    }

    @Override
    public Answer createAnswer(Answer answer, List wikiIDs, List docuIDs) {
        Answer result = save_answer(answer);
        wikiDAO.insertAnswer(result.getId() , wikiIDs);
        documentDAO.insertAnswer(result.getId() , docuIDs);
        return result;
    }

    @Override
    public void deleteByAnswerID(long AnswerID) {
        baseDAO.delete(Answer.class,AnswerID);
    }

    @Override
    public int deleteByQuestionID(long questionID) {
        Query query = em.createQuery("delete from Answer as a where a.question.id = ?1");
        query.setParameter(1,questionID);
        int size=query.executeUpdate();
        return size;
    }

    @Override
    public int deleteByUserID(long userID) {
        Query query = em.createQuery("delete from Answer as a where a.author.id = ?1");
        query.setParameter(1,userID);
        int size=query.executeUpdate();
        return size;
    }

    @Override
    public int deleteByQuestion_UserID(long questionID, long userID) {
        Query query = em.createQuery("delete from Answer as a where a.author.id = ?2 and a.question.id = ?1");
        query.setParameter(1,questionID);
        query.setParameter(2,userID);
        int size=query.executeUpdate();
        return size;
    }

    @Override
    public void update(Answer answer) {
        baseDAO.update(answer);

    }


    /**
     * 私有方法，将一个Answer无脑设置为Solution
     * @param AnswerID
     */
    private void setSolution(long AnswerID){
        Query query = em.createQuery("update Answer as a set a.solution = 1  where a.id = ?1");
        query.setParameter(1,AnswerID);
        query.executeUpdate();

    }


    @Override
    public void setSolution(long QuestionID ,long AnswerID) {

        Query query = em.createQuery("from Answer a where a.solution = 1 and a.question.id = ?1");
        query.setParameter(1,QuestionID);
        List<Answer> resultList = query.getResultList();
        /**
         * 如果这个问题没有一个答案被选为solution
         */
        if(resultList==null){

            System.err.println("Set solution!!!");
            setSolution(AnswerID);

        }else{

            if(resultList.size()==0){
                System.err.println("???");
                System.err.println("Set solution!!!");
                setSolution(AnswerID);
            }
            /**
             * 如果已经有其他回答被认可
             */
            if(resultList.size()>0){

                System.err.println("Cancle others !!!");
                cancelSolution(resultList.get(0).getId());
                setSolution(AnswerID);

            }

        }
    }


    @Override
    public void cancelSolution(long answerID) {
        Query query = em.createQuery("update Answer as a set a.solution = 0  where a.id = ?1");
        query.setParameter(1,answerID);
        query.executeUpdate();
    }



    @Override
    public Answer getAnswerByID(long answerID) {
        Query query = em.createQuery("from Answer a where a.id =?1");
        query.setParameter(1,answerID);
        List<Answer> result = query.getResultList();
        if(result == null){
            return null;
        }else if(result.size()==0){
            return null;
        }else{
            return result.get(0);
        }
    }

    @Override
    public List<Answer> getAnswerBy(WherePara byPara, Object arg) {
        String hql =hqlMaker.getHQLby(FromPara.Answer ,byPara);
        Query query = em.createQuery(hql);
        query.setParameter(1,arg);
        List<Answer> result = query.getResultList();
        return result;
    }

    @Override
    public List<Answer> getPagedAnswers(int pageNum) {
        String hql = hqlMaker.getHQLby(FromPara.Answer, null);
        return (List<Answer>) pageDAO.execHQL(hql,pageNum, CommonParas.default_pageSize);

    }

    @Override
    public List<Answer> getPagedAnswers(int pageNum, int pageSize) {
        String hql = hqlMaker.getHQLby(FromPara.Answer, null);
        return (List<Answer>) pageDAO.execHQL(hql,pageNum, pageSize);
    }

    @Override
    public List<Answer> getOrderedPagedAnswers(int pageNum, OrderByPara orderByPara) {

        String hql = hqlMaker.getHQLby(FromPara.Answer, null ,orderByPara , OrderByMethod.DESC);
        return (List<Answer>) pageDAO.execHQL(hql,pageNum, CommonParas.default_pageSize);
    }

    @Override
    public List<Answer> getOrderedPagedAnswers
            (int pageNum, int pageSize, OrderByPara orderByPara, OrderByMethod orderByMethod) {

        String hql = hqlMaker.getHQLby(FromPara.Answer, null ,orderByPara , orderByMethod);

        return (List<Answer>) pageDAO.execHQL(hql,pageNum, pageSize);
    }

    @Override
    public List<Answer> getOrderedPagedAnswersBy(WherePara byPara, Object arg, int pageNum, OrderByPara orderByPara) {

        String hql = hqlMaker.getHQLby(FromPara.Answer, byPara ,orderByPara , OrderByMethod.DESC);
        return (List<Answer>) pageDAO.execHQL(hql,pageNum, CommonParas.default_pageSize , arg);
    }

    @Override
    public List<Answer> getOrderedPagedAnswersBy(WherePara byPara, Object arg, int pageNum, int pageSize, OrderByPara orderByPara, OrderByMethod orderByMethod) {

        String hql = hqlMaker.getHQLby(FromPara.Answer, byPara ,orderByPara , orderByMethod);
        return (List<Answer>) pageDAO.execHQL(hql,pageNum, pageSize ,arg);

    }


    @Override
    public long getAnswerCountByUserName(String username) {

        Query query =  em.createQuery("select count (a) from Answer as a where a.author.userName = ?1");
        query.setParameter(1,username);
        long count = (long) query.getSingleResult();
        return count;
    }

    @Override
    public long getAnswerCountByUserID(long ID) {
        Query query =  em.createQuery("select count (a) from Answer as a where a.author.id = ?1");
        query.setParameter(1,ID);
        long count = (long) query.getSingleResult();
        return count;
    }

    @Override
    public List<WikiItem> getRelatedWikiItems(long answerID) {
        List<WikiItem> result = new ArrayList<>();
        List ids = new ArrayList();

        Query query = em.createQuery("select aw.wikiId from AnswerWiki aw where aw.answerId = ?1 ");
        query.setParameter(1,answerID);
        ids.addAll(query.getResultList());

        for(int i=0;i<ids.size();i++){

            try {
                result.add(wiki_httpDAO.getWikiById((Long) ids.get(i)));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return result;
    }

    @Override
    public List<Document> getRelatedDocuments(long answerID) {
        List<Document> result = new ArrayList<>();
        List ids = new ArrayList();

        Query query = em.createQuery
                ("select ad.documentId from AnswerDocument  ad where ad.answerId = ?1 ");
        query.setParameter(1,answerID);
        ids.addAll(query.getResultList());

        for(int i=0 ; i<ids.size() ;i++){
            try {
                result.add(docu_httpDAO.getDocumentById((Long) ids.get(i)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }


}

package edu.nju.logic.impl;

import edu.nju.data.dao.AnswerDAO;
import edu.nju.data.dao.QuestionDAO;
import edu.nju.data.dao.VoteDAO;
import edu.nju.data.entity.Answer;
import edu.nju.data.entity.Question;
import edu.nju.data.entity.Vote;
import edu.nju.data.util.VoteType;
import edu.nju.logic.service.QuestionService;
import edu.nju.logic.service.TimeService;
import edu.nju.logic.vo.AnswerVO;
import edu.nju.logic.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Dora on 2016/7/13.
 */
@Component
public class QuestionImpl implements QuestionService {

    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    AnswerDAO answerDAO;

    @Autowired
    VoteDAO voteDAO;

    @Autowired
    TimeService timeService;

    @Override
    public QuestionVO showQuestion(long id) {
        Question question = questionDAO.getQuestionByID(id);
        return timeService.transferQuestion(question);
    }

    @Override
    public QuestionVO saveQuestion(Question question) {
        return timeService.transferQuestion(questionDAO.save_question(question));
    }

    @Override
    public List<QuestionVO> getQuestions(int pageNum, int pageSize) {
        List<Question> questions = questionDAO.getPaginatedQuestions(pageNum, pageSize);
        List<QuestionVO> questionVOs = new ArrayList<>(questions.size());
        for (Question question : questions) {
            questionVOs.add(timeService.transferQuestion(question));
        }
        return questionVOs;
    }

    @Override
    public List<AnswerVO> getAnswers(long questionId, int pageNum, int pageSize) {
        List<Answer> answers =  answerDAO.getAnswerByQuestionID(questionId,pageNum,pageSize);
        List<AnswerVO> answerVOs = new ArrayList<>(answers.size());
        for (Answer answer: answers) {
            answerVOs.add(timeService.transferAnswer(answer));
        }
        return answerVOs;
    }

    @Override
    public void vote(String questionId, String userId, VoteType type) {
        Vote vote = new Vote();
        vote.setAuthorId(Long.valueOf(userId));
        vote.setCreatedAt(new Timestamp(new Date().getTime()));
        vote.setLastUpdatedAt(new Timestamp(new Date().getTime()));
        vote.setQuestionId(Long.valueOf(questionId));
        vote.setVoteType(type);
        voteDAO.vote(vote);
    }

    @Override
    public void unVote(String questionId, String userId, VoteType type) {
        Vote vote = new Vote();
        vote.setAuthorId(Long.valueOf(userId));
        vote.setCreatedAt(new Timestamp(new Date().getTime()));
        vote.setLastUpdatedAt(new Timestamp(new Date().getTime()));
        vote.setQuestionId(Long.valueOf(questionId));
        vote.setVoteType(type);
        voteDAO.cancel(vote);
    }

}

package edu.nju.logic.impl;

import edu.nju.data.dao.AnswerDAO;
import edu.nju.data.dao.MessageDAO;
import edu.nju.data.dao.QuestionDAO;
import edu.nju.data.dao.UserDAO;
import edu.nju.data.entity.Answer;
import edu.nju.data.entity.Message;
import edu.nju.data.entity.Question;
import edu.nju.data.entity.User;
import edu.nju.data.util.HQL_Helper.Enums.OrderByPara;
import edu.nju.data.util.HQL_Helper.Enums.WherePara;
import edu.nju.logic.service.TimeService;
import edu.nju.logic.service.TransferService;
import edu.nju.logic.service.UserProfileService;
import edu.nju.logic.vo.ActivityType;
import edu.nju.logic.vo.ActivityVO;
import edu.nju.logic.vo.AnswerVO;
import edu.nju.logic.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.PatternSyntaxException;


/**
 * Created by cuihao on 2016/7/12.
 */
@Component
public class UserProfileImpl implements UserProfileService {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private QuestionDAO questionDAO;
    @Autowired
    private AnswerDAO answerDAO;
    @Autowired
    private TransferService timeService;
    @Autowired
    private MessageDAO messageDAO;

    @Override
    public boolean editProfile(User user, String description, String location, String bitrhday) {
        user.setDescription(description);
        user.setLocation(location);
        user.setBirthDate(Date.valueOf(bitrhday));
        userDAO.update(user);
        return true;
    }

    @Override
    public boolean editBirthday(User user, String bitrhday) {
        User thisUser = userDAO.getUserByID(user.getId());
        try {
            String[] strs = bitrhday.split("/");
            bitrhday = strs[2]+"-"+strs[0]+"-"+strs[1];
            thisUser.setBirthDate(Date.valueOf(bitrhday));
            userDAO.update(thisUser);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean editDescription(User user, String description) {
        User thisUser = userDAO.getUserByID(user.getId());
        thisUser.setDescription(description);
        userDAO.update(thisUser);
        return true;
    }

    @Override
    public boolean editLocation(User user, String location) {
        User thisUser = userDAO.getUserByID(user.getId());
        thisUser.setLocation(location);
        userDAO.update(thisUser);
        return true;
    }

    @Override
    public User getUserByName(String name) {
        return userDAO.getUserByName(name);
    }

    @Override
    public long getQuestionCountByName(String name) {
        return questionDAO.getQuestionCountByUsername(name);
    }

    @Override
    public long getAnswerCountByName(String name) {
        return answerDAO.getAnswerCountByUserName(name);
    }

    @Override
    public List<QuestionVO> getQuestionByName(String userName, long userId) {
        List<Question> questions = questionDAO.getOrderedPagedQuestionsBy(WherePara.userName ,userName,1, OrderByPara.createdAt);
        if (questions==null) return new ArrayList<QuestionVO>();
        List<QuestionVO> questionVOs = new ArrayList<>();
        for (Question question : questions) {
            questionVOs.add(timeService.transferQuestion(question, userId));
        }
        return questionVOs;
    }

    @Override
    public List<AnswerVO> getAnswerByName(String userName, long userId) {
        List<Answer> answers =  answerDAO.getOrderedPagedAnswersBy(WherePara.userName,userName,1, OrderByPara.createdAt);
        if (answers==null) return new ArrayList<AnswerVO>();
        List<AnswerVO> answerVOs = new ArrayList<>();
        for (Answer answer: answers) {
            answerVOs.add(timeService.transferAnswer(answer, userId));
        }
        return answerVOs;
    }

    @Override
    public List<ActivityVO> orderedActivity(List<QuestionVO> questionVOs, List<AnswerVO> answerVOs) {
        List<ActivityVO> activityVOs = new ArrayList<>();
        if (questionVOs!=null) {
            for (QuestionVO questionVO: questionVOs) {
                ActivityVO activityVO = new ActivityVO(questionVO);
                activityVOs.add(activityVO);
            }
        }
        if (answerVOs!=null) {
            for (AnswerVO answerVO: answerVOs) {
                ActivityVO activityVO = new ActivityVO(answerVO);
                activityVOs.add(activityVO);
            }
        }
        if (activityVOs.size()>0)
            Collections.sort(activityVOs);
        return activityVOs;
    }

    @Override
    public List<Message> getUserMessage(long userId) {
        return userDAO.getUnCheckedMessage(userId);
    }

    @Override
    public List<User> getSearchUser(String keyword) {
        return userDAO.search(keyword);
    }

    @Override
    public long getMessageCount(long userId) {
        return userDAO.getUnCheckedMessageCount(userId);
    }

    @Override
    public void readMessage(List<Long> messageIds) {
        for (Long messageId: messageIds) {
            messageDAO.markChecked(messageId);
        }
    }

}

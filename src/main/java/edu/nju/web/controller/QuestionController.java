package edu.nju.web.controller;

import edu.nju.data.entity.Question;
import edu.nju.data.entity.User;
import edu.nju.data.entity.api.WikiItem;
import edu.nju.data.util.VoteType;
import edu.nju.logic.service.QuestionService;
import edu.nju.logic.service.TransferService;
import edu.nju.logic.service.WikiService;
import edu.nju.logic.vo.AnswerVO;
import edu.nju.logic.vo.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dora on 2016/7/11.
 */

@Controller
@SessionAttributes("user")
public class QuestionController {

    @Autowired
    QuestionService service;

    @Autowired
    TransferService timeService;

    @Autowired
    WikiService wikiService;

    @RequestMapping(value="/question",method = RequestMethod.GET)
    String showAllQuestions(@RequestParam(value = "page", defaultValue = "1") int page, Model model, HttpSession session) {
        Object user = session.getAttribute("user");
        List<QuestionVO> result = service.getQuestions(page,10, user==null?-1:((User)user).getId());
        model.addAttribute("questions",result);
        return "questionList";
    }

    
    @RequestMapping(value = "/question/{id}", method = RequestMethod.GET)
    String showQuestion(@PathVariable long id, Model model, HttpSession session,SessionStatus sessionStatus){

        Object user = session.getAttribute("user");

        Object question = session.getAttribute("question");
        Question ques = question==null?service.showQuestion(id,user==null?-1:((User)user).getId()):(QuestionVO)question;
        
        session.setAttribute("question", null);
        
        List<AnswerVO> answerVOs = service.getAnswers(ques.getId(), 1, 10, user==null?-1:((User)user).getId());

        model.addAttribute("question",ques);
        model.addAttribute("answerOfQuestion",answerVOs);

        return "questionInfo";
        
    }

    @RequestMapping(value = "/question/edit",method = RequestMethod.POST)
    String editQuestion(@RequestParam("id")String questionId, @RequestParam("title")String title,
                      @RequestParam("description") String desciption) {
        service.updateQustion(Long.valueOf(questionId),title,desciption);
        return "redirect:/question/"+questionId;
    }

    @RequestMapping(value = "/question/delete", method = RequestMethod.POST)
    String deleteQuestion(String questionId, String userId, Model model) {
        boolean result = service.deleteQuestion(Long.valueOf(questionId),Long.valueOf(userId));
        model.addAttribute("deleteResult",result);
        if (result) {
            return "redirect:/question";
        } else {
            return "redirect:/question/"+questionId;
        }
    }

    
    @RequestMapping(value = "/submitQuestion",method = RequestMethod.POST)
    String newQuestion(String title, String description,
                       @RequestParam(defaultValue = "") List<Long> wikiIds,
                       @RequestParam(defaultValue = "") List<Long> docIds,
                       @RequestParam(defaultValue = "") List<Long> inviteIds,
                       HttpSession session, @ModelAttribute("user")User user){
        
        System.out.println(wikiIds);
        System.out.println(docIds);
        
        Map<String,Object> result = new HashMap<>();
        Question question;

        if(user!=null) {

            question = new Question();
            question.setTitle(title);

            question.setAuthor(user);
            question.setLastUpdatedAt(new Timestamp(new Date().getTime()));
            question.setCreatedAt(new Timestamp(new Date().getTime()));
            question.setContent(description);

            question = service.saveQuestion(question, user.getId(), wikiIds, docIds, inviteIds);
            QuestionVO questionVO = timeService.transferQuestion(question, user.getId());
            session.setAttribute("question",questionVO);


            return "redirect:/question/"+question.getId();
        }

        return "redirect:/ask";
    }

    @RequestMapping(value = "/ask",method = RequestMethod.GET)
    String newQuestion(@RequestParam(value = "wikiId",defaultValue = "-1")String wikiId, Model model){
        long wikiIdNum = Long.valueOf(wikiId);
        if (wikiIdNum >= 0) {
            WikiItem wikiItem = wikiService.getWikiById(wikiIdNum);
            if (wikiItem!=null) {
                model.addAttribute("wiki", wikiItem);
            }
        }
        return "askQuestion";
    }



}

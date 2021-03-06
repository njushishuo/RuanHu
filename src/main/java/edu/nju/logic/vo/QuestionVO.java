package edu.nju.logic.vo;

import edu.nju.data.entity.Question;
import edu.nju.data.entity.api.Document;
import edu.nju.data.entity.api.WikiItem;

import java.util.List;

/**
 * Created by cuihao on 2016/7/14.
 */
public class QuestionVO extends Question implements ViewTime {

    private String createAtForView;
    private String updateAtForView;
    private int isVote;
    private List<WikiItem> wikiItems;
    private List<Document> documents;
    private boolean canDelete;
    private boolean canEdit;

    public QuestionVO(Question question){
        this.setId(question.getId());
        this.setCreatedAt(question.getCreatedAt());
        this.setLastUpdatedAt(question.getLastUpdatedAt());
        this.setAnswerCount(question.getAnswerCount());
        this.setAuthor(question.getAuthor());
        this.setCommentList(question.getCommentList());
        this.setContent(question.getContent());
        this.setAuthorId(question.getAuthorId());
        this.setTitle(question.getTitle());
        this.setViewCount(question.getViewCount());
        this.setVoteCount(question.getVoteCount());
    }

    @Override
    public String getCreateAtForView() {
        return createAtForView;
    }

    public void setCreateAtForView(String createAtForView) {
        this.createAtForView = createAtForView;
    }

    @Override
    public String getUpdateAtForView() {
        return updateAtForView;
    }

    public void setUpdateAtForView(String updateAtForView) {
        this.updateAtForView = updateAtForView;
    }

    public int getIsVote() {
        return isVote;
    }

    public void setIsVote(int isVote) {
        this.isVote = isVote;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public List<WikiItem> getWikiItems() {
        return wikiItems;
    }

    public void setWikiItems(List<WikiItem> wikiItems) {
        this.wikiItems = wikiItems;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }
}

package edu.nju.data.dao;

import java.util.List;


/**
 * Created by Dora on 2016/7/20.
 */
public interface WikiDAO {

    List getRelatedQuestions(long wikiID);

    List getRelatedDocuments(long wikiID);

}
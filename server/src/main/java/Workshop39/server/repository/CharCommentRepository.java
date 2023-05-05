package Workshop39.server.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import Workshop39.server.model.Comment;

@Repository
public class CharCommentRepository {
    @Autowired
    private MongoTemplate template;

    private static final String COMMENTS_COL = "comments";

    public Comment insertComment(Comment c) {
        // the data that you are inserting (c) and where you are inserting it to (COMMENTS_COL)
        return this.template.insert(c, COMMENTS_COL);
    }

    public List<Comment> getAllComment(String charId) {
        PageRequest pageable = PageRequest.of(0, 10);
        Query commentsDynamicQry = new Query()
            .addCriteria(Criteria.where("charId").is(charId))
            .with(pageable);

        // Use mongo template to do a query (find)
        // takes in a query, in which class and from where
        List<Comment> filterComments = 
            template.find(commentsDynamicQry, Comment.class, COMMENTS_COL);
        Page<Comment> commentPage = PageableExecutionUtils.getPage(
            filterComments, 
            pageable,
            ()-> template.count(commentsDynamicQry, Comment.class));

        return commentPage.toList();
    }




}

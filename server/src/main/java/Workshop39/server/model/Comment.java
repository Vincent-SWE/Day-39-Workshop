package Workshop39.server.model;

import java.io.Serializable;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Comment implements Serializable {
    
    // To know which character the person is commenting on
    private String charId;
    private String comment;

    // 
    public String getCharId() {
        return charId;
    }
    public void setCharId(String charId) {
        this.charId = charId;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    

    // Pass in a document object which is a Binary JSON
    public static Comment create(Document d) {
        Comment c = new Comment();
        c.setCharId(d.getObjectId("charId").toString());
        c.setComment(d.getString("comment"));
        return c;
    }


    // To JSON
    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("charId", getCharId())
            .add("comment", getComment())
            .build();
    }

}

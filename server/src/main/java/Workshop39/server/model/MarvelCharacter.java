package Workshop39.server.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;



import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class MarvelCharacter implements Serializable {
    
    private Integer id;
    private String name;
    private String desc;
    private String photo;
    private List<Comment> comments;


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getDesc() {
        return desc;
    }


    public void setDesc(String desc) {
        this.desc = desc;
    }


    public String getPhoto() {
        return photo;
    }


    public void setPhoto(String photo) {
        this.photo = photo;
    }


    public List<Comment> getComments() {
        return comments;
    }


    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }


    public static MarvelCharacter createJson(JsonObject o) {
        MarvelCharacter m = new MarvelCharacter();
        JsonObject t = o.getJsonObject("thumbnail");
        String path = t.getString("path");
        String fileExt = t.getString("extension");
        m.setId(o.getJsonNumber("id").intValue());
        m.setName(o.getString("name"));
        m.setDesc(o.getString("description"));
        m.setPhoto(path + "." + fileExt);
        return m;
    }


    public static List<MarvelCharacter> create(String json) throws IOException {
        List<MarvelCharacter> chars = new LinkedList<>();
        try(InputStream is = new ByteArrayInputStream(json.getBytes())) {
            JsonReader jrd = Json.createReader(is);
            JsonObject o = jrd.readObject();
            JsonObject oo = o.getJsonObject("data");
            if(oo.getJsonArray("results") != null) {
                chars = oo.getJsonArray("results").stream()
                        .map(v -> (JsonObject)v)
                        .map(v -> MarvelCharacter.createJson(v))
                        .toList();            
            }
        }
        return chars;
    }


    // Translating Json string from cache back to MarvelCharacter
    public static MarvelCharacter createFromCache(String json) throws IOException {
        MarvelCharacter m = new MarvelCharacter();
        try(InputStream is = new ByteArrayInputStream(json.getBytes())) {
            JsonReader jrd = Json.createReader(is);
            JsonObject o = jrd.readObject();
            m.setId(o.getJsonNumber("id").intValue());
            m.setName(o.getString("name"));
            m.setDesc(o.getString("description"));
            m.setPhoto(o.getString("photo"));

        }
        return m;
    }

    
    // Return a Json Object
    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("id", getId())
                .add("name", getName())
                .add("description", getDesc())
                .add("photo", getPhoto())
                .build();
    }




}

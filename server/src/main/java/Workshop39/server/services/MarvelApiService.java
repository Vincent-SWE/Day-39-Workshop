package Workshop39.server.services;



import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import Workshop39.server.model.MarvelCharacter;



@Service
public class MarvelApiService {
    
    @Value("${workshop39.marvel.api.url}")
    private String marvelApiUrl;

    @Value("${workshop39.marvel.api.priv_key}")
    private String marvelApiPrivKey;
    
    @Value("${workshop39.marvel.api.pub_key}")
    private String marvelApiPubKey;


    private String[] getMarvelApiHash() {
        String[] result = new String[2];
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long tsVal = timestamp.getTime();
        String hashVal = tsVal + marvelApiPrivKey + marvelApiPubKey;
        // created an array so [0] is to return timestamp and [1] is to return the hash
        result[0] = tsVal+"";
        result[1] = DigestUtils.md5Hex(hashVal);
        return result;

    }

    public Optional<List<MarvelCharacter>> getCharacters(String characterName,
        Integer limit, Integer offset) {
            ResponseEntity<String> resp = null;
            List<MarvelCharacter> c = null;
            String[] r = getMarvelApiHash();

            String marvelApiCharsUrl = UriComponentsBuilder
                        .fromUriString(marvelApiUrl + "characters")
                        .queryParam("ts", r[0])
                        .queryParam("apikey", marvelApiPubKey.trim())
                        .queryParam("hash", r[1])
                        .queryParam("nameStartsWith", 
                        characterName.replaceAll(" ", "+"))
                        .queryParam("limit", limit)
                        .queryParam("offset", offset)
                        .toUriString();

            // when you call API on server side need REST Template
            RestTemplate rTemplate = new RestTemplate();
            resp = rTemplate.getForEntity(marvelApiCharsUrl, String.class);
            // Take the response and translate it to a list of marvel characters
            try {
                c = MarvelCharacter.create(resp.getBody());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(c != null)
                return Optional.of(c);
            return Optional.empty();
        }
    

        public Optional<MarvelCharacter> getCharacterDetails(String charId) {
            ResponseEntity<String> resp = null;
            List<MarvelCharacter> cArr = null;
            MarvelCharacter c = null;

            String[] r = getMarvelApiHash();

            String marvelApiCharsUrl = UriComponentsBuilder
                        .fromUriString(marvelApiUrl + "characters/" + charId)
                        .queryParam("ts", r[0])
                        .queryParam("apikey", marvelApiPubKey.trim())
                        .queryParam("hash", r[1])
                        .toUriString();

            // when you call API on server side need REST Template
            RestTemplate rTemplate = new RestTemplate();
            resp = rTemplate.getForEntity(marvelApiCharsUrl, String.class);
            // Take the response and translate it to a list of marvel characters
            try {
                cArr = MarvelCharacter.create(resp.getBody());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(cArr != null)
                return Optional.of(c);
            return Optional.empty();


        }




        
}

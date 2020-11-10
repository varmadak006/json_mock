package services;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import play.Configuration;
import play.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;

/**
 * Created by indraneel on 07/11/20
 */
@Singleton
public class JsonStore {

    JsonNode storeData;

    @Inject
    Configuration configuration;

    @Inject
    public JsonStore(Configuration configuration) {
        String filePath=configuration.getString("storejson.file.location", "store.json");
        storeData=parseFileToJson(filePath);
    }
    private JsonNode parseFileToJson(String filePath)  {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode= null;
        try {
            jsonNode = objectMapper.readTree(new File(filePath));
        } catch (IOException e) {
            Logger.error("error while reading json file " , e);
        }
        return jsonNode;
    }

    public void writeJsonToFile() throws IOException {
        //TODO: compare hash of storeData and actual data in json
        //if they are

        ObjectMapper mapper = new ObjectMapper();
        String filePath=configuration.getString("storejson.file.location", "store.json");
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(new File(filePath), storeData);
    }

    public JsonNode getStoreData() {
        return storeData;
    }
}

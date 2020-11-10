package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import models.NonUniqueIdException;
import play.libs.Json;
import play.mvc.*;

import services.JsonCRUDHelper;
import services.JsonStore;
import views.html.*;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class JsonCrudController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

    @Inject
    JsonStore jsonStore;

    @Inject
    JsonCRUDHelper jsonCRUDHelper;

    public static String SORT_KEY="_sort";
    public static String ORDER_KEY="_order";

    public Result getAllFilteredEntities(String entity,Long id){
        JsonNode jsonNode=jsonStore.getStoreData();
        JsonNode entityArrayNode=jsonNode.path(entity);
        if(entityArrayNode.isArray()) {
            for (JsonNode entityNode : entityArrayNode) {
                Long curId = entityNode.path("id").asLong();
                if (curId.equals(id)) {
                    return ok(entityNode);
                }
            }
        }
        return ok();
    }

    public Result getAllEntities(String entity){
        Map<String,String[]> queryParams= request().queryString();
        Set<String> searchParams=queryParams.keySet().stream()
                .filter(s->!s.startsWith("_") && !"q".equals(s))
                .collect(Collectors.toSet());

        JsonNode jsonNode=jsonStore.getStoreData();
        JsonNode entityArrayNode=jsonNode.path(entity);
        if(queryParams.size()==0 && searchParams.isEmpty()){
            return ok(entityArrayNode);
        }

        ArrayNode root = Json.newArray();
        if(entityArrayNode.isArray()){
            for (JsonNode entityNode : entityArrayNode) {
                boolean entitysuccess=false;
                for (String param : searchParams) {
                    JsonNode value = entityNode.path(param);
                    boolean multiValFilterSuccess=false;
                    for(String queryparamvalue:queryParams.get(param)){
                        multiValFilterSuccess|=value.asText().equals(queryparamvalue);
                    }
                    entitysuccess|=multiValFilterSuccess;
                }

                if(queryParams.containsKey("q")){
                    for(String queryParamvalue:queryParams.get("q")) {
                        if(entityNode.toString().contains(queryParamvalue)) entitysuccess=true;
                    }
                }
                if(entitysuccess){
                    root.add(entityNode);
                }
            }
        }

        boolean orderByAsc=true;
        String sortCol=null;
        if(queryParams.containsKey(SORT_KEY)){
            if(queryParams.containsKey(ORDER_KEY)){
                orderByAsc=queryParams.get(ORDER_KEY)[0].equals("asc");
            }
            sortCol= queryParams.get(SORT_KEY)[0];
            return jsonCRUDHelper.sort(root,sortCol,orderByAsc);
        }
        return ok(root);
    }



    public Result create(String entityType) {
        try {
            return jsonCRUDHelper.doCrudOperation(entityType, true, false, false, false, null);
        }catch (NonUniqueIdException e){
            return badRequest(e.getMessage());
        }
    }

    public Result createOrReplace(String entityType,Long id){
        return jsonCRUDHelper.doCrudOperation(entityType,true,true,false,false,id);
    }

    public Result update(String entityType,Long id){
        return jsonCRUDHelper.doCrudOperation(entityType,false,false,true,false,id);
    }

    public Result delete(String entityType, Long id){
        return jsonCRUDHelper.doCrudOperation(entityType,false,false,false,true,id);
    }


}

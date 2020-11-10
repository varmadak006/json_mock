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

        JsonNode entityNode=jsonCRUDHelper.find(jsonNode,entity,id);
        if(entityNode!=null){
            return ok(entityNode);
        }
        return notFound();
    }

    public Result getAllFilteredSubEntities(String entity,Long id,String subentity){
        JsonNode jsonNode=jsonStore.getStoreData();
        JsonNode entityNode=jsonCRUDHelper.find(jsonNode,entity,id);
        if(entityNode.path(subentity)!=null &&!entityNode.path(subentity).isMissingNode() ){
            return ok(entityNode.path(subentity));
        }
        return notFound();
    }


    public Result getAllEntities(String entity){
        Map<String,String[]> queryParams= request().queryString();
        Set<String> queryFilterParamsCols=queryParams.keySet().stream()
                .filter(s->!s.startsWith("_") && !"q".equals(s))
                .collect(Collectors.toSet());

        JsonNode jsonNode=jsonStore.getStoreData();
        JsonNode entityArrayNode=jsonNode.path(entity);
        if(queryParams.size()==0 ){
            //if there are no query params return all
            return ok(entityArrayNode);
        }

        ArrayNode filteredArrayNode = Json.newArray();
        if(entityArrayNode.isArray()){
            for (JsonNode curEntityNode : entityArrayNode) {
                boolean entityMatchesAllFilterCriteria=false;
                for (String param : queryFilterParamsCols) {
                    JsonNode value = curEntityNode.path(param);
                    //check if criteria satisfies for any of multiple vals of single param Ex: id=1&id=2
                    boolean entityMatchesAnyFilterCriteriaVal=false;
                    for(String queryParamValue:queryParams.get(param)){
                        entityMatchesAnyFilterCriteriaVal |= value.asText().equals(queryParamValue);
                    }
                    entityMatchesAllFilterCriteria|=entityMatchesAnyFilterCriteriaVal;
                }

                //filter for basic search query
                if(queryParams.containsKey("q")){
                    for(String queryParamvalue:queryParams.get("q")) {
                        if(!curEntityNode.isNull() && curEntityNode.toString().toLowerCase().contains(queryParamvalue.toLowerCase())) entityMatchesAllFilterCriteria=true;
                    }
                }
                if(entityMatchesAllFilterCriteria){
                    filteredArrayNode.add(curEntityNode);
                }
            }
        }

        boolean orderByAsc=true;
        String sortCol=null;
        if(queryParams.containsKey(SORT_KEY)){
            if(queryParams.containsKey(ORDER_KEY)) {
                orderByAsc = queryParams.get(ORDER_KEY)[0].equals("asc");
                if(!orderByAsc && !queryParams.get(ORDER_KEY)[0].equals("desc")){
                    return badRequest("please enter valid orderBy clause => " +queryParams.get(ORDER_KEY)[0]);
                }
            }
            sortCol= queryParams.get(SORT_KEY)[0];
            if(queryFilterParamsCols.size()==0){
                return jsonCRUDHelper.sort((ArrayNode)entityArrayNode,sortCol,orderByAsc);
            }else{
                return jsonCRUDHelper.sort(filteredArrayNode,sortCol,orderByAsc);
            }
        }
        return ok(filteredArrayNode);
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

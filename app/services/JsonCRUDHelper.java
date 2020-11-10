package services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.NonUniqueIdException;
import play.libs.Json;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static play.mvc.Controller.request;
import static play.mvc.Results.internalServerError;
import static play.mvc.Results.ok;

/**
 * Created by indraneel on 10/11/20
 */
@Singleton
public class JsonCRUDHelper {

    @Inject
    JsonStore jsonStore;

    public Result doCrudOperation(String entityType, boolean iscreate, boolean isreplace, boolean isupdate, boolean isdelete, Long id){
        JsonNode requestBodyJsonNode=request().body().asJson();
        JsonNode storeData=jsonStore.getStoreData();
        if(storeData.path(entityType).isMissingNode()){
            ((ObjectNode) storeData).putArray(entityType);
        }
        JsonNode entityArray=storeData.path(entityType);
        long maxId= 0L,idFromRequest=-1L;

        if(id!=null){
            idFromRequest=id;
        }else if(!requestBodyJsonNode.path("id").isMissingNode()) {
            // id can be sent in POST request
            idFromRequest=requestBodyJsonNode.path("id").asLong();
        }
        if(entityArray.isArray()){
            for(int i=entityArray.size()-1;i>=0;i--){
                JsonNode curEntity=entityArray.get(i);
                long curEntityId=curEntity.path("id").asLong();
                if(idFromRequest==curEntityId){
                    if(isreplace || isdelete){
                        //PUT -> remove and add at the end or DELETE -> delete the node
                        ((ArrayNode)entityArray).remove(i);
                    }else if(isupdate){
                        //PATCH -> modify the field value
                        Iterator<Map.Entry<String, JsonNode>> fieldIterator = requestBodyJsonNode.fields();
                        while (fieldIterator.hasNext())
                        {
                            Map.Entry<String, JsonNode> entry = fieldIterator.next();
                            ((ObjectNode) curEntity).put(entry.getKey(),entry.getValue());
                        }
                    }else{
                        throw new NonUniqueIdException(idFromRequest,entityType);
                    }
                }
                if(curEntityId>maxId){
                    maxId=curEntityId;
                }
            }
        }

        if(!isdelete) {
            if (iscreate || isreplace) {
                if (requestBodyJsonNode.path("id").isNull() || requestBodyJsonNode.path("id").isMissingNode()) {
                    ((ObjectNode) requestBodyJsonNode).put("id", maxId + 1);
                }
                ((ArrayNode) entityArray).add(requestBodyJsonNode);
            }
            try {
                jsonStore.writeJsonToFile();
            } catch (IOException e) {
                return internalServerError("unable to create at the moment");
            }

            return ok(requestBodyJsonNode);
        }else{

            try {
                jsonStore.writeJsonToFile();
            } catch (IOException e) {
                return internalServerError("unable to create at the moment");
            }
            return ok();
        }
    }


    public Result sort(ArrayNode curRootNodeArr, String sortCol, boolean orderByAsc) {
        //converting arraynode to arr for sorting
        JsonNode[] arr = new JsonNode[curRootNodeArr.size()];
        int idx=0;
        for(JsonNode jsonNode:curRootNodeArr){
            arr[idx++]=jsonNode;
        }
        Arrays.sort(arr,(n1, n2)->{
            int orderflag=orderByAsc?1:-1;
            //sorting based on number / text( Ex:- 12,2 => 2,12   "12","2"=>"12","2")
            if(n1.path(sortCol).isNumber() && n2.path(sortCol).isNumber()){
                return (int) (orderflag *(n1.path(sortCol).asLong()-n2.path(sortCol).asLong()));
            }
            return orderflag*(n1.path(sortCol).asText().compareTo(n2.path(sortCol).asText()));
        });
        ArrayNode root = Json.newArray();
        for(Object item : arr) {
            ObjectNode node = (ObjectNode) Json.toJson(item);
            root.add(node);
        }
        return ok(root);
    }



}

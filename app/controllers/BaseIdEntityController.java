package controllers;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.BaseIdEntity;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.BaseIdEntityService;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by indraneel on 08/11/20
 */
public class BaseIdEntityController<E extends BaseIdEntity, S extends BaseIdEntityService> extends Controller {

    @Inject
    protected S entityService;

    protected ArrayNode toJsonArray(List list) {
        ArrayNode root = Json.newArray();
        for(Object item : list) {
            ObjectNode node = (ObjectNode) Json.toJson(item);
            root.add(node);
        }

        return  root;
    }

    public Result index(){
        return ok(toJsonArray(entityService.findAll()));
    }

}

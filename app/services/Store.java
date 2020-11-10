package services;

import models.Author;
import models.BaseIdEntity;
import models.Post;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by indraneel on 08/11/20
 */
@Singleton
public class Store<T extends BaseIdEntity> {
    Map<String,List<T>> mp;

    @Inject
    public void Store(){
        mp=new HashMap<>();
        List<Post> postList=new ArrayList<>();
        postList.add(new Post("title1","author1",1,1));
        postList.add(new Post("title2","author2",2,2));
        mp.put(Post.JSON_ARRAY_NAME, (List<T>) postList);
        List<Author> authorList=new ArrayList<>();
        authorList.add(new Author("first_name1","last_name1",10));
        mp.put(Author.JSON_ARRAY_NAME,new ArrayList<>());
    }
    public T add(T model){
        mp.get(model.getClass().getSimpleName()).add(model);
        return model;
    }

    public List<T> get(Class clss){
        return mp.get(clss.getSimpleName());
    }

}

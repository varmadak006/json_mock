package services;

import com.google.inject.ImplementedBy;
import models.Post;

/**
 * Created by indraneel on 08/11/20
 */
@ImplementedBy(PostServiceImpl.class)
public interface PostService extends BaseIdEntityService<Long, Post> {
    
}

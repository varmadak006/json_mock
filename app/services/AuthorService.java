package services;

import com.google.inject.ImplementedBy;
import models.Author;

/**
 * Created by indraneel on 08/11/20
 */
@ImplementedBy(AuthorServiceImpl.class)
public interface AuthorService extends BaseIdEntityService<Long, Author>{
}

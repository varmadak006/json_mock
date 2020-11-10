package services;

import models.BaseIdEntity;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Created by indraneel on 08/11/20
 */
public class BaseIdEntityServiceImpl<I,T extends BaseIdEntity> implements BaseIdEntityService<I,T> {

    @Inject
    Store store;

    protected Class entityClass;

    @Inject
    public BaseIdEntityServiceImpl(){
        //getActualTypeArguments()[1] will get parameterizedType T
        Class clazz = (Class) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[1];
        entityClass = clazz;
    }

    @Override
    public List<T> findAll() {
        return store.get(entityClass);
    }
}

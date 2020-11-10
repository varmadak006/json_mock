package services;

import models.BaseIdEntity;

import java.util.List;

/**
 * Created by indraneel on 08/11/20
 */
public interface BaseIdEntityService<I,T extends BaseIdEntity> {
    List<T> findAll();
}

package models;


import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by indraneel on 08/11/20
 */
public abstract class BaseIdEntity {

    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonIgnore
    public abstract String getJsonArrayName();
}

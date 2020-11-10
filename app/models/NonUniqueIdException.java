package models;

/**
 * Created by indraneel on 10/11/20
 */
public class NonUniqueIdException extends RuntimeException{
    private long id;
    private String entityType;

    public NonUniqueIdException( long id, String entityType) {
        super(entityType+" "+id+" already exists ");
        this.id = id;
        this.entityType = entityType;
    }
}

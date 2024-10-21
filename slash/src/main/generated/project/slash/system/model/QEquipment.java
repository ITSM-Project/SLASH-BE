package project.slash.system.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEquipment is a Querydsl query type for Equipment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEquipment extends EntityPathBase<Equipment> {

    private static final long serialVersionUID = -652166800L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEquipment equipment = new QEquipment("equipment");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final QSystems systems;

    public QEquipment(String variable) {
        this(Equipment.class, forVariable(variable), INITS);
    }

    public QEquipment(Path<? extends Equipment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEquipment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEquipment(PathMetadata metadata, PathInits inits) {
        this(Equipment.class, metadata, inits);
    }

    public QEquipment(Class<? extends Equipment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.systems = inits.isInitialized("systems") ? new QSystems(forProperty("systems")) : null;
    }

}


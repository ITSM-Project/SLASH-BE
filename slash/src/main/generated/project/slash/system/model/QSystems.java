package project.slash.system.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSystems is a Querydsl query type for Systems
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSystems extends EntityPathBase<Systems> {

    private static final long serialVersionUID = 267532422L;

    public static final QSystems systems = new QSystems("systems");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QSystems(String variable) {
        super(Systems.class, forVariable(variable));
    }

    public QSystems(Path<? extends Systems> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSystems(PathMetadata metadata) {
        super(Systems.class, metadata);
    }

}


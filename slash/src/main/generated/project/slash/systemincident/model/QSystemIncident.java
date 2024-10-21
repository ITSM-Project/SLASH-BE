package project.slash.systemincident.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSystemIncident is a Querydsl query type for SystemIncident
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSystemIncident extends EntityPathBase<SystemIncident> {

    private static final long serialVersionUID = 265370861L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSystemIncident systemIncident = new QSystemIncident("systemIncident");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> incidentTime = createNumber("incidentTime", Long.class);

    public final BooleanPath isInclude = createBoolean("isInclude");

    public final project.slash.taskrequest.model.QTaskRequest taskRequest;

    public QSystemIncident(String variable) {
        this(SystemIncident.class, forVariable(variable), INITS);
    }

    public QSystemIncident(Path<? extends SystemIncident> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSystemIncident(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSystemIncident(PathMetadata metadata, PathInits inits) {
        this(SystemIncident.class, metadata, inits);
    }

    public QSystemIncident(Class<? extends SystemIncident> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.taskRequest = inits.isInitialized("taskRequest") ? new project.slash.taskrequest.model.QTaskRequest(forProperty("taskRequest"), inits.get("taskRequest")) : null;
    }

}


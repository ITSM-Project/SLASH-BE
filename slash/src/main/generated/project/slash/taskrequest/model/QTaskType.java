package project.slash.taskrequest.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTaskType is a Querydsl query type for TaskType
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTaskType extends EntityPathBase<TaskType> {

    private static final long serialVersionUID = 885690350L;

    public static final QTaskType taskType1 = new QTaskType("taskType1");

    public final NumberPath<Integer> deadline = createNumber("deadline", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath serviceRelevance = createBoolean("serviceRelevance");

    public final StringPath taskDetail = createString("taskDetail");

    public final StringPath taskType = createString("taskType");

    public QTaskType(String variable) {
        super(TaskType.class, forVariable(variable));
    }

    public QTaskType(Path<? extends TaskType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTaskType(PathMetadata metadata) {
        super(TaskType.class, metadata);
    }

}


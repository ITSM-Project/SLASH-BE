package project.slash.taskrequest.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTaskRequest is a Querydsl query type for TaskRequest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTaskRequest extends EntityPathBase<TaskRequest> {

    private static final long serialVersionUID = -728971941L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTaskRequest taskRequest = new QTaskRequest("taskRequest");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    public final NumberPath<Integer> additionalTime = createNumber("additionalTime", Integer.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createTime = _super.createTime;

    public final BooleanPath dueOnTime = createBoolean("dueOnTime");

    public final project.slash.system.model.QEquipment equipment;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final project.slash.user.model.QUser manager;

    public final project.slash.user.model.QUser requester;

    public final EnumPath<project.slash.taskrequest.model.constant.RequestStatus> status = createEnum("status", project.slash.taskrequest.model.constant.RequestStatus.class);

    public final QTaskType taskType;

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateTime = _super.updateTime;

    public QTaskRequest(String variable) {
        this(TaskRequest.class, forVariable(variable), INITS);
    }

    public QTaskRequest(Path<? extends TaskRequest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTaskRequest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTaskRequest(PathMetadata metadata, PathInits inits) {
        this(TaskRequest.class, metadata, inits);
    }

    public QTaskRequest(Class<? extends TaskRequest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.equipment = inits.isInitialized("equipment") ? new project.slash.system.model.QEquipment(forProperty("equipment"), inits.get("equipment")) : null;
        this.manager = inits.isInitialized("manager") ? new project.slash.user.model.QUser(forProperty("manager")) : null;
        this.requester = inits.isInitialized("requester") ? new project.slash.user.model.QUser(forProperty("requester")) : null;
        this.taskType = inits.isInitialized("taskType") ? new QTaskType(forProperty("taskType")) : null;
    }

}


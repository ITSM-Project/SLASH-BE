package project.slash.contract.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEvaluationItems is a Querydsl query type for EvaluationItems
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEvaluationItems extends EntityPathBase<EvaluationItems> {

    private static final long serialVersionUID = -56385175L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEvaluationItems evaluationItems = new QEvaluationItems("evaluationItems");

    public final EnumPath<project.slash.contract.constant.Category> category = createEnum("category", project.slash.contract.constant.Category.class);

    public final QContract contract;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath period = createString("period");

    public final ListPath<ServiceTarget, QServiceTarget> serviceTargets = this.<ServiceTarget, QServiceTarget>createList("serviceTargets", ServiceTarget.class, QServiceTarget.class, PathInits.DIRECT2);

    public final NumberPath<Integer> weight = createNumber("weight", Integer.class);

    public QEvaluationItems(String variable) {
        this(EvaluationItems.class, forVariable(variable), INITS);
    }

    public QEvaluationItems(Path<? extends EvaluationItems> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEvaluationItems(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEvaluationItems(PathMetadata metadata, PathInits inits) {
        this(EvaluationItems.class, metadata, inits);
    }

    public QEvaluationItems(Class<? extends EvaluationItems> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.contract = inits.isInitialized("contract") ? new QContract(forProperty("contract")) : null;
    }

}


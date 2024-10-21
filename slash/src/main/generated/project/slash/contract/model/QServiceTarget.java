package project.slash.contract.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QServiceTarget is a Querydsl query type for ServiceTarget
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QServiceTarget extends EntityPathBase<ServiceTarget> {

    private static final long serialVersionUID = 500117867L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QServiceTarget serviceTarget = new QServiceTarget("serviceTarget");

    public final QEvaluationItems evaluationItems;

    public final StringPath grade = createString("grade");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Double> max = createNumber("max", Double.class);

    public final NumberPath<Double> min = createNumber("min", Double.class);

    public final NumberPath<Double> score = createNumber("score", Double.class);

    public QServiceTarget(String variable) {
        this(ServiceTarget.class, forVariable(variable), INITS);
    }

    public QServiceTarget(Path<? extends ServiceTarget> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QServiceTarget(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QServiceTarget(PathMetadata metadata, PathInits inits) {
        this(ServiceTarget.class, metadata, inits);
    }

    public QServiceTarget(Class<? extends ServiceTarget> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.evaluationItems = inits.isInitialized("evaluationItems") ? new QEvaluationItems(forProperty("evaluationItems"), inits.get("evaluationItems")) : null;
    }

}


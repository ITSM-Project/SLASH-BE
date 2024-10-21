package project.slash.statistics.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QStatistics is a Querydsl query type for Statistics
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStatistics extends EntityPathBase<Statistics> {

    private static final long serialVersionUID = -529951731L;

    public static final QStatistics statistics = new QStatistics("statistics");

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final StringPath grade = createString("grade");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath period = createString("period");

    public final NumberPath<Double> score = createNumber("score", Double.class);

    public final StringPath serviceType = createString("serviceType");

    public QStatistics(String variable) {
        super(Statistics.class, forVariable(variable));
    }

    public QStatistics(Path<? extends Statistics> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStatistics(PathMetadata metadata) {
        super(Statistics.class, metadata);
    }

}


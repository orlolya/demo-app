package ru.interview.demo.repository;

import java.time.OffsetDateTime;
import org.springframework.data.jpa.domain.Specification;
import ru.interview.demo.entity.ObjectEntity;

public class ObjectSpecifications {

    public static Specification<ObjectEntity> isCreatedAfter(final OffsetDateTime startDate) {
        return (root, query, builder) -> builder.greaterThan(root.get("createdAt"), startDate);
    }

    public static Specification<ObjectEntity> isCreatedBefore(final OffsetDateTime endDate) {
        return (root, query, builder) -> builder.lessThan(root.get("createdAt"), endDate);
    }

}

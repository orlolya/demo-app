package ru.interview.demo.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import ru.interview.demo.dto.BulkDeleteRequest;
import ru.interview.demo.dto.ObjectData;
import ru.interview.demo.dto.ObjectDataRequest;

public interface ObjectService {

    ObjectData createObject(ObjectDataRequest dto);

    void uplateObjectById(UUID objectId, ObjectDataRequest dto);

    ObjectData getObjectById(UUID objectId);

    void deleteObjectById(UUID objectId);

    List<ObjectData> getObjects(OffsetDateTime startDate, OffsetDateTime endDate);

    void deleteObjects(BulkDeleteRequest bulkDeleteRequest);
}

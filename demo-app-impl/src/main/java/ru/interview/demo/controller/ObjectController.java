package ru.interview.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.interview.demo.api.ObjectsApi;
import ru.interview.demo.dto.BulkDeleteRequest;
import ru.interview.demo.dto.ObjectData;
import ru.interview.demo.dto.ObjectDataRequest;
import ru.interview.demo.service.ObjectService;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ObjectController implements ObjectsApi {

    private final ObjectService objectService;

    @Override
    public ResponseEntity<ObjectData> createObject(ObjectDataRequest objectDataRequest) {
        final ObjectData data = objectService.createObject(objectDataRequest);
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteObjectById(UUID objectId) {
        objectService.deleteObjectById(objectId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<ObjectData> getObjectById(UUID objectId) {
        final ObjectData objectData = objectService.getObjectById(objectId);
        return new ResponseEntity<>(objectData, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ObjectData>> getObjects(OffsetDateTime startDate,
                                                       OffsetDateTime endDate) {
        return ResponseEntity.ok(objectService.getObjects(startDate, endDate));
    }

    @Override
    public ResponseEntity<Void> updateObjectById(UUID objectId, ObjectDataRequest objectDataRequest) {
        objectService.uplateObjectById(objectId, objectDataRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> bulkDelete(BulkDeleteRequest bulkDeleteRequest) {
        objectService.deleteObjects(bulkDeleteRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
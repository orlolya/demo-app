package ru.interview.demo.service.impl;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;
import ru.interview.demo.client.ExternalDataClient;
import ru.interview.demo.dto.BulkDeleteRequest;
import ru.interview.demo.dto.ExternalObject;
import ru.interview.demo.dto.ObjectData;
import ru.interview.demo.dto.ObjectDataRequest;
import ru.interview.demo.entity.ObjectEntity;
import ru.interview.demo.mapper.ObjectTypeMapper;
import ru.interview.demo.repository.ObjectRepository;
import ru.interview.demo.repository.ObjectSpecifications;
import ru.interview.demo.service.ObjectService;
import ru.interview.demo.service.exception.ObjectNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ObjectServiceImpl implements ObjectService {

    private final ObjectTypeMapper dataMapper;
    private final ObjectRepository repository;

    private final ExternalDataClient externalDataClient;

    @Override
    @Transactional
    public ObjectData createObject(ObjectDataRequest dto) {
        log.debug("Creating object: {}", dto);

        ExternalObject externalObject = externalDataClient.getExternalObject(dto.getTypeId());
        if (!externalObject.getRegion().equals(dto.getRegion())) {
            throw new RuntimeException("Object region must be equal to type region");
        }

        final ObjectEntity entity = repository.saveAndFlush(dataMapper.mapRequestToEntity(dto));
        return dataMapper.mapEntityToDto(entity);
    }

    @Override
    @Transactional
    public void uplateObjectById(UUID objectId, ObjectDataRequest dto) {
        log.debug("Updating object: {}", dto);

        final ObjectEntity entity = repository.findById(objectId)
            .orElseThrow(() -> new ObjectNotFoundException(objectId));
        dataMapper.mapRequestToEntity(dto, entity);
        repository.saveAndFlush(entity);
    }

    @Override
    public ObjectData getObjectById(UUID objectId) {
        log.debug("Search for object with id: {}", objectId);

        final ObjectEntity entity = repository.findById(objectId)
            .orElseThrow(() -> new ObjectNotFoundException(objectId));
        return dataMapper.mapEntityToDto(entity);
    }

    @Override
    @Transactional
    public void deleteObjectById(UUID objectId) {
        log.debug("Deleting object with ID: {}", objectId);

        ObjectEntity entity = repository.findById(objectId)
                                        .orElseThrow(() -> new ObjectNotFoundException(objectId));
        entity.setDeleted(true);
        repository.save(entity);
    }

    @Override
    public List<ObjectData> getObjects(OffsetDateTime startDate, OffsetDateTime endDate) {
        log.debug("Search for objects with startDate: {}  and endDate: {}", startDate, endDate);

        Specification<ObjectEntity> specification = Specification
            .where(startDate == null ? null : ObjectSpecifications.isCreatedAfter(startDate))
            .and(endDate == null ? null : ObjectSpecifications.isCreatedBefore(endDate));

        return repository.findAll(specification).stream()
            .map(dataMapper::mapEntityToDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteObjects(BulkDeleteRequest request) {
        log.debug("Deleting activity with ids={}", request.getIds());

        final List<ObjectEntity> objects = repository.findObjectsByIdIn(request.getIds());

        if (request.getIds().size() != objects.size()) {
            for (UUID id : request.getIds()) {
                if (objects.stream().noneMatch(a -> Objects.equals(a.getId(), id))) {
                    throw new ObjectNotFoundException(id);
                }
            }
        }
        for (ObjectEntity object : objects) {
            if (object.isDeleted()) {
                throw new RuntimeException("Object is already deleted id=" + object.getId());
            }
            if (object.getRegion() == null) {
                throw new RuntimeException("It's prohibited to delete objects with empty region id=" + object.getId());
            }
        }

        bulkDelete(objects);
    }

    private void bulkDelete(List<ObjectEntity> objects) {
        for (ObjectEntity object : objects) {
            object.setDeleted(true);
        }
        repository.saveAll(objects);
    }
}

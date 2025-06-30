package com.deopraglabs.api_prysme.mapper;

import com.deopraglabs.api_prysme.data.model.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DynamicMapper {

    /**
     * Converts a model entity to DTO, replacing nested objects with their ID references
     */
    public <T, D> D toDTO(T entity, Class<D> dtoClass) {
        if (entity == null) {
            return null;
        }

        try {
            D dto = dtoClass.getDeclaredConstructor().newInstance();
            
            Field[] entityFields = entity.getClass().getDeclaredFields();
            Field[] dtoFields = dtoClass.getDeclaredFields();
            
            for (Field entityField : entityFields) {
                entityField.setAccessible(true);
                Object value = entityField.get(entity);
                
                if (value == null) {
                    continue;
                }
                
                String fieldName = entityField.getName();
                Field correspondingDtoField = findCorrespondingDtoField(dtoFields, fieldName);
                
                if (correspondingDtoField != null) {
                    correspondingDtoField.setAccessible(true);
                    
                    // Handle nested objects by extracting their IDs
                    if (isEntityObject(value)) {
                        UUID id = extractIdFromEntity(value);
                        if (id != null) {
                            correspondingDtoField.set(dto, id);
                        }
                    } 
                    // Handle lists of entities
                    else if (value instanceof List<?> list && !list.isEmpty() && isEntityObject(list.get(0))) {
                        List<UUID> ids = list.stream()
                                .map(this::extractIdFromEntity)
                                .filter(id -> id != null)
                                .collect(Collectors.toList());
                        correspondingDtoField.set(dto, ids);
                    }
                    // Handle primitive/wrapper types directly
                    else if (isPrimitiveOrWrapper(value) || value instanceof String || value instanceof UUID) {
                        correspondingDtoField.set(dto, value);
                    }
                }
            }
            
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Error converting entity to DTO", e);
        }
    }

    /**
     * Converts a DTO to model entity, with ID references that need to be resolved separately
     */
    public <D, T> T toEntity(D dto, Class<T> entityClass) {
        if (dto == null) {
            return null;
        }

        try {
            T entity = entityClass.getDeclaredConstructor().newInstance();
            
            Field[] dtoFields = dto.getClass().getDeclaredFields();
            Field[] entityFields = entityClass.getDeclaredFields();
            
            for (Field dtoField : dtoFields) {
                dtoField.setAccessible(true);
                Object value = dtoField.get(dto);
                
                if (value == null) {
                    continue;
                }
                
                String fieldName = dtoField.getName();
                
                // Skip ID reference fields (they end with "Id" or "Ids")
                if (fieldName.endsWith("Id") || fieldName.endsWith("Ids")) {
                    continue;
                }
                
                Field correspondingEntityField = findCorrespondingEntityField(entityFields, fieldName);
                
                if (correspondingEntityField != null) {
                    correspondingEntityField.setAccessible(true);
                    
                    // Handle primitive/wrapper types directly
                    if (isPrimitiveOrWrapper(value) || value instanceof String || value instanceof UUID) {
                        correspondingEntityField.set(entity, value);
                    }
                }
            }
            
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Error converting DTO to entity", e);
        }
    }

    /**
     * Converts a list of entities to DTOs
     */
    public <T, D> List<D> toDTOList(List<T> entities, Class<D> dtoClass) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(entity -> toDTO(entity, dtoClass))
                .collect(Collectors.toList());
    }

    /**
     * Converts a list of DTOs to entities
     */
    public <D, T> List<T> toEntityList(List<D> dtos, Class<T> entityClass) {
        if (dtos == null) {
            return null;
        }
        
        return dtos.stream()
                .map(dto -> toEntity(dto, entityClass))
                .collect(Collectors.toList());
    }

    private Field findCorrespondingDtoField(Field[] dtoFields, String entityFieldName) {
        // Direct match
        for (Field field : dtoFields) {
            if (field.getName().equals(entityFieldName)) {
                return field;
            }
        }
        
        // Check for ID reference fields
        String idFieldName = entityFieldName + "Id";
        String idsFieldName = entityFieldName + "Ids";
        
        for (Field field : dtoFields) {
            if (field.getName().equals(idFieldName) || field.getName().equals(idsFieldName)) {
                return field;
            }
        }
        
        return null;
    }

    private Field findCorrespondingEntityField(Field[] entityFields, String dtoFieldName) {
        for (Field field : entityFields) {
            if (field.getName().equals(dtoFieldName)) {
                return field;
            }
        }
        return null;
    }

    private boolean isEntityObject(Object obj) {
        if (obj == null) {
            return false;
        }
        
        String packageName = obj.getClass().getPackage().getName();
        return packageName.contains(".data.model");
    }

    private UUID extractIdFromEntity(Object entity) {
        try {
            Method getIdMethod = entity.getClass().getMethod("getId");
            Object id = getIdMethod.invoke(entity);
            return (UUID) id;
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isPrimitiveOrWrapper(Object obj) {
        Class<?> type = obj.getClass();
        return type.isPrimitive() || 
               type == Boolean.class || type == Byte.class || type == Character.class ||
               type == Short.class || type == Integer.class || type == Long.class ||
               type == Float.class || type == Double.class || 
               type == java.util.Date.class || type == java.time.LocalDate.class ||
               type == java.time.LocalDateTime.class || type == java.math.BigDecimal.class ||
               type.isEnum();
    }
}
package com.deopraglabs.api_prysme.mapper;

import java.util.List;

public interface Mapper<E, D> {
    D toDTO(E entity);
    E toEntity(D dto);
    List<D> toDTOList(List<E> entities);
    List<E> toEntityList(List<D> dtos);
}

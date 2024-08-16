package com.deopraglabs.api_prysme.mapper;

import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class Mapper {

    private static final ModelMapper mapper = new ModelMapper();

    public static <Origin, Destination> Destination parseObject(Origin origin, Class<Destination> destinationClass) { return mapper.map(origin, destinationClass); }

    public static <Origin, Destination> List<Destination> parseListObjects(List<Origin> origin, Class<Destination> destinationClass) {

        final List<Destination> destinationObjects = new ArrayList<>();

        for (final Origin originObject : origin) {
            destinationObjects.add(mapper.map(originObject, destinationClass));
        }

        return destinationObjects;
    }


}

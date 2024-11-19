package com.deopraglabs.api_prysme.service;

import com.deopraglabs.api_prysme.controller.PermissionController;
import com.deopraglabs.api_prysme.data.vo.PermissionVO;
import com.deopraglabs.api_prysme.mapper.custom.PermissionMapper;
import com.deopraglabs.api_prysme.repository.PermissionRepository;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Service
@Transactional
public class PermissionService {

    private final Logger logger = Logger.getLogger(PermissionService.class.getName());

    private final PermissionMapper permissionMapper;
    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    public PermissionVO save(PermissionVO permissionVO) {
        logger.info("Saving permission: " + permissionVO);
        final List<String> validations = validatePermissionInfo(permissionVO);

        if (!validations.isEmpty()) {
            throw new CustomRuntimeException.BRValidationException(validations);
        }

        if (permissionVO.getKey() > 0) {
            return permissionMapper.convertToVO(permissionRepository.save(permissionMapper.updateFromVO(
                    permissionRepository.findById(permissionVO.getKey())
                            .orElseThrow(() -> new CustomRuntimeException.PermissionNotFoundException(permissionVO.getKey())),
                    permissionVO
            ))).add(linkTo(methodOn(PermissionController.class).findById(permissionVO.getKey())).withSelfRel());
        } else {
            final var permission = permissionRepository.save(permissionMapper.convertFromVO(permissionVO));
            return permissionMapper.convertToVO(permissionRepository.save(permission))
                    .add(linkTo(methodOn(PermissionController.class).findById(permission.getId())).withSelfRel());
        }
    }

    public List<PermissionVO> findAll() {
        logger.info("Finding all permissions");
        final var permissions = permissionMapper.convertToPermissionVOs(permissionRepository.findAll());
        permissions.forEach(permission -> permission.add(linkTo(methodOn(PermissionController.class).findById(permission.getKey())).withSelfRel()));

        return permissions;
    }

    public PermissionVO findById(long id) {
        logger.info("Finding permission by id: " + id);
        return permissionMapper.convertToVO(permissionRepository.findById(id)
                        .orElseThrow(() -> new CustomRuntimeException.PermissionNotFoundException(id)))
                .add(linkTo(methodOn(PermissionController.class).findById(id)).withSelfRel());
    }

    public ResponseEntity<?> delete(long id) {
        logger.info("Deleting permission: " + id);
        return permissionRepository.deleteById(id) > 0 ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    // Regras de Negócio
    private List<String> validatePermissionInfo(PermissionVO permissionVO) {
        final List<String> validations = new ArrayList<>();

        validateBasicFields(permissionVO, validations);
        validateUniqueFields(permissionVO, validations);

        return validations;
    }

    private void validateBasicFields(PermissionVO permissionVO, List<String> validations) {

    }

    private void validateUniqueFields(PermissionVO permissionVO, List<String> validations) {

    }
}

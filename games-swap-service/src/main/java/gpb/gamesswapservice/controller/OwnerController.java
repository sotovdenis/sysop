package gpb.gamesswapservice.controller;


import gpb.gamesswapapi.api.OwnerApi;
import gpb.gamesswapapi.dto.request.OwnerRequest;
import gpb.gamesswapapi.dto.response.OwnerResponse;
import gpb.gamesswapservice.assemblers.OwnerModelAssembler;
import gpb.gamesswapservice.service.OwnerService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OwnerController implements OwnerApi {

    private final OwnerService ownerService;
    private final OwnerModelAssembler ownerModelAssembler;

    public OwnerController(OwnerService ownerService, OwnerModelAssembler ownerModelAssembler) {
        this.ownerService = ownerService;
        this.ownerModelAssembler = ownerModelAssembler;
    }

    @Override
    public CollectionModel<EntityModel<OwnerResponse>> getAllOwners() {
        List<OwnerResponse> owners = ownerService.findAll();
        return ownerModelAssembler.toCollectionModel(owners);
    }

    @Override
    public EntityModel<OwnerResponse> getOwnerById(Long id) {
        OwnerResponse owner = ownerService.findById(id);
        return ownerModelAssembler.toModel(owner);
    }

    @Override
    public ResponseEntity<EntityModel<OwnerResponse>> createOwner(OwnerRequest request) {
        OwnerResponse createdOwner = ownerService.create(request);
        EntityModel<OwnerResponse> entityModel = ownerModelAssembler.toModel(createdOwner);

        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    @Override
    public EntityModel<OwnerResponse> updateOwner(Long id, OwnerRequest request) {
        OwnerResponse updatedOwner = ownerService.update(id, request);
        return ownerModelAssembler.toModel(updatedOwner);
    }

//    @Override
//    public void deleteOwner(Long id) {
//        ownerService.delete(id);
//    }
}

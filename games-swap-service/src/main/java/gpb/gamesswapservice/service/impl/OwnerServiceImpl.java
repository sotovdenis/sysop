package gpb.gamesswapservice.service.impl;

import gpb.gamesswapapi.dto.request.OwnerRequest;
import gpb.gamesswapapi.dto.response.OwnerResponse;
import gpb.gamesswapapi.exception.ResourceNotFoundException;
import gpb.gamesswapservice.entity.Owner;
import gpb.gamesswapservice.repository.OwnerRepository;
import gpb.gamesswapservice.service.GameService;
import gpb.gamesswapservice.service.OwnerService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;
    private final GameService gameService;

    public OwnerServiceImpl(OwnerRepository ownerRepository, @Lazy GameService gameService) {
        this.ownerRepository = ownerRepository;
        this.gameService = gameService;
    }

    public List<OwnerResponse> findAll() {
        return ownerRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public OwnerResponse findById(Long id) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner", id));
        return toResponse(owner);
    }

    public OwnerResponse create(OwnerRequest request) {
        Owner owner = new Owner(
                request.firstName(),
                request.lastName(),
                request.fathersName(),
                request.email()
        );

        Owner savedOwner = ownerRepository.save(owner);
        return toResponse(savedOwner);
    }

    public OwnerResponse update(Long id, OwnerRequest request) {
        Owner existingOwner = ownerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner", id));

        existingOwner.setFirstName(request.firstName());
        existingOwner.setLastName(request.lastName());
        existingOwner.setFathersName(request.fathersName());
        existingOwner.setEmail(request.email());

        Owner updatedOwner = ownerRepository.save(existingOwner);
        return toResponse(updatedOwner);
    }

    public void delete(Long id) {
        if (!ownerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Owner", id);
        }

        ownerRepository.deleteById(id);
    }


    private OwnerResponse toResponse(Owner owner) {
        return new OwnerResponse(
                owner.getId(),
                owner.getFirstName(),
                owner.getLastName(),
                owner.getFathersName(),
                owner.getEmail()
        );
    }
}
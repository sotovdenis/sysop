package gpb.gamesswapservice.service;

import gpb.gamesswapapi.dto.request.OwnerRequest;
import gpb.gamesswapapi.dto.response.OwnerResponse;
import gpb.gamesswapapi.dto.response.PagedResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OwnerService {
    List<OwnerResponse> findAll();
    OwnerResponse findById(Long id);
    OwnerResponse create(OwnerRequest request);
    OwnerResponse update(Long id, OwnerRequest request);
    void delete(Long id);
//    OwnerResponse findOwnerById(Long id);
//    List<OwnerResponse> findAll();
//    PagedResponse<OwnerResponse> findAllOwners(Long ownerId, int page, int size);
//    OwnerResponse createOwner(OwnerRequest request);
}

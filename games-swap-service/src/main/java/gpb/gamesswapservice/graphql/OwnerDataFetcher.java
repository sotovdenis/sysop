package gpb.gamesswapservice.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import gpb.gamesswapapi.dto.request.OwnerRequest;
import gpb.gamesswapapi.dto.response.OwnerResponse;
import gpb.gamesswapservice.service.OwnerService;

import java.util.List;
import java.util.Map;

@DgsComponent
public class OwnerDataFetcher {

    private final OwnerService ownerService;

    public OwnerDataFetcher(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @DgsQuery
    public List<OwnerResponse> owners() {
        return ownerService.findAll();
    }

    @DgsQuery
    public OwnerResponse ownerById(@InputArgument Long id) {
        return ownerService.findById(id);
    }

    @DgsMutation
    public OwnerResponse createOwner(@InputArgument("input") Map<String, String> input) {

        OwnerRequest request = new OwnerRequest(
                input.get("firstName"),
                input.get("lastName"),
                input.get("fathersName"),
                input.get("email")
        );

        return ownerService.create(request);
    }

    @DgsMutation
    public OwnerResponse updateOwner(
            @InputArgument Long id,
            @InputArgument("input") Map<String, String> input) {

        OwnerRequest request = new OwnerRequest(
                input.get("firstName"),
                input.get("lastName"),
                input.get("fathersName"),
                input.get("email")
        );

        return ownerService.update(id, request);
    }

    @DgsMutation
    public Long deleteOwner(@InputArgument Long id) {
        ownerService.delete(id);
        return id;
    }
}
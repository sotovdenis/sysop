package gpb.analyticsservice;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class AnalyticsServiceImpl extends AnalyticsServiceGrpc.AnalyticsServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsServiceImpl.class);

    @Override
    public void calculateGameValue(GameValueRequest request, StreamObserver<GameValueResponse> responseObserver) {
        try {
            log.info("Received gRPC request to evaluate game: gameId={}, ownerId={}, category={}",
                    request.getGameId(), request.getOwnerId(), request.getCategory());

            int value = (int) (Math.random() * 1000);
            String verdict = value > 500 ? "HIGH" : "LOW";

            GameValueResponse response = GameValueResponse.newBuilder()
                    .setGameId(request.getGameId())
                    .setValueScore(value)
                    .setVerdict(verdict)
                    .setOwnerId(request.getOwnerId())
                    .build();

            log.info("Game evaluation completed: gameId={}, score={}, verdict={}",
                    response.getGameId(), response.getValueScore(), response.getVerdict());

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Error during game evaluation: gameId={}, ownerId={}",
                    request.getGameId(), request.getOwnerId(), e);
            responseObserver.onError(e);
        }
    }
}
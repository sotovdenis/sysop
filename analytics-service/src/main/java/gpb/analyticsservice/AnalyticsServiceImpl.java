package gpb.analyticsservice;

import gpb.analyticsservice.AnalyticsServiceGrpc;
import gpb.analyticsservice.GameValueRequest;
import gpb.analyticsservice.GameValueResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class AnalyticsServiceImpl extends AnalyticsServiceGrpc.AnalyticsServiceImplBase {

    @Override
    public void calculateGameValue(GameValueRequest request, StreamObserver<GameValueResponse> responseObserver) {

        int value = (int) (Math.random() * 1000);

        String verdict = value > 500 ? "HIGH" : "LOW";

        GameValueResponse response = GameValueResponse.newBuilder()
                .setGameId(request.getGameId())
                .setValueScore(value)
                .setVerdict(verdict)
                .setOwnerId(request.getOwnerId())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
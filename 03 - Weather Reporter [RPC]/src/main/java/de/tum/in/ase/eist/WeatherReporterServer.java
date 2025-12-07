package de.tum.in.ase.eist;

import de.tum.in.ase.eist.grpc.*;
import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class WeatherReporterServer {
    private static final Logger logger = Logger.getLogger(WeatherReporterServer.class.getName());

    private final int port;
    private final Server server;

    /**
     * Create a WeatherReporterServer server listening on {@code port} using {@code cityWeatherData}.
     */
    public WeatherReporterServer(int port, ArrayList<CityWeatherData> cityWeatherData) throws IOException {
        this(Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create()),
                port, cityWeatherData);
    }

    /**
     * Create a WeatherReporterServer server using serverBuilder as a base
     */
    public WeatherReporterServer(ServerBuilder<?> serverBuilder, int port, ArrayList<CityWeatherData> cityWeatherData) {
        this.port = port;
        server = serverBuilder.addService(new WeatherReporterService(cityWeatherData)).build();
    }

    /**
     * Start serving requests.
     */
    public void start() throws IOException {
        server.start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    WeatherReporterServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("*** server shut down");
            }
        });
    }

    /**
     * Stop serving requests and shutdown resources.
     */
    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws Exception {
        ArrayList<CityWeatherData> cityWeatherData = new ArrayList<>();

        //add some sample data
            // Create a Location
    Location munich = Location.newBuilder()
    .setCity("Munich")
    .setCountry("Germany")
    .build();

// Create Date 1
Date date1 = Date.newBuilder()
    .setDay(1)
    .setMonth(11)
    .setYear(2023)
    .build();

// Create Weather 1
Weather weather1 = Weather.newBuilder()
    .setTemperature(15.5f)
    .setHumidity(72.3f)
    .setWind(4.2f)
    .build();

CityWeatherData munichDay1 = CityWeatherData.newBuilder()
    .setLocationDate(LocationDate.newBuilder().setLocation(munich).setDate(date1))
    .setWeather(weather1)
    .build();

// Add to list
cityWeatherData.add(munichDay1);

        WeatherReporterServer server = new WeatherReporterServer(8980, cityWeatherData);
        server.start();
        server.blockUntilShutdown();
    }

    /**
     * Implementation of WeatherReport service.
     *
     * <p>See weather_reporter.proto for details of the methods.
     */
    private static class WeatherReporterService extends WeatherReporterGrpc.WeatherReporterImplBase {
        private final ArrayList<CityWeatherData> allWeatherData;

        private WeatherReporterService(ArrayList<CityWeatherData> allWeatherData) {
            this.allWeatherData = allWeatherData;
        }

        // TODO: Task 2: Write an Implementation for the rpc methods

        @Override
        public void getCityWeatherSingleDay(LocationDate request, StreamObserver<CityWeatherData> responseObserver) {
            Date date = request.getDate();
            Location loc = request.getLocation();

            for(CityWeatherData city : allWeatherData){
                LocationDate ld = city.getLocationDate();
                if(ld.getDate().equals(date) && ld.getLocation().equals(loc)){
                    //found the city weather
                    responseObserver.onNext(city);
                    responseObserver.onCompleted();
                    return;
                }
            }


            CityWeatherData empty = CityWeatherData.newBuilder()
            .setWeather(Weather.newBuilder()
            .setHumidity(0)
            .setTemperature(0)
            .setWind(0)
            .build())
            .setLocationDate(request)
            .build();

            responseObserver.onNext(empty);
            responseObserver.onCompleted();
            
        }


        @Override
        public void getCityWeatherMultipleDays(LocationDatePeriod request, StreamObserver<CityWeatherData> responseObserver){
            Date starDate = request.getStartDate();
            Date enDate = request.getEndDate();
            Location loc = request.getLocation();

            for (CityWeatherData cityData : allWeatherData){
               LocationDate ld = cityData.getLocationDate();
               if(ld.getLocation().equals(loc)){
                Date date = ld.getDate();
                while(date.getDay() >= starDate.getDay() && date.getDay() <= enDate.getDay() &&
                      date.getMonth() >= starDate.getMonth() && date.getMonth() <= enDate.getMonth() &&
                      date.getYear() >= starDate.getYear() && date.getYear() <= enDate.getYear()){

                        responseObserver.onNext(cityData);
                }
               }
            }

            responseObserver.onCompleted();
        }
    }
}
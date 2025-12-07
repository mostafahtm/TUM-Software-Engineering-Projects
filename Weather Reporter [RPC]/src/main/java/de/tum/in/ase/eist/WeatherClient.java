package de.tum.in.ase.eist;

import io.grpc.*;
import de.tum.in.ase.eist.grpc.*;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Sample client code that makes gRPC calls to the server.
 */
public class WeatherClient {

    private static final Logger logger = Logger.getLogger(WeatherClient.class.getName());

    private final WeatherReporterGrpc.WeatherReporterBlockingStub blockingStub;

    /**
     * Construct client for accessing WeatherReporter server using the existing channel.
     */
    public WeatherClient(Channel channel) {
        blockingStub = WeatherReporterGrpc.newBlockingStub(channel);
    }

    /**
     * Blocking unary call example. Calls getCityWeatherSingleDay and prints the response.
     *
     * @param day
     * @param month
     * @param year
     * @param city
     * @param country
     */
    public void getCityWeatherSingleDay(int day, int month, int year, String city, String country) {
        // TODO: Task 3 (Optional): Call the WeatherReporterService via the blockingStub

        //build request message
        Location location = Location.newBuilder()
        .setCity(city)
        .setCountry(country)
        .build();

Date date = Date.newBuilder()
        .setDay(day)
        .setMonth(month)
        .setYear(year)
        .build();

LocationDate request = LocationDate.newBuilder()
        .setLocation(location)
        .setDate(date)
        .build();

    // Call the server using the stub
    CityWeatherData response;
    try {
        response = blockingStub.getCityWeatherSingleDay(request);
        Weather weather = response.getWeather();
        System.out.printf("Weather in %s on %d.%d.%d → %.1f°C, Humidity: %.1f%%, Wind: %.1f m/s%n",
                city, day, month, year,
                weather.getTemperature(), weather.getHumidity(), weather.getWind());
    } 
    catch (Exception e) {
        System.err.println("RPC failed: " + e.getMessage());
    }
    }


    /**
     * Blocking server-streaming example. Calls getCityWeatherMultipleDays with a locationDatePeriod. Prints each
     * response CityWeatherData as it arrives.
     */
    public void getCityWeatherMultipleDays(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear, String city, String country) {
        // TODO: Task 3 (Optional): Call the WeatherReporterService via the blockingStub

    //Build the request message
    Location location = Location.newBuilder()
            .setCity(city)
            .setCountry(country)
            .build();

    Date start = Date.newBuilder()
            .setDay(startDay)
            .setMonth(startMonth)
            .setYear(startYear)
            .build();

    Date end = Date.newBuilder()
            .setDay(endDay)
            .setMonth(endMonth)
            .setYear(endYear)
            .build();

    LocationDatePeriod request = LocationDatePeriod.newBuilder()
            .setLocation(location)
            .setStartDate(start)
            .setEndDate(end)
            .build();

    // Call the server and get an iterator for the streamed responses
    try {
        Iterator<CityWeatherData> responses = blockingStub.getCityWeatherMultipleDays(request);
        // Read all responses as they arrive
        while (responses.hasNext()) {
            CityWeatherData data = responses.next();
            Weather weather = data.getWeather();
            Date date = data.getLocationDate().getDate();
            System.out.printf("  %d.%d.%d → %.1f°C, Humidity: %.1f%%, Wind: %.1f m/s%n",
                    date.getDay(), date.getMonth(), date.getYear(),
                    weather.getTemperature(), weather.getHumidity(), weather.getWind());
        }
    } catch (Exception e) {
        System.err.println("RPC failed: " + e.getMessage());
    }
    }

    public static void main(String[] args) throws InterruptedException {
        String target = "localhost:8980";
        if (args.length > 0) {
            if ("--help".equals(args[0])) {
                System.err.println("Usage: [target]");
                System.err.println("");
                System.err.println("  target  The server to connect to. Defaults to " + target);
                System.exit(1);
            }
            target = args[0];
        }

        ManagedChannel channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create()).build();
        try {
            WeatherClient weatherClient = new WeatherClient(channel);

            // Here you can test some calls the WeatherReporterServer
            // For example:
            weatherClient.getCityWeatherSingleDay(1, 11, 2023, "Munich", "Germany");
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
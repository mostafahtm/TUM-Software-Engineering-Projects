package org.spotify.client;

import io.grpc.Channel;
import io.grpc.stub.CallStreamObserver;
import io.grpc.stub.StreamObserver;

import org.spotify.db.Database;
import org.spotify.grpc.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SpotifyClient {
    @SuppressWarnings("unused")
    private final SpotifyConnection spotifyConnection;

    @SuppressWarnings("unused")
    public SpotifyClient(Channel channel) {
        this.spotifyConnection = new SpotifyConnection(channel);
    }

    public SpotifyClient() throws SpotifyClientException {
        this.spotifyConnection = new SpotifyConnection();
    }

    private Tracks fetchTracks(List<Integer> trackIds, String errorMessage) throws SpotifyClientException {
        EntitiesRequest request = EntitiesRequest.newBuilder().addAllId(trackIds).build();
        try {
            return spotifyConnection.getStub().getTrackMetadata(request);
        } catch (Throwable e) {
            throw new SpotifyClientException(errorMessage);
        }
    }

    public String displayPlaylist(int[] playlist) throws SpotifyClientException {
        // TODO: Implement displayPlaylist

        List<Integer> playlistList = Arrays.stream(playlist).boxed().toList();
        Tracks tracks = fetchTracks(playlistList, "Failed to get track metadata");

        // Check if any track ID was not found (returned count doesn't match requested count)
        if (tracks.getTrackCount() != playlist.length) {
            throw new SpotifyClientException("Failed to get track metadata");
        }

        List<Track> trackList = tracks.getTrackList();
        StringBuilder output = new StringBuilder();
        for(int i = 0; i < trackList.size(); i++){

            Track track = trackList.get(i);
            String trackName = track.getName();
            String trackArtist = track.getArtist();

            int duration = track.getDuration();
            int minutes = duration / 60;
            int seconds = duration % 60;
            String durationFormatted = String.format("%d:%02d",minutes,seconds);
            output.append((i + 1) + ". " + trackName + " - " + trackArtist + " (" + durationFormatted + ")");
            output.append("\n");
        }

        return output.toString();
    }


    public String displayAlbum(int id) throws SpotifyClientException {
        // TODO: Implement displayAlbum
        EntitiesRequest request = EntitiesRequest.newBuilder().addId(id).build();
        Albums albums;
        try {
            albums = spotifyConnection.getStub().getAlbumMetadata(request);
        } catch (Throwable e) {
            throw new SpotifyClientException("Failed to get album metadata");
        }

        if (albums.getAlbumCount() == 0) {
            throw new SpotifyClientException("Failed to get album metadata");
        }

        Album album = albums.getAlbum(0);
        int[] trackIds = album.getTracksList().stream().mapToInt(Integer::intValue).toArray();

        // Reuse displayPlaylist to get formatted track list
        String trackOutput = displayPlaylist(trackIds);

        // Calculate total duration using helper method
        Tracks tracks = fetchTracks(album.getTracksList(), "Failed to get album metadata");
        int totalDuration = tracks.getTrackList().stream().mapToInt(Track::getDuration).sum();
        int minutes = totalDuration / 60;
        int seconds = totalDuration % 60;

        // Add tab prefix to each track line
        String tabbed = trackOutput.lines().map(line -> "\t" + line).collect(Collectors.joining("\n")) + "\n";

        return album.getName() + " (" + String.format("%d:%02d", minutes, seconds) + ")\n" + tabbed;
    }

    public static void main(String[] args) throws SpotifyClientException {
        SpotifyClient client = new SpotifyClient();

        // Examples from Artemis
         System.out.println(client.displayPlaylist(new int[] {82763, 2791, 80673, 62523, 61703}));
         System.out.println(client.displayAlbum(24534));
    }
}

package com.example.spotifybackupprojectuf2;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfUsersPlaylistsRequest;

import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class AccesToken {
    private static final String clientId = "e0d671dca23c455db7f81eb5c84da38d";
    private static final String clientSecret = "fea42deaa6994ca3ac4872f05364a7da";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .build();
    private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
            .build();

    public static void clientCredentials_Sync() {
        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();

            // Set access token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            System.out.println(spotifyApi.getAccessToken());
            System.out.println("Expires in: " + clientCredentials.getExpiresIn());

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void clientCredentials_Async() {
        try {
            final CompletableFuture<ClientCredentials> clientCredentialsFuture = clientCredentialsRequest.executeAsync();

            // Thread free to do other tasks...

            // Example Only. Never block in production code.
            final ClientCredentials clientCredentials = clientCredentialsFuture.join();

            // Set access token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            final GetListOfUsersPlaylistsRequest getListOfUsersPlaylistsRequest = spotifyApi.getListOfUsersPlaylists("qt0gu1lmwolt703dsojkf839i").build();
            Paging<PlaylistSimplified> playlistSimplifiedPaging = getListOfUsersPlaylistsRequest.execute();
            System.out.println("Total: " + playlistSimplifiedPaging);


            System.out.println("Expires in: " + clientCredentials.getExpiresIn());
        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        } catch (IOException e) {throw new RuntimeException(e);} catch (ParseException e) {throw new RuntimeException(e);} catch (SpotifyWebApiException e) {throw new RuntimeException(e);}
    }

    public static void main(String[] args) {
        //clientCredentials_Sync();
       clientCredentials_Async();

    }
}

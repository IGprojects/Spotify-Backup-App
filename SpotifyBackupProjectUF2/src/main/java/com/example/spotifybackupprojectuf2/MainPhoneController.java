package com.example.spotifybackupprojectuf2;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfUsersPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetUsersProfileRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class MainPhoneController {
    private static final String clientId = "";
    private static final String clientSecret = "";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .build();
    private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
            .build();
    public TextField TextboxUsername1;
    public Button buttonLogin1;
    public PasswordField TextboxPassword1;
    public Pane ErrorPane;
    public Button buttonErrorLogin;

    public void ActionFerLogIn(ActionEvent actionEvent) throws IOException {
        if(TextboxUsername1.getText()==null){
            Alert missatge=new Alert(Alert.AlertType.ERROR);
            missatge.setTitle("DADES INCORRECTES");
            missatge.setHeaderText("Falten dades");
            missatge.showAndWait();
        }else {
            usuari usuariNou = remplenarUsuari(TextboxUsername1.getText());
            Node node = (Node) actionEvent.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
            try {

                //QUI U FA DE FORMA QUE ES TANQUEN PER COMPLET LESCENA ACTUAL CAMBIANTALA PER LALTRE
                FXMLLoader loader = new FXMLLoader(getClass().getResource("SecondScenePhone.fxml"));

                SecondScenePhoneController controller = new SecondScenePhoneController();
                controller.setUsuariIniciat(usuariNou);
                loader.setController(controller);

                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } catch (Exception e) {
                System.err.println(String.format("Error: %s", e.getMessage()));
            }
        }
    }
    public usuari remplenarUsuari(String id){
        String nouNomUsuari="";
        String nouImatgeUsuari="";
        Map<String,String>playlistsPubliques=new HashMap<String, String>();

        usuari nouUsuari=new usuari(id,nouNomUsuari,nouImatgeUsuari,playlistsPubliques);

        try {
            final CompletableFuture<ClientCredentials> clientCredentialsFuture = clientCredentialsRequest.executeAsync();
            final ClientCredentials clientCredentials = clientCredentialsFuture.join();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());

            //REMPLENAR DADES D'USUARI (NOM,URLIMATGE)
            GetUsersProfileRequest getUsersProfileRequest = spotifyApi.getUsersProfile(id).build();
            User user = getUsersProfileRequest.execute();
            nouUsuari.nomUsuari=user.getDisplayName();
            nouUsuari.urlFotoUsuari=user.getImages().toString();

            //REMPLENAR PLAYLISTS PUBLIQUES DE L'USUARI
            final GetListOfUsersPlaylistsRequest getListOfUsersPlaylistsRequest = spotifyApi.getListOfUsersPlaylists(id).build();
            Paging<PlaylistSimplified> playlistSimplifiedPaging = getListOfUsersPlaylistsRequest.execute();
            for(int i=0;i<playlistSimplifiedPaging.getItems().length;i++){
                nouUsuari.playlistsPubliques.put(playlistSimplifiedPaging.getItems()[i].getName(),playlistSimplifiedPaging.getItems()[i].getId());
            }
        } catch (CompletionException e) {System.out.println("Error: " + e.getCause().getMessage()); ErrorPanelNotification(e.getCause().getMessage());
        } catch (CancellationException e) {ErrorPanelNotification("Async operation cancelled.");
        } catch (IOException e) {throw new RuntimeException(e);}
        catch (ParseException e) {throw new RuntimeException(e);}
        catch (SpotifyWebApiException e) {throw new RuntimeException(e);}

        return nouUsuari;
    }

    public void ErrorPanelNotification(String tipusError){
        buttonErrorLogin.setText("Errror "+tipusError);
        ErrorPane.setVisible(true);
        FadeTransition trans = new FadeTransition(Duration.seconds(6), ErrorPane);
        trans.setFromValue(100.0);
        trans.setToValue(0);
        trans.play();
        trans.setOnFinished(finish->{ErrorPane.setVisible(false); trans.stop();});
    }
}


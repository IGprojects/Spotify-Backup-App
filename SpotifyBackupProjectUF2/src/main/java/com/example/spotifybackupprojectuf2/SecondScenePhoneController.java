package com.example.spotifybackupprojectuf2;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.special.SnapshotResult;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.playlists.AddItemsToPlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistsItemsRequest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class SecondScenePhoneController {
    public Button buttonMakeBackup;
    public Button buttonRestoreBackup;
    public TextField TextfieldFile;
    public Button buttonSeectFile;
    public Button buttonExit;
    public Text TextUsuari;
    public TableView <album>TableViewAlbums;
    public TableColumn <album,String>ColumnNomsAlbums;
    public Pane NotificationPanel;
    public Text TextNotification;
    FileChooser fileChooser=new FileChooser();
    public usuari usuariIniciat;
    String playlistSeleccionada="";

    private static final String clientId = "e0d671dca23c455db7f81eb5c84da38d";
    private static final String clientSecret = "fea42deaa6994ca3ac4872f05364a7da";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .build();
    private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
    public void initialize() throws IOException {


        //EMPLENAR TABLEVIEW AMB LES PLAYLISTS DE LUSUARI INICIAT
        ObservableList<album> llistatAlbums;
        llistatAlbums= FXCollections.observableArrayList();
        BuscarPlaylistsUsuari(null,llistatAlbums);
        TableViewAlbums.setItems(llistatAlbums);
        ColumnNomsAlbums.setCellValueFactory(new PropertyValueFactory<album,String>("nomAlbum"));
        TableViewAlbums.setOnMouseClicked(event->{
            playlistSeleccionada=TableViewAlbums.getSelectionModel().getSelectedItem().nomAlbum;
        });
        //TEXT DE BENVINGUDA
        this.TextUsuari.setText(usuariIniciat.nomUsuari);
    }
    public void ActionMakeBackup(ActionEvent actionEvent) throws InterruptedException {
        try {
            Map<String,String>playlistBackup=new HashMap<String, String>();
            final CompletableFuture<ClientCredentials> clientCredentialsFuture = clientCredentialsRequest.executeAsync();
            final ClientCredentials clientCredentials = clientCredentialsFuture.join();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            GetPlaylistsItemsRequest getPlaylistsItemsRequest = spotifyApi.getPlaylistsItems(usuariIniciat.playlistsPubliques.get(playlistSeleccionada)).build();
            Paging<PlaylistTrack> playlistTrackPaging = getPlaylistsItemsRequest.execute();
            for(int i=0;i<playlistTrackPaging.getItems().length;i++){
                playlistBackup.put(playlistTrackPaging.getItems()[i].getTrack().getName(),playlistTrackPaging.getItems()[i].getTrack().getId());
            }
            escriureFitxer(new File(TextfieldFile.getText()),playlistBackup);
        }catch (IOException | SpotifyWebApiException | ParseException e) {System.out.println("Error: " + e.getMessage());}

        TextNotification.setText("Your backup is ready");
        NotificationPanel.setVisible(true);
        FadeTransition trans = new FadeTransition(Duration.seconds(4), NotificationPanel);
        trans.setFromValue(100.0);
        trans.setToValue(0);
        trans.play();
        trans.setOnFinished(finish->{NotificationPanel.setVisible(false); trans.stop();});
    }

    public void ActionRestoreBackup(ActionEvent actionEvent) throws InterruptedException {
        //PENDENT
        try{
            String[] uris = new String[]{"spotify:track:01iyCAUm8EvOFqVWYJ3dVX", "spotify:episode:4GI3dxEafwap1sFiTGPKd1"};
            AddItemsToPlaylistRequest addItemsToPlaylistRequest = spotifyApi.addItemsToPlaylist(usuariIniciat.playlistsPubliques.get(playlistSeleccionada), uris).build();
            SnapshotResult snapshotResult = addItemsToPlaylistRequest.execute();


            CreatePlaylistRequest createPlaylistRequest = spotifyApi.createPlaylist(usuariIniciat.idUSuari, "Playlist Backup").public_(true).build();
            Playlist playlist = createPlaylistRequest.execute();
        }catch (Exception e){}


        TextNotification.setText("Your backup has been restored");
        NotificationPanel.setVisible(true);
        FadeTransition trans = new FadeTransition(Duration.seconds(4), NotificationPanel);
        trans.setFromValue(100.0);
        trans.setToValue(0);
        trans.play();
        trans.setOnFinished(finish->{NotificationPanel.setVisible(false); trans.stop();});
    }

    public void ActionBuscarFile(ActionEvent actionEvent) {
        File file=fileChooser.showOpenDialog(new Stage());
        TextfieldFile.setText(file.getPath());
        TextfieldFile.setPromptText(file.getPath());
    }

    public void ActionTancarSessio(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MainPhone.fxml")));
            Scene scene = new Scene(root);
            Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            appStage.setScene(scene);
            appStage.toFront();
            appStage.show();

        } catch (Exception e) {
        }
    }

    public void ActionObrirFitxer(ActionEvent actionEvent) {
        String rutaFitxer=TextfieldFile.getText();
        try {
            File file = new File(rutaFitxer);
            if(file.exists()){
                    Runtime obj = Runtime.getRuntime();
                    obj.exec("C:\\Windows\\system32\\notepad.exe "+rutaFitxer);
            }
        }catch (Exception e){

        }
    }


    public void escriureFitxer(File archivo, Map<String,String> playlistBackup) throws IOException {
        if(archivo.exists()) {
            try (BufferedWriter bf = Files.newBufferedWriter(Path.of(archivo.getPath()),
                    StandardOpenOption.TRUNCATE_EXISTING)) {
            } catch (IOException e) {
                e.printStackTrace();
            }


            try (PrintWriter out = new PrintWriter(archivo, StandardCharsets.UTF_8)) {
                for(int i=0;i<playlistBackup.size();i++){
                    out.write(playlistBackup.keySet().toArray()[i].toString()+"º"+playlistBackup.values().toArray()[i].toString()+'\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void BuscarPlaylistsUsuari(String nomUsuari,ObservableList <album>llistatAlbumsParam){
        for(int i=0;i<usuariIniciat.playlistsPubliques.size();i++){
            llistatAlbumsParam.add(new album(usuariIniciat.playlistsPubliques.keySet().stream().toList().get(i).toString()));
        }
    }
    public void setUsuariIniciat(usuari nouusuari){
        this.usuariIniciat=nouusuari;
    }


}
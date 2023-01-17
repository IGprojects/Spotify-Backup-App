package com.example.spotifybackupprojectuf2;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class SecondSceneController {
    public Button buttonclose;
    public Button buttonExit;
    public TextField TextboxUbication;
    public Button buttonUbication;
    public TableView<album> tablePlaylists;
    public TableColumn <album,String>ColumnAlbums;
    public Text TextboxUsuari;
    public usuari usuariIniciat;
    public Text TextNotification;
    public Pane NotificationPanel;
    public Button buttonSelectFile;
    public Button buttonAjustes;
    public Pane paneAjustes;
    public TextField TextFieldRutaProgramaFileExplorer;
    public Button buttonTancarAjustes;


    private static final String clientId = "";
    private static final String clientSecret = "";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .build();
    private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
    FileChooser fileChooser=new FileChooser();
    String playlistSeleccionada="";
    String RutaFileExplorer="C:\\Windows\\system32\\notepad.exe";

        //VOID INITIALIZE FA TOT LO QUE VA DINS UN COP S INICIALITZI LA SCENA
        public void initialize() {
            //EMPLENAR TABLEVIEW AMB LES PLAYLISTS DE LUSUARI INICIAT
            ObservableList <album>llistatAlbums;
            llistatAlbums=FXCollections.observableArrayList();
            BuscarPlaylistsUsuari(null,llistatAlbums);
            tablePlaylists.setItems(llistatAlbums);
            ColumnAlbums.setCellValueFactory(new PropertyValueFactory<album,String>("nomAlbum"));

            tablePlaylists.setOnMouseClicked(event->{
                playlistSeleccionada=tablePlaylists.getSelectionModel().getSelectedItem().nomAlbum;
            });
            //TEXT DE BENVINGUDA
            this.TextboxUsuari.setText("Bienvenido "+ usuariIniciat.nomUsuari);
        }
    public void PassarInfo(String textUsuari){
        TextboxUsuari.setText(textUsuari);
    }
    public void ActionExitApp(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        System.exit(0);
    }

    public void ActionCloseSession(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main.fxml")));
            Scene scene = new Scene(root);
            Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            appStage.setScene(scene);
            appStage.toFront();
            appStage.show();

        } catch (Exception e) {
        }
    }

    public void ActionBuscarFile(ActionEvent actionEvent) {
        File file=fileChooser.showOpenDialog(new Stage());
        TextboxUbication.setText(file.getPath());
        TextboxUbication.setPromptText(file.getPath());
    }

    public void ActionObrirFitxer(ActionEvent actionEvent) {
        String rutaFitxer=TextboxUbication.getText();
        try {
            File file = new File(rutaFitxer);
            if(file.exists()){
                Runtime obj = Runtime.getRuntime();
                obj.exec(RutaFileExplorer+" "+rutaFitxer);
            }
        }catch (Exception e){

        }
    }
    public void ActionRestoreBackup(ActionEvent actionEvent) {
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

    public void ActionMakeBackup(ActionEvent actionEvent) {
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
                escriureFitxer(new File(TextboxUbication.getText()),playlistBackup);
            }catch (IOException | SpotifyWebApiException | ParseException e) {System.out.println("Error: " + e.getMessage());}

            TextNotification.setText("Your backup is ready");
            NotificationPanel.setVisible(true);
            FadeTransition trans = new FadeTransition(Duration.seconds(6), NotificationPanel);
            trans.setFromValue(100.0);
            trans.setToValue(0);
            trans.play();
            trans.setOnFinished(finish->{NotificationPanel.setVisible(false); trans.stop();});
        }


    public void ActionActivarAjustes(ActionEvent actionEvent) {
        paneAjustes.setVisible(true);
    }

    public void ActionTancarAjustes(ActionEvent actionEvent) {
        paneAjustes.setVisible(false);
    }

    public void ActonCambiarFileExplorer(ActionEvent actionEvent) {
        File file=fileChooser.showOpenDialog(new Stage());
        TextFieldRutaProgramaFileExplorer.setText(file.getPath());
        TextFieldRutaProgramaFileExplorer.setPromptText(file.getPath());
        RutaFileExplorer=TextFieldRutaProgramaFileExplorer.getText();
    }

    public String llegirFitxer(File archivo) throws IOException {
        String linea="";

        if(archivo.exists()) {

        try{
            FileReader fr = new FileReader (archivo);
            BufferedReader br = new BufferedReader(fr);
            linea = br.readLine();

        }catch (Exception e){e.printStackTrace();}
        }
        return linea;
    }

    public void escriureFitxer(File archivo,Map<String,String>playlistBackup) throws IOException {
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


    public void TancarEscena(){
        try {
            FXMLLoader loader =new FXMLLoader(getClass().getResource("Main.fxml"));
            Parent root =loader.load();
            MainController mainController=loader.getController();
            Scene scene = new Scene(root);
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.show();

            /*tancar escena actual*/
            Stage actualStage=(Stage) this.buttonUbication.getScene().getWindow();
            actualStage.close();

        } catch (Exception e) {
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

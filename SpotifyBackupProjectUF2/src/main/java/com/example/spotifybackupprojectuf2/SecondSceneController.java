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
    public Button buttonErrorNotification;


    private static final String clientId = "e0d671dca23c455db7f81eb5c84da38d";
    private static final String clientSecret = "fea42deaa6994ca3ac4872f05364a7da";

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
            //HI HAURAN DOS OPCIONS EN CAS DE QUE ES SELECCIONI UNA PLAYLIST AFEGIRA LA BACKUP A LA PLAYLIST SELECCIONADA(CONSTANT QUE HA DE SER COLABORATIVA)
            //lA SEGONA OPCIO ES QUE ES CREI UNA NOVA PLAYLIST AMB EL NOM backupSpotify
            try{

                if(llegirFitxer(new File(TextboxUbication.getText()))!=null){
                    String[] uris =llegirFitxer(new File(TextboxUbication.getText()));
                    if(playlistSeleccionada!=null) {
                        //PLAYLIST SELECCIONADA
                        AddItemsToPlaylistRequest addItemsToPlaylistRequest = spotifyApi.addItemsToPlaylist(usuariIniciat.playlistsPubliques.get(playlistSeleccionada), uris).build();
                        SnapshotResult snapshotResult = addItemsToPlaylistRequest.execute();

                    }else{
                        //PLAYLIST NO SELECCIONADA (CREA UNA NOVA)
                        CreatePlaylistRequest createPlaylistRequest = spotifyApi.createPlaylist(usuariIniciat.idUSuari, "backupSpotify").public_(true).collaborative(true).build();
                        Playlist playlist = createPlaylistRequest.execute();
                        AddItemsToPlaylistRequest addItemsToPlaylistRequest = spotifyApi.addItemsToPlaylist(playlist.getId(), uris).build();
                        SnapshotResult snapshotResult = addItemsToPlaylistRequest.execute();
                    }
                }else{
                    ErrorPanelNotification("al llegir el fitxer");
                }
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
                    System.out.println(playlistTrackPaging.getItems()[i].getTrack());
                    playlistBackup.put(playlistTrackPaging.getItems()[i].getTrack().getUri(),playlistTrackPaging.getItems()[i].getTrack().getName());
                }
                if(escriureFitxer(new File(TextboxUbication.getText()),playlistBackup)==true){
                    TextNotification.setText("Your backup is ready");
                    NotificationPanel.setVisible(true);
                    FadeTransition trans = new FadeTransition(Duration.seconds(6), NotificationPanel);
                    trans.setFromValue(100.0);
                    trans.setToValue(0);
                    trans.play();
                    trans.setOnFinished(finish->{NotificationPanel.setVisible(false); trans.stop();});
                }else{
                    ErrorPanelNotification("al escriure el fitxer");
                }
            }catch (IOException | SpotifyWebApiException | ParseException e) {System.out.println("Error: " + e.getMessage());}

        }


    public void ActionActivarAjustes(ActionEvent actionEvent) {
        paneAjustes.setVisible(true);
    }

    public void ActionTancarAjustes(ActionEvent actionEvent) {
        paneAjustes.setVisible(false);
    }

    public void ActonCambiarFileExplorer(ActionEvent actionEvent) {
            try{
                File file=fileChooser.showOpenDialog(new Stage());
                TextFieldRutaProgramaFileExplorer.setText(file.getPath());
                TextFieldRutaProgramaFileExplorer.setPromptText(file.getPath());
                RutaFileExplorer=TextFieldRutaProgramaFileExplorer.getText();
            }catch(Exception e){System.out.println(e.getMessage());}
    }

    public String[] llegirFitxer(File archivo) throws IOException {
        String linea="";
        ArrayList<String> albumsMusiques = new ArrayList<String>();
        if(archivo.exists()&&archivo.getName().endsWith("txt")) {
            try{
                FileReader fr = new FileReader (archivo);
                BufferedReader br = new BufferedReader(fr);
                linea = br.readLine();
                if (linea!=null){
                String[]Album=linea.split("º");
                albumsMusiques.add(Album[0]);
                while(linea != null) {
                    linea = br.readLine();
                    if(linea==null){break;}
                    Album=linea.split("º");
                    albumsMusiques.add(Album[0]);
                }
                return ConvertArraylistToArray(albumsMusiques);
                }else{
                    return null;
                }
            }catch (Exception e){e.printStackTrace(); return null; }
        }else{
            return null;
        }
    }

    public boolean escriureFitxer(File archivo, Map<String,String>playlistBackup) throws IOException {
            if(archivo.exists()&&archivo.getName().endsWith("txt")) {
                try (BufferedWriter bf = Files.newBufferedWriter(Path.of(archivo.getPath()),
                        StandardOpenOption.TRUNCATE_EXISTING)) {
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }


                try (PrintWriter out = new PrintWriter(archivo, StandardCharsets.UTF_8)) {
                    for(int i=0;i<playlistBackup.size();i++){
                        out.write(playlistBackup.keySet().toArray()[i].toString()+"º"+playlistBackup.values().toArray()[i].toString()+'\n');
                    }
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }else{
                return false;
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

    public void ErrorPanelNotification(String tipusError){
        buttonErrorNotification.setText("Errror "+tipusError);
        NotificationPanel.setVisible(true);
        FadeTransition trans = new FadeTransition(Duration.seconds(6), NotificationPanel);
        trans.setFromValue(100.0);
        trans.setToValue(0);
        trans.play();
        trans.setOnFinished(finish->{NotificationPanel.setVisible(false); trans.stop();});
        }

    public String[] ConvertArraylistToArray(ArrayList<String>Arraylist1)
    {
        String[] newArray = new String[Arraylist1.size()];
        for(int i=0;i<Arraylist1.size();i++)
        {
            newArray[i] = Arraylist1.get(i);
        }
        return newArray;
    }

}

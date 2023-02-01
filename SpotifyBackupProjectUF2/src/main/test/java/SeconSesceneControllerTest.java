import com.example.spotifybackupprojectuf2.MainController;
import com.example.spotifybackupprojectuf2.SecondSceneController;
import com.example.spotifybackupprojectuf2.album;
import com.example.spotifybackupprojectuf2.usuari;
import javafx.collections.ObservableList;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class SeconSesceneControllerTest {

    //TESTOS PER LLEGIR FITXERS
    @Test
    public void test1() throws IOException {
        SecondSceneController secondSceneController=new SecondSceneController();
        String[] resultat =secondSceneController.llegirFitxer(new File("C:/d.json"));
        Assertions.assertEquals(null,resultat);
    }

    @Test
    public void test2() throws IOException {
        SecondSceneController secondSceneController=new SecondSceneController();
        String[] resultat =secondSceneController.llegirFitxer(new File("C:\\Users\\iferr\\OneDrive\\Escriptori\\nuevo.txt"));
        Assertions.assertEquals(null,resultat);
    }

    //TESTOS PER ESCRIURE FITXERS
    @Test
    public void test3() throws IOException {
        SecondSceneController secondSceneController=new SecondSceneController();
        boolean resultatEsperat=false;
        Boolean resultat =secondSceneController.escriureFitxer(new File("C:/backupMusiques.json"),new HashMap<String, String>());
        Assertions.assertEquals(resultatEsperat,resultat);
    }

    @Test
    public void test4() throws IOException {
        SecondSceneController secondSceneController=new SecondSceneController();
        boolean resultatEsperat=true;
        Boolean resultat =secondSceneController.escriureFitxer(new File("C:\\Users\\iferr\\OneDrive\\Escriptori\\nuevo.txt"),new HashMap<String, String>());
        Assertions.assertEquals(resultatEsperat,resultat);
    }

    //TESTOS PER COMPROBAR SI EL ID USUARI A ESTAT ENTRAT
    @Test
    public void test5(){
        MainController mainController=new MainController();
        usuari usuariEsperat=null;
        usuari usuariResultat =mainController.remplenarUsuari(null);
        Assertions.assertEquals(usuariEsperat,usuariResultat);
    }

    @Test
    public void test6(){
        MainController mainController=new MainController();
        usuari usuariNoEsperat=null;
        usuari usuariResultat =mainController.remplenarUsuari("qt0gu1lmwolt703dsojkf839i");
        Assertions.assertNotEquals(usuariNoEsperat,usuariResultat);
    }

}

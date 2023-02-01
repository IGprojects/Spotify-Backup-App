package com.example.spotifybackupprojectuf2;
import java.util.Map;

public class usuari {
        String idUSuari;
        String nomUsuari;
        String urlFotoUsuari;
        Map<String,String> playlistsPubliques;

    public usuari(String nomNou,String nomUsuari1,String urlFotoUsuari1,Map<String,String>playlistsPubliques1){
        this.idUSuari=nomNou;
        this.nomUsuari=nomUsuari1;
        this.urlFotoUsuari=urlFotoUsuari1;
        this.playlistsPubliques=playlistsPubliques1;
    }

    public String getNomUsuari() {
        return nomUsuari;
    }

    public String getIdUSuari() {
        return idUSuari;
    }

    public String getUrlFotoUsuari() {
        return urlFotoUsuari;
    }

    public void setIdUSuari(String idUSuari) {
        this.idUSuari = idUSuari;
    }

    public void setNomUsuari(String nomUsuari) {
        this.nomUsuari = nomUsuari;
    }

    public void setUrlFotoUsuari(String urlFotoUsuari) {
        this.urlFotoUsuari = urlFotoUsuari;
    }

    public Map<String, String> getPlaylistsPubliques() {
        return playlistsPubliques;
    }

    public void setPlaylistsPubliques(Map<String, String> playlistsPubliques) {
        this.playlistsPubliques = playlistsPubliques;
    }
}

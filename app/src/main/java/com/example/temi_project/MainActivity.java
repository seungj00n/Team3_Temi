package com.example.temi_project;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.neovisionaries.i18n.CountryCode;

import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.AudioFeatures;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Recommendations;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.browse.GetRecommendationsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetAudioFeaturesForTrackRequest;


//import com.spotify.android.appremote.api.ConnectionParams;
//import com.spotify.android.appremote.api.Connector;
//import com.spotify.android.appremote.api.SpotifyAppRemote;

//import com.wrapper.spotify.SpotifyApi;

public class MainActivity extends AppCompatActivity {

    SpotifyApi spotifyApi;
    ClientCredentialsRequest clientCredentialsRequest;
    ClientCredentials clientCredentials;
    String accessToken;

    Button buttonRun;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(() -> {
            //SpotifyApi instantiate
            spotifyApi = new SpotifyApi.Builder()
                    .setClientId("11134e2974a04688a9d0519bbf8d4292")
                    .setClientSecret("36f004d9afb64d49b6b6829f83e371b4")
                    .build();
            clientCredentialsRequest = spotifyApi.clientCredentials().build();
            clientCredentials = null;
            try {
                clientCredentials = clientCredentialsRequest.execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SpotifyWebApiException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            //access token 불러오기
            accessToken = clientCredentials.getAccessToken();
            Log.d("myActivity accessToken", accessToken);

            //access token 설정
            spotifyApi.setAccessToken(accessToken);


        }).start();

        buttonRun = findViewById(R.id.buttonTest);
        txt = findViewById(R.id.textTest);

        buttonRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runtest();
            }
        });
    }

    private String show_;
    void runtest(){

        //String show_;

        Thread th = new Thread(() -> {
            List<String> TrackIDs = new ArrayList<String>();
            List<String[]> grid = new ArrayList<String[]>();
            grid.add(new String[]{"막걸리 한잔", "영탁"});
            grid.add(new String[]{"곤드레 만드레", "박현빈"});
            grid.add(new String[]{"꽃", "장윤정"});
            grid.add(new String[]{"니가 왜 거기서 나와", "영탁"});
            //grid.add(new String[]{"얼쑤", "윙크"});

            String[] search_result;
            for (String[] item : grid) {
                String searchTrackName = item[0];
                String searchArtistName = item[1];
                Log.d("Working:", searchTrackName + " " + searchArtistName);

                do{
                    search_result = this.search_music(searchTrackName, searchArtistName);
                }while(search_result[0] == "Not Found");
                Log.d("ID Found", search_result[0]);
                Double[] features = this.get_features(search_result[0]);
                Log.d("Energey", features[0].toString());
                Log.d("Valence", features[1].toString());
                TrackIDs.add(search_result[0]);
            }

            String seed_track_str = String.join(",", TrackIDs);
            Log.d("seed_tracks", seed_track_str);
            Track[] music_recommends = this.music_recommend(seed_track_str, 5);

            List<String> res = new ArrayList<String>();
            for (Track x : music_recommends) {
                String detail = get_detail(x);
                res.add(detail);
            }

            show_ = String.join("\n\n", res);

        });

        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            //throw new RuntimeException(e);
            txt.setText(e.toString());
        }
        txt.setText(show_);
    }

    public String[] search_music(String searchTrackName, String searchArtistName){

        SearchTracksRequest searchTracksRequest = spotifyApi.searchTracks(searchTrackName + " " + searchArtistName)
                .market(CountryCode.KR)
                .build();

        Paging<Track> tracks = null;
        try {
            tracks = searchTracksRequest.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SpotifyWebApiException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Track[] track = tracks.getItems();
        if(track.length == 0){
            return new String[]{"Not Found"};
        }
        Track item = track[0];

        // Get the artist name
        String artistName = item.getArtists()[0].getName();
        String musicName = item.getName();
        String trackID = item.getId();

        Log.d("MainActivity muiscName", musicName);
        Log.d("MainActivity artistName", artistName);
        Log.d("MainActivity trackID", trackID);

        return new String[]{trackID, musicName, artistName};
    }

    public Double[] get_features(String TrackID){
        GetAudioFeaturesForTrackRequest getAudioFeaturesForTrackRequest = spotifyApi
                .getAudioFeaturesForTrack(TrackID)
                .build();

        AudioFeatures audioFeatures;
        try {
            audioFeatures = getAudioFeaturesForTrackRequest.execute();
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException(e);
        }

        Double Energy = audioFeatures.getEnergy().doubleValue();
        Double Valence = audioFeatures.getValence().doubleValue();
        return new Double[]{Energy, Valence};
    }

    //List<Track> tracks
    Track[] music_recommend(String seed_track_ID, Integer amount){

        GetRecommendationsRequest recommendationsRequest = spotifyApi.getRecommendations()
                .market(CountryCode.KR).limit(amount).seed_tracks(seed_track_ID)
                .seed_genres("k-pop").build();

        Recommendations recommendations = null;
        try {
            recommendations = recommendationsRequest.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SpotifyWebApiException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        Log.d("MainActivity Recommend Song:", recommendations.getTracks()[0].getName());
        Log.d("MainActivity Recommend Artist:", recommendations.getTracks()[0].getArtists()[0].getName());
        Log.d("MainActivity Recommend TrackID:", recommendations.getTracks()[0].getId());

        return recommendations.getTracks();
    }

    String get_detail(Track track){
        String music = track.getName();
        String artist = track.getArtists()[0].getName();
        Double[] features = get_features(track.getId());
        String energy = String.valueOf(features[0]);
        String valence = String.valueOf(features[1]);

        String detail = "곡명: " + music + "\n" + "가수: " + artist + "\n" + "Energy: " + energy + "\n"
                + "Valence: " + valence;

        return detail;
    }
}
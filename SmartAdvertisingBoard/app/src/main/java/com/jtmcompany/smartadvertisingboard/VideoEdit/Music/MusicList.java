package com.jtmcompany.smartadvertisingboard.VideoEdit.Music;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jtmcompany.smartadvertisingboard.R;
import com.jtmcompany.smartadvertisingboard.VideoEdit.Adapter.MusicListViewAdapter;

import java.util.ArrayList;

public class MusicList extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final int RESULT_OK_MUSIC = 300;
    ListView musicListView;
    MusicListViewAdapter adapter;
    ArrayList<MusicData> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        init();
        getMusicData();
    }

    private void init() {
        adapter=new MusicListViewAdapter(this,R.layout.music_list_item,list);
        musicListView=findViewById(R.id.menuList);
        musicListView.setAdapter(adapter);
        musicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MusicList.this, "hihi", Toast.LENGTH_SHORT).show();
            }
        });
        musicListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Log.d("tak3","click");
        Toast.makeText(this, position+"번째", Toast.LENGTH_SHORT).show();
        Uri musicURI = Uri.withAppendedPath(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "" + list.get(position).getMusicId());
        //Log.d("tak4","uri: "+musicURI);
        //MediaPlayer mediaPlayer=new MediaPlayer();
        //mediaPlayer=MediaPlayer.create(this,musicURI);
        //mediaPlayer.start();
        Intent intent=new Intent();
        intent.putExtra("musicUri",musicURI);
        setResult(RESULT_OK_MUSIC,intent);
        finish();


    }

    private void getMusicData(){
        //1. 음악 파일인지아닌지, 2. 앨범아이디, 3. 음원명, 4. 가수명, 미디어파일아이디
        String[] projection={
                MediaStore.Audio.Media.IS_MUSIC,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media._ID
        };

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor=contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, //content://로 시작하는 content table uri
                projection, //어떤 colum을 출력한것인지
                null, //어떤 row를 출력할 것인지
                null,
                MediaStore.Audio.Media.TITLE+" ASC" //어떻게 정렬할것인지
        );

        if(cursor!=null){



            while(cursor.moveToNext()){
                try{
                    //Music이라면(mp3라면)
                    //mp3 metadata 이미지 파일의 uri값을 얻어냄
                    //이렇게 얻어낸 데이터를 arraylist에 저장
                    if(cursor.getInt(0)!=0){
                        Uri sArtworkUri=Uri.parse("content://media/external/audio/albumart");
                        Uri uri= ContentUris.withAppendedId(sArtworkUri,Integer.valueOf(cursor.getString(1)));

                        MusicData data=new MusicData();
                        data.setMusicImg(uri);
                        data.setMusicTitle(cursor.getString(2));
                        data.setMusicSinger(cursor.getString(3));
                        data.setAlbumId(cursor.getString(1));
                        data.setMusicId(cursor.getString(4));
                        Log.d("tak3",data.musicTitle);
                        list.add(data);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.setAdapterList(list);
                            }
                        });

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
        cursor.close();

    }
}

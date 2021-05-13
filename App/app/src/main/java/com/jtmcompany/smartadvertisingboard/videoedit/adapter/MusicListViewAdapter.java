package com.jtmcompany.smartadvertisingboard.videoedit.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jtmcompany.smartadvertisingboard.videoedit.model.MusicData;
import com.jtmcompany.smartadvertisingboard.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;

public class MusicListViewAdapter extends ArrayAdapter<MusicData> {
    private ArrayList<MusicData> itemList;
    private Context mContext;
    private ImageLoader loader;

    public MusicListViewAdapter(@NonNull Context context, int resource, ArrayList<MusicData> item) {
        super(context, resource);
        this.mContext=context;
        this.itemList=item;

        //이미지 로더 init
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                .threadPriority(10)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs()
                .build();
        loader= ImageLoader.getInstance();
        loader.init(config);

    }

    public void setAdapterList(ArrayList<MusicData> list){
        itemList=list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.d("tak3","getview");
        ViewHolder holder;
        View v=convertView;
        if(v==null){
            LayoutInflater vi=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=vi.inflate(R.layout.music_list_item,null);

            holder=new ViewHolder();
            holder.img=v.findViewById(R.id.imgMusic);
            holder.title=v.findViewById(R.id.txt_music_title);
            holder.name=v.findViewById(R.id.txt_singer_name);

            v.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        final MusicData data=itemList.get(position);
        if(data!=null){
            if(data.getMusicImg()!=null){
                DisplayImageOptions options=new DisplayImageOptions.Builder()
                        .showImageForEmptyUri(R.mipmap.ic_launcher_round) //Uri주소가 잘못되었을경우(이미지가없을때)
                .resetViewBeforeLoading(false) //로딩전에 뷰를 리셋
                .cacheInMemory(true) //메모리 캐시 사용여부
                .cacheOnDisc(true)// 디스크 캐시 사용여부
                .build();

                loader.displayImage(data.getMusicImg().toString(),holder.img,options);


            }else{
                holder.img.setBackgroundResource(R.mipmap.ic_launcher_round);
            }
            Log.d("tak3","test"+data.getMusicTitle());
            holder.title.setText(data.getMusicTitle());
            holder.name.setText(data.getMusicSinger());

        }
        return v;
    }

    class ViewHolder{
        ImageView img;
        TextView title;
        TextView name;
    }
}

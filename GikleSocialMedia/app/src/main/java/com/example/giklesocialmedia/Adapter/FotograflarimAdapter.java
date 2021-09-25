package com.example.giklesocialmedia.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.giklesocialmedia.Fragment.PostFragment;
import com.example.giklesocialmedia.Model.Post;
import com.example.giklesocialmedia.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FotograflarimAdapter extends RecyclerView.Adapter<FotograflarimAdapter.ViewHolder>{

    private Context mContext;
    private List<Post> postList;

    public FotograflarimAdapter(Context context, List<Post> postList) {
        this.mContext = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fotos_sab,viewGroup,false);
        return new FotograflarimAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Post post = postList.get(i);

        Glide.with(mContext).load(post.getPostiresim()).into(viewHolder.fotoadap_resm);

        viewHolder.fotoadap_resm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("Oneriler", MODE_PRIVATE).edit();
                editor.putString("postid", post.getPostid());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container,
                        new PostFragment()).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView fotoadap_resm;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoadap_resm = itemView.findViewById(R.id.fotossab_resim);
        }
    }
}
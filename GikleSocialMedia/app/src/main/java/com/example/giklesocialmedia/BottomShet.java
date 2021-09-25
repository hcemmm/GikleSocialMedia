package com.example.giklesocialmedia;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;

public class BottomShet extends BottomSheetDialogFragment {


    public BottomShet() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        TextView ayarcikisyap,ayarduzenle,ayarsifre,begeniler;

        View view = inflater.inflate(R.layout.bottomshet_sab,container,false);

        ayarcikisyap = view.findViewById(R.id.bottomsab_cikis);
        ayarduzenle = view.findViewById(R.id.bottomsab_duzenle);
        ayarsifre = view.findViewById(R.id.bottomsab_sifre);
        begeniler = view.findViewById(R.id.bottomsab_begeniler);

        ayarduzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ProfilDuzenActivity.class));

            }
        });

        begeniler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),BegenilerActivity.class));



            }
        });

        ayarsifre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SifreActivity.class));



            }
        });
        
        ayarcikisyap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), BaslangicActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        return view;


    }



}

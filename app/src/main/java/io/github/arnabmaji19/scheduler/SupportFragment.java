package io.github.arnabmaji19.scheduler;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SupportFragment extends Fragment {
    private Context context;
    private final static String SUPPORT_PHONE_NUMBER = "917548046552";
    private final static String SUPPORT_EMAIL = "arnabmaji1999@gmail.com";
    private final static String SUPPORT_SUBJECT = "Scheduler Support";

    SupportFragment(Context context){
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support,container,false);
        View.OnClickListener socialMediaOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tag = Integer.parseInt(view.getTag().toString());
                String URL = "";
                switch (tag){
                    case 1:
                        URL = "https://www.facebook.com/arnabmaji19";
                        break;
                    case 2:
                        URL = "https://www.instagram.com/arnabmaji19";
                        break;
                    case 3:
                        URL = "https://www.github.com/arnabmaji19";
                        break;
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL)); //Calls for browser for action
                startActivity(browserIntent);
            }
        };
        ImageView[] socialMediaIcons = {view.findViewById(R.id.facebook),
                view.findViewById(R.id.instagram),
                view.findViewById(R.id.github)};
        for(ImageView socialMediaIcon : socialMediaIcons){
            socialMediaIcon.setOnClickListener(socialMediaOnClickListener);
        }
        view.findViewById(R.id.whatsapp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //On WhatsApp Click, directs User to Support Number
                try {
                    Intent sendIntent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:" + "" + SUPPORT_PHONE_NUMBER + "?body=" + ""));
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);
                }
                catch (Exception e){ //In case WhatsApp is not installed on user's phone
                    e.printStackTrace();
                    Toast.makeText(context,"Whatsapp not installed!",Toast.LENGTH_LONG).show();
                }
            }
        });
        view.findViewById(R.id.email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //Opens email client for emailing
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{SUPPORT_EMAIL});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,SUPPORT_SUBJECT);
                startActivity(emailIntent);
            }
        });
        return view;
    }
}

package com.alamaanah.nisaarnadiadwala;


import uk.co.chrisjenx.paralloid.Parallaxor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fedorvlasov.lazylist.ImageLoader;
 
public class NotesFragment extends Fragment {
    int fragVal;
    SingleNote note_data;
 
    static NotesFragment init(int val,SingleNote note) {
        NotesFragment truitonFrag = new NotesFragment();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putInt("val", val);
        args.putParcelable("note", note);
        truitonFrag.setArguments(args);
        return truitonFrag;
    }
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragVal = getArguments() != null ? getArguments().getInt("val") : 1;
        note_data=getArguments().getParcelable("note");
      
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.fragment_image, container,
                false);
        Typeface typeface2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/robotolight.ttf");
        Typeface typeface3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/libre.ttf");
        View tv1 = layoutView.findViewById(R.id.tvNotesReadTitle);
        ((TextView) tv1).setText(note_data.title);
        ((TextView) tv1).setTypeface(typeface3);
        View tv2 = layoutView.findViewById(R.id.tvContent);
        ((TextView) tv2).setText(note_data.content);
        ((TextView) tv2).setTypeface(typeface2);
        
        ImageView image= (ImageView)layoutView.findViewById(R.id.ivNotesImage);
        
        String url= "http://216.12.194.26/~alamaana/nisaar/notes_pic/"+note_data.imageid+".jpg";
        ImageLoader imageLoader=new ImageLoader(getActivity());
        imageLoader.DisplayImage(url, image);
        
        ScrollView scrollView = (ScrollView) layoutView.findViewById(R.id.scroll_view);
        if (scrollView instanceof Parallaxor) {
        ((Parallaxor) scrollView).parallaxViewBy(image, 0.5f);
        }

        
        return layoutView;
    }
}
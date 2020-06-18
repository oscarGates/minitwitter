package curso.android.minitwitter;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import curso.android.minitwitter.data.TweetViewModel;

public class BottomModalTweetFragment extends BottomSheetDialogFragment {

    private TweetViewModel tweetViewModel;

    public static BottomModalTweetFragment newInstance() {
        return new BottomModalTweetFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_modal_tweet_fragment, container, false);
        final NavigationView nav = v.findViewById(R.id.navigation_view_bottom_tweet);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.action_delete_tweet){
                    tweetViewModel.deleteTweet();
                    getDialog().dismiss();
                    return true;
                }
                return false;
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tweetViewModel = ViewModelProviders.of(this).get(TweetViewModel.class);
        // TODO: Use the ViewModel
    }

}
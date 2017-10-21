package bhh.youtube.channel.datasource;

import android.content.Context;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bhh.youtube.channel.R;


/**
 * Created by Nazmul Haque on 22/12/16.
 */
public class ExpandableListDataSource {

    /**
     * Returns  data of films
     *
     * @param context
     * @return
     */
    public static Map<String, List<String>> getData(Context context) {
        Map<String, List<String>> expandableListData = new LinkedHashMap<>();
        /**This List Set GroupName In Navigation Drawer*/
        List<String> filmGenres = Arrays.asList(context.getResources().getStringArray(R.array.group_name));
        /**This Individual List Store Child Cannale Names Inside Each Group*/
        List<String> storyChannels = Arrays.asList(context.getResources().getStringArray(R.array.story));
        List<String> cartoonChannels = Arrays.asList(context.getResources().getStringArray(R.array.cartoon));
        List<String> technologyChannels= Arrays.asList(context.getResources().getStringArray(R.array.technology));
        List<String> androidChannels = Arrays.asList(context.getResources().getStringArray(R.array.android));
        List<String> firebaseChannels = Arrays.asList(context.getResources().getStringArray(R.array.firebase));

        /**Finally Set The Group name and All Child Item in expendableListData variable */

        expandableListData.put(filmGenres.get(0), storyChannels);
        expandableListData.put(filmGenres.get(1),  cartoonChannels );
        expandableListData.put(filmGenres.get(2),  technologyChannels);
        expandableListData.put(filmGenres.get(3), androidChannels );
        expandableListData.put(filmGenres.get(4),firebaseChannels );

        /** Return all binned value */
        return expandableListData;
    }
}

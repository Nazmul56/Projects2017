package learn2crack.customgridview;


import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends Activity {
	GridView grid;
	String[] web = {
		    "Google",
			"Github",
			"Instagram",
			"Facebook",
			"Flickr",
			"Pinterest",
			"Quora",
			"Twitter",
			"Vimeo",
			"WordPress",
			"Youtube",
			"Stumbleupon",
			"SoundCloud",
			"Reddit",
			"Blogger"
			
	} ;
	int[] imageId = {
			R.drawable.image1,
			R.drawable.image2,
			R.drawable.image3,
			R.drawable.image4,
			R.drawable.image5,
			R.drawable.image6,
			R.drawable.image7,
			R.drawable.image8,
			R.drawable.image9,
			R.drawable.image10,
			R.drawable.image11,
			R.drawable.image12,
			R.drawable.image13,
			R.drawable.image14,
			R.drawable.image15
			
			
			
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		CustomGrid adapter = new CustomGrid(MainActivity.this, web, imageId);
		grid=(GridView)findViewById(R.id.grid);
				grid.setAdapter(adapter);
				grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		            @Override
		            public void onItemClick(AdapterView<?> parent, View view,
		                                    int position, long id) {
		                Toast.makeText(MainActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();

		            }
		        });

	}



}

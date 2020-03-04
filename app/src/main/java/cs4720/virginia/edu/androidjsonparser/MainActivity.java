package cs4720.virginia.edu.androidjsonparser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;


public class MainActivity extends AppCompatActivity {

    EditText courseNum;
    Button searchButton;
    TextView infoList;
    RequestQueue requestQueue;

    String baseUrl = "http://stardock.cs.virginia.edu/louslist/Courses/view/CS/";
//    String baseUrl = "devhub.virginia.edu/api/API_KEY/buildings/";
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.courseNum = (EditText) findViewById(R.id.course_num);
        this.searchButton = (Button) findViewById(R.id.search_button);
        this.infoList = (TextView) findViewById(R.id.info_list);
        this.infoList.setMovementMethod(new ScrollingMovementMethod());
        requestQueue = Volley.newRequestQueue(this);
    }

    private void clearRepoList() {
        this.infoList.setText("");
    }


    private void addToRepoList(String repoName) {
        String strRow = repoName;
        String currentText = infoList.getText().toString();
        this.infoList.setText(currentText + "\n\n" + strRow);
    }

    private void setRepoListText(String str) {
        this.infoList.setText(str);
    }

    private void getRepoList(String username) {
        this.url = this.baseUrl + username + "?json";
//        this.url = this.baseUrl + username ;

       JsonArrayRequest arrReq = new JsonArrayRequest(Request.Method.GET, url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() > 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject jsonObj = response.getJSONObject(i);
                                    String repoName = jsonObj.toString();
                                    addToRepoList(repoName);
                                } catch (JSONException e) {
                                    Log.e("Volley", "Invalid JSON Object.");
                                }

                            }
                        } else {
                            setRepoListText("No course found.");
//                            setRepoListText("No building found.");
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        setRepoListText("Error while calling REST API");
                        Log.e("Volley", error.toString());
                    }
                }
        );
        requestQueue.add(arrReq);
    }
    public void getReposClicked(View v) {
        clearRepoList();
        getRepoList(courseNum.getText().toString());
    }
}

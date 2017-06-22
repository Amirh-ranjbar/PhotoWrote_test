package ranjbar.amirh.photowrote_final;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    private Button openEditorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        openEditorButton = (Button) findViewById(R.id.openEditorButton);
        openEditorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEditor(view);
            }
        });
    }

    public void onEditor(View v)
    {
        Intent i = new Intent(this , EditorActivity.class);
//        i.putExtra(Fragment_MODE, );
        startActivity(i);
    }

}

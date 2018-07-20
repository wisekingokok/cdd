package com.chewuwuyou.app;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chewuwuyou.app.transition_view.activity.SimpleActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SimpleInstrumentedTest {
    private TextView nameTV;
    private Button showBtn;
	
	
	
	

    @Rule
    public ActivityTestRule<SimpleActivity> activityRule=new ActivityTestRule<>(SimpleActivity.class);

    @Test
    public void setText() throws Exception {

        final SimpleActivity activity = activityRule.getActivity();
        nameTV  = (TextView) activity.findViewById(R.id.tv_name);
        showBtn = (Button) activity.findViewById(R.id.btn_show);


        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                nameTV.setText("Has set value");

                showBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nameTV.setText("onclick");
                    }
                });
                showBtn.performClick();
            }
        });

        Thread.sleep(7000L);
    }
}

package com.chewuwuyou.app.transition_view.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chewuwuyou.app.R;

public class LoadingFooter {

	private View mLoadingFooter;
	private TextView mLoadingText;
	private State mState = State.Idle;

	public static enum State {
		Idle, TheEnd, Loading
	}

	public LoadingFooter(Context context) {
		mLoadingFooter = LayoutInflater.from(context).inflate(
				R.layout.loading_footer, null);
		mLoadingFooter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
					
			}
		});
		mLoadingText = (TextView) mLoadingFooter.findViewById(R.id.textView);

		setState(State.Idle);
	}

	public View getView() {
		return mLoadingFooter;
	}

	public State getState() {
		return mState;
	}

	public void setState(final State state, long delay) {
		mLoadingFooter.postDelayed(new Runnable() {
			@Override
			public void run() {
				setState(state);
			}
		}, delay);
	}

	public void setState(State status) {
		if (mState == status) {
			return;
		}
		mState = status;

		switch (status) {
		case Loading:
			mLoadingFooter.setVisibility(View.VISIBLE);
			mLoadingText.setText("加载中……");

			break;
		case TheEnd:

			mLoadingText.setText("加载完毕");
			mLoadingFooter.setVisibility(View.VISIBLE);
			break;
		default:

			mLoadingFooter.setVisibility(View.GONE);
			break;
		}
	}
}

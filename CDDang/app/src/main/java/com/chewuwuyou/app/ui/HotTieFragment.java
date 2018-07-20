package com.chewuwuyou.app.ui;


//public final class HotTieFragment extends BaseFragment implements OnRefreshListener2<ListView> {
//	private HotTieActivity mActivity;
//	private View mContentView;
//	private List<HotTieItem> mData;
//	private HotTieAdapter mAdapter;
//	private PullToRefreshListView mPullToRefreshListView;
//	private TextView mEmptyTV;// 没有数据时提示用户
//	private boolean mIsRefreshing = false;// 上拉下拉要用
//	private int mCurcor;// 分页要用
//	private int mBanId = 0;
//
//	public HotTieFragment() {
//		super();
//	}
//
//	public HotTieFragment(Activity activity, int banId) {
//		this.mActivity = (HotTieActivity) activity;
//		this.mBanId = banId;
//	}
//
//	@Override
//	public void onAttach(Activity activity) {
//		// TODO Auto-generated method stub
//		super.onAttach(activity);
//	}
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		this.mContentView = LayoutInflater.from(mActivity).inflate(R.layout.hot_ties_layout, null);
//
//		initView();
//		initData();
//		initEvent();
//		getAllHotTie(true);
//		return mContentView;
//	}
//
//	@Override
//	public void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//	}
//
//	@Override
//	protected void initView() {
//		// TODO Auto-generated method stub
//		mPullToRefreshListView = (PullToRefreshListView) mContentView.findViewById(R.id.tie_list);
//		mEmptyTV = (TextView) mContentView.findViewById(R.id.tie_empty_tv);
//	}
//
//	@Override
//	protected void initData() {
//		// TODO Auto-generated method stub
//		mData = new ArrayList<HotTieItem>();
//		mAdapter = new HotTieAdapter(mActivity, mData);
//		mPullToRefreshListView.setAdapter(mAdapter);
//		mEmptyTV.setVisibility(View.GONE);
//	}
//
//	@Override
//	protected void initEvent() {
//		// TODO Auto-generated method stub
//		mPullToRefreshListView.setOnRefreshListener(this);
//	}
//
//	@Override
//	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//		// TODO Auto-generated method stub
//		String label = DateUtils.formatDateTime(mActivity.getApplicationContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
//				| DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//
//		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
//
//		if (!mIsRefreshing) {
//			mIsRefreshing = true;
//			getAllHotTie(true);
//		} else {
//			mPullToRefreshListView.onRefreshComplete();
//		}
//	}
//
//	@Override
//	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//		// TODO Auto-generated method stub
//		if (!mIsRefreshing) {
//			mIsRefreshing = true;
//			getAllHotTie(false);
//		} else {
//			mPullToRefreshListView.onRefreshComplete();
//		}
//	}
//
//	// 获得所有帖子
//	private void getAllHotTie(final boolean refresh) {
//		if (refresh) {
//			mCurcor = 0;
//		}
//		mPullToRefreshListView.setRefreshing();
//		AjaxParams params = new AjaxParams();
//		params.put("banId", String.valueOf(mBanId));
//		params.put("start", String.valueOf(mCurcor));
//		requestNet(new Handler() {
//
//			@Override
//			public void handleMessage(Message msg) {
//				// TODO Auto-generated method stub
//				super.handleMessage(msg);
//				switch (msg.what) {
//				case Constant.NET_DATA_SUCCESS:
//					mIsRefreshing = false;
//					mPullToRefreshListView.onRefreshComplete();
//
//					try {
//						JSONObject data = new JSONObject(msg.obj.toString());
//
//						List<HotTieItem> response = HotTieItem.parseList(data.getString("tie").toString());
//
//						if (response != null) {
//							if (refresh) {
//								mPullToRefreshListView.onLoadMore();
//								mData.clear();
//							}
//							mData.addAll(response);
//							for(int i = 0; i < mData.size(); i++) {
//								if(i == 0) {
//									mData.get(i).setType("1");
//								} else {
//									mData.get(i).setType("0");
//								}
//							}
//							// mEmptyText.setVisibility(View.GONE);
//							mAdapter.notifyDataSetChanged();
//							mCurcor = mData.size();
//							if (response.size() < Constant.TIE_PAGE_SIZE) {
//								mPullToRefreshListView.onLoadComplete();
//							}
//						} else {
//							if (refresh) {
//								mEmptyTV.setVisibility(View.VISIBLE);
//							}
//						}
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					break;
//
//				default:
//					mIsRefreshing = false;
//					if (refresh) {
//						mEmptyTV.setVisibility(View.VISIBLE);
//					}
//					break;
//				}
//			}
//
//		}, params, NetworkUtil.ALL_TIE, false, 1);
//	}
//
//	@Override
//	public void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//	}
//
//	@Override
//	public void onDestroyView() {
//		// TODO Auto-generated method stub
//		super.onDestroyView();
//	}
//
//	@Override
//	public void onDetach() {
//		// TODO Auto-generated method stub
//		super.onDetach();
//	}
//
//	// public static HotTieFragment newInstance(int banId) {
//	//
//	// HotTieFragment f = new HotTieFragment();
//	// Bundle bundle = new Bundle();
//	// bundle.putInt("ban_id", banId);
//	// f.setArguments(bundle);
//	//
//	// return f;
//	// }
//	
//	public class HotTieAdapter extends BaseAdapter implements OnClickListener {
//
//		private Context mContext;
//		private List<HotTieItem> mData;
//		private LayoutInflater mInflater;
//		private ImageLoader mImageLoader;
//		private static final int FLOW = 0;
//		private static final int OTHER = FLOW + 1;
//
//		public HotTieAdapter(Context context, List<HotTieItem> data) {
//
//			this.mContext = context;
//			this.mData = data;
//			this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			this.mImageLoader = ImageLoader.getInstance();
//
//		}
//
//		@Override
//		public int getCount() {
//			return mData.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return mData.get(position);
//		}
////
////		@Override
////		public int getItemViewType(int position) {
////			HotTieItem item = mData.get(position);
////			if (item.getType().equals("1"))
////				return FLOW;
////			else
////				return OTHER;
////		}
//
//		@Override
//		public int getViewTypeCount() {
//			return 2;
//		}
//		
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
////			int type = getItemViewType(position);
//			//后面再打开
////			switch (type) {
////			case FLOW:
////				convertView = createFlowView(convertView, position);
////				break;
////			default:
////				convertView = createView(convertView, position);
////				break;
////			}
//			convertView = createView(convertView, position);
//			return convertView;
//		}
//		//后面再打开
////		public View createFlowView(View convertView, int position) {
////			HotTieViewFlowViewCache viewCache = null;
////			if (convertView == null) {
////				convertView = mInflater.inflate(R.layout.hot_tie_view_flow, null);
////				viewCache = new HotTieViewFlowViewCache(convertView);
////				convertView.setTag(viewCache);
////			} else {
////				viewCache = (HotTieViewFlowViewCache) convertView.getTag();
////			}
////			final Integer position_integer = Integer.valueOf(position);
////			final HotTieItem item= mData.get(position);
////			if (item != null && item.getTus() != null && item.getTus().size() > 0) {
////				List<TieTuItem> tus = item.getTus();
////				final List<String> tuUrls = new ArrayList<String>();
////
////				for (TieTuItem tu : tus) {
////					tuUrls.add(tu.getUrl());
////				}
////				viewCache.getmViewFlow().setAdapter(new ImageAdapter(mContext, tuUrls), 0);
////				viewCache.getmViewFlow().setFlowIndicator(viewCache.getmCircleFlowIndicator());
////				viewCache.getmViewFlow().setTimeSpan(4500);
////				viewCache.getmViewFlow().setSelection(3*1000);	//设置初始位置
////				viewCache.getmViewFlow().startAutoFlowTimer(); 
////			}
////			return convertView;
////		}
//		
//		public View createView(View convertView, int position) {
//			HotTieItemViewCache viewCache = null;
//			if (convertView == null) {
//				convertView = mInflater.inflate(R.layout.hot_tie_item, null);
//				viewCache = new HotTieItemViewCache(convertView);
//				convertView.setTag(viewCache);
//			} else {
//				viewCache = (HotTieItemViewCache) convertView.getTag();
//			}
//			final Integer position_integer = Integer.valueOf(position);
//			final HotTieItem item= mData.get(position);
//				if (item != null) {
//					if (!TextUtils.isEmpty(item.getUrl())) {
//						mImageLoader.displayImage(ServerUtils.getServerIP(item.getUrl()), viewCache.getmHotTieItemAvatarIV(), new DisplayImageOptions.Builder()
//								.cacheInMemory(true).cacheOnDisc(true).showImageOnFail(R.drawable.btn_sjmarg).showImageForEmptyUri(R.drawable.btn_sjmarg)
//								.displayer(new RoundedBitmapDisplayer(10)).build());
//					}
//					viewCache.getmHotTieItemNameTV().setText(CarFriendQuanUtils.showCarFriendName(item));
//					viewCache.getmHotTieItemContentTV().setText(item.getContent());
//					if (item.getTucnt() > 0) {
//						viewCache.getmHotTieItemTuLL().setVisibility(View.VISIBLE);
//					} else {
//						viewCache.getmHotTieItemTuLL().setVisibility(View.GONE);
//					}
//
//					if (item.getTus().size() > 0) {
//						viewCache.getmHotTieItemTuLL().setVisibility(View.VISIBLE);
//					} else {
//						viewCache.getmHotTieItemTuLL().setVisibility(View.GONE);
//					}
//					List<TieTuItem> tus = item.getTus();
//					final List<String> tuUrls = new ArrayList<String>();
//
//					for (TieTuItem tu : tus) {
//						tuUrls.add(tu.getUrl());
//					}
//					final FrameLayout tusFL = viewCache.getmHotTieItemTuLL();
//					tusFL.removeAllViews();
//					for (int i = 0; i < tus.size(); i++) {
//						final ImageView tuIV = new ImageView(mContext);
//						tuIV.setAdjustViewBounds(true);
//						tuIV.setScaleType(ScaleType.FIT_XY);
//						LinearLayout.LayoutParams iv_params = new LinearLayout.LayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//								LinearLayout.LayoutParams.WRAP_CONTENT));
//						tuIV.setLayoutParams(iv_params);
//						tuIV.setImageResource(R.drawable.image_default);
//						String url = tus.get(i).getUrl();
//						ImageUtils.displayImage(url, tuIV, 0, mBgTuWidth, mBgTuHeight, ScalingLogic.CROP);
//						tusFL.addView(tuIV);
//					}
//
//					viewCache.getmHotTieItemTuLL().setOnClickListener(this);
//					viewCache.getmHotTieItemAvatarIV().setOnClickListener(this);
//					Date date = null;
//					try {
//						date = new SimpleDateFormat("yyyyMMddHHmmss").parse(item.getPublishTime());
//					} catch (ParseException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					viewCache.getmHotTieItemDateTV().setText(new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(date));
//					// 后面要改的
//					viewCache.getmHotTieItemZanIV().setImageResource((item.getZaned()).equals("1") ? R.drawable.liked : R.drawable.like);
//					viewCache.getmHotTieItemZanTV().setText(mContext.getResources().getString(R.string.item_zan_ping_cnt, item.getTiezancnt()));
//
//					viewCache.getmHotTieItemPingIV().setImageResource((item.getZaned()).equals("1") ? R.drawable.reply : R.drawable.reply);
//					viewCache.getmHotTieItemPingTV().setText(mContext.getResources().getString(R.string.item_zan_ping_cnt, item.getTiepingcnt()));
//				}
//			viewCache.getmHotTieItemTuLL().setTag(position_integer);
//			return convertView;
//		}
//
//		public void updateData(List<HotTieItem> data) {
//			this.mData = data;
//			this.notifyDataSetChanged();
//		}
//
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			Object tag = v.getTag();
//			Integer position_integer = null;
//			Intent intent = null;
//			if (tag instanceof Integer)
//				position_integer = (Integer) tag;
//			final HotTieItem item= (HotTieItem) mData.get(position_integer.intValue());
//			switch (v.getId()) {
//			case R.id.hot_tie_item_avatar_iv:
//				// 进入到个人详情中
//				intent = new Intent(mContext, PersonalHomeActivity.class);
//				intent.putExtra("userId", item.getUserId());
//				mContext.startActivity(intent);
//				break;
//			case R.id.hot_tie_item_tu_fl:
//				// 显示大图
//				intent = new Intent(mContext, HotTieDetailActivity.class);
//				intent.putExtra("id", item.getId());
//				intent.putExtra("ziji", item.getZiji());
//				mContext.startActivity(intent);
//				break;
//			default:
//				break;
//			}
//		}
//	}
//
//	public class ImageAdapter extends BaseAdapter {
//
//		private LayoutInflater mInflater;
//		private List<String> mData;
//
//		public ImageAdapter(Context context, List<String> data) {
//			this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			this.mData = data;
//		}
//
//		@Override
//		public int getCount() {
//			return mData.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return mData.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			if (convertView == null) {
//				convertView = mInflater.inflate(R.layout.capture_item, null);
//			}
//			ImageUtils.displayImage(mData.get(position), (ImageView) convertView.findViewById(R.id.capture_item_iv), 0, mBgTuWidth, mBgTuHeight,
//					ScalingLogic.CROP);
//			return convertView;
//		}
//	}
//}

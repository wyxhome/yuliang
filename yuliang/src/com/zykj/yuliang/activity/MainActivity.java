package com.zykj.yuliang.activity;

import java.io.File;

import org.apache.http.Header;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zykj.yuliang.BaseActivity;
import com.zykj.yuliang.BaseApp;
import com.zykj.yuliang.R;
import com.zykj.yuliang.http.HttpErrorHandler;
import com.zykj.yuliang.http.HttpUtils;
import com.zykj.yuliang.http.UrlContants;
import com.zykj.yuliang.utils.CircleImageView;
import com.zykj.yuliang.utils.StringUtil;
import com.zykj.yuliang.utils.Tools;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements View.OnClickListener {

	private Button btn_detail;
	private Button btn_more;
	private CircleImageView iv_header;
	private LinearLayout ll_makemaoney;// 赚钱
	private LinearLayout ll_apprentice;// 收徒
	private LinearLayout ll_duobao, ll_youhuiquan, ll_shengqian, ll_duihuan;// 一元夺宝,优惠券,省钱,兑换
	private Intent intent;
	private String userId,points;
	private TextView tv_yue,tv_todaymoney,tv_today_tudi;//余额、今日收入、今日收徒
	private RequestParams params;
	
	 protected static String url =
	 "http://gw.api.tbsandbox.com/router/rest";//沙箱环境调用地址
	     //正式环境需要设置为:http://gw.api.taobao.com/router/rest
//	     protected static String appkey = "test";
//	     protected static String appSecret = "test";
	 protected static String sessionkey = "test"; //如 沙箱测试帐号sandbox_c_1授权后得到的sessionkey
	 protected static String AppKey="23286995";
	 protected static String AppSecret="bb79863309952a3deff3438c81ea3dbd";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);  
//		userId=getIntent().getStringExtra("userId");
		initViews();
		requestData();
		
		initClick();
		initEvents();

	}
	

	@Override
	protected void onResume() {
		super.onResume();
		requestData();
	}


	protected void initClick() {
		btn_detail.setOnClickListener(this);
		btn_more.setOnClickListener(this);
		ll_makemaoney.setOnClickListener(this);
		ll_apprentice.setOnClickListener(this);
		ll_duobao.setOnClickListener(this);
		ll_youhuiquan.setOnClickListener(this);
		ll_shengqian.setOnClickListener(this);
		ll_duihuan.setOnClickListener(this);
//		iv_header.setOnClickListener(this);
	}

	protected void initViews() {
		tv_todaymoney=(TextView) findViewById(R.id.tv_todaymoney);//今日收入
		tv_today_tudi=(TextView) findViewById(R.id.tv_today_tudi_num);//今日收徒
		btn_detail = (Button) findViewById(R.id.btn_detail);//明细
		btn_more = (Button) findViewById(R.id.more);//更多
		ll_makemaoney = (LinearLayout) findViewById(R.id.ll_zhuanqian);//赚钱
		ll_apprentice = (LinearLayout) findViewById(R.id.ll_shoutu);//收徒
		ll_duobao = (LinearLayout) findViewById(R.id.ll_yiyuanduobao);//一元夺宝
		ll_youhuiquan = (LinearLayout) findViewById(R.id.ll_youhuiquan);//优惠券
		ll_shengqian = (LinearLayout) findViewById(R.id.ll_shengqian);//省钱
		ll_duihuan = (LinearLayout) findViewById(R.id.ll_duihuan);//兑换
		
		tv_yue=(TextView) findViewById(R.id.tv_yue);//我的余额
		
		iv_header=(CircleImageView) findViewById(R.id.iv_header);//我的头像
		
		
	}

	protected void initEvents() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_header:
			startActivityForResult(new Intent(MainActivity.this, ZiLiaoActivity.class), 11);
			break;
		case R.id.btn_detail://明细
			startActivity(new Intent(MainActivity.this, IncomeDetailActivity.class));
			break;
		case R.id.more://更多
			startActivity(new Intent(MainActivity.this, MoreActivity.class));
			break;
		case R.id.ll_zhuanqian://赚钱
			startActivity(new Intent(MainActivity.this, MakeMoneyActivity.class));
			break;
		case R.id.ll_shoutu://收徒
			startActivity(new Intent(MainActivity.this, ApprenticeActivity.class));
			break;
		case R.id.ll_yiyuanduobao://一元夺宝
//			startActivity(new Intent(MainActivity.this, ApprenticeActivity.class));
			break;
		case R.id.ll_youhuiquan:
			/**
			 * 跳转兑吧
			 */
			params=new RequestParams();
			params.put("uid", BaseApp.getModel().getUserid());
			params.put("points", points);//-=============@@@@@@@@@@@@@@@=================此处临时传积分,用该传元宝即我的余额
			HttpUtils.getLoginUrl(new HttpErrorHandler() {
				@Override
				public void onRecevieSuccess(JSONObject json) {
					String url = json.getJSONObject(UrlContants.jsonData).getString("url");
					Intent intent = new Intent().setClass(MainActivity.this,
							CreditActivity.class);
					intent.putExtra("navColor", "#50bf83");// 配置导航条的背景颜色，请用#ffffff长格式。
					intent.putExtra("titleColor", "#ffffff");// 配置导航条标题的颜色，请用#ffffff长格式。
					intent.putExtra("url", url);// 配置自动登陆地址，每次需服务端动态生成。
					startActivityForResult(intent, 11);
				}
			}, params);
			break;
		case R.id.ll_shengqian://省钱
			//AppKey     AppSecret
//			TaobaoClient client = new DefaultTaobaoClient(url, AppKey, AppSecret);//实例化TopClient类
//	         UserSellerGetRequest req = new UserSellerGetRequest();//实例化具体API对应的Request类
//	         req.setFields("nick,user_id,type");
//	         //req.setNick("sandbox_c_1");
//	         UserSellerGetResponse response;
//	         try {
//	             response = client.execute(req,sessionkey); //执行API请求并打印结果
//	             System.out.println("result:"+response.getBody());
//	          
//	         } catch (ApiException e) {
//	         // deal error
//	         }
			
			break;
		case R.id.ll_duihuan://兑换
			startActivity(new Intent(MainActivity.this, DuiHuanActivity.class));
			break;
		default:
			break;
		}

	}
	/***
	 * 请求服务器获取积分
	 * 从本地获取头像和我的余额
	 */
	private void requestData() {
		
		tv_yue.setText(BaseApp.getModel().getMoney());//此处暂时为积分,待改为getMoney()
		String avatar=BaseApp.getModel().getAvatar();
		ImageLoader.getInstance().displayImage(StringUtil.toString(UrlContants.IMAGE_URL+avatar, "http://"), iv_header);
		/**
		 * 获取用户积分
		 */
		params=new RequestParams();
		params.put("deviceId", BaseApp.getModel().getDeviceId());
		HttpUtils.getPoints(new HttpErrorHandler() {
			
			@Override
			public void onRecevieSuccess(JSONObject json) {
				points = json.getJSONObject(UrlContants.jsonData).getString("points");
			}
		}, params);	
		
		/**
		 * 今日收入
		 */
		params=new RequestParams();
		params.put("userid", BaseApp.getModel().getUserid());
		HttpUtils.getTodayIncome(new HttpErrorHandler() {
			
			@Override
			public void onRecevieSuccess(JSONObject json) {
				String income = json.getString("datas");
				tv_todaymoney.setText("今日收入："+income+"元");
			}
		}, params);
		
		/**
		 * 今日收徒
		 */
		params=new RequestParams();
		params.put("deviceId", BaseApp.getModel().getDeviceId());
		HttpUtils.getTodayChildren(new HttpErrorHandler() {
	
			
			@Override
			public void onRecevieSuccess(JSONObject json) {
				String children = json.getString("datas");
				tv_today_tudi.setText("今日收徒："+children+"个");				
			}
		}, params);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 11:
			requestData();
			break;
		default:
			break;
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onDestroy() {
		Tools.Log("当前tabActivity退出");
		super.onDestroy();
	}
	/**
	 * 点击返回键退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {// 返回按钮
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("提示")
					.setMessage("您确定退出当前应用")
					.setNegativeButton("取消", new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					})
					.setPositiveButton("确定", new OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							try {
								// 判断是否存在临时创建的文件
								File temp_file = new File(Environment
										.getExternalStorageDirectory()
										+ File.separator
										+ BaseApp.FILE_DIR);
								Tools.Log("文件是否存在：" + temp_file.exists());
								if (temp_file.exists()) {
									File[] file_detail = temp_file.listFiles();
									for (File file_del : file_detail) {
										file_del.delete();
									}
									temp_file.delete();
								}

							} catch (Exception e) {

							}
							System.exit(0);
						}
					})
					.setOnCancelListener(
							new DialogInterface.OnCancelListener() {
								public void onCancel(DialogInterface dialog) {
									dialog.dismiss();
								}

							}).show();
		}

		return super.onKeyDown(keyCode, event);
	}
}

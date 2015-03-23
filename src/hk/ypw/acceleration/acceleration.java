package hk.ypw.acceleration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ypw.acceleration.R;

import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class acceleration extends Activity {
	/**
	 * @author ypw
	 * 2014-09
	 */
	long uiId;
	
	int accdelay = 1000,gpsdelay=3000,gyrodelay=1000,magdelay=1000,oridelay=1000;
	String filepath = Environment.getExternalStorageDirectory()+"/Acceleration/output.txt";
	/**
	 * (acc������ٶ�,gps����GPS,gyro����������,ori�����򴫸���)
	 * ���������delay�ֱ�����������������ٶ�,Ȼ��filepath������Ǳ�����ļ�·��
	 */
	
	long nowtimeacc=0,lasttimeacc=SystemClock.elapsedRealtime();
	long nowtimegps=0,lasttimegps=SystemClock.elapsedRealtime();
	long nowtimegyro=0,lasttimegyro=SystemClock.elapsedRealtime();
	long nowtimemag=0,lasttimemag=SystemClock.elapsedRealtime();
	long nowtimeori=0,lasttimeori=SystemClock.elapsedRealtime();

	/**
	 * ��Щ�����ӳٴ���Ĳ���,����ʱ����Ĳ��������ӳٴ���
	 */
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceleration);
    	uiId=Thread.currentThread().getId();
    	/**
    	 * ��ʼ��layout
    	 * ��ȡUI�߳�ID,�����������߳��е���show�������³������
    	 */
    	
    	tv_satellites = (TextView)this.findViewById(R.id.textview_gps2);
		
    	
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){}
        else 
        	{
        	show("�Ҳ���SD��");
        	finish();
        	}
        /**
         * ���SD���Ƿ����,��������ھ�ֱ���˳�,��Ϊ����ɼ�����Ϣ���뱣�浽SD����
         */
        
        
        String SDPATH=Environment.getExternalStorageDirectory()+"/";
        File dir=new File(SDPATH+"Acceleration");
        dir.mkdir();
        /**
         * ����ļ���Acceleration�����ھʹ���һ��
         */
        
        
        File file=new File(filepath);
        try {
			FileWriter writer = new FileWriter(file,true);
			writer.write(System.currentTimeMillis()+ ":System start." +"\r\n");
			writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        /**
         * дһ����ǰʱ�����System start.
         * ����:1411137516765:System start.
         */
        
    	SensorManager sensorManager;    	
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        Sensor accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(new SensorEventListener() {
			public void onSensorChanged(SensorEvent event) {
				  nowtimeacc=SystemClock.elapsedRealtime();
		        /**
		         * ����һ�����ٶ�sensor,����sensor�ı���¼�
		         * Ȼ��ȡ��x,y,z,���д���֮����ʾ����Ļ��
		         */
				  float x=event.values[0],y=event.values[1],z=event.values[2];
				  String output = "";String output2 = "";
				  output+="AccX=\t"+String.valueOf(x)+"\r\n";
				  output+="AccY=\t"+String.valueOf(y)+"\r\n";
				  output+="AccZ=\t"+String.valueOf(z);

				  if(nowtimeacc-lasttimeacc>=accdelay)
				  {
					  /**
					   * ȡ�����ڵ�ʱ�����һ��д���ļ���ʱ����ʱ���,�������accdelay��ô�����¸�ֵ��д�����ݵ��ļ���
					   * ��ʽ����:ʱ���,acc,x,y,z
					   * ����:1411137519350,acc,0.08244324,0.1593628,9.890015
					   */
					  lasttimeacc=nowtimeacc;
					  	output2+=System.currentTimeMillis()+",acc,"+x+","+y+","+z+"\r\n";
					  	appendText(filepath,output2);
				  }
				  
				  TextView outpuTextView =(TextView)findViewById(R.id.textview_acc);
				  outpuTextView.setText(output);
			}
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}
		}, accSensor, SensorManager.SENSOR_DELAY_GAME);
        /**
         * ע��Ϊ��Ϸģʽ(SENSOR_DELAY_GAME),�����Ĳ����ٶ�������
         */
        
    	Sensor gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(new SensorEventListener() {
			public void onSensorChanged(SensorEvent event) {
				/**
				 * �����Ǻͼ��ٶȴ�������,������һ����д��
				 */
				  nowtimegyro=SystemClock.elapsedRealtime();
				  float x=event.values[0],y=event.values[1],z=event.values[2];
				  String output = "";String output2 = "";
				  output+="GyroX=\t"+String.valueOf(x)+"\r\n";
				  output+="GyroY=\t"+String.valueOf(y)+"\r\n";
				  output+="GyroZ=\t"+String.valueOf(z)+"\r\n";

				  if(nowtimegyro-lasttimegyro>=gyrodelay)
				  {
					  lasttimegyro=nowtimegyro;
					  	output2+=System.currentTimeMillis()+",gyro,"+x+","+y+","+z+"\r\n";
					  	/**
					  	 * ��ʽ:ʱ���,gyro,x,y,z
					  	 * ����:1411137519351,gyro,-0.0019989014,0.0012512207,-0.0011444092
					  	 * ���ڴ󲿷�ʱ�����Ƕ�û����ת,���ٶ�Ϊ0,����ͨ�������ǵ����ݶ��ǳ�С
					  	 */
					  	appendText(filepath,output2);
				  }
				  
				  TextView outpuTextView =(TextView)findViewById(R.id.textview_gyro);
				  outpuTextView.setText(output);
				  
			}
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}
		}, gyroSensor, SensorManager.SENSOR_DELAY_GAME);
        
        Sensor magSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(new SensorEventListener() {
			public void onSensorChanged(SensorEvent event) {
				/**
				 * �ų��ͼ��ٶȴ�������,������һ����д��
				 */
				  nowtimeori=SystemClock.elapsedRealtime();
				  
				  float x=event.values[0],y=event.values[1],z=event.values[2];
				  String output = "";String output2 = "";
				  output+="MagX=\t"+String.valueOf(x)+"\r\n";
				  output+="MagY=\t"+String.valueOf(y)+"\r\n";
				  output+="MagZ=\t"+String.valueOf(z)+"\r\n";

				  if(nowtimemag-lasttimemag>=magdelay)
				  {
					  lasttimemag=nowtimemag;
					  	output2+=System.currentTimeMillis()+",mag,"+x+","+y+","+z+"\r\n";
					  	appendText(filepath,output2);
				  }
				  
				  TextView outpuTextView =(TextView)findViewById(R.id.textview_mag);
				  outpuTextView.setText(output);
				  
			}
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}
		}, magSensor, SensorManager.SENSOR_DELAY_GAME);
        
        Sensor oriSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(new SensorEventListener() {
			public void onSensorChanged(SensorEvent event) {
				/**
				 * ���򴫸���,����Ҳ��һ����д��
				 */
				  nowtimemag=SystemClock.elapsedRealtime();
				  
				  float x=event.values[0],y=event.values[1],z=event.values[2];
				  String output = "";String output2 = "";
				  output+="OriX=\t"+String.valueOf(x)+"\r\n";
				  output+="OriY=\t"+String.valueOf(y)+"\r\n";
				  output+="OriZ=\t"+String.valueOf(z)+"\r\n";

				  if(nowtimeori-lasttimeori>=oridelay)
				  {
					  lasttimeori=nowtimeori;
					  	output2+=System.currentTimeMillis()+",ori,"+x+","+y+","+z+"\r\n";
					  	appendText(filepath,output2);
				  }
				  
				  TextView outpuTextView =(TextView)findViewById(R.id.textview_ori);
				  outpuTextView.setText(output);
				  
			}
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}
		}, oriSensor, SensorManager.SENSOR_DELAY_GAME);
        
        
        
        openGPSSetting();
        /**
         * ���������һ���������ж�GPS�Ƿ񱻴�,���û�д����Ǿ͵����������û����ֶ���GPS
         */
        
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {  
            public void onLocationChanged(Location location) {  
            	/**
            	 * ��������ע����һ����λ�ñ仯��ʱ����¼�
            	 */
            	TextView gpsTextView = (TextView)findViewById(R.id.textview_gps);
            	
            	/**
            	 * ��������˾�γ��,���ʱ�䳬����gpsdelay�����ļ������GPS����
            	 * ʱ���,gps,����,γ��
            	 * ����:1411137497047,gps,116.158073,39.71972
            	 */
            	nowtimegps=SystemClock.elapsedRealtime();
            	if (location != null) {gpsTextView.setText("����(Longitude):" + location.getLongitude() + "\r\nγ��(Latitude):"  
                        + location.getLatitude());
            	if(nowtimegps-lasttimegps>gpsdelay)
            	{
            		lasttimegps=nowtimegps;
				  	String output=System.currentTimeMillis()+",gps,"+location.getLongitude()
				  			+","+location.getLatitude()+"\r\n";
				  	appendText(filepath,output);
            	}
            	
            	// Location��ķ�����  
                // getAccuracy():���ȣ�ACCESS_FINE_LOCATION��ACCESS_COARSE_LOCATION��  
                // getAltitude():����  
                // getBearing():��λ���ж�����  
                // getLatitude():γ��  
                // getLongitude():����  
                // getProvider():λ���ṩ�ߣ�GPS��NETWORK��  
                // getSpeed():�ٶ�  
                // getTime():ʱ��
            	}else gpsTextView.setText("�޷���ȡ��GPS��λ����Ϣ");
            }  
           
            public void onStatusChanged(String provider, int status, Bundle extras) {}  
           
            public void onProviderEnabled(String provider) {}  
           
            public void onProviderDisabled(String provider) {}  
          };
        
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        /**
         * ����������֪֮ͨ�����Сʱ��������λ΢�룩����������֪֮ͨ����С�ľ���仯����λ�ף���
         */
        locationManager.addGpsStatusListener(statusListener);
    }
	
	public void appendText(String filepathString,String outputString) {
	    FileWriter writer;
		try {
		    File file=new File(filepathString);
			writer = new FileWriter(file,true);
			writer.write(outputString);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * ����״̬������
	 */
	
	private TextView tv_satellites;
	
	private List<GpsSatellite> numSatelliteList = new ArrayList<GpsSatellite>(); // �����ź�
	
	private final GpsStatus.Listener statusListener = new GpsStatus.Listener() {
		public void onGpsStatusChanged(int event) { // GPS״̬�仯ʱ�Ļص�����������
			LocationManager locationManager = (LocationManager) thisActivity.getSystemService(Context.LOCATION_SERVICE);
			GpsStatus status = locationManager.getGpsStatus(null); //ȡ��ǰ״̬
			updateGpsStatus(event, status);
		}
	};

	private String updateGpsStatus(int event, GpsStatus status) {
		StringBuilder sb2 = new StringBuilder("");
		if (status == null) {
			sb2.append("GPS Satellite Number:" +0);
		} else if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
			int maxSatellites = status.getMaxSatellites();
			Iterator<GpsSatellite> it = status.getSatellites().iterator();
			numSatelliteList.clear();
			int count = 0;
			String weixingString = "";
			while (it.hasNext() && count <= maxSatellites) {
				GpsSatellite s = it.next();
				if(s.getPrn()<=32&&s.getSnr()>0)
				{
				weixingString+=s.getPrn()+":"+s.getSnr()+"\n";
				numSatelliteList.add(s);
				}
				count++;
			}
			sb2.append("GPS Satellite Number:" + numSatelliteList.size());
			weixingString+="GPS Satellite Number:" + numSatelliteList.size();
			System.out.println(weixingString);
			tv_satellites.setText(weixingString);
		}
		return sb2.toString();
	}
	
	private void openGPSSetting() {  
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);  
        if(locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {  
        	return;
        }
        Toast.makeText(this, "�뿪��GPS��", Toast.LENGTH_SHORT).show();
        
        // ��ת��GPS������ҳ��  
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);  
        startActivityForResult(intent, 0); // ��Ϊ������ɺ󷵻ص���ȡ����  
    }
	
	
	public void openthefile(View v)
	{
		Uri uri = Uri.parse("file://"+filepath);  
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(uri, "text/html");
		startActivity(intent);
	}
	
	public void clear(View v)
	{
		File file = new File(filepath);
		try {
			new FileWriter(file).write(System.currentTimeMillis()+ ":System start." +"\r\n"); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		show("�����֮ǰ�����ݡ�");
	}
	@Override
	public void onResume()
	{
		super.onResume();
		try{
		SharedPreferences sp = getSharedPreferences("data", 0);
		String accString=sp.getString("acc", "1000");
		String gpsString=sp.getString("gps", "3000");
		String gyroString=sp.getString("gyro", "1000");
		String magString=sp.getString("mag", "1000");
		String filenameString =sp.getString("filename", "output.txt");

		accdelay=Integer.valueOf(accString);
		gpsdelay=Integer.valueOf(gpsString);
		gyrodelay=Integer.valueOf(gyroString);
		magdelay=Integer.valueOf(magString);
		filepath = Environment.getExternalStorageDirectory()+"/Acceleration/"+filenameString;
		
		TextView settingTextView = (TextView)findViewById(R.id.textview_settings);
		settingTextView.setText("���ٶ�:"+accString+"\r\nGPS:"+gpsString
				+"\r\n������:"+gyroString+"\r\n�ų�:"+magString+"\r\n�ļ���ַ:"+filepath);
		
		}catch(Exception e)
		{}
		
		/**
		 * ����Ķ��ǻ�ȡ������Ϣ,filepath���Ǳ�����ļ�·��,Ȼ����ʱ���ݶ���Ĭ��ֵ1000/3000��
		 */
	}
	
	@Override
	public void onDestroy()
	{
		FileWriter writer;
		try {
			writer = new FileWriter(new File(filepath),true);
			writer.write(System.currentTimeMillis()+ ":System stop." +"\r\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
		super.onDestroy();
	}
	public void quit(View v)
	{
		onDestroy();
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.acceleration, menu);
        return true;
    }
    Activity thisActivity = this;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	try{
    	if(item.getItemId()==R.id.about)
        {
    		Dialog alertDialog = new AlertDialog.Builder(this). 
                    setTitle("������"). 
                    setMessage("������\tQQ:0x369181DA"). 
                    setIcon(R.drawable.ic_launcher).
                    setPositiveButton("ȷ��", new DialogInterface.OnClickListener() { 
                        @Override 
                        public void onClick(DialogInterface dialog, int which) { 
                            // TODO Auto-generated method stub  
                        } 
                    }). 
                    create(); 
            alertDialog.show(); 
    	}else if(item.getItemId()==R.id.settings)
    	{
    		Intent intent = new Intent();
    		intent.setClass(thisActivity, settings.class);
    		startActivity(intent);
    	}
    	}catch(Exception ex)
    	{ 
    		show(ex.toString());
    	}
		return false;
    }

    public void show(String str){
    	//==================================================================
    	//��������show
    	//���ߣ�ypw
    	//���ܣ�����һ��toast,��ʾ��ʾ������
    	//���������String str
    	//����ֵ��void
    	//==================================================================
    	if(Thread.currentThread().getId()!=uiId)
    	{
    	Looper.prepare();
    	Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    	Looper.loop();
        //�����߳�����ʾToast����д����Ų�ǿ��
    	}else Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }
}




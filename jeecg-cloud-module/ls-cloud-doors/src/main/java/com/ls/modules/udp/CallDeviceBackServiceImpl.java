/*
package com.ls.modules.udp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.Charset;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ls.sdp.config.Config;
import com.ls.sdp.service.wedsocket.WebsocketService;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CallDeviceBackServiceImpl implements CallDeviceBackService {
	@Value("${sdp2000.udpport:6789}")
	private int udpport;
	@Value("${sdp2000.udpmac:255.255.255.255}")
	private String udpmac;
	@Autowired
	private WebsocketService websocketService;
	@Override
	public JSONObject logincapabilities(String url,JSONObject data) {
		log.info("\r\n data:{}",data);
		JSONObject result = httpData(url, data);
		return result;
	}

	@Override
	public JSONObject login(String url,JSONObject data) {
		JSONObject result = httpData(url, data);
		return result;
	}

	@Override
	public JSONObject logout(String url,JSONObject data) {
		JSONObject result = httpData(url, data);
		return result;
	}

	@Override
	public JSONObject heartbeat(String url,JSONObject data) {
		JSONObject result = httpData(url, data);
		return result;
	}

	@Override
	public JSONObject priviewencode(String url,JSONObject data) {
		JSONObject result = httpData(url, data);
		return result;
	}

	@Override
	public JSONObject talkencodeinfo(String url,JSONObject data) {
		JSONObject result = httpData(url, data);
		return result;
	}

	@Override
	public JSONObject recordencodeinfo(String url,JSONObject data) {
		JSONObject result = httpData(url, data);
		return result;
	}

	@Override
	public JSONObject channellist(String url,JSONObject data) {
		JSONObject result = httpData(url, data);
		return result;
	}
	@Override
	public JSONObject httpData(String url,JSONObject data){
		String result = null;
		HttpPost httpPost = new HttpPost(url);
		CloseableHttpClient httpClient  = null;
		try {
			RequestConfig defaultRequestConfig = RequestConfig.custom()
					.setSocketTimeout(5000)
					.setConnectTimeout(1000)
					.setConnectionRequestTimeout(5000)
//					.setStaleConnectionCheckEnabled(true)
					.build();
			httpClient =  HttpClients.custom()
					.setDefaultRequestConfig(defaultRequestConfig)
					.build();
			BasicResponseHandler handler = new BasicResponseHandler();
			String cookie = data.getJSONObject("data").getString("cookie");
			StringEntity entity = new StringEntity(data.toJSONString(), "utf-8");//解决中文乱码问题
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			httpPost.setEntity(entity);
			if(cookie != null) httpPost.setHeader("cookie","sessionID=" + cookie);
			System.out.print("请求：" + url + "\r\n参数：" + data + "\r\n");
			if(httpClient!=null)result = httpClient.execute(httpPost, handler);
			System.out.println("\r\n结果：" + result);
		} catch (Exception e) {
			data.put("code", -1);
			data.put("message", e.getMessage());
			System.out.println("\r\n请求异常[" + url + "]：" + e.getMessage());
		} finally {
			try {
				if(httpClient!=null) {
					httpClient.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result!=null?JSONObject.parseObject(result):data;
	}
	@Override
	public void ipStatus(JSONObject result) {
		JSONObject data = result.getJSONObject("data");
		if(data == null) return;
		JSONArray channelList = data.getJSONArray("channelList");
		if(channelList == null) return;
		String chioceChannel = result.getString("chioceChannel_xxxxx");
		int count = channelList.size();
		List<String> randomChannel = Lists.newArrayList();
		final CountDownLatch latch = new CountDownLatch(count);
		ExecutorService executorService = Executors.newFixedThreadPool(count);
		for (Object object : channelList) {
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					try {
						JSONObject dev = (JSONObject)object;
						boolean enable = dev.getBoolean("enable");
						String deviceIp = dev.getString("ip");
						boolean ipNotEmpty = StringUtils.isNotBlank(deviceIp);
						boolean status = InetAddress.getByName(deviceIp).isReachable(200);
						String channel = dev.getString("channel");
						System.out.println("IP:" + deviceIp + ";通道:" + channel + ";通讯能力:" + status);
						if(ipNotEmpty && status && chioceChannel == null) {
							data.put("selectChannel", channel);
						}else {
							if(ipNotEmpty == false || enable == false) channelList.remove(object);
							if(ipNotEmpty == true) randomChannel.add(channel);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						latch.countDown();
					}
				}
			});
		}
		try {
			latch.await();
			if(!data.containsKey("selectChannel")) {
				int index = RandomUtil.randomInt(0, randomChannel.size());
				data.put("selectChannel", StringUtils.isEmpty(chioceChannel)?randomChannel.get(index):Integer.parseInt(chioceChannel.toString()));
			}
			System.out.println("默认选择通道：" + data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			executorService.shutdown();
		}		
	}

	@Override
	public JSONObject searchDevice(String url, JSONObject data) {
		broadDevice(data);
		return data;
	}
	
	public JSONObject broadDevice(JSONObject data) {
		MulticastSocket socket = null;
		try {
			socket = new MulticastSocket(udpport);
            System.out.println("开始广播搜索设备.....");
            SearchDataModel searchDataModel = new SearchDataModel();
            byte[] buff = searchDataModel.toByteArray();
            int len = buff.length;
            DatagramPacket packet = new DatagramPacket(buff, len,InetAddress.getByName(udpmac), udpport);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        	if(socket != null)socket.close();
		}
		return data;
	}
	
	@Override
	public void listennerDevice() {
				System.out.println("开始接受广播消息....");
		        MulticastSocket receSocket = null;
		        try {
		        	receSocket = new MulticastSocket(6789);
		            InetAddress group = InetAddress.getByName("228.5.6.7");
		            receSocket.joinGroup(group);
		            while (true) {
		            	byte[] buff = new byte[1024*6];
		                DatagramPacket packet = new DatagramPacket(buff, buff.length);
		                receSocket.receive(packet);
//		                String ip = packet.getAddress().getHostAddress();
//		                boolean status = InetAddress.getByName(ip).isReachable(200);
		                Config.DeviceVistThreadPoolExecutor.execute(new Runnable() {
							@Override
							public void run() {
								byte [] datas = packet.getData();
								String data = new String(datas);
								String sign [] = data.split("\r\n");
//								if(sign.length>1)continue;
								int i = 0;
								int index = 0;
								TreeMap<String,byte[]> deviceInfoList = new TreeMap<String, byte[]>();
								deviceInfoList.put("00", new byte[24]);
								deviceInfoList.put("01", new byte[16]);
								deviceInfoList.put("02", new byte[16]);
								deviceInfoList.put("03", new byte[16]);
								deviceInfoList.put("04", new byte[16]);
								deviceInfoList.put("05", new byte[33]);
								deviceInfoList.put("06", new byte[23]);
								deviceInfoList.put("07", new byte[16]);
								deviceInfoList.put("08", new byte[144]);
								deviceInfoList.put("09", new byte[16]);
								deviceInfoList.put("10", new byte[128]);
								deviceInfoList.put("11", new byte[32]);
								deviceInfoList.put("12", new byte[32]);
								deviceInfoList.put("13", new byte[73]);
								deviceInfoList.put("14", new byte[32]);
								deviceInfoList.put("15", new byte[32]);
								deviceInfoList.put("16", new byte[64]);
								for (byte bytedata : datas) {
									byte [] objByte_00 = deviceInfoList.get("00");
									int start_00 = 0;
									int end_00 = objByte_00.length;
									if(i >= start_00 && i < end_00) {
										if(i==0) index = 0;
										objByte_00[index] = bytedata;
										index++;
									}
									byte [] objByte_01 = deviceInfoList.get("01");
									int start_01 = end_00;
									int end_01 = start_01 + objByte_01.length;
									if(i >= start_01 && i < end_01) {
										if(i==start_01) index = 0;
										objByte_01[index] = bytedata;
										index++;
									}
									byte [] objByte_02 = deviceInfoList.get("02");
									int start_02 = end_01;
									int end_02 = start_02 + objByte_02.length;
									if(i >= start_02 && i < end_02) {
										if(i==start_02) index = 0;
										objByte_02[index] = bytedata;
										index++;
									}
									byte [] objByte_03 = deviceInfoList.get("03");
									int start_03 = end_02;
									int end_03 = start_03 + objByte_03.length;
									if(i >= start_03 && i < end_03) {
										if(i==start_03) index = 0;
										objByte_03[index] = bytedata;
										index++;
									}
									byte [] objByte_04 = deviceInfoList.get("04");
									int start_04 = end_03;
									int end_04 = start_04 + objByte_04.length;
									if(i >= start_04 && i < end_04) {
										if(i==start_04) index = 0;
										objByte_04[index] = bytedata;
										index++;
									}
									byte [] objByte_05 = deviceInfoList.get("05");
									int start_05 = end_04;
									int end_05 = start_05 + objByte_05.length;
									if(i >= start_05 && i < end_05) {
										if(i==start_05) index = 0;
										objByte_05[index] = bytedata;
										index++;
									}
									byte [] objByte_06 = deviceInfoList.get("06");
									int start_06 = end_05;
									int end_06 = start_06 + objByte_06.length;
									if(i >= start_06 && i < end_06) {
										if(i==start_06) index = 0;
										objByte_06[index] = bytedata;
										index++;
									}
									byte [] objByte_07 = deviceInfoList.get("07");
									int start_07 = end_06;
									int end_07 = start_07 + objByte_07.length;
									if(i >= start_07 && i < end_07) {
										if(i==start_07) index = 0;
										objByte_07[index] = bytedata;
										index++;
									}
									byte [] objByte_08 = deviceInfoList.get("08");
									int start_08 = end_07;
									int end_08 = start_08 + objByte_08.length;
									if(i >= start_08 && i < end_08) {
										if(i==start_08) index = 0;
										objByte_08[index] = bytedata;
										index++;
									}
									byte [] objByte_09 = deviceInfoList.get("09");
									int start_09 = end_08;
									int end_09 = start_09 + objByte_09.length;
									if(i >= start_09 && i < end_09) {
										if(i==start_09) index = 0;
										objByte_09[index] = bytedata;
										index++;
									}
									byte [] objByte_10 = deviceInfoList.get("10");
									int start_10 = end_09;
									int end_10 = start_10 + objByte_10.length;
									if(i >= start_10 && i < end_10) {
										if(i==start_10) index = 0;
										objByte_10[index] = bytedata;
										index++;
									}
									byte [] objByte_11 = deviceInfoList.get("11");
									int start_11 = end_10;
									int end_11 = start_11 + objByte_11.length;
									if(i >= start_11 && i < end_11) {
										if(i==start_11) index = 0;
										objByte_11[index] = bytedata;
										index++;
									}
									byte [] objByte_12 = deviceInfoList.get("12");
									int start_12 = end_11;
									int end_12 = start_12 + objByte_12.length;
									if(i >= start_12 && i < end_12) {
										if(i==start_12) index = 0;
										objByte_12[index] = bytedata;
										index++;
									}
									byte [] objByte_13 = deviceInfoList.get("13");
									int start_13 = end_12;
									int end_13 = start_13 + objByte_13.length;
									if(i >= start_13 && i < end_13) {
										if(i==start_13) index = 0;
										objByte_13[index] = bytedata;
										index++;
									}
									
									byte [] objByte_14 = deviceInfoList.get("14");
									int start_14 = end_13;
									int end_14 = start_14 + objByte_14.length;
									if(i >= start_14 && i < end_14) {
										if(i==start_14) index = 0;
										objByte_14[index] = bytedata;
										index++;
									}
									byte [] objByte_15 = deviceInfoList.get("15");
									int start_15 = end_14;
									int end_15 = start_15 + objByte_15.length;
									if(i >= start_15 && i < end_15) {
										if(i==start_15) index = 0;
										objByte_15[index] = bytedata;
										index++;
									}
									byte [] objByte_16 = deviceInfoList.get("16");
									int start_16 = end_15;
									int end_16 = start_16 + objByte_16.length;
									if(i >= start_16 && i < end_16) {
										if(i==start_16) index = 0;
										objByte_16[index] = bytedata;
										index++;
									}
									i++;
								}
								int port = packet.getPort();
								String ip = packet.getAddress().getHostAddress();
								boolean status = false;
								try {
									status = InetAddress.getByName(ip).isReachable(200);
								} catch (Exception e) {
									e.printStackTrace();
								}
								if(status && data.contains("IPC") && sign.length < 2) {
									JSONObject device = new JSONObject();
									String charType = "UTF-8";//ISO-8859-1,GB18030,GB2312,GBK,xxxxx,xxxxx,xxxxx,xxxxx,
									deviceInfoList.forEach((key,value)->{
										try {
											device.put(key, new String(value).trim());
											String str = new String(value,0,value.length,Charset.forName(charType)).trim();//ISO-8859-1,GB18030
											System.out.println(key + "：" + str);
										} catch (Exception e) {
											e.printStackTrace();
										}
									});
									System.out.println(ip + ":" + port + " 说:" + new String(datas));
									websocketService.sendSync("receDevice", device);
								}
							}
						});
		            }
		        } catch (Exception e) {
		            throw new RuntimeException("接受端失败：" + e.getMessage());
		        }
	}
	
	
	
	public static void listennerDevicexx() {
		System.out.println("开始接受广播消息....");
        MulticastSocket receSocket = null;
        try {
        	receSocket = new MulticastSocket(6789);
            InetAddress group = InetAddress.getByName("228.5.6.7");
            receSocket.joinGroup(group);
            while (true) {
            	byte[] buff = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buff, buff.length);
                receSocket.receive(packet);
//                String ip = packet.getAddress().getHostAddress();
//                boolean status = InetAddress.getByName(ip).isReachable(200);
//                Config.DeviceVistThreadPoolExecutor.execute(new Runnable() {
//					@Override
//					public void run() {
						byte [] datas = packet.getData();
						String data = new String(datas);
						String sign [] = data.split("\r\n");
//						if(sign.length>1)continue;
						int i = 0;
						int index = 0;
						TreeMap<String,byte[]> deviceInfoList = new TreeMap<String, byte[]>();
						deviceInfoList.put("00", new byte[24]);
						deviceInfoList.put("01", new byte[16]);
						deviceInfoList.put("02", new byte[16]);
						deviceInfoList.put("03", new byte[16]);
						deviceInfoList.put("04", new byte[16]);
						deviceInfoList.put("05", new byte[33]);
						deviceInfoList.put("06", new byte[23]);
						deviceInfoList.put("07", new byte[16]);
						deviceInfoList.put("08", new byte[144]);
						deviceInfoList.put("09", new byte[16]);
						deviceInfoList.put("10", new byte[128]);
						deviceInfoList.put("11", new byte[32]);
						deviceInfoList.put("12", new byte[32]);
						deviceInfoList.put("13", new byte[73]);
						deviceInfoList.put("14", new byte[32]);
						deviceInfoList.put("15", new byte[32]);
						deviceInfoList.put("16", new byte[64]);
						for (byte bytedata : datas) {
							byte [] objByte_00 = deviceInfoList.get("00");
							int start_00 = 0;
							int end_00 = objByte_00.length;
							if(i >= start_00 && i < end_00) {
								if(i==0) index = 0;
								objByte_00[index] = bytedata;
								index++;
							}
							byte [] objByte_01 = deviceInfoList.get("01");
							int start_01 = end_00;
							int end_01 = start_01 + objByte_01.length;
							if(i >= start_01 && i < end_01) {
								if(i==start_01) index = 0;
								objByte_01[index] = bytedata;
								index++;
							}
							byte [] objByte_02 = deviceInfoList.get("02");
							int start_02 = end_01;
							int end_02 = start_02 + objByte_02.length;
							if(i >= start_02 && i < end_02) {
								if(i==start_02) index = 0;
								objByte_02[index] = bytedata;
								index++;
							}
							byte [] objByte_03 = deviceInfoList.get("03");
							int start_03 = end_02;
							int end_03 = start_03 + objByte_03.length;
							if(i >= start_03 && i < end_03) {
								if(i==start_03) index = 0;
								objByte_03[index] = bytedata;
								index++;
							}
							byte [] objByte_04 = deviceInfoList.get("04");
							int start_04 = end_03;
							int end_04 = start_04 + objByte_04.length;
							if(i >= start_04 && i < end_04) {
								if(i==start_04) index = 0;
								objByte_04[index] = bytedata;
								index++;
							}
							byte [] objByte_05 = deviceInfoList.get("05");
							int start_05 = end_04;
							int end_05 = start_05 + objByte_05.length;
							if(i >= start_05 && i < end_05) {
								if(i==start_05) index = 0;
								objByte_05[index] = bytedata;
								index++;
							}
							byte [] objByte_06 = deviceInfoList.get("06");
							int start_06 = end_05;
							int end_06 = start_06 + objByte_06.length;
							if(i >= start_06 && i < end_06) {
								if(i==start_06) index = 0;
								objByte_06[index] = bytedata;
								index++;
							}
							byte [] objByte_07 = deviceInfoList.get("07");
							int start_07 = end_06;
							int end_07 = start_07 + objByte_07.length;
							if(i >= start_07 && i < end_07) {
								if(i==start_07) index = 0;
								objByte_07[index] = bytedata;
								index++;
							}
							byte [] objByte_08 = deviceInfoList.get("08");
							int start_08 = end_07;
							int end_08 = start_08 + objByte_08.length;
							if(i >= start_08 && i < end_08) {
								if(i==start_08) index = 0;
								objByte_08[index] = bytedata;
								index++;
							}
							byte [] objByte_09 = deviceInfoList.get("09");
							int start_09 = end_08;
							int end_09 = start_09 + objByte_09.length;
							if(i >= start_09 && i < end_09) {
								if(i==start_09) index = 0;
								objByte_09[index] = bytedata;
								index++;
							}
							byte [] objByte_10 = deviceInfoList.get("10");
							int start_10 = end_09;
							int end_10 = start_10 + objByte_10.length;
							if(i >= start_10 && i < end_10) {
								if(i==start_10) index = 0;
								objByte_10[index] = bytedata;
								index++;
							}
							byte [] objByte_11 = deviceInfoList.get("11");
							int start_11 = end_10;
							int end_11 = start_11 + objByte_11.length;
							if(i >= start_11 && i < end_11) {
								if(i==start_11) index = 0;
								objByte_11[index] = bytedata;
								index++;
							}
							byte [] objByte_12 = deviceInfoList.get("12");
							int start_12 = end_11;
							int end_12 = start_12 + objByte_12.length;
							if(i >= start_12 && i < end_12) {
								if(i==start_12) index = 0;
								objByte_12[index] = bytedata;
								index++;
							}
							byte [] objByte_13 = deviceInfoList.get("13");
							int start_13 = end_12;
							int end_13 = start_13 + objByte_13.length;
							if(i >= start_13 && i < end_13) {
								if(i==start_13) index = 0;
								objByte_13[index] = bytedata;
								index++;
							}
							
							byte [] objByte_14 = deviceInfoList.get("14");
							int start_14 = end_13;
							int end_14 = start_14 + objByte_14.length;
							if(i >= start_14 && i < end_14) {
								if(i==start_14) index = 0;
								objByte_14[index] = bytedata;
								index++;
							}
							byte [] objByte_15 = deviceInfoList.get("15");
							int start_15 = end_14;
							int end_15 = start_15 + objByte_15.length;
							if(i >= start_15 && i < end_15) {
								if(i==start_15) index = 0;
								objByte_15[index] = bytedata;
								index++;
							}
							byte [] objByte_16 = deviceInfoList.get("16");
							int start_16 = end_15;
							int end_16 = start_16 + objByte_16.length;
							if(i >= start_16 && i < end_16) {
								if(i==start_16) index = 0;
								objByte_16[index] = bytedata;
								index++;
							}
							i++;
						}
						int port = packet.getPort();
						String ip = packet.getAddress().getHostAddress();
						boolean status = false;
						try {
							status = InetAddress.getByName(ip).isReachable(200);
							status = true;
						} catch (Exception e) {
							e.printStackTrace();
						}
						if(status && data.contains("IPC") && sign.length < 2) {
							System.out.println("================================================================================");
							JSONObject device = new JSONObject();
							String charType = "UTF-8";//ISO-8859-1,GB18030,GB2312,GBK,UTF-8,xxxxx,xxxxx,xxxxx,
							deviceInfoList.forEach((key,value)->{
								try {
									device.put(key, new String(value).trim());
									String str = new String(value,0,value.length,Charset.forName(charType)).trim();//ISO-8859-1,GB18030
//									System.out.println(key + "：" + str + JSONObject.toJSON(value));
									System.out.println(key + "：" + str);
								} catch (Exception e) {
									e.printStackTrace();
								}
							});
							System.out.println(ip + ":" + port + " 说:" + new String(datas, 0, datas.length, Charset.forName(charType)));
//							websocketService.sendSync("receDevice", device);
						}
//					}
//				});
            }
        } catch (Exception e) {
            throw new RuntimeException("接受端失败：" + e.getMessage());
        } finally{
        	if(receSocket!=null)receSocket.close();
        }
	}
	public static void main(String[] args) {
		try {
			System.out.println(JSONObject.toJSON("中国".getBytes("GBK")) + ":" + new String("中国".getBytes("GBK"),"GBK"));//8862,8962
			System.out.println(new String(new byte[] {80, 2, 42,80,44, -12}));
			System.out.println(new String(new byte[] {34, -98,35, 2,34, -98,44, -12, 1}));//8862,8962
			System.out.println(JSONObject.toJSON("8862".getBytes()) + ":" + new String("8862".getBytes(),"UTF-8"));//8862,8962
		} catch (Exception e) {
			e.printStackTrace();
		}
		listennerDevicexx();
	}
}
*/

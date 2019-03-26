package com.zulong.channels;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.zulong.channels.ProcessClearStream.ProcessClearCallback;

public class ChannelsMain {
	// 当前项目的路径
	private static final String currentPath = System.getProperty("user.dir");

	/**
	 * @读取配置文件
	 * @param fileName
	 */
	public static String readFileByLines(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			return "None";
		}
		BufferedReader reader = null;
		String tempString = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			StringBuffer strBuffer = new StringBuffer();
			while ((tempString = reader.readLine()) != null) {
				strBuffer.append(tempString);
			}
			reader.close();
			tempString = strBuffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		}
		return tempString;
	}

	/**
	 * @解析配置文件中的json数据
	 * @param json
	 * @return
	 */
	public static Vector<Channels> resolveJson(String json) {
		Vector<Channels> vector = new Vector<>();
		JSONArray channels = new JSONObject(json).getJSONArray("channels");
		Iterator<Object> iterator = channels.iterator();
		while (iterator.hasNext()) {
			JSONObject channel = (JSONObject) iterator.next();
			Channels vectorItem = new Channels();
			vectorItem.setName(channel.getString("channelName"));
			vectorItem.setFileName(channel.getString("projectFileName"));
			vectorItem.setPackageName(channel.getString("projectPackageName"));
			vector.addElement(vectorItem);
		}
		return vector;
	}

	/**
	 * @修改build.xml文件
	 * @param filePath
	 * @param fileName
	 * @param packageName
	 */
	public static void changeBuildXml(String filePath, String fileName, String packageName) {
		DocumentBuilder dBuilder;
		try {
			dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dBuilder.parse(new File(filePath));
			doc.getDocumentElement().normalize();

			NodeList properties = doc.getElementsByTagName("property");
			for (int i = 0; i < properties.getLength(); ++i) {
				if (i == 0) {
					((Element) properties.item(i)).setAttribute("value", fileName);
				} else {
					((Element) properties.item(i)).setAttribute("value", packageName);
				}
			}
			doc.getDocumentElement().normalize();
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(new DOMSource(doc), new StreamResult(new File(filePath)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		JFrame window = new JFrame("选择渠道"); // 初始化窗口
		window.setSize(200, 200); // 设置窗口大小
		window.setIconImage(new ImageIcon(currentPath + "\\imgs\\channel_img_zulong.png").getImage()); // 设置窗口的icon
		window.setLocationRelativeTo(null); // 将窗口设置到桌面中间
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // 设置右上角的叉的退出事件
		JPanel container = new JPanel(new GridLayout(2, 1, 0, 0)); // 将窗口分为2行1列0水平间隔0垂直间隔的网格布局

		String json = readFileByLines(currentPath + "\\config\\channelsProperties.json");
		if (json.equals("None") || json.equals("") || json==null) {
			JOptionPane.showMessageDialog(window, "配置文件不存在或配置文件中无数据", "错误提示", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JPanel content = new JPanel(null); // 创建选择渠道的区域并将其设置为绝对布局
			JLabel channelLabel = new JLabel("当前选择的渠道："); // 创建标签并设置标签的内容
			channelLabel.setBounds(5, 40, 105, 20); // 设置相对于父容器的左边距、上边距以及自身的宽、高
			content.add(channelLabel); // 将标签设置到选择渠道的区域中
			Vector<Channels> vector = resolveJson(json); // 读取配置文件以及解析配置文件中的json数据
			JComboBox<Channels> channelBtn = new JComboBox<>(vector); // 创建下拉框并将解析的配置设置到下拉框中
			channelBtn.setBounds(110, 40, 70, 20);
			content.add(channelBtn); // 将下拉框设置到选择渠道的区域中
			container.add(content); // 将选择渠道的区域设置到窗口的网格布局中

			JPanel isTrueBtn = new JPanel(null); // 创建按钮区域
			JButton cancelBtn = new JButton("取消"); // 创建取消按钮
			cancelBtn.setBounds(25, 40, 60, 20);
			cancelBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					window.dispose();
				}
			}); // 设置取消按钮的点击事件
			JButton confirmBtn = new JButton("确认"); // 创建确认按钮
			confirmBtn.setBounds(110, 40, 60, 20);
			ProcessClearCallback callback = new ProcessClearCallback() {
				
				@Override
				public void onCallback(String message) {
					JOptionPane.showMessageDialog(window, message, "执行结果提示", JOptionPane.INFORMATION_MESSAGE);
				}
			};	//执行结果的回调函数
			confirmBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Channels channel = vector.get(channelBtn.getSelectedIndex());	//获取当前选择的渠道
					changeBuildXml("../sdkdemo_test/build.xml", channel.getFileName(), channel.getPackageName());	//修改build.xml文件
					String buildXmlPath = currentPath.substring(0, currentPath.lastIndexOf("\\"))
							+ "\\sdkdemo_test\\build.xml";	//build.xml文件的路径
					try {
						Process process = Runtime.getRuntime().exec("cmd /c ant -f " + buildXmlPath);	//dos命令执行ant脚本
						new ProcessClearStream(process.getInputStream(), ProcessClearStream.INFO_TYPE, callback).start();	//清空输出缓存区
						new ProcessClearStream(process.getErrorStream(), ProcessClearStream.ERROR_TYPE, callback).start();	//清空错误缓存区
						process.waitFor();	//等待子进程完成之后主进程再执行
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
			}); // 设置确认按钮的点击事件

			isTrueBtn.add(cancelBtn); // 将取消按钮设置到按钮区域
			isTrueBtn.add(confirmBtn); // 将确认按钮设置到按钮区域
			container.add(isTrueBtn); // 将按钮区域设置到窗口的网格布局中
		}

		window.setContentPane(container); // 将容器设置到窗口中
		window.setVisible(true); // 显示窗口
	}

}

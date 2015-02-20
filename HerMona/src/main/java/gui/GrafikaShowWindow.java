package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import util.DBUtil;
import util.ImageImplement;

public class GrafikaShowWindow extends JFrame {
	private static final long serialVersionUID = 6071188979880909464L;
	
	private ImageImplement imgPanel;
	BufferedImage originalImage = null;
	private JScrollPane scrollPane;
	
	public GrafikaShowWindow(String imgPath) {
		super("Podgl¹d grafiki");
		setResizable(true);
		setSize(1000, 700);
		
		try {
			originalImage = ImageIO.read(new File(imgPath));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Nie mo¿na odczytaæ pliku: " + imgPath, "B£¥D", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		JMenuBar menubar = new JMenuBar();
		
		JMenu resizeMenu = new JMenu("Zmieñ rozmiar");
		menubar.add(resizeMenu);
		JMenuItem size025 = new JMenuItem("25%");
		resizeMenu.add(size025);
		size025.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resizeImg(0.25);	
			}
		});
		JMenuItem size050 = new JMenuItem("50%");
		resizeMenu.add(size050);
		size050.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resizeImg(0.50);
			}
		});
		JMenuItem size100 = new JMenuItem("100%");
		resizeMenu.add(size100);
		size100.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resizeImg(1.00);
			}
		});
		setJMenuBar(menubar);
		
		imgPanel = new ImageImplement(originalImage);
		scrollPane = new JScrollPane (imgPanel, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scrollPane);
		setVisible(true);
	}
	
	private void resizeImg(double scale) {
		int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
		int imgSize = (int)((originalImage.getHeight() > originalImage.getWidth()) ? originalImage.getHeight() * scale : originalImage.getWidth() * scale);
		BufferedImage resizedImg = DBUtil.resizeImage(originalImage, type, imgSize);
		imgPanel = new ImageImplement(resizedImg);
		this.remove(scrollPane);
		scrollPane = new JScrollPane (imgPanel, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.add(scrollPane);
		this.revalidate();
		this.repaint();
	}

}

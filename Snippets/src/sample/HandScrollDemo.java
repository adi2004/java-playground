package sample;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;

public class HandScrollDemo {
	static class HandScrollListener extends MouseAdapter {
		private final Point pp = new Point();

		@Override
		public void mouseDragged(MouseEvent e) {
			JViewport vport = (JViewport) e.getSource();
			JComponent label = (JComponent) vport.getView();
			Point cp = e.getPoint();
			Point vp = vport.getViewPosition();
			vp.translate(pp.x - cp.x, pp.y - cp.y);
			label.scrollRectToVisible(new Rectangle(vp, vport.getSize()));
			//vport.setViewPosition(vp);
			pp.setLocation(cp);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			pp.setLocation(e.getPoint());
		}
	}

	public JComponent makeUI() {
		JLabel label = new JLabel(new Icon() {
			TexturePaint TEXTURE = makeCheckerTexture();

			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setPaint(TEXTURE);
				g2.fillRect(x, y, c.getWidth(), c.getHeight());
				g2.dispose();
			}

			@Override
			public int getIconWidth() {
				return 2000;
			}

			@Override
			public int getIconHeight() {
				return 2000;
			}
		});
		label.setBorder(BorderFactory.createLineBorder(Color.RED, 20));
		JScrollPane scroll = new JScrollPane(label);
		JViewport vport = scroll.getViewport();
		MouseAdapter ma = new HandScrollListener();
		vport.addMouseMotionListener(ma);
		vport.addMouseListener(ma);
		return scroll;
	}

	private static TexturePaint makeCheckerTexture() {
		int cs = 20;
		int sz = cs * cs;
		BufferedImage img = new BufferedImage(sz, sz, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = img.createGraphics();
		g2.setPaint(Color.GRAY);
		for (int i = 0; i * cs < sz; i++) {
			for (int j = 0; j * cs < sz; j++) {
				if ((i + j) % 2 == 0) {
					g2.fillRect(i * cs, j * cs, cs, cs);
				}
			}
		}
		g2.dispose();
		return new TexturePaint(img, new Rectangle(0, 0, sz, sz));
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}

	public static void createAndShowGUI() {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.getContentPane().add(new HandScrollDemo().makeUI());
		f.setSize(320, 320);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
}
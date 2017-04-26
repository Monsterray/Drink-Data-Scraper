package applet;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class AppletListener implements ActionListener, MouseListener,
		MouseMotionListener, KeyListener, FocusListener, WindowListener,
		MouseWheelListener {
	
	private Gui instance;
	private BufferedImage tmp;

	public AppletListener(Gui inst) {
		instance = inst;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		System.out.println("Command: " + cmd);
	    int modifiers = e.getModifiers();
	    System.out.println("\tALT : "  + checkMod(modifiers, ActionEvent.ALT_MASK));	// Also triggered if middle clicked
	    System.out.println("\tCTRL : " + checkMod(modifiers, ActionEvent.CTRL_MASK));
	    System.out.println("\tMETA : " + checkMod(modifiers, ActionEvent.META_MASK));	// Also triggered if right clicked
	    System.out.println("\tSHIFT: " + checkMod(modifiers, ActionEvent.SHIFT_MASK));

	    switch(cmd){
	    
	    case("searchGo"):
	    	instance.drinkData.findSearchResults(instance.searchField.getText(), instance);
	    	instance.addAndDisplayResults();
	    	break;
	    
	    case("pause"):
	    	instance.readData();
	    	break;

		case "Load Test":
			instance.createFileWindow(0, "../", "Load testing");
			break;

		case "Save Screenshot":
		case "Screenshot":
			tmp = instance.getFrameImage(instance.mainFrame);
			instance.createFileWindow(1, "../", "Save Screenshot");
			break;

		case "ApproveSelection":
			File file = instance.fileChooser.getSelectedFile();
			if (instance.fileChooser.getDialogType() == 1) {
				if (file != null && file.isFile()) {
					int i = JOptionPane.showConfirmDialog(
							instance.mainFrame, file.getAbsolutePath() + " already exists.\n" + "Do you want to replace it?", 
							"Save Screenshot", 2);
					if (i != 0) {
						return;
					}
				}
				try {
					ImageIO.write(tmp, "png", file);
				} catch (IOException ioexception2) {
					JOptionPane.showMessageDialog(instance.mainFrame, 
							"An error occured while trying to save the screenshot!\n" + 
							"Please make sure you have\n" + 
							" write access to the screenshot directory.",
							"Screenshot Error", 0);
				}
			} else {
				try {
					Desktop.getDesktop().open(file);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
			instance.fileDialog.dispose();
			break;

		case "CancelSelection":
			instance.fileDialog.dispose();
			break;

	    case("Exit"):
	    	System.exit(0);
	    }
	}

	private boolean checkMod(int modifiers, int mask) {
	    return ((modifiers & mask) == mask);
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
